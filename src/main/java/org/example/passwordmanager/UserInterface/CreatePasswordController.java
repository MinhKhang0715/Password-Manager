package org.example.passwordmanager.UserInterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.passwordmanager.Password.PasswordDTO;
import org.example.passwordmanager.Password.PasswordService;
import org.example.passwordmanager.PasswordManager;

import java.io.IOException;
import java.time.LocalDate;

public class CreatePasswordController {
    @FXML public TextField txt_serviceName;
    @FXML public PasswordField txt_passField;
    @FXML public TextField txt_description;
    @FXML public Label lbl_emptyField;

    private String groupName;
    PasswordService passwordService;
    MainPageController mainPageController;

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @FXML
    public void onCreatePassClick(ActionEvent actionEvent) throws IOException {
        if (txt_description.getText().equals("") || txt_passField.getText().equals("") || txt_serviceName.getText().equals(""))
            lbl_emptyField.setText("Please fill all the required fields");
        else {
            PasswordDTO passwordDTO = new PasswordDTO()
                    .setValue(txt_passField.getText())
                    .setDateCreated(LocalDate.now())
                    .setDescription(txt_description.getText())
                    .setName(txt_serviceName.getText());
            passwordService.create(passwordDTO, this.groupName);
            mainPageController.updateItemList(this.groupName);
            (((Node) actionEvent.getSource())).getScene().getWindow().hide();
        }
    }

    @FXML
    public void onCancelButtonClick(ActionEvent actionEvent) {
        (((Node) actionEvent.getSource())).getScene().getWindow().hide();
    }

    public CreatePasswordController() {
        if (PasswordManager.isSignup) {
            passwordService = SignupController.getPasswordService();
            mainPageController = SignupController.getMainPageController();
        }
        else {
            passwordService = LoginController.getPasswordService();
            mainPageController = LoginController.getMainPageController();
        }
    }
}
