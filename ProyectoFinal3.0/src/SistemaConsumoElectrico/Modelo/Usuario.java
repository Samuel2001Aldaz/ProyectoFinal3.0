package SistemaConsumoElectrico.Modelo;

import SistemaConsumoElectrico.Excepciones.DatoInvalidoException;
import java.util.ArrayList;

public abstract class Usuario extends Persona implements Facturable {

    protected int id;
    protected Sector sector;
    protected ArrayList<Dispositivo> dispositivos;

    public Usuario(int id, String nombre, Sector sector) throws DatoInvalidoException {

        super(nombre);

        if(id <= 0){

            throw new DatoInvalidoException("El ID del usuario debe ser mayor a cero");
        }

        if(sector == null){

            throw new DatoInvalidoException("El sector no puede ser nulo");
        }

        this.id = id;

        this.sector = sector;

        dispositivos = new ArrayList<>();
    }

    public int getId(){

        return id;

    }

    public Sector getSector(){

        return sector;

    }

    public ArrayList<Dispositivo> getDispositivos(){

        return dispositivos;

    }

    public void agregarDispositivo(Dispositivo dispositivo) throws DatoInvalidoException {

        if(dispositivo == null){

            throw new DatoInvalidoException("No se puede agregar un dispositivo vacío");

        }

        dispositivos.add(dispositivo);
    }

    public boolean eliminarDispositivo(int idDispositivo){

        return dispositivos.removeIf(d -> d.getId() == idDispositivo);
    }

    public String obtenerListaDispositivos(){

        if(dispositivos.isEmpty()){

            return "No existen dispositivos registrados.";

        }

        StringBuilder texto = new StringBuilder();

        for(Dispositivo d : dispositivos){

            texto.append(d).append("\n");
        }

        return texto.toString();
    }
}