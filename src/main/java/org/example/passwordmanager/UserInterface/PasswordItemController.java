package org.example.passwordmanager.UserInterface;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.passwordmanager.Password.PasswordDTO;
import org.example.passwordmanager.Password.PasswordService;
import org.example.passwordmanager.PasswordManager;

import java.io.IOException;
import java.util.Objects;

public class PasswordItemController {
    @FXML public Label lbl_idPass;

    private final String groupName;
    MainPageController mainPageController;
    PasswordService passwordService;

    public PasswordItemController() {
        if (PasswordManager.isSignup) {
            mainPageController = SignupController.getMainPageController();
            passwordService = SignupController.getPasswordService();
        }
        else {
            mainPageController = LoginController.getMainPageController();
            passwordService = LoginController.getPasswordService();
        }
        groupName = mainPageController.getGroupName();
    }

    @FXML
    public void onDeleteButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/passwordmanager/popup-delete-password.fxml"));
        AnchorPane root = fxmlLoader.load();
        PopupDeletePassword controller = fxmlLoader.getController();
        controller.setGroupName(this.groupName);
        controller.setPasswordId(Integer.parseInt(lbl_idPass.getText()));
        Stage stage = new Stage();
        stage.setTitle("Delete password");
        stage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
        stage.show();
    }

    @FXML
    public void onUpdateButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/example/passwordmanager/update-pass.fxml")));
            AnchorPane root = fxmlLoader.load();
            UpdatePassController controller = fxmlLoader.getController();
            controller.showData(this.groupName, Integer.parseInt(lbl_idPass.getText()));
            Stage stage = new Stage();
            stage.setTitle("Update password no " + lbl_idPass.getText());
            stage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onCopyButtonClick() {
        PasswordDTO passwordDTO = passwordService.searchPasswordByIdFromGroup(Integer.parseInt(lbl_idPass.getText()), this.groupName);
        passwordService.copyToClipboard(passwordDTO);
    }
}
