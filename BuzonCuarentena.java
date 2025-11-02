import java.util.ArrayList;

public class BuzonCuarentena {
    private ArrayList<Correo> buzonCuarentena = new ArrayList<>();

    public synchronized void enviarCorreoSpam(Correo correo){
        buzonCuarentena.add(correo);
    }


    public Correo tomarCorreoCuarentena(){
        Correo c = null;
        while(c == null) {
            synchronized(this) {
                if (buzonCuarentena.size() > 0) {
                    c = buzonCuarentena.remove(0);
                }
            }

            if (c == null) {
                Thread.yield();
            }
        }
        return c;
    }

    public synchronized int getSize() {
        return buzonCuarentena.size();
    }
    
}
