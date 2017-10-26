/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.presentacion;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author javr
 */
public class GUI_RegistrarController implements Initializable {
   @FXML
   private Label labelNick;
   @FXML
   private Label labelClave;
   @FXML
   private Label labelConfirClave;
   @FXML
   private Label labelIdioma;
   @FXML
   private JFXButton buttonGuardar;
   @FXML
   private JFXButton buttonBaja;
   @FXML
   private JFXButton buttonRegreso;
   private boolean flag;
  
    /**
     * Initializes the controller class.
     */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
 
      cargarIdioma();
      buttonGuardar.setOnAction( event -> {
         //INCOMPLETO EL ALERT
         Alert aviso = new Alert(Alert.AlertType.INFORMATION);
         aviso.setContentText("AVISO DE GUARDADO");

      });
      buttonBaja.setOnAction( event -> {
         
      });
      buttonRegreso.setOnAction( event -> {
         if (flag) {
            //Regresar objeto o mantener el objeto en la ventana anterior
            try {
               Node node = (Node) event.getSource();
               Stage stage = (Stage) node.getScene().getWindow();
               Parent root;
               root = FXMLLoader.load(getClass().getResource("GUI_MenuPartida.fxml"));
               Scene scene = new Scene(root);
               stage.setScene(scene);
               stage.setResizable(false);   
               stage.show();
            } catch (IOException ex) {
               Logger.getLogger(GUI_RegistrarController.class.getName()).log(Level.SEVERE, null, ex);
            } 
         } else {
            try {
               Node node = (Node) event.getSource();
               Stage stage = (Stage) node.getScene().getWindow();
               Parent root;
               root = FXMLLoader.load(getClass().getResource("GUI_IniciarSesion.fxml"));
               Scene scene = new Scene(root);
               stage.setScene(scene);
               stage.setResizable(false);   
               stage.show();
            } catch (IOException ex) {
               Logger.getLogger(GUI_RegistrarController.class.getName()).log(Level.SEVERE, null, ex);
            } 
         }
      });
   }
   public void cargarDatos(){
      //Aquí debería cargar los datos del objeto Jugador y cambiar la bandera para poder hacer el regreso efectivoivo
   }   
   public void cargarIdioma(){
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle("navalBattle.recursos.idiomas.Idioma", locale);
      buttonGuardar.setText(resources.getString("buttonGuardar"));
      buttonBaja.setText(resources.getString("buttonBaja"));
      labelNick.setText(resources.getString("labelNick"));
      labelConfirClave.setText(resources.getString("labelConfirClave"));
      labelIdioma.setText(resources.getString("labelIdioma"));
      labelClave.setText(resources.getString("labelClave"));
   }
}
