import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

// Trip class
class Trip {
    private int id;
    private String destination;
    private String date;
    private double price;
    private int seatsAvailable;

    public Trip(int id, String destination, String date, double price, int seatsAvailable) {
        this.id = id;
        this.destination = destination;
        this.date = date;
        this.price = price;
        this.seatsAvailable = seatsAvailable;
    }

    public int getId() { return id; }
    public String getDestination() { return destination; }
    public String getDate() { return date; }
    public double getPrice() { return price; }
    public int getSeatsAvailable() { return seatsAvailable; }

    public void reduceSeat() { if (seatsAvailable > 0) seatsAvailable--; }

    @Override
    public String toString() {
        return "ID: " + id + ", " + destination + " on " + date + ", $" + price + ", Seats: " + seatsAvailable;
    }
}

// Customer class
class Customer {
    private int id;
    private String name;
    private String phone;

    public Customer(int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public int getId() { return id; }

    @Override
    public String toString() {
        return "ID: " + id + ", " + name + ", " + phone;
    }
}

// Booking class
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
        return "Booking ID: " + bookingId + ", Customer: " + customer.getId() +
                ", Trip: " + trip.getId() + " (" + trip.getDestination() + ")";
    }
}

// Main GUI class
public class TravelManagementGUI extends JFrame {
    private List<Trip> trips = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();
    private int nextTripId = 1, nextCustomerId = 1, nextBookingId = 1;

    private JTextArea displayArea;

    public TravelManagementGUI() {
        setTitle("Travel Management System");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 3, 10, 10));

        JButton addTripBtn = new JButton("Add Trip");
        JButton viewTripsBtn = new JButton("View Trips");
        JButton addCustomerBtn = new JButton("Add Customer");
        JButton viewCustomersBtn = new JButton("View Customers");
        JButton createBookingBtn = new JButton("Create Booking");
        JButton viewBookingsBtn = new JButton("View Bookings");
        JButton exitBtn = new JButton("Exit");

        buttonPanel.add(addTripBtn);
        buttonPanel.add(viewTripsBtn);
        buttonPanel.add(addCustomerBtn);
        buttonPanel.add(viewCustomersBtn);
        buttonPanel.add(createBookingBtn);
        buttonPanel.add(viewBookingsBtn);
        buttonPanel.add(exitBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        addTripBtn.addActionListener(e -> addTrip());
        viewTripsBtn.addActionListener(e -> viewTrips());
        addCustomerBtn.addActionListener(e -> addCustomer());
        viewCustomersBtn.addActionListener(e -> viewCustomers());
        createBookingBtn.addActionListener(e -> createBooking());
        viewBookingsBtn.addActionListener(e -> viewBookings());
        exitBtn.addActionListener(e -> System.exit(0));
    }

    private void addTrip() {
        JTextField destinationField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField seatsField = new JTextField();

        Object[] message = {
            "Destination:", destinationField,
            "Date (yyyy-mm-dd):", dateField,
            "Price:", priceField,
            "Seats Available:", seatsField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Trip", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String destination = destinationField.getText();
            String date = dateField.getText();
            double price = Double.parseDouble(priceField.getText());
            int seats = Integer.parseInt(seatsField.getText());

            Trip trip = new Trip(nextTripId++, destination, date, price, seats);
            trips.add(trip);
            displayArea.append("Trip added: " + trip + "\n");
        }
    }

    private void viewTrips() {
        displayArea.append("--- All Trips ---\n");
        for (Trip t : trips) {
            displayArea.append(t + "\n");
        }
    }

    private void addCustomer() {
        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();

        Object[] message = {
            "Customer Name:", nameField,
            "Phone Number:", phoneField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Customer", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String phone = phoneField.getText();
            Customer customer = new Customer(nextCustomerId++, name, phone);
            customers.add(customer);
            displayArea.append("Customer added: " + customer + "\n");
        }
    }

    private void viewCustomers() {
        displayArea.append("--- All Customers ---\n");
        for (Customer c : customers) {
            displayArea.append(c + "\n");
        }
    }

    private void createBooking() {
        if (customers.isEmpty() || trips.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No customers or trips available.");
            return;
        }

        String customerIdStr = JOptionPane.showInputDialog(this, "Enter Customer ID:");
        String tripIdStr = JOptionPane.showInputDialog(this, "Enter Trip ID:");

        if (customerIdStr != null && tripIdStr != null) {
            int customerId = Integer.parseInt(customerIdStr);
            int tripId = Integer.parseInt(tripIdStr);

            Customer customer = customers.stream().filter(c -> c.getId() == customerId).findFirst().orElse(null);
            Trip trip = trips.stream().filter(t -> t.getId() == tripId).findFirst().orElse(null);

            if (customer == null || trip == null) {
                JOptionPane.showMessageDialog(this, "Customer or Trip not found.");
                return;
            }

            if (trip.getSeatsAvailable() <= 0) {
                JOptionPane.showMessageDialog(this, "No seats available.");
                return;
            }

            Booking booking = new Booking(nextBookingId++, customer, trip);
            bookings.add(booking);
            trip.reduceSeat();
            displayArea.append("Booking created: " + booking + "\n");
        }
    }

    private void viewBookings() {
        displayArea.append("--- All Bookings ---\n");
        for (Booking b : bookings) {
            displayArea.append(b + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TravelManagementGUI().setVisible(true));
    }
}
