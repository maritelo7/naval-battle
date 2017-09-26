/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.gui;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

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
   private JFXButton buttonIniciarPartida;
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
      Locale locale = Locale.getDefault();
      cargarIdioma(locale);
      labelIniciando.setVisible(false);
      buttonIniciarPartida.setOnAction( event -> {
         labelIniciando.setVisible(true);
      });
    
    }    
    public void cargarIdioma(Locale localeSelect){
      
      ResourceBundle resources = ResourceBundle.getBundle("navalBattle.recursos.Idioma", localeSelect);
      
      buttonReglas.setText(resources.getString("buttonReglas"));
      buttonPuntuacion.setText(resources.getString("buttonPuntuacion"));
      buttonIniciarPartida.setText(resources.getString("buttonIniciarPartida"));
      labelSalir.setText(resources.getString("labelSalir"));
      labelConfigurar.setText(resources.getString("labelConfigurar"));
      labelIniciando.setText(resources.getString("labelIniciando"));
   }
}
