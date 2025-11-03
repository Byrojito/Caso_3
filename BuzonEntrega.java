import java.util.ArrayList;

public class BuzonEntrega {
    private final int capacidadMaxima;
    private final ArrayList<Correo> buzon;
    public BuzonEntrega(int capacidadMaxima) {
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
        if (buzon.isEmpty()) {
            return null; 
        }
        
        Correo correo = buzon.remove(0);
        notifyAll(); 
        return correo;
    }
    public synchronized void enviarFinATodosServidores(Correo finCorreo, int numServidores) throws InterruptedException {
        buzon.add(finCorreo);
        notifyAll(); 
    }
    public synchronized boolean estaVacio() {
        return buzon.isEmpty();
    }
}