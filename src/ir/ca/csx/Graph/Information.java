package ir.ca.csx.Graph;

/**
 * 
 * @author H. Roy
 *
 */

public class Information {

	// This should be changed if your data dir is different.
	static String dataDir = "D:/IR-CRAWLED-DATA/";
	
	//locations
	public static String pdfImageLocation = dataDir + "1.jpg";
	public static String cssLocation = dataDir + "css1.txt";
	public static String pdfFolderLocation = dataDir + "PDF/";
	public static String indexLocation = dataDir + "index/";
	public static String dataLocation = dataDir + "ABSTRACT/";
	
	public static String crawlStorageFolder = dataDir + "temp/";
	
	//database : ranking
	public static String qGetAllPapers = "select id, title from ir1.paper";
	public static String qGetCitedPaperIDs = "select distinct cited_paper_id from ir1.citation where paper_id = ";
	public static String qUpdateRank = "update ir1.paper set rank = ";
	
	//database: graph
	public static String qGetCitedPapers = "select distinct cited_paper_id from ir1.citation where paper_id ";
	public static String qGetParentPapers = "select distinct paper_id from ir1.citation where cited_paper_id ";
	public static String qGetRankedPapers = "select id,title,rank from ir1.paper where id ";
	
	//database delete
	public static String qDeletePapers = "delete from ir1.paper where id ";
	
	public static  String getCitationCount = "select count(*) as total from ir1.citation where paper_id ";
	public static  String getCitedCount = "select distinct paper_id as total from ir1.citation where paper_id ";
}
