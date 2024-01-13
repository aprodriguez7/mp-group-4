package mp.ims.mpgroup4.controllers;

import drivers.Item;
import javafx.application.Platform;
import javafx.fxml.Initializable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import primary.Main;

import javax.swing.plaf.nimbus.State;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.*;

/**
 * The `ItemController` class is responsible for managing and controlling the interaction
 * between the graphical user interface (GUI) and the underlying data model for items.
 * It implements the Initializable interface to perform initialization tasks when the
 * associated FXML file is loaded.
 *
 * This controller handles various actions such as adding, updating, restocking, and deleting items.
 * It also provides methods for importing items from a CSV file, generating SKU codes, and interacting
 * with a MySQL database to perform CRUD operations on item data.
 *
 * The SKU (Stock Keeping Unit) generation algorithm is based on the item's category, name, and a random
 * 4-digit code. The class includes methods for handling JFX button events and updating the item table
 * in the GUI.
 *
 * @author Arianne Acosta
 * @author Joy Arellano
 * @author Clark Rodriguez
 */
public class ItemController implements Initializable {
    @FXML
    private TableView<Item> itemTable;
    @FXML
    private TableColumn<Item, String> skuCol;
    @FXML
    private TableColumn<Item, String> itemCol;
    @FXML
    private TableColumn<Item, String> categoryCol;
    @FXML
    private TableColumn<Item, String> brandCol;
    @FXML
    private TableColumn<Item, String> sizeCol;
    @FXML
    private TableColumn<Item, String> unitCol;
    @FXML
    private TableColumn<Item, String> colorCol;
    @FXML
    private TableColumn<Item, String> typeCol;
    @FXML
    private TableColumn<Item, String> descCol;
    @FXML
    private Button exitBut;

    public boolean addClicked = false;
    private Main main;

    ObservableList<Item> listItems;
    ObservableList<String> rows;
    /**
     * Sets the main application instance for this controller.
     *
     * @param main The main application instance.
     */
    public void setMain(Main main){
        this.main = main;
        itemTable.setItems(main.getItemData());
    }

    /**
     * Initializes the controller after its root element has been completely processed.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateTable();
    }

    //JFX BUTTON HANDLING
    /**
     * Handles the addition of a new item. Invoked when the add button is clicked.
     *
     * @throws IOException If an error occurs during the addition process.
     */
    public void handleAdd() throws  IOException{
        addClicked = true;
        Item newItem = new Item();

        boolean okClicked = main.showAddScreen(newItem);
        rows = searchbyItem(newItem.getItem());
        if (okClicked) {
            Item existingItem = isExisting(newItem);

            if (existingItem != null) {
                existingItem.setItemsize(existingItem.getItemsize() + newItem.getItemsize());
                updateSQL(existingItem);
            } else {
                main.getItemData().add(newItem);
                addSQL(newItem);
            }

            Platform.runLater(() -> {
                updateTable();
                itemTable.refresh();
            });


        }
    }

    /**
     * Handles the update of an existing item. Invoked when the Update button is clicked.
     *
     * @throws IOException If an error occurs during the update process.
     */
    public void handleUpdate() throws  IOException {
        Item chosenItem = itemTable.getSelectionModel().getSelectedItem();

        if(chosenItem != null){
            boolean okClicked = main.showUpdateScreen(chosenItem);
            if(okClicked){
                updateSQL(chosenItem);
                updateTable();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Select valid item.");

            alert.show();
        }
    }

    /**
     * Handles the restocking of an item. Invoked when the Restock button is clicked.
     *
     * @throws IOException If an error occurs during the restocking process.
     */
    public void handleRestock() throws IOException {
        Item item = new Item();

        boolean okClicked = main.showRestockScreen(item);

        if(okClicked){
            updateSQL(item);
            updateTable();
        }
    }

    /**
     * Handles the usage of an item. Invoked when the Item Usage button is clicked.
     *
     * @throws IOException If an error occurs during the usage process.
     */
    public void handleUsage() throws IOException {
        Item item = new Item();

        boolean okClicked = main.showItemUsageScreen(item);
        if(okClicked){
            updateSQL(item);
            updateTable();
        }
    }

    /**
     * Handles the deletion of an item. Invoked when the corresponding button is clicked.
     */
    public void handleDelete() {
        try{
            int index = itemTable.getSelectionModel().getSelectedIndex();
            deleteEntry(itemTable.getSelectionModel().getSelectedItem());
            itemTable.getItems().remove(index);
            updateTable();
        } catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Input item to delete.");
            alert.show();
        }
    }

    /**
     * Handles the import of items from a CSV file. Invoked when the corresponding button is clicked.
     */
    public void handleImport() {
        try{
            Item item = new Item();
            boolean okClicked = main.showImportScreen(item);
            ObservableList<String> individualItem = ImportScreenController.returnQueries();

            if(okClicked){
                individualItem = ImportScreenController.returnQueries();
                for (int i = 0; i < individualItem.size()-1; ++i){
                    updateSQLImport(individualItem.get(i));
                }
                Platform.runLater(() -> {
                    updateTable();
                    itemTable.refresh();
                });
            }

        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Invalid .csv input. Please ensure correct file-path");

            alert.show();
        }
    }

