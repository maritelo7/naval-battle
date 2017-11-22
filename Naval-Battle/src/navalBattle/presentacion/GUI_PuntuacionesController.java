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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import navalBattle.logica.AdministracionCuenta;
import navalBattle.logica.CuentaUsuario;

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
   private TableView<?> tablePuntuaciones;
   @FXML
   private TableColumn<?, ?> columnNickname;
   @FXML
   private TableColumn<?, ?> columnPuntaje;      
   CuentaUsuario cuentaLogueada = null;
   

   /**
    * Initializes the controller class.
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
      cargarIdioma();
      
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
               Parent root = FXMLLoader.load(getClass().getResource("GUI_IniciarSesion.fxml"));
               scene = new Scene(root);
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
      ResourceBundle resources = ResourceBundle.getBundle("navalBattle.recursos.idiomas.Idioma", locale);
      labelPuntuaciones.setText(resources.getString("labelPuntuaciones"));
   }
   
   public void cargarTabla(){
   AdministracionCuenta adminCuenta = new AdministracionCuenta();
   List<CuentaUsuario> cuentasConMejoresPuntajes = adminCuenta.obtenerMejoresPuntajes();
   ObservableList<CuentaUsuario> listaCuentas = FXCollections.observableArrayList();
      for (int i = 0; i < cuentasConMejoresPuntajes.size(); i++) {
         listaCuentas.add(cuentasConMejoresPuntajes.get(i));
      }
   }
      
}
