package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * VentanaBase.java
 * =================
 * Clase base ABSTRACTA para todas las ventanas del sistema.
 *
 * PILAR POO: HERENCIA
 * ====================
 * Todas las ventanas extienden VentanaBase. Esto significa que heredan
 * automáticamente la paleta de colores, las fuentes y todos los métodos
 * de utilidad (crearBoton, crearCampo, estilizarTabla, mostrarError, etc.)
 * Sin herencia, habría que copiar este código en cada ventana.
 *
 * PILAR POO: ABSTRACCIÓN
 * =======================
 * La clase es abstract → no puede instanciarse directamente.
 * Solo existe para que otras clases la extiendan.
 * Representa el concepto abstracto de "una ventana del sistema de almacén".
 */
public abstract class VentanaBase extends JFrame {

    // ===== PALETA DE COLORES DEL SISTEMA (heredados por todas las ventanas) =====
    protected static final Color COLOR_AZUL_OSCURO   = new Color(26,  54,  93);   // Encabezados
    protected static final Color COLOR_AZUL_MEDIO    = new Color(44,  82,  130);  // Botones principales
    protected static final Color COLOR_AZUL_HOVER    = new Color(30,  60,  100);  // Botones al hover
    protected static final Color COLOR_AZUL_CLARO    = new Color(190, 210, 240);  // Bordes
    protected static final Color COLOR_FILA_PAR      = new Color(214, 226, 248);  // Fila par de tabla
    protected static final Color COLOR_FILA_IMPAR    = new Color(240, 245, 255);  // Fila impar de tabla
    protected static final Color COLOR_FONDO         = new Color(235, 242, 255);  // Fondo de ventanas
    protected static final Color COLOR_PANEL_BLANCO  = Color.WHITE;
    protected static final Color COLOR_TEXTO_OSCURO  = new Color(20,  20,  50);
    protected static final Color COLOR_ROJO          = new Color(190, 30,  30);
    protected static final Color COLOR_ROJO_HOVER    = new Color(140, 10,  10);

    // ===== FUENTES DEL SISTEMA (heredadas por todas las ventanas) =====
    protected static final Font FUENTE_TITULO      = new Font("Segoe UI", Font.BOLD,  22);
    protected static final Font FUENTE_SUBTITULO   = new Font("Segoe UI", Font.BOLD,  16);
    protected static final Font FUENTE_BOTON       = new Font("Segoe UI", Font.BOLD,  12);
    protected static final Font FUENTE_LABEL       = new Font("Segoe UI", Font.PLAIN, 12);
    protected static final Font FUENTE_LABEL_BOLD  = new Font("Segoe UI", Font.BOLD,  12);
    protected static final Font FUENTE_CAMPO       = new Font("Segoe UI", Font.PLAIN, 13);
    protected static final Font FUENTE_TABLA       = new Font("Segoe UI", Font.PLAIN, 12);
    protected static final Font FUENTE_ENCABEZADO  = new Font("Segoe UI", Font.BOLD,  12);

    // ===== CONSTRUCTOR BASE =====

