package SistemaConsumoElectrico.Negocio;

import SistemaConsumoElectrico.Modelo.Usuario;

public class Factura {

    private Usuario usuario;

    private CalculadoraConsumo calculadora;

    private static final double IVA = 0.15;

    public Factura(Usuario usuario){

        this.usuario = usuario;
        calculadora = new CalculadoraConsumo();
    }

    public double calcularSubtotal(){

        return calculadora.calcularCostoMensual(usuario);
    }

    public double calcularIVA(){

        return calcularSubtotal() * IVA;
    }

    public double calcularTotal(){

        return calcularSubtotal() + calcularIVA();
    }

    public String generarFactura(){

        StringBuilder texto = new StringBuilder();

        texto.append("\n==================================\n");

        texto.append("        FACTURA DE CONSUMO\n");

        texto.append("==================================\n");

        texto.append("Cliente: " + usuario.getNombre() + "\n");

        texto.append("Sector: " + usuario.getSector() + "\n");

        texto.append(String.format("Consumo mensual: %.2f kWh\n", calculadora.calcularConsumoMensual(usuario)));

        texto.append(String.format("Tarifa aplicada: $%.2f por kWh\n", usuario.obtenerTarifa()));

        texto.append(String.format("Subtotal: $%.2f\n", calcularSubtotal()));

        texto.append(String.format("IVA (15%%): $%.2f\n", calcularIVA()));

        texto.append(String.format("TOTAL A PAGAR: $%.2f\n", calcularTotal()));

        texto.append("==================================");

        return texto.toString();
    }
}