/*  
    FileManager.java
    
    This class saves and loads data for tickets, trains, and auditing.
    
    @author Reece Sellers
    @author Eli Turner
*/

package mainProject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.io.IOException;

public class FileManager {
	private File trainsFile;
	private static String trainsFileName = "trains.txt";
	private File ticketsFile;
	private static String ticketsFileName = "tickets.txt";
	private File auditsFile;
	private static String auditFileName = "audit.txt";

	public FileManager() {
		trainsFile = new File(trainsFileName);
		ticketsFile = new File(ticketsFileName);
		auditsFile = new File(auditFileName);
	}

	// Grabs ticket data and feeds it into tickets.txt
	public void writeToTicket(Ticket ticket) throws IOException {
		PrintWriter pw = new PrintWriter(new FileWriter(ticketsFile, true));
		pw.write(ticket.toString() + "\n");
		pw.close();
	}
	
	// Overwrites an existing ticket from the ticket file
	public void updateTicket(int oldTicketId, Ticket ticket) {
	    List<String> lines;
	    try {
	        lines = Files.readAllLines(Paths.get(ticketsFileName));
	    } catch (IOException e) {
	        e.printStackTrace();
	        return;
	    }

	    for (int i = 0; i < lines.size(); i++) {
	        String line = lines.get(i);
	        String[] parts = line.split(";");
	        int id = Integer.parseInt(parts[0]);
	        if (id == oldTicketId) {
	            lines.set(i, ticket.toString());
	            break;
	        }
	    }

	    try {
	        Files.write(Paths.get(ticketsFileName), lines);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	public static ArrayList<Ticket> readTickets() throws IOException {
		ArrayList<Ticket> tickets = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new FileReader(ticketsFileName));
		String line;
		while ((line = reader.readLine()) != null) {
			String[] fields = line.split(";");
			int ticketId = Integer.parseInt(fields[0]);
			int trainId = Integer.parseInt(fields[1]);
			int seatNumber = Integer.parseInt(fields[2]);
			String passengerName = fields[3];
			double subTotal = Double.parseDouble(fields[4]);
			ArrayList<Integer> packages = new ArrayList<>();
			if (fields.length > 5) {
				String packagesStr = fields[5];
				String[] subfields = packagesStr.split(",");
				for (int i = 0; i < subfields.length; i++) {
					int packageId = Integer.parseInt(subfields[i]);
					packages.add(packageId);
				}
			}
			Ticket ticket = new Ticket(ticketId, seatNumber, subTotal, passengerName, packages, trainId);
			tickets.add(ticket);
		}
		Ticket.setAmountOfTickets(tickets.size());
		reader.close();
		return tickets;
	}

	// Grabs train data and feeds it to trains.txt
	public void updateTrains() throws IOException {
		PrintWriter pw = new PrintWriter(new FileWriter(trainsFile, false));
		ArrayList<Train> trains = Train.getTrains();
		for (int i = 0; i < trains.size(); i++) {
			Train train = trains.get(i);
			pw.write(train.toString() + "\n");
		}
		pw.close();
	}

	public static ArrayList<Train> readTrains() throws IOException {
		ArrayList<Train> trains = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new FileReader(trainsFileName));
		String line;
		ArrayList<Ticket> tickets = readTickets();
		while ((line = reader.readLine()) != null) {
			String[] fields = line.split(";");
			int trainId = Integer.parseInt(fields[0]);
			int rows = Integer.parseInt(fields[1]);
			int cols = Integer.parseInt(fields[2]);
			String destination = fields[3];
			String departure = fields[4];
			int numSeats = Integer.parseInt(fields[5]);
			Seat[][] seats = new Seat[rows][cols];
			ArrayList<Integer> occupiedSeats = new ArrayList<>();
			for (int i = 0; i < tickets.size(); i++) {
				if (tickets.get(i).getTrainId() == trainId) {
					occupiedSeats.add(tickets.get(i).getSeatNumber());
				}
			}
			for (int i = 0; i < numSeats; i++) {
				line = reader.readLine();
				fields = line.split(";");
				int seatNumber = Integer.parseInt(fields[0]);
				boolean seatAvailable = true;
				for (int j = 0; j < occupiedSeats.size(); j++) {
					if (occupiedSeats.get(j) == seatNumber) {
						seatAvailable = false;
					}
				}
				//Boolean.parseBoolean(fields[1]);
				String seatClass = fields[1];
				int row = (seatNumber - 1) / cols;
				int col = (seatNumber - 1) % cols;
				Seat seat = new Seat(SeatType.valueOf(seatClass), seatNumber, seatAvailable);
				seats[row][col] = seat;
			}
			Train train = new Train(trainId, rows, cols, seats, destination, departure);
			trains.add(train);
		}
		reader.close();
		return trains;
	}
	
	// TODO: Requires toString() within Admin.java to be complete.
	// Grabs audit data and throws it into audit.txt
	public void writeAudit(Admin admin) throws IOException {
		PrintWriter pw = new PrintWriter(new FileWriter(auditsFile, true));
		pw.write(admin.toString());
		pw.close();
	}
	
	public static ArrayList<Admin> readAudit() throws IOException {
		ArrayList<Admin> logs = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new FileReader(auditFileName));
		String line;
		while ((line = reader.readLine()) != null) {
			String[] fields = line.split(";");
			String time = fields[0];
			String[] largeTimes = time.split("-");
			int year = Integer.parseInt(largeTimes[0]);
			int month = Integer.parseInt(largeTimes[1]);
			int day = Integer.parseInt(largeTimes[2].substring(0,2));
			String[] smallTimes = time.split(":");
			int hour = Integer.parseInt(smallTimes[0].substring(10));
			int minute = Integer.parseInt(smallTimes[1]);
			int second = Integer.parseInt(smallTimes[2]);
			LocalDateTime timeStamp = LocalDateTime.of(year, month, day, hour, minute, second);
			String user = fields[1];
			String action = fields[2];
			Admin admin = new Admin(timeStamp, user, action);
			logs.add(admin);
		}
		reader.close();
		return logs;
	}
}
