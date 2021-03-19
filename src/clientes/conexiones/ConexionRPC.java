
package clientes.conexiones;

import clientes.display.Mapa;
import java.util.Scanner;
import java.util.Vector;
import javax.swing.ImageIcon;
import org.apache.xmlrpc.XmlRpcClient;

/**
 *
 * @author gaboeb
 */
public class ConexionRPC extends Thread {

    private String numCliente;

    public ConexionRPC(String numCliente) {
        this.numCliente = numCliente;
    }

    @Override
    public void run() {
        try {
            XmlRpcClient cliente = new XmlRpcClient("http://localhost:5200/");
            Scanner sc = new Scanner(System.in);
            Boolean seguir = true;
            String msj;
            Vector<String> params = new Vector<String>();

            // Mensaje de bienvenida
            params.addElement(numCliente);
            msj = (String) cliente.execute("servidorRPC.iniciarConsulta", params);
            System.out.print(msj);

            // Bucle para seguir con las consultas
            while (seguir) {
                String ciudad = sc.next();
                // Usamos UTF-8 para no corromper los signos de acentuación
                ciudad = new String(ciudad.getBytes("UTF-8"), "ISO-8859-1");
                params.clear();
                params.add(numCliente);
                params.addElement(ciudad);
                Object resConsulta = cliente.execute("servidorRPC.realizarBusqueda", params);
                String ciudadExiste = (String) resConsulta;
                System.out.print(ciudadExiste);

                // Mapa
                if (!ciudadExiste.startsWith("No")) {
                    params.clear();
                    params.addElement(ciudad);
                    byte[] im = (byte[]) cliente.execute("servidorRPC.enviarMapa", params);
                    Mapa mapaCiudad = new Mapa(ciudad, new ImageIcon(im));
                    mapaCiudad.setVisible(true);
                }

                // Bucle para validar la respuesta del usuario
                while (true) {
                    String seguirConsulta = sc.next();
                    seguirConsulta = new String(seguirConsulta.getBytes("UTF-8"), "ISO-8859-1");
                    params.clear();
                    params.addElement(seguirConsulta);
                    String repetir = (String) cliente.execute("servidorRPC.repetirConsulta", params);

                    seguir = !repetir.equals("no");
                    if (repetir.equals("no")) {
                        break;
                    }
                    if (repetir.equals("\n\tCiudad: ")) {
                        System.out.print(repetir);
                        break;
                    }
                    System.out.print(repetir);
                }
            }

            // Mensaje de despedida
            params.clear();
            params.addElement(numCliente);
            msj = (String) cliente.execute("servidorRPC.finalizarConsulta", params);
            System.out.println(msj);

        } catch (Exception exception) {
            System.err.println("JavaClient: " + exception);
        }
    }
}
