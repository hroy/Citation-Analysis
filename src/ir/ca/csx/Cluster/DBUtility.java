package ir.ca.csx.Cluster;

import ir.ca.csx.Graph.Information;
import ir.ca.csx.Ranking.Paper;

import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBUtility {

	int INVALID_INT = -999;
	// JDBC driver name and database URL
	String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	String DB_URL = "jdbc:mysql://localhost/ir";

	// Database credentials
	String USER = "root";
	String PASS = "12345";

	Connection conn = null;
	// private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	Tokenizer obTokenizer = new Tokenizer();

	public DBUtility() {
		// STEP 2: Register JDBC driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Connecting to a selected database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Connected database successfully...");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public HashMap<Integer, Paper> getAllPapers() {
		HashMap<Integer, Paper> hashMap = new HashMap<Integer, Paper>();

		PreparedStatement stmt;

		try {
			String selectSQL = Information.qGetAllPapers;

			stmt = conn.prepareStatement(selectSQL);
			ResultSet rs = stmt.executeQuery(selectSQL);
			while (rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				hashMap.put(id, new Paper(id, title, 0.0));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hashMap;
	}

	public ResultSet readKeywordsFromDB() throws SQLException, IOException {
		// statements allow to issue SQL queries to the database
		Statement statement = conn.createStatement();
		// resultSet gets the result of the SQL query
		resultSet = statement
				.executeQuery("select id,title,keyword from ir.keywords");
		// statement.close();

		return resultSet;
	}

	public ResultSet readPapersFromDB() throws SQLException, IOException {
		// statements allow to issue SQL queries to the database
		Statement statement = conn.createStatement();
		// resultSet gets the result of the SQL query
		resultSet = statement.executeQuery("select * from ir.paper");
		// statement.close();

		return resultSet;
	}

	public int readPapersIDFromTitle(String title) {
		// System.out.println("title: " + title);
		// statements allow to issue SQL queries to the database
		Statement statement;
		try {
			statement = conn.createStatement();

			// resultSet gets the result of the SQL query
			ResultSet rs = statement
					.executeQuery("select id from ir.paper where title = '"
							+ title + "'");
			// statement.close();

			if (rs.next()) {
				return rs.getInt("id");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.print("not success: " + title);
			return INVALID_INT;
		}
		return INVALID_INT;
	}

	public void readDataSet() throws SQLException, IOException {
		// statements allow to issue SQL queries to the database
		Statement statement = conn.createStatement();
		// resultSet gets the result of the SQL query
		resultSet = statement.executeQuery("select id,title from ir.paper");
		// ProcessDataSet(resultSet);
		InsertKeywords(resultSet);
		statement.close();
	}

	public boolean renameFileInSameDir(String fileDirectory,
			String oldFileName, String newFileName) {
		File oldfile = new File(fileDirectory, oldFileName);
		File newfile = new File(fileDirectory, newFileName);

		if (oldfile.renameTo(newfile)) {
			System.out.println("Rename succesful");
		} else {

			System.out.println("Rename failed");
			System.out.println("Old file: " + oldFileName + " new File: "
					+ newFileName);
			return false;
		}

		return true;
	}

	public void renameFiles() throws SQLException, IOException {
		// statements allow to issue SQL queries to the database
		Statement statement = conn.createStatement();
		// resultSet gets the result of the SQL query
		resultSet = statement
				.executeQuery("select id, paper_abstract_url, paper_download_link from ir.paper");

		int success = 0;
		int fail = 0;
		while (resultSet.next()) {

			int id = resultSet.getInt("id");
			String abs = resultSet.getString("paper_abstract_url");
			String pdf = resultSet.getString("paper_download_link");

			if (renameFileInSameDir("D:\\IR_Project\\ABSTRACT", abs, id
					+ ".txt"))
				success++;
			else
				fail++;
		}

		System.out.println("Total files: " + (success + fail));
		System.out.println("# Success: " + success);
		System.out.println("# Fail: " + fail);
		statement.close();
	}

	public void InserCiterPaperId() {
		// statements allow to issue SQL queries to the database
		int success = 0;
		int fail = 0;
		Statement statement;
		try {
			statement = conn.createStatement();

			// resultSet gets the result of the SQL query
			// resultSet =
			// statement.executeQuery("select id, paper_abstract_url, paper_download_link from ir.paper");
			ResultSet rs = statement
					.executeQuery("select distinct cited_paper from ir.citation");

			// System.out.println("@func");

			try {
				while (rs.next()) {

					// System.out.println("@while");
					String paperTitle = "";
					try {
						paperTitle = rs.getString("cited_paper");
					} catch (SQLException e) {

					}
					int paperId = readPapersIDFromTitle(paperTitle);
					if (paperId != INVALID_INT) {
						System.out.println("Yes");
						updateCitedPaperID(paperTitle, paperId);
						success++;
					} else
						fail++;

					// System.out.println("Title: " + paperTitle);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("succ: " + success + ", fail: " + fail);
	}

	private void updateCitedPaperID(String paperTitle, int paperId) {
		// TODO Auto-generated method stub
		Statement statement;
		try {
			statement = conn.createStatement();

			// resultSet gets the result of the SQL query
			// resultSet =
			// statement.executeQuery("select id, paper_abstract_url, paper_download_link from ir.paper");
			int rs = statement
					.executeUpdate("update ir.citation set cited_paper_id = "
							+ paperId + " where cited_paper = '" + paperTitle
							+ "'");
		} catch (SQLException e) {
			System.out.println("not updated for- " + paperTitle);
		}

	}

	// From -> cited papers
	// public ArrayList<Integer> getCitedPaperID(ArrayList<Integer> paperIds) {
	// // TODO Auto-generated method stub
	// // statements allow to issue SQL queries to the database
	// ArrayList<Integer> result = new ArrayList<Integer>();
	//
	// if (paperIds.size() == 0)
	// return result;
	//
	// try {
	// Statement statement = conn.createStatement();
	// // resultSet gets the result of the SQL query
	//
	// String ids = " in(";
	// for (int i : paperIds) {
	// ids += i + ",";
	// }
	//
	// ids = ids.substring(0, ids.length() - 1) + ")";
	//
	// resultSet = statement
	// .executeQuery("select distinct cited_paper_id from ir.citation where citation_from "
	// + ids);
	// // statement.close();
	//
	// while (resultSet.next()) {
	// Integer paper = resultSet.getInt("cited_paper_id");
	// if (paper != null)
	// result.add(paper);
	// }
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return result;
	// }

	public void deleteRedundance(List<String> paperIds) {
		ArrayList<String> result = new ArrayList<String>();
		if (paperIds.size() == 0)
			return;

		try {
			Statement statement = conn.createStatement();
			// resultSet gets the result of the SQL query

			String ids = " not in(";
			for (String i : paperIds) {
				ids += i + ",";
			}

			ids = ids.substring(0, ids.length() - 1) + ")";

			statement.executeUpdate(Information.qDeletePapers + ids);
			// statement.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return;
	}

	public ArrayList<String> getCitedPaperID(ArrayList<String> paperIds) {
		// TODO Auto-generated method stub
		// statements allow to issue SQL queries to the database
		ArrayList<String> result = new ArrayList<String>();

		if (paperIds.size() == 0)
			return result;

		try {
			Statement statement = conn.createStatement();
			// resultSet gets the result of the SQL query

			String ids = " in(";
			for (String i : paperIds) {
				ids += i + ",";
			}

			ids = ids.substring(0, ids.length() - 1) + ")";

			resultSet = statement.executeQuery(Information.qGetCitedPapers
					+ ids);
			// statement.close();

			while (resultSet.next()) {
				Integer paper = resultSet.getInt("cited_paper_id");
				if (paper != null)
					result.add(paper + "");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public int getCitationCount(ArrayList<String> from, ArrayList<String> to) {
		// TODO Auto-generated method stub
		// statements allow to issue SQL queries to the database
		ArrayList<Paper> result = new ArrayList<Paper>();

		if (from.size() == 0 || to.size() == 0)
			return 0;

		try {
			Statement statement = conn.createStatement();
			// resultSet gets the result of the SQL query

			String fromIds = " in(";
			for (String i : from) {
				fromIds += i + ",";
			}

			fromIds = fromIds.substring(0, fromIds.length() - 1) + ")";

			String toIds = " in(";
			for (String i : to) {
				toIds += i + ",";
			}

			toIds = toIds.substring(0, toIds.length() - 1) + ")";

			fromIds = fromIds + " AND cited_paper_id " + toIds;

//			System.out.println(Information.getCitationCount	+ fromIds);
			
			resultSet = statement.executeQuery(Information.getCitationCount
					+ fromIds);
			// statement.close();

			while (resultSet.next()) {
				Integer total = resultSet.getInt("total");

				if (total != null)
					return total.intValue();

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public int getCitedCount(ArrayList<String> from, ArrayList<String> to) {
		// TODO Auto-generated method stub
		// statements allow to issue SQL queries to the database
		ArrayList<Paper> result = new ArrayList<Paper>();

		if (from.size() == 0 || to.size() == 0)
			return 0;

		try {
			Statement statement = conn.createStatement();
			// resultSet gets the result of the SQL query

			String fromIds = " in(";
			for (String i : from) {
				fromIds += i + ",";
			}

			fromIds = fromIds.substring(0, fromIds.length() - 1) + ")";

			String toIds = " in(";
			for (String i : to) {
				toIds += i + ",";
			}

			toIds = toIds.substring(0, toIds.length() - 1) + ")";

			fromIds = fromIds + " AND cited_paper_id " + toIds;

			String q = "select count(*) as total from (" + Information.getCitedCount
			+ fromIds + ") t";
//			System.out.println(q);
			
			resultSet = statement.executeQuery(q);
			// statement.close();

			while (resultSet.next()) {
				Integer total = resultSet.getInt("total");

				if (total != null)
					return total.intValue();

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public ArrayList<Paper> getPaperInRankedOrder(ArrayList<String> paperIds) {
		// TODO Auto-generated method stub
		// statements allow to issue SQL queries to the database
		ArrayList<Paper> result = new ArrayList<Paper>();

		if (paperIds.size() == 0)
			return result;

		try {
			Statement statement = conn.createStatement();
			// resultSet gets the result of the SQL query

			String ids = " in(";
			for (String i : paperIds) {
				ids += i + ",";
			}

			ids = ids.substring(0, ids.length() - 1) + ")";

			resultSet = statement.executeQuery(Information.qGetRankedPapers
					+ ids + " order by rank desc");
			// statement.close();

			while (resultSet.next()) {
				Integer id = resultSet.getInt("id");
				String title = resultSet.getString("title");
				double rank = resultSet.getDouble("rank");

				if (id != null)
					result.add(new Paper(id, title, rank));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public ArrayList<String> getCitedPaperIDs(int paperId) {
		// TODO Auto-generated method stub
		// statements allow to issue SQL queries to the database
		ArrayList<String> result = new ArrayList<String>();

		try {
			Statement statement = conn.createStatement();
			// resultSet gets the result of the SQL query

			resultSet = statement.executeQuery(Information.qGetCitedPaperIDs
					+ paperId);
			// statement.close();

			while (resultSet.next()) {
				Integer paper = resultSet.getInt("cited_paper_id");
				if (paper != null)
					result.add(paper + "");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public void updateRank(int paperId, double rank) {
		Statement statement;
		try {
			statement = conn.createStatement();

			// resultSet gets the result of the SQL query
			// resultSet =
			// statement.executeQuery("select id, paper_abstract_url, paper_download_link from ir.paper");
			int rs = statement.executeUpdate(Information.qUpdateRank + rank
					+ " where id = " + paperId);
		} catch (SQLException e) {
			System.out.println("not updated for- " + paperId);
		}
	}

	// public ArrayList<Integer> getParentPaperID(ArrayList<Integer> paperIds) {
	// // TODO Auto-generated method stub
	// // statements allow to issue SQL queries to the database
	// ArrayList<Integer> result = new ArrayList<Integer>();
	//
	// if (paperIds.size() == 0)
	// return result;
	//
	// try {
	// Statement statement = conn.createStatement();
	// // resultSet gets the result of the SQL query
	//
	// String ids = " in(";
	// for (int i : paperIds) {
	// ids += i + ",";
	// }
	//
	// ids = ids.substring(0, ids.length() - 1) + ")";
	//
	// resultSet = statement
	// .executeQuery("select distinct citation_from from ir.citation where cited_paper_id "
	// + ids);
	// // statement.close();
	//
	// while (resultSet.next()) {
	// Integer paper = resultSet.getInt("citation_from");
	// if (paper != null)
	// result.add(paper);
	//
	// }
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return result;
	// }

	public ArrayList<String> getParentPaperID(ArrayList<String> paperIds) {
		// TODO Auto-generated method stub
		// statements allow to issue SQL queries to the database
		ArrayList<String> result = new ArrayList<String>();

		if (paperIds.size() == 0)
			return result;

		try {
			Statement statement = conn.createStatement();
			// resultSet gets the result of the SQL query

			String ids = " in(";
			for (String i : paperIds) {
				ids += i + ",";
			}

			ids = ids.substring(0, ids.length() - 1) + ")";

			resultSet = statement.executeQuery(Information.qGetParentPapers
					+ ids);
			// statement.close();

			while (resultSet.next()) {
				Integer paper = resultSet.getInt("paper_id");
				if (paper != null)
					result.add(paper + "");

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	private void ProcessDataSet(ResultSet resultSet) throws SQLException {
		// resultSet is initialised before the first data set
		while (resultSet.next()) {
			// it is possible to get the columns via name
			// also possible to get the columns via the column number
			// which starts at 1
			// e.g., resultSet.getSTring(2);
			String title = resultSet.getString("title");
			System.out.println(title);
		}
	}

	public void InsertKeywords(ResultSet resultSet) throws SQLException,
			IOException {
		while (resultSet.next()) {
			String title = resultSet.getString("title");
			String id = resultSet.getString("id");
			// System.out.println(title);

			String[] strArray = obTokenizer.getTokens(title);

			String conKeywords = "";

			if (strArray.length > 0)
				conKeywords += strArray[0];
			for (int i = 1; i < strArray.length; i++) {
				conKeywords += "," + strArray[i];
			}

			String sql = "INSERT INTO ir.keywords (id,title,keyword) "
					+ "VALUES (" + "'" + id + "'," + "'" + title + "'," + "'"
					+ conKeywords + "')";

			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.executeUpdate(sql);
			stmt.close();
		}
	}

	void close() {
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
}
