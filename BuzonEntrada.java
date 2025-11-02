import java.util.ArrayList;

public class BuzonEntrada {
    private int capacidad;
    private ArrayList<Correo> buzon = new ArrayList<>();

    public BuzonEntrada(int capacidad){
        this.capacidad = capacidad;
    }

    public synchronized void enviarCorreo(Correo correo){
        while(buzon.size() == capacidad){
            try {
                wait();
            } catch (Exception e) {
            }
        }
        buzon.add(correo);
        notifyAll(); // hacer notificaciones a los filtros de spam ¿? cómo lo hago.

    }

    public synchronized Correo analizarSpam(){
        while(buzon.size() == 0){
            try {
                wait();
            } catch (Exception e) {
            }
        }
        Correo correo = buzon.remove(0);
        notifyAll();
        return correo;
    }
}