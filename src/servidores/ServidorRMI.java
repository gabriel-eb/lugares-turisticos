package servidores;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author gaboeb
 */
public class ServidorRMI {

    public void ejecutarServidor(HashMap<String, ArrayList<String>> datos) {
        try {
            // Crea el objeto para el stub
            LugaresTuristicos lt = new LugaresTuristicos(datos, "RMI");

            // Enlaza el stub del objeto remoto en el registro 
            Registry registry = LocateRegistry.createRegistry(5300);
            registry.rebind("rmi://localhost:5300/LugaresTuristicos", lt);

            System.out.println("\tIniciando servidor RMI...");
        } catch (Exception e) {
            System.err.println("Excepción del servidor: " + e.toString());
            e.printStackTrace();
        }
    }

}
