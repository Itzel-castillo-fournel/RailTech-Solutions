package fr.irontrail.railtechrh.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fr/irontrail/railtechrh/LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768); // Taille fixe
        stage.setTitle("Railtech RH !");
        stage.setScene(scene);
        // Empêcher le redimensionnement de la fenêtre
        stage.setResizable(false);
        // Centrer la fenêtre sur l'écran
        stage.centerOnScreen();
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}