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
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
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
import navalBattle.logica.Casilla;
import navalBattle.logica.CuentaUsuario;
import navalBattle.logica.Misil;
import navalBattle.logica.Nave;
import navalBattle.logica.Tablero;

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
   private JFXButton buttonRendirse;
   @FXML
   private Label labelPuntuacionAdversario;
   @FXML
   private Pane paneTableroJugador;
   @FXML
   private Pane paneTableroEnemigo;
   @FXML
   private ProgressBar pbConteo;
   VBox columnasJugador = new VBox();
   VBox columnasEnemigo = new VBox();
   Tablero tableroJugador;
   Tablero tableroEnemigo;
   CuentaUsuario cuentaLogueada;
   final static String RECURSO_IDIOMA = "navalBattle.recursos.idiomas.Idioma";

   /**
    * Initializes the controller class.
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
      cargarIdioma();

      buttonRendirse.setOnAction(event -> {
         ajustarMiTurno(true);
         cargarAviso("titleDerrota", "mensajeDerrota");
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
      });

      paneTableroEnemigo.setOnMouseClicked(event -> {
         Casilla casilla = (Casilla) event.getTarget();
         //Enviar objeto misil para actualizar el tablero del otro jugador
         Misil misil = new Misil((int) casilla.getX(), (int) casilla.getY());
         if (casilla.atacadaANave()) {
            ajustarMiTurno(true);
            cargarSonidoDestruccion();
            if (!casilla.getNave().isViva()) {
               liberarcolindantes(casilla, false);
            }
         }
         ajustarMiTurno(false);
         cargarSonidoAgua();
      });
   }

   /**
    * Método para cargar objeto cuenta y utilzar sus valores en este controller
    *
    * @param cuenta la CuentaUsuario con la que se ha iniciado sesión
    */
   public void cargarCuenta(CuentaUsuario cuenta) {
      this.cuentaLogueada = cuenta;
   }

   /**
    * Método para cargar el idioma en etiquetas y botones seleccionado por default
    */
   public void cargarIdioma() {
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle(RECURSO_IDIOMA, locale);
      labelTiempoRestante.setText(resources.getString("labelTiempoRestante"));
      labelPuntuacionHost.setText(resources.getString("labelPuntuacionHost"));
      labelPuntuacionAdversario.setText(resources.getString("labelPuntuacionAdversario"));
      buttonRendirse.setText(resources.getString("buttonRendirse"));
   }

   /**
    * Método reutilizable para cargar un ventana emergente
    *
    * @param nombreTitulo nombre del key del título
    * @param nombreMensaje nombre del key del mensaje
    */
   public void cargarAviso(String nombreTitulo, String nombreMensaje) {
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle(RECURSO_IDIOMA, locale);
      String titulo = resources.getString(nombreTitulo);
      String mensaje = resources.getString(nombreMensaje);
      Alert confirmacion = new Alert(Alert.AlertType.INFORMATION);
      confirmacion.setTitle(titulo);
      confirmacion.setHeaderText(null);
      confirmacion.setContentText(mensaje);
      ButtonType btAceptar = new ButtonType("OK", ButtonBar.ButtonData.CANCEL_CLOSE);
      confirmacion.getButtonTypes().setAll(btAceptar);
      confirmacion.showAndWait();
   }

   /**
    * Método para cargar el tablero del jugador
    */
   public void cargarTableroJugador() {
      int contador = 0;
      ArrayList<Casilla> casillas;
      casillas = tableroJugador.getCasillas();
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
      tableroEnemigo.setEnemigo(true);
      cargarTableroEnemigo();
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
    * Método para configurar el turno del jugador
    *
    * @param esTurno boolean
    */
   public void ajustarMiTurno(boolean esTurno) {
      if (esTurno) {
         paneTableroEnemigo.disableProperty().set(false);
         iniciarConteo();
      } else {
         paneTableroEnemigo.disableProperty().set(true);
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
      centecimas.set((tiempo + 1) * 100);
      Timeline timeline = new Timeline();
      timeline.getKeyFrames().add(
          new KeyFrame(Duration.seconds(tiempo), new KeyValue(centecimas, 0))
      );
      timeline.playFromStart();
      fadeConteo();

   }

   /**
    * Método para hacer un efecto de desvanecimiento de la etiqueta del tiempo
    */
   public void fadeConteo() {
      FadeTransition ft = new FadeTransition(Duration.millis(3000), labelTiempoRestante);
      ft.setFromValue(1.0);
      ft.setToValue(0.1);
      ft.setCycleCount(Timeline.INDEFINITE);
      ft.setAutoReverse(true);
      ft.play();
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
      int x = (int) casillaAtacada.getX();
      int y = (int) casillaAtacada.getY();
      Nave nave = casillaAtacada.getNave();
      boolean horizontal = nave.isHorizontal();
      int tamanio = nave.getTamanio();
      if (horizontal) {
         liberarHorizontal(x, y, tamanio, jugador);
      } else {
         liberarVertical(x, y, tamanio, jugador);
      }

   }

   public void liberarHorizontal(int x, int y, int tamanio, boolean jugador) {
      for (int i = x - tamanio; i < x; i++) {
         //Casilla casilla = getCasillaJugador(i, y, jugador);
         for (Casilla colindante : getColindantes(i, y, jugador)) {
            if (!colindante.isAtacado()) {
               colindante.liberar();
            }
         }
      }
   }

   public void liberarVertical(int x, int y, int tamanio, boolean jugador) {
      for (int i = y - tamanio; i < y; i++) {
        // Casilla casilla = getCasillaJugador(x, i, jugador);
         for (Casilla colindante : getColindantes(x, i, jugador)) {
            if (!colindante.isAtacado()) {
               colindante.liberar();
            }
         }
      }
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

         colindantes.add(getCasillaJugador((int) p.getX(), (int) p.getY(), jugador));

      }
      //En duda colindantes.size
      return colindantes.toArray(new Casilla[colindantes.size()]);
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
}
