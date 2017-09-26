/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

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
      Locale locale = Locale.getDefault();
      cargarIdioma(locale);
      
      buttonIdioma.setOnAction((ActionEvent event) -> {
         Locale localeSelect = Locale.getDefault();
         idioma = cargarAviso();
         System.out.println("Idioma"+ idioma);
         switch (idioma){
            case "English":
               Locale localeEng = new Locale("en", "US");
               cargarIdioma(localeEng);
            break;
            case "Français":
               Locale localeFran = new Locale("fr", "FR");
               cargarIdioma(localeFran);
            break;
            default:
            cargarIdioma(localeSelect);
         }
      });
   }
   
   public void cargarIdioma(Locale localeSelect){
      
      ResourceBundle resources = ResourceBundle.getBundle("navalBattle.recursos.Idioma", localeSelect);
      buttonIdioma.setText(resources.getString("buttonIdioma"));
      buttonReglas.setText(resources.getString("buttonReglas"));
      buttonPuntuacion.setText(resources.getString("buttonPuntuacion"));
      buttonIniciar.setText(resources.getString("buttonIniciar"));
      buttonRegistrar.setText(resources.getString("buttonRegistrar"));
      labelClave.setText(resources.getString("labelClave"));
      labelNick.setText(resources.getString("labelNick"));
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
      ResourceBundle resources = ResourceBundle.getBundle("navalBattle.recursos.Idioma",locale);
      String idiomaSelect;
      Alert confirmacion = new Alert(Alert.AlertType.INFORMATION);
      ComboBox comboIdiomas = new ComboBox();
      List<String> listIdiomas = new ArrayList<>();
      ObservableList<String> idiomas = FXCollections.observableList(listIdiomas);
      idiomas.addAll("Español","Français","English");
      comboIdiomas.setItems(idiomas);
      confirmacion.setTitle(resources.getString("buttonIdioma"));
      confirmacion.setHeaderText(null);
      confirmacion.setContentText(resources.getString("labelIdioma"));
      confirmacion.setGraphic(comboIdiomas);
      ButtonType btCancelar = new ButtonType(resources.getString("buttonCancelar"), ButtonBar.ButtonData.CANCEL_CLOSE);
      ButtonType btAceptar = new ButtonType(resources.getString("buttonGuardar"));
      confirmacion.getButtonTypes().setAll(btCancelar, btAceptar);
      Optional<ButtonType> eleccion = confirmacion.showAndWait();
      if (eleccion.get() == btAceptar) {
         return comboIdiomas.getValue().toString();
      }
      return " ";
   }
}
