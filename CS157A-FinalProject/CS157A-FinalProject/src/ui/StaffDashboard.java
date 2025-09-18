package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dao.BooksDAO;
import dao.BorrowRecordsDAO;
import dao.FinesDAO;
import dao.MembersDAO;
import objects.Books;
import objects.BorrowRecords;
import objects.Fines;
import objects.Members;

public class StaffDashboard extends JFrame{
	// tables to display data and arrays to hold data
	private JTable membersTable;
	private String[][] memberData;
	private String[] membersColumnName = {"Id", "First Name", "Last Name", "Fine Amount"};
	
	private JTable booksTable;
	private String[][] bookData;
	private String[] booksColumnName = {"Id", "Title", "Author", "Page Count", "Genre", "Borrowed By Mem. Id", "Overdue"};

	// Buttons
	private JButton addMember;
	private JButton addBook;

	private JButton editMember;
	private JButton editBook;

	private JButton deleteMember;
	private JButton deleteBook;

	private JButton checkOutBook;
	private JButton returnBook;

	// book search parameters
	private JTextField bookIDSearchField;
	private JTextField titleSearchField;
	private JTextField authorSearchField;
	private JTextField pageCountSearchField;
	private JTextField genreSearchField;
	private JCheckBox isBorrowedYes;
	private JCheckBox isBorrowedNo;

	private JButton searchBooks;

	// member search parameters
	private JTextField memberIDSearchField;
	private JTextField firstNameSearchField;
	private JTextField lastNameSearchField;
	private JCheckBox hasFinesYes;
	private JCheckBox hasFinesNo;

	private JButton searchMembers;
	
	// variables that hold database connection and perform operations
	private Connection conn;
	private MembersDAO mDAO;
	private BooksDAO bDAO;
	private FinesDAO fDAO;
	private BorrowRecordsDAO brDAO;

