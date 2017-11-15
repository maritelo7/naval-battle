/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.presentacion;

import com.jfoenix.controls.JFXButton;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import navalBattle.logica.Casilla;
import navalBattle.logica.Misil;
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

   /**
    * Initializes the controller class.
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
      cargarIdioma();

      buttonRendirse.setOnAction(event -> {
         ajustarMiTurno(true);
      });
      paneTableroEnemigo.setOnMouseClicked(event -> {

         Casilla casilla = (Casilla) event.getTarget();
         //Enviar objeto misil para actualizar el tablero del otro jugador
         Misil misil = new Misil((int) casilla.getX(), (int) casilla.getY());

         if (casilla.getNave() == null) {
            casilla.setFill(Color.BLACK);
            cargarSonidoAgua();
            ajustarMiTurno(false);
         } else {
            System.out.println("Tamaño " + casilla.getNave().getTamanio());
            cargarSonidoDestruccion();
            casilla.setFill(Color.RED);
            ajustarMiTurno(true);
         }
      });
   }

   /**
    * Método para cargar el idioma en etiquetas y botones seleccionado por default
    */
   public void cargarIdioma() {
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle("navalBattle.recursos.idiomas.Idioma", locale);
      labelTiempoRestante.setText(resources.getString("labelTiempoRestante"));
      labelPuntuacionHost.setText(resources.getString("labelPuntuacionHost"));
      labelPuntuacionAdversario.setText(resources.getString("labelPuntuacionAdversario"));
      buttonRendirse.setText(resources.getString("buttonRendirse"));
   }

   public void cargarTableroJugador() {
      int z = 0;

      ArrayList<Casilla> casillas = new ArrayList<>();
      casillas = tableroJugador.getCasillas();
      for (int i = 0; i < 10; i++) {
         HBox filas = new HBox();
         for (int j = 0; j < 10; j++) {
            Casilla casilla = new Casilla(j, i);
            casilla.setNave(casillas.get(z).getNave());
            casilla.setX(j);
            casilla.setY(i);
            filas.getChildren().add(casilla);
            if (casilla.getNave() != null) {
               casilla.setFill(Color.ORANGE);
            }
            z++;
         }
         columnasJugador.getChildren().add(filas);
      }
      paneTableroJugador.getChildren().add(columnasJugador);
   }

   public void cargarTableroEnemigo() {
      int z = 0;
      ArrayList<Casilla> casillas = new ArrayList<>();
      casillas = tableroEnemigo.getCasillas();
      for (int i = 0; i < 10; i++) {
         HBox filas = new HBox();
         for (int j = 0; j < 10; j++) {
            Casilla casilla = new Casilla(j, i);
            casilla.setNave(casillas.get(z).getNave());
            casilla.setX(j);
            casilla.setY(i);
            filas.getChildren().add(casilla);
            z++;
         }
         columnasEnemigo.getChildren().add(filas);
      }
      paneTableroEnemigo.getChildren().add(columnasEnemigo);
   }

   public void setTableroJugador(Tablero tableroJugador) {
      this.tableroJugador = tableroJugador;
      cargarTableroJugador();

   }

   public void setTableroEnemigo(Tablero tableroEnemigo) {
      this.tableroEnemigo = tableroEnemigo;
      tableroEnemigo.setEnemigo(true);
      cargarTableroEnemigo();
   }

   public void cargarSonidoDestruccion() {
      String separator = System.getProperty("file.separator");
      final String resourceSonido = this.getClass().getResource(separator + "navalBattle" + separator
          + "recursos" + separator + "sonidos" + separator + "Tommccann_explosion.wav").toExternalForm();
      Media sound = new Media(new File(resourceSonido).toString());
      MediaPlayer mediaP = new MediaPlayer(sound);
      mediaP.setVolume(.5);
      mediaP.play();
   }

   public void cargarSonidoAgua() {
      String separator = System.getProperty("file.separator");
      final String resourceSonido = this.getClass().getResource(separator + "navalBattle" + separator
          + "recursos" + separator + "sonidos" + separator + "Bird-man_big-splash.wav").toExternalForm();
      Media sound = new Media(new File(resourceSonido).toString());
      MediaPlayer mediaP = new MediaPlayer(sound);
      mediaP.setVolume(1);
      mediaP.play();
   }

   public void ajustarMiTurno(boolean esTurno) {
      if (esTurno) {
         System.out.println("ABLE");
         paneTableroEnemigo.disableProperty().set(false);
         iniciarConteo();
      } else {
         System.out.println("DISABLE");
         paneTableroEnemigo.disableProperty().set(true);
      }
   }

   public void actualizarTableroJugador(int x, int y) {
      //También podría ser por medio de Casilla
   }

   public void iniciarConteo() {
      final Integer tiempo = 30;
      IntegerProperty centecimas = new SimpleIntegerProperty(tiempo * 100);
      Timeline timeline = null;
      labelConteo.textProperty().bind(centecimas.divide(100).asString());
      pbConteo.progressProperty().bind(centecimas.divide(tiempo * 100.0));
      centecimas.set((tiempo + 1) * 100);
      timeline = new Timeline();
      timeline.getKeyFrames().add(
          new KeyFrame(Duration.seconds(tiempo), new KeyValue(centecimas, 0))
      );
      timeline.playFromStart();
      fadeConteo();
   }

   public void fadeConteo() {
      FadeTransition ft = new FadeTransition(Duration.millis(3000), labelTiempoRestante);
      ft.setFromValue(1.0);
      ft.setToValue(0.1);
      ft.setCycleCount(Timeline.INDEFINITE);
      ft.setAutoReverse(true);
      ft.play();
   }

}
