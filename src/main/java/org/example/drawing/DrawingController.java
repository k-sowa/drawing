package org.example.drawing;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

public class DrawingController {

    @FXML
    private Canvas canvas; // Element Canvas do rysowania
    @FXML
    private Label offsetLabel; // Etykieta pokazująca aktualne przesunięcie osi

    private GraphicsContext gc;
    private double offsetX = 0; // Przesunięcie na osi X
    private double offsetY = 0; // Przesunięcie na osi Y

    // Lista przechowująca wszystkie odcinki
    private List<LineSegment> lines = new ArrayList<>();

    @FXML
    public void initialize() {
        gc = canvas.getGraphicsContext2D();
        clearCanvas(); // Wypełnienie kanwy białym kolorem
    }

    // Rysowanie pojedynczego odcinka
    public void drawLine(double x1, double y1, double x2, double y2, String color) {
        gc.setStroke(Color.web(color)); // Ustawienie koloru linii
        gc.strokeLine(x1 + offsetX, y1 + offsetY, x2 + offsetX, y2 + offsetY); // Rysowanie odcinka z uwzględnieniem przesunięcia
        lines.add(new LineSegment(x1, y1, x2, y2, color)); // Dodanie odcinka do listy
    }

    // Obsługa klawiatury do przesuwania układu współrzędnych
    public void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case UP -> offsetY += 10; // Przesunięcie w górę
            case DOWN -> offsetY -= 10; // Przesunięcie w dół
            case LEFT -> offsetX += 10; // Przesunięcie w lewo
            case RIGHT -> offsetX -= 10; // Przesunięcie w prawo
        }
        offsetLabel.setText("Offset: (" + offsetX + ", " + offsetY + ")"); // Aktualizacja etykiety z przesunięciem
        redraw(); // Przerysowanie wszystkich odcinków po przesunięciu
    }

    // Przerysowanie wszystkich odcinków z uwzględnieniem nowego przesunięcia
    private void redraw() {
        clearCanvas(); // Wyczyść kanwę
        for (LineSegment line : lines) {
            drawLine(line.x1, line.y1, line.x2, line.y2, line.color); // Rysowanie każdego odcinka z listy
        }
    }

    // Wypełnienie kanwy białym kolorem
    private void clearCanvas() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    // Klasa reprezentująca odcinek
    private static class LineSegment {
        double x1, y1, x2, y2;
        String color;

        public LineSegment(double x1, double y1, double x2, double y2, String color) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.color = color;
        }
    }
}