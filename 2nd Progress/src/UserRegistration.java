import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class UserRegistration extends JFrame {
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    
    public UserRegistration(Menu parentWindow) {
        setTitle("User Registration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new BorderLayout());

        JPanel registrationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JLabel usernameLabel = new JLabel("Username:");
        registrationPanel.add(usernameLabel);

        usernameField = new JTextField(20);
        registrationPanel.add(usernameField);

        JLabel emailLabel = new JLabel("Email:");
        registrationPanel.add(emailLabel);

        emailField = new JTextField(20);
        registrationPanel.add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        registrationPanel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        registrationPanel.add(passwordField);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                if (parentWindow.registerUser(username, email, password)) {
                    JOptionPane.showMessageDialog(UserRegistration.this, "Registration Successful!");
                    parentWindow.setVisible(false);

                    ProductCatalog productCatalog = new ProductCatalog(email);
                    parentWindow.setProductCatalog(productCatalog);
                    productCatalog.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(UserRegistration.this, "Error occurred during registration.");
                }
            }
        });
        registrationPanel.add(registerButton);

        add(registrationPanel, BorderLayout.CENTER);
    }
}