    public void handleExport(){
        try{
            boolean okClicked = main.showExportScreen();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Invalid file-path");

            alert.show();
        }
    }
    /**
     * Generates an SKU for the given item based on its category and name. Invokes support method
     * to generate the various components.
     *
     * @param item The item for which to generate the SKU.
     * @return The generated SKU code.
     */
    public static String generateSKU(Item item){

        String category = item.getCategory();
        String name = item.getItem();

        return generateSKUSupport(category, name);
    }

    //SKU SUPPORT METHODS

    /**
     * Support Method for generating the Item's SKU
     * @param category the String category of the item, for generating.
     * @param item the Name of the item, for generating
     * @return the generated SKU entry.
     */
    private static String generateSKUSupport(String category, String item) {

        //Extracts first three consonants from the category
        String categoryConsonants = getConsonants(category, 3);

        //Extracts first three consonants from the item
        String itemConsonants = getConsonants(item, 3);

        //Generating random 4-Digit number.
        int rawCode = (int) Math.floor(Math.random() * (9999 - 0000 + 1));
        String preCode = String.valueOf(rawCode);
        StringBuilder finalCode = new StringBuilder();

        if(preCode.length() < 4 && preCode.length() > -1){
            int length = 4 - preCode.length();
            for(int i = 0; i < length; i++){
                finalCode.append(0);
            }
            finalCode.append(preCode);
        }else{
            finalCode.append(preCode);
        }

        //Constructing the SKU
        return (categoryConsonants + "/" + itemConsonants + "-" + finalCode).toUpperCase();
    }

    /**
     * Support method. Invoked when generating SKU.
     * @param input Item Name
     * @param count indexing variable
     * @return partitioned Consonants
     */
    private static String getConsonants(String input, int count) {
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

    /**
     * Support method for determining vowels within a given String
     * @param ch Indexing character for String input
     * @return True - ch is a Vowel, False - ch is not a Vowel
     */
    private static boolean isVowel(char ch) {return "AEIOUaeiou".indexOf(ch) != -1;}

    //SQL METHODS

    /**
     * The `updateSQLImport` method updates the MySQL database by executing the provided SQL_UPDATE statement.
     * It is primarily used for importing data from a CSV file.
     * @param SQL_UDPATE The SQL statement to execute for updating the database.
     */
    public static void updateSQLImport(String SQL_UDPATE){
        Connection connection = null;
        Statement statement = null;

        try{
            //PIN: MANAGE DATABASE URL!!!!
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Ratatouille?user=root&password=$Qlbench3r20");
            statement = connection.createStatement();
            statement.executeUpdate(SQL_UDPATE);
            statement.close();
            connection.close();
        }catch (SQLException e){
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }finally {
            try{
                if(connection != null){
                    connection.close();
                }
            }catch (SQLException ee){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("ERROR: Inappropriate .csv format.");
                alert.show();
            }
        }

    }

    /**
     * The `loadItems` method retrieves item data from the MySQL database and returns it as an ObservableList.
     *
     * @return An ObservableList containing Item objects loaded from the database.
     */
    public static ObservableList<Item> loadItems(){
        Connection connection = null;
        PreparedStatement statement = null;

        ObservableList<Item> lists = FXCollections.observableArrayList();
        try{
            //PIN: MANAGE DATABASE URL!!
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Ratatouille?user=root&password=$Qlbench3r20");
            statement = connection.prepareStatement("select * from Item");
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                lists.add(new Item(resultSet.getString("SKU"), resultSet.getString("Item"),
                        resultSet.getString("Category"), resultSet.getString("Brand"),
                        resultSet.getInt("Amount"),resultSet.getString("Unit"),
                        resultSet.getString("Color"), resultSet.getString("Type"),
                        resultSet.getString("Description")));
            }
        }catch (Exception e){
            e.printStackTrace();
            System.err.println("Error loading database");
        }

        return lists;
    }

