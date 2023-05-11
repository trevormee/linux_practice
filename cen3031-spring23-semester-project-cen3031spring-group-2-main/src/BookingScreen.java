/*
	BookingScreen.java
	
	This class creates the booking screen, which allows the user to choose an option from a list of trains.
	
	@author Eli Turner
	@author Selena Chiang
*/

package mainProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class BookingScreen extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Theme colors
	private Color blue = new Color(23, 73, 114);
	private Color buttonBlue = new Color(52, 152, 219);
	private Color white = new Color(241, 241, 241);
	private Color selectionWhite = new Color(200, 200, 170);
	private Color black = new Color(14, 14, 14);

	// Fonts
	private Font listFont = new Font("Helvetica", Font.BOLD, 13);
	private Font buttonFont = new Font("Helvetica", Font.BOLD, 14);

	// Shared GUI variables
	private JList<String> destinationList;
	private JButton bookButton;

	public BookingScreen() {
		super("Booking Screen");

		ArrayList<Train> trains = Train.getTrains();

		// Set the background contents
		Container contentPane = getContentPane();
		contentPane.setBackground(white);

		// Create the list model and set the background color of the list
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		destinationList = new JList<String>(listModel);
		destinationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		destinationList.setBackground(blue);

		for (int i = 0; i < trains.size(); i++) {
			Train train = trains.get(i);
			String destination = train.getDestination();
			String departure = train.getDeparture();
			listModel.addElement(destination + ": " + departure);
		}

		FileManager infoReader = new FileManager();
		try {
			infoReader.updateTrains();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Create the list of destinations
		destinationList = new JList<String>(listModel);
		destinationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		destinationList.setFont(listFont);
		destinationList.setBackground(white);
		destinationList.setForeground(black);
		destinationList.setSelectionBackground(selectionWhite);
		destinationList.setFocusable(false);

		// Create the book button
		bookButton = new JButton("Book");
		bookButton.setBackground(buttonBlue);
		bookButton.setForeground(white);
		bookButton.setFocusPainted(false);
		bookButton.setBorder(BorderFactory.createEmptyBorder(20, 35, 20, 35));
		bookButton.setPreferredSize(new Dimension(200, 35));
		bookButton.setFont(buttonFont);
		bookButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedDestination = destinationList.getSelectedValue();
				if (selectedDestination != null) {
					// Open the booking screen for the selected destination
					for (int i = 0; i < trains.size(); i++) {
						Train train = trains.get(i);
						String string = train.getDestination() + ": " + train.getDeparture();
						if (string.equals(selectedDestination)) {
							new TrainScreen(train);
							break;
						}
					}
				}
				dispose();
			}
		});

		// Allow the HomeScreen to open more booking screens when the current one is closed
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				HomeScreen.setBookingScreenOpen(false);
			}
		});

		// Add the destination list and book button to the content pane
		contentPane.setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(destinationList);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		contentPane.add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setBackground(blue);
		buttonPanel.add(bookButton);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);

		// Set the window size and make it visible
		setSize(400, 300);
		setLocationRelativeTo(null);
		setVisible(true);
	}

}
