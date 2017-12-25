/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.presentacion;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import navalBattle.logica.AdministracionCuenta;
import navalBattle.logica.Casilla;
import navalBattle.logica.CasillaSimple;
import navalBattle.logica.CuentaUsuario;
import navalBattle.logica.InteraccionServidor;
import navalBattle.logica.Misil;
import navalBattle.logica.Tablero;
import navalBattle.logica.TableroSimple;
import navalBattle.recursos.Utileria;

/**
 * FXML Controller class
 *
 * @author Maribel Tello Rodriguez
 * @author José Alí Valdivia Ruiz
 */
public class GUI_JugarPartidaController implements Initializable {

   @FXML
   private Label labelConteo;
   @FXML
   private Label labelTiempoRestante;
   @FXML
   private Label labelPuntuacionHost;
   @FXML
   private Label labelPuntuacionAdversario;
   @FXML
   private Label labelTurno;
   @FXML
   private Label labelMiPuntuacion;
   @FXML
   private Label labelPuntuacionEnemigo;
   @FXML
   private Label labelMiNombre;
   @FXML
   private Label labelNombreAdversario;
   @FXML
   private JFXButton buttonRendirse;
   @FXML
   private Pane paneTableroJugador;
   @FXML
   private Pane paneTableroEnemigo;
   @FXML
   private ProgressBar pbConteo;

   private final VBox columnasJugador = new VBox();
   private final VBox columnasEnemigo = new VBox();
   private Tablero tableroJugador;
   private Tablero tableroEnemigo;
   private CuentaUsuario cuentaLogueada;
   private int navesJugador = 9;
   private int navesEnemigo = 9;
   private final int puntajeJugador = 0;
   private final int puntajeEnemigo = 0;
   private int numeroTirosFallidos = 0;
   private int numeroTirosAcertados = 0;
   final static String RECURSO_IDIOMA = "navalBattle.recursos.idiomas.Idioma";
   private InteraccionServidor interaccionServidor = new InteraccionServidor();
   private GUI_JugarPartidaController controller;
   private final Timeline timeLine = new Timeline();
   private Event ultimoEvent;

   /**
    * Initializes the controller class.
    *
    * @param url
    * @param rb
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
      cargarIdioma();

      buttonRendirse.setOnAction(event -> {
         timeLine.stop();
         Utileria.cargarAviso("titleDerrota", "mensajeDerrota");
         notificarRendicion();
         regresarAMenu(event);
      });

      paneTableroEnemigo.setOnMouseClicked(event -> {
         this.ultimoEvent = event;
         Casilla casilla = (Casilla) event.getTarget();
         if (!casilla.isAtacado()) {
            Misil misil = new Misil((int) casilla.getX(), (int) casilla.getY());
            enviarMisil(misil);
            if (casilla.atacadaANave()) {
               numeroTirosAcertados++;
               ajustarMiTurno(true);
               cargarSonidoDestruccion();
               actualizarMiPuntuacion(numeroTirosAcertados);
            } else {
               numeroTirosFallidos++;
               cederTurno();
               ajustarMiTurno(false);
               cargarSonidoAgua();
            }
         }

      });
   }

   /**
    * Método para cargar objeto cuenta y utilzar sus valores en este controller
    *
    * @param cuenta la CuentaUsuario con la que se ha iniciado sesión
    */
   public void cargarCuenta(CuentaUsuario cuenta) {
      this.cuentaLogueada = cuenta;
      labelMiNombre.setText(cuentaLogueada.getNombreUsuario());
   }

   /**
    * Método para recibir el tablero del jugador
    *
    * @param tableroJugador Tablero
    */
   public void setTableroJugador(Tablero tableroJugador) {
      this.tableroJugador = tableroJugador;
      cargarTableroJugador();

   }

   /**
    * Método para recirbir el tablero del enemigo
    *
    * @param tableroEnemigo Tablero
    */
   public void setTableroEnemigo(Tablero tableroEnemigo) {
      this.tableroEnemigo = tableroEnemigo;
      cargarTableroEnemigo();
   }

   /**
    * Método para asignar la interacción del servidor que se ha creado desde el menú de partida
    *
    * @param interaccionServidor Instancia de la clase
    */
   public void setInteraccionServidor(InteraccionServidor interaccionServidor) {
      this.interaccionServidor = interaccionServidor;
   }

