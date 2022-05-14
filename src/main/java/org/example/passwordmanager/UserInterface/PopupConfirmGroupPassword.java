package org.example.passwordmanager.UserInterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import org.example.passwordmanager.Crypto.Hashing;
import org.example.passwordmanager.GroupPasswords.GroupDTO;
import org.example.passwordmanager.GroupPasswords.GroupService;
import org.example.passwordmanager.PasswordManager;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class PopupConfirmGroupPassword {

    @FXML public PasswordField txt_password;
    @FXML public Label lbl_wrongPassword;

    private String groupName;
    private final GroupService groupService;
    private final MainPageController controller;

    public PopupConfirmGroupPassword() {
        groupService = MainPageController.getGroupService();
        if (PasswordManager.isSignup)
            controller = SignupController.getMainPageController();
        else
            controller = LoginController.getMainPageController();
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @FXML
    public void onConfirmButtonClick(ActionEvent actionEvent) throws NoSuchAlgorithmException, IOException {
        GroupDTO groupDTO = groupService.searchGroupByName(groupName);
        if (!Hashing.SHA256(txt_password.getText()).equals(groupDTO.getGroupPassword()))
            lbl_wrongPassword.setText("Wrong password!");
        else {
            controller.lbl_groupName.setText(groupName);
            controller.updateItemList(groupName);
            controller.btn_createPassword.setDisable(false);
            controller.btn_deleteGroup.setDisable(false);
            controller.btn_updateGroup.setDisable(false);
            (((Node) actionEvent.getSource())).getScene().getWindow().hide();
        }
    }

    @FXML
    public void onCancelButtonClick(ActionEvent actionEvent) {
        (((Node) actionEvent.getSource())).getScene().getWindow().hide();
    }
}
