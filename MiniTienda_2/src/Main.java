import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        Inventory myInventory = new Inventory();
        double totalSales = (double)0.0F;
        boolean continuer = true;

        while(continuer) {
            String menu = "<html><body style='width: 200px;'><h3>MENÚ - TIENDA</h3><hr>1. Add product<br>2. List inventory<br>3. Buy product<br>4. View statistics<br>5. Search product<br>6. Exit<br><br><b>Choice a option:</b></body></html>";
            String optionStr = JOptionPane.showInputDialog((Component)null, menu);
            if (optionStr == null) {
                continuer = false;
            } else {
                try {
                    int option = Integer.parseInt(optionStr);
                    switch (option) {
                        case 1:
                            String name = JOptionPane.showInputDialog((Component)null, "Product Name");
                            if (name != null && !name.trim().isEmpty()) {
                                double price = Double.parseDouble(JOptionPane.showInputDialog((Component)null, "Price product"));
                                int stock = Integer.parseInt(JOptionPane.showInputDialog((Component)null, "Stock Initialize"));
                                boolean aggregate = myInventory.addProduct(name, price, stock);
                                if (aggregate) {
                                    JOptionPane.showMessageDialog((Component)null, "Product added successfully");
                                } else {
                                    JOptionPane.showMessageDialog((Component)null, "The product " + name + "already exists in the inventory.", "Error", 0);
                                }
                                break;
                            }

                            JOptionPane.showMessageDialog((Component)null, "The name cannot be empty.", "Error", 0);
                            break;
                        case 2:
                            String list = myInventory.listProducts();
                            JOptionPane.showMessageDialog((Component)null, list + "1");
                            break;
                        case 3:
                            String nameBuy = JOptionPane.showInputDialog((Component)null, "Name of the product to purchase");
                            if (nameBuy != null && !nameBuy.trim().isEmpty()) {
                                Product productOfBuy = myInventory.searchProductsByName(nameBuy);
                                if (productOfBuy == null) {
                                    JOptionPane.showMessageDialog((Component)null, "Product not found.", "Error", 0);
                                } else {
                                    int quantityBuy = Integer.parseInt(JOptionPane.showInputDialog((Component)null, "Quantity of buy"));
                                    int result = myInventory.buyProduct(nameBuy, quantityBuy);
                                    switch (result) {
                                        case 0:
                                            double cost = productOfBuy.getPrice() * (double)quantityBuy;
                                            totalSales += cost;
                                            JOptionPane.showMessageDialog((Component)null, "Purchase made successfully.", "\nTotal: $" + String.format("%.2f", cost), 1);
                                            break;
                                        case 1:
                                            JOptionPane.showMessageDialog((Component)null, "Product not found.", "Error", 0);
                                            break;
                                        case 2:
                                            JOptionPane.showMessageDialog((Component)null, "Insufficient stock. \\nAvailable: " + productOfBuy.getStock(), "Error", 0);
                                    }
                                }
                            }
                            break;
                        case 4:
                            Product[] statistics = myInventory.getStatistics();
                            Product cheaper = statistics[0];
                            Product mostExpensive = statistics[1];
                            if (cheaper != null && mostExpensive != null) {
                                String var10000 = cheaper.toString();
                                String message = "--- STATISTICS THE INVENTORY ---\n\nCheaper product: \n" + var10000 + "\n\nMost expensive product: \n" + mostExpensive.toString();
                                JOptionPane.showMessageDialog((Component)null, message, "Statistics", 1);
                                break;
                            }

                            JOptionPane.showMessageDialog((Component)null, "The inventory is empty.", "Error", 0);
                            break;
                        case 5:
                            String iEndSearch = JOptionPane.showInputDialog((Component)null, "Enter the name of the product you want to search for:");
                            if (iEndSearch == null || iEndSearch.trim().isEmpty()) {
                                break;
                            }

                            ArrayList<Product> results = myInventory.searchForMatchingProduct(iEndSearch);
                            if (results.isEmpty()) {
                                JOptionPane.showMessageDialog((Component)null, "Product not found." + iEndSearch, "Error", 0);
                                break;
                            }

                            StringBuilder messageResults = new StringBuilder("--- SEARCH RESULT--- \n\n");

                            for(Product product : results) {
                                messageResults.append(product.toString()).append("\n");
                            }

                            JOptionPane.showMessageDialog((Component)null, messageResults.toString(), "Search Results", 1);
                            break;
                        case 6:
                            continuer = false;
                            String ticket = String.format("Thank you for your visit.\nTotal purchases in the session: $%.2f", totalSales);
                            JOptionPane.showMessageDialog((Component)null, ticket, "Ticket Final", 1);
                            break;
                        default:
                            JOptionPane.showMessageDialog((Component)null, "Invalid option.", "Error", 0);
                    }
                } catch (NumberFormatException var26) {
                    JOptionPane.showMessageDialog((Component)null, "Entrada inválida. Asegúrate de ingresar números para precio y stock.", "Error", 0);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog((Component)null, "Ocurrió un error inesperado: " + e.getMessage(), "Error", 0);
                }
            }
        }

    }
}
