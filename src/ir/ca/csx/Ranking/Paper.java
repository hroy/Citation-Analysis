package ir.ca.csx.Ranking;

/**
 * 
 * @author H. Roy
 *
 */

public class Paper {

	int paperId;
	String paperTitle;
	double paperRank;
	
	public Paper(int paperId,String paperTitle,double paperRank)
	{
		this.paperId = paperId;
		this.paperTitle = paperTitle;
		this.paperRank = paperRank;
	}
	
	public int getPaperId() {
		return this.paperId;
	}
	
	public String getpaperTitle() {
		return this.paperTitle;
	}
	
	public double getpaperRank() {
		return this.paperRank;
	}
	
	public void setPaperId(int paperId) {
		this.paperId = paperId;
	}
	
	public void getpaperTitle(String title) {
		this.paperTitle = title;
	}
	
	public void getpaperRank(double rank) {
		this.paperRank = rank;
	}
}
