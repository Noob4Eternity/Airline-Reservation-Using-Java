import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlaneDetails extends JFrame implements ActionListener {
    private JTextField flightNumberField;
    private JTextField flightCompanyField;
    private JTextField originField;
    private JTextField destinationField;
    private JTextField departureTimeField;
    private JTextField boardingTimeField;
    private JTextField priceField;
    private JTextField numberOfSeatsField;
    private JCheckBox internationalCheckBox;
    private JButton submitButton;

    private static final String URL = "jdbc:mysql://localhost:3306/airline_reservation";
    private static final String USER = "root";
    private static final String PASSWORD = "Shaily1201"; // Change to your MySQL password

    private Connection connection;
    private PreparedStatement preparedStatement;

    public PlaneDetails() {
        setTitle("Flight Details Input");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(11, 2, 10, 10));

        JLabel flightNumberLabel = new JLabel("Flight Number:");
        flightNumberField = new JTextField();
        JLabel flightCompanyLabel = new JLabel("Flight Company:");
        flightCompanyField = new JTextField();
        JLabel originLabel = new JLabel("Origin:");
        originField = new JTextField();
        JLabel destinationLabel = new JLabel("Destination:");
        destinationField = new JTextField();
        JLabel departureTimeLabel = new JLabel("Departure Time (YYYY-MM-DD HH:MM):");
        departureTimeField = new JTextField();
        JLabel boardingTimeLabel = new JLabel("Boarding Time (YYYY-MM-DD HH:MM):");
        boardingTimeField = new JTextField();
        JLabel priceLabel = new JLabel("Price:");
        priceField = new JTextField();
        JLabel numberOfSeatsLabel = new JLabel("Number of Seats:");
        numberOfSeatsField = new JTextField();
        JLabel internationalLabel = new JLabel("International Flight:");
        internationalCheckBox = new JCheckBox();
        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);

        panel.add(flightNumberLabel);
        panel.add(flightNumberField);
        panel.add(flightCompanyLabel);
        panel.add(flightCompanyField);
        panel.add(originLabel);
        panel.add(originField);
        panel.add(destinationLabel);
        panel.add(destinationField);
        panel.add(departureTimeLabel);
        panel.add(departureTimeField);
        panel.add(boardingTimeLabel);
        panel.add(boardingTimeField);
        panel.add(priceLabel);
        panel.add(priceField);
        panel.add(numberOfSeatsLabel);
        panel.add(numberOfSeatsField);
        panel.add(internationalLabel);
        panel.add(internationalCheckBox);
        panel.add(new JLabel());
        panel.add(submitButton);

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(panel);
        setVisible(true);

        createFlightsTable(); // Call method to create flights table
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            String flightNumber = flightNumberField.getText();
            String flightCompany = flightCompanyField.getText();
            String origin = originField.getText();
            String destination = destinationField.getText();
            String departureTime = departureTimeField.getText();
            String boardingTime = boardingTimeField.getText();
            double price = Double.parseDouble(priceField.getText());
            int numberOfSeats = Integer.parseInt(numberOfSeatsField.getText());
            boolean isInternational = internationalCheckBox.isSelected();

            insertFlightDetails(flightNumber, flightCompany, origin, destination, departureTime, boardingTime,
                    price, numberOfSeats, isInternational);
        }
    }

    private void insertFlightDetails(String flightNumber, String flightCompany, String origin, String destination,
                                     String departureTime, String boardingTime,
                                     double price, int numberOfSeats, boolean isInternational) {
        String query = "INSERT INTO flights (flight_number, flight_company, origin, destination, departure_time, " +
                "boarding_time, price, number_of_seats, available_seats, is_international) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, flightNumber);
            preparedStatement.setString(2, flightCompany);
            preparedStatement.setString(3, origin);
            preparedStatement.setString(4, destination);
            preparedStatement.setString(5, departureTime);
            preparedStatement.setString(6, boardingTime);
            preparedStatement.setDouble(7, price);
            preparedStatement.setInt(8, numberOfSeats);
            preparedStatement.setInt(9, numberOfSeats); // Set available seats initially equal to total seats
            preparedStatement.setBoolean(10, isInternational);
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Flight details inserted successfully!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to insert flight details.");
        } finally {
            closeResources();
        }
    }

    private void closeResources() {
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createFlightsTable() {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS flights (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "flight_number VARCHAR(50)," +
                "flight_company VARCHAR(100)," +
                "origin VARCHAR(100)," +
                "destination VARCHAR(100)," +
                "departure_time VARCHAR(16)," +
                "boarding_time VARCHAR(16)," +
                "price DOUBLE," +
                "number_of_seats INT," +
                "available_seats INT," + // Added available seats column
                "is_international BOOLEAN)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(createTableQuery)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        SwingUtilities.invokeLater(PlaneDetails::new);
    }
}

