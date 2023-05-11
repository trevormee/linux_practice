/*
	SeatTest.java
	@author Trevor Mee
*/
package mainProject;

import org.junit.Test;
import static org.junit.Assert.*;

public class SeatTest {

	@Test
	public void testGetSeatNumber() {
		Seat seat = new Seat(SeatType.ECONOMY_CLASS, 12, true);
		assertEquals(12, seat.getSeatNumber());
	}

	@Test
	public void testGetSeatAvailable() {
		Seat seat = new Seat(SeatType.BUSINESS_CLASS, 1, false);
		assertFalse(seat.getSeatAvailable());
	}

	@Test
	public void testGetSeatType() {
		Seat seat = new Seat(SeatType.FIRST_CLASS, 4, true);
		assertEquals(SeatType.FIRST_CLASS, seat.getSeatType());
	}

	@Test
	public void testSetSeatNumber() {
		Seat seat = new Seat(SeatType.ECONOMY_CLASS, 10, true);
		seat.setSeatNumber(20);
		assertEquals(20, seat.getSeatNumber());
	}

	@Test
	public void testSetSeatAvailable() {
		Seat seat = new Seat(SeatType.BUSINESS_CLASS, 3, false);
		seat.setSeatAvailable(true);
		assertTrue(seat.getSeatAvailable());
	}

	@Test
	public void testSetSeatType() {
		Seat seat = new Seat(SeatType.FIRST_CLASS, 6, true);
		seat.setSeatType(SeatType.ECONOMY_CLASS);
		assertEquals(SeatType.ECONOMY_CLASS, seat.getSeatType());
	}

	@Test
	public void testToString() {
		Seat seat = new Seat(SeatType.BUSINESS_CLASS, 2, false);
		String expected = "2;BUSINESS_CLASS";
		assertEquals(expected, seat.toString());
	}
}
