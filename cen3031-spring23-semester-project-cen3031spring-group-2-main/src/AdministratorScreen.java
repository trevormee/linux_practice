/*
	AdministratorScreen.java
	
	This class creates the administrator suite screen, which is used by administrators to view ticket information.
	
	@author Eli Turner
	@author Sam Zou
*/

package mainProject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

public class AdministratorScreen extends JFrame implements ActionListener {

	// Class fields
	private static final long serialVersionUID = 1L;

	// Fonts
	private Font font = new Font("Helvetica", Font.PLAIN, 14);
	private Font boldFont = new Font("Helvetica", Font.BOLD, 14);

	// Set the theme colors
	private Color blue = new Color(23, 73, 114);
	private Color buttonBlue = new Color(52, 152, 219);
	private Color white = new Color(241, 241, 241);
	private Color selectionWhite = new Color(200, 200, 170);
	private Color sidePanelWhite = new Color(241, 241, 241);
	private Color buttonWhite = new Color(200, 200, 200);
	private Color buttonRed = new Color(217, 30, 24);
	private Color black = new Color(14, 14, 14);

	// Shared GUI variables
	private CardLayout cardLayout;
	private JPanel middlePanelContainer;
	
	// Shared management variables
	FileManager manager = new FileManager();
	private Admin admin = new Admin();

	public AdministratorScreen() {
		// Initialize the window
		setTitle("Administrator Suite");
		setResizable(false);
		setSize(800, 600);
		setLocationRelativeTo(null);
		JPanel loginPanel = createLoginPanel();
		add(loginPanel, BorderLayout.CENTER);
		setVisible(true);
	}

