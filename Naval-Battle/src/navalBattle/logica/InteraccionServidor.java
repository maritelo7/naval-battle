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
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import navalBattle.presentacion.GUI_JugarPartidaController;
import navalBattle.presentacion.GUI_PrepararPartidaController;
import navalBattle.recursos.Utileria;

/**
 * @author Maribel Tello Rodriguez
 * @author José Alí Valdivia Ruiz
 */
public class InteraccionServidor {

   Socket socket;

   /**
    * Método para conectar con el servidor remoto y registrar los datos del jugador
    * @param nombreUsuario nickname del jugador que intenta conectarse al servidor
    * @param bandera instancia de la clase Utileria para utilizar un atributo booleano
    */
   public void conectarServidor(String nombreUsuario, Utileria bandera) {
      try {
         ResourceBundle resources = ResourceBundle.getBundle("navalBattle.recursos.ConfiguracionServidor");
         final String numeroPuerto = resources.getString("puertoServidor");
         String ipServidor = resources.getString("ipServidor");
         socket = IO.socket("http://" + ipServidor + ":" + numeroPuerto);
         socket.on(Socket.EVENT_CONNECT_ERROR, (Object... os) -> {
            cerrarConexion();
            synchronized (bandera) {
               bandera.notify();
            }            
         });
         socket.on(Socket.EVENT_CONNECT, (Object... os) -> {
            synchronized (bandera) {
               bandera.setBandera(true);
               bandera.notify();
            }
            
         });
         socket.on(Socket.EVENT_DISCONNECT, (Object... os) -> {
            cerrarConexion();
            Platform.runLater(() -> {
               Utileria.cargarAviso("titleAlerta", "mensajeErrorConexion");
            });
         });
         socket.connect();
         socket.emit("registrarDatos", nombreUsuario); 
      } catch (URISyntaxException e) {
         Logger.getLogger(InteraccionServidor.class.getName()).log(Level.SEVERE, null, e);
      }
   }

   /**
    * Método para cerrar la conexión del socket y prepararlo para una nueva conexión
    */
   public void cerrarConexion(){
      socket.close();
      socket = null;
   }
   /**
    * Método para esperar a un adversario o activar un evento cuando se recibe un reto
    * @param controller controlador de la clase de GUI_PrepararPartidaController
    */
   public void esperarAInvitado(GUI_PrepararPartidaController controller)  {
      socket.on("retado", (Object... os) -> {
         String nombre = (String) os[0];
         Platform.runLater(() -> {
            Utileria.cargarAviso("titleRetado", "mensajeRetado", nombre);
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

   /**
    * Método para notificar que el adversario está listo
    * @param nombreUsuario nickname del jugador
    */
   public void adversarioListo(String nombreUsuario){
      socket.emit("adversarioListo", nombreUsuario);
   }

   /**
    * Método para conectar con un adversario y esperar la respuesta
    * @param nombreUsuario nickname del usuario que intenta conectarse
    * @param nombreContrincante nickname del contrincante con el que desea jugar
    * @param bandera instancia de la clase Utileria para utilizar un atributo boolean
    */
   public void conectarInvitado(String nombreUsuario, String nombreContrincante, Utileria bandera) {
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

   /**
    * Método par envíar un tablero al adversario
    * @param nombreUsuario nickname del jugador del que se va a enviar el tablero
    * @param tablero el tablero con las casillas y la disposición de las naves
    */
   public void enviarTablero(String nombreUsuario, TableroSimple tablero) {
      Gson gson = new Gson();
      socket.emit("envioTablero", nombreUsuario, gson.toJson(tablero));
   }
   
   /**
    * Método para activar la esperar del tablero enemigo
    * @param controller controlador de la clase de GUI_PrepararPartidaController
    */
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

   /**
    * Método para activar todos los eventos posibles que pueden ocurrir en una partida
    * @param controller controlador de la clase GUI_JugarPartidaController
    */
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
      socket.on("recibirRendicion", (Object... os)-> {
         Platform.runLater(() -> {
            controller.enemigoRendido();
         });
      });
   }
   
   /**
    * Método para envíar un misil al tablero enemigo
    * @param nombreUsuario nickname del jugador
    * @param misil misil que se envía al tablero enemigo
    * @param controller controlador de la clase GUI_JugarPartidaController 
    */
   public void enviarMisil(String nombreUsuario, Misil misil, GUI_JugarPartidaController controller) {
      Gson gson = new Gson();
      socket.emit("enviarMisil", nombreUsuario, gson.toJson(misil));

   }

   /**
    * Método para envíar una puntuacion local 
    * @param nombreUsuario nickname del jugador
    * @param puntaje puntaje obtenido del jugador
    */
   public void enviarPuntuacion (String nombreUsuario, int puntaje){
      socket.emit("enviarPuntuacion", nombreUsuario, puntaje);
   }

   /**
    * Método para notificar al adversario que se abandonará la partida
    * @param nombreUsuario nickname del jugador
    * @param retado nickname del adversario
    */
   public void dejarAdversario(String nombreUsuario, String retado){
      socket.emit("adiosAdversario", nombreUsuario, retado);
   }

   /**
    * Método para notificar al adversario que ahora es su turno
    * @param nombreUsuario nickname del jugador
    */
   public void cederTurno(String nombreUsuario) {
      socket.emit("cederTurno", nombreUsuario);
   }

   /**
    * Método para envíar un tablero con laa casillas actualizadas
    * @param nombreUsuario nickname del jugador
    * @param tableroActualizado tablero actualizado del jugador que se envía
    */
   public void enviarCasillasALiberar (String nombreUsuario, TableroSimple tableroActualizado){
      Gson gson = new Gson();
      socket.emit("enviarCasillasALiberar", nombreUsuario, gson.toJson(tableroActualizado));
   }

   /**
    * Método para envíar un rendición al jugador enemigo
    * @param nombreUsuario nickname del jugador
    */
   public void enviarRendicion(String nombreUsuario){
      socket.emit("enviarRendicion", nombreUsuario);
   }

}
