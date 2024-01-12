package mp.ims.mpgroup4.controllers;

import drivers.Item;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ItemUsageScreenController {
    @FXML
    private TextField enterSKU;
    @FXML
    private TextField enterUsage;
    @FXML
    private Label itemLabel;
    @FXML
    private Label categoryLabel;
    @FXML
    private Label brandLabel;
    @FXML
    private Label itemSizeLabel;
    @FXML
    private Label unitLabel;
    @FXML
    private Label colorLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Label descLabel;
    private Stage popup;
    private Item item;
    public boolean okClicked = false;
    ObservableList<String> row;
    private int usage;
    private int originalValue;
    private int newValue;

    public void setPopup(Stage popup){this.popup = popup;}

    public void setItem(Item item){this.item = item;}

    public void handleOK(){
        try{
            usage = Integer.parseInt(enterUsage.getText());
            originalValue = Integer.parseInt(itemSizeLabel.getText());
            newValue = originalValue - usage;

            if(usage > originalValue){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Warning: Usage exceeds current amount.");
                alert.show();
            } else if (usage < 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Warning: Enter positive values.");
                alert.show();
            } else {
                item.setSKU(enterSKU.getText());
                item.setItem(itemLabel.getText());
                item.setCategory(categoryLabel.getText());
                item.setBrand(brandLabel.getText());
                item.setItemsize(newValue);
                item.setUnit(unitLabel.getText());
                item.setColor(colorLabel.getText());
                item.setType(typeLabel.getText());
                item.setDescription(descLabel.getText());

                popup.close();
            }

            okClicked = true;
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Warning: Input integers.");
            alert.show();
        }
    }

    public void handleSearch(){
        try{
            Item item = new Item();
            item.setSKU(enterSKU.getText());

            row = ItemController.seleectSQL(enterSKU.getText());

            String value1 = String.valueOf(row.get(1));
            String value2 = String.valueOf(row.get(2));
            String value3 = String.valueOf(row.get(3));
            String value4 = String.valueOf(row.get(4));
            String value5 = String.valueOf(row.get(5));
            String value6 = String.valueOf(row.get(6));
            String value7 = String.valueOf(row.get(7));
            String value8 = String.valueOf(row.get(8));

            itemLabel.setText(value1);
            categoryLabel.setText(value2);
            brandLabel.setText(value3);
            itemSizeLabel.setText(value4);
            unitLabel.setText(value5);
            colorLabel.setText(value6);
            typeLabel.setText(value7);
            descLabel.setText(value8);
        }catch(IndexOutOfBoundsException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Warning: Invalid SKU-Code.");
            alert.show();
        }
    }

    public void handleCancel(){popup.close();}
}
