package mp.ims.mpgroup4.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.sql.*;
import java.util.Optional;

/**
 * Controller class for handling export functionality in the application.
 *
 * @author Arianne Acosta
 * @author Joy Arellano
 * @author Clark Rodriguez
 */
public class ExportScreenController {
    @FXML
    private TextField enterPath;
    private Stage popup;
    public boolean okClicked = false;
    private String pathString;


    /**
     * Sets the popup stage for the controller.
     *
     * @param popup The stage for the popup window.
     */
    public void setPopup(Stage popup){this.popup = popup;}

    /**
     * Handles the OK button click event to export data to a CSV file.
     *
     * @throws FileNotFoundException if the specified file path is invalid.
     */
    public void handleOK() throws FileNotFoundException{
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            pathString = enterPath.getText();

            File outfile = new File(pathString);
            if (outfile.exists()) {
                boolean userConfirmed = handleExistingFiles();
                if (!userConfirmed) {
                    return;
                }
            }
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Ratatouille?user=root&password=$Qlbench3r20");
            String query = "SELECT * FROM Item";
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            FileWriter writer = new FileWriter(pathString);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            while (resultSet.next()) {
                bufferedWriter.write(String.format("%s,%s,%s,%s,%d,%s,%s,%s,%s",
                        resultSet.getString("SKU"),
                        resultSet.getString("Item"),
                        resultSet.getString("Category"),
                        resultSet.getString("Brand"),
                        resultSet.getInt("Amount"),
                        resultSet.getString("Unit"),
                        resultSet.getString("Color"),
                        resultSet.getString("Type"),
                        resultSet.getString("Description")));
                bufferedWriter.newLine();
            }

            bufferedWriter.close();
            writer.close();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
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
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        okClicked = true;
        popup.close();
    }

    /**
     * Displays a confirmation dialog to confirm overwriting an existing file.
     *
     * @return true if the user confirms overwriting, false otherwise.
     */
    private boolean handleExistingFiles() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setContentText("The file already exists. Do you want to overwrite it?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Handles the Cancel button click event to close the export popup.
     */
    public void handleCancel(){popup.close();}
}
