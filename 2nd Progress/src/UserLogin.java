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

public class UserLogin extends JFrame{
    private JTextField emailField;
    private JPasswordField passwordField;

    public UserLogin(Menu parentWindow) {
        setTitle("User Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JLabel emailLabel = new JLabel("Email:");
        loginPanel.add(emailLabel);

        emailField = new JTextField(20);
        loginPanel.add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        loginPanel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        loginPanel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                if (Menu.validateLogin(email, password)) {
                    User.setCurrentUser(new User("", email, password));
                    parentWindow.setVisible(false);
                    JOptionPane.showMessageDialog(UserLogin.this, "Login Successful!!");
                    
                    ProductCatalog productCatalog = new ProductCatalog(email);
                    parentWindow.setProductCatalog(productCatalog);
                    productCatalog.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(UserLogin.this, "Invalid email or password.");
                }
            }
        });
        loginPanel.add(loginButton);

        add(loginPanel, BorderLayout.CENTER);
    }
}


