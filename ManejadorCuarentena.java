import java.util.Random;

public class ManejadorCuarentena extends Thread{
    private BuzonCuarentena buzonCuarentena;
    private BuzonEntrega entrega;
    private Random rand = new Random();

    public ManejadorCuarentena(BuzonCuarentena buzonCuarentena,BuzonEntrega entrega){
        this.buzonCuarentena = buzonCuarentena;
        this.entrega = entrega;
    }



    public void run(){
        boolean cons = true;
        while(cons){
            try {
               
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }

            Correo c = buzonCuarentena.tomarCorreoCuarentena();
            
            if(c.Cofinal()){
                System.out.println("=== Termino Manejador Cuarentena ===");
                cons = false; 
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
        } 
    }

}