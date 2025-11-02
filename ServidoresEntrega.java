import java.util.ArrayList;
public class ServidoresEntrega extends Thread {
    private BuzonEntrega buzonEntrega;
    private ArrayList<Correo> correos = new ArrayList<>();

    public ServidoresEntrega(BuzonEntrega buzonEntrega){
        this.buzonEntrega = buzonEntrega;
    }

    public void run(){
        boolean cas = false;
        while(cas == false){
            Correo c = buzonEntrega.recibirCorreo();
            if(c.Cofinal() == false){
                leerCorreo(c);
            } else{
                System.out.println("=== Se ha terminado de leer ===");
                return;
            }

        }

    }

    public void leerCorreo(Correo correo){
        correos.add(correo);
    }

    
}
