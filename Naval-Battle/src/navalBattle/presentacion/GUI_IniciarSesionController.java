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
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.persistence.PersistenceException;
import navalBattle.logica.AdministracionCuenta;
import navalBattle.logica.CuentaUsuario;
import navalBattle.recursos.Utileria;

/**
 * FXML Controller class
 *
 * @author Maribel Tello Rodriguez
 * @author José Alí Valdivia Ruiz
 */
public class GUI_IniciarSesionController implements Initializable {

   @FXML
   private JFXButton buttonRegistrar;
   @FXML
   private JFXButton buttonIdioma;
   @FXML
   private JFXButton buttonReglas;
   @FXML
   private JFXButton buttonPuntuacion;
   @FXML
   private JFXButton buttonIniciar;
   @FXML
   private JFXButton buttonAcerca;
   @FXML
   private JFXTextField tFieldNick;
   @FXML
   private JFXPasswordField pFieldClave;
   @FXML
   private Label labelNick;
   @FXML
   private Label labelClave;
   @FXML
   private StackPane stackMensaje;
   
   final static String IDIOMA_INGLES = "English";
   final static String RECURSO_IDIOMA = "navalBattle.recursos.idiomas.Idioma";

   /**
    * Initializes the controller class.
    *
    * @param url
    * @param rb
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
      cargarIdioma();

      buttonIdioma.setOnAction((ActionEvent event) -> {
         String idioma = cargarAvisoIdioma();
         Locale locale;
         switch (idioma) {
            case IDIOMA_INGLES:
               locale = new Locale("en", "US");
               break;
            case "Français":
               locale = new Locale("fr", "FR");
               break;
            case "Español":
               locale = Locale.ROOT;
               break;
            default:
               locale = Locale.getDefault();
         }
         Locale.setDefault(locale);
         cargarIdioma();
      });

      buttonRegistrar.setOnAction(event -> {
         cargarVentana(event, "GUI_Registrar.fxml");
      });

      buttonReglas.setOnAction(event -> {
         cargarVentana(event, "GUI_Reglas.fxml");
      });

      buttonPuntuacion.setOnAction(event -> {
         try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI_Puntuaciones.fxml"));
            Scene scene = new Scene(loader.load());
            GUI_PuntuacionesController controller = loader.getController();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();            
         } catch (IOException ex) {
            Logger.getLogger(GUI_IniciarSesionController.class.getName()).log(Level.SEVERE, null, ex);
         }
      });

      buttonIniciar.setOnAction((ActionEvent event) -> {
        if (validarCamposCuenta()) {
            //CuentaUsuario cuenta = new CuentaUsuario(tFieldNick.getText(), "0", "English");
            CuentaUsuario cuenta;
            try {
               cuenta = ingresar();
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
            } catch (ArrayIndexOutOfBoundsException ex) {
               Utileria.cargarAviso("titleAlerta", "mensajeDatosIncorrectosIniciarSesion");
               limpiar();
            } catch (PersistenceException ex) {
               Utileria.cargarAviso("titleAlerta", "mensajeErrorConexion");
               limpiar();            
            } catch (NoSuchAlgorithmException ex) {
              Logger.getLogger(GUI_IniciarSesionController.class.getName()).log(Level.SEVERE, null, ex);
           } 
         } else {
            Utileria.cargarAviso("titleAlerta", "mensajeCamposLlenos");
         }
      });
   }

   /**
    * Método para el inicio de sesión, donde se recupera el nickname y la clave y se intenta loguear
    * en una cuenta
    * @return La cuenta a la cual corresponde el nickname y la clave
    * @throws java.security.NoSuchAlgorithmException ocurre cuando un algoritmo es requerido pero no
    * está disponible
    */
  public CuentaUsuario ingresar() throws ArrayIndexOutOfBoundsException, PersistenceException, NoSuchAlgorithmException {
      CuentaUsuario cuentaRecuperada = null;
      AdministracionCuenta adminCuenta = new AdministracionCuenta();
      String nickname = tFieldNick.getText();
      String clave = pFieldClave.getText();
      cuentaRecuperada = adminCuenta.consultarCuenta(nickname, clave);
      return cuentaRecuperada;
   }

