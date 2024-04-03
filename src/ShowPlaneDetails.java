import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ShowPlaneDetails extends JFrame implements ActionListener {
    private JTable table;
    private JTextField seatsField;
    private JButton bookButton;
    private static final String URL = "jdbc:mysql://localhost:3306/airline_reservation";
    private static final String USER = "root";
    private static final String PASSWORD = "Shaily1201"; // Change to your MySQL password
    private Connection connection;

    public ShowPlaneDetails() {
        setTitle("Flight Details");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create components
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel seatsLabel = new JLabel("Number of Seats:");
        seatsField = new JTextField(10);
        bookButton = new JButton("Book");
        bookButton.addActionListener(this);
        topPanel.add(seatsLabel);
        topPanel.add(seatsField);
        topPanel.add(bookButton);

        // Add components to the frame
        add(topPanel, BorderLayout.NORTH);

        // Fetch and display flight details
        fetchAndDisplayFlightDetails();

        // Add instruction label
        JLabel instructionLabel = new JLabel("*Click on the available seats of the flight to select the flight*");
        add(instructionLabel, BorderLayout.SOUTH);
    }

    private void fetchAndDisplayFlightDetails() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            String query = "SELECT * FROM flights";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Prepare the table model
            DefaultTableModel model = new DefaultTableModel();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                model.addColumn(metaData.getColumnName(columnIndex));
            }

            // Populate the table model with data from the result set
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    rowData[i] = resultSet.getObject(i + 1);
                }
                model.addRow(rowData);
            }

            // Create and display the table
            table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch flight details.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bookButton) {
            // Retrieve the selected row from the table
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a flight first.");
                return;
            }

            // Retrieve the number of seats to book
            String seatsText = seatsField.getText();
            if (seatsText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the number of seats.");
                return;
            }

            int numberOfSeats;
            try {
                numberOfSeats = Integer.parseInt(seatsText);
                if (numberOfSeats <= 0 || numberOfSeats > 5) { // Limiting to a maximum of 5 seats
                    JOptionPane.showMessageDialog(this, "Please enter a number of seats between 1 and 5.");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number of seats.");
                return;
            }

            // Retrieve the available number of seats from the selected row
            double availableSeats = (double) table.getValueAt(selectedRow, 7); // Assuming the available seats column is at index 7

            // Check if the requested number of seats is available
            if (numberOfSeats > availableSeats) {
                JOptionPane.showMessageDialog(this, "Sorry, not enough seats available.");
            } else {
                // Perform the booking
                try {
                    double updatedAvailableSeats = availableSeats - numberOfSeats;
                    int flightId = (int) table.getValueAt(selectedRow, 0); // Assuming the first column contains the flight ID
                    updateAvailableSeatsInDatabase(flightId, updatedAvailableSeats);

                    JOptionPane.showMessageDialog(this, "Seats booked successfully!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error occurred while booking seats.");
                }
            }
        }
    }

    // Update the available seats in the database
    private void updateAvailableSeatsInDatabase(int flightId, double updatedAvailableSeats) throws SQLException {
        String query = "UPDATE flights SET available_seats = ? WHERE id = ?"; // Change 'id' to the correct column name
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setDouble(1, updatedAvailableSeats);
        preparedStatement.setInt(2, flightId);
        preparedStatement.executeUpdate();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ShowPlaneDetails frame = new ShowPlaneDetails();
            frame.setVisible(true);
        });
    }
}
