import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CustLoginPage extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    // JDBC URL, username, and password of MySQL server
    private static final String URL = "jdbc:mysql://localhost:3306/airline_reservation";
    private static final String USER = "root";
    private static final String PASSWORD = "Shaily1201";

    // JDBC variables for opening, closing and managing connection
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public CustLoginPage() {
        setTitle("Airline Reservation System - Login");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10)); // Increased the rows, added gaps between rows and columns

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        loginButton = new JButton("Login");
        loginButton.addActionListener(this);

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel()); // Empty space for better alignment
        panel.add(loginButton);

        // Add padding around the panel
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(panel);
        setVisible(true);
    }

    // Method to authenticate a user
    private boolean authenticateUser(String username, String password) {
        String query = "SELECT * FROM passengers WHERE username = ? AND password = ?";
        boolean isAuthenticated = false;

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            isAuthenticated = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }

        return isAuthenticated;
    }

    // Method to close JDBC resources
    private void closeResources() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
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
        if (e.getSource() == loginButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            boolean isAuthenticated = authenticateUser(username, password);
            if (isAuthenticated) {
                JOptionPane.showMessageDialog(this, "Authentication successful!");
                dispose(); // Close the login window
                SwingUtilities.invokeLater(() -> {
                    ShowPlaneDetails frame = new ShowPlaneDetails();
                    frame.setVisible(true);
                });
            } else {
                JOptionPane.showMessageDialog(this, "Authentication failed. Invalid credentials.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CustLoginPage::new);
    }
}
