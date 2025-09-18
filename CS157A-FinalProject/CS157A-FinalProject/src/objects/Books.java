package objects;

/*
 * 	Class to hold one instance of a book from the Books table
 * 	Setters and getters set up for each data value
 */

public class Books
{
	private int id;
	private String title;
	private String author;
	private int pageCount;
	private String genre;
	
	public Books (int id, String title, String author, int pageCount, String genre)
	{
		this.setId(id);
		this.setTitle(title);
		this.setAuthor(author);
		this.setPageCount(pageCount);
		this.setGenre(genre);
	}

	public int getId ()
	{
		return id;
	}

	public void setId (int id)
	{
		this.id = id;
	}

	public String getTitle ()
	{
		return title;
	}

	public void setTitle (String title)
	{
		this.title = title;
	}

	public String getAuthor ()
	{
		return author;
	}

	public void setAuthor (String author)
	{
		this.author = author;
	}

	public int getPageCount ()
	{
		return pageCount;
	}

	public void setPageCount (int pageCount)
	{
		this.pageCount = pageCount;
	}

	public String getGenre ()
	{
		return genre;
	}

	public void setGenre (String genre)
	{
		this.genre = genre;
	}
}
