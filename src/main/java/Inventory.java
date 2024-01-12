/**
 * Primary class for the inventory management component of the project.
 * <p>
 *     Inherits methods from {@link SQL} and handles the full implementation of the inventory
 *     management component of the project. Contains methods to handle SKU generation and SKU-based
 *     management. Alongside this, contains the raw methods to input and manage items manually
 *     should the user desire.
 * </p>
 * @author Arianne Acosta
 * @author Joy Arellano
 * @author Clark Rodriguez
 *
 * @deprecated
 */

import java.util.*;
/*
public class Inventory {

    private Map<String, InventoryItem> inventory;

    public Inventory() {
        this.inventory = new HashMap<>();
    }

    //teseter main
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Inventory inventoryManager = new Inventory();

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("[1] View inventory");
            System.out.println("[2] Add item to inventory");
            System.out.println("[3] Delete item from inventory");
            System.out.println("[4] Add item usage");
            System.out.println("[5] Exit");
            System.out.print("Enter your choice (1-5): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    inventoryManager.viewInventory();
                    break;
                case 2:
                    inventoryManager.addItem(scanner);
                    break;
                case 3:
                    inventoryManager.deleteItem(scanner);
                    break;
                //Case 4 is for fixing, countable vs weighed
                //e.g. coke (inventory must show how many cokes (in diff sizes) we have)
                //e.g. meat (pwede natin ilusot na pre-package HAHAHA pero usually these are consumed in diff amounts)
                case 4:
                    inventoryManager.addItemUsage(scanner);
                    break;
                case 5:
                    System.out.println("You have successfully exited the program.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }
    }

    private void viewInventory() {
        System.out.println("\nInventory List:");
        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            for (Map.Entry<String, InventoryItem> entry : inventory.entrySet()) {
                System.out.println(entry.getValue());
            }
        }
    }

    //Add case where user adds an item using SKU
    //Add case where user adds in bulk using CSV

    //MIGRATE INTO CONTROLLER
    private void addItem(Scanner scanner) {
        System.out.print("\nEnter item name: ");
        String itemName = scanner.nextLine();
        System.out.print("Enter category: ");
        String category = scanner.nextLine();

        if (inventory.containsKey(itemName)) {
            //Item with the same name already exists
            InventoryItem existingItem = inventory.get(itemName);

            if (existingItem.getCategory().equalsIgnoreCase(category)) {
                //Same category, merge details and update quantities
                existingItem.mergeDetails(scanner);
            } else {
                //Different category, ask user to choose a different name
                System.out.println("Item with the same name exists but in a different category.");
                System.out.println("Please choose a different item name.");
                return;
            }
        } else {
            //Item with the same name doesn't exist, create a new item
            InventoryItem newItem = new InventoryItem(itemName, category);
            newItem.populateDetails(scanner);
            inventory.put(itemName, newItem);
            System.out.println("Item added to the inventory.");
        }
    }

    private void deleteItem(Scanner scanner) {
        System.out.print("Enter item name to delete: ");
        String itemName = scanner.nextLine();

        if (inventory.containsKey(itemName)) {
            inventory.remove(itemName);
            System.out.println("Item deleted from the inventory.");
        } else {
            System.out.println("Item not found in the inventory.");
        }
    }

    private void addItemUsage(Scanner scanner) {
        System.out.print("Enter item name to add usage: ");
        String itemName = scanner.nextLine();

        if (inventory.containsKey(itemName)) {
            InventoryItem item = inventory.get(itemName);
            System.out.println("Current quantity for " + itemName + ": " + item.getQuantity());
            System.out.print("Enter quantity used (with unit, e.g., 8kg, 330ml): ");
            String usageInput = scanner.nextLine();

            double quantityUsed = convertToUniformUnits(usageInput);

            //Update quantity based on usage
            item.addUsage(quantityUsed);
            System.out.println("Usage entry added for " + itemName);
            System.out.println();
        } else {
            System.out.println("Item not found in the inventory.");
        }
    }

    private double convertToUniformUnits(String input) {
        double value = Double.parseDouble(input.replaceAll("[^0-9.]", ""));
        String unit = input.replaceAll("[0-9.]", "");

        switch (unit.toLowerCase()) {
            case "g":
                return value / 1000.0; //Converts grams to kilograms
            case "kg":
                return value;
            case "ml":
                return value;
            case "l":
                return value * 1000.0; //Converts liters to milliliters
            default:
                System.out.println("Unsupported unit.");
                return value;
        }
    }

}

class InventoryItem {
    private String name;
    private String category;
    private Map<String, String> details;
    private Map<String, String> itemRandomNumberMap;
    private double quantity;


    public InventoryItem(String name, String category) {
        this.name = name;
        this.category = category;
        this.details = new HashMap<>();
        this.itemRandomNumberMap = new HashMap<>();
        this.quantity = 0.0;
    }

    public void populateDetails(Scanner scanner) {
        switch (category.toLowerCase()) {
            case "condiment":
                System.out.print("Enter brand: ");
                details.put("Brand", scanner.nextLine());
                System.out.print("Enter weight (with unit): ");
                details.put("Weight", scanner.nextLine());
                System.out.print("Enter color: ");
                details.put("Color", scanner.nextLine());
                break;
            case "beverage":
                System.out.print("Enter brand: ");
                details.put("Brand", scanner.nextLine());
                System.out.print("Enter volume (with unit): ");
                details.put("Volume", scanner.nextLine());
                System.out.print("Enter type: ");
                details.put("Type", scanner.nextLine());
                break;
            case "meat":
                System.out.print("Enter brand: ");
                details.put("Brand", scanner.nextLine());
                System.out.print("Enter weight (with unit): ");
                details.put("Weight", scanner.nextLine());
                break;
            //Add more cases for menu other categories
            default:
                System.out.println("Invalid category.");
                System.exit(1);
        }

        System.out.print("Do you want to add additional description? (yes/no): ");
        String addDescription = scanner.nextLine().toLowerCase();

        if (addDescription.equals("yes")) {
            System.out.print("Enter description: ");
            details.put("Description", scanner.nextLine());
        }
    }


    @Override
    public String toString() {
        StringBuilder itemDetails = new StringBuilder();
        itemDetails.append("Item: ").append(name).append("\n");
        itemDetails.append("Category: ").append(category);

        //Displays details if available
        if (!details.isEmpty()) {
            for (Map.Entry<String, String> entry : details.entrySet()) {
                itemDetails.append("\n").append(entry.getKey()).append(": ").append(entry.getValue());
            }
        }

        //itemDetails.append("\n").append("Quantity: ").append(quantity);
        //Pls fix the quantity thing of our code

        //Generates SKU and adds it to the item details
        String sku = generateSKU(category, name);
        itemDetails.append("\n").append("SKU: ").append(sku).append("\n");

        return itemDetails.toString();
    }

    private String generateSKUSupport(String category, String item) {
        String key = category + "/" + item;
        if (!itemRandomNumberMap.containsKey(key)) {
            // Generate random 4-digit number if not generated for this item before
            itemRandomNumberMap.put(key, generateRandomDigits(0, 9999));
        }
        //Extracts first three consonants from the category
        String categoryConsonants = getConsonants(category, 3);

        //Extracts first three consonants from the item
        String itemConsonants = getConsonants(item, 3);

        //Reuses the random 4-digit number
        String randomDigits = itemRandomNumberMap.get(key);

        //Constructing the SKU
        return (categoryConsonants + "/" + itemConsonants + "-" + randomDigits).toUpperCase();
    }

    private String getConsonants(String input, int count) {
        StringBuilder consonants = new StringBuilder();
        int consonantCount = 0;
        List<Integer> vowelPositions = new ArrayList<>();

        for (int index = 0; index < input.length(); index++) {
            char ch = input.charAt(index);

            if (Character.isLetter(ch) && !isVowel(ch)) {
                consonants.append(ch);
                consonantCount++;

                if (consonantCount == 3) {
                    break; //Break if we have already found 3 consonants
                }
            } else if (Character.isLetter(ch) && isVowel(ch) && consonantCount < 3) {
                vowelPositions.add(consonants.length()); // Save the position of the vowel
            }
        }

        //If the word contains fewer consonants than required, insert vowels in their original positions
        for (int i = 0; consonantCount < 3 && i < vowelPositions.size(); i++) {
            char ch = input.charAt(vowelPositions.get(i));
            if (Character.isLetter(ch) && isVowel(ch)) {
                consonants.insert(vowelPositions.get(i), Character.toString(ch));
                consonantCount++;
            }
        }
        return consonants.toString();
    }

    private boolean isVowel(char ch) {
        return "AEIOUaeiou".indexOf(ch) != -1;
    }

    private String generateRandomDigits(int min, int max) {
        Random random = new Random();
        int randomNumber = random.nextInt((max - min) + 1) + min;
        return String.format("%04d", randomNumber);
    }

    //This should be called when the program is sure that the item have the same details
    //Mali ata na after item name and category agad
    //If ganun, coke in a bottle and coke in a can would have the same SKUs
    public void mergeDetails(Scanner scanner) {
        System.out.println("Item with the same name and category already exists. Merging details...");

        //Call populateDetails to update details and quantities
        populateDetails(scanner);
    }

    public String getCategory() {
        return category;
    }

    public double getQuantity() {
        return quantity;
    }

    public void addUsage(double quantityUsed) {
        this.quantity -= quantityUsed;
    }

}
*/