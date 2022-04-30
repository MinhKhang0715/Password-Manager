package org.example.passwordmanager.UserInterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import org.example.passwordmanager.Password.PasswordDTO;
import org.example.passwordmanager.Password.PasswordService;
import org.example.passwordmanager.PasswordManager;

import java.io.IOException;

public class PopupDeletePassword {

    private int passwordId;
    private String groupName;
    MainPageController mainPageController;
    PasswordService passwordService;

    public PopupDeletePassword() {
        if (PasswordManager.isSignup) {
            mainPageController = SignupController.getMainPageController();
            passwordService = SignupController.getPasswordService();
        }
        else {
            mainPageController = LoginController.getMainPageController();
            passwordService = LoginController.getPasswordService();
        }
    }

    public void setPasswordId(int passwordId) {
        this.passwordId = passwordId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @FXML
    public void onConfirmButtonClick(ActionEvent actionEvent) throws IOException {
        PasswordDTO passwordDTO = passwordService.searchPasswordByIdFromGroup(passwordId, groupName);
        passwordService.delete(passwordDTO, groupName);
        mainPageController.updateItemList(groupName);
        (((Node) actionEvent.getSource())).getScene().getWindow().hide();
    }

    @FXML
    public void onCancelButtonClick(ActionEvent actionEvent) {
        (((Node) actionEvent.getSource())).getScene().getWindow().hide();
    }
}
