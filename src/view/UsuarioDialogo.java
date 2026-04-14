package view;

import dao.UsuarioDAO;
import model.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * UsuarioDialogo.java
 * ====================
 * Diálogo modal para crear un nuevo usuario o editar uno existente.
 * Es llamado desde GestionUsuariosFrame al presionar NUEVO o ACTUALIZAR.
 *
 * PILAR POO: HERENCIA
 * ====================
 * Extiende JDialog (no VentanaBase, ya que es un diálogo modal, no un JFrame).
 * Reutiliza los métodos de utilidad pasando el frame padre como referencia.
 * Los colores y fuentes se acceden desde VentanaBase como constantes estáticas.
 *
 * Modo NUEVO: se abre sin usuario → todos los campos vacíos → botón "Registrar"
 * Modo EDITAR: se abre con un usuario → campos rellenos → botón "Guardar cambios"
 */
public class UsuarioDialogo extends JDialog {

    // El usuario a editar (null si es nuevo registro)
    private final Usuario usuarioExistente;

    // Referencia al frame padre para mostrar mensajes
    private final GestionUsuariosFrame padre;

    // Campos del formulario
    private JTextField     campoNombre;
    private JTextField     campoApellido;
    private JTextField     campoUsuario;
    private JTextField     campoTelefono;
    private JTextField     campoEmail;
    private JPasswordField campoPassword;
    private JPasswordField campoConfirmar;

    private final UsuarioDAO dao = new UsuarioDAO();

