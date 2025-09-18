package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dao.BooksDAO;
import dao.BorrowRecordsDAO;
import dao.FinesDAO;
import objects.Books;
import objects.BorrowRecords;
import objects.Fines;

public class MemberDashboard extends JFrame{
	// tables to display data and arrays to hold data
	private JTable memberBooksTable;
	private String[][] memberBookData;
	private String[] memberBooksColumnName = {"Id", "Title", "Author", "Page Count", "Genre", "Due Date"};
	private JTable booksTable;
	private String[][] bookData;
	private String[] booksColumnName = {"Id", "Title", "Author", "Page Count", "Genre", "Borrowed"};
	
	// member search and result fields
	private JTextField memberIDField;
	private JButton getMemberBooks;
	private JTextField amountOwedField;

	// book search fields
	private JTextField bookIDSearchField;
	private JTextField titleSearchField;
	private JTextField authorSearchField;
	private JTextField pageCountSearchField;
	private JTextField genreSearchField;
	private JCheckBox isBorrowedYes;
	private JCheckBox isBorrowedNo;
	private JButton searchBooks;
	
	// connection variables
	private Connection conn;
	private BooksDAO bDAO;
	private FinesDAO fDAO;
	private BorrowRecordsDAO brDAO;

	public MemberDashboard(Connection dbConn) {
		// basic set up
		super("Member Dashboard");
		
		// establish connections
		conn = dbConn;
		bDAO = new BooksDAO(conn);
		fDAO = new FinesDAO(conn);
		brDAO = new BorrowRecordsDAO(conn);

		// update table contents
		DefaultTableModel memberBooksModel = new DefaultTableModel(memberBookData, memberBooksColumnName); 
		DefaultTableModel bookTableModel = new DefaultTableModel(bookData, booksColumnName);

		this.memberBooksTable = new JTable(memberBooksModel);
		memberBooksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.booksTable = new JTable(bookTableModel);
		booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		setSize(1200, 800); 
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		// create components for Member's books Side
		JLabel yourBooksLabel = new JLabel("Your Books:");
		yourBooksLabel.setFont(new Font(Font.SERIF, Font.BOLD, 24));

		getMemberBooks = new JButton("Find my books!");
		getMemberBooks.addActionListener(new ButtonListener());
		
		JLabel memberIDLabel = new JLabel("Your Member ID:");
		memberIDField = new JTextField(15);
		memberIDField.setMaximumSize(new Dimension(200, 25));
		JLabel amountOwedLabel = new JLabel("You owe: $");
		amountOwedField = new JTextField(15);
		amountOwedField.setMaximumSize(new Dimension(200, 25));
		amountOwedField.setEditable(false);
		
		// create member's books Panel
		JPanel membersBooksPanel = new JPanel();
		membersBooksPanel.setLayout(new BoxLayout(membersBooksPanel, BoxLayout.Y_AXIS));

		// create getMembersBooks panel
		JPanel getMembersBooksPanel = new JPanel();
		getMembersBooksPanel.setLayout(new BoxLayout(getMembersBooksPanel, BoxLayout.Y_AXIS));
		getMembersBooksPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		getMembersBooksPanel.setBorder(BorderFactory.createTitledBorder("Find My Books"));

		// left-align components
		memberIDLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		memberIDField.setAlignmentX(Component.LEFT_ALIGNMENT);
		getMemberBooks.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// add components to panel
		getMembersBooksPanel.add(memberIDLabel);
		getMembersBooksPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		getMembersBooksPanel.add(memberIDField);
		getMembersBooksPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		getMembersBooksPanel.add(getMemberBooks);
		
		// create amountOwedPanel
		JPanel amountOwedPanel = new JPanel();
		amountOwedPanel.setLayout(new BoxLayout(amountOwedPanel, BoxLayout.Y_AXIS));
		amountOwedPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		amountOwedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		amountOwedField.setAlignmentX(Component.LEFT_ALIGNMENT);

		amountOwedPanel.add(amountOwedLabel);
		amountOwedPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		amountOwedPanel.add(amountOwedField);
		
		// add everything to members books panel
		membersBooksPanel.add(yourBooksLabel);
		membersBooksPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		membersBooksPanel.add(getMembersBooksPanel);
		membersBooksPanel.add(amountOwedPanel);
		membersBooksPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		membersBooksPanel.add(new JScrollPane(memberBooksTable));
		membersBooksPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		
		// Now create books side

		// create components for books side
		JLabel libraryBooksLabel = new JLabel("Library Books:");
		libraryBooksLabel.setFont(new Font(Font.SERIF, Font.BOLD, 24));

		JLabel bookIDLabel = new JLabel("Book ID:");
		bookIDSearchField = new JTextField(15);
		bookIDSearchField.setMaximumSize(new Dimension(200, 25));
		JLabel titleLabel = new JLabel("Title:");
		titleSearchField = new JTextField(15);
		titleSearchField.setMaximumSize(new Dimension(200, 25));
		JLabel authorLabel = new JLabel("Author:");
		authorSearchField = new JTextField(15);
		authorSearchField.setMaximumSize(new Dimension(200, 25));
		JLabel pageCountLabel = new JLabel("Page Count:");
		pageCountSearchField = new JTextField(15);
		pageCountSearchField.setMaximumSize(new Dimension(200, 25));
		JLabel genreLabel = new JLabel("Genre:");
		genreSearchField = new JTextField(15);
		genreSearchField.setMaximumSize(new Dimension(200, 25));
		JLabel isBorrowedLabel = new JLabel("Borrowed:");
		isBorrowedYes = new JCheckBox("Yes");
		isBorrowedNo = new JCheckBox("No");
		
		searchBooks = new JButton("Search!");
		searchBooks.addActionListener(new ButtonListener());

		// create books Panel
		JPanel booksPanel = new JPanel();
		booksPanel.setLayout(new BoxLayout(booksPanel, BoxLayout.Y_AXIS));

		// Create search panel for books
		JPanel bookSearchPanel = new JPanel();
		bookSearchPanel.setLayout(new BoxLayout(bookSearchPanel, BoxLayout.Y_AXIS));
		bookSearchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		bookSearchPanel.setBorder(BorderFactory.createTitledBorder("Search Book"));

		// Left-align components
		bookIDLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		bookIDSearchField.setAlignmentX(Component.LEFT_ALIGNMENT);
		titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		titleSearchField.setAlignmentX(Component.LEFT_ALIGNMENT);
		authorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		authorSearchField.setAlignmentX(Component.LEFT_ALIGNMENT);
		pageCountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		pageCountSearchField.setAlignmentX(Component.LEFT_ALIGNMENT);
		genreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		genreSearchField.setAlignmentX(Component.LEFT_ALIGNMENT);
		isBorrowedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		isBorrowedYes.setAlignmentX(Component.LEFT_ALIGNMENT);
		isBorrowedNo.setAlignmentX(Component.LEFT_ALIGNMENT);
		searchBooks.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Add components to bookSearchPanel
		bookSearchPanel.add(bookIDLabel);
		bookSearchPanel.add(bookIDSearchField);
		bookSearchPanel.add(titleLabel);
		bookSearchPanel.add(titleSearchField);
		bookSearchPanel.add(authorLabel);
		bookSearchPanel.add(authorSearchField);
		bookSearchPanel.add(pageCountLabel);
		bookSearchPanel.add(pageCountSearchField);
		bookSearchPanel.add(genreLabel);
		bookSearchPanel.add(genreSearchField);
		bookSearchPanel.add(Box.createVerticalStrut(10));
		bookSearchPanel.add(isBorrowedLabel);
		bookSearchPanel.add(isBorrowedYes);
		bookSearchPanel.add(isBorrowedNo);
		bookSearchPanel.add(Box.createVerticalStrut(10));
		bookSearchPanel.add(searchBooks);

		// Add everything to booksPanel
		booksPanel.add(libraryBooksLabel);
		booksPanel.add(bookSearchPanel);
		booksPanel.add(new JScrollPane(booksTable));

		// add panels to main frame
		JSplitPane layout = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, membersBooksPanel, booksPanel);
		layout.setDividerLocation(300);
		layout.setResizeWeight(0.4);
		this.add(layout);

