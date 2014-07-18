package ir.ca.csx.Ranking;

import ir.ca.csx.Cluster.DBUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author H. Roy
 *
 */

public class RemoveRedundance {

	static List<String> IDList = new ArrayList<String>();
	
	public void loadIDs(String path)
	{
		File dr = new File(path);        
        File[] files = dr.listFiles();
        
        for(File file: files)
        {
        	String name = file.getName();
        	if(file.canRead() && file.length()>0)
        	{
        		System.out.println(name.substring(0, name.indexOf(".")));
            	IDList.add(name.substring(0, name.indexOf(".")));
        	}        	
        }
        
        System.out.println("Size: " + IDList.size());
	}
	
	public void deleteRedundance(List<String> ids)
	{
		DBUtility obDB = new DBUtility();
		obDB.deleteRedundance(ids);
		System.out.println("Deleted!");
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RemoveRedundance obRR = new RemoveRedundance();
		obRR.loadIDs("D:/IR-CRAWLED-DATA/PDF/");
		obRR.deleteRedundance(IDList);
	}
}

/*
 * mysql> use ir1
Database changed
mysql> delete from ir1.citation where citation.paper_id not in (select paper.id
from ir1.paper);
Query OK, 53738 rows affected (1.49 sec)

mysql> delete from ir1.citation where citation.cited_paper_id not in (select pap
er.id from ir1.paper);
Query OK, 215955 rows affected (2.66 sec)

mysql>

 * */
