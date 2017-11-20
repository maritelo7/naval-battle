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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
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
   @FXML
   private TextField tFieldNick;
   @FXML
   private PasswordField pFieldClave;
   @FXML
   private PasswordField pFieldConfirmacionClave;
   @FXML
   private ComboBox comboIdioma;

   CuentaUsuario cuentaLogueada;

   /**
    * Initializes the controller class.
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
      cargarIdioma();
      cargarComboIdioma();
      buttonRegreso.setOnAction(event -> {
         accionButtonRegresar(event);
      });
      buttonGuardar.setOnAction(event -> {
         accionButtonGuardar(event);
      });
      buttonBaja.setOnAction(event -> {
         accionButtonBaja(event);
      });

   }

    /**
    * Método para cargar objeto cuenta y utilzar sus valores en este controller
    * @param cuenta la CuentaUsuario con la que se ha iniciado sesión
    */
   public void cargarCuenta(CuentaUsuario cuenta) {
      this.cuentaLogueada = cuenta;
      tFieldNick.setText(cuentaLogueada.getNombreUsuario());
      pFieldClave.setText(cuentaLogueada.getClave());
      pFieldConfirmacionClave.setText(cuentaLogueada.getClave());
      comboIdioma.setValue(cuentaLogueada.getLenguaje());

   }
   /**
    * Método para guardar los cambios de la CuentaUsuario
    * @param event evento del botón
    */
   public void accionButtonGuardar(Event event) {

      if (obtenerYValidarCamposCuenta()) {
         String nickname = tFieldNick.getText();
         String clave = pFieldClave.getText();
         String confirmacionClave = pFieldConfirmacionClave.getText();
         if (clave.equals(confirmacionClave)) {
            AdministracionCuenta adminCuenta = new AdministracionCuenta();
            if (cuentaLogueada == null) {
               CuentaUsuario nuevaCuenta = new CuentaUsuario(nickname, clave);
               adminCuenta.registrarCuenta(nuevaCuenta);
               cuentaLogueada = adminCuenta.consultarCuenta(nickname, clave);
            } else {
               adminCuenta.modificarCuenta(cuentaLogueada);
               cuentaLogueada = adminCuenta.consultarCuenta(nickname, clave);
            }

         } else {
            cargarAviso("titleAlerta", "mensajeClaveNoCoincide");
         }
      } else {
          cargarAviso("titleAlerta", "mensajeCamposLlenos");
      }
      cargarAviso("titleAlerta", "mensajeGuardado");
   }

   /**
    * Método auxiliar para limitar los caracteres introducidos en el field de nickname a solo letras
    * mayúsculas, minúsculas y números
    *
    * @param e evento de teclado
    */
   @FXML
   public void limitarCaracteresNick(KeyEvent e) {
      String s = e.getCharacter();
      char c = s.charAt(0);
      if ((c > 'z' || c < 'a') && (c > '9' || c < '0')) {
         e.consume();
      }
   }

   /**
    * Método auxiliar para comprobar que los campos obligatorios del Nickname y la Clave no están 
    * nulos cuando se inicie la sesión
    *
    * @return regresa que es válido si ambos campos no están nulos
    */
   public boolean obtenerYValidarCamposCuenta() {
      boolean valido = false;
      if (tFieldNick.getText() != null && pFieldClave.getText() != null && pFieldConfirmacionClave.getText() != null) {
         valido = true;
      }
      return valido;
   }
   
    /**
    * Método reutilizable para cargar un ventana emergente
    *
    * @param nombreTitulo nombre del key del título
    * @param nombreMensaje nombre del key del mensaje
    */
   public void cargarAviso(String nombreTitulo, String nombreMensaje) {
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle("navalBattle.recursos.idiomas.Idioma", locale);
      String titulo = resources.getString(nombreTitulo);
      String mensaje = resources.getString(nombreMensaje);
      Alert confirmacion = new Alert(Alert.AlertType.INFORMATION);
      confirmacion.setTitle(titulo);
      confirmacion.setHeaderText(null);
      confirmacion.setContentText(mensaje);
      ButtonType btAceptar = new ButtonType("OK", ButtonBar.ButtonData.CANCEL_CLOSE);
      confirmacion.getButtonTypes().setAll(btAceptar);
      confirmacion.showAndWait();
   }

   /**
    * Método para dar de baja la cuenta con la que se ha iniciado sesión
    * @param event evento del botón
    */
   public void accionButtonBaja(Event event) {
      AdministracionCuenta adminCuenta = new AdministracionCuenta();
      adminCuenta.desactivarCuenta(cuentaLogueada.getNombreUsuario());
      cargarAviso("titleAlerta", "mensajeBaja");
   }
   /**
    * Método para cambiar de la ventana actual a la anterior
    * @param event evento que desencadena el cambio de ventana
    */
   public void accionButtonRegresar(Event event) {
      Node node = (Node) event.getSource();
      Stage stage = (Stage) node.getScene().getWindow();
      Scene scene;
      if (cuentaLogueada != null) {
         try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI_MenuPartida.fxml"));
            scene = new Scene(loader.load());
            GUI_MenuPartidaController controller = loader.getController();
            controller.cargarCuenta(cuentaLogueada);
            loader.setController(controller);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
         } catch (IOException ex) {
            Logger.getLogger(GUI_RegistrarController.class.getName()).log(Level.SEVERE, null, ex);
         }
      } else {
         try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI_IniciarSesion.fxml"));
            scene = new Scene(loader.load());
            loader.setController(loader.getController());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
         } catch (IOException ex) {
            Logger.getLogger(GUI_RegistrarController.class.getName()).log(Level.SEVERE, null, ex);
         }
      }

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

    /**
    * Método para cargar la lista de idiomas en el combo box 
    */
   public void cargarComboIdioma() {
      final String[] data = {"Español", "Français", "English"};
      ObservableList<String> listIdiomas = FXCollections.observableArrayList(data);
      comboIdioma.setItems(listIdiomas);
   }
}
