/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.presentacion;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import navalBattle.logica.Casilla;
import navalBattle.logica.Tablero;
/**
 * FXML Controller class
 * @author Maribel Tello Rodriguez
 * @author José Alí Valdivia Ruiz
 */
public class GUI_JugarPartidaController implements Initializable {

   @FXML
   private Label labelCronometro;
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
   
   Tablero tableroJugador;
   Tablero tableroEnemigo;
  
  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
      cargarIdioma();
 

      buttonRendirse.setOnAction( event -> {
         
      });
  }  
  
   /**
    * Método para cargar el idioma en etiquetas y botones seleccionado por default
    */
   public void cargarIdioma( ){
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle("navalBattle.recursos.idiomas.Idioma",locale);
      labelCronometro.setText(resources.getString("labelCronometro"));
      labelTiempoRestante.setText(resources.getString("labelTiempoRestante"));
      labelPuntuacionHost.setText(resources.getString("labelPuntuacionHost"));
      labelPuntuacionAdversario.setText(resources.getString("labelPuntuacionAdversario"));
      buttonRendirse.setText(resources.getString("buttonRendirse"));
   }
   public void cargarTableroJugador(){
      int z = 0;
      VBox columnas = new VBox();
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
               casilla.setFill(Color.AQUAMARINE);
            }
            z++;
         }
         columnas.getChildren().add(filas);
      }
      paneTableroJugador.getChildren().add(columnas);
   }
      public void cargarTableroEnemigo(){
//      int z = 0;
      VBox columnas = new VBox();
//      ArrayList<Casilla> casillas = new ArrayList<>();
//      casillas = tableroJugador.getCasillas();
      for (int i = 0; i < 10; i++) {
         HBox filas = new HBox();
         for (int j = 0; j < 10; j++) {
            Casilla casilla = new Casilla(j, i);
//            casilla.setNave(casillas.get(z).getNave());
            casilla.setX(j);
            casilla.setY(i);
            filas.getChildren().add(casilla);
         }
         columnas.getChildren().add(filas);
      }
      paneTableroEnemigo.getChildren().add(columnas);
   }

   public void setTableroJugador(Tablero tableroJugador) {
      this.tableroJugador = tableroJugador;
      cargarTableroJugador();

   }

   public void setTableroEnemigo(Tablero tableroEnemigo) {
      this.tableroEnemigo = tableroEnemigo;
      cargarTableroEnemigo();
   }

   public void setPaneTableroJugador(Pane paneTableroJugador) {
      this.paneTableroJugador = paneTableroJugador;
   }

   
   
}
