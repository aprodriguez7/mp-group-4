package mp.ims.mpgroup4.controllers;

import drivers.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddScreenController {
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

    //Revise based on our implementation.
    private Stage popup;
    private Item item;
    public boolean okClicked = false;
    ObservableList<String> row = FXCollections.observableArrayList();

    public void setPopup(Stage popup){this.popup = popup;}
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

                row = ItemController.seleectSQL(item.getItem());

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

    public void handleSearch(){
        item.setItem(enterItem.getText());
        row = ItemController.seleectSQL(item.getItem());

        if(!row.isEmpty()){
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

    public void handleCancel(){popup.close();}
}
