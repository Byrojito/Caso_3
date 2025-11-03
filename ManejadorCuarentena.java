import java.util.Random;
import java.util.ArrayList;

public class ManejadorCuarentena extends Thread {
    private final BuzonCuarentena buzonCuarentena;
    private final BuzonEntrega buzonEntrega;

    public ManejadorCuarentena(BuzonCuarentena buzonCuarentena, BuzonEntrega buzonEntrega) {
        this.buzonCuarentena = buzonCuarentena;
        this.buzonEntrega = buzonEntrega;
        this.setName("ManejadorCuarentena");
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(1000); 

                synchronized (buzonCuarentena) {
                    ArrayList<Correo> correos = buzonCuarentena.obtenerTodos();
                    
                    if (!correos.isEmpty()) {
                        System.out.println("ManejadorCuarentena: Revisando " + correos.size() + " correos en cuarentena...");
                    }
                    
                    ArrayList<Correo> correosAConsultar = new ArrayList<>(correos);

                    for (Correo correo : correosAConsultar) {
                        
                        if (correo.Cofinal()) {
                            buzonCuarentena.eliminar(correo); 
                            System.out.println("ManejadorCuarentena: FIN RECIBIDO. Terminando."); 
                            
                            Correo finEntrega = new Correo("FIN-Entrega", false, false, true, 0);
                            buzonEntrega.depositar(finEntrega);
                            System.out.println("ManejadorCuarentena: ENVIADO FIN FINAL A ENTREGA.");

                            return; 
                        }
                        
                        Random rand = new Random();
                        if ((rand.nextInt(21) + 1) % 7 == 0) {
                            buzonCuarentena.eliminar(correo); 
                            System.out.println("ManejadorCuarentena: DESCARTADO " + correo.getId() + " (Malicioso)"); 
                        } else {
                            correo.discuarentenaTiempo();
                            System.out.println("ManejadorCuarentena: " + correo.getId() + " dism. a " + correo.getcuarentenaTiempo()); 
                                                        if (correo.getcuarentenaTiempo() <= 0) {
                                buzonCuarentena.eliminar(correo); 
                                
                                boolean depositado = false;
                                while (!depositado) {
                                    try {
                                        buzonEntrega.depositar(correo);
                                        System.out.println("ManejadorCuarentena: REENVIADO " + correo.getId() + " A ENTREGA (Fin Cuarentena)"); 
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