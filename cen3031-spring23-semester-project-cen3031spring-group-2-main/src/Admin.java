/*
	Admin.java
	
	This class holds admin data for login and auditing.
	
	@author Reece Sellers
	@author Eli Turner
	@author Sam Zou
*/

package mainProject;

import java.time.LocalDateTime;

public class Admin {
	
	private String[] username = {"admin","administrator","sudo","dev","security"};
	private String[] password = {"password","password123","superuser","developer","onion"};
	private String activity;
	private int maxUsers = username.length;
	private String loginSuccess;
	private LocalDateTime time;
	
	public Admin() {
		loginSuccess = "N/A";
		time = LocalDateTime.now();
		activity = "";
	}
	
	public Admin(LocalDateTime time, String user, String action) {
		this.time = time;
		this.loginSuccess = user;
		this.activity = action;
	}
	
	public Boolean validatePassword(String username, String password) {
		for (int i = 0; i < maxUsers; i++) {
			if (username.equals(this.username[i]) && password.equals(this.password[i])) {
				loginSuccess = this.username[i];
				return true;
			}
		}
		return false;
	}
	
	public String getLoggedInUsername() {
		return loginSuccess;
	}
	
	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getAudit() {
		return time.getYear() + "-" + time.getMonthValue() + "-" + time.getDayOfMonth() + " @" + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond() + " - "
				+ getLoggedInUsername() + " performed action: " + activity + "\n";
	}
	
	public String toString() {
		/*
		 * Format example: @timestamp;user;action.taken
		 * 				   2023-04-11-13:56:11.057;admin;viewed-ticket[id]
		 */
			return time.getYear() + "-" + time.getMonthValue() + "-" + time.getDayOfMonth() + "T" + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond() + ";"
				+ getLoggedInUsername() + ";" + activity + "\n";
	}
}
