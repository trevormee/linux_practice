/*
	Seat.java
	
	This class holds seat data for the Train class.
	
	@author Trevor Mee
	@author Eli Turner
*/

package mainProject;

public class Seat {
	private int seatNumber;
	private boolean seatAvailable;
	private SeatType seatType;

	public Seat(SeatType seatType, int seatNumber, boolean seatAvailable) {
		this.seatNumber = seatNumber;
		this.seatType = seatType;
		this.seatAvailable = seatAvailable;
	}

	public int getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(int seatNumber) {
		this.seatNumber = seatNumber;
	}

	public boolean getSeatAvailable() {
		return seatAvailable;
	}

	public void setSeatAvailable(boolean seatAvailable) {
		this.seatAvailable = seatAvailable;
	}

	public SeatType getSeatType() {
		return seatType;
	}

	public void setSeatType(SeatType seatType) {
		this.seatType = seatType;
	}

	@Override
	public String toString() {
		// Format example: 1;true;FIRST_CLASS
		String returnStr = seatNumber + ";" + seatType;
		return returnStr;
	}

}
