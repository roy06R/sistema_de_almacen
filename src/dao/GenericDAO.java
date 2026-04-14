package dao;

import java.util.List;

/**
 * GenericDAO.java
 * ================
 * Interfaz genérica que define las operaciones CRUD para cualquier entidad.
 *
 * PILAR POO: ABSTRACCIÓN
 * =======================
 * Esta interfaz define QUÉ operaciones se pueden hacer, pero no CÓMO.
 * Es una abstracción pura. Las clases concretas (UsuarioDAO, ProductoDAO)
 * son las que proveen la implementación real con SQL.
 *
 * PILAR POO: POLIMORFISMO
 * ========================
 * Al usar genéricos (<T>), esta misma interfaz funciona para cualquier
 * tipo de entidad. UsuarioDAO la implementa con <Usuario> y ProductoDAO
 * con <Producto>. Ambos tienen los mismos métodos (insertar, actualizar, etc.)
 * pero con comportamientos específicos para su entidad.
 *
 * PATRÓN DE DISEÑO: DAO (DATA ACCESS OBJECT)
 * ===========================================
 * El patrón DAO separa la lógica de acceso a datos de la lógica de negocio.
 * Ventajas:
 *   - Las ventanas (vistas) solo llaman insertar(), obtenerTodos(), etc.
 *   - Las ventanas NO saben nada de SQL ni de JDBC
 *   - Si cambia la base de datos (MySQL → PostgreSQL), solo cambian los DAOs
 */
public interface GenericDAO<T> {

    /**
     * Inserta una nueva entidad en la base de datos.
     *
     * @param entidad objeto a insertar (sin ID, MySQL lo genera automáticamente)
     * @return true si la inserción fue exitosa, false si hubo error
     */
    boolean insertar(T entidad);

    /**
     * Actualiza los datos de una entidad ya existente.
     *
     * @param entidad objeto con los nuevos datos (debe contener el ID existente)
     * @return true si la actualización fue exitosa, false si hubo error
     */
    boolean actualizar(T entidad);

    /**
     * Elimina una entidad de la base de datos por su ID.
     *
     * @param id identificador único de la entidad a eliminar
     * @return true si la eliminación fue exitosa, false si hubo error
     */
    boolean eliminar(int id);

    /**
     * Busca y devuelve una entidad específica por su ID.
     *
     * @param id identificador único a buscar
     * @return la entidad encontrada, o null si no existe
     */
    T obtenerPorId(int id);

    /**
     * Devuelve todas las entidades de la tabla correspondiente.
     *
     * @return Lista con todas las entidades; lista vacía si no hay ninguna
     */
    List<T> obtenerTodos();
}
