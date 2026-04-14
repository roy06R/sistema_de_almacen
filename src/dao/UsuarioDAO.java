package dao;

import database.DatabaseConnection;
import model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UsuarioDAO.java
 * ================
 * Implementación concreta del acceso a datos para la tabla 'usuarios'.
 * Aquí van todas las consultas SQL relacionadas con usuarios.
 *
 * PATRÓN DE DISEÑO: DAO (DATA ACCESS OBJECT)
 * ===========================================
 * Esta clase encapsula toda la lógica SQL para usuarios.
 * Las ventanas (views) usan esta clase sin saber nada de SQL.
 * Por ejemplo, LoginFrame llama dao.autenticar(user, pass) y
 * recibe un Usuario, sin saber cómo se consulta la base de datos.
 *
 * PILAR POO: POLIMORFISMO
 * ========================
 * Implementa GenericDAO<Usuario>, proporcionando la versión
 * concreta de cada método CRUD específicamente para usuarios.
 * UsuarioDAO y ProductoDAO implementan la misma interfaz pero
 * con SQL diferente — eso es polimorfismo.
 */
public class UsuarioDAO implements GenericDAO<Usuario> {

    // Obtenemos la conexión desde el Singleton — siempre la misma instancia
    private Connection getConexion() {
        return DatabaseConnection.getInstance().getConexion();
    }

    // =============================================================
    // MÉTODOS ESPECIALES (no forman parte de la interfaz genérica)
    // =============================================================

    /**
     * Verifica si un nombre de usuario ya está registrado en el sistema.
     * Se usa al registrarse para evitar duplicados.
     *
     * @param userName nombre de usuario a verificar
     * @return true si el userName ya existe, false si está disponible
     */
    public boolean existeUsuario(String userName) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE UserName = ?";
        try (PreparedStatement stmt = getConexion().prepareStatement(sql)) {
            stmt.setString(1, userName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error en existeUsuario: " + e.getMessage());
        }
        return false;
    }

    /**
     * Autentica al usuario verificando userName y password en la base de datos.
     * Se usa en la pantalla de Login.
     *
     * @param userName nombre de usuario ingresado
     * @param password contraseña ingresada
     * @return el objeto Usuario si las credenciales son válidas, null si no
     */
    public Usuario autenticar(String userName, String password) {
        String sql = "SELECT * FROM usuarios WHERE UserName = ? AND Password = ?";
        try (PreparedStatement stmt = getConexion().prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapearUsuario(rs);  // Encontró el usuario → retorna objeto
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error al autenticar: " + e.getMessage());
        }
        return null;  // No encontró → credenciales incorrectas
    }

    // =============================================================
    // IMPLEMENTACIONES DE GenericDAO<Usuario> (POLIMORFISMO)
    // =============================================================

    /**
     * POLIMORFISMO: implementación de insertar() para Usuario.
     * Inserta un nuevo usuario en la tabla 'usuarios'.
     */
    @Override
    public boolean insertar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (UserName, Nombre, Apellido, Telefono, Email, Password) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getConexion().prepareStatement(sql)) {
            stmt.setString(1, usuario.getUserName());
            stmt.setString(2, usuario.getNombre());
            stmt.setString(3, usuario.getApellido());
            stmt.setString(4, usuario.getTelefono());
            stmt.setString(5, usuario.getEmail());
            stmt.setString(6, usuario.getPassword());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error al insertar: " + e.getMessage());
            return false;
        }
    }

    /**
     * POLIMORFISMO: implementación de actualizar() para Usuario.
     * Actualiza los datos de un usuario existente identificado por su ID.
     */
    @Override
    public boolean actualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET UserName=?, Nombre=?, Apellido=?, "
                   + "Telefono=?, Email=?, Password=? WHERE idUser=?";
        try (PreparedStatement stmt = getConexion().prepareStatement(sql)) {
            stmt.setString(1, usuario.getUserName());
            stmt.setString(2, usuario.getNombre());
            stmt.setString(3, usuario.getApellido());
            stmt.setString(4, usuario.getTelefono());
            stmt.setString(5, usuario.getEmail());
            stmt.setString(6, usuario.getPassword());
            stmt.setInt(7, usuario.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    /**
     * POLIMORFISMO: implementación de eliminar() para Usuario.
     * Elimina el usuario con el ID indicado de la tabla 'usuarios'.
     */
    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE idUser = ?";
        try (PreparedStatement stmt = getConexion().prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error al eliminar: " + e.getMessage());
            return false;
        }
    }

    /**
     * POLIMORFISMO: implementación de obtenerPorId() para Usuario.
     */
    @Override
    public Usuario obtenerPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE idUser = ?";
        try (PreparedStatement stmt = getConexion().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapearUsuario(rs);
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error al obtener por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * POLIMORFISMO: implementación de obtenerTodos() para Usuario.
     * Devuelve todos los usuarios registrados en el sistema.
     */
    @Override
    public List<Usuario> obtenerTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY Nombre";
        try (Statement stmt = getConexion().createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error al obtener todos: " + e.getMessage());
        }
        return lista;
    }

    // =============================================================
    // MÉTODO AUXILIAR PRIVADO
    // =============================================================

    /**
     * Convierte una fila del ResultSet en un objeto Usuario.
     * Método privado de utilidad interna, no forma parte de la interfaz.
     *
     * @param rs ResultSet posicionado en una fila válida
     * @return objeto Usuario construido con los datos de esa fila
     */
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getInt("idUser"),
            rs.getString("UserName"),
            rs.getString("Nombre"),
            rs.getString("Apellido"),
            rs.getString("Telefono"),
            rs.getString("Email"),
            rs.getString("Password")
        );
    }
}
