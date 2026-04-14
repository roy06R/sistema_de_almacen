package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseConnection.java
 * ========================
 * Gestiona la conexión única con la base de datos MySQL remota (Aiven Cloud).
 *
 * PATRÓN DE DISEÑO: SINGLETON
 * ===========================
 * Este patrón asegura que durante toda la ejecución del programa exista
 * UNA SOLA instancia de la conexión a la base de datos. Esto es importante
 * porque abrir múltiples conexiones simultáneas al servidor remoto sería
 * costoso e innecesario. Con Singleton, todos los DAOs comparten la misma
 * conexión sin saberlo.
 *
 * Funcionamiento:
 *  1. El constructor es PRIVADO → nadie puede hacer "new DatabaseConnection()"
 *  2. El único acceso es mediante getInstance()
 *  3. getInstance() crea la instancia solo si aún no existe (lazy initialization)
 */
public class DatabaseConnection {

    // ===== DATOS DE CONEXIÓN AL SERVIDOR REMOTO DE AIVEN =====
    // Estos datos fueron provistos en el documento del proyecto final
    private static final String URL =
            "jdbc:mysql://almacenitla-db-itla-3837.e.aivencloud.com:25037/almacenitlafinal"
            + "?useSSL=true&requireSSL=true&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    private static final String USUARIO  = "avnadmin";
    private static final String PASSWORD = "AVNS_pPa2xcIg1UbjOzcsoMg";

    // ===== SINGLETON: instancia única de esta clase =====
    private static DatabaseConnection instancia;

    // La conexión real a MySQL
    private Connection conexion;

    // =====================================================
    // Constructor PRIVADO → nadie puede instanciar desde afuera
    // =====================================================
    private DatabaseConnection() {
        conectar();
    }

    /**
     * Abre la conexión con el servidor MySQL remoto.
     * Carga el driver JDBC de MySQL antes de conectarse.
     */
    private void conectar() {
        try {
            // Cargamos el driver JDBC de MySQL Connector/J
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establecemos la conexión con el servidor remoto
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("[DB] Conexion exitosa al servidor remoto.");

        } catch (ClassNotFoundException e) {
            System.err.println("[DB] ERROR: No se encontro el driver MySQL.");
            System.err.println("     Asegurate de agregar mysql-connector-j.jar al Build Path.");
        } catch (SQLException e) {
            System.err.println("[DB] ERROR al conectar a la base de datos: " + e.getMessage());
        }
    }

    // =====================================================
    // SINGLETON: único punto de acceso a esta clase
    // =====================================================
    /**
     * Devuelve la instancia única de DatabaseConnection.
     * Si aún no existe, la crea (lazy initialization).
     *
     * @return La instancia única de DatabaseConnection
     */
    public static DatabaseConnection getInstance() {
        if (instancia == null) {
            instancia = new DatabaseConnection();
        }
        return instancia;
    }

    /**
     * Devuelve el objeto Connection activo.
     * Si la conexión se cerró (timeout del servidor), la reconecta automáticamente.
     *
     * @return Connection — objeto de conexión JDBC a MySQL
     */
    public Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                System.out.println("[DB] Reconectando al servidor...");
                conectar();
            }
        } catch (SQLException e) {
            System.err.println("[DB] Error al verificar conexion: " + e.getMessage());
        }
        return conexion;
    }
}
