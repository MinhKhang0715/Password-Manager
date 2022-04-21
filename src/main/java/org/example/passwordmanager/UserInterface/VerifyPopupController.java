package org.example.passwordmanager.UserInterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import org.example.passwordmanager.Password.PasswordDTO;
import org.example.passwordmanager.Password.PasswordService;
import org.example.passwordmanager.PasswordManager;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class VerifyPopupController {
    private int id;

    PasswordService passwordService = new PasswordService();

    public VerifyPopupController() throws NoSuchPaddingException, NoSuchAlgorithmException, IOException {
    }

    public void setId(int id) {
        this.id = id;
    }

    @FXML
    public void onConfirmButtonClick(ActionEvent actionEvent) {
        PasswordDTO passwordDTO = passwordService.searchPasswordById(this.id);
        passwordService.delete(passwordDTO);
        try {
            if (PasswordManager.isSignup)
                SignupController.getMainPageController().updateItemList();
            else
                LoginController.getMainPageController().updateItemList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        (((Node) actionEvent.getSource())).getScene().getWindow().hide();
    }

    @FXML
    public void onCancelButtonClick(ActionEvent actionEvent) {
        (((Node) actionEvent.getSource())).getScene().getWindow().hide();
    }
}
