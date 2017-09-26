/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.gui;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author Mari
 */
public class GUI_JugarPartidaController implements Initializable {

  @FXML
  private Font x1;
  @FXML
  private Label labelColocaNaves;
  @FXML
  private Label labelColocaNaves2;
  @FXML
  private Label labelRotar;
  @FXML
  private Button buttonContinuar;
  

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // TODO
   cargarIdioma();
  }  
   
  public void cargarIdioma(){
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle("navalBattle.recursos.Idioma",locale);
      labelColocaNaves.setText(resources.getString("labelColocaNaves"));
      labelColocaNaves2.setText(resources.getString("labelColocaNaves2"));
      labelRotar.setText(resources.getString("labelRotar"));
      buttonContinuar.setText(resources.getString("buttonContinuar"));
   }
  
}