	public StaffDashboard(Connection dbConn) {
		// basic set up
		super("Staff Dashboard");
		
		// setup connections
		conn = dbConn;
		mDAO = new MembersDAO(conn);
		bDAO = new BooksDAO(conn);
		fDAO = new FinesDAO(conn);
		brDAO = new BorrowRecordsDAO(conn);
		
		// fill tables
		DefaultTableModel bookTableModel = new DefaultTableModel(bookData, booksColumnName);
		DefaultTableModel memberTableModel = new DefaultTableModel(memberData, membersColumnName);

		setSize(1200, 800);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		// create components for Member Side
		JLabel membersLabel = new JLabel("Members:");
		membersLabel.setFont(new Font(Font.SERIF, Font.BOLD, 24));

		addMember = new JButton("Add Member");
		addMember.addActionListener(new ButtonListener());

		JLabel memberIDLabel = new JLabel("Member ID:");
		memberIDSearchField = new JTextField(15);
		memberIDSearchField.setMaximumSize(new Dimension(200, 25));
		JLabel firstNameLabel = new JLabel("First Name:");
		firstNameSearchField = new JTextField(15);
		firstNameSearchField.setMaximumSize(new Dimension(200, 25));
		JLabel lastNameLabel = new JLabel("Last Name:");
		lastNameSearchField = new JTextField(15);
		lastNameSearchField.setMaximumSize(new Dimension(200, 25));
		JLabel hasFinesLabel = new JLabel("Has Fines:");
		hasFinesYes = new JCheckBox("Yes");
		hasFinesNo = new JCheckBox("No");

		this.membersTable = new JTable(memberTableModel);
		membersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.booksTable = new JTable(bookTableModel);
		booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		searchMembers = new JButton("Search!");
		searchMembers.addActionListener(new ButtonListener());
		editMember = new JButton("Edit Member");
		editMember.addActionListener(new ButtonListener());
		deleteMember = new JButton("Delete Member");
		deleteMember.addActionListener(new ButtonListener());

		// create member Panel
		JPanel membersPanel = new JPanel();
		membersPanel.setLayout(new BoxLayout(membersPanel, BoxLayout.Y_AXIS));

		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
		searchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		searchPanel.setBorder(BorderFactory.createTitledBorder("Search Member"));

		// Left-align and add components directly to the searchPanel
		memberIDLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		memberIDSearchField.setAlignmentX(Component.LEFT_ALIGNMENT);
		firstNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		firstNameSearchField.setAlignmentX(Component.LEFT_ALIGNMENT);
		lastNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		lastNameSearchField.setAlignmentX(Component.LEFT_ALIGNMENT);
		hasFinesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		hasFinesYes.setAlignmentX(Component.LEFT_ALIGNMENT);
		hasFinesNo.setAlignmentX(Component.LEFT_ALIGNMENT);
		searchMembers.setAlignmentX(Component.LEFT_ALIGNMENT);

		// create action Panel that will go under table
		JPanel memberActionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		memberActionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		memberActionPanel.add(editMember);
		memberActionPanel.add(deleteMember);	

		// Add components in a column, directly to searchPanel
		searchPanel.add(memberIDLabel);
		searchPanel.add(memberIDSearchField);
		searchPanel.add(firstNameLabel);
		searchPanel.add(firstNameSearchField);
		searchPanel.add(lastNameLabel);
		searchPanel.add(lastNameSearchField);
		searchPanel.add(Box.createVerticalStrut(10)); // spacer
		searchPanel.add(hasFinesLabel);
		searchPanel.add(hasFinesYes);
		searchPanel.add(hasFinesNo);
		searchPanel.add(Box.createVerticalStrut(10)); // spacer
		searchPanel.add(searchMembers);

		membersPanel.add(membersLabel);
		membersPanel.add(addMember);
		membersPanel.add(searchPanel);
		membersPanel.add(new JScrollPane(membersTable));
		membersPanel.add(memberActionPanel);

		// Now create books side

		// create components for books side
		JLabel booksLabel = new JLabel("Books:");
		booksLabel.setFont(new Font(Font.SERIF, Font.BOLD, 24));

		addBook = new JButton("Add Book");
		addBook.addActionListener(new ButtonListener());
		checkOutBook = new JButton("Check out a book!");
		checkOutBook.addActionListener(new ButtonListener());
		returnBook = new JButton("Return a book!");
		returnBook.addActionListener(new ButtonListener());

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
		editBook = new JButton("Edit Book");
		editBook.addActionListener(new ButtonListener());
		deleteBook = new JButton("Delete Book");
		deleteBook.addActionListener(new ButtonListener());


		JPanel booksPanel = new JPanel();
		booksPanel.setLayout(new BoxLayout(booksPanel, BoxLayout.Y_AXIS));

		// Create search panel for books
		JPanel bookSearchPanel = new JPanel();
		bookSearchPanel.setLayout(new BoxLayout(bookSearchPanel, BoxLayout.Y_AXIS));
		bookSearchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		bookSearchPanel.setBorder(BorderFactory.createTitledBorder("Search Book"));

		// Left-align labels and inputs
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

		// Create edit and delete panel
		JPanel bookActionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		bookActionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		bookActionPanel.add(editBook);
		bookActionPanel.add(deleteBook);

		// Add everything to booksPanel
		booksPanel.add(booksLabel);
		booksPanel.add(addBook);
		booksPanel.add(checkOutBook);
		booksPanel.add(returnBook);
		booksPanel.add(bookSearchPanel);
		booksPanel.add(new JScrollPane(booksTable));
		booksPanel.add(bookActionPanel);

		// add panels to main frame		
		JSplitPane layout = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, membersPanel, booksPanel);
		layout.setDividerLocation(300);
		layout.setResizeWeight(0.4);
		this.add(layout);

