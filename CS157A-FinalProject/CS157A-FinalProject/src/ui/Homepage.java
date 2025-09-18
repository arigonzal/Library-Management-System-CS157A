package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.*;

import dao.BorrowRecordsDAO;
import dao.FinesDAO;
import db.DBConnection;
import objects.BorrowRecords;
import objects.Fines;

public class Homepage extends JFrame{
	// buttons to display on the Home page
	private JButton staffDashboardButton;
	private JButton memberDashboardButton;
	private JButton exitButton;
	
	// variable to hold database connection
	private DBConnection dbConnection;

	public Homepage() {
		// start a new database connection when the application is open
		dbConnection = new DBConnection();
		
		// if the connection fails then close the application
		if (dbConnection.getConnection() == null)
		{
			System.out.println("\n\n Connection Error: Check connection to database");
			System.exit(0);
		}
		
		// update fine amounts for books that are overdue on a weekly basis
		BorrowRecordsDAO brDAO = new BorrowRecordsDAO(dbConnection.getConnection());
		FinesDAO fDAO = new FinesDAO(dbConnection.getConnection());
		
		ArrayList<BorrowRecords> brList = brDAO.select(new BorrowRecords(0, 0, Date.valueOf("1899-01-01")));
		ArrayList<Fines> fineList = fDAO.select(new Fines(0, -1, Date.valueOf("1899-01-01")));
		
		HashMap<Integer, Fines> fineMap = new HashMap<>();
		
		for (int i = 0; i < fineList.size(); i++)
		{
			fineMap.put(fineList.get(i).getMemberId(), fineList.get(i));
		}
		
		for (int i = 0; i < brList.size(); i++)
		{
			if ((new Date(System.currentTimeMillis()).after(brList.get(i).getDueDate())))
			{
				Date currentDate = new Date(System.currentTimeMillis());
				Date lastFineDate = fineMap.get(brList.get(i).getBookId()).getLastFineDate();
				
				if (!lastFineDate.equals(Date.valueOf("1899-01-01")))
				{
					LocalDate currentLDate = currentDate.toLocalDate();
					LocalDate lastFineLDate = lastFineDate.toLocalDate();
					
					long daysBetween = ChronoUnit.DAYS.between(lastFineLDate, currentLDate);
					
					if (daysBetween > 7)
					{
						int id = fineMap.get(brList.get(i).getBookId()).getMemberId();
						double amount = fineMap.get(brList.get(i).getBookId()).getAmount() + 5.0 * (daysBetween / 7);
						Date newLastFineDate = new Date(System.currentTimeMillis());
						fDAO.update(new Fines(id, amount, newLastFineDate));
					}
				}
			}
		}
		
		setSize(800, 500);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		JLabel header = new JLabel("Welcome!", SwingConstants.CENTER);
		header.setFont(new Font(Font.SERIF, Font.BOLD, 36));

		// create and add listeners to each button
		staffDashboardButton = new JButton("Staff Dashboard");
		staffDashboardButton.addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e) {
				StaffDashboard staffDashboard = new StaffDashboard(dbConnection.getConnection());
			}
		});

		memberDashboardButton = new JButton("Member Dashboard");
		memberDashboardButton.addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e) {
				MemberDashboard memberDashboard = new MemberDashboard(dbConnection.getConnection());
			}
		});
		exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});


		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, -150, 50, -150);


		// create gbc for buttons
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,3));
		buttonPanel.setOpaque(false);

		// add buttons to panel, add to frame 
		buttonPanel.add(staffDashboardButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(1,10)));
		buttonPanel.add(memberDashboardButton);

		contentPanel.add(header, gbc);
		contentPanel.add(buttonPanel, gbc);
		contentPanel.add(exitButton, gbc);

		add(contentPanel);
		setVisible(true);
	}
}


