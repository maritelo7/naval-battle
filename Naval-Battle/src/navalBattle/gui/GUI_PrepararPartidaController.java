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
public class GUI_PrepararPartidaController implements Initializable {

  @FXML
  private Font x1;
  @FXML
  private Label labelCronometro;
  @FXML
  private Label labelTiempoRestante;
  @FXML
  private Label labelPuntuacionHost;
  @FXML
  private Label labelPuntuacionAdversario;
  @FXML
  private Button buttonRendirse;
  
  
  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // TODO
  }  
  
  public void cargarIdioma(){
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle("navalBattle.recursos.Idioma",locale);
      labelCronometro.setText(resources.getString("labelCronometro"));
      labelTiempoRestante.setText(resources.getString("labelTiempoRestante"));
      labelPuntuacionHost.setText(resources.getString("labelPuntuacionHost"));
      labelPuntuacionHost.setText(resources.getString("labelPuntuacionAdversario"));
      buttonRendirse.setText(resources.getString("buttonRendirse"));
   }
}
