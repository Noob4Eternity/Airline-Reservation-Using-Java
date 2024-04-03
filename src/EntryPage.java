import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EntryPage extends JFrame implements ActionListener {
    private JButton adminButton;
    private JButton customerButton;

    public EntryPage() {
        setTitle("Entry Page");
        setSize(300, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10));

        adminButton = new JButton("Admin");
        adminButton.addActionListener(this);
        customerButton = new JButton("Customer");
        customerButton.addActionListener(this);

        panel.add(adminButton);
        panel.add(customerButton);

        // Add padding around the panel
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(panel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == adminButton) {
            // Handle admin button click
            new AdminPage(); // Open admin page
            dispose(); // Close the entry page
        } else if (e.getSource() == customerButton) {
            // Handle customer button click
            // You can redirect to the main page of the customer here
            new Customer();
            dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EntryPage::new);
    }
}
