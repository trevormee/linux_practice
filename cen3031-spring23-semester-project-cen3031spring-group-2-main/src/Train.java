/*
	Train.java
	
	This class holds train data and groups Seat objects together.
	
	@author Eli Turner
*/

package mainProject;

import java.util.ArrayList;

public class Train {
	private int rows;
	private int cols;
	private Seat[][] seats;
	private String destination;
	private String departure;
	private ArrayList<Ticket> tickets;
	private static ArrayList<Train> trains;
	private int trainId;
	private static int globalId = 1;

	public Train(int rows, int cols, Seat[][] seats, String destination, String departure) {
		this.rows = rows;
		this.cols = cols;
		this.seats = seats;
		this.destination = destination;
		this.departure = departure;
		this.trainId = globalId;
		globalId++;
		if (tickets == null) {
			tickets = new ArrayList<>();
		}
		if (trains == null) {
			trains = new ArrayList<>();
		}
		trains.add(this);
	}

	public Train(int id, int rows, int cols, Seat[][] seats, String destination, String departure) {
		this.rows = rows;
		this.cols = cols;
		this.seats = seats;
		this.destination = destination;
		this.departure = departure;
		this.trainId = id;
		if (tickets == null) {
			tickets = new ArrayList<>();
		}
		if (trains == null) {
			trains = new ArrayList<>();
		}
	}

	public static ArrayList<Train> getTrains() {
		return trains;
	}

	public int getId() {
		return trainId;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	public Seat[][] getSeats() {
		return seats;
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public void addTicket(Ticket ticket) {
		tickets.add(ticket);
	}

	public void clearTickets() {
		tickets.clear();
	}

	public Seat getSeatFromNumber(int seatNumber) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (seats[i][j].getSeatNumber() == seatNumber) {
					return seats[i][j];
				}
			}
		}
		return null;
	}
	
	public static Train getTrainFromId(int trainId) {
		for (int i = 0; i < trains.size(); i++) {
			Train train = trains.get(i);
			if (trainId == train.getId()) {
				return train;
			}
		}
		return null;
	}
	
	public void setSeatAvailable(int seatNumber, boolean seatAvailable) {
		Seat seat = getSeatFromNumber(seatNumber);
		seat.setSeatAvailable(seatAvailable);
	}
	
	public static void clearTrains() {
		if (trains != null) {
			trains.clear();
		}
		globalId = 1;
	}

	@Override
	public String toString() {
		/*
		 * Format example: 3;40;4;9:30 AM;2 1;true;FIRST_CLASS 2;false;ECONOMY_CLASS
		 */
		String seatsStr = "";
		int numSeats = rows * cols;
		int totalIndex = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				seatsStr += seats[i][j].toString();
				if (totalIndex < numSeats - 1) {
					seatsStr += "\n";
				}
				totalIndex++;
			}
		}
		String returnString = trainId + ";" + rows + ";" + cols + ";" + destination + ";" + departure + ";" + numSeats
				+ "\n" + seatsStr;
		return returnString;
	}
}
