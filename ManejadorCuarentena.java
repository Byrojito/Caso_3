import java.util.Random;
import java.util.ArrayList;

public class ManejadorCuarentena extends Thread {
    private final BuzonCuarentena buzonCuarentena;
    private final BuzonEntrega buzonEntrega;
    private final int numServidores;

    public ManejadorCuarentena(BuzonCuarentena buzonCuarentena, BuzonEntrega buzonEntrega, int numServidores) {
        this.buzonCuarentena = buzonCuarentena;
        this.buzonEntrega = buzonEntrega;
        this.numServidores = numServidores;
        this.setName("ManejadorCuarentena");
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(2000); 

                synchronized (buzonCuarentena) {
                    ArrayList<Correo> correos = buzonCuarentena.obtenerTodos();
                    
                    if (!correos.isEmpty()) {
                        System.out.println(Main.getLogTime() + "ManejadorCuarentena: Revisando " + correos.size() + " correos en cuarentena...");
                    }
                    
                    ArrayList<Correo> correosAConsultar = new ArrayList<>(correos);

                    for (Correo correo : correosAConsultar) {
                        
                        if (correo.Cofinal()) {
                            buzonCuarentena.eliminar(correo); 
                            System.out.println(Main.getLogTime() + "ManejadorCuarentena: FIN RECIBIDO. Terminando."); 
                            
                            Correo finEntrega = new Correo("FIN-Entrega", false, false, true, 0);
                            for (int i = 0; i < numServidores; i++) {
                                buzonEntrega.depositar(finEntrega); 
                            }
                            System.out.println(Main.getLogTime() + "ManejadorCuarentena: ENVIADAS " + numServidores + " COPIAS DE FIN FINAL A ENTREGA.");

                            return; 
                        }
                        
                        Random rand = new Random();
                        if ((rand.nextInt(21) + 1) % 7 == 0) {
                            buzonCuarentena.eliminar(correo); 
                            System.out.println(Main.getLogTime() + "ManejadorCuarentena: DESCARTADO " + correo.getId() + " (Malicioso)"); 
                        } else {
                            correo.discuarentenaTiempo();
                            System.out.println(Main.getLogTime() + "ManejadorCuarentena: " + correo.getId() + " dism. a " + correo.getcuarentenaTiempo()); 
                            
                            if (correo.getcuarentenaTiempo() <= 0) {
                                buzonCuarentena.eliminar(correo); 
                                
                                boolean depositado = false;
                                while (!depositado) {
                                    try {
                                        buzonEntrega.depositar(correo);
                                        System.out.println(Main.getLogTime() + "ManejadorCuarentena: REENVIADO " + correo.getId() + " A ENTREGA (Fin Cuarentena)"); 
                                        depositado = true;
                                    } catch (InterruptedException e) {
                                        Thread.yield();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}