package view;

import dao.UsuarioDAO;
import model.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * RegistroFrame.java
 * ===================
 * Pantalla para registrar un nuevo usuario en el sistema.
 *
 * PILAR POO: HERENCIA
 * ====================
 * Extiende VentanaBase, reutilizando crearCampo(), crearCampoPassword(),
 * crearBoton(), mostrarError() etc. de la clase padre.
 *
 * Campos del formulario:
 *  - Nombre (obligatorio)
 *  - Apellido (obligatorio)
 *  - Nombre de Usuario (obligatorio, único)
 *  - Número de Teléfono (obligatorio)
 *  - Correo Electrónico (obligatorio)
 *  - Contraseña (obligatorio, oculta)
 *  - Confirmar Contraseña (obligatorio, debe coincidir)
 *
 * Validaciones implementadas:
 *  - Ningún campo puede estar vacío
 *  - Las contraseñas deben coincidir
 *  - El nombre de usuario no puede estar ya registrado
 */
public class RegistroFrame extends VentanaBase {

    // Referencia al LoginFrame para mostrarlo de nuevo al volver
    private final LoginFrame loginFrame;

    // Campos del formulario
    private JTextField     campoNombre;
    private JTextField     campoApellido;
    private JTextField     campoUsuario;
    private JTextField     campoTelefono;
    private JTextField     campoEmail;
    private JPasswordField campoPassword;
    private JPasswordField campoConfirmar;

    public RegistroFrame(LoginFrame loginFrame) {
        super("Sistema de Almacen - Registro", 520, 600);
        this.loginFrame = loginFrame;
        construirUI();
    }

