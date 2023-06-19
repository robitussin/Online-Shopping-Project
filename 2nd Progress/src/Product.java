public class Product {
    private String name;
    private String category;
    private String ram;
    private double price;
    private int quantity;

    public Product(String name, String category, String ram, double price, int quantity) {
        this.name = name;
        this.category = category;
        this.ram = ram;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getRAM() {
        return ram;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setRAM(String ram) {
        this.ram = ram;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getContactNumber() {
        return null;
    }
}