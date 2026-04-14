package view;

import dao.ProductoDAO;
import model.Producto;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * ProductoDialogo.java
 * =====================
 * Diálogo modal para crear, editar o eliminar un producto del almacén.
 * Llamado desde GestionProductosFrame al hacer clic en una fila o en "+ Nuevo".
 *
 * Modo NUEVO (producto == null):
 *  - Todos los campos vacíos
 *  - Solo muestra el botón "Guardar" (para crear el producto)
 *
 * Modo EDITAR (producto != null):
 *  - Campos rellenos con los datos del producto
 *  - Muestra botones "Guardar" y "Eliminar"
 *
 * PILAR POO: HERENCIA
 * ====================
 * Extiende JDialog. Los colores y fuentes se acceden como constantes
 * estáticas de VentanaBase (otra forma de herencia/reutilización).
 */
public class ProductoDialogo extends JDialog {

    // El producto a editar (null si es nuevo)
    private final Producto productoExistente;

    // Frame padre para posicionamiento de este diálogo
    private final GestionProductosFrame padre;

    // Campos del formulario
    private JTextField campoNombre;
    private JTextField campoMarca;
    private JTextField campoCategoria;
    private JTextField campoPrecio;
    private JTextField campoStock;

    private final ProductoDAO dao = new ProductoDAO();

    /**
     * @param padre    frame padre (GestionProductosFrame)
     * @param producto producto a editar, o null para crear uno nuevo
     */
    public ProductoDialogo(GestionProductosFrame padre, Producto producto) {
        super(padre,
              producto == null ? "Nuevo Producto" : "Editar Producto",
              true);   // modal = bloquea la ventana padre mientras esté abierto
        this.padre = padre;
        this.productoExistente = producto;

        setSize(460, 520);
        setLocationRelativeTo(padre);
        setResizable(false);
        getContentPane().setBackground(VentanaBase.COLOR_FONDO);

        construirUI();

        if (productoExistente != null) {
            rellenarCampos();
        }
    }

    /**
     * Construye el formulario del diálogo.
     */
    private void construirUI() {
        setLayout(new BorderLayout());

        // ── Encabezado ──
        JPanel encabezado = new JPanel(new FlowLayout(FlowLayout.CENTER));
        encabezado.setBackground(VentanaBase.COLOR_AZUL_OSCURO);
        encabezado.setBorder(new EmptyBorder(12, 20, 12, 20));
        String textoTitulo = productoExistente == null ? "NUEVO PRODUCTO" : "EDITAR PRODUCTO";
        JLabel titulo = new JLabel(textoTitulo);
        titulo.setFont(VentanaBase.FUENTE_SUBTITULO);
        titulo.setForeground(Color.WHITE);
        encabezado.add(titulo);

        // ── Formulario central ──
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(VentanaBase.COLOR_FONDO);
        panelForm.setBorder(new EmptyBorder(20, 35, 10, 35));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 0, 4, 0);
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        campoNombre    = crearCampoEstilo();
        campoMarca     = crearCampoEstilo();
        campoCategoria = crearCampoEstilo();
        campoPrecio    = crearCampoEstilo();
        campoStock     = crearCampoEstilo();

        int fila = 0;
        fila = addField(panelForm, gbc, fila, "Nombre *",            campoNombre);
        fila = addField(panelForm, gbc, fila, "Marca *",             campoMarca);
        fila = addField(panelForm, gbc, fila, "Categoria *",         campoCategoria);
        fila = addField(panelForm, gbc, fila, "Precio *",            campoPrecio);
        fila = addField(panelForm, gbc, fila, "Cantidad Disponible *", campoStock);

