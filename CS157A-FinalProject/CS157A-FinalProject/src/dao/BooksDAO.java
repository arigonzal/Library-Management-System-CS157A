package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import objects.Books;

/*
 * 	DAO class controls inputs from UI to the database
 */

public class BooksDAO
{
	private Connection conn = null;
	
	public BooksDAO(Connection dbConnection)
	{
		conn = dbConnection;
	}
	
	// insert into the Books table
	public void insert(Books book)
	{
		if (conn == null)
		{
			System.out.println("Connection error");
			return;
		}
		
		try
		{
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Books (Title, Author, PageCount, Genre) values(?,?,?,?)");
			
			pstmt.setString(1, book.getTitle());
			pstmt.setString(2, book.getAuthor());
			pstmt.setInt(3, book.getPageCount());
			pstmt.setString(4, book.getGenre());
			
			pstmt.executeUpdate();
			pstmt.close();
		}
		catch (SQLException se)
		{
			System.out.println("SQL Exception: " + se.getMessage());
			se.printStackTrace(System.out);
		}
	}
	
	// update a row in the Books table
	public void update(Books book)
	{
		if (conn == null)
		{
			System.out.println("Connection error");
			return;
		}
		
		try
		{
			String sql = "UPDATE Books SET ";
			int count = 1;
			boolean setTitle = false;
			boolean setAuthor = false;
			boolean setPageCount = false;
			boolean setGenre = false;
			
			if (!book.getTitle().isEmpty())
			{
				sql += "Title = ?";
				setTitle = true;
			}
			
			if (!book.getAuthor().isEmpty())
			{
				if (setTitle)
				{
					sql += ", ";
				}
				
				sql += "Author = ?";
				setAuthor = true;
			}
			
			if (book.getPageCount() > 0)
			{
				if (setTitle || setAuthor)
				{
					sql += ", ";
				}
				
				sql += "PageCount = ?";
				setPageCount = true;
			}
			
			if (!book.getGenre().isEmpty())
			{
				if (setTitle || setAuthor || setPageCount)
				{
					sql += ", ";
				}
				
				sql += "Genre = ?";
				setGenre = true;
			}
			
			sql += " WHERE Book_ID = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			if (setTitle)
			{
				pstmt.setString(count, book.getTitle());
				count++;
			}
			
			if (setAuthor)
			{
				pstmt.setString(count, book.getAuthor());
				count++;
			}
			
			if (setPageCount)
			{
				pstmt.setInt(count, book.getPageCount());
				count++;
			}
			
			if (setGenre)
			{
				pstmt.setString(count, book.getGenre());
				count++;
			}
			
			pstmt.setInt(count, book.getId());
			
			pstmt.executeUpdate();
			pstmt.close();
		}
		catch (SQLException se)
		{
			System.out.println("SQL Exception: " + se.getMessage());
			se.printStackTrace(System.out);
		}
	}
	
	// select a specific or all rows in the Books table
	public ArrayList<Books> select(Books book)
	{
		ArrayList<Books> bookList = new ArrayList<>();
		
		if (conn == null)
		{
			System.out.println("Connection error");
			return null;
		}
		
		try
		{
			String sql = "SELECT * FROM Books";
			int count = 1;
			boolean setId = false;
			boolean setTitle = false;
			boolean setAuthor = false;
			boolean setPageCount = false;
			boolean setGenre = false;
			
			if (book.getId() > 0 || !book.getTitle().isEmpty() || !book.getAuthor().isEmpty() || book.getPageCount() > 0 || !book.getGenre().isEmpty())
			{	
				sql += " WHERE ";
				
				if (book.getId() > 0)
				{
					sql += "Book_ID = ?";
					setId = true;
				}
				
				if (!book.getTitle().isEmpty())
				{
					if (setId)
					{
						sql += " AND ";
					}
					
					sql += "Title = ?";
					setTitle = true;
				}
				
				if (!book.getAuthor().isEmpty())
				{
					if (setId || setTitle)
					{
						sql += " AND ";
					}
					
					sql += "Author = ?";
					setAuthor = true;
				}
				
				if (book.getPageCount() > 0)
				{
					if (setId || setTitle || setAuthor)
					{
						sql += " AND ";
					}
					
					sql += "PageCount = ?";
					setPageCount = true;
				}
				
				if (!book.getGenre().isEmpty())
				{
					if (setId || setTitle || setAuthor || setPageCount)
					{
						sql += " AND ";
					}
					
					sql += "Genre = ?";
					setGenre = true;
				}
			}
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			if (setId)
			{
				pstmt.setInt(count, book.getId());
				count++;
			}
			
			if (setTitle)
			{
				pstmt.setString(count, book.getTitle());
				count++;
			}
			
			if (setAuthor)
			{
				pstmt.setString(count, book.getAuthor());
				count++;
			}
			
			if (setPageCount)
			{
				pstmt.setInt(count, book.getPageCount());
				count++;
			}
			
			if (setGenre)
			{
				pstmt.setString(count, book.getGenre());
				count++;
			}
			
			ResultSet rSet = pstmt.executeQuery();
			
			while (rSet.next())
			{	
			    int bookId = rSet.getInt("Book_ID");
			    String title = rSet.getString("Title");
			    String author = rSet.getString("Author");
			    int pageCount = rSet.getInt("PageCount");
			    String genre = rSet.getString("Genre");
			    
			    Books newBook = new Books(bookId, title, author, pageCount, genre);
			    
			    bookList.add(newBook);
			}
			
			rSet.close();
			pstmt.close();
		}
		catch (SQLException se)
		{
			System.out.println("SQL Exception: " + se.getMessage());
			se.printStackTrace(System.out);
		}
		
		return bookList;
	}
	
	// delete a specific book row in the Books table
	public void delete(Books book)
	{	
		if (conn == null)
		{
			System.out.println("Connection error");
			return;
		}
		
		try
		{
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Books WHERE Book_ID = ? AND Title = ? AND Author = ? AND PageCount = ? AND Genre = ?");
			
			pstmt.setInt(1, book.getId());
			pstmt.setString(2, book.getTitle());
			pstmt.setString(3, book.getAuthor());
			pstmt.setInt(4, book.getPageCount());
			pstmt.setString(5, book.getGenre());
			
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