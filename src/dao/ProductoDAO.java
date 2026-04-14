package dao;

import database.DatabaseConnection;
import model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ProductoDAO.java
 * =================
 * Implementación concreta del acceso a datos para la tabla 'productos'.
 * Maneja todas las consultas SQL relacionadas con productos del almacén.
 *
 * PATRÓN DE DISEÑO: DAO (DATA ACCESS OBJECT)
 * ===========================================
 * Toda la lógica SQL de productos está aquí encapsulada.
 * GestionProductosFrame y ProductoDialogo usan esta clase
 * sin necesitar saber nada de cómo funciona MySQL.
 *
 * PILAR POO: POLIMORFISMO
 * ========================
 * Implementa GenericDAO<Producto>. Misma interfaz que UsuarioDAO
 * pero con SQL completamente diferente para la tabla 'productos'.
 * Esto es polimorfismo: mismos métodos, distintos comportamientos.
 *
 * Columnas reales de la tabla 'productos' en el servidor remoto:
 *   id                → int (PK, auto-increment)
 *   nombre            → varchar(100)
 *   marca             → varchar(100)
 *   categoria         → varchar(100)
 *   precio            → decimal(10,2)
 *   cantidad_disponible → int
 *   fecha_creacion    → timestamp (no se usa en el modelo)
 */
public class ProductoDAO implements GenericDAO<Producto> {

    // Obtenemos la conexión del Singleton cada vez que la necesitamos
    private Connection getConexion() {
        return DatabaseConnection.getInstance().getConexion();
    }

    // =============================================================
    // IMPLEMENTACIONES DE GenericDAO<Producto> (POLIMORFISMO)
    // =============================================================

    /**
     * POLIMORFISMO: implementación de insertar() para Producto.
     * Agrega un nuevo producto a la tabla 'productos'.
     */
    @Override
    public boolean insertar(Producto producto) {
        String sql = "INSERT INTO productos (nombre, marca, categoria, precio, cantidad_disponible) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getConexion().prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getMarcaProducto());
            stmt.setString(3, producto.getCategoriaProducto());
            stmt.setInt(4,    producto.getPrecioProducto());
            stmt.setInt(5,    producto.getStockProducto());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProductoDAO] Error al insertar: " + e.getMessage());
            return false;
        }
    }

    /**
     * POLIMORFISMO: implementación de actualizar() para Producto.
     * Actualiza los datos de un producto existente por su ID.
     */
    @Override
    public boolean actualizar(Producto producto) {
        String sql = "UPDATE productos SET nombre=?, marca=?, categoria=?, "
                   + "precio=?, cantidad_disponible=? WHERE id=?";
        try (PreparedStatement stmt = getConexion().prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getMarcaProducto());
            stmt.setString(3, producto.getCategoriaProducto());
            stmt.setInt(4,    producto.getPrecioProducto());
            stmt.setInt(5,    producto.getStockProducto());
            stmt.setInt(6,    producto.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProductoDAO] Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    /**
     * POLIMORFISMO: implementación de eliminar() para Producto.
     * Elimina el producto con el ID indicado.
     */
    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";
        try (PreparedStatement stmt = getConexion().prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProductoDAO] Error al eliminar: " + e.getMessage());
            return false;
        }
    }

    /**
     * POLIMORFISMO: implementación de obtenerPorId() para Producto.
     */
    @Override
    public Producto obtenerPorId(int id) {
        String sql = "SELECT id, nombre, marca, categoria, precio, cantidad_disponible "
                   + "FROM productos WHERE id = ?";
        try (PreparedStatement stmt = getConexion().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapearProducto(rs);
            }
        } catch (SQLException e) {
            System.err.println("[ProductoDAO] Error al obtener por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * POLIMORFISMO: implementación de obtenerTodos() para Producto.
     * Devuelve todos los productos registrados en el almacén.
     */
    @Override
    public List<Producto> obtenerTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, marca, categoria, precio, cantidad_disponible "
                   + "FROM productos ORDER BY nombre";
        try (Statement stmt = getConexion().createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearProducto(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ProductoDAO] Error al obtener todos: " + e.getMessage());
        }
        return lista;
    }

    // =============================================================
    // MÉTODO AUXILIAR PRIVADO
    // =============================================================

    /**
     * Convierte una fila del ResultSet en un objeto Producto.
     * Se leen las columnas por posición (1-6) para evitar problemas
     * de sensibilidad a mayúsculas con el servidor remoto Aiven.
     * Orden: id(1), nombre(2), marca(3), categoria(4), precio(5), cantidad_disponible(6)
     *
     * @param rs ResultSet posicionado en una fila válida
     * @return objeto Producto construido con los datos de esa fila
     */
    private Producto mapearProducto(ResultSet rs) throws SQLException {
        return new Producto(
            rs.getInt(1),      // id
            rs.getString(2),   // nombre
            rs.getString(3),   // marca
            rs.getString(4),   // categoria
            rs.getInt(5),      // precio
            rs.getInt(6)       // cantidad_disponible
        );
    }
}
