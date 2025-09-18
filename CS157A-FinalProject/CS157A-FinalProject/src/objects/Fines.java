package objects;

import java.sql.Date;

/*
 * 	Class to hold one instance of a fine from the Fines table
 * 	Setters and getters set up for each data value
 */

public class Fines
{
	private int memberId;
	private double amount;
	private Date lastFineDate;
	
	public Fines (int memberId, double amount, Date lastFineDate)
	{
		this.setMemberId(memberId);
		this.setAmount(amount);
		this.setLastFineDate(lastFineDate);
	}

	public int getMemberId ()
	{
		return memberId;
	}

	public void setMemberId (int memberId)
	{
		this.memberId = memberId;
	}

	public double getAmount ()
	{
		return amount;
	}

	public void setAmount (double amount)
	{
		this.amount = amount;
	}

	public Date getLastFineDate ()
	{
		return lastFineDate;
	}

	public void setLastFineDate (Date lastFineDate)
	{
		this.lastFineDate = lastFineDate;
	}
}