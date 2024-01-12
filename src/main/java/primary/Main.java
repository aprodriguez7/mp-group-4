package primary;

import drivers.Item;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import mp.ims.mpgroup4.controllers.*;

import java.io.IOException;

/**
 * Final class for the Inventory Management System
 * <p>
 *     Contains the culmination of the project's classes and internal systems.
 *     Initializes the welcome screen for the user to interact with and utilize
 *     for their usage of the program.
 * </p>
 *
 * @author Arianne Acosta
 * @author Joy Arellano
 * @author Clark Rodriguez
 */
public class Main extends Application {

    private ObservableList<Item> itemData;

    public Main(){
        itemData = ItemController.loadItems();
    }
    public ObservableList<Item> getItemData() {
        return itemData;
    }

    /**
     * Main method of the system
     * <p>
     *     detailed descriptor
     * </p>
     * @param args Used to launch scene for JFX GUI
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Method overridden to setup stage for IMS program.
     * <p>
     *     detailed descriptor
     * </p>
     * @param primaryStage Final stage for system
     */
    @Override
    public void start(Stage primaryStage) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mp/ims/mpgroup4/WelcomeScreen.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setTitle("Gusto's Inventory Management System");
        //PIN: Graphics!!!
        //primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("imageassets/fat rat.png")));

        ItemController itemcontrol = loader.getController();
        itemcontrol.setMain(this);
    }

    public boolean showAddScreen(Item item) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mp/ims/mpgroup4/ims-view/AddScreen.fxml"));
        Parent root = loader.load();

        Stage popup = new Stage();
        Scene scene = new Scene(root);

        AddScreenController control = loader.getController();
        control.setPopup(popup);
        control.setItem(item);

        popup.setScene(scene);
        popup.setTitle("Add Item Entry");
        //PIN: INSERT GRAPHICS
        //popup.getIcons().add(new Image(Main.class.getResourceAsStream("/imageassets/fat-rat.png")));
        popup.showAndWait();
        popup.setResizable(false);

        return control.okClicked;
    }

    public boolean showUpdateScreen(Item item) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mp/ims/mpgroup4/ims-view/UpdateScreen.fxml"));
        Parent root = loader.load();

        Stage popup = new Stage();
        Scene scene = new Scene(root);

        UpdateScreenController control = loader.getController();
        control.setPopup(popup);
        control.setItem(item);

        popup.setScene(scene);
        popup.setTitle("Update Existing Item Entry");
        //PIN: Graphics!!!
        //popup.getIcons().add(new Image(Main.class.getResourceAsStream("src/main/resources/imageassets/fat rat.png")));
        popup.showAndWait();
        popup.setResizable(false);

        return control.okClicked;
    }

    public boolean showRestockScreen(Item item) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mp/ims/mpgroup4/ims-view/RestockScreen.fxml"));
        Parent root = loader.load();

        Stage popup = new Stage();
        Scene scene = new Scene(root);

        RestockScreenController control = loader.getController();
        control.setPopup(popup);
        control.setItem(item);

        popup.setScene(scene);
        popup.setTitle("Restock Item Entry");
        //PIN: Graphics!!!
        //popup.getIcons().add(new Image(Main.class.getResourceAsStream("src/main/resources/imageassets/fat rat.png")));
        popup.showAndWait();
        popup.setResizable(false);

        return control.okClicked;
    }

    public boolean showItemUsageScreen(Item item) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mp/ims/mpgroup4/ims-view/ItemUsageScreen.fxml"));
        Parent root = loader.load();

        Stage popup = new Stage();
        Scene scene = new Scene(root);

        ItemUsageScreenController control = loader.getController();
        control.setPopup(popup);
        control.setItem(item);

        popup.setScene(scene);
        popup.setTitle("Item Usage");
        //PIN: Graphics!!!
        //popup.getIcons().add(new Image(Main.class.getResourceAsStream("src/main/resources/imageassets/fat rat.png")));
        popup.showAndWait();
        popup.setResizable(false);

        return control.okClicked;
    }

    public boolean showImportScreen(Item item) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mp/ims/mpgroup4/ims-view/ItemUsageScreen.fxml"));
        Parent root = loader.load();

        Stage popup = new Stage();
        Scene scene = new Scene(root);

        ImportScreenController control = loader.getController();
        control.setPopup(popup);

        popup.setScene(scene);
        popup.setTitle("Bulk Item Import via .csv");
        //PIN: Graphics!!!
        //popup.getIcons().add(new Image(Main.class.getResourceAsStream("src/main/resources/imageassets/fat rat.png")));
        popup.showAndWait();
        popup.setResizable(false);

        return control.okClicked;
    }

    public void pdf(){
        getHostServices().showDocument(getClass().getResource("pdf").toString());
    }
}
