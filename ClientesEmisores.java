import java.util.Random;
import java.util.random.*;

public class ClientesEmisores extends Thread {
    private long id_cli;
    private int numCorreos;
    private BuzonEntrada buzonEntrada;
    private Random rand = new Random();
    // private int identificador_hilo = 1;

    public ClientesEmisores(long id_cli , int Correos, BuzonEntrada Entrada){
        this.id_cli = id_cli;
        this.numCorreos = Correos;
        this.buzonEntrada = Entrada;
        // this.identificador_hilo = ideHilo;

    }

    public void run(){
        boolean flag = rand.nextBoolean();
        System.out.println("== Mensaje de inicio ==");
        Correo ini = new Correo(id_cli+"-"+0, flag, true, false, 0);
        buzonEntrada.enviarCorreo(ini);

        for(int i = 0; i < numCorreos-2; i++){
            Correo c = new Correo(id_cli+"-"+i+1, flag, false, false, 0);
            // poner buzon entrada y agragar el correo
            buzonEntrada.enviarCorreo(c);
        }
        System.out.println("== Mensaje de fin ==");
        Correo fin = new Correo(id_cli+"-"+numCorreos, flag, false, true, 0);
        buzonEntrada.enviarCorreo(fin);

    }

    public long getid_cli(){
        return id_cli;
    }

}
