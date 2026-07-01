package SistemaConsumoElectrico.Modelo;

import SistemaConsumoElectrico.Excepciones.DatoInvalidoException;

public class UsuarioEmpresa extends Usuario {

    private String ruc;
    private String actividadEconomica;

    private static final double TARIFA_EMPRESA = 0.15;

    // Constructor normal (todos los usuarios registrados serán USUARIO)
    public UsuarioEmpresa(String cedula, String nombre, String contraseña, Sector sector, String ruc, String actividadEconomica

    ) throws DatoInvalidoException {

        super(cedula, nombre, contraseña, sector);

        validarEmpresa(ruc, actividadEconomica);

        this.ruc = ruc;
        this.actividadEconomica = actividadEconomica;

    }

    // Constructor para poder crear administradores
    public UsuarioEmpresa(String cedula, String nombre, String contraseña, Sector sector, String ruc, String actividadEconomica, Rol rol

    ) throws DatoInvalidoException {

        super(cedula, nombre, contraseña, sector, rol);

        validarEmpresa(ruc, actividadEconomica);

        this.ruc = ruc;
        this.actividadEconomica = actividadEconomica;

    }

    private void validarEmpresa(String ruc, String actividadEconomica

    ) throws DatoInvalidoException {

        if (ruc == null || !ruc.matches("\\d{13}")) {

            throw new DatoInvalidoException("El RUC debe contener exactamente 13 números");

        }

        if (actividadEconomica == null || actividadEconomica.trim().isEmpty()) {

            throw new DatoInvalidoException("La actividad económica no puede estar vacía");

        }

    }

    public String getRuc() {

        return ruc;

    }

    public String getActividadEconomica() {

        return actividadEconomica;

    }

    public void setActividadEconomica(String actividadEconomica) throws DatoInvalidoException {

        if (actividadEconomica == null || actividadEconomica.trim().isEmpty()) {

            throw new DatoInvalidoException("La actividad económica no puede estar vacía");

        }

        this.actividadEconomica = actividadEconomica;

    }

    @Override
    public double obtenerTarifa() {

        return TARIFA_EMPRESA;

    }

    @Override
    public void mostrarInformacion() {

        System.out.println("\n===== EMPRESA =====");

        System.out.println("Rol: " + getRol());

        System.out.println("Cédula representante: " + cedula);

        System.out.println("Nombre empresa: " + nombre);

        System.out.println("RUC: " + ruc);

        System.out.println("Actividad económica: " + actividadEconomica);

        System.out.println("Sector: " + sector);

        System.out.println("Tarifa: $" + TARIFA_EMPRESA + " por kWh");

        int totalDispositivos = 0;

        for (Inmueble inmueble : inmuebles) {

            totalDispositivos += inmueble.getDispositivos().size();

        }

        System.out.println("Dispositivos registrados: " + totalDispositivos);

    }

}