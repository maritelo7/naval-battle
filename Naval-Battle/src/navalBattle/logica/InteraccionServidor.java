/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.logica;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URISyntaxException;
import io.socket.client.IO;
import io.socket.client.Socket;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import navalBattle.presentacion.GUI_MenuPartidaController;
import navalBattle.presentacion.GUI_PrepararPartidaController;
import navalBattle.recursos.Utileria;

/**
 *
 * @author Mari
 */
public class InteraccionServidor {

   public static Socket socket;
   //GUI_MenuPartidaController menuController;

//   public static String obtenerIpAddress() throws UnknownHostException {
//      InetAddress localhost;
//      String ipLocalhost = "";
//      localhost = InetAddress.getLocalHost();
//      ipLocalhost = localhost.getHostAddress().trim();
//      return ipLocalhost;
//   }
   public void conectarServidor(String nombreUsuario, GUI_MenuPartidaController controller) throws URISyntaxException, UnknownHostException {
      //this.controller = controller;
      ResourceBundle resources = ResourceBundle.getBundle("navalBattle.recursos.ConfiguracionServidor");
      final String numeroPuerto = resources.getString("puertoServidor");
      String ipServidor = resources.getString("ipServidor");
      socket = IO.socket("http://" + ipServidor + ":" + numeroPuerto);
      //System.out.println("IP " + ipServidor);
      socket.on(Socket.EVENT_DISCONNECT, (Object... os) -> {
         System.out.println("Se fue el servidor");

         //controller
      });

      socket.connect();
      socket.emit("registrarDatos", nombreUsuario);
   }

   public void esperarAInvitado(GUI_PrepararPartidaController controller) throws URISyntaxException, UnknownHostException{
      socket.on("retado", (Object... os) -> {
         String nombre = (String) os[0];
         Platform.runLater(() -> {
            controller.cargarAviso("tittleRetado", "mensajeRetado", nombre);
            try {
               controller.enlazarContrincante();
            } catch (InterruptedException | JsonProcessingException ex) {
               Logger.getLogger(InteraccionServidor.class.getName()).log(Level.SEVERE, null, ex);
            }
         });
      });
   }

   public void conectarInvitado(String nombreUsuario, String nombreContrincante, Utileria bandera) throws URISyntaxException, UnknownHostException {

      socket.emit("envioRetador", nombreUsuario, nombreContrincante);
      socket.on("sinJugadorRetado", (Object... os) -> {
         synchronized (bandera) {
            bandera.notify();
         }
      });
      socket.on("conJugadorRetado", (Object... os) -> {
         synchronized (bandera) {
            bandera.setBandera(true);
            bandera.notify();
         }
      });

   }
//   public void enviarTablero(String nombreUsuario, Tablero tablero, Tablero tableroEnemigo){
//      Gson gson = new Gson();
//      JsonParser parser = new JsonParser();
//      
//      socket.emit("envioTablero",nombreUsuario, gson.toJson(tablero));
//      socket.on("recibirTablero", (Object... os) -> {
//         String data = (String) os[0];
//         JsonObject obj = parser.parse(data).getAsJsonObject();
//         Tablero tableroEnemigoLocal = gson.fromJson(data, Tablero.class);
//         synchronized (tableroEnemigo) {
//            tableroEnemigo.setCasillas(tableroEnemigoLocal.getCasillas());
//            tableroEnemigo.setEnemigo(tableroEnemigoLocal.isEnemigo());
//            tableroEnemigo.notify();
//         }
//      
//      });
//   }

   public void enviarTablero(String nombreUsuario, Tablero tablero, Tablero tableroEnemigo) throws JsonProcessingException {
      ObjectMapper mapper = new ObjectMapper();
      mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
         System.out.println("Intenta");
         String jsonInString = mapper.writeValueAsString(tablero);
         socket.emit("envioTablero", nombreUsuario, jsonInString);
         System.out.println("Enviado");


      socket.on("recibirTablero", (Object... os) -> {
         String data = (String) os[0];
         try {
            Tablero tableroEnemigoLocal = mapper.readValue(data, Tablero.class);
            synchronized (tableroEnemigo) {
               tableroEnemigo.setCasillas(tableroEnemigoLocal.getCasillas());
               tableroEnemigo.setEnemigo(tableroEnemigoLocal.isEnemigo());
               tableroEnemigo.notify();
            }
         } catch (IOException ex) {
            Logger.getLogger(InteraccionServidor.class.getName()).log(Level.SEVERE, null, ex);
         }

      });
   }
}
