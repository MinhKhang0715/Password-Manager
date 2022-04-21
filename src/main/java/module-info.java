module org.example.passwordmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.json;
    requires java.datatransfer;
    requires java.desktop;

    opens org.example.passwordmanager to javafx.fxml;
    exports org.example.passwordmanager;
    exports org.example.passwordmanager.UserInterface;
    opens org.example.passwordmanager.UserInterface to javafx.fxml;
}