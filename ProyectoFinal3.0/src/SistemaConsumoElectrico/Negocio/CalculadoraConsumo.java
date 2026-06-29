package SistemaConsumoElectrico.Negocio;

import SistemaConsumoElectrico.Modelo.Dispositivo;
import SistemaConsumoElectrico.Modelo.Usuario;

public class CalculadoraConsumo {

    public double calcularConsumoDiario(Usuario usuario){

        double total = 0;

        for(Dispositivo d : usuario.getDispositivos()){

            total += d.calcularConsumoDiario();
        }

        return total;
    }

    public double calcularConsumoMensual(Usuario usuario){

        double total = 0;

        for(Dispositivo d : usuario.getDispositivos()){

            total += d.calcularConsumoMensual();

        }

        return total;
    }

    public double calcularConsumoAnual(Usuario usuario){

        double total = 0;

        for(Dispositivo d : usuario.getDispositivos()){

            total += d.calcularConsumoAnual();

        }

        return total;
    }

    public double calcularCostoMensual(Usuario usuario){

        return calcularConsumoMensual(usuario) * usuario.obtenerTarifa();
    }

    public double calcularCostoAnual(Usuario usuario){

        return calcularConsumoAnual(usuario) * usuario.obtenerTarifa();
    }

    public String generarResumen(Usuario usuario){

        StringBuilder texto = new StringBuilder();

        texto.append("\n===== RESUMEN ENERGÉTICO =====\n");

        texto.append(String.format("Consumo diario: %.2f kWh\n", calcularConsumoDiario(usuario)));

        texto.append(String.format("Consumo mensual: %.2f kWh\n", calcularConsumoMensual(usuario)));

        texto.append(String.format("Consumo anual: %.2f kWh\n", calcularConsumoAnual(usuario)));

        texto.append(String.format("Costo mensual estimado: $%.2f\n", calcularCostoMensual(usuario)));

        texto.append(String.format("Costo anual estimado: $%.2f\n", calcularCostoAnual(usuario)));

        return texto.toString();
    }
}