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
import javafx.scene.text.Font;
/**
 * FXML Controller class
 *
 * @author Mari
 */
public class GUI_JugarPartidaController implements Initializable {

   @FXML
   private Label labelCronometro;
   @FXML
   private Label labelTiempoRestante;
   @FXML
   private Label labelPuntuacionHost;
   @FXML
   private JFXButton buttonRendirse;
   @FXML
   private Label labelPuntuacionAdversario;
  @FXML
  private JFXButton button00;
  @FXML
  private JFXButton button01;
  @FXML
  private JFXButton button03;
  @FXML
  private JFXButton button02;
  @FXML
  private JFXButton button04;
  @FXML
  private JFXButton button06;
  @FXML
  private JFXButton button07;
  @FXML
  private JFXButton button08;
  @FXML
  private JFXButton button09;
  @FXML
  private JFXButton button10;
  @FXML
  private JFXButton button11;
  @FXML
  private JFXButton button12;
  @FXML
  private JFXButton button13;
  @FXML
  private JFXButton button14;
  @FXML
  private JFXButton button15;
  @FXML
  private JFXButton button16;
  @FXML
  private JFXButton button17;
  @FXML
  private JFXButton button18;
  @FXML
  private JFXButton button19;
  @FXML
  private JFXButton button20;
  @FXML
  private JFXButton button21;
  @FXML
  private JFXButton button22;
  @FXML
  private JFXButton button23;
  @FXML
  private JFXButton button24;
  @FXML
  private JFXButton button25;
  @FXML
  private JFXButton button27;
  @FXML
  private JFXButton button28;
  @FXML
  private JFXButton button29;
  @FXML
  private JFXButton button30;
  @FXML
  private JFXButton button31;
  @FXML
  private JFXButton button32;
  @FXML
  private JFXButton button33;
  @FXML
  private JFXButton button34;
  @FXML
  private JFXButton button35;
  @FXML
  private JFXButton button36;
  @FXML
  private JFXButton button37;
  @FXML
  private JFXButton button38;
  @FXML
  private JFXButton button39;
  @FXML
  private JFXButton button40;
  @FXML
  private JFXButton button41;
  @FXML
  private JFXButton button42;
  @FXML
  private JFXButton button43;
  @FXML
  private JFXButton button44;
  @FXML
  private JFXButton button45;
  @FXML
  private JFXButton button46;
  @FXML
  private JFXButton button47;
  @FXML
  private JFXButton button48;
  @FXML
  private JFXButton button49;
  @FXML
  private JFXButton button50;
  @FXML
  private JFXButton button51;
  @FXML
  private JFXButton button52;
  @FXML
  private JFXButton button53;
  @FXML
  private JFXButton button54;
  @FXML
  private JFXButton button55;
  @FXML
  private JFXButton button56;
  @FXML
  private JFXButton button57;
  @FXML
  private JFXButton button58;
  @FXML
  private JFXButton button59;
  @FXML
  private JFXButton button60;
  @FXML
  private JFXButton button61;
  @FXML
  private JFXButton button62;
  @FXML
  private JFXButton button63;
  @FXML
  private JFXButton button64;
  @FXML
  private JFXButton button65;
  @FXML
  private JFXButton button66;
  @FXML
  private JFXButton button67;
  @FXML
  private JFXButton button68;
  @FXML
  private JFXButton button69;
  @FXML
  private JFXButton button70;
  @FXML
  private JFXButton button71;
  @FXML
  private JFXButton button72;
  @FXML
  private JFXButton button73;
  @FXML
  private JFXButton button80;
  @FXML
  private JFXButton button90;
  @FXML
  private JFXButton button91;
  @FXML
  private JFXButton button99;
  @FXML
  private JFXButton button98;
  @FXML
  private JFXButton button97;
  @FXML
  private JFXButton button96;
  @FXML
  private JFXButton button95;
  @FXML
  private JFXButton button92;
  @FXML
  private JFXButton button93;
  @FXML
  private JFXButton button94;
  @FXML
  private JFXButton button81;
  @FXML
  private JFXButton button82;
  @FXML
  private JFXButton button83;
  @FXML
  private JFXButton button84;
  @FXML
  private JFXButton button85;
  @FXML
  private JFXButton button79;
  @FXML
  private JFXButton button78;
  @FXML
  private JFXButton button77;
  @FXML
  private JFXButton button76;
  @FXML
  private JFXButton button75;
  @FXML
  private JFXButton button74;
  @FXML
  private JFXButton button86;
  @FXML
  private JFXButton button87;
  @FXML
  private JFXButton button88;
  @FXML
  private JFXButton button89;
  @FXML
  private Font x1;
  @FXML
  private JFXButton button021;
  
  
  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
      cargarIdioma();
      buttonRendirse.setOnAction( event -> {
         
      });
  }  
  
  public void cargarIdioma( ){
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle("NavalBattle.recursos.idioma",locale);
      labelCronometro.setText(resources.getString("labelCronometro"));
      labelTiempoRestante.setText(resources.getString("labelTiempoRestante"));
      labelPuntuacionHost.setText(resources.getString("labelPuntuacionHost"));
      labelPuntuacionAdversario.setText(resources.getString("labelPuntuacionAdversario"));
      buttonRendirse.setText(resources.getString("buttonRendirse"));
   }
}