	// Create the login panel
	private JPanel createLoginPanel() {
		// Create panels for username and password
		JPanel loginPanel = new JPanel();
		loginPanel.setLayout(new GridBagLayout());
		loginPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.LINE_END;
		JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setPreferredSize(new Dimension(100, 30));
		usernameLabel.setFont(boldFont);
		usernamePanel.add(usernameLabel);
		JTextField usernameField = new JTextField(20);
		usernameField.setPreferredSize(new Dimension(300, 30));
		usernameField.setMaximumSize(new Dimension(300, 30));
		usernameField.setBorder(new EmptyBorder(5, 5, 5, 5));
		usernameField.setFont(font);
		usernamePanel.add(usernameField);
		loginPanel.add(usernamePanel, gbc);
		gbc.gridy++;
		JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setPreferredSize(new Dimension(100, 30));
		passwordLabel.setFont(boldFont);
		passwordPanel.add(passwordLabel);
		JPasswordField passwordField = new JPasswordField(20);
		passwordField.setPreferredSize(new Dimension(300, 30));
		passwordField.setMaximumSize(new Dimension(300, 30));
		passwordField.setBorder(new EmptyBorder(5, 5, 5, 5));
		passwordField.setFont(font);
		passwordPanel.add(passwordField);
		loginPanel.add(passwordPanel, gbc);

		// Create panel for login button
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setBackground(blue);
		JButton loginButton = new JButton("Login");
		loginButton.addActionListener(this);
		loginButton.setBackground(buttonBlue);
		loginButton.setForeground(white);
		loginButton.setFocusPainted(false);
		loginButton.setBorder(BorderFactory.createEmptyBorder(20, 35, 20, 35));
		loginButton.setPreferredSize(new Dimension(200, 35));
		loginButton.setFont(new Font("Helvetica", Font.BOLD, 14));
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Display the admin suite if the username and password are validated
				String username = usernameField.getText();
				String password = new String(passwordField.getPassword());
				
				if (admin.validatePassword(username, password)) {
					admin.setActivity("Successful login");
					try {
						manager.writeAudit(admin);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					loginPanel.setVisible(false);
					buttonPanel.setVisible(false);
					JPanel adminPanel = null;
					try {
						adminPanel = createAdminPanel();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					add(adminPanel);
				} else {
					admin.setActivity("Invalid login: " + username + " / " + password);
					try {
						manager.writeAudit(admin);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					JLabel errorMessage = new JLabel("Invalid username or password");
					errorMessage.setFont(font);
					JOptionPane.showMessageDialog(null, errorMessage, "Login Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		buttonPanel.add(loginButton);
		add(loginPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		setVisible(true);

		return loginPanel;
	}

	private JPanel createAdminPanel() throws IOException {
		JPanel adminPanel = new JPanel();
		adminPanel.setLayout(new BorderLayout());
		JPanel topBar = createTopBar();
		adminPanel.add(topBar, BorderLayout.NORTH);
		JPanel sidePanel = createSidePanel();
		adminPanel.add(sidePanel, BorderLayout.WEST);
		cardLayout = new CardLayout();
		middlePanelContainer = createMiddlePanels();
		adminPanel.add(middlePanelContainer);

		return adminPanel;
	}

	private JPanel createTopBar() {
		JPanel topBar = new JPanel();
		topBar.setLayout(new BorderLayout());
		JButton logoutButton = new JButton("Logout");
		JLabel usernameLabel = new JLabel(admin.getLoggedInUsername());
		usernameLabel.setFont(font);
		usernameLabel.setForeground(white);
		usernameLabel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		usernameLabel.setHorizontalAlignment(JLabel.RIGHT);
		topBar.add(usernameLabel, BorderLayout.CENTER);
		logoutButton.setBackground(buttonBlue);
		logoutButton.setForeground(white);
		logoutButton.setFont(boldFont);
		logoutButton.setFocusPainted(false);
		logoutButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		logoutButton.setPreferredSize(new Dimension(100, 25));
		logoutButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	HomeScreen.setAdminScreenOpen(false);
		        dispose();
		    }
		});
		topBar.add(logoutButton, BorderLayout.EAST);
		topBar.setBackground(blue);
		topBar.setPreferredSize(new Dimension(getPreferredSize().width, 50));
		topBar.setBorder(new EmptyBorder(8, 12, 8, 12));

		return topBar;
	}

	private JPanel createSidePanel() {
		JPanel sidePanel = new JPanel();
		sidePanel.setOpaque(true);
		sidePanel.setBackground(sidePanelWhite);
		sidePanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.insets = new Insets(7, 7, 0, 7); // Padding

		gbc.gridy = 0;
		gbc.weighty = 0.0;
		JButton trainListButton = createSideButton("Train List");
		trainListButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        cardLayout.show(middlePanelContainer, "TrainListPanel");
		    }
		});
		sidePanel.add(trainListButton, gbc);
		
		gbc.gridy = 1;
		gbc.weighty = 1.0;
		JButton logsButton = createSideButton("Audit Logs");
		logsButton.setEnabled(true);
		logsButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        cardLayout.show(middlePanelContainer, "LogsPanel");
		    }
		});
		sidePanel.add(logsButton, gbc);

		return sidePanel;
	}

	private JButton createSideButton(String text) {
		JButton button = new JButton(text);
		button.setBackground(buttonWhite);
		button.setForeground(black);
		button.setFont(boldFont);
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		button.setPreferredSize(new Dimension(125, 35));
		button.setMaximumSize(new Dimension(125, 35));

		return button;
	}

	private JPanel createMiddlePanels() throws IOException {
		JPanel middlePanelContainer = new JPanel(cardLayout);
		JPanel trainListPanel = createTrainListPanel(middlePanelContainer);
		JPanel auditLogPanel = createAuditLogPanel(middlePanelContainer);
		middlePanelContainer.add(trainListPanel, "TrainListPanel");
		middlePanelContainer.add(auditLogPanel, "LogsPanel");
		
		return middlePanelContainer;
	}
	
	private JPanel createTrainListPanel(JPanel middlePanelContainer) throws IOException {
		JPanel trainListPanel = new JPanel();
		trainListPanel.setLayout(new BorderLayout());

		ArrayList<String> trainNames = new ArrayList<>();
		final ArrayList<Train> trainsRead = FileManager.readTrains();
		ArrayList<Train> trains = trainsRead;
		
		for (int i = 0; i < trains.size(); i++) {
			Train train = trains.get(i);
			trainNames.add(train.getDestination() + " (" + train.getDeparture() + ")");
		}

		JList<String> jList = new JList<>(trainNames.toArray(new String[0]));
		JScrollPane trainList = createList(jList);
		trainListPanel.add(trainList, BorderLayout.CENTER);

		JTextField trainSearchBar = createSearchBar("Search for a train...");
		trainSearchBar.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				search((JList<String>) trainList.getViewport().getView(), trainSearchBar.getText());
			}
		});
		trainListPanel.add(trainSearchBar, BorderLayout.NORTH);
		
		JPanel bottomButtonPanel = createBottomButtonPanel("View Tickets", false);
		JButton buttonFromPanel = (JButton) bottomButtonPanel.getComponent(0);
		buttonFromPanel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = jList.getSelectedIndex();
				if (index >= 0) {
					Train train = trains.get(index);
					JPanel ticketListPanel = null;
					try {
						ticketListPanel = createTicketListPanel(train);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					middlePanelContainer.add(ticketListPanel, "TicketListPanel");
					cardLayout.show(middlePanelContainer, "TicketListPanel");
				}
			}
		});
		trainListPanel.add(bottomButtonPanel, BorderLayout.SOUTH);
		
		return trainListPanel;
	}
	
	private JPanel createAuditLogPanel(JPanel middlePanelContainer) throws IOException {
		JPanel auditLogPanel = new JPanel();
		auditLogPanel.setLayout(new BorderLayout());

		ArrayList<String> auditLog = new ArrayList<>();
		final ArrayList<Admin> auditRead = FileManager.readAudit();
		ArrayList<Admin> logs = auditRead;
		
		for (int i = 0; i < logs.size(); i++) {
			Admin admin = logs.get(i);
			auditLog.add(admin.getAudit());
		}

		JList<String> jList = new JList<>(auditLog.toArray(new String[0]));
		JScrollPane auditList = createList(jList);
		auditLogPanel.add(auditList, BorderLayout.CENTER);

		JTextField auditSearchBar = createSearchBar("Search for a log...");
		auditSearchBar.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				search((JList<String>) auditList.getViewport().getView(), auditSearchBar.getText());
			}
		});
		auditLogPanel.add(auditSearchBar, BorderLayout.NORTH);
		
		return auditLogPanel;
	}

	private JPanel createTicketListPanel(Train train) throws IOException {
		JPanel ticketListPanel = new JPanel();
		ticketListPanel.setLayout(new BorderLayout());

		ArrayList<String> ticketNames = new ArrayList<>();
		ArrayList<Ticket> ticketsRead = FileManager.readTickets();
		ArrayList<Ticket> tickets = filterTicketsByTrainId(ticketsRead, train.getId());
		
		for (int i = 0; i < tickets.size(); i++) {
			ticketNames.add("Ticket " + tickets.get(i).getId());
		}

		JList<String> jList = new JList<>(ticketNames.toArray(new String[0]));
		JScrollPane ticketList = createList(jList);
		ticketListPanel.add(ticketList, BorderLayout.CENTER);

		JTextField ticketSearchBar = createSearchBar("Search for a ticket...");
		ticketSearchBar.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				search((JList<String>) ticketList.getViewport().getView(), ticketSearchBar.getText());
			}
		});
		ticketListPanel.add(ticketSearchBar, BorderLayout.NORTH);
		
		JPanel bottomButtonPanel = createBottomButtonPanel("Edit Ticket", true);
		JButton backButtonFromPanel = (JButton) bottomButtonPanel.getComponent(0);
		JButton buttonFromPanel = (JButton) bottomButtonPanel.getComponent(1);
		buttonFromPanel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = jList.getSelectedIndex();
				if (index >= 0) {
					Ticket ticket = tickets.get(index);
					JPanel editTicketPanel = null;
					editTicketPanel = createEditTicketPanel(train, ticket);
					middlePanelContainer.add(editTicketPanel, "EditTicketPanel");
					cardLayout.show(middlePanelContainer, "EditTicketPanel");
				}
			}
		});
		backButtonFromPanel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(middlePanelContainer, "TrainListPanel");
			}
		});
		ticketListPanel.add(bottomButtonPanel, BorderLayout.SOUTH);
		
		return ticketListPanel;
	}
	
	public JPanel createEditTicketPanel(Train train, Ticket ticket) {
	    JPanel editTicketPanel = new JPanel();
	    editTicketPanel.setLayout(new BorderLayout());

	    JPanel entryPanel = new JPanel();
	    entryPanel.setLayout(new GridBagLayout());

	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.anchor = GridBagConstraints.WEST;
	    gbc.insets = new Insets(5, 5, 5, 5);
	    gbc.fill = GridBagConstraints.HORIZONTAL;

	    JLabel idLabel = new JLabel("Ticket ID:");
	    idLabel.setFont(boldFont);
	    entryPanel.add(idLabel, gbc);
	    gbc.gridx++;

	    JTextField idField = new JTextField(String.valueOf(ticket.getId()));
	    idField.setPreferredSize(new Dimension(200, 30));
	    idField.setFont(font);
	    entryPanel.add(idField, gbc);
	    gbc.gridx = 0;
	    gbc.gridy++;
	    
	    JLabel seatIdLabel = new JLabel("Seat ID:");
	    seatIdLabel.setFont(boldFont);
	    entryPanel.add(seatIdLabel, gbc);
	    gbc.gridx++;

	    JTextField seatIdField = new JTextField(String.valueOf(ticket.getSeatNumber()));
	    seatIdField.setPreferredSize(new Dimension(200, 30));
	    seatIdField.setFont(font);
	    entryPanel.add(seatIdField, gbc);
	    gbc.gridx = 0;
	    gbc.gridy++;

	    JLabel nameLabel = new JLabel("Passenger Name:");
	    nameLabel.setFont(boldFont);
	    entryPanel.add(nameLabel, gbc);
	    gbc.gridx++;

	    JTextField nameField = new JTextField(ticket.getPassengerName());
	    nameField.setPreferredSize(new Dimension(200, 30));
	    nameField.setFont(font);
	    entryPanel.add(nameField, gbc);
	    gbc.gridx = 0;
	    gbc.gridy++;

	    JLabel trainLabel = new JLabel("Train ID:");
	    trainLabel.setFont(boldFont);
	    entryPanel.add(trainLabel, gbc);
	    gbc.gridx++;

	    JTextField trainField = new JTextField(String.valueOf(ticket.getTrainId()));
	    trainField.setPreferredSize(new Dimension(200, 30));
	    trainField.setFont(font);
	    entryPanel.add(trainField, gbc);

	    editTicketPanel.add(entryPanel, BorderLayout.NORTH);
	    entryPanel.setPreferredSize(new Dimension(0, 200));
	    
	    JPanel checklistPanel = new JPanel(new GridBagLayout());
	    JPanel checklist = createPackagesChecklist(ticket);
	    checklistPanel.add(checklist, new GridBagConstraints());

	    editTicketPanel.add(checklistPanel, BorderLayout.CENTER);

	    JPanel bottomButtonPanel = createBottomButtonPanel("Save", true);
	    JButton backButton = (JButton) bottomButtonPanel.getComponent(0);
	    JButton saveButton = (JButton) bottomButtonPanel.getComponent(1);
	    saveButton.addActionListener(e -> {
	    	int oldTicketId = ticket.getId();
	        int newTicketId = Integer.parseInt(idField.getText());
	        int newSeatNumber = Integer.parseInt(seatIdField.getText());
	        String newPassengerName = nameField.getText();
	        int newTrainId = Integer.parseInt(trainField.getText());
	        Train oldTrain = Train.getTrainFromId(ticket.getTrainId());
	        Train newTrain = Train.getTrainFromId(newTrainId);
	        int oldSeatNumber = ticket.getSeatNumber();
	        Seat oldSeat = oldTrain.getSeatFromNumber(oldSeatNumber);
	        Seat newSeat = newTrain.getSeatFromNumber(newSeatNumber);
	        oldSeat.setSeatAvailable(false);
	        newSeat.setSeatAvailable(true);
	        ticket.setTicketId(newTicketId);
	        ticket.setSeatNumber(newSeatNumber);
	        ticket.setPassengerName(newPassengerName);
	        ticket.setTrainId(newTrainId);
			Component[] components = checklist.getComponents();
			ArrayList<Integer> selectedPackages = new ArrayList<>();
			for (int i = 0; i < components.length; i++) {
				if (!(components[i] instanceof JLabel)) {
					JCheckBox checkbox = (JCheckBox) components[i];
					if (checkbox.isSelected()) {
						selectedPackages.add(i);
					}
				}
			}
			ticket.setPackages(selectedPackages);
	        FileManager infoReader = new FileManager();
	        infoReader.updateTicket(oldTicketId, ticket);
	        try {
				infoReader.updateTrains();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        try {
				HomeScreen.loadTickets();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        try {
				HomeScreen.loadTrains();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        admin.setActivity("Edited ticket " + ticket.getId());
			try {
				manager.writeAudit(admin);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	        JLabel messageLabel = new JLabel("Changes saved.");
	        messageLabel.setFont(boldFont);
	        JOptionPane.showMessageDialog(editTicketPanel, messageLabel);
	    });
	    backButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	JPanel ticketListPanel = null;
				try {
					ticketListPanel = createTicketListPanel(train);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				middlePanelContainer.add(ticketListPanel, "TicketListPanel");
				cardLayout.show(middlePanelContainer, "TicketListPanel");
	        }
	    });
	    editTicketPanel.add(bottomButtonPanel, BorderLayout.SOUTH);

	    return editTicketPanel;
	}
	
	public JPanel createPackagesChecklist(Ticket ticket) {
		// Checklist
		JPanel checklist = new JPanel(new GridBagLayout());
		checklist.setVisible(true);
		checklist.setPreferredSize(new Dimension(0, 300));

		// Add label above the checklist
		JLabel label = new JLabel("Packages Included:");
		label.setFont(boldFont);
		GridBagConstraints labelGbc = new GridBagConstraints();
		labelGbc.gridx = 0;
		labelGbc.gridy = 0;
		labelGbc.anchor = GridBagConstraints.WEST;
		labelGbc.insets = new Insets(5, 5, 5, 5);
		checklist.add(label, labelGbc);

		ArrayList<Integer> packages = ticket.getPackages();
		
		// Set the font and color of the checkbox label, and add padding
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1; // start after the label
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		for (int i = 0; i < PackageSet.getSize(); i++) {
			JCheckBox checkbox = new JCheckBox(PackageSet.getPackageName(i));
			checkbox.setFont(boldFont);
			gbc.gridy = i + 1; // increment by 1 to start after the label
			checklist.add(checkbox, gbc);
			gbc.gridy++;
		}

		for (int i = 0; i < packages.size(); i++) {
			if (!(checklist.getComponent(packages.get(i)) instanceof JLabel)) {
				JCheckBox checkbox = (JCheckBox) checklist.getComponent(packages.get(i));
				checkbox.setSelected(true);
			}
		}
		
		return checklist;
	}
	
	public ArrayList<Ticket> filterTicketsByTrainId(ArrayList<Ticket> tickets, int trainId) {
	    ArrayList<Ticket> returnTickets = new ArrayList<>();
		for (int i = 0; i < tickets.size(); i++) {
	    	Ticket ticket = tickets.get(i);
	    	if (ticket.getTrainId() == trainId) {
	    		returnTickets.add(ticket);
	    	}
	    }
		return returnTickets;
	}
	
	private JScrollPane createList(JList<String> list) {
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(-1);
		list.setFixedCellWidth(700);
		list.setFont(boldFont);
		list.setBackground(white);
		list.setForeground(black);
		list.setSelectionBackground(selectionWhite);
		list.setFocusable(false);
		JScrollPane scrollPane = new JScrollPane(list);

		return scrollPane;
	}

	private JTextField createSearchBar(String defaultText) {
		JTextField searchField = new JTextField();
		searchField.setFont(font);
		searchField.setBorder(BorderFactory.createCompoundBorder(searchField.getBorder(), BorderFactory.createEmptyBorder(5, 7, 5, 7)));
		searchField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				// Remove the placeholder message when the text box is focused
				if (searchField.getText().equals(defaultText)) {
					searchField.setText("");
					searchField.setForeground(black);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				// Show the placeholder message when the text box is unfocused and empty
				if (searchField.getText().isEmpty()) {
					searchField.setText(defaultText);
					searchField.setForeground(Color.GRAY);
				}
			}
		});
		searchField.setText(defaultText);
		searchField.setForeground(Color.GRAY);

		return searchField;
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
	
	private void search(JList<String> list, String text) {
        ListModel<String> model = list.getModel();
        int size = model.getSize();
        for (int i = 0; i < size; i++) {
            String element = model.getElementAt(i);
            if (element.toLowerCase().contains(text.toLowerCase())) {
            	list.setSelectedIndex(i);
            	list.ensureIndexIsVisible(i);
                break;
            }
        }
	}

	@Override
	public void actionPerformed(ActionEvent e) {}
	
}
