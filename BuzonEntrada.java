import java.util.ArrayList;

public class BuzonEntrada {
    private final int capacidadMaxima;
    private final ArrayList<Correo> buzon;

    public BuzonEntrada(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
        this.buzon = new ArrayList<>();
    }
    public synchronized void depositar(Correo correo) throws InterruptedException {
        while (buzon.size() == capacidadMaxima) {
            wait();
        }
        
        buzon.add(correo);
        notifyAll(); 
    }
    public synchronized Correo consumir() throws InterruptedException {
        while (buzon.isEmpty()) {
            wait();
        }
                Correo correo = buzon.remove(0);
        notifyAll();
        return correo;
    }
    public synchronized boolean estaVacio() {
        return buzon.isEmpty();
    }
}