/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientes.conexiones;

import clientes.display.Mapa;
import servidores.LugaresInterface;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import javax.swing.ImageIcon;

/**
 *
 * @author gaboeb
 */
public class ConexionRMI implements Runnable {

    private String numCliente;

    public ConexionRMI(String numCliente) {
        this.numCliente = numCliente;
    }

    @Override
    public void run() {
        try {
            // Obtenemos el registro
            Registry registry = LocateRegistry.getRegistry(5300);

            // Usamos lookup para encontrar el objeto en el registro
            LugaresInterface lt = (LugaresInterface) registry.lookup("rmi://localhost:5300/LugaresTuristicos");

            // Imprime mensaje de bienvenida
            System.out.print(lt.iniciarConsulta(numCliente));

            Scanner sc = new Scanner(System.in);
            Boolean seguir = true;

            // Bucle principal para seguir haciendo consultas
            while (seguir) {
                String ciudad = sc.next();
                // Imprime resultados de la búsqueda
                String ciudadExiste = lt.realizarBusqueda(numCliente, ciudad);
                System.out.print(ciudadExiste);

                // Crea mapa de la ciudad, solo si existe
                if (!ciudadExiste.startsWith("No")) {
                    ImageIcon im = new ImageIcon(lt.enviarMapa(ciudad));
                    Mapa mapaCiudad = new Mapa(ciudad, im);
                    mapaCiudad.setVisible(true);
                }

                // Bucle para validar la respuesta del usuario
                while (true) {
                    String opc = sc.next();
                    String repetir = lt.repetirConsulta(opc);

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

            // Imprime mensaje de despedida
            System.out.println(lt.finalizarConsulta(numCliente));

        } catch (NotBoundException | RemoteException e) {
            System.err.println("Excepción del cliente: " + e.toString());
            e.printStackTrace();
        }
    }

}
