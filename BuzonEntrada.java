import java.util.ArrayList;

public class BuzonEntrada {
    private int capacidad;
    private ArrayList<Correo> buzon = new ArrayList<>();
    private boolean produccionTerminada = false; // AÑADIR esta variable

    public BuzonEntrada(int capacidad){
        this.capacidad = capacidad;
    }

    // AÑADIR este método
    public synchronized void terminarProduccion() {
        this.produccionTerminada = true;
        notifyAll(); // Despertar a todos los hilos esperando
    }

    public synchronized void enviarCorreo(Correo correo){
        while(buzon.size() == capacidad){
            try {
                wait();
            } catch (Exception e) {
            }
        }
        buzon.add(correo);
        notifyAll();
    }

    public synchronized Correo analizarSpam(){
        // MODIFICAR esta condición
        while(buzon.size() == 0 && !produccionTerminada){
            try {
                wait();
            } catch (Exception e) {
            }
        }
        
        // AÑADIR esta verificación
        if (buzon.size() == 0 && produccionTerminada) {
            return null; // Señal para terminar
        }
        
        Correo correo = buzon.remove(0);
        notifyAll();
        return correo;
    }
}