
package servidores;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gaboeb
 */
public class ManejadorHilos implements Runnable {

    private Socket entrante;
    private String numCliente;
    private LugaresTuristicos lt;

    public ManejadorHilos(Socket s,
            HashMap<String, ArrayList<String>> datos) throws Exception {
        this.entrante = s;
        lt = new LugaresTuristicos(datos, "Socket");
    }

    @Override
    public void run() {
        // Obtiene un flujo de comunicación asociado con el socket       
        try {
            // Fujo de salida
            OutputStream s1out = entrante.getOutputStream();
            DataOutputStream dos = new DataOutputStream(s1out);
            // Fujo de entrada
            InputStream s1ln = entrante.getInputStream();
            DataInputStream dis = new DataInputStream(s1ln);

            // Asigna el numero de cliente
            numCliente = dis.readUTF();

            // Envia un mensaje de bienvenida
            dos.writeUTF(lt.iniciarConsulta(numCliente));

            // Bucle de solicitud (busqueda)
            realizarBusqueda(dos, dis, numCliente);

            // Mensaje de despedida
            dos.writeUTF(lt.finalizarConsulta(numCliente));

            // Cierra la conexión, pero no el socket del servidor.
            dis.close();
            dos.close();
            s1out.close();
            entrante.close();
        } catch (IOException ex) {
            System.out.println("Fallo al crear canales del servidor.");
            Logger.getLogger(ManejadorHilos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // Función que crea un bucle para la solicitud de busqueda del cliente.
    // Todos los booleanos enviados por stream son para seguir/detener 
    // los bucles en el lado del cliente
    public void realizarBusqueda(DataOutputStream dos, DataInputStream dis,
            String numCliente) throws IOException {
        Boolean seguir = true;

        while (seguir) {
            String ciudad = dis.readUTF();

            // Envia al cliente la lista con los destinos que eligió
            String ciudadExiste = lt.realizarBusqueda(numCliente, ciudad);
            dos.writeUTF(ciudadExiste);

            // Genera imagen de mapa y se envia
            if (!ciudadExiste.startsWith("No")) {
                dos.writeLong(lt.tamImagen(ciudad));
                dos.write(lt.enviarMapa(ciudad));
            }

            while (true) {
                String repetir = lt.repetirConsulta(dis.readUTF());
                dos.writeUTF(repetir);

                seguir = !repetir.equals("no");
                if (repetir.equals("no") || repetir.equals("\n\tCiudad: ")) {
                    break;
                }
            }
        }
    }

}
