package vista;

// Controlador que conecta la vista con la lógica del sistema.
import controlador.InventarioControlador;

// Clases del modelo.
import modelo.ProductoInventario;
import modelo.StockException;

// Componentes de Swing para construir la interfaz gráfica.
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.DefaultCellEditor;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

// Clases para personalizar la tabla.
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

// Clases de AWT para diseño, colores, fuentes y componentes.
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

// SQLException permite manejar errores de base de datos.
import java.sql.SQLException;

// ArrayList permite manejar listas dinámicas de productos.
import java.util.ArrayList;

/**
 * Clase principal de la interfaz gráfica del sistema.
 *
 * Esta clase representa la Vista dentro del patrón MVC.
 *
 * Desde esta ventana el usuario puede:
 * - agregar ingredientes;
 * - registrar ingresos de stock;
 * - registrar egresos de stock;
 * - consultar ingredientes esenciales con bajo stock;
 * - generar órdenes de compra;
 * - eliminar ingredientes.
 */
public class InventarioVista extends JFrame {

    /*
     * Controlador utilizado por la vista para ejecutar acciones del sistema.
     */
    private InventarioControlador controlador;

    /*
     * Campos del formulario de carga de ingredientes.
     */
    private JTextField txtNombre;
    private JTextField txtCantidad;
    private JTextField txtStockMinimo;
    private JTextField txtMotivo;

    /*
     * Combos de selección.
     */
    private JComboBox<String> comboUnidad;
    private JComboBox<String> comboCategoria;

    /*
     * Checkbox para indicar si un ingrediente es esencial.
     */
    private JCheckBox chkEsencial;

    /*
     * Tabla donde se muestra el inventario.
     */
    private JTable tabla;

    /*
     * Modelo de la tabla.
     */
    private DefaultTableModel modeloTabla;

    /*
     * Panel de botones laterales.
     */
    private JPanel panelBotones;

    /*
     * Botón Volver, usado cuando se muestra la vista de bajo stock.
     */
    private JButton btnVolver;

    /*
     * Arreglo de unidades de medida disponibles.
     */
    private final String[] unidadesMedida = {"kg", "unidad", "litro", "gramos", "ml"};

    /*
     * Arreglo de categorías disponibles.
     */
    private final String[] categorias = {"Secos", "Frescos", "Lácteos", "Carnes", "Verduras", "Otros"};

    /*
     * Paleta de colores utilizada en la interfaz.
     */
    private final Color COLOR_FONDO = new Color(245, 247, 250);
    private final Color COLOR_HEADER = new Color(39, 55, 77);
    private final Color COLOR_PANEL = Color.WHITE;
    private final Color COLOR_BORDE = new Color(220, 225, 230);
    private final Color COLOR_TEXTO = new Color(33, 37, 41);
    private final Color COLOR_AZUL = new Color(52, 152, 219);
    private final Color COLOR_VERDE = new Color(39, 174, 96);
    private final Color COLOR_NARANJA = new Color(243, 156, 18);
    private final Color COLOR_ROJO = new Color(231, 76, 60);
    private final Color COLOR_GRIS = new Color(108, 117, 125);

    /**
     * Constructor de la ventana principal.
     */
    public InventarioVista() {
        controlador = new InventarioControlador();

        setTitle("Sistema de Gestión de Stock - Escuela de Chefs Mariano Moreno");
        setMinimumSize(new Dimension(1100, 720));
        setSize(1200, 760);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /*
         * La ventana se abre maximizada.
         */
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        inicializarComponentes();
        cargarInventario();
    }

    /**
     * Inicializa la estructura general de la ventana.
     */
    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(COLOR_FONDO);

        panelPrincipal.add(crearHeader(), BorderLayout.NORTH);
        panelPrincipal.add(crearContenido(), BorderLayout.CENTER);

