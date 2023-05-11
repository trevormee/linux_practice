/*
	RoutesScreen.java
	
	This class creates the route screen, which displays helpful visuals to the user.
	
	@author Selena Chiang
	@author Eli Turner
*/

package mainProject;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class RoutesScreen extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RoutesScreen() {
		super("Train Ticket System");
		setBounds(300, 300, 1000, 800);

		// Initialize the main panel
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(new Color(23, 73, 114));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Create the tabbed subpanel
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		Font font = new Font("Helvetica", Font.BOLD, 14);
		tabbedPane.setFont(font);
		tabbedPane.setFocusable(false);

		tabbedPane.setBounds(10, 11, 900, 700);
		contentPane.add(tabbedPane);

		// Route map
		JLabel routeLabel = new JLabel();
		routeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		ImageIcon routeImage = new ImageIcon(getClass().getClassLoader().getResource("routes.png"));
		routeLabel.setIcon(routeImage);
		tabbedPane.addTab("Destinations", null, routeLabel, null);

		// Schedule graphic
		JLabel scheduleLabel = new JLabel();
		scheduleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		ImageIcon scheduleImage = new ImageIcon(getClass().getClassLoader().getResource("schedule2.png"));
		scheduleLabel.setIcon(scheduleImage);
		tabbedPane.addTab("Train Schedule", null, scheduleLabel, null);
	}
}
