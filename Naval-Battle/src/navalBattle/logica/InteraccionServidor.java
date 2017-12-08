/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.logica;

import java.net.URISyntaxException;
import io.socket.client.IO;
import static io.socket.client.IO.*;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.net.InetAddress;
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

   public static String esperarAInvitado() throws URISyntaxException, UnknownHostException {
      String[] nombreAdversario = new String[1];
      nombreAdversario[0] = "";
      socket.on("retado", new Emitter.Listener() {
      @Override
         public void call(Object... os) {
            nombreAdversario[0] = (String)os[0];
         }
      });
      return nombreAdversario[0];
   }

   public static boolean conectarInvitado(String nombreUsuario, String nombreContrincante) throws URISyntaxException, UnknownHostException {
      boolean[] auxiliar = new boolean[1];
      socket.emit("envioRetador", nombreUsuario, nombreContrincante, socket.id());
      socket.on("sinJugadorRetado", (Object... os) -> {
         System.out.println("No encontró jugador"); 
         auxiliar[0] = false;
      });
      return auxiliar[0];
   }

}
