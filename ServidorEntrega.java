import java.util.Random;

public class ServidorEntrega extends Thread {
    private final BuzonEntrega buzonEntrega;
    private final String idServidor;

    public ServidorEntrega(String idServidor, BuzonEntrega buzonEntrega) {
        this.idServidor = idServidor;
        this.buzonEntrega = buzonEntrega;
        this.setName(idServidor);
    }

    @Override
    public void run() {
        try {
            while (true) {
                Correo correo = buzonEntrega.consumir();
                
                if (correo != null) {
                    if (correo.Cofinal()) {
                        System.out.println(Main.getLogTime() + idServidor + ": RECIBIDO FIN FINAL. Terminando."); 
                        return;
                    } else if (!correo.coIncial()) {
                        System.out.println(Main.getLogTime() + idServidor + ": ENTREGADO " + correo.getId()); 
                        Random rand = new Random();
                        Thread.sleep(rand.nextInt(800) + 200); 
                    }
                } else {
                    Thread.yield();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}