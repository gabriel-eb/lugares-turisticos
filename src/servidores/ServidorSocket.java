package servidores;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gaboeb
 */
public class ServidorSocket implements Runnable {

    private Boolean nuevo;
    private Boolean terminar;
    HashMap<String, ArrayList<String>> datos;

    public ServidorSocket(HashMap<String, ArrayList<String>> datos) {
        nuevo = false;
        terminar = true;
        this.datos = datos;
    }

    @Override
    public void run() {
        // Registrar el servicio en el puerto 5100
        try {
            ServerSocket ss = new ServerSocket(5100);
            // Mensaje de status
            System.out.println("\tIniciando servidor Socket/Hilos...");

            while (terminar) {
                Socket s = ss.accept();
                Runnable r = new ManejadorHilos(s, datos);
                Thread t = new Thread(r);
                t.start();
                nuevo = false;
            }

        } catch (IOException e) {
            System.out.println("Fallo al conectar el servidor: ");
            System.err.print(e.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(ServidorSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void nuevoHilo() {
        nuevo = true;
        terminar = true;
    }

    public void terminar() {
        terminar = false;
    }

}
