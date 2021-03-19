
package clientes;

import clientes.conexiones.*;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;

/**
 *
 * @author gaboeb
 */
public class Cliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            XmlRpcClient cliente = new XmlRpcClient("http://localhost:5000/");
            Scanner sc = new Scanner(System.in);
            Boolean seguir = true;
            String numCliente;
            String msj;
            Vector<String> params = new Vector<String>();

            // Asigna número de cliente
            params.addElement(" ");
            numCliente = "" + (Integer) cliente.execute("servidorPrincipal.conectar", params);

            // Bucle para seguir con las consultas
            while (seguir) {

                // Mensaje de bienvenida
                params.clear();
                params.addElement(numCliente);
                msj = (String) cliente.execute("servidorPrincipal.darBienvenida", params);
                System.out.print(msj);

                while (true) {
                    try{
                        int opc = sc.nextInt();
                        params.clear();
                        params.addElement(String.valueOf(opc));
                        Object repetir = (String) 
                                cliente.execute("servidorPrincipal.repetirConsulta",
                                params);
                        seguir = !repetir.equals("no");
                        if (repetir.equals("no")) {
                            break;
                        }
                        if (repetir.equals("")) {
                            cliente.execute("servidorPrincipal.arrancarServer",
                                    params);
                            seleccionarConex(opc, numCliente);
                            break;
                        }
                        System.out.print(repetir);
                    } catch (InputMismatchException ime){
                        System.out.print("Solo puedes insertar números. \n"+
                                           "\tOpción: ");
                        sc.next();
                    }
                }
            }
            sc.close();

            // Mensaje de despedida
            params.clear();
            params.addElement(numCliente);
            msj = (String) cliente.execute("servidorPrincipal.finalizarConsulta",
                    params);
            System.out.println(msj);

        } catch (IOException | XmlRpcException e) {
            System.err.println("JavaClientPrincipal: " + e);
            e.printStackTrace();
        }
    }

    // ES necesario implementar hilos para las conexiones.
    private static void seleccionarConex(int opc, String numCliente) {
        try {
            Runnable r;
            Thread t = null;
            switch (opc) {
                case 1:
                    r = new ConexionSocket(numCliente);
                    t = new Thread(r);
                    t.start();
                    t.join();
                    break;
                case 2:
                    r = new ConexionRPC(numCliente);
                    t = new Thread(r);
                    t.start();
                    t.join();
                    break;
                case 3:
                    r = new ConexionRMI(numCliente);
                    t = new Thread(r);
                    t.start();
                    t.join();
                    break;
                default:
                    r = new ConexionSocket(numCliente);
                    t = new Thread(r);
                    t.start();
                    t.join();
                    break;
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
