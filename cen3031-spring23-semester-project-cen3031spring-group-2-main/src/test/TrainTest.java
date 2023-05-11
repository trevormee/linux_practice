/*
	 TrainTest.java
	 @author Trevor Mee
 */

package mainProject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

public class TrainTest {
	
	private Train train;
	
	@Before
	public void setUp() {
		// Create a train with 2 rows, 3 columns, and a destination of "New York"
		Seat[][] seats = new Seat[2][3];
		train = new Train(2, 3, seats, "New York", "8:00 AM");
	}
	
	@Test
	public void testGetDestination() {
		// Ensure the destination is "New York"
		assertEquals("New York", train.getDestination());
	}
	
	@Test
	public void testSetDestination() {
		// Set the destination to "Boston" and ensure it was updated
		train.setDestination("Boston");
		assertEquals("Boston", train.getDestination());
	}
	
	@Test
	public void testGetSeatFromNumber() {
		Seat[][] seats = new Seat[2][2];
		seats[0][0] = new Seat(SeatType.FIRST_CLASS, 1, true);
		seats[0][1] = new Seat(SeatType.FIRST_CLASS, 2, false);
		seats[1][0] = new Seat(SeatType.ECONOMY_CLASS, 3, true);
		seats[1][1] = new Seat(SeatType.ECONOMY_CLASS, 4, false);

		Train train = new Train(2, 2, seats, "Chicago", "9:30 AM");

		Seat seat = train.getSeatFromNumber(1);
		assertNotNull(seat);
		assertEquals(1, seat.getSeatNumber());
	}


}