		setVisible(true);
	}

	public class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// update table to display books that the current member are borrowing
			if (e.getSource().equals(getMemberBooks)) {
				int id = 0;
				
				if (!memberIDField.getText().replaceAll("\\s", "").isEmpty())
				{
					try
					{
						id = Integer.parseInt(memberIDField.getText().replaceAll("\\s", ""));
					}
					catch (NumberFormatException ne)
					{
						id = -1;
					}
				}
				
				if (id > 0)
				{
					ArrayList<BorrowRecords> brList = brDAO.select(new BorrowRecords(0, id, Date.valueOf("1899-01-01")));
					
					memberBookData = new String[brList.size()][6];
					
					for (int i = 0; i < brList.size(); i++)
					{
						ArrayList<Books> bookList = bDAO.select(new Books(brList.get(i).getBookId(), "", "", 0, ""));
						
						memberBookData[i][0] = Integer.toString(bookList.get(0).getId());
						memberBookData[i][1] = bookList.get(0).getTitle();
						memberBookData[i][2] = bookList.get(0).getAuthor();
						memberBookData[i][3] = Integer.toString(bookList.get(0).getPageCount());
						memberBookData[i][4] = bookList.get(0).getGenre();
						memberBookData[i][5] = brList.get(i).getDueDate().toString();
					}
					
					ArrayList<Fines> fineList = fDAO.select(new Fines(id, -1, Date.valueOf("1899-01-01")));
					
					if (fineList.size() > 0)
					{
						amountOwedField.setText(Double.toString(fineList.get(0).getAmount()));
					}
					else
					{
						amountOwedField.setText("");
					}
				}
				else
				{
					memberBookData = new String[0][5];
					amountOwedField.setText("");
				}
				
				memberBooksTable.setModel(new DefaultTableModel(memberBookData, memberBooksColumnName));
			}

			// update table to display books based on the search parameters
			else if (e.getSource().equals(searchBooks)) {
				int id = 0;
				
				if (!bookIDSearchField.getText().replaceAll("\\s", "").isEmpty())
				{
					try
					{
						id = Integer.parseInt(bookIDSearchField.getText().replaceAll("\\s", ""));
					}
					catch (NumberFormatException se)
					{
						id = -1;
					}
				}
				
				int pageCount = 0;
				
				if (!pageCountSearchField.getText().replaceAll("\\s", "").isEmpty())
				{
					try
					{
						pageCount = Integer.parseInt(pageCountSearchField.getText().replaceAll("\\s", ""));
					}
					catch (NumberFormatException se)
					{
						pageCount = -1;
					}
				}
				
				if (id >= 0 && pageCount >= 0)
				{
					ArrayList<BorrowRecords> brList = brDAO.select(new BorrowRecords(0, 0, Date.valueOf("1899-01-01")));
					
					HashMap<Integer, Integer> brMap = new HashMap<>();
					
					for (int i = 0; i < brList.size(); i++)
					{
						brMap.put(brList.get(i).getBookId(), brList.get(i).getMemberId());
					}
					
					String title = titleSearchField.getText().trim();
					String author = authorSearchField.getText().trim();
					String genre = genreSearchField.getText().trim();
					
					ArrayList<Books> bookList = bDAO.select(new Books(id, title, author, pageCount, genre));
					
					bookData = new String[bookList.size()][6];
					
					int numRows = bookList.size();
					int insertIndex = 0;
					int getIndex = 0;
					
					while (insertIndex < numRows)
					{
						if (isBorrowedYes.isSelected())
						{
							if (brMap.containsKey(bookList.get(getIndex).getId()))
							{
								bookData[insertIndex][0] = Integer.toString(bookList.get(getIndex).getId());
								bookData[insertIndex][1] = bookList.get(getIndex).getTitle();
								bookData[insertIndex][2] = bookList.get(getIndex).getAuthor();
								bookData[insertIndex][3] = Integer.toString(bookList.get(getIndex).getPageCount());
								bookData[insertIndex][4] = bookList.get(getIndex).getGenre();
								bookData[insertIndex][5] = "Yes";
							}
							else
							{
								insertIndex--;
								numRows--;
							}
						}
						else if (isBorrowedNo.isSelected())
						{
							if (!brMap.containsKey(bookList.get(getIndex).getId()))
							{
								bookData[insertIndex][0] = Integer.toString(bookList.get(getIndex).getId());
								bookData[insertIndex][1] = bookList.get(getIndex).getTitle();
								bookData[insertIndex][2] = bookList.get(getIndex).getAuthor();
								bookData[insertIndex][3] = Integer.toString(bookList.get(getIndex).getPageCount());
								bookData[insertIndex][4] = bookList.get(getIndex).getGenre();
								bookData[insertIndex][5] = "No";
							}
							else
							{
								insertIndex--;
								numRows--;
							}
						}
						else if (!isBorrowedYes.isSelected() && !isBorrowedNo.isSelected())
						{
							bookData[insertIndex][0] = Integer.toString(bookList.get(getIndex).getId());
							bookData[insertIndex][1] = bookList.get(getIndex).getTitle();
							bookData[insertIndex][2] = bookList.get(getIndex).getAuthor();
							bookData[insertIndex][3] = Integer.toString(bookList.get(getIndex).getPageCount());
							bookData[insertIndex][4] = bookList.get(getIndex).getGenre();
							
							if (brMap.containsKey(bookList.get(getIndex).getId()))
							{
								bookData[insertIndex][5] = "Yes";
							}
							else
							{
								bookData[insertIndex][5] = "No";
							}
						}
						
						insertIndex++;
						getIndex++;
					}
				}
				else
				{
					bookData = new String[0][6];
				}
				
				booksTable.setModel(new DefaultTableModel(bookData, booksColumnName));
			}
			
		}
	}
}