        // ── Botones inferiores ──
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 14));
        panelBotones.setBackground(VentanaBase.COLOR_FONDO);
        panelBotones.setBorder(new EmptyBorder(0, 0, 8, 0));

        JButton btnCancelar = crearBotonDialog("Cancelar", new Color(90, 90, 110));
        btnCancelar.addActionListener(e -> dispose());

        JButton btnGuardar = crearBotonDialog("Guardar", VentanaBase.COLOR_AZUL_MEDIO);
        btnGuardar.addActionListener(e -> handleGuardar());

        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        // Solo en modo EDITAR mostramos el botón Eliminar
        if (productoExistente != null) {
            JButton btnEliminar = crearBotonDialog("Eliminar", VentanaBase.COLOR_ROJO);
            btnEliminar.addActionListener(e -> handleEliminar());
            panelBotones.add(btnEliminar);
        }

        add(encabezado,   BorderLayout.NORTH);
        add(panelForm,    BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Rellena los campos con los datos del producto existente.
     * Solo se llama en modo EDITAR.
     */
    private void rellenarCampos() {
        campoNombre.setText(productoExistente.getNombre());
        campoMarca.setText(productoExistente.getMarcaProducto());
        campoCategoria.setText(productoExistente.getCategoriaProducto());
        campoPrecio.setText(String.valueOf(productoExistente.getPrecioProducto()));
        campoStock.setText(String.valueOf(productoExistente.getStockProducto()));
    }

    /**
     * Valida los campos y guarda el producto (nuevo o actualizado).
     * Al finalizar, cierra el diálogo para que el padre refresque la tabla.
     */
    private void handleGuardar() {
        String nombre    = campoNombre.getText().trim();
        String marca     = campoMarca.getText().trim();
        String categoria = campoCategoria.getText().trim();
        String precioStr = campoPrecio.getText().trim();
        String stockStr  = campoStock.getText().trim();

        // Validaciones de campos obligatorios
        if (nombre.isEmpty())    { showErr("El campo 'Nombre' es obligatorio.");              campoNombre.requestFocus();    return; }
        if (marca.isEmpty())     { showErr("El campo 'Marca' es obligatorio.");               campoMarca.requestFocus();     return; }
        if (categoria.isEmpty()) { showErr("El campo 'Categoria' es obligatorio.");           campoCategoria.requestFocus(); return; }
        if (precioStr.isEmpty()) { showErr("El campo 'Precio' es obligatorio.");              campoPrecio.requestFocus();    return; }
        if (stockStr.isEmpty())  { showErr("El campo 'Cantidad Disponible' es obligatorio."); campoStock.requestFocus();     return; }

        // Validamos que precio y stock sean números enteros válidos
        int precio, stock;
        try {
            precio = Integer.parseInt(precioStr);
            if (precio < 0) {
                showErr("El precio no puede ser negativo.");
                campoPrecio.requestFocus();
                return;
            }
        } catch (NumberFormatException ex) {
            showErr("El campo 'Precio' debe ser un numero entero valido.\nEjemplo: 1500");
            campoPrecio.selectAll();
            campoPrecio.requestFocus();
            return;
        }

        try {
            stock = Integer.parseInt(stockStr);
            if (stock < 0) {
                showErr("La cantidad disponible no puede ser negativa.");
                campoStock.requestFocus();
                return;
            }
        } catch (NumberFormatException ex) {
            showErr("El campo 'Cantidad Disponible' debe ser un numero entero valido.\nEjemplo: 50");
            campoStock.selectAll();
            campoStock.requestFocus();
            return;
        }

        if (productoExistente == null) {
            // ── MODO NUEVO: crear producto ──
            Producto nuevo = new Producto(nombre, marca, categoria, precio, stock);
            if (dao.insertar(nuevo)) {
                showInfo("Producto creado exitosamente.");
                dispose();  // Cerramos → el padre llama cargarProductos() automáticamente
            } else {
                showErr("No se pudo crear el producto. Verifique la conexion.");
            }
        } else {
            // ── MODO EDITAR: actualizar producto ──
            productoExistente.setNombreProducto(nombre);
            productoExistente.setMarcaProducto(marca);
            productoExistente.setCategoriaProducto(categoria);
            productoExistente.setPrecioProducto(precio);
            productoExistente.setStockProducto(stock);

            if (dao.actualizar(productoExistente)) {
                showInfo("Producto actualizado correctamente.");
                dispose();  // Cerramos → el padre llama cargarProductos() automáticamente
            } else {
                showErr("No se pudo actualizar el producto.");
            }
        }
    }

    /**
     * Elimina el producto después de confirmación del usuario.
     * Al finalizar, cierra el diálogo para que el padre refresque la tabla.
     */
    private void handleEliminar() {
        int respuesta = JOptionPane.showConfirmDialog(
            this,
            "¿Desea eliminar el producto: " + productoExistente.getNombre() + "?\n"
            + "Esta accion no se puede deshacer.",
            "Confirmar eliminacion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            if (dao.eliminar(productoExistente.getId())) {
                showInfo("Producto eliminado correctamente.");
                dispose();  // Cerramos → el padre llama cargarProductos() automáticamente
            } else {
                showErr("No se pudo eliminar el producto.");
            }
        }
    }

    // ── Métodos de utilidad internos ──

    private void showErr(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Exito", JOptionPane.INFORMATION_MESSAGE);
    }

    private JTextField crearCampoEstilo() {
        JTextField f = new JTextField();
        f.setFont(VentanaBase.FUENTE_CAMPO);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(VentanaBase.COLOR_AZUL_CLARO, 1),
            new EmptyBorder(6, 10, 6, 10)
        ));
        f.setPreferredSize(new Dimension(0, 36));
        return f;
    }

    private JButton crearBotonDialog(String texto, Color colorFondo) {
        JButton btn = new JButton(texto);
        btn.setBackground(colorFondo);
        btn.setForeground(Color.WHITE);
        btn.setFont(VentanaBase.FUENTE_BOTON);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(9, 22, 9, 22));
        btn.setOpaque(true);
        return btn;
    }

    private int addField(JPanel panel, GridBagConstraints gbc, int fila, String label, JComponent campo) {
        gbc.gridy = fila++;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(new Color(80, 90, 120));
        panel.add(lbl, gbc);
        gbc.gridy = fila++;
        panel.add(campo, gbc);
        return fila;
    }
}
