package view;

import dao.ProductoDAO;
import model.Producto;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * GestionProductosFrame.java
 * ===========================
 * Pantalla de gestión de productos del almacén.
 * Muestra la lista completa de productos con todos sus datos,
 * y permite agregar nuevos productos o editar/eliminar los existentes.
 *
 * PILAR POO: HERENCIA
 * ====================
 * Extiende VentanaBase, heredando estilos y métodos de utilidad.
 *
 * Funcionalidades:
 *  - Tabla con todos los productos: Nombre | Marca | Categoría | Precio | Cant. Disponible
 *  - NUEVO  → abre ProductoDialogo vacío para crear
 *  - Clic en fila → abre ProductoDialogo con datos del producto (editar/eliminar)
 *  - ← Volver → regresa a PrincipalFrame
 *  - Los cambios se reflejan automáticamente en la tabla
 */
public class GestionProductosFrame extends VentanaBase {

    // Referencia al menú principal para poder volver
    private final PrincipalFrame principal;

    // Componentes de la tabla
    private JTable            tabla;
    private DefaultTableModel modeloTabla;

    // DAO para operaciones con la base de datos
    private final ProductoDAO dao = new ProductoDAO();

    // Columnas de la tabla
    private static final String[] COLUMNAS = {
        "Nombre", "Marca", "Categoria", "Precio ($)", "Cant. Disponible"
    };

    // IDs guardados en paralelo a las filas (igual que en GestionUsuariosFrame)
    private java.util.ArrayList<Integer> idsEnTabla = new java.util.ArrayList<>();

    public GestionProductosFrame(PrincipalFrame principal) {
        super("Sistema de Almacen - Productos de Almacen", 860, 560);
        this.principal = principal;
        construirUI();
        cargarProductos();  // Cargamos los datos al abrir la ventana
    }

    /**
     * Construye la interfaz: encabezado, tabla, instrucción de uso y botones.
     */
    private void construirUI() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_FONDO);

        // ── Encabezado ──
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(COLOR_AZUL_OSCURO);
        encabezado.setBorder(new EmptyBorder(14, 20, 14, 20));
        JLabel titulo = new JLabel("  Productos de Almacen");
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(Color.WHITE);

        JLabel instruccion = new JLabel("Haga clic en un producto para editarlo o eliminarlo  ");
        instruccion.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        instruccion.setForeground(new Color(180, 200, 230));

        encabezado.add(titulo,      BorderLayout.WEST);
        encabezado.add(instruccion, BorderLayout.EAST);

        // ── Tabla de productos ──
        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            // No editable directamente → se edita a través del diálogo
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        estilizarTabla(tabla);

        // Ajuste de anchos de columna
        tabla.getColumnModel().getColumn(0).setPreferredWidth(180);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(130);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(130);

        // Clic en una fila → abre el diálogo de edición/eliminación
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila >= 0) {
                    abrirProductoSeleccionado(fila);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_AZUL_CLARO));
        scrollPane.getViewport().setBackground(COLOR_FILA_IMPAR);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(COLOR_FONDO);
        panelTabla.setBorder(new EmptyBorder(20, 20, 10, 20));
        panelTabla.add(scrollPane, BorderLayout.CENTER);

        // ── Barra de botones inferior ──
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        panelBotones.setBackground(COLOR_FONDO);
        panelBotones.setBorder(new EmptyBorder(5, 12, 10, 12));

        JButton btnNuevo  = crearBoton("+ Nuevo Producto");
        JButton btnVolver = crearBoton("<- Volver");
        btnVolver.setBackground(new Color(90, 90, 110));

        btnNuevo.addActionListener(e -> abrirNuevoProducto());
        btnVolver.addActionListener(e -> volver());

        panelBotones.add(btnNuevo);
        panelBotones.add(Box.createHorizontalStrut(20));
        panelBotones.add(btnVolver);

        add(encabezado,   BorderLayout.NORTH);
        add(panelTabla,   BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Carga todos los productos desde la base de datos y los muestra en la tabla.
     * Se llama al abrir la ventana y después de cada operación CRUD.
     */
    public void cargarProductos() {
        modeloTabla.setRowCount(0);
        idsEnTabla.clear();

        List<Producto> productos = dao.obtenerTodos();
        for (Producto p : productos) {
            modeloTabla.addRow(new Object[]{
                p.getNombre(),
                p.getMarcaProducto(),
                p.getCategoriaProducto(),
                "$" + p.getPrecioProducto(),
                p.getStockProducto()
            });
            idsEnTabla.add(p.getId());
        }
    }

    /**
     * Abre el diálogo con los datos del producto seleccionado para editar/eliminar.
     *
     * @param fila índice de la fila seleccionada en la tabla
     */
    private void abrirProductoSeleccionado(int fila) {
        int idProducto = idsEnTabla.get(fila);
        Producto producto = dao.obtenerPorId(idProducto);

        if (producto != null) {
            ProductoDialogo dialogo = new ProductoDialogo(this, producto);
            dialogo.setVisible(true);
            cargarProductos();  // Refrescamos la tabla al cerrarse el diálogo
        } else {
            JOptionPane.showMessageDialog(this,
                "No se pudo cargar el producto seleccionado.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Abre el diálogo vacío para crear un nuevo producto.
     */
    private void abrirNuevoProducto() {
        ProductoDialogo dialogo = new ProductoDialogo(this, null);
        dialogo.setVisible(true);
        cargarProductos();  // Refrescamos la tabla al cerrarse el diálogo
    }

    /**
     * Cierra esta ventana y vuelve al menú principal.
     */
    private void volver() {
        dispose();
        principal.setVisible(true);
    }
}
