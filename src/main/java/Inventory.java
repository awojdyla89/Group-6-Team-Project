/*
 * For the purpose of MSU Denver, Fall 2020, CS 3250-52681 course with Dr. Geinitz
 * Contributors include Hector Cruz; Riley Strong; Firew Handiso; Busra Ozdemir; Adam Wojdyla; Dakota Miller
 */

import java.util.*;
import java.io.*;

/**
 * Inventory class is used to establish data structure to store collection of Product objects.
 */

public class Inventory {

    private static final String fileName = "inventory_team6.csv";

    //Hash map used as data structure to store collection of Product objects
    private HashMap<String, Product> inventoryMap;

    //Variable initialization - used in file reading to index input based on specific positional ordinance
    private final int prodIDIndex = 0;
    private final int quantityIndex = 1;
    private final int wholesaleIndex = 2;
    private final int salePriceIndex = 3;
    private final int supplierIndex = 4;

    /**
     * No-arg constructor, moves directly into loading an inventory object with collection of Product objects
     *
     * @throws FileNotFoundException        Used to capture bad execution due to poor file path
     */
    public Inventory() throws FileNotFoundException {
        loadInventory();
    }

    /**
     * Iterates through .csv file & produces data structure consisting of Product objects
     *  Data structure will be modified throughout program execution, then output back into .csv file
     *
     * @throws FileNotFoundException        Used to capture bad execution due to poor file path
     */
    public void loadInventory() throws FileNotFoundException {

        //scanner object created to gather file input
        Scanner input = new Scanner(new File(fileName));
        inventoryMap = new HashMap<>();

        // iterates through each line of .csv file, reading & storing input into Hash Map
        input.nextLine();
        while (input.hasNext()) {
            String[] categories = input.nextLine().split(",");
            String prodID = categories[prodIDIndex];
            int quantity = Integer.parseInt(categories[quantityIndex]);
            double wholesale = Double.parseDouble(categories[wholesaleIndex]);
            double salePrice = Double.parseDouble(categories[salePriceIndex]);
            String supplierID = categories[supplierIndex];

            Product newProduct = new Product(prodID, quantity, wholesale, salePrice, supplierID);
            inventoryMap.put(prodID, newProduct);
        }

        //file reading is complete; closes Scanner object to ensure no memory leak
        input.close();
    }
    /**
     * Outputs result of all file edits into file
     *
     * @throws FileNotFoundException        Used to capture bad execution due to poor file path
     */
    public void update() throws FileNotFoundException {
        File file = new File(fileName);
        PrintWriter writer = new PrintWriter(file);
        writer.println("product_id,quantity,wholesale_cost,sale_price,supplier_id");
        Iterator<Product> itr = iterator();
        while (itr.hasNext()) {
            writer.println(itr.next());
        }
        writer.close();
    }
    /**
     * Used to transform data into 2-D Array for visual display to user
     *
     * @return      2-D Array of inventoryMap data structure
     */
    public Object[][] toArray() {
        Object[][] data = new Object[inventoryMap.size()][5];
        Iterator<Product> product = inventoryMap.values().iterator();
        for (int i = 0; i < inventoryMap.size(); i++) {
            data[i] = product.next().toArray();
        }
        return data;
    }
    /**
     * Searches inventory data structure (hash map) for a specific Product ID
     *
     * @param productID    Product ID (unique identifier of a Product object)
     * @return      True/False result of if provided Product ID currently exists in data structure
     */
    public boolean contains(String productID) {
        return inventoryMap.containsKey(productID);
    }
    /**
     * Utilizes Collections framework to determine if extended custom data structure has no elements
     *
     * @return      True/False determination of if project data structure has any elements
     */
    public boolean isEmpty() {
        return inventoryMap.isEmpty();
    }
    /**
     * Adds a new Product object to the data structure based on Product ID (unique identifier)
     *
     * @param product   product object
     * @return          Previous value associated with Product Object of same Product ID; null if DNE
     */
    public void addProduct(Product product) {
        inventoryMap.put(product.getProductID(), product);
    }
    /**
     * Accepts string parameter for Product ID; locates the Product associated & removes it from data structure
     *
     * @param productID 12-character alphanumeric String uniquely identifying Product object
     * @return      returns Product object if one existed; otherwise, returns null
     */
    public void deleteProduct(String productID) {
        inventoryMap.remove(productID);
    }
    /**
     * Searches for Product object within data structure
     *
     * @param productID 12-character alphanumeric String uniquely identifying Product object
     * @return      returns String representation of Product object
     */
    public String searchProduct(String productID) {
        return inventoryMap.get(productID).toString();
    }

    /**
     *
     *
     * @param productID    12-character alphanumeric String uniquely identifying Product object
     * @param desiredQuantity  Desired quantity to sell
     * @return      True/False result if the quantity sell operation is possible to be performed (won't go negative)
     */
    public boolean quantityValidation(String productID, int desiredQuantity) {
        return (inventoryMap.get(productID).getQuantity() - desiredQuantity) >= 0;
    }

    /**
     *
     * @param productID    12-character alphanumeric String uniquely identifying Product object
     * @param amount integer
     */
    public void incrementQuantity(String productID, int amount) {
        Product product = inventoryMap.get(productID);
        product.setQuantity(product.getQuantity() + amount);
    }

    /**
     *
     * @param productID    12-character alphanumeric String uniquely identifying Product object
     * @param amount integer
     */
    public void decrementQuantity(String productID, int amount) {
        Product product = inventoryMap.get(productID);
        product.setQuantity(product.getQuantity() - amount);
    }

    /**
     * Returns Inventory-specific quantity.
     * @param productID String a valid product ID (as of Oct 2020, 12-digit alphanumeric)
     * @return Returns product inventory's quantity instance variable
     */
    public int getQuantity(String productID) {
        return inventoryMap.get(productID).getQuantity();
    }

    /**
     * Returns Inventory-specific WholeSale.
     * @param productID String a valid product ID (as of Oct 2020, 12-digit alphanumeric)
     * @return Returns product inventory's wholesale instance variable
     */
    public double getWholeSale(String productID) {
        return inventoryMap.get(productID).getWholesale();
    }

    /**
     * Returns Inventory-specific SalePrice.
     * @param productID String a valid product ID (as of Oct 2020, 12-digit alphanumeric)
     * @return Returns product inventory's sale price instance variable
     */
    public double getSalePrice(String productID) {
        return inventoryMap.get(productID).getSalePrice();
    }

    /**
     * Assumes valid input passed in, sets productID instance variable for a Inventory object
     * @param productID a valid product ID (as of Oct 2020, 12-digit alphanumeric)
     * @param price double
     */
    public void setSalePrice(String productID, double price) {
        inventoryMap.get(productID).setSalePrice(price);
    }

    /**
     * Assumes valid input passed in, sets WholeSalePrice instance variable for a Inventory object
     * @param productID a valid product ID (as of Oct 2020, 12-digit alphanumeric)
     * @param price double
     */
    public void setWholeSalePrice(String productID, double price) {
        inventoryMap.get(productID).setWholesale(price);
    }

    /**
     * Assumes valid input passed in, sets Quantity instance variable for a Inventory object
     * @param productID a valid product ID (as of Oct 2020, 12-digit alphanumeric)
     * @param quantity integer
     */
    public void setQuantity(String productID, int quantity) {
        inventoryMap.get(productID).setQuantity(quantity);
    }

    public Iterator<Product> iterator() {
        return inventoryMap.values().iterator();
    }
}