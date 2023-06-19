import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class UserProfile extends JFrame {
    private String username;
    private String email;
    private String password;
    private String checkoutFile;
    private JButton backButton;
    private JButton logoutButton;

    public UserProfile(ProductCatalog parentWindow, User user, List<Product> cart) {
        setTitle("User Profile");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLayout(new BorderLayout());

        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[0].equals(user.getUsername())) {
                    username = parts[0];
                    email = parts[1];
                    password = parts[2];
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        checkoutFile = "UsersCheckout/" + user.getUsername() + "_checkout.txt";

        JPanel userProfilePanel = new JPanel(new BorderLayout());

        JPanel userInfoPanel = new JPanel(new GridLayout(3, 1));
        JLabel usernameLabel = new JLabel("Username: " + username);
        JLabel emailLabel = new JLabel("Email: " + email);
        JLabel passwordLabel = new JLabel("Password: " + password);
        userInfoPanel.add(usernameLabel);
        userInfoPanel.add(emailLabel);
        userInfoPanel.add(passwordLabel);
        userProfilePanel.add(userInfoPanel, BorderLayout.NORTH);

        if (!checkoutFile.isEmpty()) {
            JPanel checkoutPanel = new JPanel(new BorderLayout());
            JLabel checkoutLabel = new JLabel("Checkout Information:");
            checkoutPanel.add(checkoutLabel, BorderLayout.NORTH);

            JTextArea checkoutTextArea = new JTextArea();
            checkoutTextArea.setEditable(false);
            checkoutTextArea.setLineWrap(true);
            checkoutTextArea.setWrapStyleWord(true);

            try (BufferedReader reader = new BufferedReader(new FileReader(checkoutFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    checkoutTextArea.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            JScrollPane scrollPane = new JScrollPane(checkoutTextArea);
            checkoutPanel.add(scrollPane, BorderLayout.CENTER);
            userProfilePanel.add(checkoutPanel, BorderLayout.CENTER);
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backButton = new JButton("Back");
        logoutButton = new JButton("Logout");
        buttonPanel.add(backButton);
        buttonPanel.add(logoutButton);
        userProfilePanel.add(buttonPanel, BorderLayout.SOUTH);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parentWindow.setVisible(true);
                dispose();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Logged out");
                System.exit(0);
            }
        });

        add(userProfilePanel, BorderLayout.CENTER);
    }
}