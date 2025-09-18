package objects;

import java.sql.Date;

/*
 * 	Class to hold one instance of a borrow record from the BorrowRecords table
 * 	Setters and getters set up for each data value
 */

public class BorrowRecords
{
	private int bookId;
	private int memberId;
	private Date dueDate;
	
	public BorrowRecords (int bookId, int memberId, Date dueDate)
	{
		this.setBookId(bookId);
		this.setMemberId(memberId);
		this.setDueDate(dueDate);
	}

	public int getMemberId ()
	{
		return memberId;
	}

	public void setMemberId (int memberId)
	{
		this.memberId = memberId;
	}

	public int getBookId ()
	{
		return bookId;
	}

	public void setBookId (int bookId)
	{
		this.bookId = bookId;
	}

	public java.sql.Date getDueDate ()
	{
		return dueDate;
	}

	public void setDueDate (java.sql.Date dueDate)
	{
		this.dueDate = dueDate;
	}
}