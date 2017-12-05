/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.logica;

import java.net.URISyntaxException;
import io.socket.client.IO;
import io.socket.client.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
/**
 *
 * @author Mari
 */
public class InteraccionServidor {
   
   public static String obtenerIpAddress() throws UnknownHostException {
      InetAddress localhost;
      String ipLocalhost = "";
      localhost = InetAddress.getLocalHost();
      ipLocalhost = localhost.getHostAddress().trim();
      return ipLocalhost;
   }
   
    public static void conectar(String nombreUsuario) throws URISyntaxException, UnknownHostException {
      ResourceBundle resources = ResourceBundle.getBundle("navalBattle.recursos.ConfiguracionServidor");
      //String nombreDelEquipoServidor = resources.getString("nombreDelEquipoServidor");
      String numeroPuerto = resources.getString("puertoServidor");
      String ipServidor = resources.getString("ipServidor");      
      Socket socket = IO.socket("http://" + ipServidor + ":"+numeroPuerto);
      //Socket socket = IO.socket("http://"+nombreDelEquipoServidor+":"+numeroPuerto);
      socket.on(Socket.EVENT_CONNECT, (Object... os) -> {
      });   
      String ipLocalHost = obtenerIpAddress();
      socket.emit("envioDatos", nombreUsuario, ipLocalHost);
      socket.connect();
   }
   
}
