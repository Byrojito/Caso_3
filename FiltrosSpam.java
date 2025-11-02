import java.util.Random;

public class FiltrosSpam extends Thread {
    private BuzonEntrada buzonEntrada;
    private BuzonCuarentena buzonCuarentena;
    private BuzonEntrega buzonEntrega;
    private Random rand = new Random();
    private int numClientesEmisores;
    private int numServidoresEmisores;
    private static int finClientesRecibidos = 0;

    public FiltrosSpam(BuzonEntrada buzonEntrada,BuzonCuarentena buzonCuarentena, BuzonEntrega buzonEntrega, int numClientesEmisores, int numServidoresEmisores){
        this.numServidoresEmisores = numServidoresEmisores;
        this.numClientesEmisores = numClientesEmisores;
        this.buzonCuarentena = buzonCuarentena;
        this.buzonEntrega = buzonEntrega;
        this.buzonEntrada = buzonEntrada;
    }

    // FiltrosSpam.java

    public void run(){
        
        
        while(finClientesRecibidos < numClientesEmisores){
            Correo correo = buzonEntrada.analizarSpam(); 
            correo.setflagSpam(); 

            if(correo.coIncial()){
                buzonEntrega.enviarCorreo(correo);
            
            } else if(correo.Cofinal()) {
                buzonEntrega.enviarCorreo(correo); 
                
                boolean soyElResponsableDeCerrar = false;
                
                // Usamos un bloque 'synchronized' para proteger la variable estática
                synchronized(FiltrosSpam.class) {
                    finClientesRecibidos++;
                    if (finClientesRecibidos == numClientesEmisores) {
                        soyElResponsableDeCerrar = true;
                    }
                }
                
                if(soyElResponsableDeCerrar) {
                   
                    System.out.println("--- Filtro Responsable: Esperando a que BuzonCuarentena se vacíe...");
                    
                    while (buzonCuarentena.getSize() > 0) {
                        Thread.yield(); 
                    }

                    System.out.println("--- Filtro Responsable: Cuarentena VACÍA. Enviando FIN final a Servidores y Manejador.");
                    
                    Correo fin = new Correo("FIN", false, false, true, 0);

                    for(int i = 0; i < numServidoresEmisores; i++){
                        buzonEntrega.enviarCorreo(fin);
                    }
                    
                    buzonCuarentena.enviarCorreoSpam(fin);
                }
                
            } else if(correo.getflagSpam() == false){
                buzonEntrega.enviarCorreo(correo);

            } else{
                int t = rand.nextInt(11)+10; 
                correo.setcuarentenaTiempo(t);
                buzonCuarentena.enviarCorreoSpam(correo);
            }
            
           
        }
        
        System.out.println("=== Filtro Spam - finalizado ===");
    }
 
}
