package org.example.passwordmanager.UserInterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.passwordmanager.Password.PasswordDTO;
import org.example.passwordmanager.Password.PasswordService;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class ItemController {
    @FXML public Label lbl_idPass;
    @FXML public Button btn_copy;
    @FXML public Button btn_update;
    @FXML public Button btn_delete;
    @FXML public Label lbl_dateCreated;
    @FXML public Label lbl_serviceName;
    @FXML public TextField txt_description;
    @FXML public HBox item;

    PasswordService passwordService;

    @FXML
    public void initialize() throws NoSuchPaddingException, NoSuchAlgorithmException, IOException {
        passwordService = new PasswordService();
    }

    public void onCopyButtonClick(ActionEvent actionEvent) {
        try {
            passwordService = new PasswordService();
            PasswordDTO passwordDTO = passwordService.searchPasswordById(Integer.parseInt(lbl_idPass.getText()));
            passwordService.copyToClipboard(passwordDTO);
        } catch (NoSuchPaddingException |
                NoSuchAlgorithmException |
                IOException e) {
            e.printStackTrace();
        }
    }

    void updateItem(int id) {
        PasswordDTO passwordDTO = passwordService.searchPasswordById(id);
        txt_description.setText(passwordDTO.getDescription());
        lbl_serviceName.setText(passwordDTO.getName());
        lbl_dateCreated.setText(passwordDTO.getDateCreated().toString());
    }

    public void onUpdateButtonClick(ActionEvent actionEvent) throws IOException {
        AnchorPane root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/passwordmanager/update-pass.fxml")));
        Stage stage = new Stage();
        stage.setTitle(lbl_idPass.getText());
        stage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
        stage.show();
        stage.setOnHidden(windowEvent -> updateItem(Integer.parseInt(lbl_idPass.getText())));
    }

    public void onDeleteButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/passwordmanager/verify-popup.fxml"));
        AnchorPane root = loader.load();
        VerifyPopupController controller = loader.getController();
        controller.setId(Integer.parseInt(lbl_idPass.getText()));
        Stage stage = new Stage();
        stage.setTitle("Confirm delete password");
        stage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
        stage.show();
    }
}
