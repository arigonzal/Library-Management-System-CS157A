package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import objects.Members;

/*
 * 	DAO class controls inputs from UI to the database
 */

public class MembersDAO
{
	private Connection conn = null;
	
	public MembersDAO(Connection dbConnection)
	{
		conn = dbConnection;
	}
	
	// insert a row into the Members table
	public void insert(Members mem)
	{
		if (conn == null)
		{
			System.out.println("Connection error");
			return;
		}
		
		try
		{
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Members (FirstName, LastName) values(?,?)");
			
			pstmt.setString(1, mem.getFirstName());
			pstmt.setString(2, mem.getLastName());
			
			pstmt.executeUpdate();
			pstmt.close();
		}
		catch (SQLException se)
		{
			System.out.println("SQL Exception: " + se.getMessage());
			se.printStackTrace(System.out);
		}
	}
	
	// update one row in the Members table
	public void update(Members mem)
	{
		if (conn == null)
		{
			System.out.println("Connection error");
			return;
		}
		
		try
		{
			String sql = "UPDATE Members SET ";
			int count = 1;
			boolean setFirstName = false;
			boolean setLastName = false;
			
			if (!mem.getFirstName().isEmpty())
			{
				sql += "FirstName = ?";
				setFirstName = true;
			}
			
			if (!mem.getLastName().isEmpty())
			{
				if (setFirstName)
				{
					sql += ", ";
				}
				
				sql += "LastName = ?";
				setLastName = true;
			}
			
			sql += "WHERE Member_ID = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			if (setFirstName)
			{
				pstmt.setString(count, mem.getFirstName());
				count++;
			}
			
			if (setLastName)
			{
				pstmt.setString(count, mem.getLastName());
				count++;
			}
			
			pstmt.setInt(count, mem.getId());
			
			pstmt.executeUpdate();
			pstmt.close();
		}
		catch (SQLException se)
		{
			System.out.println("SQL Exception: " + se.getMessage());
			se.printStackTrace(System.out);
		}
	}
	
	// select one or all rows in the Members table
	public ArrayList<Members> select(Members member)
	{
		ArrayList<Members> memberList = new ArrayList<>();
		
		if (conn == null)
		{
			System.out.println("Connection error");
			return null;
		}
		
		try
		{
			String sql = "SELECT * FROM Members";
			int count = 1;
			boolean setId = false;
			boolean setFirstName = false;
			boolean setLastName = false;
			
			if (member.getId() > 0 || !member.getFirstName().isEmpty() || !member.getLastName().isEmpty())
			{	
				sql += " WHERE ";
				
				if (member.getId() > 0)
				{
					sql += "Member_ID = ?";
					setId = true;
				}
				
				if (!member.getFirstName().isEmpty())
				{
					if (setId)
					{
						sql += " AND ";
					}
					
					sql += "FirstName = ?";
					setFirstName = true;
				}
				
				if (!member.getLastName().isEmpty())
				{
					if (setId || setFirstName)
					{
						sql += " AND ";
					}
					
					sql += "LastName = ?";
					setLastName = true;
				}
			}
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			if (setId)
			{
				pstmt.setInt(count, member.getId());
				count++;
			}
			
			if (setFirstName)
			{
				pstmt.setString(count, member.getFirstName());
				count++;
			}
			
			if (setLastName)
			{
				pstmt.setString(count, member.getLastName());
				count++;
			}
			
			ResultSet rSet = pstmt.executeQuery();
			
			while (rSet.next())
			{	
			    int memberId = rSet.getInt("Member_ID");
			    String firstName = rSet.getString("FirstName");
			    String lastName = rSet.getString("LastName");

			    Members newMember = new Members(memberId, firstName, lastName);
			    
			    memberList.add(newMember);
			}
			
			rSet.close();
			pstmt.close();
		}
		catch (SQLException se)
		{
			System.out.println("SQL Exception: " + se.getMessage());
			se.printStackTrace(System.out);
		}
		
		return memberList;
	}
	
	// delete one row in the Members table
	public void delete(Members member)
	{
		if (conn == null)
		{
			System.out.println("Connection error");
			return;
		}
		
		try
		{
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Members WHERE Member_ID = ? AND FirstName = ? AND LastName = ?");
			
			pstmt.setInt(1, member.getId());
			pstmt.setString(2, member.getFirstName());
			pstmt.setString(3, member.getLastName());
			
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