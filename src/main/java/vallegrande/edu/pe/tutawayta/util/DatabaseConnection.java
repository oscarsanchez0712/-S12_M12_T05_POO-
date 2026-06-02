    package vallegrande.edu.pe.tutawayta.util;

    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.SQLException;

    public class DatabaseConnection {

        // ─── Configuración Docker MySQL ───────────────────────────────────────────
        private static final String URL      = "jdbc:mysql://localhost:3307/tuta_wayta_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        private static final String USER     = "root";
        private static final String PASSWORD = "root1234";
        // ─────────────────────────────────────────────────────────────────────────

        private static Connection connection = null;

        public static Connection getConnection() {
            try {
                if (connection == null || connection.isClosed()) {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    connection = DriverManager.getConnection(URL, USER, PASSWORD);
                    System.out.println("✅ Conexión exitosa a MySQL Docker");
                }
            } catch (ClassNotFoundException e) {
                System.err.println("❌ Driver no encontrado: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("❌ Error de conexión: " + e.getMessage());
            }
            return connection;
        }

        public static void closeConnection() {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                    System.out.println("🔌 Conexión cerrada.");
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }

        public static boolean testConnection() {
            try {
                Connection conn = getConnection();
                return conn != null && !conn.isClosed();
            } catch (SQLException e) {
                return false;
            }
        }
    }
