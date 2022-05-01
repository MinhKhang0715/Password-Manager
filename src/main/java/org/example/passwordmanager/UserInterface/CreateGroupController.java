package org.example.passwordmanager.UserInterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.passwordmanager.Crypto.Hashing;
import org.example.passwordmanager.GroupPasswords.GroupDTO;
import org.example.passwordmanager.GroupPasswords.GroupService;
import org.example.passwordmanager.PasswordManager;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class CreateGroupController {
    @FXML public CheckBox chk_passwordEnabled;
    @FXML public TextField txt_groupName;
    @FXML public PasswordField txt_groupPassword;
    public Label lbl_empty;

    GroupService groupService;
    MainPageController mainPageController;

    @FXML
    public void initialize() {
        chk_passwordEnabled.setOnAction(actionEvent -> {
            if (chk_passwordEnabled.isSelected()) {
                chk_passwordEnabled.setSelected(true);
                txt_groupPassword.setEditable(true);
            }
            else {
                chk_passwordEnabled.setSelected(false);
                txt_groupPassword.clear();
                txt_groupPassword.setEditable(false);
            }
        });
    }

    @FXML
    public void onCreateButtonClick(ActionEvent actionEvent) throws NoSuchAlgorithmException, IOException {
        if (txt_groupName.getText().equals("")) {
            lbl_empty.setText("Group name cannot left empty");
        }
        else {
            GroupDTO groupDTO = new GroupDTO().setGroupName(txt_groupName.getText());
            if (chk_passwordEnabled.isSelected()) {
                if (txt_groupPassword.getText().equals(""))
                    lbl_empty.setText("Please enter password of the group");
                else {
                    groupDTO = groupDTO.setGroupPassword(Hashing.SHA256(txt_groupPassword.getText()));
                    groupService.createGroup(groupDTO);
                    mainPageController.updateGroupList();
                    (((Node) actionEvent.getSource())).getScene().getWindow().hide();
                }
            }
            else {
                groupDTO = groupDTO.setGroupPassword("");
                groupService.createGroup(groupDTO);
                mainPageController.updateGroupList();
                (((Node) actionEvent.getSource())).getScene().getWindow().hide();
            }
        }
    }

    @FXML
    public void onCancelButtonClick(ActionEvent actionEvent) {
        (((Node) actionEvent.getSource())).getScene().getWindow().hide();
    }

    public CreateGroupController() {
        groupService = MainPageController.getGroupService();
        if (PasswordManager.isSignup)
            mainPageController = SignupController.getMainPageController();
        else
            mainPageController = LoginController.getMainPageController();
    }
}
