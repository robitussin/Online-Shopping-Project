import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CustomizeProduct extends JFrame {
    private ProductCatalog productCatalog;
    private JPanel productsPanel;
    private String productsFilePath; 

    public CustomizeProduct(ProductCatalog productCatalog, List<Product> products, String productsFilePath) {
        this.productCatalog = productCatalog;
        this.productsFilePath = productsFilePath;
        setTitle("Customize Products");
        setSize(400, 400);
        setLayout(new GridLayout(0, 1));

        productsPanel = new JPanel(new GridLayout(0, 1));

        for (Product product : products) {
            addProductPanel(product);
        }

        add(productsPanel);

        JButton addProductButton = new JButton("Add Product");
        addProductButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAddProductDialog();
            }
        });
        add(addProductButton);
    }

    private void addProductPanel(Product product) {
        JPanel productPanel = new JPanel(new GridLayout(1, 3));
        JLabel nameLabel = new JLabel(product.getName());
        JButton removeButton = new JButton("Remove");
        JButton customizeButton = new JButton("Edit");

        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeProduct(product);
            }
        });

        customizeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                customizeProduct(product);
            }
        });

        productPanel.add(nameLabel);
        productPanel.add(removeButton);
        productPanel.add(customizeButton);
        productsPanel.add(productPanel);
    }

    private void removeProduct(Product product) {
        List<Product> updatedProducts = productCatalog.getProducts();
        updatedProducts.remove(product);
        productCatalog.updateProducts(updatedProducts);
        productsPanel.removeAll();
        for (Product p : updatedProducts) {
            addProductPanel(p);
        }
        productsPanel.revalidate();
        productsPanel.repaint();
        saveProductsToFile(updatedProducts, productsFilePath);
    }

    
    private void customizeProduct(Product product) {
        JPanel inputPanel = new JPanel(new GridLayout(0, 1));

        JTextField nameField = new JTextField(product.getName(), 20);
        JTextField categoryField = new JTextField(product.getCategory(), 20);
        JTextField ramField = new JTextField(product.getRAM(), 20);
        JTextField priceField = new JTextField(String.valueOf(product.getPrice()), 20);
        JTextField quantityField = new JTextField(String.valueOf(product.getQuantity()), 20);

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryField);
        inputPanel.add(new JLabel("RAM:"));
        inputPanel.add(ramField);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(this, inputPanel, "Edit",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String newName = nameField.getText();
            String newCategory = categoryField.getText();
            String newRAM = ramField.getText();
            double newPrice = Double.parseDouble(priceField.getText());
            int newQuantity = Integer.parseInt(quantityField.getText());

            boolean isUpdated = false;
            if (!newName.isEmpty()) {
                product.setName(newName);
                isUpdated = true;
            }
            if (!newCategory.isEmpty()) {
                product.setCategory(newCategory);
                isUpdated = true;
            }
            if (!newRAM.isEmpty()) {
                product.setRAM(newRAM);
                isUpdated = true;
            }
            if (newPrice != product.getPrice()) {
                product.setPrice(newPrice);
                isUpdated = true;
            }
            if (newQuantity != product.getQuantity()) {
                product.setQuantity(newQuantity);
                isUpdated = true;
            }

            if (isUpdated) {
                updateProductPanel(product);
                saveProductsToFile(productCatalog.getProducts(), productsFilePath);
            }
        }
    }
    private void updateProductPanel(Product product) {
        for (Component component : productsPanel.getComponents()) {
            if (component instanceof JPanel) {
                JPanel productPanel = (JPanel) component;
                JLabel nameLabel = (JLabel) productPanel.getComponent(0);
                if (nameLabel.getText().equals(product.getName())) {
                    nameLabel.setText(product.getName());
                    break;
                }
            }
        }
    }

   private void showAddProductDialog() {
        JTextField nameField = new JTextField(20);
        JTextField categoryField = new JTextField(20);
        JTextField ramField = new JTextField(20);
        JTextField priceField = new JTextField(20);
        JTextField quantityField = new JTextField(20);

        JPanel inputPanel = new JPanel(new GridLayout(0, 1));
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryField);
        inputPanel.add(new JLabel("RAM:"));
        inputPanel.add(ramField);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(this, inputPanel, "Add Product",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String category = categoryField.getText();
            String ram = ramField.getText();
            double price = Double.parseDouble(priceField.getText());
            int quantity = Integer.parseInt(quantityField.getText());

            if (!name.isEmpty() && !category.isEmpty() && !ram.isEmpty()) {
                Product newProduct = new Product(name, category, ram, price, quantity);
                List<Product> updatedProducts = productCatalog.getProducts();
                updatedProducts.add(newProduct);
                productCatalog.updateProducts(updatedProducts);
                addProductPanel(newProduct);
                productsPanel.revalidate();
                productsPanel.repaint();
                String newFilePath = productsFilePath;
                saveProductsToFile(updatedProducts, newFilePath);
            }
        }
    }


    private void saveProductsToFile(List<Product> products, String filePath) {
        File folder = new File(filePath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    
        File[] existingFiles = folder.listFiles();
        if (existingFiles != null) {
            for (File file : existingFiles) {
                if (file.isFile()) {
                    file.delete();
                }
            }
        }
    
    for (Product product : products) {
        String productFilePath = folder.getPath() + File.separator + product.getName() + ".txt";
        File file = new File(productFilePath);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(product.getName());
            writer.newLine();
            writer.write(product.getCategory());
            writer.newLine();
            writer.write(product.getRAM());
            writer.newLine();
            writer.write(String.valueOf(product.getPrice()));
            writer.newLine();
            writer.write(String.valueOf(product.getQuantity()));
            writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
             System.out.println("Error");
            }
        }
    }
}