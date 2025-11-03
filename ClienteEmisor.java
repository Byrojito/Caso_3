import java.util.Random;

public class ClienteEmisor extends Thread {
    private final int numCorreos;
    private final BuzonEntrada buzonEntrada;
    private final String idCliente;

    public ClienteEmisor(String idCliente, int numCorreos, BuzonEntrada buzonEntrada) {
        this.idCliente = idCliente;
        this.numCorreos = numCorreos;
        this.buzonEntrada = buzonEntrada;
    }

    @Override
    public void run() {
        try {
            Correo inicio = new Correo(idCliente + "-I", false, true, false, 0);
            buzonEntrada.depositar(inicio);
            System.out.println(idCliente + ": ENVIADO INICIO " + inicio.getId()); // LOG

            Random rand = new Random();
            for (int i = 1; i <= numCorreos; i++) {
                String id = idCliente + "-" + i;
                boolean isSpam = rand.nextInt(4) == 0; 
                Correo correo = new Correo(id, isSpam, false, false, 0);
                
                buzonEntrada.depositar(correo);
                System.out.println(idCliente + ": ENVIADO CORREO " + id + " (Spam: " + isSpam + ")"); // LOG
                Thread.sleep(rand.nextInt(100)); 
            }

            Correo fin = new Correo(idCliente + "-F", false, false, true, 0);
            buzonEntrada.depositar(fin);
            System.out.println(idCliente + ": ENVIADO FIN " + fin.getId()); // LOG
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}