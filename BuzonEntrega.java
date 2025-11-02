import java.util.ArrayList;

public class BuzonEntrega {
    private ArrayList<Correo> buzonEntrega = new ArrayList<>();
    private int capacidad;

    public BuzonEntrega(int capacidad, ArrayList<Correo> buzonEntrega){
        this.buzonEntrega = buzonEntrega;
        this.capacidad = capacidad;
    }

    public void enviarCorreo(Correo correo){
        while(true) {
            synchronized(this) {
                if (buzonEntrega.size() < capacidad) {
                    buzonEntrega.add(correo);
                    return; 
                }
            }
            Thread.yield(); // AÑADIR yield para espera semi-activa
        }
    }

    public Correo recibirCorreo(){
        Correo c = null;
        while(c == null) {
            synchronized(this) {
                if (buzonEntrega.size() > 0) {
                    c = buzonEntrega.remove(0);
                }
            }
            // AÑADIR yield para espera semi-activa
            if (c == null) {
                Thread.yield();
            }
        }
        return c;
    }
}