    /**
     * The `addSQL` method adds a new item to the MySQL database using the provided Item object.
     *
     * @param item The Item object to be added to the database.
     */
    public static void addSQL(Item item){
        Connection connection = null;
        PreparedStatement statement = null;

        final String SQL_INSERT = "INSERT INTO Item (SKU, Item, Category, Brand, Amount, Unit, Color, Type, Description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";


        try{
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Ratatouille?user=root&password=$Qlbench3r20");
            statement = connection.prepareStatement(SQL_INSERT);

            statement.setString(1, item.getSKU());
            statement.setString(2, item.getItem());
            statement.setString(3, item.getCategory());
            statement.setString(4, item.getBrand());
            statement.setString(5, String.valueOf(item.getItemsize()));
            statement.setString(6, item.getUnit());
            statement.setString(7, item.getColor());
            statement.setString(8, item.getType());
            statement.setString(9, item.getDescription());

            statement.executeUpdate();


        }catch (SQLException e){
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     * The `searchbySKU` method searches for items in the database based on the given SKU code.
     *
     * @param SKUCode The SKU code used to search for items in the database.
     * @return An ObservableList containing information about items matching the given SKU code.
     */
    public static ObservableList<String> searchbySKU(String SKUCode){
        Connection connection = null;
        PreparedStatement statement = null;

        ObservableList<String> list = FXCollections.observableArrayList();

        try{
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Ratatouille?user=root&password=$Qlbench3r20");
            statement = connection.prepareStatement("select * from Item WHERE SKU='" + SKUCode + "'");
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                list.add(resultSet.getString("SKU"));
                list.add(resultSet.getString("Item"));
                list.add(resultSet.getString("Category"));
                list.add(resultSet.getString("Brand"));
                list.add(Integer.toString(resultSet.getInt("Amount")));
                list.add(resultSet.getString("Unit"));
                list.add(resultSet.getString("Color"));
                list.add(resultSet.getString("Type"));
                list.add(resultSet.getString("Description"));

            }
        } catch (SQLException e){
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return list;
    }

    /**
     * The `searchbyItem` method searches for items in the database based on the given item name.
     *
     * @param itemString The name of the item used to search for items in the database.
     * @return An ObservableList containing information about items matching the given item name.
     */
    public static ObservableList<String> searchbyItem(String itemString){
        Connection connection = null;
        PreparedStatement statement = null;

        ObservableList<String> list = FXCollections.observableArrayList();

        try{
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Ratatouille?user=root&password=$Qlbench3r20");
            statement = connection.prepareStatement("select * from Item WHERE Item='" + itemString + "'");
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                list.add(resultSet.getString("SKU"));
                list.add(resultSet.getString("Item"));
                list.add(resultSet.getString("Category"));
                list.add(resultSet.getString("Brand"));
                list.add(Integer.toString(resultSet.getInt("Amount")));
                list.add(resultSet.getString("Unit"));
                list.add(resultSet.getString("Color"));
                list.add(resultSet.getString("Type"));
                list.add(resultSet.getString("Description"));
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return list;
    }

    /**
     * The `updateSQL` method updates an existing item in the MySQL database using the provided Item object.
     *
     * @param item The Item object containing updated information.
     */
    public static void updateSQL(Item item){
        Connection connection = null;
        PreparedStatement statement = null;

        try{
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Ratatouille?user=root&password=$Qlbench3r20");

            String value1 = item.getSKU();
            String value2 = item.getItem();
            String value3 = item.getCategory();
            String value4 = item.getBrand();
            String value5 = String.valueOf(item.getItemsize());
            String value6 = item.getUnit();
            String value7 = item.getColor();
            String value8 = item.getType();
            String value9 = item.getDescription();

            final String SQL_UPDATE = "UPDATE Item set SKU='" + value1 + "', Item='" +
                    value2 + "', Category='" + value3 + "', Brand='" + value4 + "', Amount='"
                    + value5 + "', Unit='" + value6 + "', Color='" + value7 + "', Type='"
                    + value8 + "', Description='" + value9 + "' WHERE SKU='" + value1 + "'";

            statement = connection.prepareStatement(SQL_UPDATE);
            statement.executeUpdate();
        }catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Error!");

                alert.show();
            }
        }
    }

    /**
     * The `deleteEntry` method deletes an item from the MySQL database based on the provided Item object.
     *
     * @param item The Item object to be deleted from the database.
     */
    public static void deleteEntry(Item item){
        Connection connection = null;
        PreparedStatement statement = null;

        final String SQL_DELETE = "DELETE FROM Item WHERE SKU = ?";

        try {
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Ratatouille?user=root&password=$Qlbench3r20");

            statement = connection.prepareStatement(SQL_DELETE);
            statement.setString(1, item.getSKU());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Error!");

                alert.show();
            }
        }
    }

    /**
     * The `updateTable` method refreshes the TableView in the GUI with the latest item data.
     */
    public void updateTable(){
        skuCol.setCellValueFactory(cellData -> cellData.getValue().SKUProperty());
        itemCol.setCellValueFactory(cellData -> cellData.getValue().itemProperty());
        categoryCol.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        brandCol.setCellValueFactory(cellData -> cellData.getValue().brandProperty());
        sizeCol.setCellValueFactory(cellData -> cellData.getValue().itemsizeProperty().asString());
        unitCol.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
        colorCol.setCellValueFactory(cellData -> cellData.getValue().colorProperty());
        typeCol.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        descCol.setCellValueFactory(cellData -> cellData.getValue().desciptionProperty());

        listItems = loadItems();
        itemTable.setItems(listItems);
    }

    /**
     * The `handleExit` method exits the application when the corresponding button is clicked.
     */
    @FXML
    public void handleExit(){System.exit(0);}

    /**
     * The `isExisting` method checks if the given Item object already exists in the main application's item data.
     *
     * @param item The Item object to check for existence.
     * @return The existing Item object if found, or null if not found.
     */
    private Item isExisting(Item item){
        for(Item existingItems : main.getItemData()){
            if (existingItems.equals(item)){
                return existingItems;
            }
        }
        return null;
    }


    /**
     * Void method to handle opening the User Manual.
     */
    public void openPDF(){main.pdf();}
}
