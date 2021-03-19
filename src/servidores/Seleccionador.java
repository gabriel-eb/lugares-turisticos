package servidores;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gaboeb
 */
public class Seleccionador {

    private int numCliente;
    private HashMap<String, ArrayList<String>> datos;
    private Runnable sS;
    private ServidorRPC sRPC;
    private ServidorRMI sRMI;

    public Seleccionador() throws Exception {
        numCliente = 0;
        datos = leerDatos();
        sS = new ServidorSocket(datos);
        sRPC = new ServidorRPC();
        sRMI = new ServidorRMI();
        iniciarServers();
    }

    public void iniciarServers() {
        Thread t = new Thread(sS);
        t.start();
        sRPC.ejecutarServidor(datos);
        sRMI.ejecutarServidor(datos);

    }

    public int arrancarServer(String opc) throws Exception {
//        Thread t;
//        switch(Integer.parseInt(opc)){
//            case 1:
//                t = new Thread(sS);
//                t.start();
//                break;
//            case 2:
//                sRPC.ejecutarServidor(datos);
//                break;
//            case 3:
//                sRMI.ejecutarServidor(datos);
//                break;
//            default:
//                t = new Thread(sS);
//                t.start();
//        }
        return 0;
    }

    public int conectar(String a) {
        numCliente++;
        return numCliente;
    }

    public String darBienvenida(String cliente) {
        // Mensaje de conexión iniciada.
        System.out.println("El cliente " + cliente + " se ha conectado "
                + "al servidor principal.");

        // Envia un mensaje de bienvenida
        String msj = "Bienvenido, elige el tipo de conexión con la que deseas"
                + " realizar tus consultas:"
                + "\n\t1. Socket/Hilos"
                + "\n\t2. RPC"
                + "\n\t3. RMI"
                + "\n\t4. Desconectarme"
                + "\n\n\tOpción: ";
        msj = usarUTF8(msj);
        return msj;
    }

    // Obtiene la desición del cliente de seguir o no y regresa un mensaje
    public String repetirConsulta(String opc) {
        if (opc.equals("1") || opc.equals("2") || opc.equals("3")) {
            return "";
        }
        if (opc.equals("4")) {
            return "no";
        }

        return usarUTF8("\nNo entendí tu respuesta, repitela por favor."
                + " \n \t¿Desea hacer otra búsqueda? (Sí/No): ");
    }

    // Envia mensaje de despedida (al cliente) y de desconexión (al server)
    public String finalizarConsulta(String cliente) {
        System.out.println("El cliente " + cliente
                + " ha terminado su conexión.");
        return ("\n Se ha desconectado.");
    }

    // Esta función lee los destinos turísticos de un archivo de texto
    public HashMap<String, ArrayList<String>> leerDatos() {
        File archivo = new File("destinosTuristicos.txt");
        BufferedReader br;
        String linea;
        String ciudad;
        HashMap<String, ArrayList<String>> listaCompleta;
        listaCompleta = new HashMap<String, ArrayList<String>>();
        ArrayList<String> listaDestinos;

        try {
            br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(archivo), "UTF-8"));

            while ((linea = br.readLine()) != null) {
                String[] lista;
                lista = linea.split(",");
                ciudad = lista[0].toLowerCase();
                listaDestinos = new ArrayList();
                for (int i = 1; i < lista.length; i++) {
                    listaDestinos.add(lista[i] + "\n");
                }

                listaCompleta.put(ciudad, listaDestinos);
            }
            br.close();
        } catch (UnsupportedEncodingException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Fallo al leer base de datos.");
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return listaCompleta;
    }

    // Función que quita acentos al input del cliente
    public String quitarAcentos(String cadena) {
        return cadena.toLowerCase().replace("á", "a").replace("é", "e")
                .replace("í", "i").replace("ó", "o").replace("ú", "u");
    }

    // Codifica las cadenas para no corromper la acentuación
    public String usarUTF8(String msj) {
        try {
            return new String(msj.getBytes("UTF-8"), "ISO-8859-1");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(LugaresTuristicos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
}
