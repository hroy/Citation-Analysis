package ir.ca.csx.Crawler;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import ir.ca.csx.Graph.Information;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Yasser Ganjisaffar <lastname at gmail dot com>
 */
public class CiteSeerXCrawler extends WebCrawler {

//	String pdfFolderLocation = "D:/IR_Project/PDF";
//	String abstractFolderLocation = "D:/IR_Project/ABSTRACT";
	
	String pdfFolderLocation = Information.pdfFolderLocation;
	String abstractFolderLocation = Information.dataLocation;

	private final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|bmp|gif|jpe?g"
					+ "|png|tiff?|mid|mp2|mp3|mp4"
					+ "|wav|avi|mov|mpeg|ram|m4v|pdf"
					+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	/**
	 * You should implement this function to specify whether the given url
	 * should be crawled or not (based on your crawling logic).
	 */
	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		// return !FILTERS.matcher(href).matches() &&
		// href.startsWith("http://www.ics.uci.edu/");

		boolean match = FILTERS.matcher(href).matches();

		// if (!match && href.contains("pdf")) {
		// String fileName = "";
		//
		// if (href.contains(".pdf"))
		// fileName = href.substring(href.lastIndexOf('/') + 1,
		// href.length());
		// else
		// fileName = href.substring(href.lastIndexOf('?') + 1,
		// href.length())
		// + ".pdf";
		//
		// System.out.println("url: " + href + " file: " + fileName);
		// File downloadFile = new File("D:\\IR Project\\PDF", fileName);
		//
		// try {
		// download(href, downloadFile);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// downloadFile.delete();
		// }
		//
		// }

		return !match;

	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(Page page) {
		int docid = page.getWebURL().getDocid();
		String url = page.getWebURL().getURL();
		String domain = page.getWebURL().getDomain();
		String path = page.getWebURL().getPath();
		String subDomain = page.getWebURL().getSubDomain();
		String parentUrl = page.getWebURL().getParentUrl();
		String anchor = page.getWebURL().getAnchor();

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			List<WebURL> links = htmlParseData.getOutgoingUrls();

			// System.out.println("Text length: " + text.length());
			// System.out.println("Html length: " + html.length());
			// System.out.println("Number of outgoing links: " + links.size());

			System.out.println("Paper Url: " + url);
			Document doc = Jsoup.parse(html);
			Element titleHeader = doc.getElementById("viewHeader");
			Elements header = titleHeader.getElementsByTag("h2");
			System.out.println("Paper Title: " + header.text());

			if (header.text() != null) {
				int paper_id;

				MySQLAccess mySQLDB = new MySQLAccess();
				paper_id = mySQLDB.insertPaper(header.text());

				// Writing abstract.
				Element paperAbstract = doc.getElementById("abstract");
				Elements paperAbstractParaGraph = paperAbstract
						.getElementsByTag("p");

				String textParagraph = "";
				for (Element paragraph : paperAbstractParaGraph) {
					textParagraph += paragraph.text() + "\n";
				}

				System.out.println("Paper Abstract: " + textParagraph);

				File absFile = new File(abstractFolderLocation, paper_id
						+ ".txt");
				write(textParagraph, absFile);

				// Get the citations

				System.out.println("Cited Paper Link");
				Element citationSection = doc.getElementById("citations");
				for (Element table : citationSection.select("table")) {
					for (Element row : table.select("tr")) {
						Elements tds = row.select("td").get(1)
								.getElementsByTag("a");

						for (Element citedPapers : tds) {
							String linkHref = citedPapers.attr("href");
							String linkText = citedPapers.text();

							System.out.println("href: " + linkHref + " text: "
									+ linkText);

							if (linkText != null) {
								int cited_paper_id = mySQLDB
										.insertPaper(linkText);
								mySQLDB.insertCitation(paper_id, cited_paper_id);

							}
						}

					}
				}

				// Download pdf
				System.out.println("Downloaded Link of the paper");
				Element downLoadSection = doc.getElementById("downloads");
				Elements downloadLinks = downLoadSection.getElementsByTag("a");

				String activeUrl = "";
				String fileName = "";
                boolean alreadyDownload = false; 
				
				
				for (Element downloadLink : downloadLinks) {

					if (alreadyDownload == true)
						break;
					
					activeUrl = "";
					fileName = "";

					String linkHref = downloadLink.attr("href");
					String linkText = downloadLink.text();

					if (linkHref.contains(".pdf"))
						fileName = linkHref.substring(
								linkHref.lastIndexOf('/') + 1,
								linkHref.length());
					else
						fileName = linkHref.substring(
								linkHref.lastIndexOf('?') + 1,
								linkHref.length())
								+ ".pdf";

					// System.out.println("url: " + href + " file: " +
					// fileName);
					File downloadFile = new File(pdfFolderLocation, paper_id
							+ ".pdf");
					if (download(linkHref, downloadFile)) {

						if (downloadFile.canRead() && downloadFile.length() > 0) {
							activeUrl = downloadFile.getAbsolutePath();
							alreadyDownload = true;
						} else
						{
							downloadFile.delete();
							alreadyDownload = false;
						}
					}

				}

				mySQLDB.close();

			}//
		}

		Header[] responseHeaders = page.getFetchResponseHeaders();
		if (responseHeaders != null) {
			// System.out.println("Response headers:");
			for (Header header : responseHeaders) {
				// System.out.println("\t" + header.getName() + ": " +
				// header.getValue());
			}
		}

		// System.out.println("=============");
	}

	void write(String content, File file) {

		try {
			FileWriter fw = new FileWriter(file);
			fw.write(content);
			fw.close();

		} catch (IOException iox) {
			// do stuff with exception
			iox.printStackTrace();
		}
	}

	public static boolean download(final String url, final File destination) {

		if (destination.exists())
			destination.delete();

		URLConnection connection;
		try {
			connection = new URL(url).openConnection();
			connection.setConnectTimeout(60000);
			connection.setReadTimeout(60000);
			connection.addRequestProperty("User-Agent", "Mozilla/5.0");
			final FileOutputStream output = new FileOutputStream(destination,
					false);
			final byte[] buffer = new byte[2048];
			int read;
			final InputStream input = connection.getInputStream();
			while ((read = input.read(buffer)) > -1)
				output.write(buffer, 0, read);
			output.flush();
			output.close();
			input.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return false;
		}

		return true;

	}
}