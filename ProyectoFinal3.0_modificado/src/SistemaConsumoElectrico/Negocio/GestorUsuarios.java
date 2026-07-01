package SistemaConsumoElectrico.Negocio;

import SistemaConsumoElectrico.Excepciones.DatoInvalidoException;
import SistemaConsumoElectrico.Modelo.*;

import java.util.ArrayList;

public class GestorUsuarios {

    private ArrayList<Usuario> usuarios;

    public GestorUsuarios() {

        usuarios = new ArrayList<>();

        // Administrador por defecto
        try {

            Usuario administrador = new UsuarioEmpresa("1710034065", "Administracion", "admin123", Sector.CENTRO, "1799999999001", "Administración del sistema", Rol.ADMINISTRADOR);

            usuarios.add(administrador);

        } catch (DatoInvalidoException e) {

            e.printStackTrace();

        }

    }

    public void agregarUsuario(Usuario usuario

    ) throws DatoInvalidoException {

        if (usuario == null) {

            throw new DatoInvalidoException("No se puede registrar un usuario vacío");

        }

        Usuario existente = buscarUsuario(usuario.getCedula());

        if (existente != null) {

            // Si ya existía pero estaba desactivado,
            // simplemente se reactiva.

            if (!existente.isActivo()) {

                existente.setActivo(true);

                return;

            }

            throw new DatoInvalidoException("Ya existe un usuario registrado con esa cédula");

        }

        usuarios.add(usuario);

    }

    public Usuario buscarUsuario(String cedula

    ) {

        for (Usuario u : usuarios) {

            if (u.getCedula().equals(cedula)) {

                return u;

            }

        }

        return null;

    }

    public Usuario validarLogin(String cedula, String contraseña

    ) throws DatoInvalidoException {

        Usuario usuario = buscarUsuario(cedula);

        if (usuario == null) {

            throw new DatoInvalidoException("La cédula no se encuentra registrada");

        }

        if (!usuario.getContraseña().equals(contraseña)) {

            throw new DatoInvalidoException("Contraseña incorrecta");

        }

        return usuario;

    }

    // Ahora solo desactiva

    public boolean eliminarUsuario(String cedula

    ) {

        Usuario usuario = buscarUsuario(cedula);

        if (usuario == null) {

            return false;

        }

        if (usuario.getRol() == Rol.ADMINISTRADOR) {

            return false;

        }

        usuario.setActivo(false);

        return true;

    }

    // Nuevo método

    public boolean reactivarUsuario(String cedula

    ) {

        Usuario usuario = buscarUsuario(cedula);

        if (usuario == null) {

            return false;

        }

        usuario.setActivo(true);

        return true;

    }

    public String obtenerListaUsuarios() {

        if (usuarios.isEmpty()) {

            return "No existen usuarios registrados.";

        }

        StringBuilder texto = new StringBuilder();

        texto.append("\n===== USUARIOS REGISTRADOS =====\n");

        for (Usuario u : usuarios) {

            texto.append("Cédula: ").append(u.getCedula()).append(" | Nombre: ").append(u.getNombre()).append(" | Rol: ").append(u.getRol()).append(" | Sector: ").append(u.getSector()).append(" | Estado: ").append(u.isActivo() ? "ACTIVO" : "INACTIVO").append("\n");

        }

        return texto.toString();

    }

    public int cantidadUsuarios() {

        return usuarios.size();

    }

    public ArrayList<Usuario> getUsuarios() {

        return usuarios;

    }

    public ArrayList<Usuario> getUsuariosVisibles() {

        ArrayList<Usuario> visibles = new ArrayList<>();

        for (Usuario u : usuarios) {

            if (u.getRol() != Rol.ADMINISTRADOR) {

                visibles.add(u);

            }

        }

        return visibles;

    }

}