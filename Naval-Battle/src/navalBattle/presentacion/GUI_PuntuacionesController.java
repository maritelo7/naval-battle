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
import javafx.scene.control.Label;
import javafx.stage.Stage;
import navalBattle.logica.CuentaUsuario;

/**
 * FXML Controller class
 *
 * @author Maribel Tello Rodriguez
 * @author José Alí Valdivia Ruiz
 */
public class GUI_PuntuacionesController implements Initializable {
   @FXML 
   private JFXButton buttonRegresar;
   @FXML
   private Label labelPuntuaciones;
   
   CuentaUsuario cuentaLogueada = null;
   boolean sesionIniciada = false;
   /**
    * Initializes the controller class.
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
      cargarIdioma();  
      buttonRegresar.setOnAction( event -> {   
         Node node = (Node) event.getSource();
         Stage stage = (Stage) node.getScene().getWindow();
         try {
            Scene scene;
            if (sesionIniciada) {
               FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI_MenuPartida.fxml"));
               GUI_MenuPartidaController controller = new GUI_MenuPartidaController(cuentaLogueada);
               loader.setController(controller);
               scene = new Scene(loader.load());
            } else {
               Parent root = FXMLLoader.load(getClass().getResource("GUI_IniciarSesion.fxml"));
               scene = new Scene(root);
            }
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
         } catch (IOException ex) {
            Logger.getLogger(GUI_IniciarSesionController.class.getName()).log(Level.SEVERE, null, ex);
         }
      });
      System.out.println("CUENTA DESDE PUNTUACIONES" + cuentaLogueada.getNombreUsuario()); 
   }      

   public GUI_PuntuacionesController(CuentaUsuario cuenta){
     cuentaLogueada = cuenta;
     sesionIniciada = true;
    }
   
   public GUI_PuntuacionesController(){
    }
   /**
    * Método para carga el idioma seleccionado por default en botones y etiquetas 
    */
   public void cargarIdioma(){
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle("navalBattle.recursos.idiomas.Idioma", locale);
      labelPuntuaciones.setText(resources.getString("labelPuntuaciones"));
   }   
}
