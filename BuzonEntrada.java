import java.util.ArrayList;

public class BuzonEntrada {
    private int capacidad;
    private ArrayList<Correo> buzon = new ArrayList<>();
    private boolean produccionTerminada = false; 
    public BuzonEntrada(int capacidad){
        this.capacidad = capacidad;
    }

    public synchronized void terminarProduccion() {
        this.produccionTerminada = true;
        notifyAll(); 
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
        while(buzon.size() == 0 && !produccionTerminada){
            try {
                wait();
            } catch (Exception e) {
            }
        }
        
        if (buzon.size() == 0 && produccionTerminada) {
            return null; // Señal para terminar
        }
        
        Correo correo = buzon.remove(0);
        notifyAll();
        return correo;
    }
}