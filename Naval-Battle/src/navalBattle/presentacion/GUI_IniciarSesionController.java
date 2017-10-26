/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.presentacion;

import com.jfoenix.controls.JFXButton;
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author javr
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
   public String idioma;

   /**
    * Initializes the controller class.
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {

      cargarIdioma();
      cargarSonido();

      buttonIdioma.setOnAction((ActionEvent event) -> {
         Locale localeSelect = Locale.getDefault();
         idioma = cargarAviso();
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
      buttonIniciar.setOnAction(event -> {
         try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Parent root;
            //PASAR OBJETO USUARIO
            root = FXMLLoader.load(getClass().getResource("GUI_MenuPartida.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
         } catch (IOException ex) {
            Logger.getLogger(GUI_IniciarSesionController.class.getName()).log(Level.SEVERE, null, ex);
         }

      });
      buttonRegistrar.setOnAction(event -> {
         Node node = (Node) event.getSource();
         Stage stage = (Stage) node.getScene().getWindow();
         Parent root;
         try {
            root = FXMLLoader.load(getClass().getResource("GUI_Registrar.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
         } catch (IOException ex) {
            Logger.getLogger(GUI_IniciarSesionController.class.getName()).log(Level.SEVERE, null, ex);
         }
      });
      buttonReglas.setOnAction(event -> {
         try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Parent root;
            root = FXMLLoader.load(getClass().getResource("GUI_Reglas.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
         } catch (IOException ex) {
            Logger.getLogger(GUI_IniciarSesionController.class.getName()).log(Level.SEVERE, null, ex);
         }
      });
      buttonPuntuacion.setOnAction(event -> {
         try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Parent root;
            root = FXMLLoader.load(getClass().getResource("GUI_Puntuaciones.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
         } catch (IOException ex) {
            Logger.getLogger(GUI_IniciarSesionController.class.getName()).log(Level.SEVERE, null, ex);
         }
      });
   }

   public void cargarIdioma() {
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle("navalBattle.recursos.idiomas.Idioma", locale);
      buttonIdioma.setText(resources.getString("buttonIdioma"));
      buttonReglas.setText(resources.getString("buttonReglas"));
      buttonPuntuacion.setText(resources.getString("buttonPuntuacion"));
      buttonIniciar.setText(resources.getString("buttonIniciar"));
      buttonRegistrar.setText(resources.getString("buttonRegistrar"));
      labelClave.setText(resources.getString("labelClave"));

   }

   public void cargarAviso(String title, String mensaje) {
      Alert confirmacion = new Alert(Alert.AlertType.INFORMATION);
      confirmacion.setTitle(title);
      confirmacion.setHeaderText(null);
      confirmacion.setContentText(mensaje);
      ButtonType btAceptar = new ButtonType("Aceptar", ButtonBar.ButtonData.CANCEL_CLOSE);
      confirmacion.getButtonTypes().setAll(btAceptar);
      Optional<ButtonType> eleccion = confirmacion.showAndWait();
   }

   public String cargarAviso() {
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle("navalBattle.recursos.Idioma", locale);
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

   public void cargarSonido() {
      final String resourceSonido = this.getClass().getResource("/navalBattle/recursos/sonidos/Main Theme on Marimba.mp3").toExternalForm();
      Media sound = new Media(new File(resourceSonido).toString());
      MediaPlayer mediaP = new MediaPlayer(sound);
      mediaP.setVolume(1);
      mediaP.play();

   }
}
