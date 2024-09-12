package org.example.drawing;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.net.Socket;

public class LineClientFX extends Application {

    private PrintWriter out;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Line Client");

        // Połączenie z serwerem
        try {
            Socket socket = new Socket("localhost", 6123);
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Tworzenie interfejsu użytkownika
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        TextField colorField = new TextField();
        colorField.setPromptText("Enter color (hex)");

        TextField x1Field = new TextField();
        x1Field.setPromptText("x1");

        TextField y1Field = new TextField();
        y1Field.setPromptText("y1");

        TextField x2Field = new TextField();
        x2Field.setPromptText("x2");

        TextField y2Field = new TextField();
        y2Field.setPromptText("y2");

        Button sendColorButton = new Button("Send Color");
        Button sendLineButton = new Button("Send Line");

        grid.add(colorField, 0, 0);
        grid.add(sendColorButton, 1, 0);
        grid.add(x1Field, 0, 1);
        grid.add(y1Field, 1, 1);
        grid.add(x2Field, 0, 2);
        grid.add(y2Field, 1, 2);
        grid.add(sendLineButton, 0, 3, 2, 1);

        sendColorButton.setOnAction(event -> {
            String color = colorField.getText();
            sendColor(color);
        });

        sendLineButton.setOnAction(event -> {
            try {
                double x1 = Double.parseDouble(x1Field.getText());
                double y1 = Double.parseDouble(y1Field.getText());
                double x2 = Double.parseDouble(x2Field.getText());
                double y2 = Double.parseDouble(y2Field.getText());
                sendLine(x1, y1, x2, y2);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void sendColor(String color) {
        if (color.matches("[0-9A-Fa-f]{6}")) {
            out.println(color);
        } else {
            System.err.println("Invalid color format. Use six hexadecimal digits.");
        }
    }

    private void sendLine(double x1, double y1, double x2, double y2) {
        out.printf("%f %f %f %f%n", x1, y1, x2, y2);
    }
}