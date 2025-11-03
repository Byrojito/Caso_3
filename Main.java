import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    private static long startTime;

    public static String getLogTime() {
        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        return String.format("[%d s] ", elapsed);
    }
    public static void main(String[] args) {
        
        int NUM_CLIENTES = 0;
        int MENSAJES_POR_CLIENTE = 0;
        int NUM_FILTROS = 0;
        int NUM_SERVIDORES = 0;
        int CAPACIDAD_ENTRADA = 0;
        int CAPACIDAD_ENTREGA = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("config.txt"));
            
            NUM_CLIENTES = Integer.parseInt(reader.readLine().trim());
            MENSAJES_POR_CLIENTE = Integer.parseInt(reader.readLine().trim());
            NUM_FILTROS = Integer.parseInt(reader.readLine().trim());
            NUM_SERVIDORES = Integer.parseInt(reader.readLine().trim());
            CAPACIDAD_ENTRADA = Integer.parseInt(reader.readLine().trim());
            CAPACIDAD_ENTREGA = Integer.parseInt(reader.readLine().trim());
            
            reader.close();
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error al leer config.txt o formato incorrecto. Terminando.");
            return;
        }
        startTime = System.currentTimeMillis();
        System.out.println(getLogTime() + "--- INICIANDO SIMULACIÓN ---");
        System.out.println(getLogTime() + "Clientes: " + NUM_CLIENTES + ", Mensajes/C: " + MENSAJES_POR_CLIENTE + ", Filtros: " + NUM_FILTROS + ", Servidores: " + NUM_SERVIDORES);
        System.out.println(getLogTime() + "Cap. Entrada: " + CAPACIDAD_ENTRADA + ", Cap. Entrega: " + CAPACIDAD_ENTREGA + "\n");
        
        BuzonEntrada buzonEntrada = new BuzonEntrada(CAPACIDAD_ENTRADA);
        BuzonCuarentena buzonCuarentena = new BuzonCuarentena();
        BuzonEntrega buzonEntrega = new BuzonEntrega(CAPACIDAD_ENTREGA);

        ArrayList<Thread> servidores = new ArrayList<>();
        ArrayList<Thread> filtros = new ArrayList<>();
        
        for (int i = 0; i < NUM_CLIENTES; i++) {
            ClienteEmisor cliente = new ClienteEmisor("C" + (i + 1), MENSAJES_POR_CLIENTE, buzonEntrada);
            cliente.start();
        }

        for (int i = 0; i < NUM_FILTROS; i++) {
            FiltroSpam filtro = new FiltroSpam(buzonEntrada, buzonCuarentena, buzonEntrega, NUM_CLIENTES, NUM_FILTROS);
            filtro.setName("Filtro-" + (i + 1)); 
            filtro.start();
            filtros.add(filtro);
        }
        
        ManejadorCuarentena manejador = new ManejadorCuarentena(buzonCuarentena, buzonEntrega, NUM_SERVIDORES);
        manejador.setName("ManejadorCuarentena");
        manejador.start();

        for (int i = 0; i < NUM_SERVIDORES; i++) {
            ServidorEntrega servidor = new ServidorEntrega("Servidor-" + (i + 1), buzonEntrega);
            servidor.start();
            servidores.add(servidor);
        }
        
        try {
            for (Thread filtro : filtros) {
                filtro.join();
            }
            System.out.println(getLogTime() + ">>> Todos los filtros han terminado");
            
            for (Thread servidor : servidores) {
                servidor.join(); 
            }
            System.out.println(getLogTime() + ">>> Todos los servidores han terminado");
            
            manejador.join();
            System.out.println(getLogTime() + ">>> Manejador ha terminado");
       
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n" + getLogTime() + "*** SIMULACION FINALIZADA COMPLETAMENTE ***");
    }
}