import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Trip {
    private int id;
    private String destination;
    private String date;  // simple String (e.g., 2025-12-01)
    private double price;
    private int seatsAvailable;

    public Trip(int id, String destination, String date, double price, int seatsAvailable) {
        this.id = id;
        this.destination = destination;
        this.date = date;
        this.price = price;
        this.seatsAvailable = seatsAvailable;
    }

    public int getId() {
        return id;
    }

    public String getDestination() {
        return destination;
    }

    public String getDate() {
        return date;
    }

    public double getPrice() {
        return price;
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public void reduceSeat() {
        if (seatsAvailable > 0) {
            seatsAvailable--;
        }
    }

    @Override
    public String toString() {
        return "Trip ID: " + id +
                ", Destination: " + destination +
                ", Date: " + date +
                ", Price: " + price +
                ", Seats Available: " + seatsAvailable;
    }
}

class Customer {
    private int id;
    private String name;
    private String phone;

    public Customer(int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Customer ID: " + id +
                ", Name: " + name +
                ", Phone: " + phone;
    }
}

class Booking {
    private int bookingId;
    private Customer customer;
    private Trip trip;

    public Booking(int bookingId, Customer customer, Trip trip) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.trip = trip;
    }

    @Override
    public String toString() {
        return "Booking ID: " + bookingId +
                ", Customer: " + customer.getId() +
                ", Trip: " + trip.getId() +
                " (" + trip.getDestination() + " on " + trip.getDate() + ")";
    }
}

public class TravelManagementSystem {

    private static Scanner scanner = new Scanner(System.in);
    private static List<Trip> trips = new ArrayList<>();
    private static List<Customer> customers = new ArrayList<>();
    private static List<Booking> bookings = new ArrayList<>();

    private static int nextTripId = 1;
    private static int nextCustomerId = 1;
    private static int nextBookingId = 1;

    public static void main(String[] args) {
        int choice;
        do {
            showMainMenu();
            choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1:
                    addTrip();
                    break;
                case 2:
                    viewTrips();
                    break;
                case 3:
                    addCustomer();
                    break;
                case 4:
                    viewCustomers();
                    break;
                case 5:
                    createBooking();
                    break;
                case 6:
                    viewBookings();
                    break;
                case 0:
                    System.out.println("Exiting Travel Management System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
            System.out.println();
        } while (choice != 0);
    }

    private static void showMainMenu() {
        System.out.println("========================================");
        System.out.println("       TRAVEL MANAGEMENT SYSTEM");
        System.out.println("========================================");
        System.out.println("1. Add Trip");
        System.out.println("2. View All Trips");
        System.out.println("3. Register Customer");
        System.out.println("4. View All Customers");
        System.out.println("5. Create Booking");
        System.out.println("6. View All Bookings");
        System.out.println("0. Exit");
        System.out.println("========================================");
    }

    // ------- Utility Input Methods -------
    private static int readInt(String msg) {
        System.out.print(msg);
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // consume newline
        return value;
    }

    private static double readDouble(String msg) {
        System.out.print(msg);
        while (!scanner.hasNextDouble()) {
            System.out.print("Please enter a valid number: ");
            scanner.next();
        }
        double value = scanner.nextDouble();
        scanner.nextLine(); // consume newline
        return value;
    }

    private static String readLine(String msg) {
        System.out.print(msg);
        return scanner.nextLine();
    }

    // ------- Trip Management -------
    private static void addTrip() {
        System.out.println("--- Add New Trip ---");
        String destination = readLine("Enter destination: ");
        String date = readLine("Enter date (e.g., 2025-12-01): ");
        double price = readDouble("Enter price: ");
        int seats = readInt("Enter number of seats available: ");

        Trip trip = new Trip(nextTripId++, destination, date, price, seats);
        trips.add(trip);
        System.out.println("Trip added successfully with ID: " + trip.getId());
    }

    private static void viewTrips() {
        System.out.println("--- List of Trips ---");
        if (trips.isEmpty()) {
            System.out.println("No trips available.");
        } else {
            for (Trip t : trips) {
                System.out.println(t);
            }
        }
    }

    // ------- Customer Management -------
    private static void addCustomer() {
        System.out.println("--- Register New Customer ---");
        String name = readLine("Enter customer name: ");
        String phone = readLine("Enter customer phone: ");

        Customer customer = new Customer(nextCustomerId++, name, phone);
        customers.add(customer);
        System.out.println("Customer registered successfully with ID: " + customer.getId());
    }

    private static void viewCustomers() {
        System.out.println("--- List of Customers ---");
        if (customers.isEmpty()) {
            System.out.println("No customers registered.");
        } else {
            for (Customer c : customers) {
                System.out.println(c);
            }
        }
    }

    // ------- Booking Management -------
    private static void createBooking() {
        System.out.println("--- Create Booking ---");
        if (customers.isEmpty()) {
            System.out.println("No customers found. Please register a customer first.");
            return;
        }
        if (trips.isEmpty()) {
            System.out.println("No trips found. Please add a trip first.");
            return;
        }

        int customerId = readInt("Enter customer ID: ");
        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found!");
            return;
        }

        int tripId = readInt("Enter trip ID: ");
        Trip trip = findTripById(tripId);
        if (trip == null) {
            System.out.println("Trip not found!");
            return;
        }

        if (trip.getSeatsAvailable() <= 0) {
            System.out.println("No seats available for this trip!");
            return;
        }

        // Create booking
        Booking booking = new Booking(nextBookingId++, customer, trip);
        bookings.add(booking);
        trip.reduceSeat();
        System.out.println("Booking created successfully with Booking ID: " + nextBookingId);
    }

    private static void viewBookings() {
        System.out.println("--- List of Bookings ---");
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
        } else {
            for (Booking b : bookings) {
                System.out.println(b);
            }
        }
    }

    // ------- Helper Search Methods -------
    private static Customer findCustomerById(int id) {
        for (Customer c : customers) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    private static Trip findTripById(int id) {
        for (Trip t : trips) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }
}