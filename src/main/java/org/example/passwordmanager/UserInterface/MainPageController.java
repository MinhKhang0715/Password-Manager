package org.example.passwordmanager.UserInterface;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.passwordmanager.GroupPasswords.GroupDTO;
import org.example.passwordmanager.GroupPasswords.GroupService;
import org.example.passwordmanager.Password.PasswordDTO;
import org.example.passwordmanager.Password.PasswordService;
import org.example.passwordmanager.PasswordManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class MainPageController {
    @FXML public VBox pnlPassItems;
    @FXML public VBox pnlGroupItems;
    @FXML public Label lbl_groupName;
    @FXML public Button btn_createPassword;
    @FXML public Button btn_updateGroup;
    @FXML public Button btn_deleteGroup;
    static PasswordService passwordService;
    private static GroupService groupService;

    @FXML
    public void onCreateGroupButtonClick() throws IOException{
        AnchorPane root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/passwordmanager/create-group.fxml")));
        Stage stage = new Stage();
        stage.setTitle("Create group");
        stage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
        stage.show();
    }

    @FXML
    public void onCreatePasswordButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/example/passwordmanager/create-password.fxml")));
        AnchorPane root = fxmlLoader.load();
        CreatePasswordController controller = fxmlLoader.getController();
        controller.setGroupName(lbl_groupName.getText());
        Stage stage = new Stage();
        stage.setTitle("Create password");
        stage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
        stage.show();
    }

    @FXML
    public void initialize() throws IOException {
        populateGroupList();

    }

    @FXML
    public void onUpdateGroupButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/example/passwordmanager/update-group.fxml")));
        AnchorPane root = fxmlLoader.load();
        UpdateGroupController controller = fxmlLoader.getController();
        controller.showData(lbl_groupName.getText());
        Stage stage = new Stage();
        stage.setTitle("Update group " + lbl_groupName.getText());
        stage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
        stage.show();
    }

    @FXML
    public void onDeleteGroupButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/example/passwordmanager/popup-delete-group.fxml")));
        AnchorPane root = fxmlLoader.load();
        PopupDeleteGroup controller = fxmlLoader.getController();
        controller.setGroupName(lbl_groupName.getText());
        Stage stage = new Stage();
        stage.setTitle("Delete group " + lbl_groupName.getText());
        stage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
        stage.show();
    }

    static GroupService getGroupService() {
        return groupService;
    }

    public String getGroupName() {
        return lbl_groupName.getText();
    }

    public MainPageController() throws IOException {
        if (PasswordManager.isSignup)
            passwordService = SignupController.getPasswordService();
        else
            passwordService = LoginController.getPasswordService();
        groupService = new GroupService();
    }

    public void populateItemList(String groupName) throws IOException {
        ArrayList<PasswordDTO> passwordDTOS = passwordService.readAllFromGroup(groupName);
        HBox[] nodes = new HBox[passwordDTOS.size()];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/passwordmanager/password-item.fxml")));
            for (Node nodeIn : nodes[i].getChildren()) {
                if (nodeIn instanceof Label) {
                    if (nodeIn.getId().equals("id-pass"))
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

    public void populateGroupList() throws IOException {
        ArrayList<GroupDTO> groupDTOArrayList = groupService.readAllGroup();
        Button[] nodes = new Button[groupDTOArrayList.size()];
        for (int i = 0; i < nodes.length; i++) {
            String groupName = groupDTOArrayList.get(i).getGroupName();
            nodes[i] = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/passwordmanager/group-item.fxml")));
            nodes[i].setText(groupName);
            nodes[i].setOnAction(actionEvent -> {
                try {
                    lbl_groupName.setText(groupName);
                    updateItemList(groupName);
                    btn_createPassword.setDisable(false);
                    btn_deleteGroup.setDisable(false);
                    btn_updateGroup.setDisable(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            pnlGroupItems.getChildren().add(nodes[i]);
        }
    }

    public void updateGroupList() throws IOException {
        ObservableList<Node> observableList = pnlGroupItems.getChildren();
        pnlGroupItems.getChildren().removeAll(observableList);
        populateGroupList();
    }

    public void updateItemList(String groupName) throws IOException {
        ObservableList<Node> observableList = pnlPassItems.getChildren();
        pnlPassItems.getChildren().removeAll(observableList);
        populateItemList(groupName);
    }
}
