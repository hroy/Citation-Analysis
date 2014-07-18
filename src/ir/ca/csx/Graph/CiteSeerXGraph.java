package ir.ca.csx.Graph;

import ir.ca.csx.Cluster.DBUtility;
import ir.ca.csx.Ranking.Paper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.graphstream.algorithm.PageRank;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;

import edu.uci.ics.jung.graph.DirectedSparseGraph;

/**
 * 
 * @author H. Roy
 * 
 */
public class CiteSeerXGraph {

	Graph graph;	
	HashMap<String,String> clusterMap = new HashMap<String, String>();
	
	public CiteSeerXGraph() {
		graph = new MultiGraph("mg");

		graph.addAttribute("ui.stylesheet",
				"url('file:///"+ Information.cssLocation+"')");
		System.setProperty("org.graphstream.ui.renderer",
				"org.graphstream.ui.j2dviewer.J2DGraphRenderer");

		graph.setStrict(false);
		graph.setAutoCreate(true);	
	}

	public Graph GraphDisplay(ArrayList<String> nodes, ArrayList<EdgeCiteSeerX> edges, String query) {
		graph.clear();
		//graph = new MultiGraph("mg");
		graph.addAttribute("ui.stylesheet",
				"url('file:///"+ Information.cssLocation+"')");
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

		graph.setStrict(false);
		graph.setAutoCreate(true);
		
//		graph.getNodeCount();
		
//		clusterMap.clear();
//		for(int j=0;j<nodes.size();j++)
//		{
//			graph.removeNode(j+"");
//		}
		
//		System.out.println("before- No of Clusters: " + nodes.size()+", nodes: "+graph.getNodeCount()+", q: "+query);
		
		//graph.clear();
//		System.out.println("Already added node " + graph.getNodeCount());
		
		
		for (int i=0;i<nodes.size();i++) {
//			clusterMap.put(nodes.get(i), i+"");
//			addNode(i+"", nodes.get(i));
			addNode(nodes.get(i), nodes.get(i));
		}

		for (EdgeCiteSeerX edge : edges) {
			if(!(edge.start.trim().equalsIgnoreCase(edge.end.trim())))
			{
				addEdge(edge.start, edge.end, true);
//				addEdge(edge.start, edge.end, true,20+"");
			}
//			addEdge(clusterMap.get(edge.start), clusterMap.get(edge.end), true);
		}
//		System.out.println("after- No of Clusters: " + nodes.size()+", nodes: "+graph.getNodeCount());
		
		return graph;
	}

	public static Graph displayTotalGraph() {

		Graph graphForRanking = new MultiGraph("pageRankGraph");
		DBUtility obDB = new DBUtility();
		
		HashMap<Integer, Paper> hashMapForPaper = obDB.getAllPapers();
//		for(int node: hashMapForPaper.keySet())
//		{
//			addNodeForRanking(graphForRanking,node+"", node+"");
//		}
		
		System.out.println("Total number of papers: " + hashMapForPaper.size());
		
		for(int node: hashMapForPaper.keySet())
		{
			if(graphForRanking.getNode(node+"")==null)addNodeForRanking(graphForRanking,node+"", node+"");
			ArrayList<String> citedPapers = obDB.getCitedPaperIDs(node);
			
			for(String node2: citedPapers)
			{
				if(graphForRanking.getNode(node2)==null)addNodeForRanking(graphForRanking,node2, node2);
				addEdgeForRanking(graphForRanking,node+"", node2, true);
			}
		}
		
		System.out.println("Graph drawing done! Now doing page rank.. :)");
		
		PageRank pageRank = new PageRank();
		pageRank.init(graphForRanking);

//		while (graphForRanking.getNodeCount() < 100) {
	        for (Node node : graphForRanking) {
	                double rank = pageRank.getRank(node);
	                System.out.println("nodeid: "+node.getId() + ", rank: "+rank);
	                obDB.updateRank(Integer.parseInt(node.getId()), rank);
	        }
//	 }

		return graphForRanking;
	}

	public Graph getGraph() {
		return graph;
	}

	void addNode(String NodeId, String NodeName) {
		Node node = graph.addNode(NodeId);
		node.addAttribute("ui.label", NodeName);
	}

	void addEdge(String formNodeId, String toNodeId, boolean isDirected) {
		graph.addEdge(formNodeId + toNodeId, formNodeId, toNodeId, isDirected);
		//addEdge(formNodeId, toNodeId, isDirected, Math.random()+"");
	}

	void addEdge(String formNodeId, String toNodeId, boolean isDirected, String edgeLabel) {		
		String edgeId = formNodeId + toNodeId;
		graph.addEdge(edgeId, formNodeId, toNodeId, isDirected);
		graph.getEdge(edgeId).addAttribute("ui.label", edgeLabel);
 	}

	
	static void addNodeForRanking(Graph graphForRanking, String NodeId, String NodeName) {
		Node node = graphForRanking.addNode(NodeId);
		node.addAttribute("ui.label", NodeName);
	}

	static void addEdgeForRanking(Graph graphForRanking, String formNodeId, String toNodeId, boolean isDirected) {
		graphForRanking.addEdge(formNodeId + toNodeId, formNodeId, toNodeId, isDirected);
	}

}
