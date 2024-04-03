import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminLoginPage extends JFrame implements ActionListener {
    private JTextField adminNameField;
    private JPasswordField adminPasswordField;
    private JButton loginButton;

    private static final String URL = "jdbc:mysql://localhost:3306/airline_reservation";
    private static final String USER = "root";
    private static final String PASSWORD = "Shaily1201";

    private Connection connection;
    private PreparedStatement preparedStatement;

    public AdminLoginPage() {
        setTitle("Admin Login");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel adminNameLabel = new JLabel("Admin Name:");
        adminNameField = new JTextField();
        JLabel adminPasswordLabel = new JLabel("Admin Password:");
        adminPasswordField = new JPasswordField();

        loginButton = new JButton("Login");
        loginButton.addActionListener(this);

        panel.add(adminNameLabel);
        panel.add(adminNameField);
        panel.add(adminPasswordLabel);
        panel.add(adminPasswordField);
        panel.add(new JLabel()); // Empty space for better alignment
        panel.add(loginButton);

        // Add padding around the panel
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(panel);
        setVisible(true);
    }

    // Method to authenticate admin login
    private boolean authenticateAdmin(String adminName, String adminPassword) {
        String query = "SELECT * FROM admin WHERE admin_username = ? AND admin_password = ?";
        boolean isAuthenticated = false;

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, adminName);
            preparedStatement.setString(2, adminPassword);
            ResultSet resultSet = preparedStatement.executeQuery();
            isAuthenticated = resultSet.next();
            if (isAuthenticated) {
                // Insert login time into the admin_login_time table
                insertLoginTime(adminName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }

        return isAuthenticated;
    }

    // Method to insert login time into the admin_login_time table
    private void insertLoginTime(String adminName) {
        String insertQuery = "INSERT INTO admin_login_time (admin_username) VALUES (?)";

        try {
            preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, adminName);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        if (e.getSource() == loginButton) {
            String adminName = adminNameField.getText();
            String adminPassword = new String(adminPasswordField.getPassword());
            boolean isAuthenticated = authenticateAdmin(adminName, adminPassword);
            if (isAuthenticated) {
                JOptionPane.showMessageDialog(this, "Admin authentication successful!");
                // Proceed with admin functionality or redirect as needed
                new PlaneDetails();
            } else {
                JOptionPane.showMessageDialog(this, "Authentication failed. Invalid credentials.");
                dispose(); // Close the window immediately if authentication fails
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminLoginPage::new);
    }
}
