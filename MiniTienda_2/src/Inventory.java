import java.util.ArrayList;

public class Inventory {
    private ArrayList<Product> products = new ArrayList();

    public boolean addProduct(String name, double price, int stock) {
        if (this.searchProductsByName(name) != null) {
            return false;
        } else {
            Product newProduct = new Product(name, price, stock);
            this.products.add(newProduct);
            return true;
        }
    }

    public Product searchProductsByName(String name) {
        for(Product product : this.products) {
            if (product.getName().equalsIgnoreCase(name)) {
                return product;
            }
        }

        return null;
    }

    public String listProducts() {
        if (this.products.isEmpty()) {
            return "The inventory is empty";
        } else {
            StringBuilder list = new StringBuilder("--- CURRENT INVENTORY ---\n");

            for(Product product : this.products) {
                list.append(product.toString()).append("\n");
            }

            return list.toString();
        }
    }

    public int buyProduct(String name, int quantity) {
        Product product = this.searchProductsByName(name);
        if (product == null) {
            return 1;
        } else if (product.getStock() < quantity) {
            return 2;
        } else {
            product.setStock(product.getStock() - quantity);
            return 0;
        }
    }

    public Product[] getStatistics() {
        if (this.products.isEmpty()) {
            return new Product[2];
        } else {
            Product cheaper = (Product)this.products.get(0);
            Product mostExpensive = (Product)this.products.get(0);

            for(Product currentProduct : this.products) {
                if (currentProduct.getPrice() < cheaper.getPrice()) {
                    cheaper = currentProduct;
                }

                if (currentProduct.getPrice() > mostExpensive.getPrice()) {
                    mostExpensive = currentProduct;
                }
            }

            return new Product[]{cheaper, mostExpensive};
        }
    }

    public ArrayList<Product> searchForMatchingProduct(String termino) {
        ArrayList<Product> Matching = new ArrayList();

        for(Product product : this.products) {
            if (product.getName().toLowerCase().contains(termino.toLowerCase())) {
                Matching.add(product);
            }
        }

        return Matching;
    }
}
