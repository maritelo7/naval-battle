/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.presentacion;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
public class GUI_PuntuacionesController implements Initializable {

   @FXML
   private JFXButton buttonRegresar;
   @FXML
   private Label labelPuntuaciones;
   @FXML
   private TableView<CuentaUsuario> tablePuntuaciones;
   private CuentaUsuario cuentaLogueada = null;
   final static String RECURSO_IDIOMA = "navalBattle.recursos.idiomas.Idioma";
   private String usuarioTable;
   private String puntajeTable;

   /**
    * Initializes the controller class.
    *
    * @param url
    * @param rb
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
      cargarIdioma();
      cargarTabla();
      cargarInformacionTabla();
      buttonRegresar.setOnAction(event -> {
         Node node = (Node) event.getSource();
         Stage stage = (Stage) node.getScene().getWindow();
         try {
            Scene scene;
            if (cuentaLogueada != null) {
               FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI_MenuPartida.fxml"));
               scene = new Scene(loader.load());
               GUI_MenuPartidaController controller = loader.getController();
               controller.cargarCuenta(cuentaLogueada);
               loader.setController(controller);

            } else {
               FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI_IniciarSesion.fxml"));
               scene = new Scene(loader.load());
               GUI_IniciarSesionController controller = loader.getController();
               controller.cargarSonido(false);
               loader.setController(controller);
            }
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
         } catch (IOException ex) {
            Logger.getLogger(GUI_IniciarSesionController.class.getName()).log(Level.SEVERE, null, ex);
         }
      });

   }

   /**
    * Método para cargar objeto cuenta y utilzar sus valores en este controller
    *
    * @param cuenta la CuentaUsuario con la que se ha iniciado sesión
    */
   public void cargarCuenta(CuentaUsuario cuenta) {
      this.cuentaLogueada = cuenta;
   }

   /**
    * Método para carga el idioma seleccionado por default en botones y etiquetas
    */
   public void cargarIdioma() {
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle(RECURSO_IDIOMA, locale);
      labelPuntuaciones.setText(resources.getString("labelPuntuaciones"));
      usuarioTable = resources.getString("usuarioTable");
      puntajeTable = resources.getString("puntajeTable");

   }

   /**
    * Método para cargar los elementos de la tabla de puntuaciones
    */
   public void cargarTabla() {
      tablePuntuaciones.setStyle("-fx-selection-bar: orange; -fx-selection-bar-non-focused: salmon");
      TableColumn nomUsuario = new TableColumn(usuarioTable);
      nomUsuario.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
      nomUsuario.setPrefWidth(175);
      TableColumn puntUsuario = new TableColumn(puntajeTable);
      puntUsuario.setCellValueFactory(new PropertyValueFactory<>("puntaje"));
      puntUsuario.setPrefWidth(175);
      tablePuntuaciones.getColumns().addAll(nomUsuario, puntUsuario);
   }

   /**
    * Método para cargar la información en la tabla de puntajes
    */
   public void cargarInformacionTabla() {
      AdministracionCuenta adminCuenta = new AdministracionCuenta();
      List<CuentaUsuario> cuentasConMejoresPuntajes;
      try {
         cuentasConMejoresPuntajes = adminCuenta.obtenerMejoresPuntajes();
         ObservableList<CuentaUsuario> listaCuentas = FXCollections.observableArrayList();
         for (int i = 0; i < cuentasConMejoresPuntajes.size(); i++) {
            if(cuentasConMejoresPuntajes.get(i).getPuntaje() > 0){
            listaCuentas.add(cuentasConMejoresPuntajes.get(i));
            }
         }
         tablePuntuaciones.setItems(listaCuentas);
      } catch (PersistenceException e) {
         Utileria.cargarAviso("titleAlerta", "mensajeErrorConexion");
      } catch (ArrayIndexOutOfBoundsException e) {
         Logger.getLogger(GUI_PuntuacionesController.class.getName()).log(Level.SEVERE, null, e);
      }
      
   }

}