    /**
     * Constructor del diálogo.
     *
     * @param padre    frame padre (GestionUsuariosFrame)
     * @param usuario  usuario a editar, o null si es creación nueva
     */
    public UsuarioDialogo(GestionUsuariosFrame padre, Usuario usuario) {
        super(padre,
              usuario == null ? "Nuevo Usuario" : "Editar Usuario",
              true);   // true = modal (bloquea el padre mientras está abierto)
        this.padre = padre;
        this.usuarioExistente = usuario;

        setSize(480, 660);
        setLocationRelativeTo(padre);
        setResizable(false);
        getContentPane().setBackground(VentanaBase.COLOR_FONDO);

        construirUI();

        // Si hay un usuario, llenamos los campos con sus datos
        if (usuarioExistente != null) {
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
        String tituloText = usuarioExistente == null ? "NUEVO USUARIO" : "EDITAR USUARIO";
        JLabel titulo = new JLabel(tituloText);
        titulo.setFont(VentanaBase.FUENTE_SUBTITULO);
        titulo.setForeground(Color.WHITE);
        encabezado.add(titulo);

        // ── Formulario central ──
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(VentanaBase.COLOR_FONDO);
        panelForm.setBorder(new EmptyBorder(18, 30, 10, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 0, 3, 0);
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        campoNombre   = crearCampoEstilo();
        campoApellido = crearCampoEstilo();
        campoUsuario  = crearCampoEstilo();
        campoTelefono = crearCampoEstilo();
        campoEmail    = crearCampoEstilo();
        campoPassword = crearPasswordEstilo();
        campoConfirmar = crearPasswordEstilo();

        int fila = 0;
        fila = addField(panelForm, gbc, fila, "Nombre *",              campoNombre);
        fila = addField(panelForm, gbc, fila, "Apellido *",             campoApellido);
        fila = addField(panelForm, gbc, fila, "Nombre de Usuario *",    campoUsuario);
        fila = addField(panelForm, gbc, fila, "Telefono *",             campoTelefono);
        fila = addField(panelForm, gbc, fila, "Correo Electronico *",   campoEmail);
        fila = addField(panelForm, gbc, fila, "Contrasena *",           campoPassword);
        fila = addField(panelForm, gbc, fila, "Confirmar Contrasena *", campoConfirmar);

        // Nota: al editar, si no escribe nueva contraseña se mantiene la anterior
        if (usuarioExistente != null) {
            JLabel nota = new JLabel("  Deje la contrasena en blanco para mantener la actual.");
            nota.setFont(new Font("Segoe UI", Font.ITALIC, 10));
            nota.setForeground(new Color(120, 130, 160));
            gbc.gridy = fila++;
            panelForm.add(nota, gbc);
        }

        // ── Botones inferiores ──
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        panelBotones.setBackground(VentanaBase.COLOR_FONDO);
        panelBotones.setBorder(new EmptyBorder(0, 0, 10, 0));

        JButton btnCancelar = crearBotonDialog("Cancelar", new Color(90, 90, 110));
        btnCancelar.addActionListener(e -> dispose());

        String textoGuardar = usuarioExistente == null ? "Registrar" : "Guardar cambios";
        JButton btnGuardar = crearBotonDialog(textoGuardar, VentanaBase.COLOR_AZUL_MEDIO);
        btnGuardar.addActionListener(e -> handleGuardar());

        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        add(encabezado,   BorderLayout.NORTH);
        add(panelForm,    BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Rellena los campos del formulario con los datos del usuario existente.
     * Solo se llama en modo EDITAR.
     */
    private void rellenarCampos() {
        campoNombre.setText(usuarioExistente.getNombre());
        campoApellido.setText(usuarioExistente.getApellido());
        campoUsuario.setText(usuarioExistente.getUserName());
        campoTelefono.setText(usuarioExistente.getTelefono());
        campoEmail.setText(usuarioExistente.getEmail());
        // No llenamos la contraseña por seguridad
    }

    /**
     * Valida los campos y guarda el usuario (nuevo o actualizado).
     */
    private void handleGuardar() {
        String nombre    = campoNombre.getText().trim();
        String apellido  = campoApellido.getText().trim();
        String usuario   = campoUsuario.getText().trim();
        String telefono  = campoTelefono.getText().trim();
        String email     = campoEmail.getText().trim();
        String password  = new String(campoPassword.getPassword());
        String confirmar = new String(campoConfirmar.getPassword());

        // ── Validaciones ──
        if (nombre.isEmpty())   { showErr("El campo 'Nombre' es obligatorio.");            campoNombre.requestFocus();    return; }
        if (apellido.isEmpty()) { showErr("El campo 'Apellido' es obligatorio.");           campoApellido.requestFocus();  return; }
        if (usuario.isEmpty())  { showErr("El campo 'Nombre de Usuario' es obligatorio.");  campoUsuario.requestFocus();   return; }
        if (telefono.isEmpty()) { showErr("El campo 'Telefono' es obligatorio.");           campoTelefono.requestFocus(); return; }
        if (email.isEmpty())    { showErr("El campo 'Correo Electronico' es obligatorio."); campoEmail.requestFocus();    return; }

        // Si es nuevo usuario, la contraseña es obligatoria
        if (usuarioExistente == null && password.isEmpty()) {
            showErr("El campo 'Contrasena' es obligatorio.");
            campoPassword.requestFocus();
            return;
        }

        // Si escribió contraseña, debe confirmarla y coincidir
        if (!password.isEmpty() && !password.equals(confirmar)) {
            showErr("La contrasena y la confirmacion no coinciden.");
            campoConfirmar.setText("");
            campoPassword.setText("");
            campoPassword.requestFocus();
            return;
        }

        if (usuarioExistente == null) {
            // ── MODO NUEVO: crear usuario ──
            if (dao.existeUsuario(usuario)) {
                showErr("El nombre de usuario '" + usuario + "' ya esta en uso.");
                campoUsuario.selectAll();
                campoUsuario.requestFocus();
                return;
            }
            Usuario nuevo = new Usuario(usuario, nombre, apellido, telefono, email, password);
            if (dao.insertar(nuevo)) {
                showInfo("Usuario creado exitosamente.");
                dispose();
            } else {
                showErr("No se pudo crear el usuario.");
            }
        } else {
            // ── MODO EDITAR: actualizar usuario ──
            usuarioExistente.setNombre(nombre);
            usuarioExistente.setApellido(apellido);
            usuarioExistente.setUserName(usuario);
            usuarioExistente.setTelefono(telefono);
            usuarioExistente.setEmail(email);
            // Si la contraseña está vacía, conservamos la anterior
            if (!password.isEmpty()) {
                usuarioExistente.setPassword(password);
            }
            if (dao.actualizar(usuarioExistente)) {
                showInfo("Usuario actualizado correctamente.");
                dispose();
            } else {
                showErr("No se pudo actualizar el usuario.");
            }
        }
    }

    // ── Métodos de utilidad para este diálogo ──

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

    private JPasswordField crearPasswordEstilo() {
        JPasswordField f = new JPasswordField();
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
