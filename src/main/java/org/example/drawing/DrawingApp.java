package org.example.drawing;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DrawingApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Załadowanie pliku FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent root = loader.load();

        // Utworzenie sceny o wymiarach 500x500
        Scene scene = new Scene(root, 500, 500);

        // Ustawienie tytułu okna
        primaryStage.setTitle("Drawing App");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Przekazanie zdarzeń klawiatury do kontrolera
        DrawingController controller = loader.getController();
        scene.setOnKeyPressed(controller::handleKeyPress);

        // Uruchomienie serwera
        LineServer server = new LineServer(controller);
        server.startServer();
    }

    public static void main(String[] args) {
        launch(args); // Uruchomienie aplikacji JavaFX
    }
}