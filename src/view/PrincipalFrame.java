package view;

import model.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * PrincipalFrame.java
 * ====================
 * Pantalla principal del sistema después de iniciar sesión exitosamente.
 * Muestra dos botones grandes: "Usuarios" y "Productos", y un botón
 * de "Cerrar Sesion" en la esquina superior derecha.
 *
 * PILAR POO: HERENCIA
 * ====================
 * Extiende VentanaBase, usando su paleta de colores y métodos de utilidad.
 */
public class PrincipalFrame extends VentanaBase {

    // El usuario que inició sesión — lo usamos para mostrar su nombre
    private final Usuario usuarioActivo;

    public PrincipalFrame(Usuario usuarioActivo) {
        super("Sistema de Almacen - Menu Principal", 620, 420);
        this.usuarioActivo = usuarioActivo;
        construirUI();
    }

    /**
     * Construye la interfaz principal con los dos botones de navegación.
     */
    private void construirUI() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_FONDO);

        // ── Barra superior con bienvenida y botón de cerrar sesión ──
        JPanel barraTop = new JPanel(new BorderLayout());
        barraTop.setBackground(COLOR_AZUL_OSCURO);
        barraTop.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel lblBienvenida = new JLabel(
            "Bienvenido, " + usuarioActivo.getNombre() + " " + usuarioActivo.getApellido()
        );
        lblBienvenida.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblBienvenida.setForeground(new Color(200, 215, 240));

        JLabel lblTitulo = new JLabel("  Sistema de Almacen");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);

        JButton btnCerrar = new JButton("Cerrar Sesion");
        btnCerrar.setBackground(new Color(160, 30, 30));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.setBorder(new EmptyBorder(7, 18, 7, 18));
        btnCerrar.addActionListener(e -> cerrarSesion());

        JPanel izquierdaTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        izquierdaTop.setBackground(COLOR_AZUL_OSCURO);
        izquierdaTop.add(lblTitulo);
        izquierdaTop.add(Box.createHorizontalStrut(10));

        JPanel derechaTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        derechaTop.setBackground(COLOR_AZUL_OSCURO);
        derechaTop.add(lblBienvenida);
        derechaTop.add(Box.createHorizontalStrut(15));
        derechaTop.add(btnCerrar);

        barraTop.add(izquierdaTop, BorderLayout.WEST);
        barraTop.add(derechaTop,   BorderLayout.EAST);

        // ── Panel central con los dos botones grandes ──
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(COLOR_FONDO);
        panelCentral.setBorder(new EmptyBorder(40, 60, 40, 60));

        // Subtítulo
        JLabel lblSubtitulo = new JLabel("Seleccione una opcion:");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(80, 100, 140));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        panelCentral.add(lblSubtitulo, gbc);

        // Botón Usuarios
        JButton btnUsuarios = crearBotonIcono(
            crearIconoUsuario(),
            "Usuarios",
            "Gestionar usuarios del sistema"
        );
        btnUsuarios.addActionListener(e -> abrirGestionUsuarios());

        // Botón Productos
        JButton btnProductos = crearBotonIcono(
            crearIconoProducto(),
            "Productos",
            "Gestionar productos del almacen"
        );
        btnProductos.addActionListener(e -> abrirGestionProductos());

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 0, 25);
        panelCentral.add(btnUsuarios, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 25, 0, 0);
        panelCentral.add(btnProductos, gbc);

        add(barraTop,     BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
    }

    /**
     * Crea un botón grande estilo "card" con un ícono dibujado, un título y una descripción.
     *
     * @param icono    imagen del ícono
     * @param titulo   texto principal del botón
     * @param subtexto descripción pequeña debajo del título
     * @return JButton estilizado tipo "card"
     */
    private JButton crearBotonIcono(ImageIcon icono, String titulo, String subtexto) {
        JButton btn = new JButton();
        btn.setLayout(new BoxLayout(btn, BoxLayout.Y_AXIS));
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_AZUL_CLARO, 2),
            new EmptyBorder(25, 35, 25, 35)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(190, 190));
        btn.setOpaque(true);

        JLabel lblIcono = new JLabel(icono);
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(COLOR_AZUL_OSCURO);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel(subtexto);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblSub.setForeground(new Color(130, 140, 170));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.add(lblIcono);
        btn.add(Box.createVerticalStrut(12));
        btn.add(lblTitulo);
        btn.add(Box.createVerticalStrut(4));
        btn.add(lblSub);

        // Efecto hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(COLOR_FILA_IMPAR);
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_AZUL_MEDIO, 2),
                    new EmptyBorder(25, 35, 25, 35)
                ));
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(Color.WHITE);
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_AZUL_CLARO, 2),
                    new EmptyBorder(25, 35, 25, 35)
                ));
            }
        });

        return btn;
    }

    /**
     * Dibuja un ícono de persona (cabeza + cuerpo) para el botón Usuarios.
     * Creado programáticamente con Graphics2D para no depender de archivos externos.
     */
    private ImageIcon crearIconoUsuario() {
        int tam = 60;
        BufferedImage img = new BufferedImage(tam, tam, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(COLOR_AZUL_MEDIO);
        g.setStroke(new BasicStroke(3.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        // Cabeza
        g.draw(new Ellipse2D.Float(20, 4, 20, 20));
        // Cuerpo / hombros
        g.drawArc(8, 36, 44, 28, 0, 180);

        g.dispose();
        return new ImageIcon(img);
    }

    /**
     * Dibuja un ícono de caja/paquete para el botón Productos.
     * Creado programáticamente con Graphics2D.
     */
    private ImageIcon crearIconoProducto() {
        int tam = 60;
        BufferedImage img = new BufferedImage(tam, tam, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(COLOR_AZUL_MEDIO);
        g.setStroke(new BasicStroke(3.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        // Cara frontal de la caja
        g.draw(new RoundRectangle2D.Float(10, 20, 40, 34, 4, 4));
        // Tapa superior de la caja
        g.drawLine(10, 20, 18, 10);
        g.drawLine(50, 20, 42, 10);
        g.drawLine(18, 10, 42, 10);
        // Línea central (fleco de la caja)
        g.drawLine(18, 20, 18, 54);

        // Flechas de recarga (ciclo) encima
        g.drawArc(20, 2, 20, 14, 30, 270);
        int[] arX = { 40, 36, 44 };
        int[] arY = {  9,  5,  5 };
        g.fillPolygon(arX, arY, 3);

        g.dispose();
        return new ImageIcon(img);
    }

    /**
     * Abre la pantalla de gestión de usuarios.
     * Oculta esta ventana mientras GestionUsuariosFrame está abierta.
     */
    private void abrirGestionUsuarios() {
        setVisible(false);
        new GestionUsuariosFrame(this).setVisible(true);
    }

    /**
     * Abre la pantalla de gestión de productos.
     * Oculta esta ventana mientras GestionProductosFrame está abierta.
     */
    private void abrirGestionProductos() {
        setVisible(false);
        new GestionProductosFrame(this).setVisible(true);
    }

    /**
     * Cierra la sesión: cierra esta ventana y muestra de nuevo el Login.
     */
    private void cerrarSesion() {
        dispose();
        new LoginFrame().setVisible(true);
    }
}
