package ir.ca.csx.Crawler;

import java.sql.*;
import java.util.ArrayList;

public class MySQLAccess {
	// JDBC driver name and database URL
	String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	String DB_URL = "jdbc:mysql://localhost/ir1";

	// Database credentials
	String USER = "root";
	String PASS = "12345";

	Connection conn = null;

	public MySQLAccess() {
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

	public int insertPaper(String paperTitle) {
		PreparedStatement stmt;
		int id = 0;

		try {

			paperTitle = paperTitle.replace("\'", "\\'");

			String selectSQL = "select ir1.insert_paper('" + paperTitle
					+ "') as id";

			stmt = conn.prepareStatement(selectSQL);
			ResultSet rs = stmt.executeQuery(selectSQL);
			// STEP 5: Extract data from result set
			while (rs.next()) {
				// Retrieve by column name
				id = rs.getInt("id");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}

	public void insertCitation(int citation_from, int cited_paper) {
		try {

			String sql = "INSERT INTO ir1.citation (paper_id, cited_paper_id) "
					+ "VALUES (" + citation_from + ",'" + cited_paper + "')";

			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.executeUpdate(sql);
			System.out.println("Inserted records into the table...");
			stmt.close();

		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} // end try
	}// end main

	void close() {
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

}// end JDBCExample