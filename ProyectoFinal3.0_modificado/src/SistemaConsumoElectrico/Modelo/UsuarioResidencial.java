package SistemaConsumoElectrico.Modelo;

import SistemaConsumoElectrico.Excepciones.DatoInvalidoException;

public class UsuarioResidencial extends Usuario {

    private static final double TARIFA_RESIDENCIAL = 0.10;

    public UsuarioResidencial(String cedula, String nombre, String contraseña, Sector sector

    ) throws DatoInvalidoException {

        super(cedula, validarNombre(nombre), contraseña, sector);

    }

    private static String validarNombre(String nombre

    ) throws DatoInvalidoException {

        if (nombre == null || nombre.trim().isEmpty()) {

            throw new DatoInvalidoException("El nombre no puede estar vacío.");

        }

        if (!nombre.matches("[A-Za-zÁÉÍÓÚáéíóúÑñ ]+")) {

            throw new DatoInvalidoException("El nombre del usuario residencial solo puede contener letras y espacios.");

        }

        return nombre.trim();

    }

    @Override
    public double obtenerTarifa() {

        return TARIFA_RESIDENCIAL;

    }

    @Override
    public void mostrarInformacion() {

        System.out.println("\n===== USUARIO RESIDENCIAL =====");

        System.out.println("Cédula: " + cedula);

        System.out.println("Nombre: " + nombre);

        System.out.println("Sector: " + sector);

        System.out.println("Rol: " + rol);

        System.out.println("Tarifa: $" + TARIFA_RESIDENCIAL + " por kWh");

        int totalDispositivos = 0;

        for (Inmueble inmueble : inmuebles) {

            totalDispositivos += inmueble.getDispositivos().size();

        }

        System.out.println("Dispositivos registrados: " + totalDispositivos);

    }

}