   /**
    * Método para cargar el controller de la ventana y así la clase de interacción servidor puede
    * invocar métodos de la clase
    *
    * @param controller
    */
   public void cargarController(GUI_JugarPartidaController controller) {
      this.controller = controller;
   }

   /**
    * Método para cargar el tablero del jugador
    */
   public void cargarTableroJugador() {
      int contador = 0;
      ArrayList<Casilla> casillas = tableroJugador.getCasillas();
      for (int i = 0; i < 10; i++) {
         HBox filas = new HBox();
         for (int j = 0; j < 10; j++) {
            Casilla casilla = new Casilla(j, i);
            casilla.setNave(casillas.get(contador).getNave());
            casilla.setX(j);
            casilla.setY(i);
            filas.getChildren().add(casilla);
            if (casilla.getNave() != null) {
               casilla.setFill(Color.ORANGE);
            }
            contador++;
         }
         columnasJugador.getChildren().add(filas);
      }
      paneTableroJugador.getChildren().add(columnasJugador);
   }

   /**
    * Método para cargar el tablero del enemigo
    */
   public void cargarTableroEnemigo() {
      int contador = 0;
      ArrayList<Casilla> casillas;
      casillas = tableroEnemigo.getCasillas();
      for (int i = 0; i < 10; i++) {
         HBox filas = new HBox();
         for (int j = 0; j < 10; j++) {
            Casilla casilla = new Casilla(j, i);
            casilla.setNave(casillas.get(contador).getNave());
            casilla.setX(j);
            casilla.setY(i);
            filas.getChildren().add(casilla);
            contador++;
         }
         columnasEnemigo.getChildren().add(filas);
      }
      paneTableroEnemigo.getChildren().add(columnasEnemigo);
   }

   /**
    * Método para obtener la casilla en tales posiciones
    *
    * @param x posición x de la casilla
    * @param y posición y de la casilla
    * @param jugador boolean para determinar cuál casilla regresar
    * @return Casilla de tablero
    */
   public Casilla getCasillaJugador(int x, int y, boolean jugador) {
      if (jugador) {
         return (Casilla) ((HBox) columnasJugador.getChildren().get(y)).getChildren().get(x);
      }
      return (Casilla) ((HBox) columnasEnemigo.getChildren().get(y)).getChildren().get(x);
   }

   /**
    * Método para liberar las casillas adyacentes cuando se ha destruido completamente una nave
    *
    * @param casillaAtacada última casilla atacada
    * @param jugador boolean para determinar el tablero
    */
   public void liberarcolindantes(Casilla casillaAtacada, boolean jugador) {
      boolean horizontal = casillaAtacada.getNave().isHorizontal();
      if (horizontal) {
         ArrayList<Casilla> casillas = casillasDeNaveHorizontal(casillaAtacada, jugador);
         liberarCasillas(casillas, jugador);
      } else {
         ArrayList<Casilla> casillas = casillasDeNaveVertical(casillaAtacada, jugador);
         liberarCasillas(casillas, jugador);
      }
   }

   /**
    * Método para liberar (colorear) las casillas colindantes en caso de destruir por completo una
    * nave
    *
    * @param casillas array con las casillas colindantes
    * @param jugador bandera para identificar tablero
    */
   public void liberarCasillas(ArrayList<Casilla> casillas, boolean jugador) {
      int x;
      int y;
      ArrayList<CasillaSimple> casillasALiberar = new ArrayList<>();
      CasillaSimple casillaSimple;
      for (Casilla casilla : casillas) {
         x = (int) casilla.getX();
         y = (int) casilla.getY();
         for (Casilla colindante : getColindantes(x, y, jugador)) {
            if (!colindante.isAtacado()) {
               casillaSimple = new CasillaSimple((int) colindante.getX(), (int) colindante.getY());
               colindante.liberar();
               casillasALiberar.add(casillaSimple);
            }
         }
      }
      actualizarTableroEnemigo(casillasALiberar);
      reducirNaves(jugador);
   }

