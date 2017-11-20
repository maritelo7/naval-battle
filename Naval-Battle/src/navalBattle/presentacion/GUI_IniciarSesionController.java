/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.presentacion;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import navalBattle.logica.AdministracionCuenta;
import navalBattle.logica.CuentaUsuario;

/**
 * FXML Controller class
 *
 * @author Maribel Tello Rodriguez
 * @author José Alí Valdivia Ruiz
 */
public class GUI_IniciarSesionController implements Initializable {

   @FXML
   private JFXButton buttonIdioma;
   @FXML
   private JFXButton buttonReglas;
   @FXML
   private JFXButton buttonPuntuacion;
   @FXML
   private JFXButton buttonIniciar;
   @FXML
   private JFXButton buttonRegistrar;
   @FXML
   private JFXTextField tFieldNick;
   @FXML
   private JFXPasswordField pFieldClave;
   @FXML
   private Label labelNick;
   @FXML
   private Label labelClave;
   @FXML
   private JFXButton buttonAcerca;
   @FXML
   private StackPane stackMensaje;
   public String idioma;

   /**
    * Initializes the controller class.
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
      cargarIdioma();
      //cargarSonido();

      buttonIdioma.setOnAction((ActionEvent event) -> {
         Locale localeSelect = Locale.getDefault();
         idioma = cargarAvisoIdioma();
         System.out.println("Idioma" + idioma);
         switch (idioma) {
            case "English":
               Locale localeEng = new Locale("en", "US");
               Locale.setDefault(localeEng);
               cargarIdioma();
               break;
            case "Français":
               Locale localeFran = new Locale("fr", "FR");
               Locale.setDefault(localeFran);
               cargarIdioma();
               break;
            default:
               Locale locale = Locale.ROOT;
               Locale.setDefault(locale);
               cargarIdioma();
         }
      });
      buttonRegistrar.setOnAction(event -> {
         cargarVentana(event, "GUI_Registrar.fxml");
      });
      buttonReglas.setOnAction(event -> {
         cargarVentana(event, "GUI_Reglas.fxml");
      });
      buttonPuntuacion.setOnAction(event -> {
         cargarVentana(event, "GUI_Puntuaciones.fxml");
      });
      
      buttonIniciar.setOnAction((ActionEvent event) -> {
         CuentaUsuario cuenta = ingresar();
         if (cuenta != null) {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            try { 
               FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI_MenuPartida.fxml"));
               Scene scene = new Scene(loader.load());
               GUI_MenuPartidaController controller = loader.getController();
               controller.cargarCuenta(cuenta);
               stage.setScene(scene);
               stage.setResizable(false);
               stage.show();
            } catch (IOException ex) {
               Logger.getLogger(GUI_IniciarSesionController.class.getName()).log(Level.SEVERE, null, ex);
            }           
         } else {
            cargarAviso("titleAlerta", "mensajeImposibleIniciarSesion");
         }
      });
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
      if ((c > 'z' || c < 'A') && (c > '9' || c < '0')) {
         e.consume();
      }
   }

   /**
    * Método para el inicio de sesión, donde se recupera el nickname y la clave y se intenta loguear
    * en una cuenta
    *
    * @return La cuenta a la cual corresponde el nickname y la clave
    */
   public CuentaUsuario ingresar() {
      CuentaUsuario cuentaRecuperada = null;
      if (obtenerYValidarCamposCuenta()) {
         System.out.println("ESTO NO DEBERIA PASAR");
         AdministracionCuenta adminCuenta = new AdministracionCuenta();
         String nickname = tFieldNick.getText();
         String clave = pFieldClave.getText();
         try {
            cuentaRecuperada = adminCuenta.consultarCuenta(nickname, clave);
         } catch (Exception ex) {
           Logger.getLogger(GUI_IniciarSesionController.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
      return cuentaRecuperada;
   }
   /**
    * Método auxiliar para comprobar que los campos obligatorios del Nickname y la Clave no están 
    * nulos cuando se inicie la sesión
    *
    * @return regresa que es válido si ambos campos no están nulos
    */
   
   public boolean obtenerYValidarCamposCuenta() {
      boolean valido = false;
      if (tFieldNick.getText() != null || pFieldClave.getText() != null) {
         valido = true;
      }
      return valido;
   }
    /**
    * Método para cambiar de la ventana actual a otra
    * @param event evento que desencadena un cambio de ventana
    * @param url nombre del archivo .fxml de la ventana a cargar
    */
   public void cargarVentana(Event event, String url) {
         Node node = (Node) event.getSource();
         Stage stage = (Stage) node.getScene().getWindow();
         Parent root;
         try {
            root = FXMLLoader.load(getClass().getResource(url));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);   
            stage.show();
         } catch (IOException ex) {
            Logger.getLogger(GUI_IniciarSesionController.class.getName()).log(Level.SEVERE, null, ex);
         }
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
    * Método para cargar el idioma en etiquetas y botones establecido como default
    */
   public void cargarIdioma() {
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle("navalBattle.recursos.idiomas.Idioma", locale);
      buttonIdioma.setText(resources.getString("buttonIdioma"));
      buttonReglas.setText(resources.getString("buttonReglas"));
      buttonPuntuacion.setText(resources.getString("buttonPuntuacion"));
      buttonIniciar.setText(resources.getString("buttonIniciar"));
      buttonRegistrar.setText(resources.getString("buttonRegistrar"));
      labelClave.setText(resources.getString("labelClave"));
      labelNick.setText(resources.getString("labelNick"));
   }

   /**
    * Método para cargar el aviso de seleccionar un idioma. Muestra las opciones en un combobox
    *
    * @return regresa el idioma seleccionado
    */
   public String cargarAvisoIdioma() {
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle("navalBattle.recursos.idiomas.Idioma", locale);
      final String[] data = {"Español", "Français", "English"};
      List<String> listIdiomas = new ArrayList<>();
      listIdiomas = Arrays.asList(data);
      ChoiceDialog choiceIdioma = new ChoiceDialog(listIdiomas.get(0), listIdiomas);
      choiceIdioma.setHeaderText(resources.getString("buttonIdioma"));
      choiceIdioma.setContentText(resources.getString("labelIdioma"));
      Optional<ButtonType> eleccion = choiceIdioma.showAndWait();
      if (eleccion.isPresent()) {
         return choiceIdioma.getResult().toString();
      }
      return " ";
   }

   /**
    * Método para cargar el sonido de la ventana
    */
   public void cargarSonido() {
      String separator = System.getProperty("file.separator");
      System.out.println(separator +"navalBattle" + separator + "recursos" + separator + "sonidos" + separator +"MainThemeonMarimba.mp3");
      final String resourceSonido = this.getClass().getResource(separator +"navalBattle" + separator 
          + "recursos" + separator + "sonidos" + separator + "MainThemeonMarimba.mp3").toExternalForm();
//      final String resourceSonido = this.getClass().getResource("\navalBattle\recursos\sonidos\MainThemeonMarimba.mp3").toExternalForm();
      Media sound = new Media(new File(resourceSonido).toString());
      MediaPlayer mediaP = new MediaPlayer(sound);
      mediaP.setVolume(1);
      mediaP.play();

   }

   /**
    * Acción del botón buttonAcerca. Crea un mensaje de dialogo
    */
   @FXML
   public void mensajeAcerca() {
      JFXDialogLayout dialogLayout = new JFXDialogLayout();
      dialogLayout.setBody(new Text(bodyMensaje()));
      JFXDialog dialog = new JFXDialog(stackMensaje, dialogLayout, JFXDialog.DialogTransition.CENTER);
      JFXButton buttonOk = new JFXButton("OK");
      buttonOk.setOnAction(event -> {
         dialog.close();
      });
      dialogLayout.setActions(buttonOk);
      dialog.show();
   }

   /**
    * Método auxiliar para la internacionalización del texto del mensaje de dialogo
    *
    * @return String del mensaje internacionalizado
    */
   public String bodyMensaje() {
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle("navalBattle.recursos.idiomas.Idioma", locale);//Modificar paquete idioma
      String mensaje = "Naval Battle\n" + resources.getString("mensajeDesa") + resources.getString("mensajeTradu");
      return mensaje;
   }

}
