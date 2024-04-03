import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminPage extends JFrame implements ActionListener {
    private JButton signUpButton;
    private JButton loginButton;

    public AdminPage() {
        setTitle("Admin Page");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10));

        signUpButton = new JButton("Sign Up");
        signUpButton.addActionListener(this);
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);

        panel.add(signUpButton);
        panel.add(loginButton);

        // Add padding around the panel
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(panel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            // Handle login button click
            new AdminLoginPage(); // Open login page for admin
            dispose(); // Close the admin page
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminPage::new);
    }
}
