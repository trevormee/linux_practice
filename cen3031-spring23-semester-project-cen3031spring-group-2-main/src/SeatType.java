/*
	SeatType.java
	
	This class stores the seat types in a uniform format for use by other classes.
	
	@author Trevor Mee
*/

package mainProject;

public enum SeatType {
	FIRST_CLASS(10.99), BUSINESS_CLASS(8.99), ECONOMY_CLASS(6.99);

	private double price;

	SeatType(double price) {
		this.price = price;
	}

	public double getPrice() {
		return price;
	}
}
