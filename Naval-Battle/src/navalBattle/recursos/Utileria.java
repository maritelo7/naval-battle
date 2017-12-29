/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.recursos;

import java.util.Locale;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * Clase con métodos o atributos de utilidad para diferentes clases y usos
 *
 * @author Maribel Tello Rodriguez
 * @author José Alí Valdivia Ruiz
 */
public class Utileria {
   boolean bandera;

   public boolean isBandera() {
      return bandera;
   }

   public void setBandera(boolean bandera) {
      this.bandera = bandera;
   }

   public Utileria(boolean bandera) {
      this.bandera = bandera;
   }

   /**
    * Método reutilizable para cargar un ventana emergente
    *
    * @param nombreTitulo nombre del key del título
    * @param nombreMensaje nombre del key del mensaje
    */
   public static void cargarAviso(String nombreTitulo, String nombreMensaje) {
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle("navalBattle.recursos.idiomas.Idioma", locale);
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
    * Método para hacer un efecto de desvanecimiento de la etiqueta del tiempo
    *
    * @param nombreLabel
    */
   public static void fadeConteo(Label nombreLabel) {
      FadeTransition ft = new FadeTransition(Duration.millis(3000), nombreLabel);
      ft.setFromValue(1.0);
      ft.setToValue(0.1);
      ft.setCycleCount(Timeline.INDEFINITE);
      ft.setAutoReverse(true);
      ft.play();
   }

   /**
    * Método sobrecargado en caso de ser necesario desplegar 3 elementos en un aviso
    * @param nombreTitulo
    * @param cabecera
    * @param body
    */
   public static void cargarAviso(String nombreTitulo, String cabecera, String body) {
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle("navalBattle.recursos.idiomas.Idioma", locale);
      String titulo = resources.getString(nombreTitulo);
      String head = resources.getString(cabecera);
      Alert confirmacion = new Alert(Alert.AlertType.INFORMATION);
      confirmacion.setTitle(titulo);
      confirmacion.setHeaderText(head);
      confirmacion.setContentText(body);
      ButtonType btAceptar = new ButtonType("OK", ButtonBar.ButtonData.CANCEL_CLOSE);
      confirmacion.getButtonTypes().setAll(btAceptar);
      confirmacion.showAndWait();
   }
}
