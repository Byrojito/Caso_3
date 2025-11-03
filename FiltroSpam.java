import java.util.Random;

public class FiltroSpam extends Thread {
    private final BuzonEntrada buzonEntrada;
    private final BuzonCuarentena buzonCuarentena;
    private final BuzonEntrega buzonEntrega;
    private final int numClientes;
    private static int finesRecibidosGlobal = 0;
    private static final Object lockFin = new Object();

    public FiltroSpam(BuzonEntrada buzonEntrada, BuzonCuarentena buzonCuarentena, BuzonEntrega buzonEntrega, int numClientes) {
        this.buzonEntrada = buzonEntrada;
        this.buzonCuarentena = buzonCuarentena;
        this.buzonEntrega = buzonEntrega;
        this.numClientes = numClientes;
        this.setName(Thread.currentThread().getName()); 
    }

    @Override
    public void run() {
        try {
            boolean debeContinuar = true;
            while (debeContinuar) {
                Correo correo = buzonEntrada.consumir();
                System.out.println(Thread.currentThread().getName() + ": RECIBIDO " + correo.getId());
                
                if (correo.Cofinal()) {
                    synchronized (lockFin) {
                        finesRecibidosGlobal++;
                        System.out.println(Thread.currentThread().getName() + ": FIN RECIBIDO. Total fines: " + finesRecibidosGlobal);
                        if (finesRecibidosGlobal == numClientes) {
                            debeContinuar = false;
                            Correo finCuarentena = new Correo("FIN-Cuarentena", false, false, true, 0);
                            buzonCuarentena.depositar(finCuarentena);
                            System.out.println(Thread.currentThread().getName() + ": ENVIADO FIN A CUARENTENA");
                        }
                    }
                } else if (!correo.coIncial() && correo.getflagSpam()) {
                    Random rand = new Random();
                    correo.setcuarentenaTiempo(rand.nextInt(10001) + 10000); 
                    buzonCuarentena.depositar(correo);
                    System.out.println(Thread.currentThread().getName() + ": CORREO " + correo.getId() + " ENVIADO A CUARENTENA. T: " + correo.getcuarentenaTiempo());
                } else if (!correo.coIncial()) {
                    while (true) {
                        try {
                            buzonEntrega.depositar(correo);
                            System.out.println(Thread.currentThread().getName() + ": CORREO " + correo.getId() + " ENVIADO A ENTREGA");
                            break;
                        } catch (InterruptedException e) {
                            Thread.yield();
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}