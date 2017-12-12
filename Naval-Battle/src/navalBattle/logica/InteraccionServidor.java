/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.logica;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.net.URISyntaxException;
import io.socket.client.IO;
import io.socket.client.Socket;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import navalBattle.presentacion.GUI_MenuPartidaController;
import navalBattle.presentacion.GUI_PrepararPartidaController;
import navalBattle.recursos.Utileria;
import org.json.JSONException;
import org.json.JSONObject;

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
   public void enviarTablero(String nombreUsuario, Tablero tablero, Tablero tableroEnemigo){
      Gson gson = new Gson();
      JsonParser parser = new JsonParser();
      TableroSimple tableroSimple = new TableroSimple(tablero.isEnemigo());
      tableroSimple.setCasillasSimples(tablero.getCasillasSimples());
      socket.emit("envioTablero",nombreUsuario, gson.toJson(tableroSimple));
      socket.on("recibirTablero", (Object... os) -> {
         String data = (String) os[0];
         JsonObject obj = parser.parse(data).getAsJsonObject();
         TableroSimple tableroEnemigoLocalSimple = gson.fromJson(data, TableroSimple.class);
         synchronized (tableroEnemigo) {
            CasillaSimple casillaSimple;
            Casilla casilla;
            ArrayList<Casilla> casillas = new ArrayList<>();
            int contador = 0;
            for (int i = 0; i < 10; i++) {
               for (int j = 0; j < 10; j++) {
                  casillaSimple = tableroEnemigoLocalSimple.getCasillasSimples().get(contador);
                  casilla = new Casilla(j,i);
                  casilla.setNave(casillaSimple.getNave());
                  casillas.add(casilla);
                  contador++;
               }
            }
            tableroEnemigo.setCasillas(casillas);
            tableroEnemigo.setEnemigo(tableroEnemigoLocalSimple.isEnemigo());
            tableroEnemigo.notify();
         }
      
      });
   }

//   public void enviarTablero(String nombreUsuario, Tablero tablero, Tablero tableroEnemigo) throws JsonProcessingException {
//      ObjectMapper mapper = new ObjectMapper();
//      //mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//      //System.out.println("Intenta");
//      //String jsonInString = mapper.writeValueAsString(tablero);
//      JSONObject json = new JSONObject();
//      try {
//         //json.putOnce("Tablero", tablero);
//         json.putOpt("Tablero", tablero);
//      } catch (JSONException jsone) {
//      }
//      
//      socket.emit("envioTablero", nombreUsuario, json.toString());
//      
//      System.out.println("Enviado");
//
//      socket.on("recibirTablero", (Object... os) -> {
//         String data = (String) os[0];
//         System.out.println(data);
//         try {
//            Tablero tableroEnemigoLocal = deserializeJson(data, Tablero.class);
//            synchronized (tableroEnemigo) {
//               tableroEnemigo.setCasillas(tableroEnemigoLocal.getCasillas());
//               tableroEnemigo.setEnemigo(tableroEnemigoLocal.isEnemigo());
//               tableroEnemigo.notify();
//            }
//         } catch (IOException ex) {
//            Logger.getLogger(InteraccionServidor.class.getName()).log(Level.SEVERE, null, ex);
//         }
//      });
//   }
//      
//   public static <T> T deserializeJson(final String json, final Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
//      ObjectMapper om = new ObjectMapper();
//   return om.readValue(json, clazz);
//}
   
}
