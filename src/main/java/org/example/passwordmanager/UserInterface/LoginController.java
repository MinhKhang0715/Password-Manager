package org.example.passwordmanager.UserInterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.passwordmanager.Crypto.Hashing;
import org.example.passwordmanager.Database.DBConfig;
import org.example.passwordmanager.Password.PasswordDTO;
import org.example.passwordmanager.Password.PasswordService;

import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class LoginController {
    @FXML public TextField txt_password;
    @FXML public Label lbl_text;
    PasswordService passwordService;
    String passwordHash;
    static MainPageController controller;
    public static MainPageController getMainPageController() {
        return controller;
    }

    @FXML
    public void initialize() throws NoSuchPaddingException, NoSuchAlgorithmException, IOException {
        passwordService = new PasswordService();
        PasswordDTO passwordDTO = passwordService.searchPasswordById(0);
        if (passwordDTO == null) {
            VBox node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/passwordmanager/login-error.fxml")));
            for (Node nodeIn : node.getChildren()) {
                if (nodeIn instanceof Label)
                    ((Label) nodeIn).setText("Master password not found" + "\nCreating new DB file");
                if (nodeIn instanceof Button)
                    nodeIn.setOnMouseClicked(MouseEvent -> {
                        File file = new File(DBConfig.pathInLinux);
                        boolean isDeleted = file.delete();
                        if (!isDeleted) System.out.println("ERROR (Login Controller): Cannot delete file");
                        try {
                            VBox signupPage = FXMLLoader.load(Objects.requireNonNull(LoginController.class.getResource("/org/example/passwordmanager/signup.fxml")));
                            Stage stage = new Stage();
                            stage.setTitle("Sign up");
                            stage.setScene(new Scene(signupPage, signupPage.getPrefWidth(), signupPage.getPrefHeight()));
                            stage.show();
                            (((Node) MouseEvent.getSource())).getScene().getWindow().hide();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            }
        } else this.passwordHash = passwordDTO.getValue();
    }

    public void onLoginButtonClick(ActionEvent actionEvent) throws NoSuchAlgorithmException, IOException {
        String password = txt_password.getText();
        System.out.println("Hashed: " + Hashing.SHA256(password));
        if (Hashing.SHA256(password).equals(this.passwordHash)) {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(LoginController.class.getResource("/org/example/passwordmanager/main-page.fxml")));
            AnchorPane mainPage = loader.load();
            controller = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Main page");
            stage.setScene(new Scene(mainPage, mainPage.getPrefWidth(), mainPage.getPrefHeight()));
            stage.show();
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
        } else {
            lbl_text.setText("Wrong password!");
        }
    }
}

