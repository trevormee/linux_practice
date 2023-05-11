/*
	CheckoutScreen.java
	
	This class creates the checkout screen, which allows the user to securely purchase their tickets from the TrainScreen cart and stores their information.
	
	@author Eli Turner
*/

package mainProject;

import javax.swing.*;
import javax.swing.text.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;

public class CheckoutScreen extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double total = 0;
	private NumberFormat formatter = NumberFormat.getCurrencyInstance();
	private String name;
	
	// Theme colors
	private Color blue = new Color(23, 73, 114);
	private Color buttonBlue = new Color(52, 152, 219);
	private Color white = new Color(241, 241, 241);
    
    // Fonts
	private Font font = new Font("Helvetica", Font.PLAIN, 14);
	private Font boldFont = new Font("Helvetica", Font.BOLD, 14);
	private Font titleFont = new Font("Helvetica", Font.BOLD, 22);
    
	// Used to limit inputs to prevent human error
    public class NumberOnlyFilter extends DocumentFilter {
        private final int maxLength;

        public NumberOnlyFilter(int maxLength) {
            this.maxLength = maxLength;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String text, AttributeSet attrs)
                throws BadLocationException {
            if (isNumeric(text) && fb.getDocument().getLength() + text.length() <= maxLength) {
                super.insertString(fb, offset, text, attrs);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (isNumeric(text) && fb.getDocument().getLength() - length + text.length() <= maxLength) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        private boolean isNumeric(String text) {
            return text.matches("\\d*");
        }
    }
	
    public CheckoutScreen(ArrayList<Ticket> tickets, Train train, JFrame previousFrame) {
        super("Checkout");
        
        for (int i = 0; i < tickets.size(); i++) {
        	total += tickets.get(i).getPrice();
        }
        
        // Initializes the main panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 5, 10));
        
        // Creates the credit card fields
        JLabel creditCardLabel = new JLabel("Credit Card Number:");
        creditCardLabel.setFont(boldFont);
        JPanel creditCardPanel = new JPanel(new GridLayout(1, 4, 5, 0));
        JTextField creditCard1 = new JTextField();
        ((AbstractDocument) creditCard1.getDocument()).setDocumentFilter(new NumberOnlyFilter(4));
        creditCard1.setFont(font);
        JTextField creditCard2 = new JTextField();
        creditCard2.setFont(font);
        ((AbstractDocument) creditCard2.getDocument()).setDocumentFilter(new NumberOnlyFilter(4));
        JTextField creditCard3 = new JTextField();
        creditCard3.setFont(font);
        ((AbstractDocument) creditCard3.getDocument()).setDocumentFilter(new NumberOnlyFilter(4));
        JTextField creditCard4 = new JTextField();
        creditCard4.setFont(font);
        ((AbstractDocument) creditCard4.getDocument()).setDocumentFilter(new NumberOnlyFilter(4));
        creditCardPanel.add(creditCard1);
        creditCardPanel.add(creditCard2);
        creditCardPanel.add(creditCard3);
        creditCardPanel.add(creditCard4);

        // Creates the name field
        JLabel nameOnCardLabel = new JLabel("Name on Card:");
        nameOnCardLabel.setFont(boldFont);
        JTextField nameOnCardField = new JTextField();
        nameOnCardField.setFont(font);

        //Creates the expiration date fields
        JLabel expDateLabel = new JLabel("Expiration Date:");
        expDateLabel.setFont(boldFont);
        JPanel expDatePanel = new JPanel(new GridLayout(1, 3, 5, 0));
        JTextField expMonthField = new JTextField();
        ((AbstractDocument) expMonthField.getDocument()).setDocumentFilter(new NumberOnlyFilter(2));
        expMonthField.setFont(font);
        JLabel slashLabel = new JLabel("        /");
        slashLabel.setFont(boldFont);
        JTextField expYearField = new JTextField();
        expYearField.setFont(font);
        ((AbstractDocument) expYearField.getDocument()).setDocumentFilter(new NumberOnlyFilter(2));
        expDatePanel.add(expMonthField);
        expDatePanel.add(slashLabel);
        expDatePanel.add(expYearField);

        // Creates the CVV field
        JLabel cvvLabel = new JLabel("CVV:");
        cvvLabel.setFont(boldFont);
        JTextField cvvField = new JTextField();
        cvvField.setFont(font);
        ((AbstractDocument) cvvField.getDocument()).setDocumentFilter(new NumberOnlyFilter(3));

        // Creates the total label
        JLabel totalLabel = new JLabel("Total:");
        totalLabel.setFont(boldFont);
        String formattedTotal = formatter.format(total);
        JLabel subtotalAmountLabel = new JLabel(formattedTotal);
        subtotalAmountLabel.setFont(boldFont);

        panel.add(creditCardLabel);
        panel.add(creditCardPanel);
        panel.add(nameOnCardLabel);
        panel.add(nameOnCardField);
        panel.add(expDateLabel);
        panel.add(expDatePanel);
        panel.add(cvvLabel);
        panel.add(cvvField);
        panel.add(totalLabel);
        panel.add(subtotalAmountLabel);

        JButton checkoutButton = new JButton("Check Out");
        checkoutButton.setBackground(buttonBlue);
        checkoutButton.setForeground(white);
        checkoutButton.setFocusPainted(false);
        checkoutButton.setBorder(BorderFactory.createEmptyBorder(20, 35, 20, 35));
        checkoutButton.setPreferredSize(new Dimension(200, 35));
		checkoutButton.setFont(boldFont);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setBackground(blue);
		buttonPanel.add(checkoutButton);
        buttonPanel.add(checkoutButton);
        
        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	name = nameOnCardField.getText();
            	dispose();
                previousFrame.dispose();
                JFrame confirmationScreen = new JFrame("Transaction Confirmation");
                int ticketNum = tickets.size();
                confirmationScreen.setSize(550, 240 + ticketNum * 100);
                confirmationScreen.setResizable(false);
                confirmationScreen.setLocationRelativeTo(null);

                JPanel confirmationPanel = new JPanel();
                confirmationPanel.setLayout(new BoxLayout(confirmationPanel, BoxLayout.Y_AXIS));

                JLabel totalLabel = new JLabel("Total: " + formatter.format(total), SwingConstants.CENTER);
                totalLabel.setFont(boldFont);
                JLabel destinationLabel = new JLabel("Destination: " + train.getDestination(), SwingConstants.CENTER);
                destinationLabel.setFont(boldFont);
                JLabel departureLabel = new JLabel("Time of Departure: " + train.getDeparture(), SwingConstants.CENTER);
                departureLabel.setFont(boldFont);
                confirmationPanel.add(totalLabel);
                confirmationPanel.add(departureLabel);
                confirmationPanel.add(destinationLabel);
                totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                destinationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                departureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                totalLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
                destinationLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
                departureLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

                // Add the list of tickets to the panel
                JPanel ticketPanel = new JPanel();
                ticketPanel.setLayout(new BoxLayout(ticketPanel, BoxLayout.Y_AXIS));
                JLabel ticketLabel = new JLabel("Tickets Purchased (" + tickets.size() + "):");
                ticketLabel.setFont(boldFont);
                ticketLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                ticketPanel.add(ticketLabel);
                
                for (Ticket ticket : tickets) {
                	int seatNumber = ticket.getSeatNumber();
                	Seat seat = train.getSeatFromNumber(seatNumber);
                	
                	JLabel newLineLabel = new JLabel(" ");
                	newLineLabel.setFont(boldFont);
                	newLineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                	ticketPanel.add(newLineLabel);
                	
                	JLabel idLabel = new JLabel("Ticket ID: " + ticket.getId());
                	idLabel.setFont(boldFont);
                	idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                	ticketPanel.add(idLabel);
                	
                	JLabel seatNumberLabel = new JLabel("Seat Number: " + seatNumber);
                	seatNumberLabel.setFont(font);
                	seatNumberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                	ticketPanel.add(seatNumberLabel);
                	
                	JLabel seatTypeLabel = new JLabel("Seat Type: " + seat.getSeatType());
                	seatTypeLabel.setFont(font);
                	seatTypeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                	ticketPanel.add(seatTypeLabel);
                	
                	JLabel ticketSubtotalLabel = new JLabel("Subtotal: " + formatter.format(ticket.getPrice()));
                	ticketSubtotalLabel.setFont(font);
                	ticketSubtotalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                	ticketPanel.add(ticketSubtotalLabel);
                	
                	ticket.setPassengerName(name);
                	FileManager infoReader = new FileManager();
                	try {
                		infoReader.writeToTicket(ticket);
                	}
                	catch(IOException e1) {
                		e1.printStackTrace();
                	}
                }
                
        		FileManager infoReader = new FileManager();
            	try {
            		infoReader.updateTrains();
            	}
            	catch(IOException e1) {
            		e1.printStackTrace();
            	}

                ticketPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
                confirmationPanel.add(ticketPanel);

                confirmationScreen.add(confirmationPanel, BorderLayout.CENTER);

                JLabel messageLabel = new JLabel("Transaction Completed Successfully!", SwingConstants.CENTER);
                messageLabel.setFont(titleFont);
                messageLabel.setForeground(blue);
                messageLabel.setPreferredSize(new Dimension(0, 50));
                confirmationScreen.add(messageLabel, BorderLayout.NORTH);
                confirmationScreen.setVisible(true);
                confirmationScreen.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        HomeScreen.setBookingScreenOpen(false);
                    }
                });
            }
        });
        
        // Creates the title label
        JLabel title = new JLabel("Checkout", SwingConstants.CENTER);
        title.setFont(titleFont);
        title.setForeground(blue);
        title.setPreferredSize(new Dimension(0, 40));
        
        add(title, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setSize(400, 330);
        setVisible(true);
    }
}
