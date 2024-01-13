package mp.ims.mpgroup4.controllers;

import drivers.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;

/**
 * Controller class for the ImportScreen.fxml file, responsible for handling user interactions related to importing data from CSV files.
 *
 * @author Arianne Acosta
 * @author Joy Arellano
 * @author Clark Rodriguez
 */
public class ImportScreenController {
    @FXML
    private TextField enterFile;
    private Stage popup;
    public boolean okClicked = false;
    private String fileString;
    private String itemString;
    private String categoryString;
    private String brandString;
    private int sizeInt;
    private String unitString;
    private String colorString;
    private String typeString;
    private String descString;
    private ObservableList<String> row;
    private static ObservableList<String> updateQueries = FXCollections.observableArrayList();

    /**
     * Sets the popup stage for the controller.
     *
     * @param popup The stage for the popup window.
     */
    public void setPopup(Stage popup){this.popup = popup;}

    /**
     * Handles the action when the OK button is clicked.
     *
     * @throws FileNotFoundException If the specified file is not found.
     */
    public void handleOK() throws FileNotFoundException{
        try{
            fileString = enterFile.getText();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileString));
            String s;

            Item item = new Item();
            while((s = bufferedReader.readLine()) != null){
                String[] partitions = s.split(",");

                itemString = partitions[0].replaceAll("'", "");
                categoryString = partitions[1].replaceAll("'", "");
                brandString = partitions[2].replaceAll("'", "");
                sizeInt = Integer.parseInt(partitions[3]);
                unitString = partitions[4].replaceAll("'", "");

                row = ItemController.searchbyItem(itemString);
                if(partitions[5].equals(".")){
                    colorString = "";
                } else {
                    colorString = partitions[5].replaceAll("'", "");
                }

                if (partitions[6].equals("-")){
                    typeString = "";
                } else {
                    typeString = partitions[6].replaceAll("'", "");
                }

                if (partitions[7].equals("-")){
                    descString = "";
                } else {
                    descString = partitions[7].replaceAll("'", "");
                }

                item.setItem(itemString);
                item.setCategory(categoryString);
                item.setBrand(brandString);
                item.setItemsize(sizeInt);
                item.setUnit(unitString);
                item.setColor(colorString);
                item.setType(typeString);
                item.setDescription(descString);

                if(row.isEmpty()){
                    item.setSKU(ItemController.generateSKU(item));
                    ItemController.addSQL(item);
                } else {
                    int sizeAdd = Integer.parseInt(row.get(4));
                    int sizeTotal = sizeAdd + sizeInt;
                    String SKU = String.valueOf(row.get(0));

                    item.setSKU(SKU);
                    item.setItemsize(sizeTotal);
                    String updateQuery = setUpdateQueries(item);
                    updateQueries.add(updateQuery);
                }
            }

        } catch (FileNotFoundException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Invalid file name.");
            alert.show();
        } catch (IOException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Invalid file-path");
            alert.show();
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Content mismatch within csv file.");
            alert.show();
        } catch (ArrayIndexOutOfBoundsException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Invalid .csv formatting");
            alert.show();
        }

        okClicked = true;
        popup.close();
    }

    /**
     * Generates an SQL update query for an item.
     *
     * @param item The item for which the update query is generated.
     * @return The SQL update query as a string.
     */
    public String setUpdateQueries(Item item){
        String value1 = item.getSKU().replaceAll("'", "");
        String value2 = item.getItem().replaceAll("'", "");
        String value3 = item.getCategory().replaceAll("'", "");
        String value4 = item.getBrand().replaceAll("'", "");
        String value5 = String.valueOf(item.getItemsize());
        String value6 = item.getUnit().replaceAll("'", "");;
        String value7 = item.getColor().replaceAll("'", "");
        String value8 = item.getType().replaceAll("'", "");
        String value9 = item.getDescription().replaceAll("'", "");

        String SQL_UPDATE = "UPDATE Item set SKU='" + value1 + "', Item='" +
                value2 + "', Category='" + value3 + "', Brand='" +
                value4 + "', Amount='" + value5 + "', Unit='" + value6
                + "', Color='" + value7 + "', Type='" + value8
                + "', Description='" + value9
                + "' WHERE SKU='" + value1 + "'";

        return SQL_UPDATE;
    }

    /**
     * Returns the list of update queries for existing items.
     *
     * @return The list of update queries.
     */
    public static ObservableList<String> returnQueries(){return updateQueries;}

    /**
     * Handles the action when the Cancel button is clicked.
     * Closes the popup window without making any changes.
     */
    public void handleCancel(){popup.close();}

}
