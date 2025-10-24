public class ClientesEmisores extends Thread {
    private int numCorreos;
    private BuzonEntrada buzonEntrada;

    public ClientesEmisores(int Correos, BuzonEntrada Entrada){
        this.numCorreos = Correos;
        this.buzonEntrada = Entrada;
    }

    public void run(){
        System.out.println("== Mensaje de inicio ==");
    }
}
