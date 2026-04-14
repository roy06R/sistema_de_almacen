package model;

/**
 * Entidad.java
 * =============
 * Clase base ABSTRACTA que representa cualquier entidad del sistema.
 *
 * PILAR POO: ABSTRACCIÓN
 * =======================
 * Esta clase no puede instanciarse directamente (es abstracta).
 * Define una "plantilla" de lo que cualquier entidad del sistema debe tener:
 * un ID, un nombre, y un resumen descriptivo. Los detalles específicos
 * de cada entidad los implementan las subclases (Usuario, Producto).
 *
 * PILAR POO: POLIMORFISMO
 * ========================
 * El método getResumen() es abstracto → cada subclase lo implementa
 * a su manera. Esto permite tratar objetos de tipo diferente
 * (Usuario, Producto) de forma uniforme a través de esta clase base.
 *
 * Ejemplo de polimorfismo:
 *   Entidad e1 = new Usuario(...);
 *   Entidad e2 = new Producto(...);
 *   System.out.println(e1.getResumen()); // imprime resumen de usuario
 *   System.out.println(e2.getResumen()); // imprime resumen de producto
 */
public abstract class Entidad {

    /**
     * Devuelve el identificador único de la entidad en la base de datos.
     * Cada subclase retorna el campo de ID que corresponde a su tabla.
     *
     * @return ID numérico de la entidad
     */
    public abstract int getId();

    /**
     * Devuelve el nombre principal de la entidad.
     *
     * @return nombre de la entidad
     */
    public abstract String getNombre();

    /**
     * POLIMORFISMO — Devuelve una descripción breve de la entidad.
     * Usuario lo implementa como: "Nombre Apellido (@username)"
     * Producto lo implementa como: "Nombre | Marca | $Precio"
     *
     * @return String con el resumen descriptivo de la entidad
     */
    public abstract String getResumen();
}
