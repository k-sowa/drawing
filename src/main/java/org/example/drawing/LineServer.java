package org.example.drawing;

import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class LineServer {

    private final DrawingController drawingController;

    public LineServer(DrawingController drawingController) {
        this.drawingController = drawingController;
    }

    // Uruchomienie serwera nasłuchującego na porcie 5000
    public void startServer() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(6123)) {
                while (true) {
                    Socket clientSocket = serverSocket.accept(); // Akceptowanie nowych połączeń
                    new Thread(new ClientHandler(clientSocket)).start(); // Obsługa każdego klienta w oddzielnym wątku
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Klasa obsługująca każdego klienta
    private class ClientHandler implements Runnable {
        private Socket socket;
        private String currentColor = "#000000"; // Domyślny kolor - czarny

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String message;
                while ((message = reader.readLine()) != null) {
                    if (message.matches("[0-9A-Fa-f]{6}")) { // Jeśli wiadomość to sześciocyfrowy kolor
                        currentColor = "#" + message; // Zapisujemy kolor
                    } else if (message.matches("[-+]?[0-9]*\\.?[0-9]+ [-+]?[0-9]*\\.?[0-9]+ [-+]?[0-9]*\\.?[0-9]+ [-+]?[0-9]*\\.?[0-9]+")) {
                        // Otrzymanie czterech liczb (x1, y1, x2, x2)
                        String[] parts = message.split(" ");
                        double x1 = Double.parseDouble(parts[0]);
                        double y1 = Double.parseDouble(parts[1]);
                        double x2 = Double.parseDouble(parts[2]);
                        double y2 = Double.parseDouble(parts[3]);

                        // Rysowanie linii w wątku JavaFX
                        Platform.runLater(() -> drawingController.drawLine(x1, y1, x2, y2, currentColor));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}