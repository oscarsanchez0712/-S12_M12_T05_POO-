package vallegrande.edu.pe.tutawayta;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import vallegrande.edu.pe.tutawayta.util.DatabaseConnection;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Verificar conexión al iniciar
        if (!DatabaseConnection.testConnection()) {
            System.err.println("⚠️  No se pudo conectar a MySQL. Verifique que Docker esté corriendo.");
        }

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/vallegrande/edu/pe/tutawayta/view/Welcome.fxml")
        );
        Parent root = loader.load();

        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(
                getClass().getResource("/vallegrande/edu/pe/tutawayta/view/styles.css").toExternalForm()
        );

        primaryStage.setTitle("🌵 Tuta Wayta - Sistema de Gestión");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() {
        DatabaseConnection.closeConnection();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
