/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.recursos;

import java.security.MessageDigest;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javax.xml.bind.DatatypeConverter;

/**
 * Clase con métodos o atributos de utilidad para diferentes clases y usos
 * @author Maribel Tello Rodriguez
 * @author José Alí Valdivia Ruiz
 */
public class Utileria {

   /**
    * Método para crear un SHA256 hash a partir de una cadena String
    *
    * @param data información a crear hash
    * @return String del hash
    */
   public String getSHA256Hash(String data) {

      String result = null;

      try {

         MessageDigest digest = MessageDigest.getInstance("SHA-256");
         byte[] hash = digest.digest(data.getBytes("UTF-8"));
         return bytesToHex(hash); // make it printable
      } catch (Exception ex) {
         ex.printStackTrace();
      }
      return result;
   }

   private String bytesToHex(byte[] hash) {
      return DatatypeConverter.printHexBinary(hash);

   }

   /**
    * Método génerico para crear un aviso
    *
    * @param title título del aviso
    * @param mensaje mensaje del aviso
    */
   public void cargarAviso(String title, String mensaje) {
      Alert confirmacion = new Alert(Alert.AlertType.INFORMATION);
      confirmacion.setTitle(title);
      confirmacion.setHeaderText(null);
      confirmacion.setContentText(mensaje);
      ButtonType btAceptar = new ButtonType("Aceptar", ButtonBar.ButtonData.CANCEL_CLOSE);
      confirmacion.getButtonTypes().setAll(btAceptar);
      Optional<ButtonType> eleccion = confirmacion.showAndWait();
   }

}
