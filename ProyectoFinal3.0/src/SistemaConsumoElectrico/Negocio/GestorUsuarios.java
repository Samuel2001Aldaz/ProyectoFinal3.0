package SistemaConsumoElectrico.Negocio;

import SistemaConsumoElectrico.Excepciones.DatoInvalidoException;
import SistemaConsumoElectrico.Modelo.Usuario;
import java.util.ArrayList;

public class GestorUsuarios {

    private ArrayList<Usuario> usuarios;

    public GestorUsuarios(){

        usuarios = new ArrayList<>();
    }

    public void agregarUsuario(Usuario usuario) throws DatoInvalidoException {

        if(usuario == null){

            throw new DatoInvalidoException("No se puede registrar un usuario vacío.");

        }

        if(buscarUsuario(usuario.getId()) != null){

            throw new DatoInvalidoException("Ya existe un usuario con ese ID.");

        }

        usuarios.add(usuario);
    }

    public Usuario buscarUsuario(int id){

        for(Usuario u : usuarios){

            if(u.getId() == id){

                return u;

            }
        }

        return null;
    }

    public void eliminarUsuario(int id) throws DatoInvalidoException {

        Usuario usuario = buscarUsuario(id);

        if(usuario == null){

            throw new DatoInvalidoException("No existe un usuario con ese ID.");

        }

        usuarios.remove(usuario);
    }

    public String generarListaUsuarios(){

        StringBuilder texto = new StringBuilder();

        if(usuarios.isEmpty()){

            return "No existen usuarios registrados.";

        }

        texto.append("\n===== USUARIOS REGISTRADOS =====\n");

        for(Usuario u : usuarios){

            texto.append("ID: "

                    + u.getId()

                    + " | Nombre: "

                    + u.getNombre()

                    + " | Sector: "

                    + u.getSector()

                    + "\n"
            );
        }

        return texto.toString();
    }

    public int cantidadUsuarios(){

        return usuarios.size();

    }

    public ArrayList<Usuario> getUsuarios(){

        return usuarios;

    }
}