    /**
     * Inicializa la ventana con título, dimensiones y configuración básica.
     * Todas las subclases llaman a este constructor con super().
     *
     * @param titulo título de la ventana
     * @param ancho  ancho en píxeles
     * @param alto   alto en píxeles
     */
    public VentanaBase(String titulo, int ancho, int alto) {
        super(titulo);
        setSize(ancho, alto);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);   // Centra en la pantalla
        setResizable(false);
        getContentPane().setBackground(COLOR_FONDO);
    }

    // ===== MÉTODOS DE UTILIDAD (heredados por todas las subclases) =====

    /**
     * Crea un botón con el estilo azul estándar del sistema.
     * Las subclases llaman esto para mantener consistencia visual.
     *
     * @param texto texto del botón
     * @return JButton estilizado y listo para usar
     */
    protected JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(COLOR_AZUL_MEDIO);
        btn.setForeground(Color.WHITE);
        btn.setFont(FUENTE_BOTON);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(9, 22, 9, 22));
        btn.setOpaque(true);

        // Efecto hover: oscurece el botón cuando el ratón pasa encima
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(COLOR_AZUL_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(COLOR_AZUL_MEDIO);
            }
        });

        return btn;
    }

    /**
     * Crea un botón rojo para acciones destructivas como "Eliminar".
     *
     * @param texto texto del botón
     * @return JButton rojo estilizado
     */
    protected JButton crearBotonPeligro(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(COLOR_ROJO);
        btn.setForeground(Color.WHITE);
        btn.setFont(FUENTE_BOTON);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(9, 22, 9, 22));
        btn.setOpaque(true);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(COLOR_ROJO_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(COLOR_ROJO);
            }
        });

        return btn;
    }

    /**
     * Crea un JTextField con el estilo estándar del sistema.
     *
     * @return JTextField estilizado
     */
    protected JTextField crearCampo() {
        JTextField campo = new JTextField();
        campo.setFont(FUENTE_CAMPO);
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_AZUL_CLARO, 1),
            new EmptyBorder(6, 10, 6, 10)
        ));
        campo.setBackground(COLOR_PANEL_BLANCO);
        return campo;
    }

    /**
     * Crea un JPasswordField (contraseña oculta) con el estilo estándar.
     * Al escribir se muestran puntos en lugar de los caracteres reales.
     *
     * @return JPasswordField estilizado
     */
    protected JPasswordField crearCampoPassword() {
        JPasswordField campo = new JPasswordField();
        campo.setFont(FUENTE_CAMPO);
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_AZUL_CLARO, 1),
            new EmptyBorder(6, 10, 6, 10)
        ));
        campo.setBackground(COLOR_PANEL_BLANCO);
        return campo;
    }

    /**
     * Crea un JLabel con el estilo estándar para etiquetas de formulario.
     *
     * @param texto texto del label
     * @return JLabel estilizado
     */
    protected JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(FUENTE_LABEL);
        label.setForeground(COLOR_TEXTO_OSCURO);
        return label;
    }

    /**
     * Aplica el estilo visual del sistema a una JTable (colores, fuentes, etc.)
     * Incluye colores alternados por fila para mejor legibilidad.
     *
     * @param tabla la JTable a estilizar
     */
    protected void estilizarTabla(JTable tabla) {
        tabla.setFont(FUENTE_TABLA);
        tabla.setRowHeight(30);
        tabla.setSelectionBackground(COLOR_AZUL_MEDIO);
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setGridColor(COLOR_AZUL_CLARO);
        tabla.setShowGrid(true);
        tabla.setIntercellSpacing(new Dimension(0, 1));
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Estilo del encabezado de la tabla
        // Se usa un renderer personalizado para garantizar que los colores
        // se apliquen sin importar el Look & Feel del sistema operativo.
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getTableHeader().setPreferredSize(new Dimension(0, 34));
        tabla.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        t, value, isSelected, hasFocus, row, col);
                label.setBackground(COLOR_AZUL_OSCURO);
                label.setForeground(Color.WHITE);
                label.setFont(FUENTE_ENCABEZADO);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setOpaque(true);
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, COLOR_AZUL_CLARO));
                return label;
            }
        });

        // Renderer que alterna colores de filas (par/impar)
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? COLOR_FILA_IMPAR : COLOR_FILA_PAR);
                    c.setForeground(COLOR_TEXTO_OSCURO);
                }
                setBorder(new EmptyBorder(0, 8, 0, 8));
                return c;
            }
        });
    }

    /**
     * Muestra un diálogo de error con mensaje personalizado.
     *
     * @param mensaje texto del error a mostrar
     */
    protected void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Muestra un diálogo de éxito/información con mensaje personalizado.
     *
     * @param mensaje texto informativo a mostrar
     */
    protected void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Exito", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Muestra un diálogo de confirmación (Sí / No).
     *
     * @param mensaje pregunta a mostrar al usuario
     * @return true si el usuario hizo clic en "Sí", false si canceló
     */
    protected boolean confirmar(String mensaje) {
        int respuesta = JOptionPane.showConfirmDialog(
            this, mensaje, "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
        );
        return respuesta == JOptionPane.YES_OPTION;
    }

    /**
     * Crea un panel separador horizontal con el color azul claro del sistema.
     * Se usa para dividir secciones visualmente.
     *
     * @return JPanel configurado como línea separadora
     */
    protected JPanel crearSeparador() {
        JPanel sep = new JPanel();
        sep.setBackground(COLOR_AZUL_CLARO);
        sep.setPreferredSize(new Dimension(0, 1));
        return sep;
    }
}
