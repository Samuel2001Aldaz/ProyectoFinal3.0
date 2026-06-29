package SistemaConsumoElectrico.Modelo;

import SistemaConsumoElectrico.Excepciones.DatoInvalidoException;

public class UsuarioEmpresa extends Usuario {

    private String ruc;
    private String actividadEconomica;
    private static final double TARIFA_EMPRESA = 0.15;

    public UsuarioEmpresa(int id, String nombre, Sector sector, String ruc, String actividadEconomica) throws DatoInvalidoException {

        super(id, nombre, sector);

        validarEmpresa(ruc, actividadEconomica);

        this.ruc = ruc;

        this.actividadEconomica = actividadEconomica;
    }

    private void validarEmpresa(String ruc, String actividad) throws DatoInvalidoException {

        if (ruc == null || ruc.isBlank()) {

            throw new DatoInvalidoException("El RUC no puede estar vacío");
        }

        if (actividad == null || actividad.isBlank()) {

            throw new DatoInvalidoException("La actividad económica no puede estar vacía");
        }
    }

    public String getRuc(){

        return ruc;

    }

    public String getActividadEconomica(){

        return actividadEconomica;

    }

    public void setActividadEconomica(String actividadEconomica) throws DatoInvalidoException {

        if(actividadEconomica == null || actividadEconomica.isBlank()){

            throw new DatoInvalidoException("Actividad económica inválida");
        }

        this.actividadEconomica = actividadEconomica;
    }

    @Override
    public double obtenerTarifa(){

        return TARIFA_EMPRESA;

    }

    @Override
    public String mostrarInformacion(){

        return "\n===== EMPRESA ====="
                + "\nID: " + id
                + "\nNombre Empresa: " + nombre
                + "\nRUC: " + ruc
                + "\nActividad: " + actividadEconomica
                + "\nSector: " + sector
                + "\nTarifa: $"
                + obtenerTarifa()
                + " por kWh"
                + "\nDispositivos registrados: "
                + dispositivos.size();

    }
}