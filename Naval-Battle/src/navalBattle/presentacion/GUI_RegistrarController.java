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
import java.util.Optional;
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
import navalBattle.datos.exceptions.PreexistingEntityException;
import navalBattle.logica.AdministracionCuenta;
import navalBattle.logica.CuentaUsuario;
import navalBattle.recursos.Utileria;

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

   private final static String MENSAJE_ERROR = "mensajeErrorConexion";
   private final static String TITULO_ALERTA = "titleAlerta";
   private CuentaUsuario cuentaLogueada;

   /**
    * Initializes the controller class.
    *
    * @param url
    * @param rb
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
      cargarIdioma();
      cargarComboIdioma();
      buttonBaja.setDisable(true);
      buttonRegreso.setOnAction(event -> {
         accionButtonRegresar(event);
      });
      buttonGuardar.setOnAction(event -> {
         accionButtonGuardar(event);
      });
      buttonBaja.setOnAction(event -> {
         cargarConfirmacionDeEliminarCuenta(event);
      });
   }

   /**
    * Método para cargar objeto cuenta y utilzar sus valores en este controller
    *
    * @param cuenta la CuentaUsuario con la que se ha iniciado sesión
    */
   public void cargarCuenta(CuentaUsuario cuenta) {
      this.cuentaLogueada = cuenta;
      if (cuentaLogueada != null) {
         tFieldNick.setText(cuentaLogueada.getNombreUsuario());
         comboIdioma.setValue(cuentaLogueada.getLenguaje());
         tFieldNick.setEditable(false);
         buttonBaja.setDisable(false);
      }
   }

   /**
    * Método para guardar los cambios de la CuentaUsuario
    *
    * @param event evento(nodo) del botón para accionar un cambio de ventana
    */
   public void accionButtonGuardar(Event event) {
      String mensaje;
      if (validarCamposCuenta()) {
         String nickname = tFieldNick.getText();
         String clave = pFieldClave.getText();
         String idioma = comboIdioma.getValue().toString();
         String confirmacionClave = pFieldConfirmacionClave.getText();
         if (clave.equals(confirmacionClave)) {
            AdministracionCuenta adminCuenta = new AdministracionCuenta();
            if (cuentaLogueada == null) {
               CuentaUsuario nuevaCuenta = new CuentaUsuario(nickname, clave, idioma, 0);
               try {
                  adminCuenta.registrarCuenta(nuevaCuenta);
                  cuentaLogueada = adminCuenta.consultarCuenta(nickname, clave);
                  mensaje = "mensajeGuardado";
               } catch (PreexistingEntityException ex) {
                  mensaje = "mensajeCuentaYaExistente";
               } catch (NoSuchAlgorithmException ex) {
                  mensaje = MENSAJE_ERROR;
               }
            } else {
               try {
                  adminCuenta.modificarCuenta(cuentaLogueada);
                  cuentaLogueada = adminCuenta.consultarCuenta(nickname, clave);
                  mensaje = "mensajeGuardado";
               } catch (Exception e) {
                  mensaje = MENSAJE_ERROR;
               }
            }
            accionButtonRegresar(event);
         } else {
            mensaje = "mensajeClaveNoCoincide";
         }
      } else {
         mensaje = "mensajeCamposLlenos";
      }
      Utileria.cargarAviso(TITULO_ALERTA, mensaje);

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
    * Método para dar de baja la cuenta con la que se ha iniciado sesión
    *
    */
   public void darDeBajaCuenta() {
      AdministracionCuenta adminCuenta = new AdministracionCuenta();
      boolean check = adminCuenta.desactivarCuenta(cuentaLogueada.getNombreUsuario());
      if (check) {
         cuentaLogueada = null;
         Utileria.cargarAviso(TITULO_ALERTA, "mensajeBaja");
      } else {
         Utileria.cargarAviso(TITULO_ALERTA, MENSAJE_ERROR);
      }
   }

   /**
    * Método para cambiar de la ventana actual a la anterior
    *
    * @param event evento que desencadena el cambio de ventana
    */
   public void accionButtonRegresar(Event event) {
      Node node = (Node) event.getSource();
      Stage stage;
      stage = (Stage) node.getScene().getWindow();
      Scene scene;
      if (cuentaLogueada != null) {
         try {
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("GUI_MenuPartida.fxml"));
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
            GUI_IniciarSesionController controller = loader.getController();
            controller.cargarSonido(false);
            loader.setController(controller);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
         } catch (IOException ex) {
            Logger.getLogger(GUI_RegistrarController.class.getName()).log(Level.SEVERE, null, ex);
         }
      }

   }

   /**
    * Método para cargar la confirmación antes de eliminar una cuenta
    *
    * @param event Evento(nodo) para desencadenar un cambio de ventana
    */
   public void cargarConfirmacionDeEliminarCuenta(Event event) {
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle("navalBattle.recursos.idiomas.Idioma", locale);
      String titulo = resources.getString("titleAlerta");
      String mensaje = resources.getString("mensajeconfirmacionElimiarCuenta");
      Alert confirmacion = new Alert(Alert.AlertType.INFORMATION);
      confirmacion.setTitle(titulo);
      confirmacion.setHeaderText(mensaje);
      ButtonType btAceptar = new ButtonType("OK", ButtonBar.ButtonData.CANCEL_CLOSE);
      confirmacion.getButtonTypes().setAll(btAceptar);
      Optional<ButtonType> eleccion = confirmacion.showAndWait();
      if (eleccion.get() == btAceptar) {
         darDeBajaCuenta();
         accionButtonRegresar(event);
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
      comboIdioma.getSelectionModel().selectFirst();
   }

   /**
    * Método auxiliar para comprobar que los campos obligatorios del Nickname y la Clave no están
    * nulos cuando se inicie la sesión
    *
    *
    * @return regresa que es válido si ambos campos no están nulos
    */
   public boolean validarCamposCuenta() {
      boolean esValido = false;
      if ((!tFieldNick.getText().isEmpty() && !(tFieldNick.getText().trim().isEmpty()))
          && (!pFieldClave.getText().isEmpty() && !(pFieldClave.getText().trim().isEmpty()))
          && (!pFieldConfirmacionClave.getText().isEmpty() && !(pFieldConfirmacionClave.getText().trim().isEmpty()))) {
         esValido = true;
      }
      return esValido;
   }
}