   /**
    * Método para recolectar las casillas totales de la nave destruida con posición horizontal
    *
    * @param casillaAtacada Última casilla atacada
    * @param jugador bandera para identificar el tablero
    * @return Array con las casillas colindantes
    */
   public ArrayList<Casilla> casillasDeNaveHorizontal(Casilla casillaAtacada, boolean jugador) {
      int x = (int) casillaAtacada.getX();
      int y = (int) casillaAtacada.getY();
      int tamanio = casillaAtacada.getNave().getTamanio();
      ArrayList<Casilla> casillas = new ArrayList<>();
      for (int i = x; i > x - tamanio; i--) {
         if (posicionValida(i, y)) {
            Casilla casilla = getCasillaJugador(i, y, jugador);
            if (casilla.getNave() != null && casillas.size() < tamanio) {
               casillas.add(casilla);
            } else {
               break;
            }
         }
      }
      if (casillas.size() < tamanio) {
         for (int i = x; i < x + tamanio; i++) {
            if (posicionValida(i, y)) {
               Casilla casilla = getCasillaJugador(i, y, jugador);
               if (casilla.getNave() != null && casillas.size() <= tamanio) {
                  casillas.add(casilla);
               } else {
                  break;
               }
            }
         }
      }
      return casillas;
   }

   /**
    * Método para recolectar las casillas totales de la nave destruida con posición vertical
    *
    * @param casillaAtacada Última casilla atacada
    * @param jugador bandera para identificar el tablero
    * @return Array con las casillas colindantes
    */
   public ArrayList<Casilla> casillasDeNaveVertical(Casilla casillaAtacada, boolean jugador) {
      int x = (int) casillaAtacada.getX();
      int y = (int) casillaAtacada.getY();
      int tamanio = casillaAtacada.getNave().getTamanio();
      ArrayList<Casilla> casillas = new ArrayList<>();
      for (int i = y; i > y - tamanio; i--) {
         if (posicionValida(x, i)) {
            Casilla casilla = getCasillaJugador(x, i, jugador);
            if (casilla.getNave() != null && casillas.size() < tamanio) {
               casillas.add(casilla);
            } else {
               break;
            }
         }
      }
      if (casillas.size() < tamanio) {
         for (int i = y; i < y + tamanio; i++) {
            if (posicionValida(x, i)) {
               Casilla casilla = getCasillaJugador(x, i, jugador);
               if (casilla.getNave() != null && casillas.size() <= tamanio) {
                  casillas.add(casilla);
               } else {
                  break;
               }
            }
         }
      }
      return casillas;
   }

   /**
    * Método para obtener las casillas colindantes
    *
    * @param x posición de x de la casilla final
    * @param y posición de y de la casilla final
    * @param jugador boolean para determinar el tablero
    * @return arreglo de casillas
    */
   public Casilla[] getColindantes(int x, int y, boolean jugador) {
      Point2D[] points = new Point2D[]{
         new Point2D(x - 1, y),
         new Point2D(x + 1, y),
         new Point2D(x + 1, y + 1),
         new Point2D(x - 1, y + 1),
         new Point2D(x, y - 1),
         new Point2D(x, y + 1),
         new Point2D(x + 1, y - 1),
         new Point2D(x - 1, y - 1),};
      List<Casilla> colindantes = new ArrayList<>();
      for (Point2D p : points) {
         if (posicionValida((int) p.getX(), (int) p.getY())) {
            colindantes.add(getCasillaJugador((int) p.getX(), (int) p.getY(), jugador));
         }
      }
      return colindantes.toArray(new Casilla[colindantes.size()]);
   }

   /**
    * Método para verificar si la posición seleccionada es válida
    *
    * @param x Posición de x de la casilla
    * @param y Posición de y de la casilla
    * @return Si es posible
    */
   public boolean posicionValida(int x, int y) {
      return x >= 0 && x < 10 && y >= 0 && y < 10;
   }

   /**
    * Método para actualizar el estado del tablero del jugador
    *
    * @param misil Misil con posicion de la casilla
    */
   public void actualizarMiTablero(Misil misil) {
      Casilla casilla = getCasillaJugador(misil.getxDestino(), misil.getyDestino(), true);
      if (casilla.atacadaANave()) {
         if (!casilla.getNave().isViva()) {
            liberarcolindantes(casilla, true);
         }
      }
   }

   /**
    * Método para configurar el turno del jugador
    *
    * @param esTurno boolean
    */
   public void ajustarMiTurno(boolean esTurno) {
      if (esTurno) {
         paneTableroEnemigo.disableProperty().set(false);
         labelTurno.setVisible(true);
         Utileria.fadeConteo(labelTurno);
         iniciarConteo();
      } else {
         paneTableroEnemigo.disableProperty().set(true);
      }
   }

