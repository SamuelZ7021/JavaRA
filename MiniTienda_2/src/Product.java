public class Product {
    private String name;
    private double price;
    private int stock;

    public Product(String name, double price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }

    public int getStock() {
        return this.stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String toString() {
        String var10000 = this.name;
        return "Product: " + var10000 + " \n Price: $" + String.format("%.2f", this.price) + "\n Stock:" + this.stock;
    }
}
