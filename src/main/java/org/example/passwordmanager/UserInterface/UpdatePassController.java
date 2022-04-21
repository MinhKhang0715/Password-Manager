package org.example.passwordmanager.UserInterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.passwordmanager.Password.PasswordDTO;
import org.example.passwordmanager.Password.PasswordService;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

public class UpdatePassController {
    @FXML
    public TextField txt_serviceName;
    @FXML
    public PasswordField txt_password;
    @FXML
    public TextField txt_description;

    PasswordService passwordService = new PasswordService();

    public UpdatePassController() throws NoSuchPaddingException, NoSuchAlgorithmException, IOException {
    }

    public void onUpdateButtonClick(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        int id = Integer.parseInt(stage.getTitle());
        PasswordDTO passwordDTO = new PasswordDTO()
                .setId(id)
                .setName(txt_serviceName.getText())
                .setDescription(txt_description.getText())
                .setValue(txt_password.getText())
                .setDateCreated(LocalDate.now());
        passwordService.update(passwordDTO);
        stage.hide();
    }

    public void onCancelButtonClick(ActionEvent actionEvent) {
        (((Node) actionEvent.getSource())).getScene().getWindow().hide();
    }
}
