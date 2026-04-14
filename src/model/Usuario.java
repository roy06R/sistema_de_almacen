package model;

/**
 * Usuario.java
 * =============
 * Representa a un usuario registrado en el sistema de almacén.
 * Corresponde a la tabla 'usuarios' de la base de datos MySQL.
 *
 * PILAR POO: ENCAPSULAMIENTO
 * ===========================
 * Todos los atributos son PRIVADOS. Nadie puede acceder ni modificar
 * los datos del usuario directamente. Solo se puede hacer a través de
 * los métodos getters y setters públicos que yo controlo.
 * Esto protege la integridad de los datos del usuario.
 *
 * PILAR POO: HERENCIA
 * ====================
 * Extiende la clase abstracta Entidad, heredando su estructura.
 * Implementa los métodos getId(), getNombre() y getResumen()
 * que Entidad declara como abstractos.
 *
 * Columnas de la tabla 'usuarios' en MySQL:
 *   idUser       → int (PK, auto-increment)
 *   UserName     → varchar(140)
 *   Nombre       → varchar(140)
 *   Apellido     → varchar(140)
 *   Telefono     → varchar(140)
 *   Email        → varchar(140)
 *   Password     → varchar(140)
 */
public class Usuario extends Entidad {

    // ===== ATRIBUTOS PRIVADOS (ENCAPSULAMIENTO) =====
    // Nadie puede acceder a estos campos directamente desde afuera
    private int    idUser;
    private String userName;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String password;

    // ===== CONSTRUCTORES =====

    /** Constructor vacío — necesario para ciertas operaciones */
    public Usuario() {}

    /**
     * Constructor completo — incluye el ID (se usa al leer de la base de datos)
     */
    public Usuario(int idUser, String userName, String nombre, String apellido,
                   String telefono, String email, String password) {
        this.idUser   = idUser;
        this.userName = userName;
        this.nombre   = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email    = email;
        this.password = password;
    }

    /**
     * Constructor sin ID — se usa al crear un usuario nuevo
     * (MySQL asigna el ID automáticamente con AUTO_INCREMENT)
     */
    public Usuario(String userName, String nombre, String apellido,
                   String telefono, String email, String password) {
        this.userName = userName;
        this.nombre   = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email    = email;
        this.password = password;
    }

    // ===== GETTERS Y SETTERS (ENCAPSULAMIENTO) =====
    // La única forma de leer/modificar los datos del usuario

    /** HERENCIA: implementa getId() de Entidad */
    @Override
    public int getId() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    /** HERENCIA: implementa getNombre() de Entidad */
    @Override
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // ===== POLIMORFISMO =====

    /**
     * POLIMORFISMO — Implementación específica del resumen para Usuario.
     * Entidad lo declara abstracto, Usuario lo implementa a su manera.
     *
     * @return "Nombre Apellido (@username)"
     */
    @Override
    public String getResumen() {
        return nombre + " " + apellido + " (@" + userName + ")";
    }

    @Override
    public String toString() {
        return getResumen();
    }
}
