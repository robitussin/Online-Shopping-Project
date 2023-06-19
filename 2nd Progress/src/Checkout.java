public class Checkout {
    private String item;
    private int quantity;
    private String arrivalDate;
    private double price;
    private String address;
    private String contactNumber;
    private String paymentMethod;

    public Checkout(String item, int quantity, String arrivalDate, double price, String address, String contactNumber, String paymentMethod) {
        this.item = item;
        this.quantity = quantity;
        this.arrivalDate = arrivalDate;
        this.price = price;
        this.address = address;
        this.contactNumber = contactNumber;
        this.paymentMethod = paymentMethod;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        return "Item: " + item + ", Quantity: " + quantity + ", Arrival Date: " + arrivalDate
                + ", Price: " + price + ", Address: " + address + ", Contact Number: " + contactNumber
                + ", Payment Method: " + paymentMethod;
    }
}