		setVisible(true);
	}

	public class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// pop up panel for adding a new member
			if (e.getSource().equals(addMember)) {
			    JTextField firstNameField = new JTextField();
			    firstNameField.setPreferredSize(new Dimension(150, 25));
			    JTextField lastNameField = new JTextField();
			    lastNameField.setPreferredSize(new Dimension(150, 25));

			    // Panel for dialog
			    JPanel panel = new JPanel();
			    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			    panel.add(new JLabel("First Name:"));
			    panel.add(firstNameField);
			    panel.add(new JLabel("Last Name:"));
			    panel.add(lastNameField);

			    int result = JOptionPane.showConfirmDialog(null, panel, "Add New Member", JOptionPane.OK_CANCEL_OPTION);

			    if (result == JOptionPane.OK_OPTION) {
			    	Members mem = new Members(0, firstNameField.getText(), lastNameField.getText());
			    	mDAO.insert(mem);
			    }
			} 

			// pop up panel for adding a new book
			else if (e.getSource().equals(addBook)) {
				// Create editable fields
			    JTextField titleField = new JTextField();
			    titleField.setPreferredSize(new Dimension(150, 25));
			    JTextField authorField = new JTextField();
			    authorField.setPreferredSize(new Dimension(150, 25));
			    JTextField pageCountField = new JTextField();
			    pageCountField.setPreferredSize(new Dimension(150, 25));
			    JTextField genreField = new JTextField();
			    genreField.setPreferredSize(new Dimension(150, 25));

			    // Panel for dialog
			    JPanel panel = new JPanel();
			    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			    panel.add(new JLabel("Title:"));
			    panel.add(titleField);
			    panel.add(new JLabel("Author:"));
			    panel.add(authorField);
			    panel.add(new JLabel("Page Count:"));
			    panel.add(pageCountField);
			    panel.add(new JLabel("Genre:"));
			    panel.add(genreField);

			    int result = JOptionPane.showConfirmDialog(null, panel, "Add New Book", JOptionPane.OK_CANCEL_OPTION);

		    	if (result == JOptionPane.OK_OPTION) {
			    	int newPageCount = 0;
			    	
			    	if (!pageCountField.getText().replaceAll("\\s", "").isEmpty())
			    	{
			    		try
			    		{
			    			newPageCount = Integer.parseInt(pageCountField.getText().replaceAll("\\s", ""));
			    		}
			    		catch (NumberFormatException ne)
			    		{
			    			JOptionPane.showMessageDialog(null, "Invalid page count. Try again");
			    			return;
			    		}
			    	}
			    	
		    		Books book = new Books(0, titleField.getText(), authorField.getText(), newPageCount, genreField.getText());
		    		bDAO.insert(book);
			    }
			}

			// pop up panel for updating a member row
			else if (e.getSource().equals(editMember)) {
				int selectedRow = membersTable.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(StaffDashboard.this, "Please select a member to edit.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				try {
					// Get existing values
					int id = Integer.parseInt(membersTable.getValueAt(selectedRow, 0).toString());
					String firstName = membersTable.getValueAt(selectedRow, 1).toString();
					String lastName = membersTable.getValueAt(selectedRow, 2).toString();
					String fineAmount = membersTable.getValueAt(selectedRow, 3).toString();

					// Create editable fields
					JTextField firstNameField = new JTextField(firstName);
					firstNameField.setPreferredSize(new Dimension(150, 25));
					JTextField lastNameField = new JTextField(lastName);
					lastNameField.setPreferredSize(new Dimension(150, 25));
					JTextField fineAmountField = new JTextField(fineAmount);
					fineAmountField.setPreferredSize(new Dimension(150, 25));

					// Panel for dialog
					JPanel panel = new JPanel();
					panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
					panel.add(new JLabel("First Name:"));
					panel.add(firstNameField);
					panel.add(new JLabel("Last Name:"));
					panel.add(lastNameField);
					panel.add(new JLabel("Fine Amount:"));
					panel.add(fineAmountField);

					int result = JOptionPane.showConfirmDialog(null, panel, "Edit Member", JOptionPane.OK_CANCEL_OPTION);

					if (result == JOptionPane.OK_OPTION) {
						mDAO.update(new Members(id, firstNameField.getText(), lastNameField.getText()));
						fDAO.update(new Fines(id, Double.parseDouble(fineAmountField.getText()), Date.valueOf("1899-01-01")));
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(StaffDashboard.this, "Could not edit member.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

			// pop up panel for updating a book row
			else if (e.getSource().equals(editBook)) {
				int selectedRow = booksTable.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(StaffDashboard.this, "Please select a book to edit.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				try {
					// Get existing values
					int id = Integer.parseInt(booksTable.getValueAt(selectedRow, 0).toString());
					String title = booksTable.getValueAt(selectedRow, 1).toString();
					String author = booksTable.getValueAt(selectedRow, 2).toString();
					String pageCount = booksTable.getValueAt(selectedRow, 3).toString();
					String genre = booksTable.getValueAt(selectedRow, 4).toString();

					// Create editable fields
					JTextField titleField = new JTextField(title);
					titleField.setPreferredSize(new Dimension(150, 25));
					JTextField authorField = new JTextField(author);
					authorField.setPreferredSize(new Dimension(150, 25));
					JTextField pageCountField = new JTextField(pageCount);
					pageCountField.setPreferredSize(new Dimension(150, 25));
					JTextField genreField = new JTextField(genre);
					genreField.setPreferredSize(new Dimension(150, 25));

					// Panel for dialog
					JPanel panel = new JPanel();
					panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
					panel.add(new JLabel("Title:"));
					panel.add(titleField);
					panel.add(new JLabel("Author:"));
					panel.add(authorField);
					panel.add(new JLabel("Page Count:"));
					panel.add(pageCountField);
					panel.add(new JLabel("Genre:"));
					panel.add(genreField);

					int result = JOptionPane.showConfirmDialog(null, panel, "Edit Book", JOptionPane.OK_CANCEL_OPTION);

					if (result == JOptionPane.OK_OPTION) {
						bDAO.update(new Books(id, titleField.getText(), authorField.getText(), Integer.parseInt(pageCountField.getText()), genreField.getText()));
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(StaffDashboard.this, "Could not edit book.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

			// pop up panel for deleting a member row
			else if (e.getSource().equals(deleteMember)) {
				int selectedRow = membersTable.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(StaffDashboard.this, "Please select a member to delete.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				else {
					var success = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this member?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
					if (success == 0) { // if deletion is confirmed
						int id = Integer.parseInt(membersTable.getValueAt(selectedRow, 0).toString());
						String firstName = membersTable.getValueAt(selectedRow, 1).toString();
						String lastName = membersTable.getValueAt(selectedRow, 2).toString();
						
						mDAO.delete(new Members(id, firstName, lastName));
					}
				}
			}

			// pop up panel for deleting a book row
			else if (e.getSource().equals(deleteBook)) {
				int selectedRow = booksTable.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(StaffDashboard.this, "Please select a book to delete.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				else {
					var success = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this book?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
					if (success == 0) { // if deletion is confirmed
						int id = Integer.parseInt(booksTable.getValueAt(selectedRow, 0).toString());
						String title = booksTable.getValueAt(selectedRow, 1).toString();
						String author = booksTable.getValueAt(selectedRow, 2).toString();
						int pageCount = Integer.parseInt(booksTable.getValueAt(selectedRow, 3).toString());
						String genre = booksTable.getValueAt(selectedRow, 4).toString();
						
						bDAO.delete(new Books(id, title, author, pageCount, genre));
					}
				}
			}

			// updating the search parameters and querying the database
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
					
					HashMap<Integer, Integer> brMemberMap = new HashMap<>();
					HashMap<Integer, Date> brDateMap = new HashMap<>();
					
					for (int i = 0; i < brList.size(); i++)
					{
						brMemberMap.put(brList.get(i).getBookId(), brList.get(i).getMemberId());
						brDateMap.put(brList.get(i).getBookId(), brList.get(i).getDueDate());
					}
					
					String title = titleSearchField.getText().trim();
					String author = authorSearchField.getText().trim();
					String genre = genreSearchField.getText().trim();
					
					ArrayList<Books> bookList = bDAO.select(new Books(id, title, author, pageCount, genre));
					
					bookData = new String[bookList.size()][7];
					
					int numRows = bookList.size();
					int insertIndex = 0;
					int getIndex = 0;
					
					while (insertIndex < numRows)
					{
						if (isBorrowedYes.isSelected())
						{
							if (brMemberMap.containsKey(bookList.get(getIndex).getId()))
							{
								bookData[insertIndex][0] = Integer.toString(bookList.get(getIndex).getId());
								bookData[insertIndex][1] = bookList.get(getIndex).getTitle();
								bookData[insertIndex][2] = bookList.get(getIndex).getAuthor();
								bookData[insertIndex][3] = Integer.toString(bookList.get(getIndex).getPageCount());
								bookData[insertIndex][4] = bookList.get(getIndex).getGenre();
								bookData[insertIndex][5] = Integer.toString(brMemberMap.get(bookList.get(getIndex).getId()));
								if ((new Date(System.currentTimeMillis())).after(brDateMap.get(bookList.get(getIndex).getId())))
								{
									bookData[insertIndex][6] = "Yes";
								}
								else
								{
									bookData[insertIndex][6] = "No";
								}
								
							}
							else
							{
								insertIndex--;
								numRows--;
							}
						}
						else if (isBorrowedNo.isSelected())
						{
							if (!brMemberMap.containsKey(bookList.get(getIndex).getId()))
							{
								bookData[insertIndex][0] = Integer.toString(bookList.get(getIndex).getId());
								bookData[insertIndex][1] = bookList.get(getIndex).getTitle();
								bookData[insertIndex][2] = bookList.get(getIndex).getAuthor();
								bookData[insertIndex][3] = Integer.toString(bookList.get(getIndex).getPageCount());
								bookData[insertIndex][4] = bookList.get(getIndex).getGenre();
								bookData[insertIndex][5] = "No one";
								bookData[insertIndex][6] = "No";
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
							
							if (brMemberMap.containsKey(bookList.get(getIndex).getId()))
							{
								bookData[insertIndex][5] = Integer.toString(brMemberMap.get(bookList.get(getIndex).getId()));
								if ((new Date(System.currentTimeMillis())).after(brDateMap.get(bookList.get(getIndex).getId())))
								{
									bookData[insertIndex][6] = "Yes";
								}
								else
								{
									bookData[insertIndex][6] = "No";
								}
							}
							else
							{
								bookData[insertIndex][5] = "No one";
								bookData[insertIndex][6] = "No";
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

			// updating the search parameters and querying the database
			else if (e.getSource().equals(searchMembers)) {
				int id = 0;
				
				if (!memberIDSearchField.getText().replaceAll("\\s", "").isEmpty())
				{
					try
					{
						id = Integer.parseInt(memberIDSearchField.getText().replaceAll("\\s", ""));
					}
					catch (NumberFormatException se)
					{
						id = -1;
					}
				}
				
				if (id >= 0)
				{
					String fName = firstNameSearchField.getText().trim();
					String lName = lastNameSearchField.getText().trim();
					
					ArrayList<Members> memList = mDAO.select(new Members(id, fName, lName));
					ArrayList<Fines> fineList = fDAO.select(new Fines(id, -1, Date.valueOf("1899-01-01")));
					
					memberData = new String[memList.size()][4];
					int numRows = memList.size();
					int insertIndex = 0;
					int getIndex = 0;
					
					while (insertIndex < numRows)
					{
						if (hasFinesYes.isSelected())
						{
							if (fineList.get(getIndex).getAmount() > 0)
							{
								memberData[insertIndex][0] = Integer.toString(memList.get(getIndex).getId());
								memberData[insertIndex][1] = memList.get(getIndex).getFirstName();
								memberData[insertIndex][2] = memList.get(getIndex).getLastName();
								memberData[insertIndex][3] = Double.toString(fineList.get(getIndex).getAmount());
							}
							else
							{
								insertIndex--;
								numRows--;
							}
						}
						else if (hasFinesNo.isSelected())
						{
							if (fineList.get(getIndex).getAmount() == 0)
							{
								memberData[insertIndex][0] = Integer.toString(memList.get(getIndex).getId());
								memberData[insertIndex][1] = memList.get(getIndex).getFirstName();
								memberData[insertIndex][2] = memList.get(getIndex).getLastName();
								memberData[insertIndex][3] = Double.toString(fineList.get(getIndex).getAmount());
							}
							else
							{
								insertIndex--;
								numRows--;
							}
						}
						else if (!hasFinesYes.isSelected() && !hasFinesNo.isSelected())
						{
							memberData[insertIndex][0] = Integer.toString(memList.get(getIndex).getId());
							memberData[insertIndex][1] = memList.get(getIndex).getFirstName();
							memberData[insertIndex][2] = memList.get(getIndex).getLastName();
							memberData[insertIndex][3] = Double.toString(fineList.get(getIndex).getAmount());
						}
						
						insertIndex++;
						getIndex++;
					}
				}
				else
				{
					memberData = new String[0][4];
				}
				
				membersTable.setModel(new DefaultTableModel(memberData, membersColumnName));
			}
			
			// pop up panel for checking out a book
			else if (e.getSource().equals(checkOutBook)) {				
				JTextField bookIDField = new JTextField();
			    JTextField memberIDField = new JTextField();

			    JPanel panel = new JPanel();
			    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			    panel.add(new JLabel("Enter Book ID:"));
			    panel.add(bookIDField);
			    panel.add(new JLabel("Enter Member ID:"));
			    panel.add(memberIDField);

			    int result = JOptionPane.showConfirmDialog(null, panel, "Check Out Book", JOptionPane.OK_CANCEL_OPTION);
			    
			    if (result == JOptionPane.OK_OPTION) {
					int bookId = 0;
					
					if (!bookIDField.getText().replaceAll("\\s", "").isEmpty())
					{
						try
						{
							bookId = Integer.parseInt(bookIDField.getText().replaceAll("\\s", ""));
						}
						catch (NumberFormatException se)
						{
							JOptionPane.showMessageDialog(null, "Invalid book id. Try again");
							return;
						}
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Empty book id value. Try again");
						return;
					}
					
					int memberId = 0;
					
					if (!memberIDField.getText().replaceAll("\\s", "").isEmpty())
					{
						try
						{
							memberId = Integer.parseInt(memberIDField.getText().replaceAll("\\s", ""));
						}
						catch (NumberFormatException se)
						{
							JOptionPane.showMessageDialog(null, "Invalid member id. Try again");
							return;
						}
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Empty member id value. Try again");
						return;
					}
					
					ArrayList<BorrowRecords> borrowRecord = brDAO.select(new BorrowRecords(bookId, 0, Date.valueOf("1899-01-01")));
					
					if (borrowRecord.size() > 0)
					{
						JOptionPane.showMessageDialog(null, "Book already borrowed. Try a different book");
						return;
					}
			        
		        	ArrayList<Books> selectedBook = bDAO.select(new Books(bookId, "", "", 0, ""));
			        ArrayList<Members> selectedMember = mDAO.select(new Members(memberId, "", ""));

			        String bookTitle = selectedBook.get(0).getTitle();
			        String memberName = selectedMember.get(0).getFirstName() + " " + selectedMember.get(0).getLastName();

			        int confirm = JOptionPane.showConfirmDialog(null, "Check out \"" + bookTitle + "\" to " + memberName + "?", "Confirm Checkout", JOptionPane.YES_NO_OPTION);

			        if (confirm == JOptionPane.YES_OPTION) {
			        	Date dueDate = Date.valueOf(LocalDate.now().plusWeeks(2));
			        	Date fineDate = Date.valueOf(LocalDate.now().plusWeeks(2).plusDays(1));
			        	fDAO.update(new Fines(memberId, -1, fineDate));
				    	brDAO.insert(new BorrowRecords(bookId, memberId, dueDate));
			            JOptionPane.showMessageDialog(null, "Checked out successfully.");
			        }
			    }
			}
			
			// pop up panel for returning a book
			else if (e.getSource().equals(returnBook)) {
				JTextField bookIDField = new JTextField();

			    JPanel panel = new JPanel();
			    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			    panel.add(new JLabel("Enter Book ID:"));
			    panel.add(bookIDField);

			    int result = JOptionPane.showConfirmDialog(null, panel, "Return Book", JOptionPane.OK_CANCEL_OPTION);

			    if (result == JOptionPane.OK_OPTION) {
			    	int bookId = 0;
			    	
			    	if (!bookIDField.getText().replaceAll("\\s", "").isEmpty())
					{
						try
						{
							bookId = Integer.parseInt(bookIDField.getText().replaceAll("\\s", ""));
						}
						catch (NumberFormatException se)
						{
							JOptionPane.showMessageDialog(null, "Invalid book id. Try again");
							return;
						}
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Empty book id value. Try again");
						return;
					}
			    	
			    	ArrayList<BorrowRecords> selectedBr = brDAO.select(new BorrowRecords(bookId, 0, Date.valueOf("1899-01-01")));
			    	
			    	if (selectedBr.size() == 0)
			    	{
			    		JOptionPane.showMessageDialog(null, "Book was not borrowed. Try a different book");
						return;
			    	}
			    	
			    	ArrayList<Books> selectedBook = bDAO.select(new Books(bookId, "", "", 0, ""));
			    	ArrayList<Members> selectedMember = mDAO.select(new Members(selectedBr.get(0).getMemberId(), "", ""));
			    	

			        String bookTitle = selectedBook.get(0).getTitle();
			        String memberName = selectedMember.get(0).getFirstName() + " " + selectedMember.get(0).getLastName();

			        int confirm = JOptionPane.showConfirmDialog(null, memberName + " is returning \"" + bookTitle + "\"?", "Confirm Return", JOptionPane.YES_NO_OPTION);

			        if (confirm == JOptionPane.YES_OPTION) {
			        	fDAO.update(new Fines(selectedBr.get(0).getMemberId(), -1, Date.valueOf("1899-01-01")));
			        	brDAO.delete(selectedBr.get(0));
			            JOptionPane.showMessageDialog(null, "Book returned successfully.");
			        }
			    }
			}
		}
	}
}

