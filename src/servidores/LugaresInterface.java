package servidores;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author gaboeb
 */
public interface LugaresInterface extends Remote {
    // Asigna un número de cliente al conectarse
//    String  conectar(String nuevo) throws RemoteException;

    // Envia mensaje de bienvenida (al cliente) y de conexión (al server)
    String iniciarConsulta(String cliente) throws RemoteException;

    // Obtiene la desición del cliente de seguir o no y regresa un mensaje
    String repetirConsulta(String opc) throws RemoteException;

    // Envia mensaje de despedida (al cliente) y de desconexión (al server)
    String finalizarConsulta(String cliente) throws RemoteException;

    // Función que realiza la consulta del cliente
    String realizarBusqueda(String cliente, String ciudad) throws RemoteException;

    // Envia imagen de la ciudad elegida
    byte[] enviarMapa(String ciudad) throws RemoteException;
}
