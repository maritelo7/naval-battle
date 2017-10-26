/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.presentacion;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import javafx.scene.text.Font;
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

/**
 * FXML Controller class
 *
 * @author Mari
 */
public class GUI_PrepararPartidaController implements Initializable {


  @FXML
  private Label labelColocaNaves;
  @FXML
  private Label labelRotar;
  @FXML
  private JFXButton buttonContinuar;
  @FXML
  private Font x1; //No tengo idea qué hace :l
  @FXML
  private JFXButton buttonRegresar;
  

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {

   cargarIdioma();
   buttonContinuar.setOnAction( event -> {   
      try {
         Node node = (Node) event.getSource();
         Stage stage = (Stage) node.getScene().getWindow();
         Parent root;
         root = FXMLLoader.load(getClass().getResource("GUI_JugarPartida.fxml"));
         Scene scene = new Scene(root);
         stage.setScene(scene);
         stage.setResizable(false);
         stage.show();
      } catch (IOException ex) {
         Logger.getLogger(GUI_PrepararPartidaController.class.getName()).log(Level.SEVERE, null, ex);
      }
   });
   buttonRegresar.setOnAction( event -> {
      //Aquí se debe cerrar la conexión y avisar al otro jugador
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
         Logger.getLogger(GUI_PrepararPartidaController.class.getName()).log(Level.SEVERE, null, ex);
      }
   });
   
  }  
   
  public void cargarIdioma(){
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle("navalBattle.recursos.idioma.Idioma", locale);
      labelColocaNaves.setText(resources.getString("labelColocaNaves"));
      labelRotar.setText(resources.getString("labelRotar"));
      buttonContinuar.setText(resources.getString("buttonContinuar"));
   }
  
}