        add(panelPrincipal);
    }

    /**
     * Crea el encabezado superior del sistema.
     */
    private JPanel crearHeader() {
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(COLOR_HEADER);
        panelHeader.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));

        JLabel lblLogo = new JLabel("MM");
        lblLogo.setOpaque(true);
        lblLogo.setBackground(new Color(255, 152, 0));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblLogo.setPreferredSize(new Dimension(54, 54));

        JLabel lblTitulo = new JLabel("Sistema de Gestión de Stock");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));

        JLabel lblSubtitulo = new JLabel("Escuela de Chefs Mariano Moreno | Gestión de ingredientes esenciales y órdenes de compra");
        lblSubtitulo.setForeground(new Color(220, 230, 240));
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JPanel panelTexto = new JPanel();
        panelTexto.setOpaque(false);
        panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));
        panelTexto.add(lblTitulo);
        panelTexto.add(Box.createVerticalStrut(5));
        panelTexto.add(lblSubtitulo);

        JPanel panelIzquierdo = new JPanel(new BorderLayout(16, 0));
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.add(lblLogo, BorderLayout.WEST);
        panelIzquierdo.add(panelTexto, BorderLayout.CENTER);

        panelHeader.add(panelIzquierdo, BorderLayout.WEST);

        return panelHeader;
    }

    /**
     * Crea el contenido central de la pantalla.
     */
    private JPanel crearContenido() {
        JPanel panelContenido = new JPanel(new BorderLayout(15, 15));
        panelContenido.setBackground(COLOR_FONDO);
        panelContenido.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panelContenido.add(crearPanelLateral(), BorderLayout.WEST);
        panelContenido.add(crearPanelTabla(), BorderLayout.CENTER);

        return panelContenido;
    }

    /**
     * Crea el panel lateral izquierdo.
     */
    private JPanel crearPanelLateral() {
        JPanel panelLateral = new JPanel(new BorderLayout(0, 12));
        panelLateral.setBackground(COLOR_FONDO);
        panelLateral.setPreferredSize(new Dimension(340, 0));

        panelLateral.add(crearPanelFormulario(), BorderLayout.NORTH);
        panelLateral.add(crearPanelBotones(), BorderLayout.CENTER);

        return panelLateral;
    }

    /**
     * Crea el formulario para cargar ingredientes.
     */
    private JPanel crearPanelFormulario() {
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBackground(COLOR_PANEL);
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));

        JLabel lblTitulo = new JLabel("Datos del ingrediente");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 17));
        lblTitulo.setForeground(COLOR_HEADER);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtNombre = new JTextField();
        txtCantidad = new JTextField();
        txtStockMinimo = new JTextField();
        txtMotivo = new JTextField();

        /*
         * Nueva regla:
         * Por defecto, un ingrediente general no requiere stock mínimo.
         * Por eso el campo inicia en 0 y deshabilitado.
         */
        txtStockMinimo.setText("0");
        txtStockMinimo.setEnabled(false);

        comboUnidad = new JComboBox<>(unidadesMedida);
        comboCategoria = new JComboBox<>(categorias);
        chkEsencial = new JCheckBox("Ingrediente esencial");

        chkEsencial.setBackground(COLOR_PANEL);
        chkEsencial.setFont(new Font("SansSerif", Font.PLAIN, 13));
        chkEsencial.setAlignmentX(Component.LEFT_ALIGNMENT);

        /*
         * El motivo esencial también inicia deshabilitado.
         */
        txtMotivo.setEnabled(false);

        /*
         * Evento del checkbox Ingrediente esencial.
         *
         * Si se marca:
         * - se habilita el stock mínimo;
         * - se habilita el motivo esencial.
         *
         * Si se desmarca:
         * - el stock mínimo vuelve a 0;
         * - el motivo se limpia;
         * - ambos campos se deshabilitan.
         */
        chkEsencial.addActionListener(e -> {
            boolean seleccionado = chkEsencial.isSelected();

            txtMotivo.setEnabled(seleccionado);
            txtStockMinimo.setEnabled(seleccionado);

            if (seleccionado) {
                txtStockMinimo.setText("");
            } else {
                txtStockMinimo.setText("0");
                txtMotivo.setText("");
            }
        });

        estilizarCampo(txtNombre);
        estilizarCampo(txtCantidad);
        estilizarCampo(txtStockMinimo);
        estilizarCampo(txtMotivo);
        estilizarCombo(comboUnidad);
        estilizarCombo(comboCategoria);

        panelFormulario.add(lblTitulo);
        panelFormulario.add(Box.createVerticalStrut(15));

        agregarCampoVertical(panelFormulario, "Nombre:", txtNombre);
        agregarCampoVertical(panelFormulario, "Unidad de medida:", comboUnidad);
        agregarCampoVertical(panelFormulario, "Cantidad:", txtCantidad);
        agregarCampoVertical(panelFormulario, "Stock mínimo (solo esenciales):", txtStockMinimo);
        agregarCampoVertical(panelFormulario, "Categoría:", comboCategoria);

        panelFormulario.add(crearLabel("Tipo:"));
        panelFormulario.add(Box.createVerticalStrut(5));
        panelFormulario.add(chkEsencial);
        panelFormulario.add(Box.createVerticalStrut(12));

        agregarCampoVertical(panelFormulario, "Motivo esencial:", txtMotivo);

        return panelFormulario;
    }

    /**
     * Crea el panel de botones principales.
     */
    private JPanel crearPanelBotones() {
        panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(0, 1, 0, 10));
        panelBotones.setBackground(COLOR_PANEL);
        panelBotones.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));

        JButton btnAgregar = crearBoton("Agregar ingrediente", COLOR_AZUL);
        JButton btnIngreso = crearBoton("Registrar ingreso", COLOR_VERDE);
        JButton btnEgreso = crearBoton("Registrar egreso", COLOR_ROJO);
        JButton btnBajoStock = crearBoton("Ver bajo stock", COLOR_NARANJA);

        /*
         * El botón Volver se crea, pero se agrega al panel solamente
         * cuando el usuario entra a la vista de bajo stock.
         */
        btnVolver = crearBoton("Volver", COLOR_HEADER);

        JButton btnOrdenCompra = crearBoton("Generar orden", new Color(22, 160, 133));
        JButton btnLimpiar = crearBoton("Limpiar campos", COLOR_GRIS);
        JButton btnSalir = crearBoton("Salir", new Color(44, 62, 80));

        panelBotones.add(btnAgregar);
        panelBotones.add(btnIngreso);
        panelBotones.add(btnEgreso);
        panelBotones.add(btnBajoStock);
        panelBotones.add(btnOrdenCompra);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnSalir);

        btnAgregar.addActionListener(e -> agregarIngrediente());
        btnIngreso.addActionListener(e -> registrarIngreso());
        btnEgreso.addActionListener(e -> registrarEgreso());
        btnBajoStock.addActionListener(e -> cargarBajoStock());
        btnVolver.addActionListener(e -> cargarInventario());
        btnOrdenCompra.addActionListener(e -> generarOrdenCompra());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnSalir.addActionListener(e -> System.exit(0));

        return panelBotones;
    }

    /**
     * Crea la tabla del inventario.
     */
    private JPanel crearPanelTabla() {
        JPanel panelTabla = new JPanel(new BorderLayout(0, 10));
        panelTabla.setBackground(COLOR_PANEL);
        panelTabla.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));

        JLabel lblTabla = new JLabel("Inventario actual");
        lblTabla.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTabla.setForeground(COLOR_HEADER);

        /*
         * Modelo de tabla.
         * Solo la columna Eliminar es editable porque allí se usa un botón.
         */
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };

        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Tipo");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Unidad");
        modeloTabla.addColumn("Cantidad");
        modeloTabla.addColumn("Stock mínimo");
        modeloTabla.addColumn("Estado");
        modeloTabla.addColumn("Eliminar");

        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(32);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabla.setSelectionBackground(new Color(214, 234, 248));
        tabla.setSelectionForeground(Color.BLACK);
        tabla.setGridColor(new Color(235, 238, 240));
        tabla.setShowVerticalLines(false);
        tabla.setAutoCreateRowSorter(true);
        tabla.setFillsViewportHeight(true);

        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBackground(new Color(236, 240, 241));
        header.setForeground(COLOR_TEXTO);
        header.setReorderingAllowed(false);

        /*
         * Renderer para pintar las filas según el estado del stock.
         */
        tabla.setDefaultRenderer(Object.class, new RenderEstadoStock());

        /*
         * Renderer y editor para mostrar el botón X en la columna Eliminar.
         */
        tabla.getColumnModel().getColumn(7).setCellRenderer(new BotonEliminarRenderer());
        tabla.getColumnModel().getColumn(7).setCellEditor(new BotonEliminarEditor());
        tabla.getColumnModel().getColumn(7).setMaxWidth(90);
        tabla.getColumnModel().getColumn(7).setMinWidth(80);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDE));

        panelTabla.add(lblTabla, BorderLayout.NORTH);
        panelTabla.add(scroll, BorderLayout.CENTER);

        return panelTabla;
    }

    /**
     * Agrega un campo al formulario con su etiqueta.
     */
    private void agregarCampoVertical(JPanel panel, String textoLabel, Component componente) {
        JLabel label = crearLabel(textoLabel);

        panel.add(label);
        panel.add(Box.createVerticalStrut(5));

        if (componente instanceof JTextField) {
            ((JTextField) componente).setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        }

        if (componente instanceof JComboBox) {
            ((JComboBox<?>) componente).setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        }

        panel.add(componente);
        panel.add(Box.createVerticalStrut(12));
    }

    /**
     * Crea etiquetas con estilo uniforme.
     */
    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(COLOR_TEXTO);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    /**
     * Aplica estilo a un campo de texto.
     */
    private void estilizarCampo(JTextField campo) {
        campo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        campo.setPreferredSize(new Dimension(250, 34));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    /**
     * Aplica estilo a un combo.
     */
    private void estilizarCombo(JComboBox<String> combo) {
        combo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        combo.setPreferredSize(new Dimension(250, 34));
        combo.setBackground(Color.WHITE);
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    /**
     * Crea botones con estilo uniforme.
     */
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);

        boton.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        boton.setFont(new Font("SansSerif", Font.BOLD, 13));
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setOpaque(true);
        boton.setContentAreaFilled(true);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setHorizontalAlignment(SwingConstants.CENTER);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker()),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));

        /*
         * Efecto visual al pasar el mouse.
         */
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setBackground(color.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setBackground(color);
            }
        });

        return boton;
    }

    /**
     * Muestra u oculta el botón Volver.
     */
    private void mostrarBotonVolver(boolean mostrar) {
        if (mostrar) {
            if (btnVolver.getParent() == null) {
                panelBotones.add(btnVolver, 4);
            }
        } else {
            if (btnVolver.getParent() != null) {
                panelBotones.remove(btnVolver);
            }
        }

        panelBotones.revalidate();
        panelBotones.repaint();
    }

    /**
     * Agrega un ingrediente nuevo.
     */
    private void agregarIngrediente() {
        try {
            String nombre = txtNombre.getText();
            String unidad = comboUnidad.getSelectedItem().toString();
            double cantidad = Double.parseDouble(txtCantidad.getText().replace(",", "."));
            String categoria = comboCategoria.getSelectedItem().toString();
            boolean esEsencial = chkEsencial.isSelected();
            String motivo = txtMotivo.getText();

            double stockMinimo;

            /*
             * Si es esencial, se toma el stock mínimo ingresado.
             * Si no es esencial, se guarda automáticamente en 0.
             */
            if (esEsencial) {
                stockMinimo = Double.parseDouble(txtStockMinimo.getText().replace(",", "."));
            } else {
                stockMinimo = 0;
            }

            controlador.agregarIngrediente(nombre, unidad, cantidad, stockMinimo, categoria, esEsencial, motivo);

            JOptionPane.showMessageDialog(this, "Ingrediente agregado correctamente.");
            limpiarCampos();
            cargarInventario();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Debe ingresar valores numéricos válidos en cantidad y stock mínimo.");
        } catch (SQLException | StockException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    /**
     * Abre una ventana para registrar ingresos de stock.
     */
    private void registrarIngreso() {
        try {
            ArrayList<ProductoInventario> productos = controlador.listarInventario();

            if (productos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay ingredientes registrados para realizar ingresos.");
                return;
            }

            JDialog dialogo = new JDialog(this, "Registrar ingreso de stock", true);
            dialogo.setSize(520, 300);
            dialogo.setLocationRelativeTo(this);
            dialogo.setLayout(new BorderLayout(12, 12));

            JPanel panelPrincipal = new JPanel();
            panelPrincipal.setBackground(COLOR_PANEL);
            panelPrincipal.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
            panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));

            JLabel lblTitulo = new JLabel("Registrar ingreso de ingrediente");
            lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
            lblTitulo.setForeground(COLOR_HEADER);
            lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel lblDescripcion = new JLabel("Seleccione el ingrediente e indique la cantidad que ingresó al stock.");
            lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 13));
            lblDescripcion.setForeground(COLOR_TEXTO);
            lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

            JComboBox<String> comboIngredientes = new JComboBox<>();

            /*
             * Se cargan todos los ingredientes disponibles en el combo.
             */
            for (ProductoInventario producto : productos) {
                comboIngredientes.addItem(
                        producto.getId() + " - " + producto.getNombre()
                                + " | Stock actual: " + producto.getCantidadDisponible() + " " + producto.getUnidadMedida()
                );
            }

            JTextField txtCantidadIngreso = new JTextField();

            JLabel lblStockActual = new JLabel();
            lblStockActual.setFont(new Font("SansSerif", Font.BOLD, 13));
            lblStockActual.setForeground(COLOR_HEADER);
            lblStockActual.setAlignmentX(Component.LEFT_ALIGNMENT);

            /*
             * Actualiza el texto del stock actual cuando cambia la selección.
             */
            comboIngredientes.addActionListener(e -> {
                int indice = comboIngredientes.getSelectedIndex();

                if (indice >= 0) {
                    ProductoInventario seleccionado = productos.get(indice);
                    lblStockActual.setText(
                            "Stock actual: " + seleccionado.getCantidadDisponible()
                                    + " " + seleccionado.getUnidadMedida()
                    );
                }
            });

            /*
             * Se muestra el stock del primer ingrediente por defecto.
             */
            if (!productos.isEmpty()) {
                ProductoInventario primero = productos.get(0);
                lblStockActual.setText(
                        "Stock actual: " + primero.getCantidadDisponible()
                                + " " + primero.getUnidadMedida()
                );
            }

            estilizarCombo(comboIngredientes);
            estilizarCampo(txtCantidadIngreso);

            JPanel panelBotonesDialogo = new JPanel(new GridLayout(1, 2, 10, 0));
            panelBotonesDialogo.setBackground(COLOR_PANEL);

            JButton btnConfirmar = crearBoton("Confirmar ingreso", COLOR_VERDE);
            JButton btnCancelar = crearBoton("Cancelar", COLOR_GRIS);

            panelBotonesDialogo.add(btnConfirmar);
            panelBotonesDialogo.add(btnCancelar);

            panelPrincipal.add(lblTitulo);
            panelPrincipal.add(Box.createVerticalStrut(6));
            panelPrincipal.add(lblDescripcion);
            panelPrincipal.add(Box.createVerticalStrut(18));

            panelPrincipal.add(crearLabel("Ingrediente:"));
            panelPrincipal.add(Box.createVerticalStrut(5));
            comboIngredientes.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
            panelPrincipal.add(comboIngredientes);
            panelPrincipal.add(Box.createVerticalStrut(12));

            panelPrincipal.add(lblStockActual);
            panelPrincipal.add(Box.createVerticalStrut(12));

            panelPrincipal.add(crearLabel("Cantidad a ingresar:"));
            panelPrincipal.add(Box.createVerticalStrut(5));
            txtCantidadIngreso.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
            panelPrincipal.add(txtCantidadIngreso);
            panelPrincipal.add(Box.createVerticalStrut(18));

            panelPrincipal.add(panelBotonesDialogo);

            dialogo.add(panelPrincipal, BorderLayout.CENTER);

            btnCancelar.addActionListener(e -> dialogo.dispose());

            /*
             * Acción para confirmar el ingreso.
             */
            btnConfirmar.addActionListener(e -> {
                try {
                    int indice = comboIngredientes.getSelectedIndex();

                    if (indice < 0) {
                        JOptionPane.showMessageDialog(dialogo, "Debe seleccionar un ingrediente.");
                        return;
                    }

                    ProductoInventario productoSeleccionado = productos.get(indice);
                    double cantidadIngreso = Double.parseDouble(txtCantidadIngreso.getText().replace(",", "."));

                    if (cantidadIngreso <= 0) {
                        JOptionPane.showMessageDialog(dialogo, "La cantidad a ingresar debe ser mayor a cero.");
                        return;
                    }

                    controlador.registrarIngreso(productoSeleccionado.getId(), cantidadIngreso);

                    JOptionPane.showMessageDialog(dialogo, "Ingreso registrado correctamente.");
                    cargarInventario();
                    dialogo.dispose();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialogo, "Debe ingresar una cantidad numérica válida.");
                } catch (SQLException | StockException ex) {
                    JOptionPane.showMessageDialog(dialogo, "Error: " + ex.getMessage());
                }
            });

            dialogo.setVisible(true);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar ingredientes: " + e.getMessage());
        }
    }

    /**
     * Abre una ventana para registrar egresos de stock.
     */
    private void registrarEgreso() {
        try {
            ArrayList<ProductoInventario> productos = controlador.listarInventario();

            if (productos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay ingredientes registrados para realizar egresos.");
                return;
            }

            JDialog dialogo = new JDialog(this, "Registrar egreso de stock", true);
            dialogo.setSize(520, 320);
            dialogo.setLocationRelativeTo(this);
            dialogo.setLayout(new BorderLayout(12, 12));

            JPanel panelPrincipal = new JPanel();
            panelPrincipal.setBackground(COLOR_PANEL);
            panelPrincipal.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
            panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));

            JLabel lblTitulo = new JLabel("Registrar egreso de ingrediente");
            lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
            lblTitulo.setForeground(COLOR_HEADER);
            lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel lblDescripcion = new JLabel("Seleccione el ingrediente e indique la cantidad que se utilizará.");
            lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 13));
            lblDescripcion.setForeground(COLOR_TEXTO);
            lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

            JComboBox<String> comboIngredientes = new JComboBox<>();

            /*
             * Se cargan los ingredientes en el combo.
             * Para ingredientes generales, el stock mínimo se muestra como No aplica.
             */
            for (ProductoInventario producto : productos) {
                String minimoTexto;

                if ("Ingrediente esencial".equalsIgnoreCase(producto.getTipoProducto())) {
                    minimoTexto = String.valueOf(producto.getStockMinimo());
                } else {
                    minimoTexto = "No aplica";
                }

                comboIngredientes.addItem(
                        producto.getId() + " - " + producto.getNombre()
                                + " | Stock: " + producto.getCantidadDisponible() + " " + producto.getUnidadMedida()
                                + " | Mínimo: " + minimoTexto
                );
            }

            JTextField txtCantidadEgreso = new JTextField();

            JLabel lblStockDisponible = new JLabel();
            lblStockDisponible.setFont(new Font("SansSerif", Font.BOLD, 13));
            lblStockDisponible.setForeground(COLOR_HEADER);
            lblStockDisponible.setAlignmentX(Component.LEFT_ALIGNMENT);

            /*
             * Actualiza la información mostrada cuando cambia el ingrediente.
             */
            comboIngredientes.addActionListener(e -> {
                int indice = comboIngredientes.getSelectedIndex();

                if (indice >= 0) {
                    ProductoInventario seleccionado = productos.get(indice);

                    String minimoTexto;

                    if ("Ingrediente esencial".equalsIgnoreCase(seleccionado.getTipoProducto())) {
                        minimoTexto = String.valueOf(seleccionado.getStockMinimo());
                    } else {
                        minimoTexto = "No aplica";
                    }

                    lblStockDisponible.setText(
                            "Stock disponible: " + seleccionado.getCantidadDisponible()
                                    + " " + seleccionado.getUnidadMedida()
                                    + " | Stock mínimo: " + minimoTexto
                    );
                }
            });

            /*
             * Se inicializa el texto del stock con el primer ingrediente.
             */
            if (!productos.isEmpty()) {
                ProductoInventario primero = productos.get(0);

                String minimoTexto;

                if ("Ingrediente esencial".equalsIgnoreCase(primero.getTipoProducto())) {
                    minimoTexto = String.valueOf(primero.getStockMinimo());
                } else {
                    minimoTexto = "No aplica";
                }

                lblStockDisponible.setText(
                        "Stock disponible: " + primero.getCantidadDisponible()
                                + " " + primero.getUnidadMedida()
                                + " | Stock mínimo: " + minimoTexto
                );
            }

            estilizarCombo(comboIngredientes);
            estilizarCampo(txtCantidadEgreso);

            JPanel panelBotonesDialogo = new JPanel(new GridLayout(1, 2, 10, 0));
            panelBotonesDialogo.setBackground(COLOR_PANEL);

            JButton btnConfirmar = crearBoton("Confirmar egreso", COLOR_ROJO);
            JButton btnCancelar = crearBoton("Cancelar", COLOR_GRIS);

            panelBotonesDialogo.add(btnConfirmar);
            panelBotonesDialogo.add(btnCancelar);

            panelPrincipal.add(lblTitulo);
            panelPrincipal.add(Box.createVerticalStrut(6));
            panelPrincipal.add(lblDescripcion);
            panelPrincipal.add(Box.createVerticalStrut(18));

            panelPrincipal.add(crearLabel("Ingrediente:"));
            panelPrincipal.add(Box.createVerticalStrut(5));
            comboIngredientes.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
            panelPrincipal.add(comboIngredientes);
            panelPrincipal.add(Box.createVerticalStrut(12));

            panelPrincipal.add(lblStockDisponible);
            panelPrincipal.add(Box.createVerticalStrut(12));

            panelPrincipal.add(crearLabel("Cantidad a egresar:"));
            panelPrincipal.add(Box.createVerticalStrut(5));
            txtCantidadEgreso.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
            panelPrincipal.add(txtCantidadEgreso);
            panelPrincipal.add(Box.createVerticalStrut(18));

            panelPrincipal.add(panelBotonesDialogo);

            dialogo.add(panelPrincipal, BorderLayout.CENTER);

            btnCancelar.addActionListener(e -> dialogo.dispose());

            /*
             * Acción para confirmar el egreso.
             */
            btnConfirmar.addActionListener(e -> {
                try {
                    int indice = comboIngredientes.getSelectedIndex();

                    if (indice < 0) {
                        JOptionPane.showMessageDialog(dialogo, "Debe seleccionar un ingrediente.");
                        return;
                    }

                    ProductoInventario productoSeleccionado = productos.get(indice);
                    double cantidadEgreso = Double.parseDouble(txtCantidadEgreso.getText().replace(",", "."));

                    if (cantidadEgreso <= 0) {
                        JOptionPane.showMessageDialog(dialogo, "La cantidad a egresar debe ser mayor a cero.");
                        return;
                    }

                    boolean esEsencial = "Ingrediente esencial".equalsIgnoreCase(productoSeleccionado.getTipoProducto());

                    /*
                     * Si se intenta egresar más de lo disponible,
                     * el sistema analiza si el ingrediente es esencial o no.
                     */
                    if (cantidadEgreso > productoSeleccionado.getCantidadDisponible()) {

                        /*
                         * Si no es esencial, no se actualiza stock mínimo
                         * ni se genera futura orden de compra.
                         */
                        if (!esEsencial) {
                            JOptionPane.showMessageDialog(
                                    dialogo,
                                    "No hay stock suficiente para realizar el egreso.\n\n"
                                            + "Ingrediente: " + productoSeleccionado.getNombre() + "\n"
                                            + "Stock disponible: " + productoSeleccionado.getCantidadDisponible() + " " + productoSeleccionado.getUnidadMedida() + "\n"
                                            + "Cantidad solicitada: " + cantidadEgreso + " " + productoSeleccionado.getUnidadMedida() + "\n\n"
                                            + "Como este ingrediente no es esencial, no se actualizará stock mínimo ni se incluirá en orden de compra."
                            );

                            return;
                        }

                        /*
                         * Si es esencial, se ofrece actualizar el stock mínimo
                         * para que aparezca en la próxima orden de compra.
                         */
                        int respuesta = JOptionPane.showConfirmDialog(
                                dialogo,
                                "La cantidad solicitada supera el stock disponible.\n\n"
                                        + "Ingrediente: " + productoSeleccionado.getNombre() + "\n"
                                        + "Stock disponible: " + productoSeleccionado.getCantidadDisponible() + " " + productoSeleccionado.getUnidadMedida() + "\n"
                                        + "Cantidad solicitada: " + cantidadEgreso + " " + productoSeleccionado.getUnidadMedida() + "\n\n"
                                        + "¿Desea actualizar el stock mínimo a " + cantidadEgreso + " para que este ingrediente sea incluido en la próxima orden de compra?",
                                "Stock insuficiente",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE
                        );

                        if (respuesta == JOptionPane.YES_OPTION) {
                            controlador.actualizarStockMinimo(productoSeleccionado.getId(), cantidadEgreso);

                            JOptionPane.showMessageDialog(
                                    dialogo,
                                    "No se registró el egreso porque no hay stock suficiente.\n"
                                            + "Se actualizó el stock mínimo del ingrediente esencial.\n"
                                            + "Ahora quedará marcado para la próxima orden de compra."
                            );

                            cargarInventario();
                            dialogo.dispose();
                        }

                        return;
                    }

                    /*
                     * Si hay stock suficiente, se registra normalmente el egreso.
                     */
                    controlador.registrarEgreso(productoSeleccionado.getId(), cantidadEgreso);

                    JOptionPane.showMessageDialog(dialogo, "Egreso registrado correctamente.");
                    cargarInventario();
                    dialogo.dispose();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialogo, "Debe ingresar una cantidad numérica válida.");
                } catch (SQLException | StockException ex) {
                    JOptionPane.showMessageDialog(dialogo, "Error: " + ex.getMessage());
                }
            });

            dialogo.setVisible(true);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar ingredientes: " + e.getMessage());
        }
    }

    /**
     * Carga el inventario completo.
     */
    private void cargarInventario() {
        try {
            ArrayList<ProductoInventario> productos = controlador.listarInventario();
            cargarTabla(productos);
            mostrarBotonVolver(false);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al consultar la base de datos: " + e.getMessage());
        }
    }

    /**
     * Carga solamente los ingredientes esenciales con bajo stock.
     */
    private void cargarBajoStock() {
        try {
            ArrayList<ProductoInventario> productos = controlador.listarBajoStock();
            cargarTabla(productos);
            mostrarBotonVolver(true);

            if (productos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay ingredientes esenciales con bajo stock.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al consultar bajo stock: " + e.getMessage());
        }
    }

    /**
     * Carga los productos recibidos en la tabla.
     */
    private void cargarTabla(ArrayList<ProductoInventario> productos) {
        modeloTabla.setRowCount(0);

        for (ProductoInventario producto : productos) {
            Object stockMinimoTabla;

            /*
             * Para ingredientes esenciales se muestra el stock mínimo.
             * Para ingredientes generales se muestra "No aplica".
             */
            if ("Ingrediente esencial".equalsIgnoreCase(producto.getTipoProducto())) {
                stockMinimoTabla = producto.getStockMinimo();
            } else {
                stockMinimoTabla = "No aplica";
            }

            Object[] fila = {
                    producto.getId(),
                    producto.getTipoProducto(),
                    producto.getNombre(),
                    producto.getUnidadMedida(),
                    producto.getCantidadDisponible(),
                    stockMinimoTabla,
                    producto.obtenerEstadoStock(),
                    "X"
            };

            modeloTabla.addRow(fila);
        }
    }

    /**
     * Genera una orden de compra para ingredientes esenciales con bajo stock.
     */
    private void generarOrdenCompra() {
        try {
            String mensaje = controlador.generarOrdenCompra();
            JOptionPane.showMessageDialog(this, mensaje);
        } catch (SQLException | StockException e) {
            JOptionPane.showMessageDialog(this, "Aviso: " + e.getMessage());
        }
    }

    /**
     * Limpia el formulario.
     */
    private void limpiarCampos() {
        txtNombre.setText("");
        txtCantidad.setText("");
        txtStockMinimo.setText("0");
        txtMotivo.setText("");
        comboUnidad.setSelectedIndex(0);
        comboCategoria.setSelectedIndex(0);
        chkEsencial.setSelected(false);

        /*
         * El formulario vuelve al estado de ingrediente general.
         */
        txtStockMinimo.setEnabled(false);
        txtMotivo.setEnabled(false);
    }

    /**
     * Renderer personalizado para colorear filas según el estado.
     */
    private class RenderEstadoStock extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {

            Component componente = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (!isSelected) {
                componente.setBackground(Color.WHITE);
                componente.setForeground(Color.BLACK);

                String estado = table.getValueAt(row, 6).toString();

                if ("STOCK BAJO".equalsIgnoreCase(estado)) {
                    componente.setBackground(new Color(255, 245, 230));
                }

                if ("SIN STOCK".equalsIgnoreCase(estado)) {
                    componente.setBackground(new Color(255, 230, 230));
                }

                if ("NO CRÍTICO".equalsIgnoreCase(estado)) {
                    componente.setBackground(new Color(245, 245, 245));
                }
            }

            /*
             * Personalización especial de la columna Estado.
             */
            if (column == 6) {
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(new Font("SansSerif", Font.BOLD, 12));

                String estado = value.toString();

                if ("STOCK BAJO".equalsIgnoreCase(estado)) {
                    componente.setForeground(new Color(211, 84, 0));
                } else if ("SIN STOCK".equalsIgnoreCase(estado)) {
                    componente.setForeground(new Color(192, 57, 43));
                } else if ("NO CRÍTICO".equalsIgnoreCase(estado)) {
                    componente.setForeground(COLOR_GRIS);
                } else {
                    componente.setForeground(new Color(39, 174, 96));
                }
            } else {
                setHorizontalAlignment(SwingConstants.LEFT);
                setFont(new Font("SansSerif", Font.PLAIN, 13));
            }

            return componente;
        }
    }

    /**
     * Renderer visual del botón eliminar.
     */
    private class BotonEliminarRenderer extends JButton implements TableCellRenderer {

        public BotonEliminarRenderer() {
            setUI(new javax.swing.plaf.basic.BasicButtonUI());
            setText("X");
            setFont(new Font("SansSerif", Font.BOLD, 12));
            setForeground(Color.WHITE);
            setBackground(COLOR_ROJO);
            setOpaque(true);
            setBorderPainted(false);
            setFocusPainted(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            setText("X");
            return this;
        }
    }

    /**
     * Editor del botón eliminar.
     *
     * Este editor permite detectar el click y ejecutar la eliminación.
     */
    private class BotonEliminarEditor extends DefaultCellEditor {

        private JButton boton;
        private int filaModelo;

        public BotonEliminarEditor() {
            super(new JCheckBox());

            boton = new JButton("X");
            boton.setUI(new javax.swing.plaf.basic.BasicButtonUI());
            boton.setFont(new Font("SansSerif", Font.BOLD, 12));
            boton.setForeground(Color.WHITE);
            boton.setBackground(COLOR_ROJO);
            boton.setOpaque(true);
            boton.setBorderPainted(false);
            boton.setFocusPainted(false);

            /*
             * Permite eliminar con un solo click.
             */
            setClickCountToStart(1);

            boton.addActionListener(e -> eliminarFilaSeleccionada());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
                                                     int row, int column) {
            /*
             * Como la tabla puede estar ordenada, se convierte el índice visual
             * al índice real del modelo.
             */
            filaModelo = table.convertRowIndexToModel(row);
            return boton;
        }

        /**
         * Elimina el ingrediente seleccionado.
         */
        private void eliminarFilaSeleccionada() {
            try {
                int id = Integer.parseInt(modeloTabla.getValueAt(filaModelo, 0).toString());
                String nombre = modeloTabla.getValueAt(filaModelo, 2).toString();

                int respuesta = JOptionPane.showConfirmDialog(
                        InventarioVista.this,
                        "¿Está seguro de que desea eliminar el ingrediente?\n\n"
                                + "ID: " + id + "\n"
                                + "Nombre: " + nombre + "\n\n"
                                + "Esta acción también eliminará sus movimientos relacionados.",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (respuesta == JOptionPane.YES_OPTION) {
                    controlador.eliminarIngrediente(id);

                    JOptionPane.showMessageDialog(
                            InventarioVista.this,
                            "Ingrediente eliminado correctamente."
                    );

                    cargarInventario();
                }

            } catch (SQLException | StockException ex) {
                JOptionPane.showMessageDialog(
                        InventarioVista.this,
                        "Error al eliminar ingrediente: " + ex.getMessage()
                );
            } finally {
                fireEditingStopped();
            }
        }
    }
}