package db;

import java.sql.*;

public class DBConnection
{
	// variables that hold values to connect to the database
	private String dbUrl = "jdbc:mysql://localhost:3306/LibrarySystem";
	private String dbUser = "root";
	private String dbPass = "";
	
	// database connection
	private static Connection conn = null;
	
	/*
	 * 	Create a connection to the database if possible.
	 * 
	 * 	Tries and catches to respond to errors such as the inability to
	 * 	find the connection driver, the connection fails, or there is
	 * 	already an existing connection.
	 */
	public DBConnection()
	{
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Unable to load the Driver class");
			return;
		}
		
		if (conn == null)
		{
			try
			{
				conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
			}
			catch (SQLException se)
			{
				System.out.println("SQL Exception: " + se.getMessage());
				se.printStackTrace(System.out);
			}
		}
		else
		{
			System.out.println("Existing connection available!");
		}
	}
	
	// returns the existing connection
	public Connection getConnection()
	{
		return conn;
	}
}