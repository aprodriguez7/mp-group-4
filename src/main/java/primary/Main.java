package primary;

import drivers.Item;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.text.Font;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import mp.ims.mpgroup4.controllers.*;

import java.awt.*;
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

    /**
     * Default constructor for Main class.
     */
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
     * The start method of the JavaFX application. This method is automatically called when the
     * JavaFX application is launched. It initializes the main stage, loads the WelcomeScreen.fxml file,
     * sets up the necessary components, and displays the primary stage for the Gusto's Inventory Management System.
     *
     * @param primaryStage The primary stage for the JavaFX application.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    @Override
    public void start(Stage primaryStage) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mp/ims/mpgroup4/WelcomeScreen.fxml"));
        Parent root = loader.load();
        Font.loadFont(getClass().getResourceAsStream("mp/ims/mpgroup4/assets/BOLDSTROM D.OTF"), 14);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setTitle("Gusto's Inventory Management System");
        //PIN: Graphics!!!
        Image appicon = new Image(getClass().getResourceAsStream("/imageassets/fat-rat.png"));
        primaryStage.getIcons().add(appicon);

        ItemController itemcontrol = loader.getController();
        itemcontrol.setMain(this);
    }

    /**
     * Displays the Add Screen for entering a new item. This method loads the AddScreen.fxml file,
     * sets up the necessary components, and shows a popup window. The method waits for user input
     * before returning the status of the operation.
     *
     * @param item The item to be added.
     * @return True if the user clicked 'OK' to confirm, false otherwise.
     * @throws IOException If an error occurs while loading the FXML file.
     */
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
        Image appicon = new Image(getClass().getResourceAsStream("/imageassets/fat-rat.png"));
        popup.getIcons().add(appicon);
        popup.showAndWait();
        popup.setResizable(false);

        return control.okClicked;
    }

    /**
     * Displays the Update Screen for modifying an existing item. This method loads the UpdateScreen.fxml file,
     * sets up the necessary components, and shows a popup window. The method waits for user input
     * before returning the status of the operation.
     *
     * @param item The item to be updated.
     * @return True if the user clicked 'OK' to confirm, false otherwise.
     * @throws IOException If an error occurs while loading the FXML file.
     */
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
        Image appicon = new Image(getClass().getResourceAsStream("/imageassets/easteregg.png"));
        popup.getIcons().add(appicon);
        popup.showAndWait();
        popup.setResizable(false);

        return control.okClicked;
    }

    /**
     * Displays the Restock Screen for updating the stock quantity of an item. This method loads the RestockScreen.fxml file,
     * sets up the necessary components, and shows a popup window. The method waits for user input
     * before returning the status of the operation.
     *
     * @param item The item to be restocked.
     * @return True if the user clicked 'OK' to confirm, false otherwise.
     * @throws IOException If an error occurs while loading the FXML file.
     */
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
        Image appicon = new Image(getClass().getResourceAsStream("/imageassets/fat-rat.png"));
        popup.getIcons().add(appicon);
        popup.showAndWait();
        popup.setResizable(false);

        return control.okClicked;
    }

    /**
     * Displays the Item Usage Screen for viewing the usage details of an item. This method loads the ItemUsageScreen.fxml file,
     * sets up the necessary components, and shows a popup window. The method waits for user input
     * before returning the status of the operation.
     *
     * @param item The item to view usage details for.
     * @return True if the user clicked 'OK' to confirm, false otherwise.
     * @throws IOException If an error occurs while loading the FXML file.
     */
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
        Image appicon = new Image(getClass().getResourceAsStream("/imageassets/fat-rat.png"));
        popup.getIcons().add(appicon);
        popup.showAndWait();
        popup.setResizable(false);

        return control.okClicked;
    }

    /**
     * Displays the Import Screen for importing items from a .csv file. This method loads the ImportScreen.fxml file,
     * sets up the necessary components, and shows a popup window. The method waits for user input
     * before returning the status of the operation.
     *
     * @param item The Item to be imported
     * @return True if the user clicked 'OK' to confirm, false otherwise.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    public boolean showImportScreen(Item item) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mp/ims/mpgroup4/ims-view/ImportScreen.fxml"));
        Parent root = loader.load();

        Stage popup = new Stage();
        Scene scene = new Scene(root);

        ImportScreenController control = loader.getController();
        control.setPopup(popup);

        popup.setScene(scene);
        popup.setTitle("Bulk Item Import via .csv");
        Image appicon = new Image(getClass().getResourceAsStream("/imageassets/fat-rat.png"));
        popup.getIcons().add(appicon);
        popup.showAndWait();
        popup.setResizable(false);

        return control.okClicked;
    }

    public boolean showExportScreen() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mp/ims/mpgroup4/ims-view/ExportScreen.fxml"));
        Parent root = loader.load();

        Stage popup = new Stage();
        Scene scene = new Scene(root);

        ExportScreenController control = loader.getController();
        control.setPopup(popup);

        popup.setScene(scene);
        popup.setTitle("Export Current Inventory as .csv File");
        Image appicon = new Image(getClass().getResourceAsStream("/imageassets/fat-rat.png"));
        popup.getIcons().add(appicon);
        popup.showAndWait();
        popup.setResizable(false);

        return control.okClicked;
    }
}
