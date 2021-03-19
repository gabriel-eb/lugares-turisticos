package servidores;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.xmlrpc.WebServer;

/**
 *
 * @author gaboeb
 */
public class ServidorRPC {

    public void ejecutarServidor(HashMap<String, ArrayList<String>> datos) {
        try {

            System.out.println("\tIniciando servidor XML-RPC...");

            WebServer server = new WebServer(5200);
            LugaresTuristicos op = new LugaresTuristicos(datos, "RPC");
            server.addHandler("servidorRPC", op);
            server.start();

        } catch (Exception e) {
            System.err.println("El servidor encontró el siguiente problema: "
                    + e);
        }
    }

}
