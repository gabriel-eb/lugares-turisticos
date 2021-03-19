package servidores;

import java.io.*;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gaboeb
 */
public class LugaresTuristicos extends UnicastRemoteObject
        implements LugaresInterface {

    private final String serverTipo;
    private final HashMap<String, ArrayList<String>> datos;

    LugaresTuristicos(HashMap<String, ArrayList<String>> datos, String serverTipo)
            throws Exception {
        super();
        this.datos = datos;
        this.serverTipo = serverTipo;
    }

//    @Override
//    public String conectar(String numCliente) throws RemoteException {
//        return numCliente;
//    }
    @Override
    public String iniciarConsulta(String cliente) throws RemoteException {
        // Mensaje de conexión iniciada al server
        System.out.println("El cliente " + cliente
                + " se ha conectado al servidor " + serverTipo + ".");

        // El mensaje de bienvenida al cliente
        String msj = "Bienvenido, elige una ciudad y te mostraremos"
                + " algunos destinos turísticos que visitar."
                + "\n(CDMX, Puebla, Queretaro, Monterrey, Guadalajara)"
                + "\n Ciudad: ";
        return usarUTF8(msj);
    }

    @Override
    public String repetirConsulta(String opc) throws RemoteException {
        opc = quitarAcentos(opc);
        if (opc.equals("si")) {
            return "\n\tCiudad: ";
        }
        if (opc.equals("no") || opc.equals("adios") || opc.equals("bye")) {
            return "no";
        }

        return usarUTF8("\nNo entendí tu respuesta, repitela por favor."
                + " \n \t¿Desea hacer otra búsqueda? (Sí/No): ");
    }

    @Override
    public String finalizarConsulta(String cliente) throws RemoteException {
        System.out.println("El cliente " + cliente
                + " ha terminado sus consultas al servidor "
                + serverTipo + ".");
        return usarUTF8("\n Esperamos le haya sido de ayuda, buen viaje.");
    }

    @Override
    public String realizarBusqueda(String cliente, String ciudad)
            throws RemoteException {
        System.out.println("El cliente " + cliente + " eligió (en server "
                + serverTipo + "): " + ciudad);
        // Obtener destinos de la cd. elegida en un arreglo
        String destinos = obtenerListaDest(ciudad);

        return usarUTF8(destinos + "\n\n¿Desea hacer otra búsqueda? (Sí/No): ");
    }

    @Override
    public byte[] enviarMapa(String ciudad) throws RemoteException {
        try {
            File imgPath = new File("imagenes/" + ciudad + ".png");
            return Files.readAllBytes(imgPath.toPath());
        } catch (IOException ex) {
            Logger.getLogger(LugaresTuristicos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    // Recibe la ciudad que se va a buscar:
    // En caso de encontrarla, regresa una cadena con todos los destinos,
    // si no regresa un mensaje que no se ha encontrado.
    public String obtenerListaDest(String ciudad) {
        HashMap<String, ArrayList<String>> listaCompleta;
        ciudad = quitarAcentos(ciudad);

        listaCompleta = datos;

        String destinos = "";
        if (listaCompleta.get(ciudad) == null) {
            return "No se ha encontrado destinos en esta ciudad.";
        } else {
            for (String elem : listaCompleta.get(ciudad)) {
                destinos = destinos.concat("\t- " + elem);
            }

            return destinos;
        }
    }

    // Función que quita acentos al input del cliente
    public String quitarAcentos(String cadena) {
        return cadena.toLowerCase().replace("á", "a").replace("é", "e")
                .replace("í", "i").replace("ó", "o").replace("ú", "u");
    }

    // Codifica las cadenas para no corromper la acentuación
    public String usarUTF8(String msj) {
        try {
            if (serverTipo.equals("RPC")) {
                return new String(msj.getBytes("UTF-8"), "ISO-8859-1");
            }
            return msj;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(LugaresTuristicos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public long tamImagen(String ciudad) {
        File img = new File("imagenes/" + ciudad + ".png");
        return img.length();
    }

}
