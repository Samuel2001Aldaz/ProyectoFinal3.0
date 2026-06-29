package SistemaConsumoElectrico.Interfaz;
import SistemaConsumoElectrico.Modelo.*;
import SistemaConsumoElectrico.Negocio.*;
import SistemaConsumoElectrico.Excepciones.DatoInvalidoException;
import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
import java.awt.Component;
import java.awt.Color;
import java.awt.Font;

public class MainInterfaz extends JFrame {
    private SistemaEnergetico sistema;
    private CatalogoDispositivos catalogo;
    private Usuario usuarioActual;
    private Image logoInicio;
    private JPanel panelInicio;
    private JTabbedPane pestanas;
    private static final Color COLOR_PRIMARIO = new Color(6, 125, 233);
    private static final Color COLOR_SECUNDARIO = new Color(102, 187, 106);
    private static final Color COLOR_BARRA_SUPERIOR = new Color(10, 25, 47);
    private static final Color COLOR_FONDO = new Color(245, 245, 245);
    private static final Color COLOR_TEXTO = new Color(33, 33, 33);
    private static final Color COLOR_BORDE = new Color(224, 224, 224);
    private static final String NOMBRE_ADMIN = "Administracion";
    public MainInterfaz() {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
            sistema = new SistemaEnergetico();
            catalogo = new CatalogoDispositivos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            System.exit(1);
        }
        setTitle("Sistema de Consumo Electrico");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1060, 800);
        setLocationRelativeTo(null);
        setBackground(COLOR_FONDO);
        logoInicio = cargarLogoInicio();
        mostrarPantallaLogin();
        setVisible(true);
    }
    private Image cargarLogoInicio() {
        String[] rutas = {
                "/SistemaConsumoElectrico/Recursos/logo_inicio.jpeg",
                "/SistemaConsumoElectrico/Recursos/logo_inicio.jpg",
                "/SistemaConsumoElectrico/recursos/logo_inicio.jpeg",
                "/SistemaConsumoElectrico/recursos/logo_inicio.jpg"
        };
        for (String ruta : rutas) {
            java.net.URL recurso = MainInterfaz.class.getResource(ruta);
            if (recurso != null) {
                return new ImageIcon(recurso).getImage();
            }
        }
        String base = System.getProperty("user.dir");
        String[] rutasArchivo = {
                base + "\\src\\SistemaConsumoElectrico\\Recursos\\logo_inicio.jpeg",
                base + "\\src\\SistemaConsumoElectrico\\Recursos\\logo_inicio.jpg"
        };
        for (String ruta : rutasArchivo) {
            ImageIcon icono = new ImageIcon(ruta);
            if (icono.getIconWidth() > 0) {
                return icono.getImage();
            }
        }
        return null;
    }
    private void mostrarPantallaLogin() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(COLOR_FONDO);
        panelPrincipal.add(crearPanelMarca(), BorderLayout.NORTH);
        PanelConFondoPCB panelCentro = new PanelConFondoPCB();
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));
        panelCentro.setBackground(COLOR_FONDO);
        panelCentro.setBorder(BorderFactory.createEmptyBorder(22, 60, 60, 60));
        JTabbedPane tabsLogin = new JTabbedPane();
        tabsLogin.addTab("Iniciar Sesion", crearPanelLogin());
        tabsLogin.addTab("Registrarse", crearPanelRegistro());
        tabsLogin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        panelCentro.add(Box.createVerticalGlue());
        panelCentro.add(crearPanelLogo());
        panelCentro.add(Box.createVerticalStrut(18));
        panelCentro.add(tabsLogin);
        panelCentro.add(Box.createVerticalGlue());
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);
        setContentPane(panelPrincipal);
        revalidate();
        repaint();
    }
    private JPanel crearPanelLogin() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel lblId = new JLabel("ID de Usuario:");
        lblId.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JTextField tfId = new JTextField();
        estilizarTextField(tfId);
        JButton btnLogin = new JButton("Iniciar Sesion");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(COLOR_PRIMARIO);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(e -> {
            try {
                int id = Integer.parseInt(tfId.getText().trim());
                Usuario usuario = sistema.getGestorUsuarios().buscarUsuario(id);
                if (usuario != null) {
                    usuarioActual = usuario;
                    mostrarAlertaInteligente(usuarioActual);
                    mostrarPantallaPrincipal();
                } else {
                    JOptionPane.showMessageDialog(panel, "Usuario no encontrado");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "ID invalido");
            }
        });
        panel.add(lblId);
        panel.add(Box.createVerticalStrut(8));
        panel.add(tfId);
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnLogin);
        panel.add(Box.createVerticalGlue());
        return panel;
    }
    private JPanel crearPanelRegistro() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));

        JLabel lblModo = new JLabel("Registrar como:");
        lblModo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JComboBox<String> cbModo = new JComboBox<>(new String[]{"Usuario", "Administracion"});
        cbModo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        CardLayout cardLayout = new CardLayout();
        JPanel panelCards = new JPanel(cardLayout);
        panelCards.setBackground(COLOR_FONDO);

        // ===== Panel Usuario =====
        JPanel panelUsuario = new JPanel();
        panelUsuario.setLayout(new BoxLayout(panelUsuario, BoxLayout.Y_AXIS));
        panelUsuario.setBackground(COLOR_FONDO);

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Residencial", "Empresa"});
        cbTipo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel lblId = new JLabel("ID:");
        lblId.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JTextField tfId = new JTextField();
        estilizarTextField(tfId);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JTextField tfNombre = new JTextField();
        estilizarTextField(tfNombre);

        JLabel lblSector = new JLabel("Sector:");
        lblSector.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JComboBox<Sector> cbSector = new JComboBox<>(Sector.values());
        cbSector.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel lblRuc = new JLabel("RUC:");
        lblRuc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JTextField tfRuc = new JTextField();
        estilizarTextField(tfRuc);
        tfRuc.setEnabled(false);

        JLabel lblActividad = new JLabel("Actividad:");
        lblActividad.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JTextField tfActividad = new JTextField();
        estilizarTextField(tfActividad);
        tfActividad.setEnabled(false);

        cbTipo.addActionListener(e -> {
            boolean esEmpresa = "Empresa".equals(cbTipo.getSelectedItem());
            tfRuc.setEnabled(esEmpresa);
            tfActividad.setEnabled(esEmpresa);
        });

        JButton btnRegistrarUsuario = new JButton("Registrarse");
        btnRegistrarUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegistrarUsuario.setBackground(COLOR_SECUNDARIO);
        btnRegistrarUsuario.setForeground(Color.WHITE);
        btnRegistrarUsuario.setFocusPainted(false);
        btnRegistrarUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnRegistrarUsuario.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegistrarUsuario.addActionListener(e -> {
            try {
                int id = Integer.parseInt(tfId.getText().trim());
                String nombre = tfNombre.getText().trim();
                Sector sector = (Sector) cbSector.getSelectedItem();

                if (nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Nombre requerido");
                    return;
                }
                if (nombre.equalsIgnoreCase(NOMBRE_ADMIN)) {
                    JOptionPane.showMessageDialog(panel, "El nombre Administracion esta reservado");
                    return;
                }

                if ("Residencial".equals(cbTipo.getSelectedItem())) {
                    UsuarioResidencial usuario = new UsuarioResidencial(id, nombre, sector);
                    sistema.getGestorUsuarios().agregarUsuario(usuario);
                    usuarioActual = usuario;
                } else {
                    String ruc = tfRuc.getText().trim();
                    String actividad = tfActividad.getText().trim();
                    if (ruc.isEmpty() || actividad.isEmpty()) {
                        JOptionPane.showMessageDialog(panel, "Completa todos los campos");
                        return;
                    }
                    UsuarioEmpresa empresa = new UsuarioEmpresa(id, nombre, sector, ruc, actividad);
                    sistema.getGestorUsuarios().agregarUsuario(empresa);
                    usuarioActual = empresa;
                }

                JOptionPane.showMessageDialog(panel, "Registro exitoso!");
                mostrarPantallaPrincipal();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
            }
        });

        panelUsuario.add(lblTipo);
        panelUsuario.add(Box.createVerticalStrut(5));
        panelUsuario.add(cbTipo);
        panelUsuario.add(Box.createVerticalStrut(12));
        panelUsuario.add(lblId);
        panelUsuario.add(Box.createVerticalStrut(5));
        panelUsuario.add(tfId);
        panelUsuario.add(Box.createVerticalStrut(12));
        panelUsuario.add(lblNombre);
        panelUsuario.add(Box.createVerticalStrut(5));
        panelUsuario.add(tfNombre);
        panelUsuario.add(Box.createVerticalStrut(12));
        panelUsuario.add(lblSector);
        panelUsuario.add(Box.createVerticalStrut(5));
        panelUsuario.add(cbSector);
        panelUsuario.add(Box.createVerticalStrut(12));
        panelUsuario.add(lblRuc);
        panelUsuario.add(Box.createVerticalStrut(5));
        panelUsuario.add(tfRuc);
        panelUsuario.add(Box.createVerticalStrut(12));
        panelUsuario.add(lblActividad);
        panelUsuario.add(Box.createVerticalStrut(5));
        panelUsuario.add(tfActividad);
        panelUsuario.add(Box.createVerticalStrut(15));
        panelUsuario.add(btnRegistrarUsuario);
        panelUsuario.add(Box.createVerticalGlue());

        // ===== Panel Administracion =====
        JPanel panelAdmin = new JPanel();
        panelAdmin.setLayout(new BoxLayout(panelAdmin, BoxLayout.Y_AXIS));
        panelAdmin.setBackground(COLOR_FONDO);

        JLabel lblAdminId = new JLabel("ID de Administracion:");
        lblAdminId.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JTextField tfAdminId = new JTextField();
        estilizarTextField(tfAdminId);

        JButton btnRegistrarAdmin = new JButton("Registrar Administracion");
        btnRegistrarAdmin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegistrarAdmin.setBackground(COLOR_PRIMARIO);
        btnRegistrarAdmin.setForeground(Color.WHITE);
        btnRegistrarAdmin.setFocusPainted(false);
        btnRegistrarAdmin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnRegistrarAdmin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegistrarAdmin.addActionListener(e -> {
            try {
                if (buscarUsuarioAdministracion() != null) {
                    JOptionPane.showMessageDialog(panel, "Administracion ya existe y solo puede crearse una vez");
                    return;
                }

                int id = Integer.parseInt(tfAdminId.getText().trim());
                UsuarioResidencial admin = new UsuarioResidencial(id, NOMBRE_ADMIN, Sector.CENTRO);
                sistema.getGestorUsuarios().agregarUsuario(admin);
                usuarioActual = admin;

                JOptionPane.showMessageDialog(panel, "Administracion registrada");
                mostrarPantallaPrincipal();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
            }
        });

        panelAdmin.add(lblAdminId);
        panelAdmin.add(Box.createVerticalStrut(5));
        panelAdmin.add(tfAdminId);
        panelAdmin.add(Box.createVerticalStrut(15));
        panelAdmin.add(btnRegistrarAdmin);
        panelAdmin.add(Box.createVerticalGlue());

        panelCards.add(panelUsuario, "USUARIO");
        panelCards.add(panelAdmin, "ADMIN");

        cbModo.addActionListener(e -> {
            if ("Administracion".equals(cbModo.getSelectedItem())) {
                cardLayout.show(panelCards, "ADMIN");
            } else {
                cardLayout.show(panelCards, "USUARIO");
            }
        });

        panel.add(lblModo);
        panel.add(Box.createVerticalStrut(5));
        panel.add(cbModo);
        panel.add(Box.createVerticalStrut(14));
        panel.add(panelCards);

        cardLayout.show(panelCards, "USUARIO");
        return panel;
    }
    private JPanel crearPanelUsuarios() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBackground(COLOR_FONDO);

        JLabel lblTitulo = new JLabel("Gestion de usuarios");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JLabel lblEliminar = new JLabel("Eliminar usuario por ID:");
        lblEliminar.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JTextField tfEliminarId = new JTextField();
        estilizarTextField(tfEliminarId);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnEliminar.setBackground(new Color(239, 83, 80));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEliminar.setPreferredSize(new Dimension(120, 38));

        JButton btnMostrar = new JButton("Mostrar usuarios");
        btnMostrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnMostrar.setBackground(COLOR_PRIMARIO);
        btnMostrar.setForeground(Color.WHITE);
        btnMostrar.setFocusPainted(false);
        btnMostrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMostrar.setPreferredSize(new Dimension(170, 38));

        String[] columnas = {"ID", "Nombre", "Rol", "Tipo", "Sector", "RUC", "Actividad"};
        DefaultTableModel modeloUsuarios = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tablaUsuarios = new JTable(modeloUsuarios);
        tablaUsuarios.setRowHeight(26);
        tablaUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaUsuarios.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollUsuarios = new JScrollPane(tablaUsuarios);
        scrollUsuarios.setPreferredSize(new Dimension(0, 230));
        scrollUsuarios.setVisible(false);

        btnMostrar.addActionListener(e -> {
            modeloUsuarios.setRowCount(0);
            for (Usuario u : sistema.getGestorUsuarios().getUsuarios()) {
                String rol = esUsuarioAdministracion(u) ? "Administracion" : "Usuario";
                String tipo = (u instanceof UsuarioEmpresa) ? "Empresa" : "Residencial";
                String ruc = "";
                String actividad = "";

                if (u instanceof UsuarioEmpresa) {
                    UsuarioEmpresa emp = (UsuarioEmpresa) u;
                    ruc = emp.getRuc();
                    actividad = emp.getActividadEconomica();
                }

                modeloUsuarios.addRow(new Object[]{
                        u.getId(),
                        u.getNombre(),
                        rol,
                        tipo,
                        u.getSector(),
                        ruc,
                        actividad
                });
            }

            scrollUsuarios.setVisible(true);
            panel.revalidate();
            panel.repaint();
        });

        btnEliminar.addActionListener(e -> {
            try {
                int idEliminar = Integer.parseInt(tfEliminarId.getText().trim());
                Usuario admin = buscarUsuarioAdministracion();

                if (admin != null && admin.getId() == idEliminar) {
                    JOptionPane.showMessageDialog(panel, "No se puede eliminar al usuario Administracion");
                    return;
                }

                sistema.getGestorUsuarios().eliminarUsuario(idEliminar);

                if (usuarioActual != null && usuarioActual.getId() == idEliminar) {
                    usuarioActual = null;
                }

                JOptionPane.showMessageDialog(panel, "Usuario eliminado");
                tfEliminarId.setText("");

                if (scrollUsuarios.isVisible()) {
                    btnMostrar.doClick(); // refresca tabla
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
            }
        });

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelAcciones.setBackground(COLOR_FONDO);
        panelAcciones.add(btnEliminar);
        panelAcciones.add(btnMostrar);

        panelSuperior.add(lblTitulo);
        panelSuperior.add(Box.createVerticalStrut(12));
        panelSuperior.add(lblEliminar);
        panelSuperior.add(Box.createVerticalStrut(5));
        panelSuperior.add(tfEliminarId);
        panelSuperior.add(Box.createVerticalStrut(10));
        panelSuperior.add(panelAcciones);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(scrollUsuarios, BorderLayout.CENTER);

        return panel;
    }
    private void mostrarPantallaPrincipal() {
        PanelConFondoPCB panelPrincipal = new PanelConFondoPCB();
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.setBackground(COLOR_FONDO);

        JPanel panelContenedor = new JPanel(new BorderLayout());
        panelContenedor.setOpaque(false);

        JPanel panelEncabezado = new JPanel(new BorderLayout());
        panelEncabezado.setBackground(COLOR_BARRA_SUPERIOR);
        panelEncabezado.setBorder(BorderFactory.createEmptyBorder(22, 28, 22, 28));
        panelEncabezado.setPreferredSize(new Dimension(0, 98));

        JLabel lblBienvenida = new JLabel("Bienvenido, " + usuarioActual.getNombre());
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 23));
        lblBienvenida.setForeground(Color.WHITE);

        JLabel lblInfo = new JLabel("ID: " + usuarioActual.getId() + " | Sector: " + usuarioActual.getSector());
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblInfo.setForeground(new Color(220, 220, 220));

        JPanel panelIzq = new JPanel();
        panelIzq.setLayout(new BoxLayout(panelIzq, BoxLayout.Y_AXIS));
        panelIzq.setBackground(COLOR_BARRA_SUPERIOR);
        panelIzq.add(lblBienvenida);
        panelIzq.add(lblInfo);

        JButton btnSalir = new JButton("Cerrar Sesion");
        btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSalir.setBackground(new Color(255, 107, 107));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.setPreferredSize(new Dimension(140, 42));
        btnSalir.addActionListener(e -> {
            usuarioActual = null;
            mostrarPantallaLogin();
        });

        panelEncabezado.add(panelIzq, BorderLayout.WEST);
        panelEncabezado.add(btnSalir, BorderLayout.EAST);

        pestanas = new JTabbedPane();
        JTabbedPane tabbedPane = pestanas;

        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 15));

        if (esUsuarioAdministracion(usuarioActual)) {
            tabbedPane.addTab("Usuarios", crearPanelUsuarios());
        } else {

            tabbedPane.addTab(
                    "Inicio",
                    crearPestanaInicio()
            );


            tabbedPane.addTab(
                    "Dispositivos",
                    crearPestanaDispositivos()
            );


            tabbedPane.addTab(
                    "Resumen",
                    crearPestanaResumen()
            );


            tabbedPane.addTab(
                    "Factura",
                    crearPestanaFactura()
            );


            tabbedPane.addTab(
                    "Recomendaciones",
                    crearPestanaRecomendaciones()
            );
        }

        panelContenedor.add(crearPanelMarca(), BorderLayout.NORTH);
        panelContenedor.add(panelEncabezado, BorderLayout.CENTER);

        panelPrincipal.add(panelContenedor, BorderLayout.NORTH);
        panelPrincipal.add(tabbedPane, BorderLayout.CENTER);

        setContentPane(panelPrincipal);
        revalidate();
        repaint();
    }
    private JPanel crearPestanaDispositivos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JPanel panelEntrada = new JPanel();
        panelEntrada.setLayout(new BoxLayout(panelEntrada, BoxLayout.Y_AXIS));
        panelEntrada.setBackground(Color.WHITE);
        panelEntrada.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Agregar Dispositivo"),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        JLabel lblDisp = new JLabel("Dispositivo:");
        lblDisp.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JComboBox<String> cbDisp = new JComboBox<>();
        cargarCatalogo(cbDisp);
        cbDisp.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel lblNombreOtro = new JLabel("Nombre personalizado:");
        lblNombreOtro.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JTextField tfNombreOtro = new JTextField();
        estilizarTextField(tfNombreOtro);

        JLabel lblPotenciaOtro = new JLabel("Potencia (W):");
        lblPotenciaOtro.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JTextField tfPotenciaOtro = new JTextField();
        estilizarTextField(tfPotenciaOtro);

        lblNombreOtro.setVisible(false);
        tfNombreOtro.setVisible(false);
        lblPotenciaOtro.setVisible(false);
        tfPotenciaOtro.setVisible(false);

        JLabel lblCant = new JLabel("Cantidad:");
        lblCant.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JTextField tfCant = new JTextField();
        estilizarTextField(tfCant);

        JLabel lblHoras = new JLabel("Horas Diarias:");
        lblHoras.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JTextField tfHoras = new JTextField();
        estilizarTextField(tfHoras);

        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.setBackground(COLOR_PRIMARIO);
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAgregar.setPreferredSize(new Dimension(150, 40));
        btnAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnVer = new JButton("Ver Dispositivos");
        btnVer.setBackground(new Color(158, 158, 158));
        btnVer.setForeground(Color.WHITE);
        btnVer.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnVer.setPreferredSize(new Dimension(170, 40));
        btnVer.setCursor(new Cursor(Cursor.HAND_CURSOR));

        String[] columnas = {"ID", "Dispositivo", "Potencia (W)", "Cantidad", "Horas/día", "Consumo (kWh/día)"};
        DefaultTableModel modeloDisp = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tablaDisp = new JTable(modeloDisp);
        tablaDisp.setRowHeight(26);
        tablaDisp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaDisp.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scroll = new JScrollPane(tablaDisp);
        scroll.setPreferredSize(new Dimension(0, 160));

        cbDisp.addActionListener(e -> {
            boolean esOtro = "Otro dispositivo".equals(cbDisp.getSelectedItem());
            lblNombreOtro.setVisible(esOtro);
            tfNombreOtro.setVisible(esOtro);
            lblPotenciaOtro.setVisible(esOtro);
            tfPotenciaOtro.setVisible(esOtro);
            panelEntrada.revalidate();
            panelEntrada.repaint();
        });

        btnAgregar.addActionListener(e -> {
            try {
                String selec = (String) cbDisp.getSelectedItem();
                if (selec == null || selec.startsWith("Seleccione")) {
                    JOptionPane.showMessageDialog(this, "Seleccione un dispositivo");
                    return;
                }

                int cant = Integer.parseInt(tfCant.getText().trim());
                double hrs = Double.parseDouble(tfHoras.getText().trim());

                if (cant <= 0 || hrs <= 0) {
                    JOptionPane.showMessageDialog(this, "Valores invalidos");
                    return;
                }

                Dispositivo nuevo;
                if ("Otro dispositivo".equals(selec)) {
                    String nombreOtro = tfNombreOtro.getText().trim();
                    double potenciaOtro = Double.parseDouble(tfPotenciaOtro.getText().trim());
                    if (nombreOtro.isEmpty() || potenciaOtro <= 0) {
                        JOptionPane.showMessageDialog(this, "Completa nombre y potencia");
                        return;
                    }
                    nuevo = new Dispositivo(generarIdTemporal(), nombreOtro, potenciaOtro, cant, hrs);
                } else {
                    int idDisp = obtenerIdDesdeItem(selec);
                    Dispositivo base = catalogo.buscarDispositivo(idDisp);
                    if (base == null) {
                        JOptionPane.showMessageDialog(this, "Dispositivo no encontrado");
                        return;
                    }
                    nuevo = new Dispositivo(base.getId(), base.getNombre(), base.getPotencia(), cant, hrs);
                }

                usuarioActual.agregarDispositivo(nuevo);
                actualizarInicio();
                JOptionPane.showMessageDialog(this, "Dispositivo agregado");

                tfCant.setText("");
                tfHoras.setText("");
                tfNombreOtro.setText("");
                tfPotenciaOtro.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        btnVer.addActionListener(e -> {
            modeloDisp.setRowCount(0);
            for (Dispositivo d : usuarioActual.getDispositivos()) {
                double consumo = (d.getPotencia() * d.getCantidad() * d.getHorasUsoDiarias()) / 1000.0;
                modeloDisp.addRow(new Object[]{
                        d.getId(),
                        d.getNombre(),
                        String.format("%.0f", d.getPotencia()),
                        d.getCantidad(),
                        String.format("%.2f", d.getHorasUsoDiarias()),
                        String.format("%.2f", consumo)
                });
            }
        });

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(btnAgregar);
        panelBotones.add(btnVer);

        panelEntrada.add(lblDisp);
        panelEntrada.add(Box.createVerticalStrut(5));
        panelEntrada.add(cbDisp);
        panelEntrada.add(Box.createVerticalStrut(10));
        panelEntrada.add(lblNombreOtro);
        panelEntrada.add(Box.createVerticalStrut(5));
        panelEntrada.add(tfNombreOtro);
        panelEntrada.add(Box.createVerticalStrut(10));
        panelEntrada.add(lblPotenciaOtro);
        panelEntrada.add(Box.createVerticalStrut(5));
        panelEntrada.add(tfPotenciaOtro);
        panelEntrada.add(Box.createVerticalStrut(12));
        panelEntrada.add(lblCant);
        panelEntrada.add(Box.createVerticalStrut(5));
        panelEntrada.add(tfCant);
        panelEntrada.add(Box.createVerticalStrut(12));
        panelEntrada.add(lblHoras);
        panelEntrada.add(Box.createVerticalStrut(5));
        panelEntrada.add(tfHoras);
        panelEntrada.add(Box.createVerticalStrut(15));
        panelEntrada.add(panelBotones);

        panel.add(panelEntrada, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }
    private JPanel crearPestanaResumen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(BorderFactory.createTitledBorder("Analisis"));

        JButton btnResumen = new JButton("Ver Resumen");
        btnResumen.setBackground(COLOR_PRIMARIO);
        btnResumen.setForeground(Color.WHITE);
        btnResumen.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnResumen.setPreferredSize(new Dimension(150, 40));
        btnResumen.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnAnalisis = new JButton("Ver Analisis");
        btnAnalisis.setBackground(COLOR_PRIMARIO);
        btnAnalisis.setForeground(Color.WHITE);
        btnAnalisis.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAnalisis.setPreferredSize(new Dimension(150, 40));
        btnAnalisis.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JTextPane taResultados = crearAreaInforme();
        JScrollPane scroll = new JScrollPane(taResultados);
        scroll.setPreferredSize(new Dimension(0, 170));

        btnResumen.addActionListener(e -> {
            String texto = sistema.obtenerResumenUsuario(usuarioActual);
            taResultados.setText(formatearInformeHtml(texto, false));
            taResultados.setCaretPosition(0);
        });

        btnAnalisis.addActionListener(e -> {
            String texto = sistema.obtenerAnalisisUsuario(usuarioActual);
            taResultados.setText(formatearInformeHtml(texto, true));
            taResultados.setCaretPosition(0);
        });

        panelBotones.add(btnResumen);
        panelBotones.add(btnAnalisis);
        panel.add(panelBotones, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }
    private JPanel crearPestanaFactura() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBoton.setBackground(Color.WHITE);
        panelBoton.setBorder(BorderFactory.createTitledBorder("Factura"));

        JButton btnGenerar = new JButton("Generar");
        btnGenerar.setBackground(COLOR_PRIMARIO);
        btnGenerar.setForeground(Color.WHITE);
        btnGenerar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGenerar.setPreferredSize(new Dimension(150, 40));
        btnGenerar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JTextPane taFactura = crearAreaInforme();
        JScrollPane scroll = new JScrollPane(taFactura);
        scroll.setPreferredSize(new Dimension(0, 170));

        btnGenerar.addActionListener(e -> {
            String texto = sistema.generarFactura(usuarioActual);
            taFactura.setText(formatearInformeHtml(texto, false));
            taFactura.setCaretPosition(0);
        });

        panelBoton.add(btnGenerar);
        panel.add(panelBoton, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }
    private JPanel crearPestanaRecomendaciones() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBoton.setBackground(Color.WHITE);
        panelBoton.setBorder(BorderFactory.createTitledBorder("Recomendaciones"));

        JButton btnObtener = new JButton("Obtener");
        btnObtener.setBackground(COLOR_PRIMARIO);
        btnObtener.setForeground(Color.WHITE);
        btnObtener.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnObtener.setPreferredSize(new Dimension(150, 40));
        btnObtener.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JTextPane taRecom = crearAreaInforme();
        JScrollPane scroll = new JScrollPane(taRecom);
        scroll.setPreferredSize(new Dimension(0, 170));

        btnObtener.addActionListener(e -> {
            String texto = sistema.obtenerRecomendaciones(usuarioActual);
            taRecom.setText(formatearInformeHtml(texto, false));
            taRecom.setCaretPosition(0);
        });

        panelBoton.add(btnObtener);
        panel.add(panelBoton, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }
    private JPanel crearPestanaInicio() {


        panelInicio = new JPanel(new BorderLayout());

        JPanel panel = panelInicio;

        panel.setBackground(COLOR_FONDO);

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,20,20,20
                )
        );



        JPanel contenedor = new JPanel();

        contenedor.setLayout(
                new GridLayout(3,2,20,20)
        );


        contenedor.setBackground(
                COLOR_FONDO
        );




        // TARJETA USUARIO

        JPanel tarjetaUsuario =
                crearTarjeta(
                        "USUARIO",
                        "Nombre: "
                                + usuarioActual.getNombre()
                                + "\nID: "
                                + usuarioActual.getId()
                                + "\nSector: "
                                + usuarioActual.getSector()
                );




        // TARJETA CONSUMO

        double consumo =
                sistema
                        .getCalculadora()
                        .calcularConsumoMensual(usuarioActual);


        JPanel tarjetaConsumo =
                crearTarjeta(
                        "CONSUMO ENERGÉTICO",
                        String.format(
                                "%.2f kWh mensual",
                                consumo
                        )
                );





        // TARJETA ESTADO

        String estado =
                sistema
                        .getAnalizador()
                        .clasificarConsumo(usuarioActual);



        JPanel tarjetaEstado =
                crearTarjeta(
                        "ESTADO ENERGÉTICO",
                        estado
                );






        // TARJETA DISPOSITIVOS


        JPanel tarjetaDispositivos =
                crearTarjeta(
                        "DISPOSITIVOS",
                        "Registrados: "
                                + usuarioActual
                                .getDispositivos()
                                .size()
                );






        // TARJETA EMPRESA

        String extra="";


        if(usuarioActual instanceof UsuarioEmpresa){


            UsuarioEmpresa emp =
                    (UsuarioEmpresa) usuarioActual;


            extra =
                    "RUC: "
                            + emp.getRuc()
                            + "\nActividad: "
                            + emp.getActividadEconomica();

        }


        JPanel tarjetaExtra =
                crearTarjeta(
                        "INFORMACIÓN",
                        extra.isEmpty()
                                ?
                                "Usuario residencial"
                                :
                                extra
                );





        contenedor.add(tarjetaUsuario);

        contenedor.add(tarjetaConsumo);

        contenedor.add(tarjetaEstado);

        contenedor.add(tarjetaDispositivos);

        contenedor.add(tarjetaExtra);




        panel.add(
                contenedor,
                BorderLayout.NORTH
        );


        return panel;

    }

    private void actualizarInicio(){

        if(pestanas != null){


            pestanas.setComponentAt(
                    0,
                    crearPestanaInicio()
            );


            pestanas.revalidate();

            pestanas.repaint();

        }

    }

    private JPanel crearTarjeta(
            String titulo,
            String contenido
    ){


        JPanel tarjeta = new JPanel();


        tarjeta.setLayout(
                new BorderLayout()
        );


        tarjeta.setBackground(
                Color.WHITE
        );


        tarjeta.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                new Color(210,210,210),
                                1
                        ),

                        BorderFactory.createEmptyBorder(
                                18,
                                18,
                                18,
                                18
                        )
                )
        );



        JLabel encabezado =
                new JLabel(titulo);



        encabezado.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        17
                )
        );


        encabezado.setForeground(
                new Color(20,60,100)
        );



        JTextArea texto =
                new JTextArea(
                        contenido
                );


        texto.setEditable(false);


        texto.setLineWrap(true);


        texto.setWrapStyleWord(true);



        texto.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );



        texto.setForeground(
                new Color(50,50,50)
        );



        texto.setBackground(
                Color.WHITE
        );



    /*
        CAMBIO VISUAL DEL ESTADO ENERGÉTICO
    */

        /*
    CAMBIO VISUAL DEL ESTADO ENERGÉTICO
*/

        if(titulo.equals("ESTADO ENERGÉTICO")){


            texto.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            24
                    )
            );


            texto.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );


            switch(contenido){


                case "BAJO":

                    texto.setForeground(
                            new Color(0,150,0)
                    );

                    break;



                case "NORMAL":

                    texto.setForeground(
                            new Color(0,120,220)
                    );

                    break;



                case "ALTO":

                    texto.setForeground(
                            new Color(230,140,0)
                    );

                    break;



                case "CRITICO":

                    texto.setForeground(
                            Color.RED
                    );

                    break;
            }

        }



        tarjeta.add(
                encabezado,
                BorderLayout.NORTH
        );



        tarjeta.add(
                texto,
                BorderLayout.CENTER
        );



        return tarjeta;

    }

    private JPanel crearPanelMarca() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_BARRA_SUPERIOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 8, 20));
        JLabel lblSistema = new JLabel("Sistema de monitoreo de consumo electrico");
        lblSistema.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSistema.setForeground(Color.WHITE);
        lblSistema.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel lblMarca = new JLabel("Inteligencia electrica");
        lblMarca.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblMarca.setForeground(new Color(230, 230, 230));
        lblMarca.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblSistema);
        panel.add(lblMarca);
        return panel;
    }
    private JPanel crearPanelLogo() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                if (logoInicio != null) {
                    g2.drawImage(logoInicio, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g2.setColor(new Color(230, 230, 230));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    g2.setColor(new Color(90, 90, 90));
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
                    g2.drawString("Logo no encontrado", 65, getHeight() / 2);
                }

                // Capa ligera para estilo profesional
                g2.setColor(new Color(10, 25, 47, 45));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);

                g2.dispose();
            }
        };
        panel.setPreferredSize(new Dimension(280, 140));
        panel.setMaximumSize(new Dimension(280, 140));
        panel.setMinimumSize(new Dimension(280, 140));
        panel.setOpaque(false);
        return panel;
    }
    private String generarDetalleUsuarios() {
        if (sistema.getGestorUsuarios().getUsuarios().isEmpty()) {
            return "No existen usuarios registrados.";
        }
        StringBuilder texto = new StringBuilder("===== USUARIOS REGISTRADOS =====\n");
        for (Usuario u : sistema.getGestorUsuarios().getUsuarios()) {
            texto.append("ID: ").append(u.getId())
                    .append(" | Nombre: ").append(u.getNombre())
                    .append(" | Sector: ").append(u.getSector());
            if (esUsuarioAdministracion(u)) {
                texto.append(" | Rol: Administracion");
            }
            if (u instanceof UsuarioEmpresa) {
                UsuarioEmpresa emp = (UsuarioEmpresa) u;
                texto.append(" | Tipo: Empresa")
                        .append(" | RUC: ").append(emp.getRuc())
                        .append(" | Actividad: ").append(emp.getActividadEconomica());
            } else {
                texto.append(" | Tipo: Residencial");
            }
            texto.append("\n");
        }
        return texto.toString();
    }
    private Usuario buscarUsuarioAdministracion() {
        for (Usuario u : sistema.getGestorUsuarios().getUsuarios()) {
            if (esUsuarioAdministracion(u)) {
                return u;
            }
        }
        return null;
    }
    private boolean esUsuarioAdministracion(Usuario usuario) {
        return usuario != null
                && usuario.getNombre() != null
                && usuario.getNombre().trim().equalsIgnoreCase(NOMBRE_ADMIN);
    }
    private void cargarCatalogo(JComboBox<String> cb) {
        cb.removeAllItems();
        cb.addItem("Seleccione un dispositivo");
        for (Dispositivo d : catalogo.getCatalogo()) {
            cb.addItem(String.format("ID: %d - %s (%.0f W)", d.getId(), d.getNombre(), d.getPotencia()));
        }
        cb.addItem("Otro dispositivo");
    }

    private void dibujarFondoPCB(JPanel panel) {
        panel.setOpaque(true);
        panel.setBackground(COLOR_FONDO);
    }

    private int obtenerIdDesdeItem(String item) {
        int inicio = item.indexOf("ID:");
        int separador = item.indexOf(" - ");
        if (inicio != 0 || separador < 0) {
            throw new NumberFormatException("Formato de dispositivo invalido");
        }
        return Integer.parseInt(item.substring(3, separador).trim());
    }
    private int generarIdTemporal() {
        return 1000 + usuarioActual.getDispositivos().size() + 1;
    }
    private void estilizarTextField(JTextField tf) {
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBackground(Color.WHITE);
        tf.setForeground(COLOR_TEXTO);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
    }

    private class PanelConFondoPCB extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(6, 125, 233, 20));
            g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            int spacing = 50;
            for (int i = 0; i < getWidth(); i += spacing) {
                g2.drawLine(i, 0, i, getHeight());
            }
            for (int i = 0; i < getHeight(); i += spacing) {
                g2.drawLine(0, i, getWidth(), i);
            }

            for (int i = spacing; i < getWidth(); i += spacing * 2) {
                for (int j = spacing; j < getHeight(); j += spacing * 2) {
                    g2.fillOval(i - 2, j - 2, 4, 4);
                }
            }

            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainInterfaz::new);
    }
    private JTextPane crearAreaInforme() {
        JTextPane area = new JTextPane();
        area.setContentType("text/html");
        area.setEditable(false);
        area.setBackground(Color.WHITE);
        area.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        return area;
    }

    private String escaparHtml(String s) {
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    private String formatearInformeHtml(String textoPlano, boolean resaltarCritico) {
        String t = textoPlano == null ? "" : textoPlano;

        StringBuilder sb = new StringBuilder();
        sb.append("<html><body style='font-family:Segoe UI; font-size:15px; color:#222;'>");

        String[] lineas = t.split("\\R");
        for (String l : lineas) {
            String raw = l == null ? "" : l.trim();
            String line = escaparHtml(raw);

            if (line.isEmpty()) {
                sb.append("<br/>");
                continue;
            }

            String x = raw.toLowerCase();

            boolean titulo =
                    raw.startsWith("====")
                            || raw.endsWith(":")
                            || x.contains("resumen")
                            || x.contains("analisis")
                            || x.contains("análisis")
                            || x.contains("factura")
                            || x.contains("recomend");

            boolean esCritico = x.contains("critico") || x.contains("crítico");
            boolean esAlto = x.contains("alto");
            boolean esMedio = x.contains("medio")
                    || x.contains("normal")
                    || x.contains("moderado")
                    || x.contains("moderada");
            boolean esBajo = x.contains("bajo");

            if (titulo) {
                sb.append("<div style='font-weight:700; margin-top:6px;'>").append(line).append("</div>");
            } else if (resaltarCritico && (esCritico || esAlto)) {
                sb.append("<div style='color:#b71c1c; font-weight:700;'>⚠ ").append(line).append("</div>");
            } else if (resaltarCritico && esMedio) {
                sb.append("<div style='color:#f57f17; font-weight:700;'>").append(line).append("</div>");
            } else if (resaltarCritico && esBajo) {
                sb.append("<div style='color:#2e7d32; font-weight:700;'>").append(line).append("</div>");
            } else {
                sb.append("<div>").append(line).append("</div>");
            }
        }

        sb.append("</body></html>");
        return sb.toString();
    }

    private final Map<Integer, Double> ultimoConsumoDiarioPorUsuario = new HashMap<>();

    private double calcularConsumoDiarioKwh(Usuario usuario) {
        double total = 0.0;
        if (usuario == null) return total;

        for (Dispositivo d : usuario.getDispositivos()) {
            // kWh diarios = (W * cantidad * horas) / 1000
            total += (d.getPotencia() * d.getCantidad() * d.getHorasUsoDiarias()) / 1000.0;
        }
        return total;
    }

    private Dispositivo obtenerDispositivoMayorConsumo(Usuario usuario) {
        Dispositivo mayor = null;
        double max = -1.0;

        if (usuario == null) return null;

        for (Dispositivo d : usuario.getDispositivos()) {
            double consumo = (d.getPotencia() * d.getCantidad() * d.getHorasUsoDiarias()) / 1000.0;
            if (consumo > max) {
                max = consumo;
                mayor = d;
            }
        }
        return mayor;
    }

    private String generarMensajeAlertaInteligente(Usuario usuario) {
        String nombre = usuario.getNombre();
        double consumoActual = calcularConsumoDiarioKwh(usuario);
        Double consumoAnterior = ultimoConsumoDiarioPorUsuario.get(usuario.getId());

        // Guardamos para comparar en el próximo inicio
        ultimoConsumoDiarioPorUsuario.put(usuario.getId(), consumoActual);

        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family:Segoe UI; font-size:14px;'>");
        html.append("<b>Buenos días ").append(nombre).append("</b><br/><br/>");

        if (consumoAnterior != null && consumoAnterior > 0) {
            double aumento = ((consumoActual - consumoAnterior) / consumoAnterior) * 100.0;

            if (aumento >= 20.0) {
                Dispositivo mayor = obtenerDispositivoMayorConsumo(usuario);
                String nombreDispositivo = (mayor != null) ? mayor.getNombre() : "No identificado";

                html.append("<span style='color:#b71c1c;'><b>⚠ Detectamos:</b></span><br/>");
                html.append("Tu consumo aumentó <b>").append(String.format("%.0f", aumento)).append("%</b><br/><br/>");
                html.append("<b>Dispositivo responsable:</b><br/>").append(nombreDispositivo).append("<br/><br/>");
                html.append("<b>Recomendación:</b><br/>Reducir 2 horas diarias.");
            } else {
                html.append("<span style='color:#2e7d32;'><b>✅ Consumo estable</b></span><br/>");
                html.append("No se detectaron aumentos críticos hoy.");
            }
        } else {
            html.append("Bienvenido. Aún no hay histórico para comparar consumo.<br/>");
            html.append("Tu consumo actual estimado es <b>")
                    .append(String.format("%.2f", consumoActual))
                    .append(" kWh/día</b>.");
        }

        html.append("</body></html>");
        return html.toString();
    }

    private void mostrarAlertaInteligente(Usuario usuario) {
        if (usuario == null) return;
        String mensaje = generarMensajeAlertaInteligente(usuario);
        JOptionPane.showMessageDialog(this, mensaje, "Alerta inteligente", JOptionPane.INFORMATION_MESSAGE);
    }
}
