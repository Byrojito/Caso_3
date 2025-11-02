import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // --- 1. Declaración de Parámetros ---
        int numClientes = 0;
        int numMensajesPorCliente = 0;
        int numFiltros = 0;
        int numServidores = 0;
        int capacidadEntrada = 0;
        int capacidadEntrega = 0;
        
        // --- 2. Lectura del Archivo de Configuración ---
        try {
            File configFile = new File("config.txt"); 
            Scanner sc = new Scanner(configFile);

            // Se leen los 6 parámetros en el orden especificado:
            numClientes = sc.nextInt();
            numMensajesPorCliente = sc.nextInt();
            numFiltros = sc.nextInt();
            numServidores = sc.nextInt();
            capacidadEntrada = sc.nextInt();
            capacidadEntrega = sc.nextInt();

            sc.close();
            System.out.println("==================================================");
            System.out.println("Parámetros leídos y simulación iniciada:");
            System.out.println("Clientes: " + numClientes + ", Mensajes/Cliente: " + numMensajesPorCliente);
            System.out.println("Filtros: " + numFiltros + ", Servidores: " + numServidores);
            System.out.println("Cap. Entrada: " + capacidadEntrada + ", Cap. Entrega: " + capacidadEntrega);
            System.out.println("==================================================");

        } catch (FileNotFoundException e) {
            System.err.println("Error: Archivo de configuración 'config.txt' no encontrado.");
            return; // Termina el programa si no se encuentra el archivo
        } catch (Exception e) {
            System.err.println("Error al leer los parámetros. Verifique que 'config.txt' contenga 6 números enteros.");
            return;
        }
        
        // --- 3. Inicialización de Buzones ---
        BuzonEntrada buzonEntrada = new BuzonEntrada(capacidadEntrada);
        BuzonCuarentena buzonCuarentena = new BuzonCuarentena();
        
        // Nota: Se pasa un ArrayList vacío a BuzonEntrega como requiere tu constructor
        BuzonEntrega buzonEntrega = new BuzonEntrega(capacidadEntrega, new ArrayList<Correo>()); 
        
        // --- 4. Creación y Lanzamiento de Threads ---
        
        // Almacenar clientes para el join()
        ArrayList<ClientesEmisores> clientes = new ArrayList<>(); 
        
        // Clientes Emisores
        for (int i = 0; i < numClientes; i++) {
            ClientesEmisores cliente = new ClientesEmisores(i + 1, numMensajesPorCliente, buzonEntrada);
            clientes.add(cliente);
            cliente.start();
        }
        
        // Filtros de Spam
        for (int i = 0; i < numFiltros; i++) {
            new FiltrosSpam(buzonEntrada, buzonCuarentena, buzonEntrega, numClientes, numServidores).start();
        }

        // Manejador Cuarentena (solo uno)
        new ManejadorCuarentena(buzonCuarentena, buzonEntrega).start();

        // Servidores de Entrega
        for (int i = 0; i < numServidores; i++) {
            new ServidoresEntrega(buzonEntrega).start();
        }

        // --- 5. Espera a la Terminación de Clientes (Join) ---
        // El Main thread espera a que todos los Clientes Emisores terminen de enviar sus correos
        for (ClientesEmisores cliente : clientes) {
            try {
                cliente.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Al terminar el join, la cadena de terminación (con los correos finales) se disparará automáticamente.
        System.out.println("=== TODOS LOS CLIENTES HAN TERMINADO. ESPERANDO EL CIERRE DE LOS CONSUMIDORES ===");
    }
}