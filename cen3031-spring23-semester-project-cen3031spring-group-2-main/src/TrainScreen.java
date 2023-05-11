/*
	TrainScreen.java
	
	This class creates the train screen, which allows users to add seats of their choice to a cart and proceed to the checkout screen.
	
	@author Eli Turner
	@author Sam Zou
*/

package mainProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TrainScreen extends JFrame {
	private static final long serialVersionUID = 1L;

	// Theme colors
	private Color blue = new Color(23, 73, 114);
	private Color disabledWhite = new Color(200, 200, 200);
	private Color white = new Color(241, 241, 241);
	private Color black = new Color(14, 14, 14);

	// Fonts
	private Font defaultFont = new Font("Helvetica", Font.BOLD, 13);
	private Font textAreaFont = new Font("Helvetica", Font.PLAIN, 14);

	// Shared GUI variables
	private TrainScreen screen;
	private JButton[] seatButtons;
	private JTextArea seatInfoTextArea;
	private JButton addToCartButton;
	private JButton currentSelection;
	private JComboBox<String> cartDropdown;
	private DefaultComboBoxModel<String> cartModel;
	private JButton removeButton;
	private JButton checkoutButton;
	private CheckoutScreen checkoutScreen;
	private JPanel checklist;

	// Other variables
	private Train train;
	private ArrayList<Seat> seats = new ArrayList<Seat>();
	private Seat currentSeat;
	private Seat currentSeatInCart;
	private ArrayList<Ticket> tickets;
	private ArrayList<Integer> selectedPackages = new ArrayList<Integer>();
	private Map<Integer, ArrayList<Integer>> packagesInCart;
	private NumberFormat formatter = NumberFormat.getCurrencyInstance();

	public TrainScreen(Train newTrain) {
		super("Seat Selection for Train to " + newTrain.getDestination());
		setBackground(blue);
		setResizable(false);
		screen = this;
		train = newTrain;
		packagesInCart = new HashMap<>();
		int rows = train.getRows();
		int cols = train.getCols();
		tickets = new ArrayList<Ticket>();
		JPanel seatPanel = new JPanel(new GridLayout(rows, cols));
		seatButtons = new JButton[rows * cols];
		Seat[][] seats = train.getSeats();
		for (int i = 0; i < rows * cols; i++) {
			Seat seat = seats[i / cols][i % cols];
			String buttonText = "Seat " + seat.getSeatNumber();
			JButton button = new JButton(buttonText);
			boolean seatAvailable = seat.getSeatAvailable();
			if (seatAvailable == true) {
				button.setBackground(blue);
			} else {
				button.setBackground(disabledWhite);
			}
			button.setEnabled(seatAvailable);
			button.addActionListener(new SeatButtonListener(seat));
			button.setSize(70, 50);
			button.setForeground(white);
			button.setBorderPainted(false);
			button.setFocusable(false);
			seatButtons[i] = button;
			seatPanel.add(button);
		}

		seatInfoTextArea = new JTextArea();
		seatInfoTextArea.setEditable(false);
		seatInfoTextArea.setFont(textAreaFont);
		seatInfoTextArea.setText("Please make a selection.");
		seatInfoTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		seatInfoTextArea.setPreferredSize(new Dimension(0, rows * 16));
		seatInfoTextArea.setOpaque(false);

		// Checklist
		checklist = new JPanel(new GridLayout(0, 1));
		checklist.setVisible(false);
		checklist.setPreferredSize(new Dimension(0, 250));

		// Set the font and color of the checkbox label
		for (int i = 0; i < PackageSet.getSize(); i++) {
			JCheckBox checkbox = new JCheckBox(
					PackageSet.getPackageName(i) + " ($" + PackageSet.getPackagePrice(i) + ")");
			checkbox.setFont(defaultFont);
			checklist.add(checkbox);
		}

		addToCartButton = new JButton("Add to Cart");
		addToCartButton.setEnabled(false);
		addToCartButton.setBackground(disabledWhite);
		addToCartButton.setBorderPainted(false);
		addToCartButton.setForeground(white);
		addToCartButton.addActionListener(new AddToCartButtonListener());

		JPanel seatInfoPanel = new JPanel(new BorderLayout());
		seatInfoPanel.add(seatInfoTextArea, BorderLayout.NORTH);
		seatInfoPanel.add(checklist, BorderLayout.CENTER);
		seatInfoPanel.add(addToCartButton, BorderLayout.SOUTH);

		JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		JPanel leftPanel = new JPanel(new GridLayout(rows, cols / 2, 10, 10));
		JPanel rightPanel = new JPanel(new GridLayout(rows, cols / 2, 10, 10));
		for (int i = 0; i < rows * cols; i++) {
			if (i % cols < cols / 2) {
				leftPanel.add(seatButtons[i]);
			} else {
				rightPanel.add(seatButtons[i]);
			}
		}
		mainPanel.add(leftPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(50, 0)));
		mainPanel.add(rightPanel);

		cartModel = new DefaultComboBoxModel<>();
		cartDropdown = new JComboBox<>(cartModel);
		cartDropdown.setFont(defaultFont);
		cartDropdown.setPreferredSize(new Dimension(200, 25));
		cartDropdown.setBorder(BorderFactory.createEmptyBorder());
		cartDropdown.setBackground(white);
		cartDropdown.setForeground(black);
		JScrollPane cartScrollPane = new JScrollPane(cartDropdown);

		removeButton = new JButton("Remove From Cart");
		removeButton.setEnabled(false);
		removeButton.setBackground(disabledWhite);
		removeButton.setBorderPainted(false);
		removeButton.setForeground(white);
		removeButton.setBorderPainted(false);
		removeButton.setFocusable(false);
		removeButton.addActionListener(new RemoveFromCartButtonListener());

		cartDropdown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedItem = (String) cartDropdown.getSelectedItem();
				if (selectedItem != null && selectedItem != "") {
					int seatNumber = getNumbersFromString(selectedItem);
					Seat seat = train.getSeatFromNumber(seatNumber);
					currentSeatInCart = seat;
					removeButton.setEnabled(true);
					removeButton.setBackground(blue);
				} else {
					removeButton.setEnabled(false);
					removeButton.setBackground(disabledWhite);
				}
			}
		});

		// Allow the HomeScreen to open more booking screens when the current one is
		// closed
		screen.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				HomeScreen.setBookingScreenOpen(false);
			}
		});

		checkoutButton = new JButton("Proceed to Checkout");
		checkoutButton.setEnabled(false);
		checkoutButton.setBackground(disabledWhite);
		checkoutButton.setBorderPainted(false);
		checkoutButton.setForeground(white);
		checkoutButton.addActionListener(new CheckoutButtonListener());

		JPanel cartPanel = new JPanel(new BorderLayout());
		cartPanel.add(cartDropdown, BorderLayout.NORTH);
		cartPanel.add(cartScrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
		buttonPanel.add(removeButton);
		buttonPanel.add(checkoutButton);

		cartPanel.add(buttonPanel, BorderLayout.SOUTH);

		Container contentPane = getContentPane();
		contentPane.add(mainPanel, BorderLayout.WEST);
		contentPane.add(seatInfoPanel, BorderLayout.CENTER);
		contentPane.add(cartPanel, BorderLayout.EAST);

		checkoutButton.setPreferredSize(removeButton.getPreferredSize());

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setSize(900, (rows + 1) * 36 - 7);
		setVisible(true);
	}

	private void refreshComboBox(JComboBox<String> comboBox, ArrayList<Seat> stringList) {
		Component[] components = checklist.getComponents();
		for (Component component : components) {
			if (component instanceof JCheckBox) {
				JCheckBox checkbox = (JCheckBox) component;
				checkbox.setSelected(false);
			}
		}
		comboBox.removeAllItems();
		comboBox.addItem("");
		for (Seat seat : stringList) {
			comboBox.addItem("Ticket for Seat " + seat.getSeatNumber());
		}
		if (stringList.size() > 0) {
			checkoutButton.setBackground(blue);
			checkoutButton.setEnabled(true);
		} else {
			checkoutButton.setBackground(disabledWhite);
			checkoutButton.setEnabled(false);
		}
	}

	private int getNumbersFromString(String input) {
		String numbersOnly = input.replaceAll("\\D+", "");
		int result = Integer.parseInt(numbersOnly);
		return result;
	}

	private JButton getSeatButton(int seatNumber) {
		for (int i = 0; i < seatButtons.length; i++) {
			JButton seatButton = seatButtons[i];
			if (getNumbersFromString(seatButton.getText()) == seatNumber) {
				return seatButton;
			}
		}
		return null;
	}

	private class SeatButtonListener implements ActionListener {
		private Seat seat;

		public SeatButtonListener(Seat seat) {
			this.seat = seat;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			currentSeat = seat;
			addToCartButton.setEnabled(true);
			addToCartButton.setBackground(blue);
			String status = "";
			if (seat.getSeatAvailable()) {
				status = "Available";
			} else {
				status = "Booked";
			}
			String seatInfo = "CURRENT SELECTION:\nSeat Number: " + seat.getSeatNumber() + "\nSeat Type: "
					+ seat.getSeatType() + " (" + formatter.format(seat.getSeatType().getPrice()) + ")\nAvailability: "
					+ status;
			seatInfoTextArea.setText(seatInfo);
			currentSelection = (JButton) e.getSource();
			addToCartButton.setEnabled(seat.getSeatAvailable());
			if (seat.getSeatAvailable() == true) {
				addToCartButton.setBackground(blue);
			} else {
				addToCartButton.setBackground(disabledWhite);
			}
			checklist.setVisible(true);
		}
	}

	private class AddToCartButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			seats.add(currentSeat);
			Component[] components = checklist.getComponents();
			for (int i = 0; i < components.length; i++) {
				JCheckBox checkbox = (JCheckBox) components[i];
				if (checkbox.isSelected()) {
					selectedPackages.add(i);
				}
			}
			checklist.setVisible(false);
			@SuppressWarnings("unchecked")
			ArrayList<Integer> selectedPackagesClone = (ArrayList<Integer>) selectedPackages.clone();
			packagesInCart.put(currentSeat.getSeatNumber(), selectedPackagesClone);
			selectedPackages.clear();
			refreshComboBox(cartDropdown, seats);
			currentSeat.setSeatAvailable(false);
			currentSelection.setEnabled(false);
			currentSelection.setBackground(disabledWhite);
			addToCartButton.setEnabled(false);
			addToCartButton.setBackground(disabledWhite);
			seatInfoTextArea.setText("Please make a selection.");
		}
	}

	private class RemoveFromCartButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			currentSeatInCart.setSeatAvailable(true);
			packagesInCart.remove(currentSeatInCart.getSeatNumber());
			JButton button = getSeatButton(currentSeatInCart.getSeatNumber());
			seats.remove(currentSeatInCart);
			refreshComboBox(cartDropdown, seats);
			button.setEnabled(true);
			button.setBackground(blue);
		}
	}

	private class CheckoutButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (checkoutScreen == null) {
				for (int i = 0; i < seats.size(); i++) {
					Seat seat = seats.get(i);
					ArrayList<Integer> ticketPackages = packagesInCart.get(seat.getSeatNumber());
					Ticket newTicket = new Ticket(seat.getSeatNumber(), seat.getSeatType().getPrice(), ticketPackages,
							train.getId());
					tickets.add(newTicket);
					train.addTicket(newTicket);
				}
				checkoutScreen = new CheckoutScreen(tickets, train, screen);
				checkoutScreen.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						checkoutScreen = null;
						train.clearTickets();
					}
				});
			}
		}
	}
}
