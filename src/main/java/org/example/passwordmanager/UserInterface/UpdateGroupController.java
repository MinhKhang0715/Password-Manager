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

public class UpdateGroupController {

    @FXML public TextField txt_groupName;
    @FXML public PasswordField txt_groupPassword;
    @FXML public Label txt_emptyName;
    @FXML public CheckBox chk_changePassword;

    private final GroupService groupService;
    private GroupDTO oldGroupDTO;

    public UpdateGroupController() {
        groupService = MainPageController.getGroupService();
    }

    public void showData(String groupName) {
        oldGroupDTO = groupService.searchGroupByName(groupName);
        txt_groupName.setText(groupName);
    }

    @FXML
    public void initialize() {
        chk_changePassword.setOnAction(actionEvent -> {
            if (chk_changePassword.isSelected()) {
                chk_changePassword.setSelected(true);
                txt_groupPassword.setEditable(true);
            }
            else {
                chk_changePassword.setSelected(false);
                txt_groupPassword.clear();
                txt_groupPassword.setEditable(false);
            }
        });
    }

    @FXML
    public void onUpdateButtonClick(ActionEvent actionEvent) throws IOException, NoSuchAlgorithmException {
        if (txt_groupName.getText().equals(""))
            txt_emptyName.setText("Group name cannot be left empty");
        else {
            GroupDTO newGroup = new GroupDTO().setGroupName(txt_groupName.getText());
            if (chk_changePassword.isSelected()) {
                if (txt_groupPassword.getText().equals(""))
                    txt_emptyName.setText("Please enter your password for the group");
                else {
                    newGroup = newGroup.setGroupPassword(Hashing.SHA256(txt_groupPassword.getText()));
                    groupService.updateGroup(oldGroupDTO, newGroup);
                }
            }
            else {
                newGroup.setGroupPassword(oldGroupDTO.getGroupPassword());
                groupService.updateGroup(oldGroupDTO, newGroup);
            }
        }
        if (PasswordManager.isSignup) {
            SignupController.getMainPageController().updateGroupList();
            SignupController.getMainPageController().lbl_groupName.setText(txt_groupName.getText());
        }
        else {
            LoginController.getMainPageController().updateGroupList();
            LoginController.getMainPageController().lbl_groupName.setText(txt_groupName.getText());
        }
        (((Node) actionEvent.getSource())).getScene().getWindow().hide();
    }

    @FXML
    public void onCancelButtonClick(ActionEvent actionEvent) {
        (((Node) actionEvent.getSource())).getScene().getWindow().hide();
    }
}
