import java.util.Random;

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
        System.out.println("== Mensaje de inicio ==");
        Correo ini = new Correo(id_cli+"-"+0, rand.nextBoolean(), true, false, 0);
        buzonEntrada.enviarCorreo(ini);

        for(int i = 1; i < numCorreos-1; i++){
            Correo c = new Correo(id_cli+"-"+i, rand.nextBoolean(), false, false, 0);
            buzonEntrada.enviarCorreo(c);
        }
        System.out.println("== Mensaje de fin ==");
        Correo fin = new Correo(id_cli+"-"+(numCorreos-1), rand.nextBoolean(), false, true, 0);
        buzonEntrada.enviarCorreo(fin);
    }

    public long getid_cli(){
        return id_cli;
    }

}
