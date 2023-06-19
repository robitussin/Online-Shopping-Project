import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class Cart extends JFrame {
    private List<Product> cart;
    private JPanel cartPanel;
    private ProductCatalog productCatalog;
    private User user;

    public Cart(ProductCatalog productCatalog, User user, List<Product> cart) {
        this.productCatalog = productCatalog;
        this.user = user;
        this.cart = cart;

        setTitle("Cart");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 400);
        setLayout(new BorderLayout());
        setResizable(true);

        cartPanel = new JPanel();
        cartPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        cartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(cartPanel, BorderLayout.CENTER);

        displayCart();

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                productCatalog.setVisible(true);
                dispose();
            }
        });

        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performCheckout();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backButton);
        buttonPanel.add(checkoutButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void displayCart() {
        cartPanel.removeAll();

        Iterator<Product> iterator = cart.iterator();
        while (iterator.hasNext()) {
            Product product = iterator.next();

            if (product.getQuantity() <= 0) {
                iterator.remove();
                continue;
            }

            JPanel productPanel = new JPanel(new BorderLayout());
            productPanel.setPreferredSize(new Dimension(500, 50));
            productPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(getForeground(), 1),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));

            JLabel nameLabel = new JLabel(product.getName());
            nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 14f));
            productPanel.add(nameLabel, BorderLayout.WEST);

            JPanel quantityPanel = new JPanel(new BorderLayout());
            quantityPanel.setOpaque(false);

            JLabel quantityLabel = new JLabel("Quantity: " + product.getQuantity());
            quantityLabel.setFont(quantityLabel.getFont().deriveFont(12f));
            quantityPanel.add(quantityLabel, BorderLayout.CENTER);

            JButton removeButton = new JButton("Remove");
            removeButton.setPreferredSize(new Dimension(100, 25));
            removeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String input = JOptionPane.showInputDialog(Cart.this, "Enter quantity to remove:", "Remove Product",
                            JOptionPane.QUESTION_MESSAGE);
                    int quantityToRemove;
                    try {
                        quantityToRemove = Integer.parseInt(input);
                    } catch (NumberFormatException ex) {
                        return;
                    }

                    if (quantityToRemove > 0 && quantityToRemove <= product.getQuantity()) {
                        product.setQuantity(product.getQuantity() - quantityToRemove);
                        displayCart();
                        productCatalog.updateProductQuantity(product.getName(), product.getCategory(),
                                product.getRAM(), product.getPrice(), product.getQuantity(), rootPaneCheckingEnabled);
                    } else {
                        JOptionPane.showMessageDialog(Cart.this, "Invalid quantity!", "Remove Product",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            quantityPanel.add(removeButton, BorderLayout.EAST);
            productPanel.add(quantityPanel, BorderLayout.EAST);

            cartPanel.add(productPanel);
        }

        cartPanel.revalidate();
        cartPanel.repaint();
    }

    private void performCheckout() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty!", "Checkout", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] productNames = new String[cart.size()];
        for (int i = 0; i < cart.size(); i++) {
            productNames[i] = cart.get(i).getName();
        }

        JComboBox<String> productComboBox = new JComboBox<>(productNames);

        int option = JOptionPane.showConfirmDialog(this, productComboBox, "Select Product to Checkout",
                JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String selectedProduct = (String) productComboBox.getSelectedItem();

            Product product = null;
            for (Product p : cart) {
                if (p.getName().equalsIgnoreCase(selectedProduct)) {
                    product = p;
                    break;
                }
            }

            String input = JOptionPane.showInputDialog(this,
                    "Enter the quantity to checkout (current quantity: " + product.getQuantity() + "):", "Checkout",
                    JOptionPane.QUESTION_MESSAGE);

            String address = JOptionPane.showInputDialog(this, "Enter your address:", "Checkout",
                    JOptionPane.QUESTION_MESSAGE);
            String contactNumber = JOptionPane.showInputDialog(this, "Enter your contact number:", "Checkout",
                    JOptionPane.QUESTION_MESSAGE);

            int quantityToCheckout;
            try {
                quantityToCheckout = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                return;
            }

            if (quantityToCheckout > 0 && quantityToCheckout <= product.getQuantity()) {
                double totalPrice = quantityToCheckout * product.getPrice();

                JRadioButton codRadioButton = new JRadioButton("Cash on Delivery");
                JRadioButton onlinePaymentRadioButton = new JRadioButton("Online Payment");

                ButtonGroup paymentGroup = new ButtonGroup();
                paymentGroup.add(codRadioButton);
                paymentGroup.add(onlinePaymentRadioButton);

                JPanel paymentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                paymentPanel.add(codRadioButton);
                paymentPanel.add(onlinePaymentRadioButton);

                int paymentOption = JOptionPane.showConfirmDialog(this, paymentPanel, "Select Payment Method",
                        JOptionPane.OK_CANCEL_OPTION);

                String paymentMethod;
                if (paymentOption == JOptionPane.OK_OPTION) {
                    if (codRadioButton.isSelected()) {
                        paymentMethod = "COD";
                    } else if (onlinePaymentRadioButton.isSelected()) {
                        paymentMethod = "Online Payment";
                    } else {
                        JOptionPane.showMessageDialog(this, "Please select a payment method!", "Checkout",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else {
                    return;
                }

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime arrivalDate = now.plusDays(7);
                String formattedDate = dtf.format(arrivalDate);

                StringBuilder summaryBuilder = new StringBuilder();
                summaryBuilder.append("Order Summary:\n");
                summaryBuilder.append("Product: ").append(product.getName()).append("\n");
                summaryBuilder.append("Quantity: ").append(quantityToCheckout).append("\n");
                summaryBuilder.append("Total Price: PHP").append(totalPrice).append("\n");
                summaryBuilder.append("Address: ").append(address).append("\n");
                summaryBuilder.append("Contact Number: ").append(contactNumber).append("\n");
                summaryBuilder.append("Payment Method: ").append(paymentMethod).append("\n");
                summaryBuilder.append("Estimated Date of Arrival: ").append(formattedDate).append("\n");

                int confirmationOption = JOptionPane.showConfirmDialog(this, summaryBuilder.toString(),
                        "Confirm Order", JOptionPane.YES_NO_OPTION);

                if (confirmationOption == JOptionPane.YES_OPTION) {
                    productCatalog.updateProductQuantity(product.getName(), product.getCategory(), product.getRAM(),
                            product.getPrice(), quantityToCheckout, rootPaneCheckingEnabled);

                    cart.remove(product);

                    saveCheckedOutItem(summaryBuilder.toString(), user.getUsername());
                    JOptionPane.showMessageDialog(this, "Order placed successfully!", "Order Confirmation",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid quantity!", "Checkout", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveCheckedOutItem(String orderSummary, String username) {

        if (user != null) {
            String folderPath = "C:/Users/SLY/Desktop/GABION/UsersCheckout";
            String filename = username + "_checkout.txt";
            String filePath = folderPath + File.separator + filename;

            File directory = new File(folderPath);
            if (!directory.exists()) {
                boolean created = directory.mkdirs();
                if (!created) {
                    JOptionPane.showMessageDialog(this, "Failed to create the directory.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                writer.write(orderSummary);
                writer.newLine();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to save the checked out item:\n" + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}