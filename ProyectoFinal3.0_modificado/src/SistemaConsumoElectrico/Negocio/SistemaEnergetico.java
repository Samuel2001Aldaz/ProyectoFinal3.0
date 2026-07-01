package SistemaConsumoElectrico.Negocio;

import SistemaConsumoElectrico.Modelo.Dispositivo;
import SistemaConsumoElectrico.Modelo.Usuario;

public class SistemaEnergetico {

    private GestorUsuarios gestorUsuarios;

    private CalculadoraConsumo calculadora;

    private AnalizadorConsumo analizador;

    private Recomendador recomendador;

    public SistemaEnergetico() {

        gestorUsuarios = new GestorUsuarios();

        calculadora = new CalculadoraConsumo();

        analizador = new AnalizadorConsumo();

        recomendador = new Recomendador();

    }

    public GestorUsuarios getGestorUsuarios() {

        return gestorUsuarios;

    }

    public CalculadoraConsumo getCalculadora() {

        return calculadora;

    }

    public AnalizadorConsumo getAnalizador() {

        return analizador;

    }

    //=========================================================
    // FUNCIONES DEL USUARIO
    //=========================================================

    public String obtenerResumenUsuario(Usuario usuario) {

        return calculadora.generarResumen(usuario);

    }

    public String obtenerAnalisisUsuario(Usuario usuario) {

        return analizador.generarAnalisis(usuario);

    }

    public String obtenerRecomendaciones(Usuario usuario) {

        return recomendador.generarRecomendaciones(usuario);

    }

    public String generarFactura(Usuario usuario) {

        Factura factura = new Factura(usuario);

        return factura.generarFactura();

    }

    //=========================================================
    // REPORTES DEL ADMINISTRADOR
    //=========================================================

    public Usuario obtenerUsuarioMayorConsumo() {

        return calculadora.obtenerUsuarioMayorConsumo(gestorUsuarios.getUsuarios());

    }

    public double obtenerConsumoPromedio() {

        return calculadora.calcularConsumoPromedio(gestorUsuarios.getUsuarios());

    }

    public double obtenerConsumoTotalSistema() {

        return calculadora.calcularConsumoTotal(gestorUsuarios.getUsuarios());

    }

    public Dispositivo obtenerDispositivoMayorConsumo(Usuario usuario) {

        return calculadora.obtenerDispositivoMayorConsumo(usuario);

    }

    public double obtenerConsumoMensualUsuario(Usuario usuario) {

        return calculadora.calcularConsumoMensual(usuario);

    }

    public int obtenerCantidadDispositivos(Usuario usuario) {

        return calculadora.contarDispositivos(usuario);

    }

}