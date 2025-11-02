import java.util.ArrayList;
import java.util.Random;
public class ServidoresEntrega extends Thread {
    private BuzonEntrega buzonEntrega;
    private ArrayList<Correo> correos = new ArrayList<>();

    public ServidoresEntrega(BuzonEntrega buzonEntrega){
        this.buzonEntrega = buzonEntrega;
    }

    public void run(){
        while(true){
            Correo c = buzonEntrega.recibirCorreo();
            if(c.getId().equals("FIN")){ // Solo terminar con el FIN artificial
                System.out.println("=== Servidor de entrega finalizado ===");
                return;
            } else if(c.Cofinal()) {
                // Ignorar los mensajes de fin de clientes, seguir procesando
                leerCorreo(c);
            } else {
                leerCorreo(c);
            }
        }
    }

    public void leerCorreo(Correo correo){
        correos.add(correo);
        System.out.println("Servidor leyó: " + correo.getId());
        
        try {
            Thread.sleep(new Random().nextInt(100) + 50); // Entre 50-150ms
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}