/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.logica;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URISyntaxException;
import io.socket.client.IO;
import io.socket.client.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import navalBattle.presentacion.GUI_JugarPartidaController;
import navalBattle.presentacion.GUI_PrepararPartidaController;
import navalBattle.recursos.Utileria;

/**
 *
 * @author Mari
 */
public class InteraccionServidor {

   public static Socket socket;

   public void conectarServidor(String nombreUsuario, Utileria bandera) {
      try {
         ResourceBundle resources = ResourceBundle.getBundle("navalBattle.recursos.ConfiguracionServidor");
         final String numeroPuerto = resources.getString("puertoServidor");
         String ipServidor = resources.getString("ipServidor");
         socket = IO.socket("http://" + ipServidor + ":" + numeroPuerto);
         socket.on(Socket.EVENT_DISCONNECT, (Object... os) -> {
            Platform.runLater(() -> {
               Utileria.cargarAviso("titleAviso", "mensajeErrorConexion");
            });
            System.out.println("Se fue el servidor");

         });
         socket.connect();
         socket.emit("registrarDatos", nombreUsuario);
         socket.on(Socket.EVENT_CONNECT_ERROR, (Object... os) -> {
            synchronized (bandera) {
               bandera.setBandera(false);
               bandera.notify();
            }
         });
         socket.on(Socket.EVENT_CONNECT, (Object... os) -> {
            synchronized (bandera) {
               bandera.notify();
            }
         });
      } catch (URISyntaxException e) {
         Logger.getLogger(InteraccionServidor.class.getName()).log(Level.SEVERE, null, e);
      }
   }

   public void esperarAInvitado(String jugador, GUI_PrepararPartidaController controller) throws URISyntaxException, UnknownHostException {
      socket.on("retado", (Object... os) -> {
         String nombre = (String) os[0];
         Platform.runLater(() -> {
            controller.cargarAviso("titleRetado", "mensajeRetado", nombre);
            controller.setNombreAdversario(nombre);

         });
      });
      socket.on("esperarAdversario", (Object... os)->{
         controller.activarReady();
         try {
            controller.checkListo();
         } catch (InterruptedException ex) {
            Logger.getLogger(InteraccionServidor.class.getName()).log(Level.SEVERE, null, ex);
         }
      });
      esperarTablero(controller);
   }
   public void adversarioListo(String nombreUsuario){
      socket.emit("adversarioListo", nombreUsuario);
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

   public void enviarTablero(String nombreUsuario, TableroSimple tablero) {
      Gson gson = new Gson();
      socket.emit("envioTablero", nombreUsuario, gson.toJson(tablero));
   }

   public void enviarMisil(String nombreUsuario, Misil misil, GUI_JugarPartidaController controller) {
      Gson gson = new Gson();
      socket.emit("enviarMisil", nombreUsuario, gson.toJson(misil));

   }

   public void activarServiciosJugarPartida(GUI_JugarPartidaController controller) {
      JsonParser parser = new JsonParser();
      Gson gson = new Gson();
      socket.on("recibirMisil", (Object... os) -> {
         String data = (String) os[0];
         JsonObject obj = parser.parse(data).getAsJsonObject();
         Misil misilRecibido = gson.fromJson(data, Misil.class);
         Platform.runLater(() -> {
            controller.actualizarMiTablero(misilRecibido);
         });

      });
      socket.on("ajustarTurno", (Object... os) -> {
         Platform.runLater(() -> {
            controller.ajustarMiTurno(true);
         });
      });
      socket.on("recibirCasillasALiberar", (Object... os)-> {
         String data = (String) os[0];
         JsonObject obj = parser.parse(data).getAsJsonObject();
         TableroSimple tableroActualizadoSimple = gson.fromJson(data, TableroSimple.class);
         Platform.runLater(() -> {
            controller.liberarCasillasEnemigo(tableroActualizadoSimple);
         });
      });
      socket.on(("recibirPuntuacion"), (Object... os)-> {
         int puntos = (int)os[0];
         Platform.runLater(()->{
            controller.actualizarPuntuacionEnemigo(puntos);
         });
      });
   }
   public void enviarPuntuacion (String nombreUsuario, int puntaje){
      socket.emit("enviarPuntuacion", nombreUsuario, puntaje);
   }
   public void dejarAdversario(String nombreUsuario, String retado){
      socket.emit("adiosAdversario", nombreUsuario, retado);
   }

   public void cederTurno(String nombreUsuario) {
      socket.emit("cederTurno", nombreUsuario);
   }
   public void enviarCasillasALiberar (String nombreUsuario, TableroSimple tableroActualizado){
      Gson gson = new Gson();
      socket.emit("enviarCasillasALiberar", nombreUsuario, gson.toJson(tableroActualizado));
   }

   public void esperarTablero(GUI_PrepararPartidaController controller) {
      Gson gson = new Gson();
      JsonParser parser = new JsonParser();
      socket.on("recibirTablero", (Object... os) -> {
         Tablero tableroEnemigo = new Tablero(true);
         String data = (String) os[0];
         JsonObject obj = parser.parse(data).getAsJsonObject();
         TableroSimple tableroEnemigoLocalSimple = gson.fromJson(data, TableroSimple.class);
         CasillaSimple casillaSimple;
         Casilla casilla;
         ArrayList<Casilla> casillas = new ArrayList<>();
         int contador = 0;
         for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
               casillaSimple = tableroEnemigoLocalSimple.getCasillasSimples().get(contador);
               casilla = new Casilla(j, i);
               casilla.setNave(casillaSimple.getNave());
               casillas.add(casilla);
               contador++;
            }
         }
         tableroEnemigo.setCasillas(casillas);
         tableroEnemigo.setEnemigo(tableroEnemigoLocalSimple.isEnemigo());
         Platform.runLater(() -> {
            controller.setTableroEnemigo(tableroEnemigo);

         });

      });
   }

}
