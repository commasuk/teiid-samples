package com.jboss.teiid.client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

/**
 * 
 * @author ksoong
 * 
 */
public class XMLDataSourceClient {

	private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
	private static final String JDBC_URL = "jdbc:teiid:Books_VDB@mm://localhost:31000;version=1";
	private static final String JDBC_USER = "user";
	private static final String JDBC_PASS = "user";

	private static final String SQL_QUERY = "SELECT * FROM ViewModel.Books";
	private static final String SQL_UPDATE = "UPDATE ViewModel.Books set author = 'Arnold Johansson, Anders Welén' where id = 100";


	private Connection getDriverConnection() throws Exception {
		Class.forName(JDBC_DRIVER);
		return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS); 
	}


	protected void executeQuery(Connection conn, String sql) throws Exception {
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			ResultSetMetaData metadata = rs.getMetaData();
			int columns = metadata.getColumnCount();
			for (int row = 1; rs.next(); row++) {
				System.out.print(row + ": ");
				for (int i = 0; i < columns; i++) {
					if (i > 0) {
						System.out.print(", ");
					}
					System.out.print(rs.getString(i + 1));
				}
				System.out.println();
			}
		} catch (Exception e) {
			throw e ;
		} finally {
			if (null != rs) {
				rs.close();
			}
			if(null != stmt) {
				stmt.close();
			}
		}
		
	}
	
	/*
	 * Enable Metadata to allow updates
	 */
	protected void executeUpdate(Connection conn, String sql) throws Exception {
		
		Statement stmt = null;
		
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			throw e;
		} finally {
			if(null != stmt) {
				stmt.close();
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		XMLDataSourceClient client = new XMLDataSourceClient();
		
		Connection conn = client.getDriverConnection();
		
		try {
			client.executeQuery(conn, SQL_QUERY);
//			client.executeUpdate(conn, SQL_UPDATE);
		} catch (Exception e) {
			throw e;
		} finally {
			conn.close();
		}

	}

}