   /**
    * Método para cambiar de la ventana actual a otra
    *
    * @param event evento que desencadena un cambio de ventana
    * @param url nombre del archivo .fxml de la ventana a cargar
    */
   public void cargarVentana(Event event, String url) {
      Node node = (Node) event.getSource();
      Stage stage = (Stage) node.getScene().getWindow();
      try {
         Parent root = FXMLLoader.load(getClass().getResource(url));
         Scene scene = new Scene(root);
         stage.setScene(scene);
         stage.setResizable(false);
         stage.show();
      } catch (IOException ex) {
         Logger.getLogger(GUI_IniciarSesionController.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   /**
    * Método para cargar el idioma establecido como default en etiquetas y botones
    */
   public void cargarIdioma() {
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle(RECURSO_IDIOMA, locale);
      buttonIdioma.setText(resources.getString("buttonIdioma"));
      buttonReglas.setText(resources.getString("buttonReglas"));
      buttonPuntuacion.setText(resources.getString("buttonPuntuacion"));
      buttonIniciar.setText(resources.getString("buttonIniciar"));
      buttonRegistrar.setText(resources.getString("buttonRegistrar"));
      labelClave.setText(resources.getString("labelClave"));
      labelNick.setText(resources.getString("labelNick"));
      buttonAcerca.setText(resources.getString("buttonAcerca"));
   }

   /**
    * Método para cargar el aviso de seleccionar un idioma. Muestra las opciones en un combobox
    *
    * @return regresa el idioma seleccionado
    */
   public String cargarAvisoIdioma() {
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle(RECURSO_IDIOMA, locale);
      final String[] data = {"Español", "Français", IDIOMA_INGLES};
      List<String> listIdiomas = Arrays.asList(data);
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
    * Método para cargar el sonido de la ventana ya se por primera vez o después de regresar de otra
    * ventana
    *
    * @param estado Valor para definir si es necesario reproducir el sonido o no
    */
   public void cargarSonido(boolean estado) {
      URL resourceSonido = this.getClass().getResource("/navalBattle/recursos/sonidos/"
          + "MainThemeonMarimba.mp3");
      Media sound = new Media((resourceSonido).toString());
      MediaPlayer mediaP = new MediaPlayer(sound);
      mediaP.setVolume(1);
      if (estado) {
         mediaP.play();
      } else {
         mediaP.stop();
      }
   }

   /**
    * Acción del botón buttonAcerca que crea un mensaje de dialogo
    */
   @FXML
   public void mensajeAcerca() {
      JFXDialogLayout dialogLayout = new JFXDialogLayout();
      dialogLayout.setBody(new Text(bodyMensaje()));
      JFXDialog dialog = new JFXDialog(stackMensaje, dialogLayout, JFXDialog.DialogTransition.CENTER);
      JFXButton buttonOk = new JFXButton("OK");
      buttonOk.setOnAction((ActionEvent event) -> {
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
      ResourceBundle resources = ResourceBundle.getBundle(RECURSO_IDIOMA, locale);
      String mensaje = "Naval Battle\n" + resources.getString("mensajeDesa") + resources.getString("mensajeTradu");
      return mensaje;
   }

   /**
    * Método auxiliar par limpiar los campos de texto
    */
   public void limpiar() {
      tFieldNick.clear();
      pFieldClave.clear();
   }

   /**
    * Método auxiliar para comprobar que los campos obligatorios del Nickname y la Clave no están
    * nulos cuando se inicie la sesión
    *
    * @return regresa que es válido si ambos campos no están nulos
    */
   public boolean validarCamposCuenta() {
      return ((tFieldNick.getText() != null && !(tFieldNick.getText().trim().isEmpty()))
          && (pFieldClave.getText() != null && !(pFieldClave.getText().trim().isEmpty())));
   }
}
