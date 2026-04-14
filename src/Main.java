import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import view.LoginFrame;

/**
 * Main.java — Punto de entrada del Sistema de Gestión de Productos de Almacén
 * =============================================================================
 * ─────────────────────────────────────────────────────────────────────────────
 * ARQUITECTURA DEL PROYECTO
 * ─────────────────────────────────────────────────────────────────────────────
 *
 *  database/
 *    DatabaseConnection.java  ← SINGLETON: única conexión a MySQL remoto
 *
 *  model/
 *    Entidad.java             ← ABSTRACCIÓN: clase base abstracta para entidades
 *    Usuario.java             ← ENCAPSULAMIENTO + HERENCIA (extiende Entidad)
 *    Producto.java            ← ENCAPSULAMIENTO + HERENCIA (extiende Entidad)
 *
 *  dao/
 *    GenericDAO.java          ← ABSTRACCIÓN: interfaz genérica CRUD (DAO pattern)
 *    UsuarioDAO.java          ← POLIMORFISMO: implementa GenericDAO<Usuario>
 *    ProductoDAO.java         ← POLIMORFISMO: implementa GenericDAO<Producto>
 *
 *  view/
 *    VentanaBase.java         ← HERENCIA: clase base abstracta para ventanas
 *    LoginFrame.java          ← Pantalla de login
 *    RegistroFrame.java       ← Pantalla de registro
 *    PrincipalFrame.java      ← Menú principal (Usuarios / Productos)
 *    GestionUsuariosFrame.java← Lista y CRUD de usuarios
 *    UsuarioDialogo.java      ← Diálogo crear/editar usuario
 *    GestionProductosFrame.java ← Lista de productos con clic para editar
 *    ProductoDialogo.java     ← Diálogo crear/editar/eliminar producto
 *
 * ─────────────────────────────────────────────────────────────────────────────
 * PILARES DE POO APLICADOS
 * ─────────────────────────────────────────────────────────────────────────────
 *
 *  ABSTRACCIÓN:
 *    → Entidad.java define getId(), getNombre(), getResumen() como abstractos
 *    → GenericDAO<T> define las operaciones CRUD sin implementarlas
 *    → VentanaBase es abstract (no instanciable directamente)
 *
 *  ENCAPSULAMIENTO:
 *    → Usuario y Producto tienen todos sus campos private
 *    → Solo accesibles a través de getters y setters públicos
 *
 *  HERENCIA:
 *    → Usuario extends Entidad
 *    → Producto extends Entidad
 *    → LoginFrame, RegistroFrame, PrincipalFrame, GestionUsuariosFrame,
 *      GestionProductosFrame todas extienden VentanaBase
 *
 *  POLIMORFISMO:
 *    → getResumen() se comporta distinto en Usuario vs Producto
 *    → UsuarioDAO y ProductoDAO implementan los mismos métodos CRUD
 *      (insertar, actualizar, eliminar, obtenerTodos) de manera diferente
 *
 * ─────────────────────────────────────────────────────────────────────────────
 * PATRONES DE DISEÑO UTILIZADOS
 * ─────────────────────────────────────────────────────────────────────────────
 *
 *  1. SINGLETON (database/DatabaseConnection.java):
 *     Garantiza una única instancia de la conexión a la base de datos.
 *     Constructor privado + método getInstance() estático.
 *
 *  2. DAO — DATA ACCESS OBJECT (dao/UsuarioDAO.java, dao/ProductoDAO.java):
 *     Separa la lógica de acceso a datos (SQL/JDBC) de la lógica de las vistas.
 *     Las pantallas solo llaman métodos como insertar(), obtenerTodos(), etc.
 *     sin saber nada de cómo funciona MySQL internamente.
 *
 * ─────────────────────────────────────────────────────────────────────────────
 * BASE DE DATOS REMOTA (Aiven Cloud MySQL)
 * ─────────────────────────────────────────────────────────────────────────────
 *  Host:     almacenitla-db-itla-3837.e.aivencloud.com:25037
 *  DB:       almacenitlafinal
 *  User:     avnadmin
 *  Tablas:   usuarios, productos (ver DatabaseConnection.java para la URL completa)
 */
public class Main {

    public static void main(String[] args) {
        // Intentamos usar el look and feel nativo del sistema operativo
        // para que la aplicación se vea más "real" en Windows
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si no se puede cargar el nativo, Swing usa el suyo por defecto
            System.out.println("[Main] Usando look and feel por defecto de Java.");
        }

        // Iniciamos la interfaz gráfica en el Event Dispatch Thread de Swing.
        // Esto es obligatorio en Java Swing para evitar problemas de concurrencia.
        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }
}
