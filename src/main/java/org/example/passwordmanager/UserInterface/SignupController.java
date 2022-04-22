package org.example.passwordmanager.UserInterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.passwordmanager.Crypto.Hashing;
import org.example.passwordmanager.Password.PasswordDTO;
import org.example.passwordmanager.Password.PasswordService;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Objects;

public class SignupController {
    @FXML private PasswordField txt_password;

    private static PasswordService passwordService;
    private static MainPageController controller;

    public static MainPageController getMainPageController() {
        return controller;
    }

    static PasswordService getPasswordService() {
        return passwordService;
    }

    @FXML
    protected void onSignupButtonClick(ActionEvent actionEvent) throws NoSuchAlgorithmException, NoSuchPaddingException {
        String password = txt_password.getText();
        System.out.println(password);
        try {
            passwordService = new PasswordService(Hashing.MD5(password));
            PasswordDTO passwordDTO = new PasswordDTO()
                    .setDateCreated(LocalDate.now())
                    .setDescription("Master password")
                    .setName("master")
                    .setValue(Hashing.SHA256(password));
            passwordService.create(passwordDTO);
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(LoginController.class.getResource("/org/example/passwordmanager/main-page.fxml")));
            AnchorPane mainPage = loader.load();
            controller = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Main page");
            stage.setScene(new Scene(mainPage, mainPage.getPrefWidth(), mainPage.getPrefHeight()));
            stage.show();
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}