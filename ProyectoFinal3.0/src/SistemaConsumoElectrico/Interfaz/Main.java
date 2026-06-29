package SistemaConsumoElectrico.Interfaz;

import SistemaConsumoElectrico.Modelo.*;
import SistemaConsumoElectrico.Negocio.*;
import SistemaConsumoElectrico.Excepciones.DatoInvalidoException;
import java.util.Scanner;

public class Main {

    private static Usuario usuario;

    public static void main(String[] args) throws DatoInvalidoException {

        Scanner sc = new Scanner(System.in);

        SistemaEnergetico sistema = new SistemaEnergetico();

        CatalogoDispositivos catalogo = new CatalogoDispositivos();

        int opcion;

        do{

            System.out.println("\n==============================");

            System.out.println(" SISTEMA CONSUMO ENERGÉTICO");

            System.out.println("==============================");

            System.out.println("1. Registrar usuario residencial");

            System.out.println("2. Registrar empresa");

            System.out.println("3. Mostrar usuarios");

            System.out.println("4. Seleccionar usuario");

            System.out.println("5. Eliminar usuario");

            System.out.println("6. Salir");

            opcion = leerEntero(sc, "Seleccione opción: ");

            try{

                switch(opcion){

                    case 1:

                        registrarResidencial(sc, sistema);
                        break;

                    case 2:

                        registrarEmpresa(sc, sistema);
                        break;

                    case 3:

                        System.out.println(sistema.getGestorUsuarios().generarListaUsuarios());
                        break;

                    case 4:

                        seleccionarUsuario(sc, sistema, catalogo);
                        break;

                    case 5:

                        int id = leerEntero(sc, "ID usuario eliminar: ");

                        sistema.getGestorUsuarios().eliminarUsuario(id);

                        System.out.println("Usuario eliminado.");
                        break;

                    case 6:

                        System.out.println("Sistema finalizado.");
                        break;

                    default:

                        System.out.println("Opción inválida.");
                }
            }
            catch(DatoInvalidoException e){

                System.out.println("ERROR: "
                        + e.getMessage());
            }

        }while(opcion != 6);

        sc.close();
    }

    private static void registrarResidencial(Scanner sc, SistemaEnergetico sistema) throws DatoInvalidoException {

        int id = leerEntero(sc, "ID: ");

        System.out.print("Nombre: ");

        String nombre;

        while(true){

            nombre = sc.nextLine();

            if(!nombre.trim().isEmpty()){
                break;
            }

            System.out.println("El nombre no puede estar vacío:");
        }

        Sector sector = seleccionarSector(sc);

        UsuarioResidencial usuario = new UsuarioResidencial(id, nombre, sector);

        sistema.getGestorUsuarios().agregarUsuario(usuario);

        System.out.println("Usuario registrado.");

    }

    private static void registrarEmpresa(Scanner sc, SistemaEnergetico sistema) throws DatoInvalidoException {

        int id = leerEntero(sc, "ID empresa: ");

        System.out.print("Nombre empresa: ");

        String nombre;

        while(true){

            nombre = sc.nextLine();

            if(!nombre.trim().isEmpty()){
                break;
            }

            System.out.println("El nombre no puede estar vacío:");
        }

        Sector sector = seleccionarSector(sc);

        System.out.print("RUC: ");

        String ruc = sc.nextLine();

        System.out.print("Actividad económica: ");

        String actividad = sc.nextLine();

        UsuarioEmpresa empresa = new UsuarioEmpresa(id, nombre, sector, ruc, actividad);

        sistema.getGestorUsuarios().agregarUsuario(empresa);

        System.out.println("Empresa registrada.");
    }

    private static void seleccionarUsuario(Scanner sc, SistemaEnergetico sistema, CatalogoDispositivos catalogo) throws DatoInvalidoException {

        int id = leerEntero(sc, "ID usuario: ");

        Usuario usuario = sistema.getGestorUsuarios().buscarUsuario(id);

        if(usuario == null){

            System.out.println("No existe un usuario con ese ID.");

            return;
        }

        menuUsuario(sc, sistema, catalogo, usuario);

    }

    private static void menuUsuario(Scanner sc, SistemaEnergetico sistema, CatalogoDispositivos catalogo, Usuario usuario) throws DatoInvalidoException {

        int opcion;

        do{

            System.out.println("\nUsuario actual: " + usuario.getNombre());

            System.out.println("1. Ver dispositivos");

            System.out.println("2. Ver resumen");

            System.out.println("3. Ver análisis");

            System.out.println("4. Factura");

            System.out.println("5. Recomendaciones");

            System.out.println("6. Agregar dispositivo catálogo");

            System.out.println("7. Regresar");

            opcion = leerEntero(sc, "Opción: ");

            switch(opcion){

                case 1:

                    System.out.println(usuario.obtenerListaDispositivos());
                    break;

                case 2:

                    System.out.println(sistema.obtenerResumenUsuario(usuario));
                    break;

                case 3:

                    System.out.println(sistema.obtenerAnalisisUsuario(usuario));
                    break;

                case 4:

                    System.out.println(sistema.generarFactura(usuario));
                    break;

                case 5:

                    System.out.println(sistema.obtenerRecomendaciones(usuario));
                    break;

                case 6:

                    agregarCatalogo(sc, catalogo, usuario);
                    break;
            }

        }while(opcion != 7);
    }

    private static void agregarCatalogo(Scanner sc, CatalogoDispositivos catalogo, Usuario usuario) throws DatoInvalidoException {

        System.out.println(catalogo.mostrarCatalogo());

        int id = leerEntero(sc, "Seleccione dispositivo: ");

        Dispositivo base = catalogo.buscarDispositivo(id);

        if(base == null){

            System.out.println("Dispositivo no encontrado.");
            return;
        }

        int cantidad;

        do{
            cantidad = leerEntero(sc, "Cantidad: ");

            if(cantidad <=0){

                System.out.println("Debe ser mayor que cero.");
            }

        }while(cantidad <=0);

        double horas = leerDouble(sc, "Horas uso diario: ");

        Dispositivo nuevo = new Dispositivo(base.getId(), base.getNombre(), base.getPotencia(), cantidad, horas);

        try{

            usuario.agregarDispositivo(nuevo);

            System.out.println("Dispositivo agregado.");

        }
        catch(DatoInvalidoException e){

            System.out.println(e.getMessage());

        }
        catch(Exception e){

            System.out.println("Error inesperado.");
        }
    }

    private static Sector seleccionarSector(Scanner sc) {

        Sector[] sectores = Sector.values();

        while(true){

            System.out.println("\nSeleccione sector:");

            for(int i = 0; i < sectores.length; i++){

                System.out.println((i + 1) + ". " + sectores[i]);

            }

            int opcion = leerEntero(sc, "Opción: ");


            if(opcion >= 1 && opcion <= sectores.length){

                return sectores[opcion - 1];

            }

            System.out.println("Sector inválido. Intente nuevamente.");
        }
    }

    private static int leerEntero(Scanner sc, String mensaje){

        while(true){

            try{

                System.out.print(mensaje);

                int valor = Integer.parseInt(sc.nextLine());

                return valor;
            }
            catch(Exception e){

                System.out.println("Debe ingresar un número entero.");

            }
        }
    }

    private static double leerDouble(Scanner sc, String mensaje){

        while(true){

            try{

                System.out.print(mensaje);

                double valor = Double.parseDouble(sc.nextLine());

                if(valor < 0){

                    System.out.println("El valor no puede ser negativo.");

                    continue;
                }

                return valor;
            }
            catch(Exception e){

                System.out.println("Ingrese un valor válido.");
            }
        }
    }
}