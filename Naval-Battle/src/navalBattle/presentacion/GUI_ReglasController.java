/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.presentacion;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author javr
 */
public class GUI_ReglasController implements Initializable {
   @FXML 
   private JFXButton buttonRegreso;
  @FXML
  private Label labelReglaUno;
  @FXML
  private Label labelReglasDelJuego;
  @FXML
  private Label labelReglaDos;
  
  @FXML
  private Label labelReglaCuatro;
  @FXML
  private Label labelReglaTres;
  @FXML
  private Label labelReglaCinco;

 
   /**
    * Initializes the controller class.
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
        cargarIdioma();      
   }   
   
   public void cargarIdioma(){
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle("navalBattle.recursos.idiomas.Idioma", locale);
      labelReglaUno.setText(resources.getString("labelReglaUno"));
      labelReglaDos.setText(resources.getString("labelReglaDos"));
      labelReglaTres.setText(resources.getString("labelReglaTres"));
      labelReglaCuatro.setText(resources.getString("labelReglaCuatro"));
      labelReglaCinco.setText(resources.getString("labelReglaCinco"));
      labelReglasDelJuego.setText(resources.getString("labelReglasDelJuego"));     
   }  
   
}
