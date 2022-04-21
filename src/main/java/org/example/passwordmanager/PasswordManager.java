package org.example.passwordmanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.passwordmanager.Database.DBConfig;

import java.io.File;
import java.io.IOException;

public class PasswordManager extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        if (DBConfig.OS_NAME.equals("Linux"))
            runInLinux(stage);
        else if (DBConfig.OS_NAME.contains("Windows"))
            runInWindows(stage);
    }

    static void runInLinux(Stage stage1) throws IOException {
        File dbFile = new File(DBConfig.pathInLinux);
        if (!dbFile.exists()) {
            FXMLLoader fxmlLoader = new FXMLLoader(PasswordManager.class.getResource("signup.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 352, 110);
            stage1.setTitle("Signup");
            stage1.setScene(scene);
            stage1.show();
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(PasswordManager.class.getResource("login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 352, 110);
            stage1.setTitle("Login");
            stage1.setScene(scene);
            stage1.show();
        }
    }

    static void runInWindows(Stage stage2) throws IOException {
        File dbFile = new File(DBConfig.pathInWindows);
        if (!dbFile.exists()) {
            FXMLLoader fxmlLoader = new FXMLLoader(PasswordManager.class.getResource("signup.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 352, 110);
            stage2.setTitle("Signup");
            stage2.setScene(scene);
            stage2.show();
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(PasswordManager.class.getResource("login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 352, 110);
            stage2.setTitle("Login");
            stage2.setScene(scene);
            stage2.show();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}