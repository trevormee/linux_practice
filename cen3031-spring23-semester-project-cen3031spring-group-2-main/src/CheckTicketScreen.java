package mainProject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class CheckTicketScreen extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	// Fonts
	private Font font = new Font("Helvetica", Font.PLAIN, 14);
	private Font boldFont = new Font("Helvetica", Font.BOLD, 14);
	private Font titleFont = new Font("Helvetica", Font.BOLD, 22);
	
	// Set the theme colors
	private Color blue = new Color(23, 73, 114);
	private Color buttonBlue = new Color(52, 152, 219);
	private Color white = new Color(241, 241, 241);
	private Color buttonRed = new Color(217, 30, 24);
	
	private boolean userInfoFrameOpen = false;
	
	public CheckTicketScreen() {
		// Initialize the window
		setTitle("Check Ticket Info");
		setResizable(false);
		setSize(500, 350);
		setLocationRelativeTo(null);
		JPanel customerPanel = createCustomerPanel();
		add(customerPanel, BorderLayout.CENTER);
		setVisible(true);
		
		// Handle window closing
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				HomeScreen.setAdminScreenOpen(false);
			}
		});
	}
	
	private JFrame createUserInfoFrame(String name) throws IOException {
	    ArrayList<Ticket> allTickets = FileManager.readTickets();
	    ArrayList<Ticket> tickets = new ArrayList<>();

	    for (int i = 0; i < allTickets.size(); i++) {
	        Ticket ticket = allTickets.get(i);
	        System.out.println(ticket.getPassengerName());
	        if (ticket.getPassengerName().equalsIgnoreCase(name)) {
	            tickets.add(ticket);
	        }
	    }

	    // Create a new panel with a BoxLayout
	    JPanel userInfoPanel = new JPanel();
	    userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.PAGE_AXIS));

	    // Add the list of tickets to the panel
	    JLabel ticketLabel = new JLabel("Tickets Purchased (" + tickets.size() + "):");
	    ticketLabel.setFont(titleFont);
	    ticketLabel.setForeground(blue);
	    ticketLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

	    userInfoPanel.add(ticketLabel);
	    boolean first = true;

	    for (Ticket ticket : tickets) {
	        int seatNumber = ticket.getSeatNumber();
	        Train train = Train.getTrainFromId(ticket.getTrainId());
	        Seat seat = train.getSeatFromNumber(seatNumber);

	        if (first != true) {
		        JLabel newLineLabel = new JLabel(" ");
		        newLineLabel.setFont(boldFont);
		        newLineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		        userInfoPanel.add(newLineLabel);
	        } else {
	        	first = false;
	        }

	        JLabel idLabel = new JLabel("Ticket ID: " + ticket.getId());
	        idLabel.setFont(boldFont);
	        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        userInfoPanel.add(idLabel);
	        
	        JLabel destinationLabel = new JLabel("Destination: " + train.getDestination());
	        destinationLabel.setFont(font);
	        destinationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        userInfoPanel.add(destinationLabel);

	        JLabel seatNumberLabel = new JLabel("Seat Number: " + seatNumber);
	        seatNumberLabel.setFont(font);
	        seatNumberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        userInfoPanel.add(seatNumberLabel);

	        JLabel seatTypeLabel = new JLabel("Seat Type: " + seat.getSeatType());
	        seatTypeLabel.setFont(font);
	        seatTypeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        userInfoPanel.add(seatTypeLabel);
	    }

	    // Add the panel to a new JFrame
	    JFrame userInfoFrame = new JFrame("Tickets for " + name);
	    userInfoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	    // Create a new panel with a BorderLayout and padding
	    JPanel mainPanel = new JPanel(new BorderLayout());
	    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

	    // Add the ticketLabel to the top of the panel, centered horizontally
	    JPanel ticketPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    ticketPanel.add(ticketLabel);
	    mainPanel.add(ticketPanel, BorderLayout.NORTH);

	    // Remove the outline from the scrolling panel
	    JScrollPane scrollPane = new JScrollPane(userInfoPanel);
	    mainPanel.add(scrollPane, BorderLayout.CENTER);

	    userInfoFrame.getContentPane().add(mainPanel);
	    userInfoFrame.setLocationRelativeTo(null);
	    userInfoFrame.setSize(new Dimension(500, 400));
	    userInfoFrame.setVisible(true);

	    return userInfoFrame;
	}

	// Create the customer info panel
	private JPanel createCustomerPanel() {
			JPanel customerPanel = new JPanel();
			customerPanel.setLayout(new GridBagLayout());
			customerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.anchor = GridBagConstraints.LINE_END;
			JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			JLabel nameLabel = new JLabel("Name:");
			nameLabel.setPreferredSize(new Dimension(100, 30));
			nameLabel.setFont(boldFont);
			namePanel.add(nameLabel);
			JTextField nameField = new JTextField(20);
			nameField.setPreferredSize(new Dimension(300, 30));
			nameField.setMaximumSize(new Dimension(300, 30));
			nameField.setBorder(new EmptyBorder(5, 5, 5, 5));
			nameField.setFont(font);
			namePanel.add(nameField);
			customerPanel.add(namePanel, gbc);
			gbc.gridy++;
			
			// Create panel for search button
			JPanel buttonPanel = createBottomButtonPanel("Search", false);
		    JButton searchButton = (JButton) buttonPanel.getComponent(0);
		    searchButton.addActionListener(e -> {
		    	if (userInfoFrameOpen == false) {
			    	try {
						JFrame userInfoFrame = createUserInfoFrame(nameField.getText());
						userInfoFrameOpen = true;
						userInfoFrame.addWindowListener(new WindowAdapter() {
							@Override
							public void windowClosing(WindowEvent e) {
								userInfoFrameOpen = false;
							}
						});
					} catch (IOException e1) {
						e1.printStackTrace();
					}
		    	}
		    });

			add(customerPanel, BorderLayout.CENTER);
			add(buttonPanel, BorderLayout.SOUTH);
			setVisible(true);
			
			return customerPanel;
	}
	
	private JPanel createBottomButtonPanel(String buttonText, boolean hasBackButton) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panel.setBackground(blue);
		if (hasBackButton == true) {
			JButton backButton = createButton("Back", buttonRed);
			panel.add(backButton);
		}
		JButton button = createButton(buttonText, buttonBlue);
		panel.add(button);
		
		return panel;
	}
	
	private JButton createButton(String buttonText, Color color) {
		JButton button = new JButton(buttonText);
		button.addActionListener(this);
		button.setBackground(color);
		button.setForeground(white);
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(20, 35, 20, 35));
		button.setPreferredSize(new Dimension(200, 35));
		button.setFont(new Font("Helvetica", Font.BOLD, 14));
		
		return button;
	}

	@Override
	public void actionPerformed(ActionEvent e) {}
}
