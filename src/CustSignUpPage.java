import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustSignUpPage extends JFrame implements ActionListener {
    private JTextField usernameField; // Added username field
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField addressField;
    private JTextField phoneNumberField;
    private JPasswordField passwordField;
    private JButton registerButton;

    // JDBC URL, username, and password of MySQL server
    private static final String URL = "jdbc:mysql://localhost:3306/airline_reservation";
    private static final String USER = "root";
    private static final String PASSWORD = "Shaily1201";

    // JDBC variables for opening, closing and managing connection
    private Connection connection;
    private PreparedStatement preparedStatement;

    public CustSignUpPage() {
        setTitle("Airline Reservation System - Sign Up");
        setSize(400, 350); // Increased the height to accommodate the new field
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9, 2, 10, 10)); // Increased the rows, added gaps between rows and columns

        JLabel usernameLabel = new JLabel("Username:"); // Added label for username
        usernameField = new JTextField(); // Added username field
        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameField = new JTextField();
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        JLabel addressLabel = new JLabel("Address:");
        addressField = new JTextField();
        JLabel phoneNumberLabel = new JLabel("Phone Number:");
        phoneNumberField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        registerButton = new JButton("Register");
        registerButton.addActionListener(this);

        panel.add(usernameLabel); // Added username label
        panel.add(usernameField); // Added username field
        panel.add(firstNameLabel);
        panel.add(firstNameField);
        panel.add(lastNameLabel);
        panel.add(lastNameField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(addressLabel);
        panel.add(addressField);
        panel.add(phoneNumberLabel);
        panel.add(phoneNumberField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel()); // Empty space for better alignment
        panel.add(registerButton);

        // Add padding around the panel
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(panel);
        setVisible(true);

        // Create tables when the application starts
        createTables();
    }

    // Method to register a new passenger
    private void registerPassenger(String username, String firstName, String lastName, String email, String address, String phoneNumber, String password) {
        String query = "INSERT INTO passengers (username, first_name, last_name, email, address, phone_number, password) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, address);
            preparedStatement.setString(6, phoneNumber);
            preparedStatement.setString(7, password);
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Passenger registered successfully!");
                dispose(); // Close the sign-up window
                openLoginWindow(); // Open the login window
            } else {
                JOptionPane.showMessageDialog(this, "Failed to register passenger.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    // Method to create tables in the database
    private void createTables() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            // Create passengers table
            String createPassengersTableQuery = "CREATE TABLE IF NOT EXISTS Passengers (id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(50), first_name VARCHAR(50), last_name VARCHAR(50), email VARCHAR(100), " +
                    "address VARCHAR(255), phone_number VARCHAR(20), password VARCHAR(50))";
            preparedStatement = connection.prepareStatement(createPassengersTableQuery);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    // Method to open the login window
    private void openLoginWindow() {
        CustLoginPage loginWindow = new CustLoginPage();
        loginWindow.setVisible(true);
    }

    // Method to close JDBC resources
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            String username = usernameField.getText(); // Get username value
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            String address = addressField.getText();
            String phoneNumber = phoneNumberField.getText();
            String password = new String(passwordField.getPassword());
            registerPassenger(username, firstName, lastName, email, address, phoneNumber, password);
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        SwingUtilities.invokeLater(CustSignUpPage::new);
    }
}
