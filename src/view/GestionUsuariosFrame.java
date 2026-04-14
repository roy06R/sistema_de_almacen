package view;

import dao.UsuarioDAO;
import model.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * GestionUsuariosFrame.java
 * ==========================
 * Pantalla de gestión de usuarios: muestra la lista completa de usuarios
 * registrados y permite crear, editar y eliminar usuarios.
 *
 * PILAR POO: HERENCIA
 * ====================
 * Extiende VentanaBase, heredando estilos y métodos de utilidad.
 * Usa estilizarTabla(), crearBoton(), confirmar(), mostrarExito() de la clase padre.
 *
 * Funcionalidades:
 *  - Tabla con todos los usuarios: Nombre | Apellido | Teléfono | Correo | Usuario
 *  - NUEVO     → abre UsuarioDialogo vacío para crear
 *  - ACTUALIZAR → abre UsuarioDialogo con datos del usuario seleccionado
 *  - ELIMINAR  → confirma y elimina el usuario seleccionado
 *  - ← Volver → regresa a PrincipalFrame
 *  - Los cambios se reflejan automáticamente en la tabla
 */
public class GestionUsuariosFrame extends VentanaBase {

    // Referencia al menú principal para volver
    private final PrincipalFrame principal;

    // Componentes de la tabla
    private JTable              tabla;
    private DefaultTableModel   modeloTabla;

    // DAO para operaciones con la base de datos
    private final UsuarioDAO dao = new UsuarioDAO();

    // Columnas de la tabla (en el mismo orden que los datos)
    private static final String[] COLUMNAS = {
        "Nombre", "Apellido", "Telefono", "Correo Electronico", "Usuario"
    };

    // Guardamos los IDs en paralelo a las filas de la tabla
    // (la tabla no muestra el ID pero lo necesitamos para CRUD)
    private java.util.ArrayList<Integer> idsEnTabla = new java.util.ArrayList<>();

    public GestionUsuariosFrame(PrincipalFrame principal) {
        super("Sistema de Almacen - Gestion de Usuarios", 820, 560);
        this.principal = principal;
        construirUI();
        cargarUsuarios();   // Cargamos los datos al abrir la ventana
    }

    /**
     * Construye la interfaz: encabezado, tabla y barra de botones inferior.
     */
    private void construirUI() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_FONDO);

        // ── Encabezado ──
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(COLOR_AZUL_OSCURO);
        encabezado.setBorder(new EmptyBorder(14, 20, 14, 20));
        JLabel titulo = new JLabel("  Clientes Registrados");
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(Color.WHITE);
        encabezado.add(titulo, BorderLayout.WEST);

        // ── Tabla de usuarios ──
        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            // Hacemos la tabla no editable directamente
            // (la edición se hace a través del diálogo de actualizar)
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        estilizarTabla(tabla);

        // Ajustamos el ancho de cada columna para que se vea bien
        tabla.getColumnModel().getColumn(0).setPreferredWidth(130);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(130);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(110);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(120);

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

        JButton btnNuevo      = crearBoton("+ Nuevo");
        JButton btnActualizar = crearBoton("Actualizar");
        JButton btnEliminar   = crearBotonPeligro("Eliminar");
        JButton btnVolver     = crearBoton("<- Volver");
        btnVolver.setBackground(new Color(90, 90, 110));

        btnNuevo.addActionListener(e -> abrirNuevoUsuario());
        btnActualizar.addActionListener(e -> abrirActualizarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuarioSeleccionado());
        btnVolver.addActionListener(e -> volver());

        panelBotones.add(btnNuevo);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(Box.createHorizontalStrut(20));
        panelBotones.add(btnVolver);

        add(encabezado,   BorderLayout.NORTH);
        add(panelTabla,   BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Carga todos los usuarios desde la base de datos y los muestra en la tabla.
     * Se llama al abrir la ventana y después de cada operación CRUD.
     */
    public void cargarUsuarios() {
        modeloTabla.setRowCount(0);    // Limpiamos la tabla
        idsEnTabla.clear();            // Limpiamos los IDs guardados

        List<Usuario> usuarios = dao.obtenerTodos();
        for (Usuario u : usuarios) {
            modeloTabla.addRow(new Object[]{
                u.getNombre(),
                u.getApellido(),
                u.getTelefono(),
                u.getEmail(),
                u.getUserName()
            });
            idsEnTabla.add(u.getId());  // Guardamos el ID en la misma posición
        }
    }

    /**
     * Abre el diálogo de creación de nuevo usuario.
     * Al cerrarse, refresca la tabla automáticamente.
     */
    private void abrirNuevoUsuario() {
        UsuarioDialogo dialogo = new UsuarioDialogo(this, null);
        dialogo.setVisible(true);
        cargarUsuarios();  // Refrescamos la tabla al cerrarse el diálogo
    }

    /**
     * Abre el diálogo de edición del usuario seleccionado.
     * Muestra error si no hay ningún usuario seleccionado.
     */
    private void abrirActualizarUsuario() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada < 0) {
            mostrarError("Debe seleccionar un usuario de la lista para actualizarlo.");
            return;
        }

        // Obtenemos el ID del usuario seleccionado (la fila coincide con idsEnTabla)
        int idUsuario = idsEnTabla.get(filaSeleccionada);
        Usuario usuario = dao.obtenerPorId(idUsuario);

        if (usuario != null) {
            UsuarioDialogo dialogo = new UsuarioDialogo(this, usuario);
            dialogo.setVisible(true);
            cargarUsuarios();  // Refrescamos la tabla al cerrarse el diálogo
        } else {
            mostrarError("No se pudo cargar el usuario seleccionado.");
        }
    }

    /**
     * Elimina el usuario seleccionado en la tabla, previa confirmación.
     */
    private void eliminarUsuarioSeleccionado() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada < 0) {
            mostrarError("Debe seleccionar un usuario de la lista para eliminarlo.");
            return;
        }

        String nombre = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
        String apellido = (String) modeloTabla.getValueAt(filaSeleccionada, 1);

        if (confirmar("¿Desea eliminar al usuario: " + nombre + " " + apellido + "?")) {
            int idUsuario = idsEnTabla.get(filaSeleccionada);
            if (dao.eliminar(idUsuario)) {
                mostrarExito("Usuario eliminado correctamente.");
                cargarUsuarios();  // Refrescamos la tabla automáticamente
            } else {
                mostrarError("No se pudo eliminar el usuario. Intente de nuevo.");
            }
        }
    }

    /**
     * Cierra esta ventana y vuelve al menú principal.
     */
    private void volver() {
        dispose();
        principal.setVisible(true);
    }
}
