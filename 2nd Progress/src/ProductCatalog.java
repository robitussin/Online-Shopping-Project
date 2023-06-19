import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.SysexMessage;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ProductCatalog extends JFrame {
    private List<Product> products;
    private JPanel productPanel;
    private List<Product> cart;
    private List<Product> filteredProducts;
    private ProductDetails productDetailsWindow;
    private JButton backButton;
    private JButton customizeProductsButton;
    private JButton userProfileButton;
    private User user;

    public ProductCatalog(String userEmail) {
        setTitle("Online Shop");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new BorderLayout());

        products = new ArrayList<>();
        cart = new ArrayList<>();
        filteredProducts = new ArrayList<>();

        productPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        add(productPanel, BorderLayout.CENTER);

        JPanel down = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton viewCartButton = new JButton("View Cart");
        viewCartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openCart();
            }
        });
        down.add(viewCartButton);
        add(down, BorderLayout.SOUTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String query = searchField.getText();
                searchProducts(query);
            }
        });
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayProducts(products);
                backButton.setVisible(false);
            }
        });
        backButton.setVisible(false);
        searchPanel.add(backButton);

        userProfileButton = new JButton("User Profile");
        userProfileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openUserProfile();
            }
        });
        down.add(userProfileButton);

        customizeProductsButton = new JButton("Customize Products");
        customizeProductsButton.setVisible(userIsAdmin(userEmail));

        customizeProductsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openCustomizeProductsWindow();
            }
        });
        down.add(customizeProductsButton);

        user = getUserByEmail(userEmail);

        displayProducts("C:/Users/SLY/Desktop/GABION/Product");
    }

    private void displayProducts(String directoryPath) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String name = reader.readLine();
                    String category = reader.readLine();
                    String ram = reader.readLine();
                    String priceString = reader.readLine();
                    String quantityString = reader.readLine();

                    while (name != null && category != null && ram != null && priceString != null
                            && quantityString != null) {
                        double price = Double.parseDouble(priceString);
                        int quantity = Integer.parseInt(quantityString);
                        Product product = new Product(name, category, ram, price, quantity);
                        products.add(product);

                        name = reader.readLine();
                        category = reader.readLine();
                        ram = reader.readLine();
                        priceString = reader.readLine();
                        quantityString = reader.readLine();
                    }
                } catch (IOException e) {
                    System.out.println("Error");
                }
            }
        }

        displayProducts(products);
    }

    private void displayProducts(List<Product> productList) {
        DefaultListModel<Product> listModel = new DefaultListModel<>();
        JList<Product> productListJList = new JList<>(listModel);
        productListJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        for (Product product : productList) {
            listModel.addElement(product);
        }

        productListJList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Product selectedProduct = productListJList.getSelectedValue();
                    if (selectedProduct != null) {
                        openProductDetails(selectedProduct);
                    }
                }
            }
        });

        productListJList.setCellRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Product) {
                    Product product = (Product) value;
                    setText(product.getName());
                }
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(productListJList);
        productPanel.removeAll();
        productPanel.add(scrollPane);
        productPanel.revalidate();

        boolean isFiltered = productList != products;
        showBackButton(isFiltered);
    }

    private void openProductDetails(Product product) {
        productDetailsWindow = new ProductDetails(this, product.getName(), product.getCategory(), product.getRAM(),
                product.getPrice(), product.getQuantity());
        productDetailsWindow.setVisible(true);
        setVisible(false);
    }

    private void searchProducts(String query) {
        filteredProducts.clear();

        for (Product product : products) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())
                    || product.getCategory().toLowerCase().contains(query.toLowerCase())
                    || product.getRAM().toLowerCase().contains(query.toLowerCase())
                    || product.getPrice() == Double.parseDouble(query)) {
                filteredProducts.add(product);
            }
        }

        displayProducts(filteredProducts);
        backButton.setVisible(true);
    }

    private boolean userIsAdmin(String userEmail) {
        return userEmail != null && userEmail.endsWith("@admin.gmail.com");
    }

    public void showProductCatalog() {
        setVisible(true);
    }

    public void showBackButton(boolean show) {
        backButton.setVisible(show);
    }

    public void addToCart(Product product) {
        cart.add(product);
    }

    private void openCart() {

        String checkoutFile = "UsersCart/" + user.getUsername() +
                "_itemsincart.txt";

        File file = new File(checkoutFile);

        System.out.println(checkoutFile);
        if (!checkoutFile.isEmpty() && file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(checkoutFile))) {
                String line;
                Cart cartWindow = null;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    String[] splited = line.split("\\s+");
                    Product productincart = new Product(splited[0], splited[1], splited[2],
                            Double.parseDouble(splited[3]), Integer.parseInt(splited[4]));

                    cart.add(productincart);
                    cartWindow = new Cart(this, user, cart);
                }

                cartWindow.setVisible(true);
                setVisible(false);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println(cart);
            Cart cartWindow = new Cart(this, user, cart);
            cartWindow.setVisible(true);
            setVisible(false);
        }
    }

    private void openCustomizeProductsWindow() {
        String productsFilePath = "C:/Users/SLY/Desktop/GABION/Product";
        CustomizeProduct customizeProductWindow = new CustomizeProduct(this, products, productsFilePath);
        customizeProductWindow.setVisible(true);
    }

    private void openUserProfile() {
        User currentUser = User.getCurrentUser();

        if (currentUser != null) {
            String loggedInUserEmail = currentUser.getEmail();
            User user = getUserByEmail(loggedInUserEmail);

            if (user != null) {
                UserProfile userProfile = new UserProfile(this, user, cart);
                userProfile.setVisible(true);
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Error opening user profile. Please log in again.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                User.setCurrentUser(null);
                dispose();

                UserLogin loginScreen = new UserLogin(null);
                loginScreen.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please exit the program first then log in to access the user profile.",
                    "User Profile", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private User getUserByEmail(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[1].equals(email)) {
                    String username = parts[0];
                    String password = parts[2];
                    return new User(username, email, password);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updateProducts(List<Product> updatedProducts) {
        products = updatedProducts;
        displayProducts(products);
    }

    public void updateProductQuantity(String productName, String category, String ram, double price,
            int updatedQuantity, boolean addToCart) {
        String folderPath = "C:/Users/SLY/Desktop/GABION/Product";
        String filePath = folderPath + "/" + productName + ".txt";

        File productFile = new File(filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(productFile))) {
            String name = reader.readLine();
            String currentCategory = reader.readLine();
            String currentRAM = reader.readLine();
            String priceString = reader.readLine();
            String quantityString = reader.readLine();

            List<String> lines = new ArrayList<>();
            while (name != null && currentCategory != null && currentRAM != null && priceString != null
                    && quantityString != null) {
                if (name.equals(productName) && currentCategory.equals(category) && currentRAM.equals(ram)
                        && Double.parseDouble(priceString) == price) {
                    if (addToCart) {
                        int currentQuantity = Integer.parseInt(quantityString);
                        int newQuantity = currentQuantity - updatedQuantity;
                        if (newQuantity < 0) {
                            newQuantity = 0;
                        }
                        lines.add(name);
                        lines.add(currentCategory);
                        lines.add(currentRAM);
                        lines.add(priceString);
                        lines.add(String.valueOf(newQuantity));

                        for (Product product : products) {
                            if (product.getName().equals(productName) && product.getCategory().equals(category)
                                    && product.getRAM().equals(ram) && product.getPrice() == price) {
                                product.setQuantity(newQuantity);
                                break;
                            }
                        }
                    } else {
                        lines.add(name);
                        lines.add(currentCategory);
                        lines.add(currentRAM);
                        lines.add(priceString);
                        lines.add(quantityString);
                    }
                } else {
                    lines.add(name);
                    lines.add(currentCategory);
                    lines.add(currentRAM);
                    lines.add(priceString);
                    lines.add(quantityString);
                }

                name = reader.readLine();
                currentCategory = reader.readLine();
                currentRAM = reader.readLine();
                priceString = reader.readLine();
                quantityString = reader.readLine();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(productFile))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (productDetailsWindow != null && productDetailsWindow.isVisible() && addToCart) {
            productDetailsWindow.updateQuantityLabel(updatedQuantity);
        }
    }

    public List<Product> getProducts() {
        return products;
    }

}
