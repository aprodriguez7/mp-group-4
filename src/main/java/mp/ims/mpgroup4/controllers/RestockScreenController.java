package mp.ims.mpgroup4.controllers;

import drivers.Item;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.InputMismatchException;

public class RestockScreenController {
    @FXML
    private TextField enterSKU;
    @FXML
    private TextField enterAmount;
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
    private int amountAdd;
    private int formeramount;
    private int newamount;

    public void setPopup(Stage popup){this.popup = popup;}

    public void setItem(Item item){this.item = item;}

    public void handleOK(){
        try{
            amountAdd = Integer.parseInt(enterAmount.getText());
            formeramount = Integer.parseInt(itemSizeLabel.getText());
            newamount = amountAdd + formeramount;

            if (amountAdd < 0){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Enter positive values.");
                alert.show();
            }else {
                item.setSKU(enterSKU.getText());
                item.setItem(itemLabel.getText());
                item.setCategory(categoryLabel.getText());
                item.setBrand(brandLabel.getText());
                item.setItemsize(newamount);
                item.setUnit(unitLabel.getText());
                item.setColor(colorLabel.getText());
                item.setType(typeLabel.getText());
                item.setDescription(descLabel.getText());

                popup.close();
            }

            okClicked = true;
        }catch(InputMismatchException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Enter numerical values only.");
            alert.show();
        }catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Input an integer");
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
        }catch (IndexOutOfBoundsException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Enter valid SKU code.");
            alert.show();
        }
    }

    public void handleCancel() {popup.close();}
}
