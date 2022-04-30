package org.example.passwordmanager.UserInterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.passwordmanager.Crypto.Hashing;
import org.example.passwordmanager.GroupPasswords.GroupDTO;
import org.example.passwordmanager.GroupPasswords.GroupService;
import org.example.passwordmanager.PasswordManager;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class UpdateGroupController {

    @FXML public TextField txt_groupName;
    @FXML public PasswordField txt_groupPassword;
    public Label txt_emptyName;
    GroupService groupService;
    GroupDTO oldGroupDTO;

    public UpdateGroupController() {
        groupService = MainPageController.getGroupService();
    }

    public void showData(String groupName) {
        oldGroupDTO = groupService.searchGroupByName(groupName);
        txt_groupName.setText(groupName);
        txt_groupPassword.setText(oldGroupDTO.getGroupPassword());
    }

    @FXML
    public void onUpdateButtonClick(ActionEvent actionEvent) throws IOException, NoSuchAlgorithmException {
        if (txt_groupName.getText().equals(""))
            txt_emptyName.setText("Group name cannot be left empty");
        else {
            GroupDTO newGroup = new GroupDTO().setGroupName(txt_groupName.getText());
            if (txt_groupPassword.getText().equals("")) {
                newGroup = newGroup.setGroupPassword("");
                groupService.updateGroup(oldGroupDTO, newGroup);
            }
            else {
                newGroup = newGroup.setGroupPassword(Hashing.SHA256(txt_groupPassword.getText()));
                groupService.updateGroup(oldGroupDTO, newGroup);
            }
        }
        if (PasswordManager.isSignup)
            SignupController.getMainPageController().updateGroupList();
        else
            LoginController.getMainPageController().updateGroupList();
        (((Node) actionEvent.getSource())).getScene().getWindow().hide();
    }

    @FXML
    public void onCancelButtonClick(ActionEvent actionEvent) {
        (((Node) actionEvent.getSource())).getScene().getWindow().hide();
    }
}
