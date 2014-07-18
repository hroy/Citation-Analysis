package ir.ca.csx.Graph;

import java.awt.Desktop;
import java.io.File;

import javax.swing.JOptionPane;

import org.graphstream.ui.swingViewer.View;

/**
 * 
 * @author H. Roy
 * 
 */
public class PDFOpener {
	public void openPdf(String pdfFilePath, View view) {
		String pdfFolderLocation = Information.pdfFolderLocation;
		try {

			File pdfFile = new File(pdfFolderLocation, pdfFilePath + ".pdf");
			if (pdfFile.exists()) {

				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(pdfFile);
				} else {
					System.out.println("Awt Desktop is not supported!");
				}

			} else {
				JOptionPane.showMessageDialog(view, "File: " + pdfFilePath + " does not exist!");
				System.out.println("File: " + pdfFilePath + " does not exist!");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