    /**
     * Construye todos los componentes de la pantalla de registro.
     */
    private void construirUI() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_FONDO);

        // ── Encabezado azul con el título ──
        JPanel encabezado = new JPanel(new FlowLayout(FlowLayout.CENTER));
        encabezado.setBackground(COLOR_AZUL_OSCURO);
        encabezado.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel titulo = new JLabel("REGISTRO DE USUARIO");
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(Color.WHITE);
        encabezado.add(titulo);

        // ── Panel central con el formulario ──
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(COLOR_FONDO);
        panelCentral.setBorder(new EmptyBorder(20, 40, 10, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 0, 4, 0);
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // Inicializamos los campos
        campoNombre   = crearCampo();
        campoApellido = crearCampo();
        campoUsuario  = crearCampo();
        campoTelefono = crearCampo();
        campoEmail    = crearCampo();
        campoPassword = crearCampoPassword();
        campoConfirmar = crearCampoPassword();

        // Agregamos cada campo al panel con su etiqueta
        int fila = 0;
        fila = agregarCampoAPanel(panelCentral, gbc, fila, "Nombre *",              campoNombre);
        fila = agregarCampoAPanel(panelCentral, gbc, fila, "Apellido *",             campoApellido);
        fila = agregarCampoAPanel(panelCentral, gbc, fila, "Nombre de Usuario *",    campoUsuario);
        fila = agregarCampoAPanel(panelCentral, gbc, fila, "Numero de Telefono *",   campoTelefono);
        fila = agregarCampoAPanel(panelCentral, gbc, fila, "Correo Electronico *",   campoEmail);
        fila = agregarCampoAPanel(panelCentral, gbc, fila, "Contrasena *",           campoPassword);
        fila = agregarCampoAPanel(panelCentral, gbc, fila, "Confirmar Contrasena *", campoConfirmar);

        // ── Panel inferior con los botones ──
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.setBackground(COLOR_FONDO);
        panelBotones.setBorder(new EmptyBorder(0, 0, 15, 0));

        JButton btnVolver = crearBoton("<- Volver");
        btnVolver.setBackground(new Color(90, 90, 110));
        btnVolver.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                btnVolver.setBackground(new Color(60, 60, 80));
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                btnVolver.setBackground(new Color(90, 90, 110));
            }
        });
        btnVolver.addActionListener(e -> volverAlLogin());

        JButton btnRegistrar = crearBoton("Registrar");
        btnRegistrar.addActionListener(e -> handleRegistro());

        panelBotones.add(btnVolver);
        panelBotones.add(btnRegistrar);

        add(encabezado,   BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Método auxiliar que agrega un label y su campo de texto al panel de registro.
     *
     * @param panel el panel donde agregar
     * @param gbc   el GridBagConstraints compartido
     * @param fila  fila actual del GridBagLayout
     * @param label texto de la etiqueta
     * @param campo el campo de texto o contraseña
     * @return la siguiente fila disponible
     */
    private int agregarCampoAPanel(JPanel panel, GridBagConstraints gbc,
                                    int fila, String label, JComponent campo) {
        gbc.gridy = fila++;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(new Color(80, 90, 120));
        panel.add(lbl, gbc);

        gbc.gridy = fila++;
        campo.setPreferredSize(new Dimension(0, 36));
        panel.add(campo, gbc);

        return fila;
    }

    /**
     * Maneja el evento de click en "Registrar".
     *
     * Validaciones:
     *  1. Todos los campos son obligatorios → indica cuál falta
     *  2. Las contraseñas deben coincidir
     *  3. El nombre de usuario no debe existir ya
     */
    private void handleRegistro() {
        String nombre    = campoNombre.getText().trim();
        String apellido  = campoApellido.getText().trim();
        String usuario   = campoUsuario.getText().trim();
        String telefono  = campoTelefono.getText().trim();
        String email     = campoEmail.getText().trim();
        String password  = new String(campoPassword.getPassword());
        String confirmar = new String(campoConfirmar.getPassword());

        // Validación 1: todos los campos son obligatorios
        if (nombre.isEmpty()) {
            mostrarError("El campo 'Nombre' es obligatorio.");
            campoNombre.requestFocus();
            return;
        }
        if (apellido.isEmpty()) {
            mostrarError("El campo 'Apellido' es obligatorio.");
            campoApellido.requestFocus();
            return;
        }
        if (usuario.isEmpty()) {
            mostrarError("El campo 'Nombre de Usuario' es obligatorio.");
            campoUsuario.requestFocus();
            return;
        }
        if (telefono.isEmpty()) {
            mostrarError("El campo 'Numero de Telefono' es obligatorio.");
            campoTelefono.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            mostrarError("El campo 'Correo Electronico' es obligatorio.");
            campoEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            mostrarError("El campo 'Contrasena' es obligatorio.");
            campoPassword.requestFocus();
            return;
        }
        if (confirmar.isEmpty()) {
            mostrarError("Debe confirmar la contrasena.");
            campoConfirmar.requestFocus();
            return;
        }

        // Validación 2: las contraseñas deben coincidir
        if (!password.equals(confirmar)) {
            mostrarError("La contrasena y la confirmacion no coinciden.\nVerifique e intente de nuevo.");
            campoConfirmar.setText("");
            campoPassword.setText("");
            campoPassword.requestFocus();
            return;
        }

        UsuarioDAO dao = new UsuarioDAO();

        // Validación 3: el nombre de usuario debe ser único
        if (dao.existeUsuario(usuario)) {
            mostrarError("El nombre de usuario '" + usuario + "' ya esta en uso.\nElija un nombre de usuario diferente.");
            campoUsuario.selectAll();
            campoUsuario.requestFocus();
            return;
        }

        // Todo OK → creamos el usuario y lo guardamos
        Usuario nuevoUsuario = new Usuario(usuario, nombre, apellido, telefono, email, password);

        if (dao.insertar(nuevoUsuario)) {
            mostrarExito("Usuario registrado exitosamente.\nYa puede iniciar sesion.");
            volverAlLogin();
        } else {
            mostrarError("No se pudo registrar el usuario.\nVerifique la conexion a la base de datos.");
        }
    }

    /**
     * Cierra esta ventana y muestra de nuevo el Login.
     */
    private void volverAlLogin() {
        dispose();
        loginFrame.setVisible(true);
    }
}
