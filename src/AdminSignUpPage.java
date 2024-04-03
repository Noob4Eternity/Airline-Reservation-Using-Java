import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminSignUpPage extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField addressField;
    private JTextField phoneNumberField;
    private JPasswordField passwordField;
    private JButton registerButton;

    private static final String URL = "jdbc:mysql://localhost:3306/airline_reservation";
    private static final String USER = "root";
    private static final String PASSWORD = "Shaily1201";

    private Connection connection;
    private PreparedStatement preparedStatement;

    public AdminSignUpPage() {
        setTitle("Admin Sign Up");
        setSize(400, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9, 2, 10, 10));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
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

        panel.add(usernameLabel);
        panel.add(usernameField);
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
        panel.add(new JLabel());
        panel.add(registerButton);

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(panel);
        setVisible(true);

        createTables();
    }

    private void registerAdmin(String username, String firstName, String lastName, String email, String address, String phoneNumber, String password) {
        String query = "INSERT INTO admins (username, first_name, last_name, email, address, phone_number, password) VALUES (?, ?, ?, ?, ?, ?, ?)";

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
                JOptionPane.showMessageDialog(this, "Admin registered successfully!");
                dispose();
                openAdminLoginWindow();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to register admin.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    private void createTables() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            String createAdminsTableQuery = "CREATE TABLE IF NOT EXISTS admins (id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(50), first_name VARCHAR(50), last_name VARCHAR(50), email VARCHAR(100), " +
                    "address VARCHAR(255), phone_number VARCHAR(20), password VARCHAR(50))";
            preparedStatement = connection.prepareStatement(createAdminsTableQuery);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    private void openAdminLoginWindow() {
        AdminLoginPage adminLoginWindow = new AdminLoginPage();
        adminLoginWindow.setVisible(true);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            String username = usernameField.getText();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            String address = addressField.getText();
            String phoneNumber = phoneNumberField.getText();
            String password = new String(passwordField.getPassword());
            registerAdmin(username, firstName, lastName, email, address, phoneNumber, password);
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        SwingUtilities.invokeLater(AdminSignUpPage::new);
    }
}
