/*
    AdminTest.java
    @author Trevor Mee
*/

package mainProject;

import static org.junit.Assert.*;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class AdminTest {

	private Admin admin;

	@Before
	public void setUp() throws Exception {
		admin = new Admin();
	}

	@Test
	public void testValidatePassword() {
		// Test valid login
		assertTrue(admin.validatePassword("admin", "password"));
		assertEquals("admin", admin.getLoggedInUsername());

	}
	
	@Test
	public void testGetLoggedInUsername() {
		Admin admin = new Admin();
		admin.validatePassword("admin", "password");
		assertEquals("admin", admin.getLoggedInUsername());
	}
	

	@Test
	public void testGetAudit() {
		LocalDateTime time = LocalDateTime.now();
		Admin admin = new Admin(time, "admin", "created user");
		assertEquals(time.getYear() + "-" + time.getMonthValue() + "-" + time.getDayOfMonth() + " @" + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond() + " - "
				+ "admin" + " performed action: " + "created user" + "\n", admin.getAudit());
	}

	@Test
	public void testToString() {
		LocalDateTime time = LocalDateTime.of(2023, 4, 26, 10, 30, 0);
		String user = "admin";
		String action = "created-user";
		admin = new Admin(time, user, action);
		assertEquals("2023-4-26T10:30:0;admin;created-user\n", admin.toString());
	}

}

