/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.gui;

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
import javafx.scene.control.Label;
import javafx.stage.Stage;
import navalBattle.Util.Utileria;

/**
 * FXML Controller class
 *
 * @author javr
 */
public class GUI_MenuPartidaController implements Initializable {
   @FXML
   private JFXButton buttonReglas;
   @FXML
   private JFXButton buttonPuntuacion;
   @FXML
   private JFXButton buttonSalir;
   @FXML
   private JFXButton buttonCrearPartida;
   @FXML
   private JFXButton buttonUnirmePartida;
   @FXML
   private JFXButton buttonConfigurar;
   @FXML
   private Label labelSalir;
   @FXML
   private Label labelConfigurar;
   @FXML
   private Label labelIniciando;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

      cargarIdioma();
      labelIniciando.setVisible(false);
      buttonCrearPartida.setOnAction( event -> {
         labelIniciando.setVisible(true);
         //ESTABLECER CONEXIÓN Y SER SERVER
      });
      buttonUnirmePartida.setOnAction( event -> {
         labelIniciando.setVisible(true);
         //ESTABLECER CONEXIÓN Y SER CLIENTE
      });
      buttonConfigurar.setOnAction( event ->  {            
         Node node = (Node) event.getSource();
         Stage stage = (Stage) node.getScene().getWindow();
         Parent root;

         try {
            //Pasar objeto Usuario
            root = FXMLLoader.load(getClass().getResource("GUI_Registrar.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);   
            stage.show();
         } catch (IOException ex) {
            Logger.getLogger(GUI_MenuPartidaController.class.getName()).log(Level.SEVERE, null, ex);
         }
      });
      buttonSalir.setOnAction( event -> {
         Node node = (Node) event.getSource();
         Stage stage = (Stage) node.getScene().getWindow();
         Parent root;
         try {  
            root = FXMLLoader.load(getClass().getResource("GUI_IniciarSesion.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);   
            stage.show();
         } catch (IOException ex) {
            Logger.getLogger(GUI_MenuPartidaController.class.getName()).log(Level.SEVERE, null, ex);
         }
      });
      buttonReglas.setOnAction( event -> {
        try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Parent root;
            root = FXMLLoader.load(getClass().getResource("GUI_Reglas.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);   
            stage.show();
         } catch (IOException ex) {
            Logger.getLogger(GUI_MenuPartidaController.class.getName()).log(Level.SEVERE, null, ex);
         }         
      });
      buttonPuntuacion.setOnAction( event -> {
        try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Parent root;
            root = FXMLLoader.load(getClass().getResource("GUI_Puntuaciones.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);   
            stage.show();
         } catch (IOException ex) {
            Logger.getLogger(GUI_MenuPartidaController.class.getName()).log(Level.SEVERE, null, ex);
         }          
      });      
    
    }    
    public void cargarIdioma(){
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle("navalBattle.recursos.Idioma", locale);
      buttonReglas.setText(resources.getString("buttonReglas"));
      buttonPuntuacion.setText(resources.getString("buttonPuntuacion"));
      buttonCrearPartida.setText(resources.getString("buttonCrearPartida"));
      buttonUnirmePartida.setText(resources.getString("buttonUnirmePartida"));
      labelSalir.setText(resources.getString("labelSalir"));
      labelConfigurar.setText(resources.getString("labelConfigurar"));
      labelIniciando.setText(resources.getString("labelIniciando"));
   }
}
