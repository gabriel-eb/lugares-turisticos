
package clientes.conexiones;

import clientes.display.Mapa;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.ImageIcon;

/**
 *
 * @author gaboeb
 */
public class ConexionSocket implements Runnable {

    private final String numCliente;

    public ConexionSocket(String numCliente) {
        this.numCliente = numCliente;
    }

    @Override
    public void run() {
        try {
            Socket s = new Socket("localhost", 5100);
            // Obtener un manejador de flujo de entrada en el socket 
            // y leer entrada
            // Fujo de entrada
            InputStream sIn = s.getInputStream();
            DataInputStream dis = new DataInputStream(sIn);
            // Fujo de salida
            OutputStream sOut = s.getOutputStream();
            DataOutputStream dos = new DataOutputStream(sOut);

            // Envia número de cliente
            dos.writeUTF(numCliente);

            // Lee y muestra el mensaje de bienvenida
            System.out.print(dis.readUTF());

            // Crea el bucle de consulta
            iniciarBusqueda(dis, dos);

            // Lee y muestra el mensaje de despedida
            System.out.print(dis.readUTF());

            // Cerrar la conexión
            dos.close();
            dis.close();
            sIn.close();
            sOut.close();
            s.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    // Función que contiene el bucle de las consultas que hará el cliente al
    // servidor hasta que el cliente decida terminarlo.
    public static void iniciarBusqueda(DataInputStream dis, DataOutputStream dos)
            throws IOException {
        Scanner sc = new Scanner(System.in);
        Boolean seguir = true;

        while (seguir) {
            // Lee la ciudad que instroduzca el cliente y la envia al server
            String ciudad = sc.next();
            dos.writeUTF(ciudad);

            // Lee del stream los resultados de la busqueda y los imprime
            String ciudadExiste = dis.readUTF();
            System.out.print(ciudadExiste);

            // Muestra mapa de la ciudad
            if (!ciudadExiste.startsWith("No")) {
                long size = dis.readLong();
                byte[] imgBytes = new byte[(int) size];
                dis.readFully(imgBytes);
                ImageIcon im = new ImageIcon(imgBytes);
                Mapa mapaCiudad = new Mapa(ciudad, im);
                mapaCiudad.setVisible(true);
            }

            // Lee mensaje de opciones del teclado y lo envia al stream
            while (true) {
                String opcion = sc.next();
                dos.writeUTF(opcion);
                String repetir = dis.readUTF();

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
    }

}
