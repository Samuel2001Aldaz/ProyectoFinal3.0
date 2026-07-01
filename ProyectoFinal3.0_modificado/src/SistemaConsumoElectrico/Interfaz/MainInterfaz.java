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

    private Inmueble inmuebleActual;

    private JPanel panelInicio;
    private JTabbedPane pestanas;

    private JTable tablaDisp;
    private DefaultTableModel modeloDisp;

    // NUEVO: selector de inmuebles
    private JComboBox<Inmueble> cbInmuebles;

    // Selector de dispositivos
    private JComboBox<String> cbDisp;

    private JTextField tfCant;
    private JTextField tfHoras;
    private JTextField tfNombreOtro;
    private JTextField tfPotenciaOtro;

    private JTextField txtBuscar;

    private JLabel lblConsumoTotal;

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
        mostrarPantallaLogin();
        setVisible(true);
    }

    private void mostrarPantallaLogin() {


        JPanel panel = new JPanel(new GridBagLayout());


        panel.setBackground(COLOR_FONDO);


        GridBagConstraints gbc = new GridBagConstraints();


        gbc.insets = new Insets(10, 10, 10, 10);


        JLabel titulo = new JLabel("Sistema Inteligente de Consumo Energético");


        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;


        panel.add(titulo, gbc);


        gbc.gridwidth = 1;


        JLabel lblCedula = new JLabel("Cédula:");


        gbc.gridy = 1;
        gbc.gridx = 0;


        panel.add(lblCedula, gbc);


        JTextField txtCedula = new JTextField(15);


        gbc.gridx = 1;


        panel.add(txtCedula, gbc);


        JLabel lblPassword = new JLabel("Contraseña:");


        gbc.gridy = 2;
        gbc.gridx = 0;


        panel.add(lblPassword, gbc);


        JPasswordField txtPassword = new JPasswordField(15);


        gbc.gridx = 1;


        panel.add(txtPassword, gbc);


        JButton ingresar = new JButton("Ingresar");


        gbc.gridy = 3;
        gbc.gridx = 0;


        panel.add(ingresar, gbc);


        JButton registrar = new JButton("Registrar");


        gbc.gridx = 1;


        panel.add(registrar, gbc);


        ingresar.addActionListener(e -> {


            try {


                String cedula = txtCedula.getText().trim();


                String contraseña = new String(txtPassword.getPassword());


                usuarioActual = sistema.getGestorUsuarios().validarLogin(cedula, contraseña);


                mostrarPantallaPrincipal();


            } catch (DatoInvalidoException ex) {


                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de ingreso", JOptionPane.ERROR_MESSAGE);


            }


        });


        registrar.addActionListener(e -> {


            mostrarRegistro();


        });


        setContentPane(panel);


        revalidate();

        repaint();


    }


    private void mostrarRegistro() {

        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(9, 2, 10, 10));

        JTextField cedula = new JTextField();

        JTextField nombre = new JTextField();

        JPasswordField contraseña = new JPasswordField();

        JComboBox<Sector> sector = new JComboBox<>(Sector.values());

        JComboBox<String> tipo = new JComboBox<>(new String[]{"Residencial", "Empresa"});

        JTextField ruc = new JTextField();

        JTextField actividad = new JTextField();

        // NUEVO
        JTextField nombreInmueble = new JTextField();

        JComboBox<TipoInmueble> tipoInmueble = new JComboBox<>(TipoInmueble.values());

        // Al iniciar, si es Residencial los campos estarán deshabilitados
        ruc.setEnabled(false);
        actividad.setEnabled(false);

        // Habilitar o deshabilitar según el tipo de usuario
        tipo.addActionListener(e -> {

            boolean empresa = tipo.getSelectedItem().equals("Empresa");

            ruc.setEnabled(empresa);
            actividad.setEnabled(empresa);

            if (!empresa) {

                ruc.setText("");
                actividad.setText("");

            }

        });

        panel.add(new JLabel("Tipo usuario"));
        panel.add(tipo);

        panel.add(new JLabel("Cédula"));
        panel.add(cedula);

        panel.add(new JLabel("Nombre"));
        panel.add(nombre);

        panel.add(new JLabel("Contraseña"));
        panel.add(contraseña);

        panel.add(new JLabel("Sector"));
        panel.add(sector);

        panel.add(new JLabel("RUC (empresa)"));
        panel.add(ruc);

        panel.add(new JLabel("Actividad"));
        panel.add(actividad);

        // NUEVOS CAMPOS
        panel.add(new JLabel("Nombre del inmueble"));
        panel.add(nombreInmueble);

        panel.add(new JLabel("Tipo de inmueble"));
        panel.add(tipoInmueble);

        int opcion = JOptionPane.showConfirmDialog(this, panel, "Registro usuario", JOptionPane.OK_CANCEL_OPTION);

        if (opcion == JOptionPane.OK_OPTION) {

            try {

                Usuario usuario;

                if (tipo.getSelectedItem().equals("Residencial")) {

                    usuario = new UsuarioResidencial(cedula.getText().trim(), nombre.getText().trim(), new String(contraseña.getPassword()), (Sector) sector.getSelectedItem());

                } else {

                    usuario = new UsuarioEmpresa(cedula.getText().trim(), nombre.getText().trim(), new String(contraseña.getPassword()), (Sector) sector.getSelectedItem(), ruc.getText().trim(), actividad.getText().trim());

                }

                // Validar nombre del inmueble
                if (nombreInmueble.getText().trim().isEmpty()) {

                    JOptionPane.showMessageDialog(this, "Debe ingresar el nombre del inmueble.", "Error", JOptionPane.ERROR_MESSAGE);

                    return;

                }

                // Crear primer inmueble del usuario
                Inmueble inmueble = new Inmueble(generarIdInmueble(), nombreInmueble.getText().trim(), (TipoInmueble) tipoInmueble.getSelectedItem());

                usuario.agregarInmueble(inmueble);

                sistema.getGestorUsuarios().agregarUsuario(usuario);

                JOptionPane.showMessageDialog(this, "Usuario registrado correctamente");

            } catch (DatoInvalidoException ex) {

                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

            }

        }

    }

    private JPanel crearPanelUsuarios() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBackground(COLOR_FONDO);

        JLabel lblTitulo = new JLabel("Gestión de usuarios");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JLabel lblEliminar = new JLabel("Desactivar usuario por Cédula:");
        lblEliminar.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JTextField tfEliminarId = new JTextField();
        estilizarTextField(tfEliminarId);

        JButton btnEliminar = new JButton("Desactivar");
        btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnEliminar.setBackground(new Color(239, 83, 80));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEliminar.setPreferredSize(new Dimension(120, 38));

        JButton btnReactivar = new JButton("Reactivar");
        btnReactivar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnReactivar.setBackground(COLOR_SECUNDARIO);
        btnReactivar.setForeground(Color.WHITE);
        btnReactivar.setFocusPainted(false);
        btnReactivar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReactivar.setPreferredSize(new Dimension(120, 38));

        JButton btnMostrar = new JButton("Mostrar usuarios");
        btnMostrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnMostrar.setBackground(COLOR_PRIMARIO);
        btnMostrar.setForeground(Color.WHITE);
        btnMostrar.setFocusPainted(false);
        btnMostrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMostrar.setPreferredSize(new Dimension(170, 38));

        String[] columnas = {"Cédula", "Nombre", "Rol", "Tipo", "Sector", "Estado", "RUC", "Actividad"};

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

                if (u.getRol() == Rol.ADMINISTRADOR) {
                    continue;
                }

                String rol = u.getRol().toString();

                String tipo = (u instanceof UsuarioEmpresa) ? "Empresa" : "Residencial";

                String estado = u.isActivo() ? "Activo" : "Inactivo";

                String ruc = "";
                String actividad = "";

                if (u instanceof UsuarioEmpresa) {

                    UsuarioEmpresa emp = (UsuarioEmpresa) u;

                    ruc = emp.getRuc();
                    actividad = emp.getActividadEconomica();

                }

                modeloUsuarios.addRow(new Object[]{

                        u.getCedula(), u.getNombre(), rol, tipo, u.getSector(), estado, ruc, actividad

                });

            }

            scrollUsuarios.setVisible(true);

            panel.revalidate();
            panel.repaint();

        });
        btnEliminar.addActionListener(e -> {

            try {

                String cedula = tfEliminarId.getText().trim();

                if (cedula.isEmpty()) {

                    JOptionPane.showMessageDialog(panel, "Ingrese la cédula del usuario.");

                    return;

                }

                Usuario usuario = sistema.getGestorUsuarios().buscarUsuario(cedula);

                if (usuario == null) {

                    JOptionPane.showMessageDialog(panel, "No existe un usuario con esa cédula.");

                    return;

                }

                if (usuario.getRol() == Rol.ADMINISTRADOR) {

                    JOptionPane.showMessageDialog(panel, "No se puede desactivar al administrador.");

                    return;

                }

                if (!usuario.isActivo()) {

                    JOptionPane.showMessageDialog(panel, "El usuario ya se encuentra desactivado.");

                    return;

                }

                usuario.setActivo(false);

                JOptionPane.showMessageDialog(panel, "Usuario desactivado correctamente.");

                tfEliminarId.setText("");

                if (scrollUsuarios.isVisible()) {

                    btnMostrar.doClick();

                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());

            }

        });

        btnReactivar.addActionListener(e -> {

            try {

                String cedula = tfEliminarId.getText().trim();

                if (cedula.isEmpty()) {

                    JOptionPane.showMessageDialog(panel, "Ingrese la cédula del usuario.");

                    return;

                }

                Usuario usuario = sistema.getGestorUsuarios().buscarUsuario(cedula);

                if (usuario == null) {

                    JOptionPane.showMessageDialog(panel, "No existe un usuario con esa cédula.");

                    return;

                }

                if (usuario.isActivo()) {

                    JOptionPane.showMessageDialog(panel, "El usuario ya se encuentra activo.");

                    return;

                }

                usuario.setActivo(true);

                JOptionPane.showMessageDialog(panel, "Usuario reactivado correctamente.");

                tfEliminarId.setText("");

                if (scrollUsuarios.isVisible()) {

                    btnMostrar.doClick();

                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());

            }

        });

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

        panelAcciones.setBackground(COLOR_FONDO);

        panelAcciones.add(btnEliminar);
        panelAcciones.add(btnReactivar);
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

        // NUEVO: seleccionar automáticamente el primer inmueble del usuario
        if (usuarioActual != null && usuarioActual.getRol() != Rol.ADMINISTRADOR) {

            if (!usuarioActual.getInmuebles().isEmpty()) {

                inmuebleActual = usuarioActual.getInmuebles().get(0);

            } else {

                inmuebleActual = null;

            }

        }

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

        String estado = usuarioActual.isActivo() ? "Activo" : "Inactivo";

        JLabel lblInfo = new JLabel("Cédula: " + usuarioActual.getCedula() + " | Sector: " + usuarioActual.getSector() + " | Rol: " + usuarioActual.getRol() + " | Estado: " + estado);

        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblInfo.setForeground(new Color(220, 220, 220));

        JPanel panelIzq = new JPanel();
        panelIzq.setLayout(new BoxLayout(panelIzq, BoxLayout.Y_AXIS));
        panelIzq.setBackground(COLOR_BARRA_SUPERIOR);

        panelIzq.add(lblBienvenida);
        panelIzq.add(lblInfo);

        JButton btnSalir = new JButton("Cerrar Sesión");
        btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSalir.setBackground(new Color(255, 107, 107));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.setPreferredSize(new Dimension(140, 42));

        btnSalir.addActionListener(e -> {
            usuarioActual = null;
            inmuebleActual = null;
            mostrarPantallaLogin();
        });

        panelEncabezado.add(panelIzq, BorderLayout.WEST);
        panelEncabezado.add(btnSalir, BorderLayout.EAST);

        pestanas = new JTabbedPane();
        pestanas.setFont(new Font("Segoe UI", Font.BOLD, 15));

        if (usuarioActual.getRol() == Rol.ADMINISTRADOR) {

            pestanas.addTab("Usuarios", crearPanelUsuarios());

            pestanas.addTab("Reportes", crearPanelReportes());

        } else {

            if (usuarioActual.isActivo()) {

                pestanas.addTab("Inicio", crearPestanaInicio());

                pestanas.addTab("Dispositivos", crearPestanaDispositivos());

                pestanas.addTab("Resumen", crearPestanaResumen());

                pestanas.addTab("Factura", crearPestanaFactura());

                pestanas.addTab("Recomendaciones", crearPestanaRecomendaciones());

            } else {

                JOptionPane.showMessageDialog(this, "Su cuenta se encuentra desactivada.\n\n" + "Solo puede consultar la información histórica de su cuenta.", "Cuenta desactivada", JOptionPane.INFORMATION_MESSAGE);

                pestanas.addTab("Resumen", crearPestanaResumen());

                pestanas.addTab("Factura", crearPestanaFactura());

            }

        }

        panelContenedor.add(crearPanelMarca(), BorderLayout.NORTH);

        panelContenedor.add(panelEncabezado, BorderLayout.CENTER);

        panelPrincipal.add(panelContenedor, BorderLayout.NORTH);

        panelPrincipal.add(pestanas, BorderLayout.CENTER);

        setContentPane(panelPrincipal);

        revalidate();
        repaint();
    }

    private JPanel crearPanelReportes() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));

        JLabel lblTitulo = new JLabel("Reportes del Sistema");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelSuperior.setBackground(COLOR_FONDO);

        JButton btnConsumo = new JButton("Consumo por usuario");
        JButton btnSector = new JButton("Filtrar por sector");
        JButton btnMayorConsumo = new JButton("Mayor consumo");
        JButton btnDispositivos = new JButton("Dispositivos más usados");
        JButton btnResumen = new JButton("Resumen general");

        JButton[] botones = {btnConsumo, btnSector, btnMayorConsumo, btnDispositivos, btnResumen};

        for (JButton boton : botones) {

            boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
            boton.setBackground(COLOR_PRIMARIO);
            boton.setForeground(Color.WHITE);
            boton.setFocusPainted(false);
            boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            panelSuperior.add(boton);

        }

        JComboBox<Sector> cbSector = new JComboBox<>(Sector.values());
        cbSector.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelSuperior.add(cbSector);

        JTextArea areaReporte = new JTextArea();
        areaReporte.setEditable(false);
        areaReporte.setFont(new Font("Consolas", Font.PLAIN, 14));
        areaReporte.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(areaReporte);

        btnConsumo.addActionListener(e -> {

            StringBuilder reporte = new StringBuilder();

            reporte.append("========= CONSUMO POR USUARIO =========\n\n");

            for (Usuario u : sistema.getGestorUsuarios().getUsuarios()) {

                if (u.getRol() == Rol.ADMINISTRADOR) {
                    continue;
                }

                double consumoTotal = sistema.getCalculadora().calcularConsumoMensual(u);

                reporte.append("Nombre: ").append(u.getNombre()).append("\n");

                reporte.append("Cédula: ").append(u.getCedula()).append("\n");

                reporte.append("Tipo: ").append(u instanceof UsuarioEmpresa ? "Empresa" : "Residencial").append("\n");

                reporte.append("Sector: ").append(u.getSector()).append("\n");

                reporte.append("Consumo mensual: ").append(String.format("%.2f", consumoTotal)).append(" kWh\n");

                reporte.append("------------------------------------------\n");

            }

            areaReporte.setText(reporte.toString());

        });

        btnSector.addActionListener(e -> {

            Sector sectorSeleccionado = (Sector) cbSector.getSelectedItem();

            StringBuilder reporte = new StringBuilder();

            reporte.append("======= USUARIOS DEL SECTOR ").append(sectorSeleccionado).append(" =======\n\n");

            boolean existe = false;

            for (Usuario u : sistema.getGestorUsuarios().getUsuarios()) {

                if (u.getRol() == Rol.ADMINISTRADOR) {
                    continue;
                }

                if (u.getSector() == sectorSeleccionado) {

                    existe = true;

                    double consumo = sistema.getCalculadora().calcularConsumoMensual(u);

                    reporte.append("Nombre: ").append(u.getNombre()).append("\n");

                    reporte.append("Tipo: ").append(u instanceof UsuarioEmpresa ? "Empresa" : "Residencial").append("\n");

                    reporte.append("Consumo mensual: ").append(String.format("%.2f", consumo)).append(" kWh\n");

                    reporte.append("------------------------------------------\n");

                }

            }

            if (!existe) {

                reporte.append("No existen usuarios registrados en este sector.");

            }

            areaReporte.setText(reporte.toString());

        });

        btnMayorConsumo.addActionListener(e -> {

            Usuario mayor = null;
            double mayorConsumo = 0;

            for (Usuario u : sistema.getGestorUsuarios().getUsuarios()) {

                if (u.getRol() == Rol.ADMINISTRADOR) {
                    continue;
                }

                double consumo = sistema.getCalculadora().calcularConsumoMensual(u);

                if (consumo > mayorConsumo) {

                    mayorConsumo = consumo;
                    mayor = u;

                }

            }

            StringBuilder reporte = new StringBuilder();

            reporte.append("===== USUARIO CON MAYOR CONSUMO =====\n\n");

            if (mayor != null) {

                reporte.append("Nombre: ").append(mayor.getNombre()).append("\n");

                reporte.append("Cédula: ").append(mayor.getCedula()).append("\n");

                reporte.append("Sector: ").append(mayor.getSector()).append("\n");

                reporte.append("Consumo mensual: ").append(String.format("%.2f", mayorConsumo)).append(" kWh\n");

            }

            areaReporte.setText(reporte.toString());

        });
        btnDispositivos.addActionListener(e -> {

            Map<String, Integer> contador = new HashMap<>();

            for (Usuario u : sistema.getGestorUsuarios().getUsuarios()) {

                if (u.getRol() == Rol.ADMINISTRADOR) {
                    continue;
                }

                for (Inmueble inmueble : u.getInmuebles()) {

                    for (Dispositivo d : inmueble.getDispositivos()) {

                        String nombre = d.getNombre();

                        contador.put(nombre, contador.getOrDefault(nombre, 0) + d.getCantidad());

                    }

                }

            }

            StringBuilder reporte = new StringBuilder();

            reporte.append("===== DISPOSITIVOS MÁS UTILIZADOS =====\n\n");

            if (contador.isEmpty()) {

                reporte.append("No existen dispositivos registrados.");

            } else {

                for (Map.Entry<String, Integer> dato : contador.entrySet()) {

                    reporte.append(dato.getKey()).append(" : ").append(dato.getValue()).append(" unidades\n");

                }

            }

            areaReporte.setText(reporte.toString());

        });

        btnResumen.addActionListener(e -> {

            int totalUsuarios = 0;
            int residenciales = 0;
            int empresas = 0;
            int totalDispositivos = 0;
            double consumoTotal = 0;

            for (Usuario u : sistema.getGestorUsuarios().getUsuarios()) {

                if (u.getRol() == Rol.ADMINISTRADOR) {
                    continue;
                }

                totalUsuarios++;

                if (u instanceof UsuarioEmpresa) {
                    empresas++;
                } else {
                    residenciales++;
                }

                for (Inmueble inmueble : u.getInmuebles()) {

                    totalDispositivos += inmueble.getDispositivos().size();

                }

                consumoTotal += sistema.getCalculadora().calcularConsumoMensual(u);

            }

            StringBuilder reporte = new StringBuilder();

            reporte.append("========== RESUMEN GENERAL ==========\n\n");

            reporte.append("Usuarios registrados: ").append(totalUsuarios).append("\n");

            reporte.append("Usuarios residenciales: ").append(residenciales).append("\n");

            reporte.append("Empresas: ").append(empresas).append("\n");

            reporte.append("Dispositivos registrados: ").append(totalDispositivos).append("\n");

            reporte.append("Consumo mensual total: ").append(String.format("%.2f", consumoTotal)).append(" kWh\n");

            if (totalUsuarios > 0) {

                reporte.append("Consumo promedio por usuario: ").append(String.format("%.2f", consumoTotal / totalUsuarios)).append(" kWh\n");

            }

            areaReporte.setText(reporte.toString());

        });

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(panelSuperior, BorderLayout.CENTER);
        panel.add(scroll, BorderLayout.SOUTH);

        scroll.setPreferredSize(new Dimension(0, 420));

        return panel;

    }

    private JPanel crearPestanaDispositivos() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JPanel panelEntrada = new JPanel();
        panelEntrada.setLayout(new BoxLayout(panelEntrada, BoxLayout.Y_AXIS));
        panelEntrada.setBackground(Color.WHITE);
        panelEntrada.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Agregar Dispositivo"), BorderFactory.createEmptyBorder(16, 16, 16, 16)));

        //==============================
        // SELECCIÓN DE INMUEBLE
        //==============================

        JLabel lblInmueble = new JLabel("Vivienda:");
        lblInmueble.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JComboBox<Inmueble> cbInmuebles = new JComboBox<>();

        for (Inmueble inmueble : usuarioActual.getInmuebles()) {

            cbInmuebles.addItem(inmueble);

        }

        if (!usuarioActual.getInmuebles().isEmpty()) {

            inmuebleActual = usuarioActual.getInmuebles().get(0);

            cbInmuebles.setSelectedItem(inmuebleActual);

        }

        JButton btnNuevoInmueble = new JButton("Nueva vivienda");

        btnNuevoInmueble.setBackground(COLOR_SECUNDARIO);
        btnNuevoInmueble.setForeground(Color.WHITE);
        btnNuevoInmueble.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnNuevoInmueble.setFocusPainted(false);
        btnNuevoInmueble.setCursor(new Cursor(Cursor.HAND_CURSOR));

        cbInmuebles.addActionListener(e -> {

            inmuebleActual = (Inmueble) cbInmuebles.getSelectedItem();

            cargarTablaDispositivos(modeloDisp);

            actualizarInicio();

        });

        btnNuevoInmueble.addActionListener(e -> {

            try {

                JTextField txtNombre = new JTextField();

                JComboBox<TipoInmueble> cbTipo = new JComboBox<>(TipoInmueble.values());

                JPanel formulario = new JPanel(new GridLayout(2, 2, 8, 8));

                formulario.add(new JLabel("Nombre"));
                formulario.add(txtNombre);

                formulario.add(new JLabel("Tipo"));
                formulario.add(cbTipo);

                int opcion = JOptionPane.showConfirmDialog(this, formulario, "Nueva vivienda", JOptionPane.OK_CANCEL_OPTION);

                if (opcion != JOptionPane.OK_OPTION) {

                    return;

                }

                if (txtNombre.getText().trim().isEmpty()) {

                    JOptionPane.showMessageDialog(this, "Ingrese un nombre.");

                    return;

                }

                Inmueble nuevo = new Inmueble(generarIdInmueble(), txtNombre.getText().trim(), (TipoInmueble) cbTipo.getSelectedItem());

                usuarioActual.agregarInmueble(nuevo);

                cbInmuebles.addItem(nuevo);

                cbInmuebles.setSelectedItem(nuevo);

                inmuebleActual = nuevo;

                cargarTablaDispositivos(modeloDisp);

                actualizarInicio();

                JOptionPane.showMessageDialog(this, "Vivienda registrada correctamente.");

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this, ex.getMessage());

            }

        });

        //==============================
        // DISPOSITIVOS
        //==============================

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
        btnAgregar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnEditar = new JButton("Editar");
        btnEditar.setBackground(new Color(255, 193, 7));
        btnEditar.setForeground(Color.BLACK);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(239, 83, 80));
        btnEliminar.setForeground(Color.WHITE);

        String[] columnas = {"ID", "Dispositivo", "Potencia (W)", "Cantidad", "Horas/día", "Consumo (kWh/día)"};

        modeloDisp = new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {

                return false;

            }

        };

        tablaDisp = new JTable(modeloDisp);

        tablaDisp.setRowHeight(26);

        JScrollPane scroll = new JScrollPane(tablaDisp);

        cbDisp.addActionListener(e -> {

            boolean otro = "Otro dispositivo".equals(cbDisp.getSelectedItem());

            lblNombreOtro.setVisible(otro);
            tfNombreOtro.setVisible(otro);

            lblPotenciaOtro.setVisible(otro);
            tfPotenciaOtro.setVisible(otro);

            panelEntrada.revalidate();
            panelEntrada.repaint();

        });
        btnAgregar.addActionListener(e -> {

            try {

                if (inmuebleActual == null) {

                    JOptionPane.showMessageDialog(this, "Primero seleccione o cree una vivienda.");

                    return;

                }

                String seleccionado = (String) cbDisp.getSelectedItem();

                if (seleccionado == null || seleccionado.startsWith("Seleccione")) {

                    JOptionPane.showMessageDialog(this, "Seleccione un dispositivo.");

                    return;

                }

                int cantidad = Integer.parseInt(tfCant.getText().trim());

                double horas = Double.parseDouble(tfHoras.getText().trim());

                if (cantidad <= 0 || horas <= 0 || horas > 24) {

                    JOptionPane.showMessageDialog(this, "Cantidad u horas inválidas.");

                    return;

                }

                Dispositivo nuevo;

                if (seleccionado.equals("Otro dispositivo")) {

                    String nombre = tfNombreOtro.getText().trim();

                    if (nombre.isEmpty()) {

                        JOptionPane.showMessageDialog(this, "Ingrese el nombre del dispositivo.");

                        return;

                    }

                    double potencia = Double.parseDouble(tfPotenciaOtro.getText().trim());

                    if (potencia <= 0) {

                        JOptionPane.showMessageDialog(this, "La potencia debe ser mayor que cero.");

                        return;

                    }

                    nuevo = new Dispositivo(generarIdTemporal(), nombre, potencia, cantidad, horas);

                } else {

                    int id = obtenerIdDesdeItem(seleccionado);

                    Dispositivo base = catalogo.buscarDispositivo(id);

                    if (base == null) {

                        JOptionPane.showMessageDialog(this, "Dispositivo no encontrado.");

                        return;

                    }

                    nuevo = new Dispositivo(generarIdTemporal(), base.getNombre(), base.getPotencia(), cantidad, horas);

                }

                inmuebleActual.agregarDispositivo(nuevo);

                cargarTablaDispositivos(modeloDisp);

                actualizarInicio();

                tfCant.setText("");
                tfHoras.setText("");
                tfNombreOtro.setText("");
                tfPotenciaOtro.setText("");

                cbDisp.setSelectedIndex(0);

                JOptionPane.showMessageDialog(this, "Dispositivo agregado correctamente.");

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this, ex.getMessage());

            }

        });
        btnEditar.addActionListener(e -> {

            int fila = tablaDisp.getSelectedRow();

            if (fila == -1) {

                JOptionPane.showMessageDialog(this, "Seleccione un dispositivo.");

                return;

            }

            if (inmuebleActual == null) {

                JOptionPane.showMessageDialog(this, "Seleccione una vivienda.");

                return;

            }

            try {

                int id = Integer.parseInt(modeloDisp.getValueAt(fila, 0).toString());

                Dispositivo dispositivoEditar = inmuebleActual.buscarDispositivo(id);

                if (dispositivoEditar == null) {

                    JOptionPane.showMessageDialog(this, "No se encontró el dispositivo.");

                    return;

                }

                String nuevaCantidad = JOptionPane.showInputDialog(this, "Nueva cantidad:", dispositivoEditar.getCantidad());

                if (nuevaCantidad == null) {

                    return;

                }

                String nuevasHoras = JOptionPane.showInputDialog(this, "Nuevas horas de uso por día:", dispositivoEditar.getHorasUsoDiarias());

                if (nuevasHoras == null) {

                    return;

                }

                dispositivoEditar.setCantidad(Integer.parseInt(nuevaCantidad.trim()));

                dispositivoEditar.setHorasUsoDiarias(Double.parseDouble(nuevasHoras.trim()));

                cargarTablaDispositivos(modeloDisp);

                actualizarInicio();

                tablaDisp.clearSelection();

                JOptionPane.showMessageDialog(this, "Dispositivo actualizado correctamente.");

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this, ex.getMessage());

            }

        });

        btnEliminar.addActionListener(e -> {

            int fila = tablaDisp.getSelectedRow();

            if (fila == -1) {

                JOptionPane.showMessageDialog(this, "Seleccione un dispositivo.");

                return;

            }

            if (inmuebleActual == null) {

                JOptionPane.showMessageDialog(this, "Seleccione una vivienda.");

                return;

            }

            int opcion = JOptionPane.showConfirmDialog(this, "¿Eliminar el dispositivo seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION);

            if (opcion != JOptionPane.YES_OPTION) {

                return;

            }

            try {

                int id = Integer.parseInt(modeloDisp.getValueAt(fila, 0).toString());

                if (inmuebleActual.eliminarDispositivo(id)) {

                    cargarTablaDispositivos(modeloDisp);

                    actualizarInicio();

                    tablaDisp.clearSelection();

                    JOptionPane.showMessageDialog(this, "Dispositivo eliminado correctamente.");

                } else {

                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el dispositivo.");

                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this, ex.getMessage());

            }

        });
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));

        panelBotones.setBackground(Color.WHITE);

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

