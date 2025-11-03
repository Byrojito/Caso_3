import java.util.Random;

public class ManejadorCuarentena extends Thread{
    private BuzonCuarentena buzonCuarentena;
    private BuzonEntrega entrega;
    private Random rand = new Random();
    private boolean procesando = false;

    public ManejadorCuarentena(BuzonCuarentena buzonCuarentena,BuzonEntrega entrega){
        this.buzonCuarentena = buzonCuarentena;
        this.entrega = entrega;
    }



    public void run(){
        boolean finRecibido = false;
        
        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }

            int cantidadMensajes = buzonCuarentena.getSize();
            
            if (finRecibido && cantidadMensajes == 0) {
                System.out.println("=== Termino Manejador Cuarentena ===");
                break;
            }
            
            for(int i = 0; i < cantidadMensajes; i++){
                synchronized(this) {
                    procesando = true;
                }
                
                Correo c = buzonCuarentena.tomarCorreoCuarentena();
                
                if(c.Cofinal()){
                    finRecibido = true;  
                    System.out.println("--- Manejador: FIN recibido, procesando mensajes restantes...");
                } else {
                    c.discuarentenaTiempo();
                    int aleatorio = rand.nextInt(21)+1;

                    if(aleatorio % 7 == 0){
                        System.out.println("--- Manejador: Correo " + c.getId() + " DESCARTADO (malicioso).");
                    
                    } else if (c.getcuarentenaTiempo() <= 0) {
                        System.out.println("--- Manejador: Correo " + c.getId() + " enviado a ENTREGA.");
                        entrega.enviarCorreo(c);
                    
                    } else {
                        System.out.println("--- Manejador: Correo " + c.getId() + " devuelto a cuarentena (tiempo restante: " + c.getcuarentenaTiempo() + ").");
                        buzonCuarentena.enviarCorreoSpam(c);
                    }
                }
                
                synchronized(this) {
                    procesando = false;
                }
            }
        }
    }

    public synchronized boolean estaProcesando() {
        return procesando;
    }

}