/*
	TicketTest.java
	@author Trever Mee
	@author Eli Turner
*/

package mainProject;

import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class TicketTest {

    @Test
    public void testGetId() {
        Ticket ticket = new Ticket(3, 10.99, new ArrayList<Integer>(), 1);
        assertEquals(3, ticket.getId());

        ticket = new Ticket(21, 13.99, new ArrayList<Integer>(), 2);
        assertEquals(4, ticket.getId());

        ticket = new Ticket(12, 15.99, new ArrayList<Integer>(), 3);
        assertEquals(5, ticket.getId());
    }

    @Test
    public void testToStr() {
        Ticket ticket = new Ticket(1, 50.0, new ArrayList<Integer>(), 1);
        ticket.setPassengerName("John Doe");
        assertEquals("6;1;1;John Doe;50.0;", ticket.toString());

        ticket = new Ticket(2, 60.0, new ArrayList<Integer>(), 1);
        ticket.setPassengerName("Jane Doe");
        assertEquals("7;1;2;Jane Doe;60.0;", ticket.toString());
    }

    @Test
    public void testGetSeatNumber() {
        Ticket ticket = new Ticket(1, 50.0, new ArrayList<Integer>(), 1);
        assertEquals(1, ticket.getSeatNumber());

        ticket = new Ticket(2, 60.0, new ArrayList<Integer>(), 1);
        assertEquals(2, ticket.getSeatNumber());
    }

    @Test
    public void testGetPrice() {
        ArrayList<Integer> packages = new ArrayList<Integer>();
        packages.add(0);
        packages.add(2);

        Ticket ticket = new Ticket(1, 50.0, packages, 1);
        assertEquals(50.0, ticket.getPrice(), 0.001);

        packages = new ArrayList<Integer>();
        packages.add(1);
        packages.add(3);

        ticket = new Ticket(2, 60.0, packages, 1);
        assertEquals(60.0, ticket.getPrice(), 0.001);
    }
}
