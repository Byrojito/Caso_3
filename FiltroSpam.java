import java.util.Random;

public class FiltroSpam extends Thread {
    private final BuzonEntrada buzonEntrada;
    private final BuzonCuarentena buzonCuarentena;
    private final BuzonEntrega buzonEntrega;
    private final int numClientes;
    private static int finesRecibidosGlobal = 0;
    private static final Object lockFin = new Object();
    private final int numFiltros;

    public FiltroSpam(BuzonEntrada buzonEntrada, BuzonCuarentena buzonCuarentena, BuzonEntrega buzonEntrega, int numClientes, int numFiltros) {
        this.buzonEntrada = buzonEntrada;
        this.buzonCuarentena = buzonCuarentena;
        this.buzonEntrega = buzonEntrega;
        this.numClientes = numClientes;
        this.numFiltros = numFiltros;
    }

    @Override
    public void run() {
        try {
            boolean debeContinuar = true;
            while (debeContinuar) {
                Correo correo = buzonEntrada.consumir();
                System.out.println(Main.getLogTime() + Thread.currentThread().getName() + ": RECIBIDO " + correo.getId());
                
                // ✅ PRIMERO: Verificar si es FIN-Filtro
                if (correo.getId().equals("FIN-Filtro")) {
                    System.out.println(Main.getLogTime() + Thread.currentThread().getName() + ": RECIBIDO FIN-Filtro. Terminando.");
                    debeContinuar = false;
                }
                // ✅ SEGUNDO: Verificar si es FIN de cliente
                else if (correo.Cofinal()) {
                    synchronized (lockFin) {
                        finesRecibidosGlobal++;
                        System.out.println(Main.getLogTime() + Thread.currentThread().getName() + ": FIN RECIBIDO. Total fines: " + finesRecibidosGlobal);
                        
                        if (finesRecibidosGlobal == numClientes) {
                            for (int i = 0; i < numFiltros; i++) {
                                Correo finFiltro = new Correo("FIN-Filtro", false, false, true, 0);
                                buzonEntrada.depositar(finFiltro);
                            }
                            System.out.println(Main.getLogTime() + Thread.currentThread().getName() + ": ENVIADOS " + numFiltros + " FIN A FILTROS");
                            
                            Correo finCuarentena = new Correo("FIN-Cuarentena", false, false, true, 0);
                            buzonCuarentena.depositar(finCuarentena);
                            System.out.println(Main.getLogTime() + Thread.currentThread().getName() + ": ENVIADO FIN A CUARENTENA");
                            
                            // ❌ ELIMINAR ESTA LÍNEA:
                            // debeContinuar = false;
                            
                            // ✅ El filtro debe seguir y consumir su FIN-Filtro
                        }
                    }
                }
                else if (!correo.coIncial() && correo.getflagSpam()) {
                    Random rand = new Random();
                    correo.setcuarentenaTiempo(rand.nextInt(10001) + 10000); 
                    buzonCuarentena.depositar(correo);
                    System.out.println(Main.getLogTime() + Thread.currentThread().getName() + ": CORREO " + correo.getId() + " ENVIADO A CUARENTENA. T: " + correo.getcuarentenaTiempo());
                } else if (!correo.coIncial()) {
                    while (true) {
                        try {
                            buzonEntrega.depositar(correo);
                            System.out.println(Main.getLogTime() + Thread.currentThread().getName() + ": CORREO " + correo.getId() + " ENVIADO A ENTREGA");
                            break;
                        } catch (InterruptedException e) {
                            Thread.yield();
                        }
                    }
                }
            }
            
            System.out.println(Main.getLogTime() + Thread.currentThread().getName() + ": FILTRO TERMINADO");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}