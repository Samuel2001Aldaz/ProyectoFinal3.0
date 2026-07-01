package SistemaConsumoElectrico.Modelo;

import SistemaConsumoElectrico.Excepciones.DatoInvalidoException;

public abstract class Persona {

    protected String nombre;

    public Persona(String nombre) throws DatoInvalidoException {

        validarNombre(nombre);

        this.nombre = nombre;
    }

    public String getNombre() {

        return nombre;
    }

    public void setNombre(String nombre) throws DatoInvalidoException {

        validarNombre(nombre);

        this.nombre = nombre;
    }

    private void validarNombre(String nombre) throws DatoInvalidoException {

        if (nombre == null || nombre.isBlank()) {

            throw new DatoInvalidoException("El nombre de la persona no puede estar vacío");

        }
    }


    public abstract void mostrarInformacion();

}
