package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import objects.BorrowRecords;

/*
 * 	DAO class controls inputs from UI to the database
 */

public class BorrowRecordsDAO
{
	private Connection conn = null;
	
	public BorrowRecordsDAO(Connection dbConnection)
	{
		conn = dbConnection;
	}
	
	// insert into the BorrowRecords table
	public void insert(BorrowRecords borrowRecord)
	{
		if (conn == null)
		{
			System.out.println("Connection error");
			return;
		}
		
		try
		{
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO BorrowRecords values(?,?,?)");
			
			pstmt.setInt(1, borrowRecord.getBookId());
			pstmt.setInt(2, borrowRecord.getMemberId());
			pstmt.setDate(3, borrowRecord.getDueDate());
			
			pstmt.executeUpdate();
			pstmt.close();
		}
		catch (SQLException se)
		{
			System.out.println("SQL Exception: " + se.getMessage());
			se.printStackTrace(System.out);
		}
	}
	
	// update a row in the BorrowRecords table
	public void update(BorrowRecords borrowRecord)
	{
		if (conn == null)
		{
			System.out.println("Connection error");
			return;
		}
		
		try
		{
			String sql = "UPDATE BorrowRecords SET ";
			int count = 1;
			boolean setMemberId = false;
			boolean setDueDate = false;
			
			if (borrowRecord.getMemberId() > 0)
			{	
				sql += "Member_ID = ?";
				setMemberId = true;
			}
			
			if (borrowRecord.getDueDate().after(Date.valueOf("1900-01-01")))
			{
				if (setMemberId)
				{
					sql += ", ";
				}
				
				sql += "DueDate = ?";
				setDueDate = true;
			}
			
			sql += " WHERE Book_ID = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			if (setMemberId)
			{
				pstmt.setInt(count, borrowRecord.getMemberId());
				count++;
			}
			
			if (setDueDate)
			{
				pstmt.setDate(count, borrowRecord.getDueDate());
				count++;
			}
			
			pstmt.setInt(count, borrowRecord.getBookId());
			
			pstmt.executeUpdate();
			pstmt.close();
		}
		catch (SQLException se)
		{
			System.out.println("SQL Exception: " + se.getMessage());
			se.printStackTrace(System.out);
		}
	}
	
	// select a row or all rows in the BorrowRecords table
	public ArrayList<BorrowRecords> select(BorrowRecords borrowRecord)
	{
		ArrayList<BorrowRecords> borrowRecordList = new ArrayList<>();
		
		if (conn == null)
		{
			System.out.println("Connection error");
			return null;
		}
		
		try
		{
			String sql = "SELECT * FROM BorrowRecords";
			int count = 1;
			boolean setBookId = false;
			boolean setMemberId = false;
			boolean setDueDate = false;
			
			if (borrowRecord.getBookId() > 0 || borrowRecord.getMemberId() > 0 || borrowRecord.getDueDate().after(Date.valueOf("1900-01-01")))
			{
				sql += " WHERE ";
				
				if (borrowRecord.getBookId() > 0)
				{
					sql += "Book_ID = ?";
					setBookId = true;
				}
				
				if (borrowRecord.getMemberId() > 0)
				{
					if (setBookId)
					{
						sql += " AND ";
					}
					
					sql += "Member_ID = ?";
					setMemberId = true;
				}
				
				if (borrowRecord.getDueDate().after(Date.valueOf("1900-01-01")))
				{
					if (setBookId || setMemberId)
					{
						sql += " AND ";
					}
					
					sql += "DueDate = ?";
					setDueDate = true;
				}
			}

			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			if (setBookId)
			{
				pstmt.setInt(count, borrowRecord.getBookId());
				count++;
			}
			
			if (setMemberId)
			{
				pstmt.setInt(count, borrowRecord.getMemberId());
				count++;
			}
			
			if (setDueDate)
			{
				pstmt.setDate(count, borrowRecord.getDueDate());
				count++;
			}
			
			ResultSet rSet = pstmt.executeQuery();
			
			while (rSet.next())
			{	
			    int bookId = rSet.getInt("Book_ID");
			    int memberId = rSet.getInt("Member_ID");
			    Date dueDate = rSet.getDate("DueDate");
			    
			    BorrowRecords newBorrowRecord = new BorrowRecords(bookId, memberId, dueDate);
			    
			    borrowRecordList.add(newBorrowRecord);
			}
			
			rSet.close();
			pstmt.close();
		}
		catch (SQLException se)
		{
			System.out.println("SQL Exception: " + se.getMessage());
			se.printStackTrace(System.out);
		}
		
		return borrowRecordList;
	}
	
	// delete a specific row in the BorrowRecords
	public void delete(BorrowRecords borrowRecord)
	{	
		if (conn == null)
		{
			System.out.println("Connection error");
			return;
		}
		
		try
		{
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM BorrowRecords WHERE Book_ID = ? AND Member_ID = ? AND DueDate = ?");
			
			pstmt.setInt(1, borrowRecord.getBookId());
			pstmt.setInt(2, borrowRecord.getMemberId());
			pstmt.setDate(3, borrowRecord.getDueDate());
			
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