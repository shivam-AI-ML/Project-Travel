import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class TravelManagementGUI extends JFrame {

    private JTextArea displayArea;

    public TravelManagementGUI() {
        setTitle("Travel Management System");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 3, 10, 10));

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

        addTripBtn.addActionListener(e -> addTrip());
        viewTripsBtn.addActionListener(e -> viewTrips());
        addCustomerBtn.addActionListener(e -> addCustomer());
        viewCustomersBtn.addActionListener(e -> viewCustomers());
        createBookingBtn.addActionListener(e -> createBooking());
        viewBookingsBtn.addActionListener(e -> viewBookings());
        exitBtn.addActionListener(e -> System.exit(0));
    }

    // ---------------- ADD TRIP ----------------
    private void addTrip() {
        JTextField destination = new JTextField();
        JTextField date = new JTextField();
        JTextField price = new JTextField();
        JTextField seats = new JTextField();

        Object[] msg = {
                "Destination:", destination,
                "Date (yyyy-mm-dd):", date,
                "Price:", price,
                "Seats:", seats
        };

        if (JOptionPane.showConfirmDialog(this, msg, "Add Trip",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

            try (Connection con = DBConnection.getConnection()) {
                String sql = "INSERT INTO trips(destination, trip_date, price, seats) VALUES (?, ?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(sql);

                ps.setString(1, destination.getText());
                ps.setDate(2, Date.valueOf(date.getText()));
                ps.setDouble(3, Double.parseDouble(price.getText()));
                ps.setInt(4, Integer.parseInt(seats.getText()));

                ps.executeUpdate();
                displayArea.append("Trip added successfully\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // ---------------- VIEW TRIPS ----------------
    private void viewTrips() {
        displayArea.append("--- All Trips ---\n");
        try (Connection con = DBConnection.getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM trips");

            while (rs.next()) {
                displayArea.append(
                        "ID: " + rs.getInt("id") +
                        ", " + rs.getString("destination") +
                        ", " + rs.getDate("trip_date") +
                        ", $" + rs.getDouble("price") +
                        ", Seats: " + rs.getInt("seats") + "\n"
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- ADD CUSTOMER ----------------
    private void addCustomer() {
        JTextField name = new JTextField();
        JTextField phone = new JTextField();

        Object[] msg = {
                "Name:", name,
                "Phone:", phone
        };

        if (JOptionPane.showConfirmDialog(this, msg, "Add Customer",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

            try (Connection con = DBConnection.getConnection()) {
                String sql = "INSERT INTO customers(name, phone) VALUES (?, ?)";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, name.getText());
                ps.setString(2, phone.getText());
                ps.executeUpdate();

                displayArea.append("Customer added successfully\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // ---------------- VIEW CUSTOMERS ----------------
    private void viewCustomers() {
        displayArea.append("--- All Customers ---\n");
        try (Connection con = DBConnection.getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM customers");

            while (rs.next()) {
                displayArea.append(
                        "ID: " + rs.getInt("id") +
                        ", " + rs.getString("name") +
                        ", " + rs.getString("phone") + "\n"
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- CREATE BOOKING ----------------
    private void createBooking() {
        String customerId = JOptionPane.showInputDialog(this, "Enter Customer ID:");
        String tripId = JOptionPane.showInputDialog(this, "Enter Trip ID:");

        try (Connection con = DBConnection.getConnection()) {

            PreparedStatement check = con.prepareStatement(
                    "SELECT seats FROM trips WHERE id=?");
            check.setInt(1, Integer.parseInt(tripId));
            ResultSet rs = check.executeQuery();

            if (rs.next() && rs.getInt("seats") > 0) {

                PreparedStatement book = con.prepareStatement(
                        "INSERT INTO bookings(customer_id, trip_id) VALUES (?, ?)");
                book.setInt(1, Integer.parseInt(customerId));
                book.setInt(2, Integer.parseInt(tripId));
                book.executeUpdate();

                PreparedStatement update = con.prepareStatement(
                        "UPDATE trips SET seats = seats - 1 WHERE id=?");
                update.setInt(1, Integer.parseInt(tripId));
                update.executeUpdate();

                displayArea.append("Booking created successfully\n");
            } else {
                JOptionPane.showMessageDialog(this, "No seats available!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- VIEW BOOKINGS ----------------
    private void viewBookings() {
        displayArea.append("--- All Bookings ---\n");
        try (Connection con = DBConnection.getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT b.booking_id, c.name, t.destination " +
                    "FROM bookings b " +
                    "JOIN customers c ON b.customer_id = c.id " +
                    "JOIN trips t ON b.trip_id = t.id"
            );

            while (rs.next()) {
                displayArea.append(
                        "Booking ID: " + rs.getInt("booking_id") +
                        ", Customer: " + rs.getString("name") +
                        ", Trip: " + rs.getString("destination") + "\n"
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TravelManagementGUI().setVisible(true));
    }
}


