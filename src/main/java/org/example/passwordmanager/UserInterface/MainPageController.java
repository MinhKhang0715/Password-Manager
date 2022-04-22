package org.example.passwordmanager.UserInterface;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.passwordmanager.Password.PasswordDTO;
import org.example.passwordmanager.Password.PasswordService;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class MainPageController {
    @FXML public VBox pnlPassItems;
    @FXML public TextField txt_serviceName;
    @FXML public TextField txt_description;
    @FXML public Label lbl_emptyField;
    @FXML public PasswordField txt_passField;

    PasswordService passwordService = new PasswordService();

    public MainPageController() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException {
    }

    @FXML
    public void initialize() throws IOException {
        populateItemList();
    }

    private void populateItemList() throws IOException {
        ArrayList<PasswordDTO> passwordDTOS = passwordService.readAll();
        HBox[] nodes = new HBox[passwordDTOS.size()];
        for (int i = 1; i < nodes.length; i++) {
            nodes[i] = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/passwordmanager/items.fxml")));
            for (Node nodeIn : nodes[i].getChildren()) {
                if (nodeIn instanceof Label) {
                    if (nodeIn.getId().equals("idPass"))
                        ((Label) nodeIn).setText(String.valueOf(passwordDTOS.get(i).getId()));
                    if (nodeIn.getId().equals("date-create"))
                        ((Label) nodeIn).setText(passwordDTOS.get(i).getDateCreated().toString());
                    if (nodeIn.getId().equals("service-name"))
                        ((Label) nodeIn).setText(passwordDTOS.get(i).getName());
                    if (nodeIn.getId().equals("update-rec")) {
                        long days = passwordService.datesPass(passwordDTOS.get(i));
                        String message = days > 150 ? "Update!" : "Good";
                        if (days > 150) nodeIn.setStyle("-fx-text-fill: red;");
                        ((Label) nodeIn).setText(message);
                    }
                }
                if (nodeIn instanceof PasswordField)
                    ((PasswordField) nodeIn).setText(passwordDTOS.get(i).getValue());
                if (nodeIn instanceof TextField)
                    ((TextField) nodeIn).setText(passwordDTOS.get(i).getDescription());
            }
            pnlPassItems.getChildren().add(nodes[i]);
        }
    }

    public void updateItemList() throws IOException {
        ObservableList<Node> observableList = pnlPassItems.getChildren();
        pnlPassItems.getChildren().removeAll(observableList);
        populateItemList();
    }

    public void onCreatePassClick(ActionEvent actionEvent) throws IOException {
        if (txt_serviceName.getText().equals("") || txt_passField.getText().equals("") || txt_description.getText().equals(""))
            lbl_emptyField.setText("Please fill all the required field");
        else {
            PasswordDTO passwordDTO = new PasswordDTO()
                    .setValue(txt_passField.getText())
                    .setName(txt_serviceName.getText())
                    .setDescription(txt_description.getText())
                    .setDateCreated(LocalDate.now());
            passwordService.create(passwordDTO);
            ArrayList<PasswordDTO> passwordDTOS = passwordService.readAll();
            int arrSize = passwordDTOS.size();
            HBox node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/passwordmanager/items.fxml")));
            for (Node nodeIn : node.getChildren()) {
                if (nodeIn instanceof Label) {
                    if (nodeIn.getId().equals("idPass"))
                        ((Label) nodeIn).setText(String.valueOf(passwordDTOS.get(arrSize - 1).getId()));
                    if (nodeIn.getId().equals("date-create"))
                        ((Label) nodeIn).setText(passwordDTOS.get(arrSize - 1).getDateCreated().toString());
                    if (nodeIn.getId().equals("service-name"))
                        ((Label) nodeIn).setText(passwordDTOS.get(arrSize - 1).getName());
                    if (nodeIn.getId().equals("update-rec"))
                        ((Label) nodeIn).setText("Good");
                }
                if (nodeIn instanceof PasswordField)
                    ((PasswordField) nodeIn).setText(passwordDTOS.get(arrSize - 1).getValue());
                if (nodeIn instanceof TextField)
                    ((TextField) nodeIn).setText(passwordDTOS.get(arrSize - 1).getDescription());
            }
            pnlPassItems.getChildren().add(node);
        }
    }
}
