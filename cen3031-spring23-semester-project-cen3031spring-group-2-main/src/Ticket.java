/*
	Ticket.java
	
	This class holds ticket data for payment and file saving.
	
	@author Sam Zou
	@author Eli Turner
*/

package mainProject;

import java.util.ArrayList;

public class Ticket {
	private static int amountOfTickets = 0;
	private int ticketId;
	private double subTotal;
	private String passengerName;
	private ArrayList<Integer> packages;
	private int seatNumber;
	private int trainId;

	public Ticket(int ticketId, int seatNumber, double subTotal, String passengerName, ArrayList<Integer> packages,
			int trainId) {
		this.ticketId = ticketId;
		this.seatNumber = seatNumber;
		this.passengerName = passengerName;
		this.subTotal = subTotal;
		this.packages = packages;
		this.trainId = trainId;
	}

	public Ticket(int seatNumber, double subTotal, ArrayList<Integer> packages, int trainId) {
		ticketId = ++amountOfTickets;
		this.seatNumber = seatNumber;
		this.subTotal = subTotal;
		this.packages = packages;
		this.trainId = trainId;
	}

	public int getId() {
		return ticketId;
	}
	
	public void setTicketId(int ticketId) {
		this.ticketId = ticketId;
	}

	public int getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(int seatNumber) {
		this.seatNumber = seatNumber;
	}
	
	public double getPrice() {
		double totalPrice = subTotal;
		for (int i = 0; i < packages.size(); i++) {
			int packageIndex = packages.get(i);
			totalPrice += PackageSet.getPackagePrice(packageIndex);
		}
		return totalPrice;
	}

	public String getPassengerName() {
		return passengerName;
	}

	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}

	public int getTrainId() {
		return trainId;
	}
	
	public void setTrainId(int trainId) {
		this.trainId = trainId;
	}

	public static int getAmountOfTickets() {
		return amountOfTickets;
	}

	public static void setAmountOfTickets(int amountOfTickets) {
		Ticket.amountOfTickets = amountOfTickets;
	}

	public ArrayList<Integer> getPackages() {
		return packages;
	}
	
	public void setPackages(ArrayList<Integer> packages) {
		this.packages = packages;
	}

	@Override
	public String toString() {
		// Format example: 1;1;23;John Doe;19.99;3,2
		String packagesStr = "";
		int numPackages = packages.size();
		for (int i = 0; i < numPackages; i++) {
			packagesStr += packages.get(i).toString();
			if (i < numPackages - 1) {
				packagesStr += ",";
			}
		}
		String returnStr = ticketId + ";" + trainId + ";" + seatNumber + ";" + passengerName + ";" + subTotal + ";"
				+ packagesStr;
		return returnStr;
	}
}
