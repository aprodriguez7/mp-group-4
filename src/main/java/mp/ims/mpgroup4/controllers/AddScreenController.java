package mp.ims.mpgroup4.controllers;

import drivers.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller class for the AddScreen.fxml file, responsible for handling user interactions and managing data for adding or modifying items.
 *
 * @author Arianne Acosta
 * @author Joy Arellano
 * @author Clark Rodriguez
 */
public class AddScreenController {

    @FXML
    private TextField enterSKU;
    @FXML
    private TextField enterItem;
    @FXML
    private TextField enterCategory;
    @FXML
    private TextField enterSize;
    @FXML
    private TextField enterUnit;
    @FXML
    private TextField enterBrand;
    @FXML
    private TextField enterColor;
    @FXML
    private TextField enterType;
    @FXML
    private TextField enterDesc;

    private Stage popup;
    private Item item;
    public boolean okClicked = false;
    ObservableList<String> row = FXCollections.observableArrayList();

    /**
     * Sets the popup stage for the controller.
     *
     * @param popup The stage for the popup window.
     */
    public void setPopup(Stage popup){this.popup = popup;}

    /**
     * Sets the item to be displayed and edited in the form.
     *
     * @param item The item to be displayed and edited.
     */
    public void setItem(Item item){
        this.item = item;

        enterItem.setText(item.getItem());
        enterCategory.setText(item.getCategory());
        enterBrand.setText(item.getBrand());
        enterSize.setText(Integer.toString(item.getItemsize()));
        enterUnit.setText(item.getUnit());
        enterColor.setText(item.getColor());
        enterType.setText(item.getType());
        enterDesc.setText(item.getDescription());
    }

    /**
     * Handler method for when the OK button is clicked.
     */
    public void handleOK(){
        try{
            if(enterItem.getText() == null){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Please fill in the required fields accordingly.");
                alert.show();
            } else if (enterCategory.getText() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Please fill in the required fields accordingly.");
                alert.show();
            } else if (enterBrand.getText() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Please fill in the required fields accordingly.");
                alert.show();
            } else if (enterSize.getText() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Please fill in the required fields accordingly.");
                alert.show();
            } else if (enterUnit.getText() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Please fill in the required fields accordingly.");
                alert.show();
            } else if (enterColor.getText() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Please fill in the required fields accordingly.");
                alert.show();
            } else if (enterDesc.getText() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Please fill in the required fields accordingly.");
                alert.show();
            }else {
                item.setItem(enterItem.getText());

                row = ItemController.searchbySKU(item.getItem());

                item.setCategory(enterCategory.getText());
                item.setBrand(enterBrand.getText());
                item.setUnit(enterUnit.getText());
                item.setColor(enterColor.getText());
                item.setType(enterType.getText());
                item.setDescription(enterDesc.getText());

                if(row.isEmpty()){
                    item.setItemsize(Integer.parseInt(enterSize.getText()));
                    item.setSKU(ItemController.generateSKU(item));
                } else {
                    String SKU = String.valueOf(row.get(0));
                    int sizeAdd = Integer.parseInt(row.get(6));
                    int sizeTotal = sizeAdd + (Integer.parseInt(enterSize.getText()));

                    item.setSKU(SKU);
                    item.setItemsize(sizeTotal);
                }

                okClicked = true;
                popup.close();
            }
        }catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Enter numerical values only.");
            alert.show();
        } catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Invalid input.");
            alert.show();
        }
    }

    /**
     * Handles the action when the Search button is clicked.
     * Searches for an item and populates the form with its details if found.
     */
    public void handleSearch(){
        Item item = new Item();

        item.setSKU(enterSKU.getText());
        row = ItemController.searchbySKU(enterSKU.getText());

        if(!row.isEmpty()){
            enterItem.setText(row.get(1));
            enterCategory.setText(row.get(2));
            enterBrand.setText(row.get(3));
            enterUnit.setText(row.get(5));
            enterColor.setText(row.get(6));
            enterType.setText(row.get(7));
            enterDesc.setText(row.get(8));
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Item does not exist!");
            alert.show();
        }

    }

    /**
     * Handles the action when the Cancel button is clicked.
     * Closes the popup window without making any changes.
     */
    public void handleCancel(){popup.close();}
}