//==============================
// FORMULARIO
//==============================

        panelEntrada.add(lblInmueble);
        panelEntrada.add(Box.createVerticalStrut(5));

        JPanel panelInmueble = new JPanel(new BorderLayout(8, 0));

        panelInmueble.setBackground(Color.WHITE);

        panelInmueble.add(cbInmuebles, BorderLayout.CENTER);

        panelInmueble.add(btnNuevoInmueble, BorderLayout.EAST);

        panelEntrada.add(panelInmueble);

        panelEntrada.add(Box.createVerticalStrut(15));

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

//==============================
// CARGA INICIAL
//==============================

        if (inmuebleActual != null) {

            cargarTablaDispositivos(modeloDisp);

        }

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

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel contenedor = new JPanel();

        contenedor.setLayout(new GridLayout(3, 2, 20, 20));

        contenedor.setBackground(COLOR_FONDO);

        // ==========================
        // TARJETA USUARIO
        // ==========================

        String datosUsuario = "Nombre: " + usuarioActual.getNombre() + "\nCédula: " + usuarioActual.getCedula() + "\nSector: " + usuarioActual.getSector();

        if (inmuebleActual != null) {

            datosUsuario += "\nInmueble: " + inmuebleActual.getNombre() + "\nTipo: " + inmuebleActual.getTipo();

        }

        JPanel tarjetaUsuario = crearTarjeta("USUARIO", datosUsuario);

        // ==========================
        // TARJETA CONSUMO
        // ==========================

        double consumo = sistema.getCalculadora().calcularConsumoMensual(usuarioActual);

        JPanel tarjetaConsumo = crearTarjeta("CONSUMO ENERGÉTICO", String.format("%.2f kWh mensual", consumo));

        // ==========================
        // TARJETA ESTADO
        // ==========================

        String estado = sistema.getAnalizador().clasificarConsumo(usuarioActual);

        JPanel tarjetaEstado = crearTarjeta("ESTADO ENERGÉTICO", estado);

        // ==========================
        // TARJETA DISPOSITIVOS
        // ==========================

        int cantidad = 0;

        if (inmuebleActual != null) {

            cantidad = inmuebleActual.getDispositivos().size();

        }

        JPanel tarjetaDispositivos = crearTarjeta("DISPOSITIVOS", "Registrados: " + cantidad);

        // ==========================
        // TARJETA EMPRESA
        // ==========================

        String extra = "";

        if (usuarioActual instanceof UsuarioEmpresa) {

            UsuarioEmpresa emp = (UsuarioEmpresa) usuarioActual;

            extra = "RUC: " + emp.getRuc() + "\nActividad: " + emp.getActividadEconomica();

        }

        JPanel tarjetaExtra = crearTarjeta("INFORMACIÓN", extra.isEmpty() ? "Usuario residencial" : extra);

        contenedor.add(tarjetaUsuario);
        contenedor.add(tarjetaConsumo);
        contenedor.add(tarjetaEstado);
        contenedor.add(tarjetaDispositivos);
        contenedor.add(tarjetaExtra);

        panel.add(contenedor, BorderLayout.NORTH);

        return panel;

    }

    private JPanel crearTarjeta(String titulo, String contenido) {


        JPanel tarjeta = new JPanel();


        tarjeta.setLayout(new BorderLayout());


        tarjeta.setBackground(Color.WHITE);


        tarjeta.setBorder(BorderFactory.createCompoundBorder(

                BorderFactory.createLineBorder(new Color(210, 210, 210), 1),

                BorderFactory.createEmptyBorder(18, 18, 18, 18)));


        JLabel encabezado = new JLabel(titulo);


        encabezado.setFont(new Font("Segoe UI", Font.BOLD, 17));


        encabezado.setForeground(new Color(20, 60, 100));


        JTextArea texto = new JTextArea(contenido);


        texto.setEditable(false);


        texto.setLineWrap(true);


        texto.setWrapStyleWord(true);


        texto.setFont(new Font("Segoe UI", Font.PLAIN, 15));


        texto.setForeground(new Color(50, 50, 50));


        texto.setBackground(Color.WHITE);



    /*
        CAMBIO VISUAL DEL ESTADO ENERGÉTICO
    */

        /*
    CAMBIO VISUAL DEL ESTADO ENERGÉTICO
*/

        if (titulo.equals("ESTADO ENERGÉTICO")) {


            texto.setFont(new Font("Segoe UI", Font.BOLD, 24));


            texto.setAlignmentX(Component.CENTER_ALIGNMENT);


            switch (contenido) {


                case "BAJO":

                    texto.setForeground(new Color(0, 150, 0));

                    break;


                case "NORMAL":

                    texto.setForeground(new Color(0, 120, 220));

                    break;


                case "ALTO":

                    texto.setForeground(new Color(230, 140, 0));

                    break;


                case "CRITICO":

                    texto.setForeground(Color.RED);

                    break;
            }

        }


        tarjeta.add(encabezado, BorderLayout.NORTH);


        tarjeta.add(texto, BorderLayout.CENTER);


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

                // Cambia el nombre si tu archivo no se llama logo_inicio.jpg
                ImageIcon icono = new ImageIcon("SistemaConsumoElectrico\\recursos\\logo_inicio.jpg");
                Image img = icono.getImage();

                g2.drawImage(img, 0, 0, getWidth(), getHeight(), this);

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

            texto.append("Cédula: ").append(u.getCedula()).append(" | Nombre: ").append(u.getNombre()).append(" | Rol: ").append(u.getRol()).append(" | Sector: ").append(u.getSector());

            if (u instanceof UsuarioEmpresa) {

                UsuarioEmpresa emp = (UsuarioEmpresa) u;

                texto.append(" | Tipo: Empresa").append(" | RUC: ").append(emp.getRuc()).append(" | Actividad: ").append(emp.getActividadEconomica());

            } else {

                texto.append(" | Tipo: Residencial");

            }

            texto.append("\n");

        }

        return texto.toString();

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

        int mayor = 999;

        if (usuarioActual == null) {

            return 1000;

        }

        for (Inmueble inmueble : usuarioActual.getInmuebles()) {

            for (Dispositivo dispositivo : inmueble.getDispositivos()) {

                if (dispositivo.getId() > mayor) {

                    mayor = dispositivo.getId();

                }

            }

        }

        return mayor + 1;

    }

    private int generarIdInmueble() {

        int mayor = 0;

        for (Usuario u : sistema.getGestorUsuarios().getUsuarios()) {

            for (Inmueble i : u.getInmuebles()) {

                if (i.getId() > mayor) {

                    mayor = i.getId();

                }

            }

        }

        return mayor + 1;

    }

    private void estilizarTextField(JTextField tf) {
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBackground(Color.WHITE);
        tf.setForeground(COLOR_TEXTO);
        tf.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1), BorderFactory.createEmptyBorder(5, 8, 5, 8)));
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
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
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

            boolean titulo = raw.startsWith("====") || raw.endsWith(":") || x.contains("resumen") || x.contains("analisis") || x.contains("análisis") || x.contains("factura") || x.contains("recomend");

            boolean esCritico = x.contains("critico") || x.contains("crítico");
            boolean esAlto = x.contains("alto");
            boolean esMedio = x.contains("medio") || x.contains("normal") || x.contains("moderado") || x.contains("moderada");
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

    private final Map<String, Double> ultimoConsumoDiarioPorUsuario = new HashMap<>();

    private double calcularConsumoDiarioKwh(Usuario usuario) {

        double total = 0.0;

        if (usuario == null || inmuebleActual == null) {

            return total;

        }

        for (Dispositivo d : inmuebleActual.getDispositivos()) {

            // kWh diarios = (W * cantidad * horas) / 1000
            total += (d.getPotencia() * d.getCantidad() * d.getHorasUsoDiarias()) / 1000.0;

        }

        return total;

    }

    private Dispositivo obtenerDispositivoMayorConsumo(Usuario usuario) {

        Dispositivo mayor = null;
        double max = -1.0;

        if (usuario == null || inmuebleActual == null) {

            return null;

        }

        for (Dispositivo d : inmuebleActual.getDispositivos()) {

            double consumo = (d.getPotencia() * d.getCantidad() * d.getHorasUsoDiarias()) / 1000.0;

            if (consumo > max) {

                max = consumo;
                mayor = d;

            }

        }

        return mayor;

    }

    private void cargarTablaDispositivos(DefaultTableModel modelo) {

        modelo.setRowCount(0);

        if (usuarioActual == null || inmuebleActual == null) {

            return;

        }

        for (Dispositivo d : inmuebleActual.getDispositivos()) {

            double consumo = (d.getPotencia() * d.getCantidad() * d.getHorasUsoDiarias()) / 1000.0;

            modelo.addRow(new Object[]{d.getId(), d.getNombre(), String.format("%.0f", d.getPotencia()), d.getCantidad(), String.format("%.2f", d.getHorasUsoDiarias()), String.format("%.2f", consumo)});

        }

    }

    private void actualizarInicio() {

        if (pestanas != null) {

            pestanas.setComponentAt(0, crearPestanaInicio());

            pestanas.revalidate();

            pestanas.repaint();

        }

    }

    private String generarMensajeAlertaInteligente(Usuario usuario) {
        String nombre = usuario.getNombre();
        double consumoActual = calcularConsumoDiarioKwh(usuario);
        Double consumoAnterior = ultimoConsumoDiarioPorUsuario.get(usuario.getCedula());

        // Guardamos para comparar en el próximo inicio
        ultimoConsumoDiarioPorUsuario.put(usuario.getCedula(), consumoActual);

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
            html.append("Tu consumo actual estimado es <b>").append(String.format("%.2f", consumoActual)).append(" kWh/día</b>.");
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