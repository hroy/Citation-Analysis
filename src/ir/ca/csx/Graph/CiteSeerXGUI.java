package ir.ca.csx.Graph;

import ir.ca.csx.Cluster.CiteSeerXCluster;
import ir.ca.csx.Cluster.CiteSeerXSearcher;
import ir.ca.csx.Cluster.DBUtility;
import ir.ca.csx.Ranking.Paper;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.util.DefaultMouseManager;
import org.graphstream.ui.swingViewer.util.DefaultShortcutManager;

/**
 * 
 * @author H. Roy
 * 
 */
public class CiteSeerXGUI extends javax.swing.JFrame {

	CiteSeerXGraph citationGraph;

	Viewer viewer;

	/**
	 * Creates new form Citation
	 */
	public CiteSeerXGUI() {

		this.setTitle("Citation Analysis (CiteSeerX)");
		initComponents();
		citationGraph = new CiteSeerXGraph();
		GraphInitialDisplay();
		drawGraph(false);
	}

	private void GraphInitialDisplay() {
		// TODO Auto-generated method stub
		try {
			// buildGraph("Information Retrieval");
			// buildGraph("Data Mining");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void drawGraph(boolean shouldDrawCompleteGraph) {
		jPanel1.removeAll();

		if (shouldDrawCompleteGraph)
			viewer = new Viewer(citationGraph.displayTotalGraph(),
					Viewer.ThreadingModel.GRAPH_IN_SWING_THREAD);
		else
			viewer = new Viewer(citationGraph.getGraph(),
					Viewer.ThreadingModel.GRAPH_IN_SWING_THREAD);
		jPanel1.setBorder(new EmptyBorder(0, 8, 0, 8));
		jPanel1.setBackground(SystemColor.activeCaption);
		jPanel1.setLayout(new BorderLayout(0, 0));

		viewer.enableAutoLayout();
		// viewer.disableAutoLayout();
		View view = viewer.addDefaultView(false);

		view.setMouseManager(new InternalMouseManager());

		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
		jPanel1.add(view, BorderLayout.CENTER);

		Toolkit tk = Toolkit.getDefaultToolkit();
		int xSize = ((int) tk.getScreenSize().getWidth());
		int ySize = ((int) tk.getScreenSize().getHeight());
		setSize(1366, 740);
		setVisible(true);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jPanel2 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jTextField1 = new javax.swing.JTextField();
		jButton1 = new javax.swing.JButton();
		jLabel2 = new javax.swing.JLabel();
		jPanel1 = new javax.swing.JPanel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		jPanel2.setBackground(java.awt.SystemColor.inactiveCaptionBorder);
		jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		jPanel2.setForeground(new java.awt.Color(255, 255, 255));

		jLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
		jLabel1.setText("Search");

		jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				jTextField1KeyPressed(evt);
			}
		});

		jButton1.setText("Go");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(
				jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout
				.setHorizontalGroup(jPanel2Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel2Layout
										.createSequentialGroup()
										.addGap(64, 64, 64)
										.addComponent(
												jLabel1,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												54,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												jTextField1,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												195,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(jButton1)
										.addGap(18, 18, 18)
										.addComponent(jLabel2)
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));
		jPanel2Layout
				.setVerticalGroup(jPanel2Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel2Layout
										.createSequentialGroup()
										.addGap(25, 25, 25)
										.addGroup(
												jPanel2Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(jLabel1)
														.addComponent(
																jTextField1,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(jButton1)
														.addComponent(jLabel2))
										.addContainerGap(19, Short.MAX_VALUE)));

		jPanel1.setBackground(java.awt.SystemColor.inactiveCaptionBorder);
		jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(
				jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 0,
				Short.MAX_VALUE));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 226,
				Short.MAX_VALUE));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addComponent(jPanel2,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(jPanel1,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
		// TODO add your handling code here:
		System.out.println();
		System.out.println("Searching for- " + jTextField1.getText());
		String searchString = jTextField1.getText().trim();

		if (searchString.isEmpty())
			return;

		if (!jTextField1.getText().trim().isEmpty())
			jLabel2.setText("Searchin- for: " + jTextField1.getText());

		try {
			jPanel1.removeAll();
			buildGraph(searchString);
			System.out.println("Drawing Graph...");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		drawGraph(false);
	}// GEN-LAST:event_jButton1ActionPerformed

	private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jTextField1KeyPressed
		// TODO add your handling code here:
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			System.out.println();
			System.out.println("Searching for- " + jTextField1.getText());
			String searchString = jTextField1.getText().trim();

			if (searchString.isEmpty())
				return;

			if (!jTextField1.getText().trim().isEmpty())
				jLabel2.setText("Searching for- " + jTextField1.getText());

			try {
				jPanel1.removeAll();
				buildGraph(searchString);
				System.out.println("Drawing Graph...");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			drawGraph(false);
		}
	}// GEN-LAST:event_jTextField1KeyPressed

	CiteSeerXCluster obClstr;
	DBUtility obDB;
	
	private void buildGraph(String query) throws Exception {
		ArrayList<String> docs = new ArrayList<String>();
		obClstr = new CiteSeerXCluster();

		CiteSeerXSearcher search = new CiteSeerXSearcher(query.trim());
		docs = search.searchIndex();

		Iterator<String> itr = docs.iterator();
		while (itr.hasNext()) {
			String f = itr.next();
			String abs = readFile(f);
			String id = f.substring(f.lastIndexOf("\\") + 1,
					f.lastIndexOf(".txt"));
			// System.out.println(id);

			// obClstr.insertSummeryToCluster(i++,abs,f);
			obClstr.insertSummeryToCluster(id.trim(), abs, f);
		}

		ArrayList<EdgeCiteSeerX> edges = new ArrayList<EdgeCiteSeerX>();
		obDB = new DBUtility();

		ArrayList<String> clusters = obClstr.getClusters(query);
		for (String start : clusters) {
			ArrayList<String> docsInCluster = obClstr.getDocIds(start);
			ArrayList<String> docsCited = obDB.getCitedPaperID(docsInCluster);
			ArrayList<String> connCluster = obClstr
					.getClustersGivenDocID(docsCited);

			// ArrayList<String> docsInCluster = obClstr.getDocIds(start);
			// ArrayList<String> connCluster =
			// obClstr.getClustersGivenDocID(docsInCluster);

			// System.out.println("connected: "+ connCluster.size());

			for (String end : connCluster) {
//				if(obDB.getCitationCount(obClstr.getDocIds(start), obClstr.getDocIds(end))!=0)
//				{
					EdgeCiteSeerX edge = new EdgeCiteSeerX(start, end);
					edges.add(edge);
//				}
			}

//			ArrayList<String> docsCitedby = obDB
//					.getParentPaperID(docsInCluster);
//			;
//			ArrayList<String> connClusterby = obClstr
//					.getClustersGivenDocID(docsCited);
//
//			for (String end : connCluster) {
//				EdgeCiteSeerX edge = new EdgeCiteSeerX(end, start);
//				edges.add(edge);
//			}
		}

		citationGraph.GraphDisplay(clusters, edges, query);
	}

	public String readFile(String path) throws FileNotFoundException {
		File file = new File(path);
		if (file.isFile()) {
			String str = new Scanner(file).useDelimiter("\\Z").next().trim();
			return str;
		} else
			return "";
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed"
		// desc=" Look and feel setting code (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the
		 * default look and feel. For details see
		 * http://download.oracle.com/javase
		 * /tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
					.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(CiteSeerXGUI.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(CiteSeerXGUI.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(CiteSeerXGUI.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(CiteSeerXGUI.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new CiteSeerXGUI().setVisible(true);
			}
		});
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jButton1;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JTextField jTextField1;
	// End of variables declaration//GEN-END:variables
	private JPopupMenu popupMenu; // popup menu for choosing a shape

	private JMenuItem nameItem; // menu item for drawing a square

	// private JMenuItem reset;

	class InternalMouseManager extends DefaultMouseManager {
		@Override
		protected void mouseButtonPressOnElement(GraphicElement element,
				MouseEvent event) {

			super.mouseButtonPressOnElement(element, event);
			if (event.getButton() == 3) {
				popupMenu = new JPopupMenu();

				nameItem = new JMenuItem("Cluster: " + element.getId());
				popupMenu.add(nameItem);

				ImageIcon icon = createImageIcon(Information.pdfImageLocation);
				AddPapersInMenu(element.getId(), popupMenu, icon);
				// square = new JMenuItem("Node id: " + element.getId(), icon);
				// square.setName("49440");
				//
				// square.addActionListener(new MyActionListener(view));
				// popupMenu.add(square);

				// reset = new JMenuItem("Reset");
				// reset.setText("Reset");
				// reset.addActionListener(new MyActionListener(view));
				//
				// popupMenu.add(reset);
				popupMenu
						.show(event.getComponent(), event.getX(), event.getY());
			} else if (event.getButton() == 1) {
				// Node node = graph.getNode(element.getId());
				//
				// Iterator outgoingEdges =
				// node.getEachLeavingEdge().iterator();
				//
				// Iterator incomingEdges =
				// node.getEachEnteringEdge().iterator();
				// while (incomingEdges.hasNext()) {
				// Edge edge = (Edge) incomingEdges.next();
				//
				// if (edge.getId().endsWith(node.getId()))
				// edge.addAttribute("ui.label", "");
				// // edge.clearAttributes();
				// }
				//
				// while (outgoingEdges.hasNext()) {
				// Edge edge = (Edge) outgoingEdges.next();
				//
				// if (edge.getId().startsWith(node.getId())) {
				// edge.addAttribute("ui.label", "Edge:");
				// System.out.println("EdgeId: " + edge.getId());
				// }
				// }

				popupMenu = new JPopupMenu();

				 nameItem = new JMenuItem("Cluster: " + element.getId());
				 popupMenu.add(nameItem);

				AddEdgeInMenu(element.getId(), popupMenu);
				// square = new JMenuItem("Node id: " + element.getId(), icon);
				// square.setName("49440");
				//
				// square.addActionListener(new MyActionListener(view));
				// popupMenu.add(square);

				// reset = new JMenuItem("Reset");
				// reset.setText("Reset");
				// reset.addActionListener(new MyActionListener(view));
				//
				// popupMenu.add(reset);
				popupMenu
						.show(event.getComponent(), event.getX(), event.getY());
			}

		}

		private void AddPapersInMenu(String cluster, JPopupMenu popupMenu,
				ImageIcon icon) {
			// TODO Auto-generated method stub
			ArrayList<String> docsInCluster = obClstr.getDocIds(cluster);
			ArrayList<Paper> rankedDocs = new DBUtility()
					.getPaperInRankedOrder(docsInCluster);

			popupMenu.add("Total papers inside: " + docsInCluster.size());
			popupMenu.add(" ");
			
			for (int i = 0; i < 10 && i < rankedDocs.size(); i++) {
				Paper paper = rankedDocs.get(i);
				JMenuItem menuItem = new JMenuItem(paper.getPaperId() + "",
						icon);
				menuItem.setName(paper.getPaperId() + "");
				menuItem.setText(paper.getPaperId() + "-"
						+ paper.getpaperTitle() + " (" + paper.getpaperRank()
						+ ")");
				menuItem.addActionListener(new MyActionListener(view));
				popupMenu.add(menuItem);
			}
		}

		private void AddEdgeInMenu(String nodeID, JPopupMenu popupMenu) {

			Node node = graph.getNode(nodeID);

			ArrayList<String> fromIDs = obClstr.getDocIds(nodeID);
			popupMenu.add("Total papers inside: " + fromIDs.size());
			
			popupMenu.add(" ");
			popupMenu.add("Cited Clusters are following-");
			
			Iterator outgoingEdges = node.getEachLeavingEdge().iterator();

			while (outgoingEdges.hasNext()) {
				Edge edge = (Edge) outgoingEdges.next();

				if (edge.getId().startsWith(node.getId())) {

					String citedNodeId = edge.getId().replace(node.getId(), "");					
					
					ArrayList<String> toIDs = obClstr.getDocIds(citedNodeId);
					
					popupMenu.add(citedNodeId + ": <" +obDB.getCitedCount(fromIDs, toIDs) + ","+ obDB.getCitationCount(fromIDs, toIDs)+">");
//					popupMenu.add(citedNodeId);

				}
			}
		}
	}

	protected static ImageIcon createImageIcon(String path) {
		return new ImageIcon(path);
	}

	class MyActionListener implements ActionListener {

		View view;

		public MyActionListener(View v) {
			view = v;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JMenuItem menue = (JMenuItem) e.getSource();
			if (menue.getText().equalsIgnoreCase("Reset")) {
				// JOptionPane.showMessageDialog(view, "Reset");
				GraphInitialDisplay();
				drawGraph(false);

			} else {
				// JOptionPane.showMessageDialog(view, e.getID());
				new PDFOpener().openPdf(menue.getName(), view);
			}
		}
	}
}
