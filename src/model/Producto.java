package model;

/**
 * Producto.java
 * ==============
 * Representa un producto del almacén registrado en el sistema.
 * Corresponde a la tabla 'productos' de la base de datos MySQL.
 *
 * PILAR POO: ENCAPSULAMIENTO
 * ===========================
 * Todos los atributos son privados. Solo se acceden mediante getters/setters.
 * Esto asegura que nadie pueda asignar valores inválidos directamente
 * (por ejemplo, un precio negativo).
 *
 * PILAR POO: HERENCIA
 * ====================
 * Extiende Entidad, heredando su contrato abstracto.
 * Implementa getId(), getNombre() y getResumen() de forma propia.
 *
 * Columnas de la tabla 'productos' en MySQL:
 *   idProducto        → int (PK, auto-increment)
 *   NombreProducto    → varchar(140)
 *   MarcaProducto     → varchar(140)
 *   CategoriaProducto → varchar(140)
 *   PrecioProducto    → int
 *   StockProducto     → int
 */
public class Producto extends Entidad {

    // ===== ATRIBUTOS PRIVADOS (ENCAPSULAMIENTO) =====
    private int    idProducto;
    private String nombreProducto;
    private String marcaProducto;
    private String categoriaProducto;
    private int    precioProducto;
    private int    stockProducto;

    // ===== CONSTRUCTORES =====

    /** Constructor vacío */
    public Producto() {}

    /**
     * Constructor completo con ID — se usa al leer de la base de datos
     */
    public Producto(int idProducto, String nombreProducto, String marcaProducto,
                    String categoriaProducto, int precioProducto, int stockProducto) {
        this.idProducto        = idProducto;
        this.nombreProducto    = nombreProducto;
        this.marcaProducto     = marcaProducto;
        this.categoriaProducto = categoriaProducto;
        this.precioProducto    = precioProducto;
        this.stockProducto     = stockProducto;
    }

    /**
     * Constructor sin ID — se usa al registrar un producto nuevo
     */
    public Producto(String nombreProducto, String marcaProducto,
                    String categoriaProducto, int precioProducto, int stockProducto) {
        this.nombreProducto    = nombreProducto;
        this.marcaProducto     = marcaProducto;
        this.categoriaProducto = categoriaProducto;
        this.precioProducto    = precioProducto;
        this.stockProducto     = stockProducto;
    }

    // ===== GETTERS Y SETTERS (ENCAPSULAMIENTO) =====

    /** HERENCIA: implementa getId() de Entidad */
    @Override
    public int getId() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    /** HERENCIA: implementa getNombre() de Entidad */
    @Override
    public String getNombre() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getMarcaProducto() {
        return marcaProducto;
    }

    public void setMarcaProducto(String marcaProducto) {
        this.marcaProducto = marcaProducto;
    }

    public String getCategoriaProducto() {
        return categoriaProducto;
    }

    public void setCategoriaProducto(String categoriaProducto) {
        this.categoriaProducto = categoriaProducto;
    }

    public int getPrecioProducto() {
        return precioProducto;
    }

    public void setPrecioProducto(int precioProducto) {
        this.precioProducto = precioProducto;
    }

    public int getStockProducto() {
        return stockProducto;
    }

    public void setStockProducto(int stockProducto) {
        this.stockProducto = stockProducto;
    }

    // ===== POLIMORFISMO =====

    /**
     * POLIMORFISMO — Implementación específica del resumen para Producto.
     * Distinta a la de Usuario, aunque comparten el mismo método abstracto.
     *
     * @return "NombreProducto | MarcaProducto | $Precio"
     */
    @Override
    public String getResumen() {
        return nombreProducto + " | " + marcaProducto + " | $" + precioProducto;
    }

    @Override
    public String toString() {
        return getResumen();
    }
}
