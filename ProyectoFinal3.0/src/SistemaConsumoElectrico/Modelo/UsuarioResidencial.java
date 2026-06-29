package SistemaConsumoElectrico.Modelo;

import SistemaConsumoElectrico.Excepciones.DatoInvalidoException;

public class UsuarioResidencial extends Usuario {

    private static final double TARIFA_RESIDENCIAL = 0.10;

    public UsuarioResidencial(int id, String nombre, Sector sector) throws DatoInvalidoException {

        super(id, nombre, sector);
    }

    @Override
    public double obtenerTarifa(){

        return TARIFA_RESIDENCIAL;

    }

    @Override
    public String mostrarInformacion(){

        return "\n===== USUARIO RESIDENCIAL ====="
                + "\nID: " + id
                + "\nNombre: " + nombre
                + "\nSector: " + sector
                + "\nTarifa: $"
                + obtenerTarifa()
                + " por kWh"
                + "\nDispositivos registrados: "
                + dispositivos.size();

    }
}