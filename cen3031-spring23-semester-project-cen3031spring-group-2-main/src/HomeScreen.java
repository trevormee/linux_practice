/*
	HomeScreen.java
	
	This class creates the main home screen, which allows users to navigate to other screens.
	
	@author Selena Chiang
	@author Eli Turner
*/

package mainProject;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

public class HomeScreen {
	// Set the theme colors
	Color blue = new Color(23, 73, 114);
	Color buttonBlue = new Color(52, 152, 219);
	Color white = new Color(241, 241, 241);
	Color buttonRed = new Color(217, 30, 24);

	// Set the fonts
	Font boldFont = new Font("Helvetica", Font.BOLD, 14);

	// Shared GUI variables
	JFrame frame;
	boolean ticketsPageOpen = false;
	boolean routePageOpen = false;
	boolean checkTicketScreenOpen = false;
	private static boolean bookingScreenOpen = false;
	private static boolean adminScreenOpen = false;
	private boolean routesScreenOpen = false;
	JPanel mainPanel;
	JLabel welcomeLabel;
	JLabel pictureLabel;
	JPanel rightPanel;

	public HomeScreen() throws IOException {
		frame = new JFrame("Amtrak Booking System");
		frame.setBounds(100, 100, 598, 400);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		frame.setVisible(true);
		initializePanels();
		initializeLabels();
		initializeButtons();
		loadTrains();
		loadTickets();
		loadPackages();
		mainPanel.repaint();
	}

	private void initializePanels() {
		mainPanel = new JPanel();
		mainPanel.setBackground(white);
		mainPanel.setBounds(0, 0, 600, 427);
		frame.getContentPane().add(mainPanel);
		mainPanel.setLayout(null);

		rightPanel = new JPanel();
		rightPanel.setBackground(blue);
		rightPanel.setBounds(205, 0, 377, 427);
		mainPanel.add(rightPanel);
		rightPanel.setLayout(null);
	}

	private void initializeLabels() {
		welcomeLabel = new JLabel(
				"<html><div style='text-align: center;'>Welcome to Amtrak's Booking System</div></html>");
		welcomeLabel.setFont(new Font("Helvetica", Font.BOLD, 22));
		welcomeLabel.setBounds(0, 0, 205, 126);
		welcomeLabel.setForeground(blue);
		mainPanel.add(welcomeLabel);

		pictureLabel = new JLabel();
		pictureLabel.setBounds(27, 160, 160, 160);
		ImageIcon icon = new ImageIcon(this.getClass().getResource("/Picture1.png"));
		int iconWidth = icon.getIconWidth();
		int iconHeight = icon.getIconHeight();
		double aspectRatio = (double) iconWidth / iconHeight;
		int labelWidth = pictureLabel.getWidth();
		int labelHeight = pictureLabel.getHeight();

		// Scale based on the dimension that requires the most reduction while
		// maintaining aspect ratio
		if (labelWidth / aspectRatio > labelHeight) {
			icon = new ImageIcon(icon.getImage().getScaledInstance((int) (labelHeight * aspectRatio), labelHeight,
					Image.SCALE_SMOOTH));
		} else {
			icon = new ImageIcon(icon.getImage().getScaledInstance(labelWidth, (int) (labelWidth / aspectRatio),
					Image.SCALE_SMOOTH));
		}
		pictureLabel.setIcon(icon);
		mainPanel.add(pictureLabel);
	}

	private void initializeButtons() {
		JButton purchaseTicketsButton = createButton("Purchase Tickets", buttonBlue, 1);
		purchaseTicketsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!bookingScreenOpen) {
					bookingScreenOpen = true;
					BookingScreen frame = new BookingScreen();
					frame.setVisible(true);
				}
			}
		});
		rightPanel.add(purchaseTicketsButton);
		
		JButton checkTicketButton = createButton("Check Tickets", buttonBlue, 2);
		checkTicketButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!checkTicketScreenOpen) {
					checkTicketScreenOpen = true;
					CheckTicketScreen frame = new CheckTicketScreen();
					frame.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent e) {
							checkTicketScreenOpen = false;
						}
					});
					frame.setVisible(true);
				}
			}
		});
		rightPanel.add(checkTicketButton);
		
		JButton trainRouteButton = createButton("Train Routes ", buttonBlue, 3);
		trainRouteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!routesScreenOpen) {
					routesScreenOpen = true;
					RoutesScreen frame = new RoutesScreen();
					frame.setVisible(true);
					frame.addWindowListener(new WindowAdapter() {
						public void windowClosing(WindowEvent e) {
							routesScreenOpen = false;
							frame.dispose();
						}
					});
				}
			}
		});
		rightPanel.add(trainRouteButton);

		JButton adminButton = createButton("Administrator", buttonBlue, 4);
		adminButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!adminScreenOpen) {
					adminScreenOpen = true;
					new AdministratorScreen();
				}
			}
		});
		rightPanel.add(adminButton);

		JButton exitButton = createButton("Exit", buttonRed, 5);
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		rightPanel.add(exitButton);
	}

	public static void loadTrains() throws IOException {
		int rows = 10;
		int cols = 4;
		try {
			BufferedReader reader = new BufferedReader(new FileReader("destinations.txt"));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(";");
				String destination = parts[0];
				String departure = parts[1];
				Seat[][] seats = new Seat[rows][cols];
				int currSeatNum = 1;
				for (int i = 0; i < rows; i++) {
					SeatType seatType;
					if (i < Double.valueOf(rows) * (1.0 / 3.0)) {
						seatType = SeatType.FIRST_CLASS;
					} else if (i < Double.valueOf(rows) * (2.0 / 3.0)) {
						seatType = SeatType.BUSINESS_CLASS;
					} else {
						seatType = SeatType.ECONOMY_CLASS;
					}
					for (int j = 0; j < cols; j++) {
						Seat seat = new Seat(seatType, currSeatNum, true);
						seats[i][j] = seat;
						currSeatNum++;
					}
				}
				new Train(rows, cols, seats, destination, departure);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayList<Train> newTrains = Train.getTrains();
		ArrayList<Train> trainsFromSave = FileManager.readTrains();
		for (int i = 0; i < newTrains.size(); i++) {
			Train newTrain = newTrains.get(i);
			for (int j = 0; j < trainsFromSave.size(); j++) {
				Train trainFromSave = trainsFromSave.get(j);
				if (newTrain.getDestination().equals(trainFromSave.getDestination())
						&& newTrain.getDeparture().equals(trainFromSave.getDeparture())) {
					newTrains.set(i, trainFromSave);
				}
			}
		}
	}

	public static void loadTickets() throws IOException {
		@SuppressWarnings("unused")
		ArrayList<Ticket> tickets = FileManager.readTickets();
	}

	private void loadPackages() {
		PackageSet packageSet = new PackageSet("packages.txt", 4);
		try {
			packageSet.LoadFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private JButton createButton(String text, Color color, int index) {
		JButton button = new JButton(text);
		button.setSize(163, 33);
		button.setLocation((rightPanel.getWidth() - button.getWidth()) / 2, 17 + index * 52);
		button.setBackground(color);
		button.setForeground(white);
		button.setFocusPainted(false);
		button.setFont(boldFont);
		button.setBorderPainted(false);

		return button;
	}

	public static void setBookingScreenOpen(boolean bookingScreenOpen) {
		HomeScreen.bookingScreenOpen = bookingScreenOpen;
	}

	public static void setAdminScreenOpen(boolean adminScreenOpen) {
		HomeScreen.adminScreenOpen = adminScreenOpen;
	}

	public static void main(String[] args) throws IOException {
		new HomeScreen();
	}
}
