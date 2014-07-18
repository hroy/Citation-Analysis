package ir.ca.csx.Cluster;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.carrot2.clustering.kmeans.BisectingKMeansClusteringAlgorithm;
import org.carrot2.clustering.lingo.LingoClusteringAlgorithm;
import org.carrot2.clustering.synthetic.ByUrlClusteringAlgorithm;
import org.carrot2.core.Cluster;
import org.carrot2.core.Controller;
import org.carrot2.core.ControllerFactory;
import org.carrot2.core.Document;
import org.carrot2.core.IDocumentSource;
import org.carrot2.core.ProcessingResult;
import org.carrot2.core.attribute.CommonAttributesDescriptor;

import edu.uci.ics.jung.algorithms.util.KMeansClusterer;


public class CiteSeerXCluster
{
	final ArrayList<Document> documents = new ArrayList<Document>();
	static HashMap<String,ArrayList> clusterDocs = new HashMap<String, ArrayList>();
	
	public CiteSeerXCluster()
	{
		documents.clear();
	}
	
	public void insertSummeryToCluster(int id, String summery, String path)
	{
//		System.out.println(summery);
		documents.add(new Document(id+"", summery, path));
	}
	
	public void insertSummeryToCluster(String id, String summery, String path)
	{
		documents.add(new Document(id, summery, path));
	}
	
	public void InsertToCluster(ResultSet resultSet) throws SQLException, IOException {
				
		while (resultSet.next()) {
	      String title = resultSet.getString("title");
	      String id = resultSet.getString("id");
	      //String url = resultSet.getString("citeseer_url");
	      String keyword = resultSet.getString("keyword");
	      //System.out.println(title);
	      
	      documents.add(new Document("", keyword, id));
	    }
	  }	
	
//	public void getClusters(String query) throws SQLException, IOException
//	{		
//		final Controller controller = ControllerFactory.createSimple();
//        final ProcessingResult byTopicClusters = controller.process(documents, query,
//            LingoClusteringAlgorithm.class);
//        final List<Cluster> clustersByTopic = byTopicClusters.getClusters();
//        
//        System.out.println("Number of Clusters created: " + clustersByTopic.size());
//        
//        ConsoleFormatter.displayClustersNameOnly(clustersByTopic);
//        
////        ConsoleFormatter.displayClusters(clustersByTopic); 
//	}
	
	public ArrayList<String> getClusters(String query) throws SQLException, IOException
	{		
		clusterDocs.clear();
		ArrayList<String> clusterList = new ArrayList<String>();
		
		final Controller controller = ControllerFactory.createSimple();
		
		/* Prepare attribute map */
        final Map<String, Object> attributes = new HashMap<String, Object>();
//        attributes.put("BisectingKMeansClusteringAlgorithm.desiredClusterCountBase", 10);
        attributes.put("BisectingKMeansClusteringAlgorithm.clusterCount", 10);
        attributes.put( CommonAttributesDescriptor.Keys.DOCUMENTS, documents );
//		attributes.put("BisectingKMeansClusteringAlgorithm.clusters", 500);
        
//        final ProcessingResult byTopicClusters = controller.process(documents, query,
//            LingoClusteringAlgorithm.class);
        final ProcessingResult byTopicClusters = controller.process(attributes,
        		BisectingKMeansClusteringAlgorithm.class);
        //BisectingKMeansClusteringAlgorithm.class. = 10;
//        final ProcessingResult byTopicClusters = controller.process(documents, query,
//                BisectingKMeansClusteringAlgorithm.class);
                
        final List<Cluster> clustersByTopic = byTopicClusters.getClusters();
        
        System.out.println("Number of Clusters created: " + clustersByTopic.size());
        
//        ConsoleFormatter.displayClustersNameOnly(clustersByTopic);
        ConsoleFormatter.getClusterNames(clustersByTopic, clusterList);
        
//        ConsoleFormatter.displayClusters(clustersByTopic);   
        
        return clusterList;
	}
	
	public void getClustersGivenDocID(String docId) throws SQLException, IOException
	{	
		for(String clstr: clusterDocs.keySet())
		{
			ArrayList<String> docs = clusterDocs.get(clstr);
			if(docs.contains(docId))
			{
				System.out.println(clstr);
			}
		}
	}
	
	public ArrayList<String> getDocIds(String cluster)
	{
		return clusterDocs.get(cluster);
	}
	
	public ArrayList<String> getClustersGivenDocID(ArrayList docIds) throws SQLException, IOException
	{	
		ArrayList<String> clusters = new ArrayList<String>();
		for(String clstr: clusterDocs.keySet())
		{
			ArrayList<String> docs = clusterDocs.get(clstr);
			if(isExist(docIds,docs))
			{
				clusters.add(clstr);
//				System.out.println("connecting: " + clstr);
			}
		}
		return clusters;
	}
	
	private boolean isExist(ArrayList<String> test, ArrayList<String> org)
	{
		for(int i=0;i<test.size();i++)
		{
			if(org.contains(test.get(i))) return true;
		}
		return false;
	}
	
	public void getClusterResult(String query) throws SQLException, IOException
	{
		DBUtility obDB = new DBUtility();
		//ResultSet rsltSet = obDB.readPapersFromDB();
		ResultSet rsltSet = obDB.readKeywordsFromDB();
		InsertToCluster(rsltSet);		
		
		final Controller controller = ControllerFactory.createSimple();
//        final ProcessingResult byTopicClusters = controller.process(documents, query,
//            LingoClusteringAlgorithm.class);
		final ProcessingResult byTopicClusters = controller.process(documents, query,
                BisectingKMeansClusteringAlgorithm.class);
		
        final List<Cluster> clustersByTopic = byTopicClusters.getClusters();
        
        System.out.println("Number of Clusters created: " + clustersByTopic.size());
        
//        Iterator<Cluster> clstrIterator = clustersByTopic.listIterator();
//        while(clstrIterator.hasNext())
//        {
//        	Cluster clstr = clstrIterator.next();
//        	System.out.println("Cluster Name: " + clstr.getLabel());
//        	
//        	System.out.println(clstr.getLabel() + " "
//                    + ClusterDetailsFormatter.formatClusterDetails(clstr));
//        }
        
        ConsoleFormatter.displayClustersNameOnly(clustersByTopic);
        
//        ConsoleFormatter.displayClusters(clustersByTopic);   
        
        obDB.close();
	}
	
	public static void main(String [] args)
    {
		CiteSeerXCluster ob = new CiteSeerXCluster();
		try {
			ob.getClusterResult("");
			
//			DBUtility obDB = new DBUtility();
//			obDB.readDataSet();		
//			obDB.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
