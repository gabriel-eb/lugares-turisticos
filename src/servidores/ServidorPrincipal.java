package servidores;

import org.apache.xmlrpc.WebServer;

/**
 *
 * @author gaboeb
 */
public class ServidorPrincipal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Se eligió RPC para hacer la conexión al servidor principal
        try {
            System.out.println("Iniciando servidor principal...");
            WebServer server = new WebServer(5000);
            Seleccionador op = new Seleccionador();
            server.addHandler("servidorPrincipal", op);
            server.start();
            System.out.println("Servidor en línea. Esperando solicitud.\n");

        } catch (Exception e) {
            System.err.println("El servidor encontró el siguiente problema: "
                    + e);
        }
    }

}
