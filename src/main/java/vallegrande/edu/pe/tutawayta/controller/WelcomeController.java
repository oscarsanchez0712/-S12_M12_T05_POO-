package vallegrande.edu.pe.tutawayta.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import vallegrande.edu.pe.tutawayta.util.DatabaseConnection;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable {

    @FXML private Label lblConexion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new Thread(() -> {
            boolean ok = DatabaseConnection.testConnection();
            Platform.runLater(() -> {
                if (ok) {
                    lblConexion.setText("⬤  Conectado a MySQL Docker  (localhost:3307)");
                    lblConexion.getStyleClass().setAll("status-ok");
                } else {
                    lblConexion.setText("⬤  Sin conexión — verifique que Docker esté corriendo");
                    lblConexion.getStyleClass().setAll("status-error");
                }
            });
        }).start();
    }

    @FXML private void abrirProductos() { abrirVentana("Productos.fxml", "📦 Gestión de Productos", 980, 660); }
    @FXML private void abrirPedidos()   { abrirVentana("Pedidos.fxml",   "🛒 Gestión de Pedidos",   980, 660); }
    @FXML private void abrirClientes()  { abrirVentana("Clientes.fxml",  "👥 Gestión de Clientes",  980, 660); }

    private void abrirVentana(String fxml, String titulo, int w, int h) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/vallegrande/edu/pe/tutawayta/view/" + fxml)
            );
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root, w, h);
            scene.getStylesheets().add(
                getClass().getResource("/vallegrande/edu/pe/tutawayta/view/styles.css").toExternalForm()
            );
            stage.setTitle(titulo + " — Tuta Wayta");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
