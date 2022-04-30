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

public class UpdatePassController {
    @FXML public TextField txt_serviceName;
    @FXML public PasswordField txt_password;
    @FXML public TextField txt_description;
    @FXML public Label txt_empty;

    private final PasswordService passwordService;
    private int passwordId;
    private String groupName;

    public UpdatePassController() {
        if (PasswordManager.isSignup)
            passwordService = SignupController.getPasswordService();
        else
            passwordService = LoginController.getPasswordService();
    }

    public void showData(String groupName, int passwordId) {
        this.passwordId = passwordId;
        this.groupName = groupName;
        PasswordDTO passwordDTO = passwordService.searchPasswordByIdFromGroup(this.passwordId, this.groupName);
        txt_serviceName.setText(passwordDTO.getName());
        txt_description.setText(passwordDTO.getDescription());
        txt_password.setText(passwordService.getAesCrypto().decrypt(passwordDTO.getValue()));
    }

    public void onUpdateButtonClick(ActionEvent actionEvent) throws IOException {
        if (txt_password.getText().equals("") || txt_description.getText().equals("") || txt_serviceName.getText().equals(""))
            txt_empty.setText("Please fill all the required fields");
        else {
            PasswordDTO passwordDTO = new PasswordDTO()
                    .setId(this.passwordId)
                    .setName(txt_serviceName.getText())
                    .setDescription(txt_description.getText())
                    .setValue(txt_password.getText())
                    .setDateCreated(LocalDate.now());
            passwordService.update(passwordDTO, this.groupName);
            if (PasswordManager.isSignup)
                SignupController.getMainPageController().updateItemList(this.groupName);
            else
                LoginController.getMainPageController().updateItemList(this.groupName);
            (((Node) actionEvent.getSource())).getScene().getWindow().hide();
        }
    }

    public void onCancelButtonClick(ActionEvent actionEvent) {
        (((Node) actionEvent.getSource())).getScene().getWindow().hide();
    }
}
