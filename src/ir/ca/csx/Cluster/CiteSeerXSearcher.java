package ir.ca.csx.Cluster;

import java.io.File;
import java.util.ArrayList;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class CiteSeerXSearcher {
	
	String query = "";
	File indexDir = new File("D:/IR-CRAWLED-DATA/index/");
	int hits = Integer.MAX_VALUE;
	
	ArrayList<String> docs = new ArrayList<String>();
	
	public CiteSeerXSearcher(String query) throws Exception
	{
		this.query = query;
		this.hits = 9000;
		this.indexDir = new File("D:/IR-CRAWLED-DATA/index/");
//		searchIndex(indexDir, query, hits);
	}	

	public ArrayList<String> searchIndex() throws Exception {
		
		docs.clear();
		Directory directory = FSDirectory.open(this.indexDir);

		IndexSearcher searcher = new IndexSearcher(directory);
		QueryParser parser = new QueryParser(Version.LUCENE_30, "contents", new SimpleAnalyzer());
		Query query = parser.parse(this.query);
		
		TopDocs topDocs = searcher.search(query, this.hits);
		
		ScoreDoc[] hits = topDocs.scoreDocs;
		for (int i = 0; i < hits.length; i++) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			docs.add(d.get("filename"));
//			System.out.println(d.get("filename"));
		}
		
		System.out.println("Found " + hits.length);
		
		return docs;
	}

	public void searchIndex(File indexDir, String queryStr, int maxHits) throws Exception {
		
		Directory directory = FSDirectory.open(indexDir);

		IndexSearcher searcher = new IndexSearcher(directory);
		QueryParser parser = new QueryParser(Version.LUCENE_30, "contents", new SimpleAnalyzer());
		Query query = parser.parse(queryStr);
		
		TopDocs topDocs = searcher.search(query, maxHits);
		
		ScoreDoc[] hits = topDocs.scoreDocs;
		for (int i = 0; i < hits.length; i++) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			System.out.println(d.get("filename"));
		}
		
		System.out.println("Found " + hits.length);
		
	}
	
//	public static void main(String[] args) throws Exception {
//	
//	File indexDir = new File("D:/IR-CRAWLED-DATA/index/");
//	String query = "network";
//	int hits = 100;
//	
//	CiteSeerXSearcher searcher = new CiteSeerXSearcher();
//	searcher.searchIndex(indexDir, query, hits);		
//}


}