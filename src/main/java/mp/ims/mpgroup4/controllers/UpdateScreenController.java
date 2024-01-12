package mp.ims.mpgroup4.controllers;

import drivers.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UpdateScreenController {
    @FXML
    private Label itemLabel;
    @FXML
    private Label categoryLabel;
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
    private String SKUString;
    private String itemString;
    private String categoryString;

    public void setPopup(Stage popup){this.popup = popup;}

    public void setItem(Item item){
        this.item = item;

        SKUString = item.getSKU();
        itemString = item.getItem();
        categoryString = item.getCategory();

        itemLabel.setText(itemString);
        categoryLabel.setText(categoryString);

        enterBrand.setText(item.getBrand());
        enterSize.setText(Integer.toString(item.getItemsize()));
        enterUnit.setText(item.getUnit());
        enterColor.setText(item.getColor());
        enterType.setText(item.getType());
        enterDesc.setText(item.getDescription());
    }

    public void handleOK(){
        try{
            if ((Integer.parseInt(enterSize.getText()))< 0){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Enter positive values.");
                alert.show();
            }else {
                item.setSKU(SKUString);
                item.setItem(itemString);
                item.setCategory(categoryString);
                item.setBrand(enterBrand.getText());
                item.setItemsize(Integer.parseInt(enterSize.getText()));
                item.setUnit(enterUnit.getText());
                item.setColor(enterColor.getText());
                item.setType(enterType.getText());
                item.setDescription(enterDesc.getText());

                popup.close();
            }

            okClicked = true;

        }catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("ERROR! Enter numerical values.");
            alert.show();
        }
    }

    public void handleCancel(){popup.close();}

}
