import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Menu extends JFrame {
       private static final String USERS_FILE = "users.txt";
    
    private ProductCatalog productCatalog;
    
    public Menu() {
        setTitle("User Registration and Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JLabel label = new JLabel("WELCOME!");
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(Color.BLACK);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));
        mainPanel.add(label, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 100, 100, 100));

        Font buttonFont = new Font("Arial", Font.BOLD, 16);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        JButton registerButton = new JButton("Register");
        registerButton.setFont(buttonFont);
        registerButton.setBackground(new Color(59, 89, 182));
        registerButton.setForeground(Color.WHITE);
        registerButton.setPreferredSize(new Dimension(500, 50)); 
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openRegistrationWindow();
            }
        });
        buttonPanel.add(registerButton, gbc);

        gbc.gridy = 1;
        JButton loginButton = new JButton("Login");
        loginButton.setFont(buttonFont);
        loginButton.setBackground(new Color(59, 89, 182));
        loginButton.setForeground(Color.WHITE);
        loginButton.setPreferredSize(new Dimension(500, 50)); 
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openLoginWindow();
            }
        });
        buttonPanel.add(loginButton, gbc);

        gbc.gridy = 2;
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(buttonFont);
        exitButton.setBackground(new Color(192, 57, 43));
        exitButton.setForeground(Color.WHITE);
        exitButton.setPreferredSize(new Dimension(500, 50)); 
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exitProgram();
            }
        });
        buttonPanel.add(exitButton, gbc);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void openRegistrationWindow() {
        UserRegistration registrationWindow = new UserRegistration(this);
        registrationWindow.setVisible(true);
        setVisible(false);
    }

    private void openLoginWindow() {
        UserLogin loginWindow = new UserLogin(this);
        loginWindow.setVisible(true);
        setVisible(false);
    }
    
    private void exitProgram() {
        dispose();
    }

    public boolean registerUser(String username, String email, String password) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true));
            writer.write(username + "," + email + "," + password);
            writer.newLine();
            writer.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean validateLogin(String email, String password) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(USERS_FILE));
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[1].equals(email) && parts[2].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public ProductCatalog getProductCatalog() {
        return productCatalog;
    }
    
    public void setProductCatalog(ProductCatalog productCatalog) {
        this.productCatalog = productCatalog;
    }
    
}