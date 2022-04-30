package org.example.passwordmanager.UserInterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import org.example.passwordmanager.GroupPasswords.GroupDTO;
import org.example.passwordmanager.GroupPasswords.GroupService;
import org.example.passwordmanager.PasswordManager;

import java.io.IOException;

public class PopupDeleteGroup {
    private String groupName;
    GroupService groupService;

    public PopupDeleteGroup() {
        groupService = MainPageController.getGroupService();
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @FXML
    public void onDeleteButtonClick(ActionEvent actionEvent) throws IOException {
        GroupDTO groupDTO = groupService.searchGroupByName(groupName);
        groupService.deleteGroup(groupDTO);
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