   /**
    * Método para liberar las casillas del tablero enemigo que rodean a una nave que ha sido
    * completamente destruida
    *
    * @param tableroActual tablero con arreglo de casillas actualizadas
    */
   public void liberarCasillasEnemigo(TableroSimple tableroActual) {
      Casilla casilla;
      for (CasillaSimple casillasSimple : tableroActual.getCasillasSimples()) {
         casilla = getCasillaJugador(casillasSimple.getX(), casillasSimple.getY(), false);
         casilla.liberar();
      }
      reducirNaves(false);
   }

   /**
    * Método para envíar las casillas que necesitan ser actualizadas en el tablero del otro jugador
    *
    * @param casillasSimples arreglo de casillas que no heredan de rectángulo
    */
   public void actualizarTableroEnemigo(ArrayList<CasillaSimple> casillasSimples) {
      TableroSimple tableroActualizado = new TableroSimple(true);
      tableroActualizado.setCasillasSimples(casillasSimples);
      interaccionServidor.enviarCasillasALiberar(cuentaLogueada.getNombreUsuario(), tableroActualizado);
   }

   /**
    * Método para actualizar la puntuación obtenida en el tablero del adversario remoto
    *
    * @param tirosAcertados número de tiros para calcular el puntaje
    */
   public void actualizarMiPuntuacion(int tirosAcertados) {
      int puntos = tirosAcertados * 50;
      labelMiPuntuacion.setText(Integer.toString(puntos));
      interaccionServidor.enviarPuntuacion(cuentaLogueada.getNombreUsuario(), puntos);
   }

   /**
    * Método para actualizar la puntuación local del tablero del enemigo
    *
    * @param puntos puntuación que ha obtenido el enemigo
    */
   public void actualizarPuntuacionEnemigo(int puntos) {
      labelPuntuacionEnemigo.setText(Integer.toString(puntos));
   }

   /**
    * Método para asignar el nombre del adversario en la etiqueta del tablero
    *
    * @param nombre
    */
   public void setNombreAdversario(String nombre) {
      labelNombreAdversario.setText(nombre);
   }

   /**
    * Método para obtener la puntuación final de la partida
    *
    * @return valor de puntuación final
    */
   public int ajustarPuntuacion() {
      int puntuacion;
      puntuacion = (numeroTirosAcertados * 50) - (numeroTirosFallidos * 10);
      if (puntuacion > 0) {
         return puntuacion;
      }
      return 0;
   }

   /**
    * Método para reducir las naves cuando se ha destruido completamente una. También lleva el
    * control para finalizar la partida en caso que uno jugador haya destruido todas las naves del
    * adversario.
    *
    * @param jugador booleano para determinar a quién se deberá reducir naves
    */
   public void reducirNaves(boolean jugador) {
      if (jugador) {
         navesJugador--;
         if (navesJugador == 0) {
            timeLine.stop();
            Utileria.cargarAviso("titleAlerta", "mensajeDerrota");
            regresarAMenu(ultimoEvent);
         }
      } else {
         navesEnemigo--;
         if (navesEnemigo == 0) {
            timeLine.stop();
            int puntuacionFinal = ajustarPuntuacion();
            Utileria.cargarAviso("titleAlerta", "mensajeVictoria", Integer.toString(puntuacionFinal));
            interaccionServidor.dejarAdversario(cuentaLogueada.getNombreUsuario(),
                labelNombreAdversario.getText());
            comprobarPuntaje(puntuacionFinal);
            regresarAMenu(ultimoEvent);
         }
      }
   }

   /**
    * Método para activar todos los servicios en espera del servidor
    */
   public void activarServicios() {
      interaccionServidor.activarServiciosJugarPartida(controller);
   }

   /**
    * Método para enviar un Misil con coordenadas al tablero del jugador remoto
    *
    * @param misil
    */
   public void enviarMisil(Misil misil) {
      interaccionServidor.enviarMisil(cuentaLogueada.getNombreUsuario(), misil, controller);
   }

   /**
    * Método para notificar al adversario que es su turno
    */
   public void cederTurno() {
      timeLine.playFrom(Duration.ZERO);
      timeLine.stop();
      interaccionServidor.cederTurno(cuentaLogueada.getNombreUsuario());
   }

