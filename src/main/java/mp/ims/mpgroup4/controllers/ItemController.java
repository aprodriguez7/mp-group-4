package mp.ims.mpgroup4.controllers;

import drivers.Item;
import javafx.fxml.Initializable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import primary.Main;

import java.io.IOException;
import java.net.URL;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.*;
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

    public boolean addClicked = false;
    private Main main;

    ObservableList<Item> listItems;
    ObservableList<String> rows;
    public void setMain(Main main){
        this.main = main;
        itemTable.setItems(main.getItemData());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateTable();
    }

    //JFX BUTTON HANDLING
    public void handleAdd() throws  IOException{
        addClicked = true;
        Item newItem = new Item();
        //PIN
        boolean okClicked = main.showAddScreen(newItem);
        rows = seleectSQL(newItem.getItem());

        if(okClicked){
            main.getItemData().add(newItem);
            if(rows.isEmpty()){
                addSQL(newItem);
                updateTable();
            }else {
                updateSQL(newItem);
                updateTable();
            }
        }
    }

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

    public void handleRestock() throws IOException {
        Item item = new Item();

        boolean okClicked = main.showRestockScreen(item);

        if(okClicked){
            updateSQL(item);
            updateTable();
        }
    }

    public void handleUsage() throws IOException {
        Item item = new Item();

        boolean okClicked = main.showItemUsageScreen(item);
        if(okClicked){
            updateSQL(item);
            updateTable();
        }
    }

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

    public void handleImport() {
        try{
            Item item = new Item();
            boolean okClicked = main.showImportScreen(item);
            ObservableList<String> individualItem = ImportScreenController.returnQueries();

            if(okClicked){
                individualItem = ImportScreenController.returnQueries();
                for (int i = 0; i < individualItem.size()-1; ++i){
                    updateSQLImport(individualItem.get(i));
                    updateTable();
                }
            }

        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Invalid .csv formatting.");

            alert.show();
        }
    }

    public static String generateSKU(Item item){

        String category = item.getCategory();
        String name = item.getItem();

        return generateSKUSupport(category, name);
    }

    //SKU SUPPORT METHODS
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

    private static boolean isVowel(char ch) {return "AEIOUaeiou".indexOf(ch) != -1;}

    //SQL METHODS
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

    public static ObservableList<Item> loadItems(){
        Connection connection = null;
        Statement statement = null;

        ObservableList<Item> lists = FXCollections.observableArrayList();
        try{
            //PIN: MANAGE DATABASE URL!!
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Ratatouille?user=root&password=$Qlbench3r20");
            statement = connection.prepareStatement("select * from Item");
            ResultSet resultSet = statement.executeQuery("");

            while(resultSet.next()){
                lists.add(new Item(resultSet.getString("SKU"), resultSet.getString("Item"),
                        resultSet.getString("Category"), resultSet.getString("Brand"),
                        resultSet.getInt("Amount"),resultSet.getString("Unit"),
                        resultSet.getString("Color"), resultSet.getString("Type"),
                        resultSet.getString("Description")));
            }
        }catch (Exception e){

        }

        return lists;
    }

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

    public static ObservableList<String> seleectSQL(String SKUCode){
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

            final String SQL_UPDATE = "UPDATE Ingredient set SKU='" + value1 + "', Item='" +
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

    public void updateTable(){
        skuCol.setCellValueFactory(cellData -> cellData.getValue().SKUProperty());
        itemCol.setCellValueFactory(cellData -> cellData.getValue().itemProperty());
        categoryCol.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        brandCol.setCellValueFactory(cellData -> cellData.getValue().brandProperty());
        sizeCol.setCellValueFactory(cellData -> cellData.getValue().itemsizeProperty().asString());
        unitCol.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
        colorCol.setCellValueFactory(cellData -> cellData.getValue().colorProperty());
        descCol.setCellValueFactory(cellData -> cellData.getValue().desciptionProperty());

        listItems = loadItems();
        itemTable.setItems(listItems);
    }

    //Manual Handling
    public void openPDF(){main.pdf();}
}
