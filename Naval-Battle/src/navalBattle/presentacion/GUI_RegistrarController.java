/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.presentacion;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import navalBattle.logica.AdministracionCuenta;
import navalBattle.logica.CuentaUsuario;

/**
 * FXML Controller class
 *
 * @author Maribel Tello Rodriguez
 * @author José Alí Valdivia Ruiz
 */
public class GUI_RegistrarController implements Initializable {
CuentaUsuario cuentaLogueada = null;
boolean tieneCuenta = false;
   
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
   @FXML
   private TextField tFieldNick;
   @FXML
   private PasswordField pFieldClave;
   @FXML
   private PasswordField pFieldcConfirmacionClave;
   @FXML
   private ComboBox<?> comboIdioma;

   /**
    * Initializes the controller class.
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
      cargarIdioma();
   }
   
   public void setCuentaUsuario(CuentaUsuario cuenta){
      cuentaLogueada = cuenta;
      tieneCuenta = true;
   }

   public void accionButtonRegistrar() {
      buttonGuardar.setOnAction(event -> {
         if (obtenerYValidarCamposCuenta()) {           
            String nickname = tFieldNick.getText();
            String clave = pFieldClave.getText();
            String confirmacionClave = pFieldcConfirmacionClave.getText();
            if(clave.equals(confirmacionClave)){
            AdministracionCuenta adminCuenta = null;
            if (tieneCuenta) {              
               try {
                  System.out.println("HI");
                  CuentaUsuario nuevaCuenta = new CuentaUsuario(nickname, clave);
                  adminCuenta.registrarCuenta(nuevaCuenta);
                  cuentaLogueada = adminCuenta.consultarCuenta(nickname, clave);
                  //INCOMPLETO EL ALERT
               } catch (NoSuchAlgorithmException ex) {
                  Logger.getLogger(GUI_RegistrarController.class.getName()).log(Level.SEVERE, null, ex);
               }
            } else {
               try {
                  adminCuenta.modificarCuenta(cuentaLogueada);
                  cuentaLogueada = adminCuenta.consultarCuenta(nickname, clave);
                  //INCOMPLETO EL ALERT
               } catch (NoSuchAlgorithmException ex) {
                  Logger.getLogger(GUI_RegistrarController.class.getName()).log(Level.SEVERE, null, ex);
               }
            }
            
         }else{
               //MENSAJE DE QUE DE CONFIRMACIÓN DE LA CLAVE NO ES LA MISMA
            }
         }else{
            //MENSAJE DE QUE NO TODOS LOS CAMPOS ESTÁN LLENOS
         }
         Alert aviso = new Alert(Alert.AlertType.INFORMATION);
         aviso.setContentText("AVISO DE GUARDADO");
      });
   }

   
   @FXML
   public void limitarCaracteresNick(KeyEvent e){
      String s = e.getCharacter();
      char c = s.charAt(0);
      if ((c > 'z' || c < 'a') && (c > '9' || c < '0')){
         e.consume();
      }
      if (tFieldNick.getText().length() > 12){
         e.consume();
      }
   }
   
   @FXML
   public void limitarCaracteresClave(KeyEvent e){
      String s = e.getCharacter();
      char c = s.charAt(0);
      if (pFieldClave.getText().length() > 12){
         e.consume();
      }
   }

     public boolean obtenerYValidarCamposCuenta(){
      boolean valido = false;
      if (tFieldNick.getText() != null && pFieldClave.getText() != null && pFieldcConfirmacionClave.getText() != null){
         valido = true;
      }
      return valido;
   }
   
     public void accionButtonBaja() {
      buttonBaja.setOnAction(event -> {
         try {
            AdministracionCuenta adminCuenta = null;
            adminCuenta.desactivarCuenta(cuentaLogueada.getNombreUsuario());
         } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(GUI_RegistrarController.class.getName()).log(Level.SEVERE, null, ex);
         }
      });
   }

   public void accionButtonRegresar() {
      buttonRegreso.setOnAction(event -> {
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

   /**
    * Método para cargar los datos del jugador en los cuadros
    */
   public void cargarDatos() {
      //Aquí debería cargar los datos del objeto Jugador y cambiar la bandera para poder hacer el regreso efectivo
      
   }

   /**
    * Método para cargar el idioma seleccionado por default en botones y etiquetas
    */
   public void cargarIdioma() {
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