   /**
    * Método para notificar al adversario que se ha rendido el jugador
    */
   public void notificarRendicion() {
      interaccionServidor.enviarRendicion(cuentaLogueada.getNombreUsuario());
   }

   /**
    * Método para finalizar la partida en caso que el jugador adversario se haya rendido
    */
   public void enemigoRendido() {
      timeLine.stop();
      Utileria.cargarAviso("titleAlerta", "mensajeVictoria");
      Utileria.cargarAviso("titleAlerta", "mensajeRendicion");
      interaccionServidor.dejarAdversario(cuentaLogueada.getNombreUsuario(), labelNombreAdversario.getText());
      regresarAMenu(ultimoEvent);
   }

   /**
    * Método para comprobar si la puntuación final es mayor a la actual registrada en la cuenta del
    * jugador
    *
    * @param puntajeFinal
    */
   public void comprobarPuntaje(int puntajeFinal) {
      if (cuentaLogueada.getPuntaje() < puntajeFinal) {
         boolean check;
         Utileria.cargarAviso("titleAlerta", "mensajeNuevoPuntaje");
         AdministracionCuenta adminCuenta = new AdministracionCuenta();
         check = adminCuenta.registrarPuntajeMasAlto(cuentaLogueada, puntajeFinal);
         if (check == false) {
            Utileria.cargarAviso("titleAlerta", "mensajeErrorConexion");
         }
      }
   }

   /**
    * Método para regresar al menú de la partida en caso de finalizar la partida
    *
    * @param event
    */
   public void regresarAMenu(Event event) {
      Node node = (Node) event.getSource();
      Stage stage = (Stage) node.getScene().getWindow();
      try {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI_MenuPartida.fxml"));
         Scene scene = new Scene(loader.load());
         GUI_MenuPartidaController controller = loader.getController();
         controller.cargarCuenta(cuentaLogueada);
         loader.setController(controller);
         stage.setScene(scene);
         stage.setResizable(false);
         stage.show();
      } catch (IOException ex) {
         Logger.getLogger(GUI_JugarPartidaController.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   /**
    * Método para establer el conteo de 30 segundos del turno
    */
   public void iniciarConteo() {
      final Integer tiempo = 30;
      IntegerProperty centecimas = new SimpleIntegerProperty(tiempo * 100);
      labelConteo.textProperty().bind(centecimas.divide(100).asString());
      pbConteo.progressProperty().bind(centecimas.divide(tiempo * 100.0));
      centecimas.set((tiempo) * 100);
      timeLine.getKeyFrames().add(
          new KeyFrame(Duration.seconds(tiempo), new KeyValue(centecimas, 0))
      );
      timeLine.playFromStart();
      Utileria.fadeConteo(labelTiempoRestante);
      timeLine.setOnFinished(event -> {
         cederTurno();
         ajustarMiTurno(false);
      });

   }

   /**
    * Método para cargar el recurso de sonido de destrucción de una parte de la nave
    */
   public void cargarSonidoDestruccion() {
      final URL resourceSonido;
      resourceSonido = this.getClass().getResource("/navalBattle/recursos/sonidos/"
          + "Tommccann_explosion.wav");
      Media sound = new Media((resourceSonido).toString());
      MediaPlayer mediaP = new MediaPlayer(sound);
      mediaP.setVolume(.5);
      mediaP.play();
   }

   /**
    * Método para cargar el recurso de sonido de agua, en caso no de existir nave
    */
   public void cargarSonidoAgua() {
      final URL resourceSonido = this.getClass().getResource("/navalBattle/recursos/sonidos/"
          + "Bird-man_big-splash.wav");
      Media sound = new Media((resourceSonido).toString());
      MediaPlayer mediaP = new MediaPlayer(sound);
      mediaP.setVolume(1);
      mediaP.play();
   }

   /**
    * Método para cargar el idioma seleccionado por default en etiquetas y botones
    */
   public void cargarIdioma() {
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle(RECURSO_IDIOMA, locale);
      labelTiempoRestante.setText(resources.getString("labelTiempoRestante"));
      labelPuntuacionHost.setText(resources.getString("labelPuntuacionHost"));
      labelPuntuacionAdversario.setText(resources.getString("labelPuntuacionAdversario"));
      labelTurno.setText(resources.getString("labelTurno"));
      buttonRendirse.setText(resources.getString("buttonRendirse"));
      labelTurno.setVisible(false);
   }

}
