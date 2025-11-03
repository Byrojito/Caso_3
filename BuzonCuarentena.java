import java.util.ArrayList;

public class BuzonCuarentena {
    private final ArrayList<Correo> buzon;
    public BuzonCuarentena() {
        this.buzon = new ArrayList<>();
    }
    public synchronized void depositar(Correo correo) {
        buzon.add(correo);
        notifyAll(); 
    }

    public synchronized ArrayList<Correo> obtenerTodos() {
        return buzon;
    }
    
    public synchronized void eliminar(Correo correo) {
        buzon.remove(correo);
    }
    public synchronized boolean estaVacio() {
        return buzon.isEmpty();
    }
}