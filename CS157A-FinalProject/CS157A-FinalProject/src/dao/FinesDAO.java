package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import objects.Fines;

/*
 * 	DAO class controls inputs from UI to the database
 */

public class FinesDAO
{
	private Connection conn = null;
	
	public FinesDAO(Connection dbConnection)
	{
		conn = dbConnection;
	}
	
	// insert a row into the Fines table
	public void insert(Fines fine)
	{
		if (conn == null)
		{
			System.out.println("Connection error");
			return;
		}
		
		try
		{
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Fines values(?,?,?)");
			
			pstmt.setInt(1, fine.getMemberId());
			pstmt.setDouble(2, fine.getAmount());
			pstmt.setDate(3, fine.getLastFineDate());
			
			pstmt.executeUpdate();
			pstmt.close();
		}
		catch (SQLException se)
		{
			System.out.println("SQL Exception: " + se.getMessage());
			se.printStackTrace(System.out);
		}
	}
	
	// update a row in the Fines table
	public void update(Fines fine)
	{
		if (conn == null)
		{
			System.out.println("Connection error");
			return;
		}
		
		try
		{
			String sql = "UPDATE Fines SET ";
			int count = 1;
			boolean setAmount = false;
			boolean setDate = false;
			
			if (fine.getAmount() > 0.0)
			{
				sql += "Amount = ?";
				setAmount = true;
			}
			
			if (fine.getLastFineDate() != null)
			{
				if (setAmount)
				{
					sql += ", ";
				}
				
				sql += "LastFineDate = ?";
				setDate = true;
			}
			
			sql += " WHERE Member_ID = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			if (setAmount)
			{
				pstmt.setDouble(count, fine.getAmount());
				count++;
			}
			
			if (setDate)
			{
				pstmt.setDate(count, fine.getLastFineDate());
				count++;
			}
			
			pstmt.setInt(count, fine.getMemberId());
			
			pstmt.executeUpdate();
			pstmt.close();
		}
		catch (SQLException se)
		{
			System.out.println("SQL Exception: " + se.getMessage());
			se.printStackTrace(System.out);
		}
	}
	
	// select one or all rows in the Fines table
	public ArrayList<Fines> select(Fines fine)
	{
		ArrayList<Fines> fineList = new ArrayList<>();
		
		if (conn == null)
		{
			System.out.println("Connection error");
			return null;
		}
		
		try
		{
			String sql = "SELECT * FROM Fines";
			int count = 1;
			boolean setMemberId = false;
			boolean setAmount = false;
			boolean setDate = false;
			
			if (fine.getMemberId() > 0 || fine.getAmount() >= 0 || fine.getLastFineDate().after(Date.valueOf("1900-01-01")))
			{
				sql += " WHERE ";
				
				if (fine.getMemberId() > 0)
				{
					sql += "Member_ID = ?";
					setMemberId = true;
				}
				
				if (fine.getAmount() >= 0)
				{
					if (setMemberId)
					{
						sql += " AND ";
					}
					
					sql += "Amount = ?";
					setAmount = true;
				}
				
				if (fine.getLastFineDate().after(Date.valueOf("1900-01-01")))
				{
					if (setMemberId || setAmount)
					{
						sql += " AND ";
					}
					
					sql += "LastFineDate = ?";
					setDate = true;
				}
			}
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			if (setMemberId)
			{
				pstmt.setInt(count, fine.getMemberId());
				count++;
			}
			
			if (setAmount)
			{
				pstmt.setDouble(count, fine.getAmount());
				count++;
			}
			
			if (setDate)
			{
				pstmt.setDate(count, fine.getLastFineDate());
				count++;
			}
			
			ResultSet rSet = pstmt.executeQuery();
			
			while (rSet.next())
			{	
			    int memberId = rSet.getInt("Member_ID");
			    double amount = rSet.getDouble("Amount");
			    Date lastFineDate = rSet.getDate("LastFineDate");
			    
			    Fines newFine = new Fines(memberId, amount, lastFineDate);
			    
			    fineList.add(newFine);
			}
			
			rSet.close();
			pstmt.close();
		}
		catch (SQLException se)
		{
			System.out.println("SQL Exception: " + se.getMessage());
			se.printStackTrace(System.out);
		}
		
		return fineList;
	}
	
	// delete one row in the Fines table
	public void delete(Fines fine)
	{	
		if (conn == null)
		{
			System.out.println("Connection error");
			return;
		}
		
		try
		{
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Fines WHERE Member_ID = ? AND Amount = ? AND LastFineDate = ?");
			
			pstmt.setInt(1, fine.getMemberId());
			pstmt.setDouble(2, fine.getAmount());
			pstmt.setDate(3, fine.getLastFineDate());
			
			pstmt.executeUpdate();
			pstmt.close();
		}
		catch (SQLException se)
		{
			System.out.println("SQL Exception: " + se.getMessage());
			se.printStackTrace(System.out);
		}
	}
}