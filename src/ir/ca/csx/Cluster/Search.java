package ir.ca.csx.Cluster;

import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;

public class Search {
		
	public static String readFile(String path) throws FileNotFoundException
	{
		File file = new File(path);
		if(file.isFile())
		{
			String str = new Scanner(file).useDelimiter("\\Z").next().trim();
			return str;
		}
		else return "";
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String query = "";
		
		ArrayList<String> docs = new ArrayList<String>();
		
		while(true)
		{
			int i = 1;
			CiteSeerXCluster obClstr = new CiteSeerXCluster();
			
			System.out.println("Enter query: ");
			try {
				query = br.readLine().trim();
				
				if(query.equalsIgnoreCase("exit")) System.exit(0);
				else
				{
					CiteSeerXSearcher search = new CiteSeerXSearcher(query.trim());
					docs = search.searchIndex();
					
					Iterator<String> itr = docs.iterator();
					
					while(itr.hasNext())
					{
						String f = itr.next();
						String abs = readFile(f);
						
						String id = f.substring(f.lastIndexOf("\\")+1,f.lastIndexOf(".txt"));
//						System.out.println(id);
						
//						obClstr.insertSummeryToCluster(i++,abs,f);						
						obClstr.insertSummeryToCluster(id.trim(),abs,f);
					}
					obClstr.getClusters(query);
					
					System.out.println("Now enter docIds to get cluster (type done to exit): ");
					while(true)
					{						
						query = br.readLine().trim();
						if(query.equalsIgnoreCase("exit")) break;
						else 
						{
							String[] ids = query.split(" ");
							//ArrayList<String> idsCheck = (ArrayList<String>)Arrays.asList(ids);
							//ArrayList<String> idsCheck = new ArrayList<String>(Arrays.asList(ids))
							//List<String> idsCheck = Arrays.asList(ids); 
							
							ArrayList<String> idsCheck = new ArrayList<String>();
							Collections.addAll(idsCheck,ids); 
							obClstr.getClustersGivenDocID(idsCheck);							
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
