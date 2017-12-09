/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.logica;

import java.net.URISyntaxException;
import io.socket.client.IO;
import io.socket.client.Socket;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

/**
 *
 * @author Mari
 */
public class InteraccionServidor {

   public static Socket socket;


//   public static String obtenerIpAddress() throws UnknownHostException {
//      InetAddress localhost;
//      String ipLocalhost = "";
//      localhost = InetAddress.getLocalHost();
//      ipLocalhost = localhost.getHostAddress().trim();
//      return ipLocalhost;
//   }
   public static void conectarServidor(String nombreUsuario) throws URISyntaxException, UnknownHostException {
      ResourceBundle resources = ResourceBundle.getBundle("navalBattle.recursos.ConfiguracionServidor");
      final String numeroPuerto = resources.getString("puertoServidor");
      String ipServidor = resources.getString("ipServidor");
      socket = IO.socket("http://" + ipServidor + ":" + numeroPuerto);
      //System.out.println("IP " + ipServidor);
      socket.on(Socket.EVENT_DISCONNECT, (Object... os) -> {
         //System.out.println("Se fue el servidor");
      });
      
      socket.connect();
      socket.emit("registrarDatos", nombreUsuario);
   }

   public static void esperarAInvitado() throws URISyntaxException, UnknownHostException {
      socket.on("retado", (Object... os) -> {
         
         
         System.out.println("Fuiste retado en interacción");
         
      });
      
   }

   public static boolean conectarInvitado(String nombreUsuario, String nombreContrincante) throws URISyntaxException, UnknownHostException {
      boolean[] auxiliar = new boolean[1];
      auxiliar[0] = true;
      socket.emit("envioRetador", nombreUsuario, nombreContrincante);
      socket.on("sinJugadorRetado", (Object... os) -> {
         System.out.println("No encontró jugador"); 
         auxiliar[0] = false;
      });
      return auxiliar[0];
   }

}
