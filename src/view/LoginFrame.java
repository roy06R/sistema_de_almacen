package view;

import dao.UsuarioDAO;
import model.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * LoginFrame.java
 * ================
 * Pantalla de inicio de sesión del sistema de almacén.
 *
 * PILAR POO: HERENCIA
 * ====================
 * Extiende VentanaBase, heredando colores, fuentes y métodos de utilidad.
 * Reutiliza crearCampoPassword(), crearBoton(), mostrarError() de la clase padre.
 *
 * Funcionalidades:
 *  - Campo de nombre de usuario
 *  - Campo de contraseña OCULTA (JPasswordField)
 *  - Botón "Entrar" → autentica con la base de datos
 *  - Enlace "Registrarse" → abre la pantalla de registro
 *  - Validación: campos vacíos muestran mensaje de error
 *  - Al autenticarse correctamente abre PrincipalFrame y cierra el login
 */
public class LoginFrame extends VentanaBase {

    private JTextField     campoUsuario;
    private JPasswordField campoPassword;

    public LoginFrame() {
        super("Sistema de Almacen - Login", 480, 420);
        construirUI();
    }

    /**
     * Construye todos los componentes de la interfaz del login.
     */
    private void construirUI() {
        setLayout(new GridBagLayout());
        getContentPane().setBackground(COLOR_FONDO);

        // ── Panel central blanco que contiene el formulario ──
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBackground(COLOR_PANEL_BLANCO);
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_AZUL_CLARO, 2),
            new EmptyBorder(35, 45, 35, 45)
        ));
        panelFormulario.setPreferredSize(new Dimension(320, 310));

        // Título "LOGIN"
        JLabel titulo = new JLabel("LOGIN");
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(COLOR_AZUL_OSCURO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(new EmptyBorder(0, 0, 25, 0));

        // Label + campo de nombre de usuario
        JLabel lblUsuario = crearLabel("Nombre de Usuario");
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblUsuario.setForeground(new Color(100, 110, 140));

        campoUsuario = crearCampo();
        campoUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        // Presionar Enter en el campo de usuario intenta el login
        campoUsuario.addActionListener(this::handleLogin);

        // Label + campo de contraseña (texto OCULTO)
        JLabel lblPassword = crearLabel("Contrasena");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblPassword.setForeground(new Color(100, 110, 140));
        lblPassword.setBorder(new EmptyBorder(12, 0, 0, 0));

        campoPassword = crearCampoPassword();   // JPasswordField → muestra puntos
        campoPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        // Presionar Enter en el campo de contraseña intenta el login
        campoPassword.addActionListener(this::handleLogin);

        // Botón "Entrar"
        JButton btnEntrar = crearBoton("Entrar");
        btnEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEntrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnEntrar.addActionListener(this::handleLogin);

        // Enlace "Registrarse" (botón sin borde, estilo link)
        JButton btnRegistrarse = new JButton("Registrarse");
        btnRegistrarse.setBackground(COLOR_PANEL_BLANCO);
        btnRegistrarse.setForeground(COLOR_AZUL_MEDIO);
        btnRegistrarse.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnRegistrarse.setBorderPainted(false);
        btnRegistrarse.setFocusPainted(false);
        btnRegistrarse.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegistrarse.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegistrarse.addActionListener(e -> abrirRegistro());

        // Ensamblamos el formulario
        panelFormulario.add(titulo);
        panelFormulario.add(lblUsuario);
        panelFormulario.add(Box.createVerticalStrut(4));
        panelFormulario.add(campoUsuario);
        panelFormulario.add(lblPassword);
        panelFormulario.add(Box.createVerticalStrut(4));
        panelFormulario.add(campoPassword);
        panelFormulario.add(Box.createVerticalStrut(20));
        panelFormulario.add(btnEntrar);
        panelFormulario.add(Box.createVerticalStrut(10));
        panelFormulario.add(btnRegistrarse);

        // Añadimos el panel al centro del frame
        add(panelFormulario);
    }

    /**
     * Maneja el evento de "Entrar" (clic en botón o Enter en campos).
     *
     * Requisitos cubiertos:
     *  - Si algún campo está vacío → muestra mensaje de error
     *  - Si las credenciales son incorrectas → muestra mensaje de error
     *  - Si el login es exitoso → cierra esta ventana, abre PrincipalFrame
     */
    private void handleLogin(ActionEvent e) {
        String usuario  = campoUsuario.getText().trim();
        String password = new String(campoPassword.getPassword());

        // Validación: campos obligatorios
        if (usuario.isEmpty() || password.isEmpty()) {
            mostrarError("Debe ingresar su usuario y contrasena.\nSi no esta registrado debe registrarse.");
            return;
        }

        // Autenticamos con la base de datos a través del DAO
        UsuarioDAO dao = new UsuarioDAO();
        Usuario usuarioAutenticado = dao.autenticar(usuario, password);

        if (usuarioAutenticado != null) {
            // Login exitoso: cerramos esta ventana y abrimos la pantalla principal
            dispose();
            new PrincipalFrame(usuarioAutenticado).setVisible(true);
        } else {
            // Credenciales incorrectas
            mostrarError("Usuario o contrasena incorrectos.\nVerifique sus datos e intente de nuevo.");
            campoPassword.setText("");
            campoPassword.requestFocus();
        }
    }

    /**
     * Abre la pantalla de registro y oculta el login mientras se registra.
     */
    private void abrirRegistro() {
        setVisible(false);
        RegistroFrame registro = new RegistroFrame(this);
        registro.setVisible(true);
    }
}
