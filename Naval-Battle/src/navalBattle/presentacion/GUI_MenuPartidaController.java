/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.presentacion;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import navalBattle.logica.CuentaUsuario;
import navalBattle.logica.InteraccionServidor;
import navalBattle.recursos.Utileria;
import navalBattle.recursos.animaciones.Barco;
import navalBattle.recursos.animaciones.SpriteAnimation;

/**
 * FXML Controller class
 *
 * @author Maribel Tello Rodriguez
 * @author José Alí Valdivia Ruiz
 */
public class GUI_MenuPartidaController implements Initializable {

   CuentaUsuario cuentaLogueada;

   @FXML
   private JFXButton buttonReglas;
   @FXML
   private JFXButton buttonPuntuacion;
   @FXML
   private JFXButton buttonSalir;
   @FXML
   private JFXButton buttonCrearPartida;
   @FXML
   private JFXButton buttonUnirmePartida;
   @FXML
   private JFXButton buttonConfigurar;
   @FXML
   private Label labelSalir;
   @FXML
   private Label labelConfigurar;
   @FXML
   private Label labelIniciando;
   @FXML
   private ImageView imageVBoat;

   /**
    * Initializes the controller class.
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {

      cargarAnimacion();
      labelIniciando.setVisible(false);

      buttonCrearPartida.setOnAction(event -> {
         labelIniciando.setVisible(true);
         conectarConServidor();
         boolean conexion = true;
         if (conexion) {
            irPrepararPartida(event);
         }
      });

      buttonUnirmePartida.setOnAction(event -> {
         labelIniciando.setVisible(true);
         //ESTABLECER CONEXIÓN Y SER CLIENTE
         boolean conexion = true;
         if (conexion) {
            irPrepararPartida(event);
         }
      });

      buttonConfigurar.setOnAction(event -> {
         Node node = (Node) event.getSource();
         Stage stage = (Stage) node.getScene().getWindow();
         try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI_Registrar.fxml"));
            Scene scene = new Scene(loader.load());
            GUI_RegistrarController controller = loader.getController();
            controller.cargarCuenta(cuentaLogueada);
            loader.setController(controller);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
         } catch (IOException ex) {
            Logger.getLogger(GUI_MenuPartidaController.class.getName()).log(Level.SEVERE, null, ex);
         }
      });

      buttonSalir.setOnAction(event -> {
         cargarVentana(event, "GUI_IniciarSesion.fxml");
      });

      buttonReglas.setOnAction(event -> {
         Node node = (Node) event.getSource();
         Stage stage = (Stage) node.getScene().getWindow();
         try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI_Reglas.fxml"));
            Scene scene = new Scene(loader.load());
            GUI_ReglasController controller = loader.getController();
            controller.cargarCuenta(cuentaLogueada);
            loader.setController(controller);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
         } catch (IOException ex) {
            Logger.getLogger(GUI_MenuPartidaController.class.getName()).log(Level.SEVERE, null, ex);
         }
      });

      buttonPuntuacion.setOnAction(event -> {
         Node node = (Node) event.getSource();
         Stage stage = (Stage) node.getScene().getWindow();
         try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI_Puntuaciones.fxml"));
            Scene scene = new Scene(loader.load());
            GUI_PuntuacionesController controller = loader.getController();
            controller.cargarCuenta(cuentaLogueada);
            loader.setController(controller);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
         } catch (IOException ex) {
            Logger.getLogger(GUI_MenuPartidaController.class.getName()).log(Level.SEVERE, null, ex);
         }
      });
   }
    /**
    * Método para cargar objeto cuenta y utilzar sus valores en este controller
    * @param cuenta la CuentaUsuario con la que se ha iniciado sesión
    */
   public void cargarCuenta(CuentaUsuario cuenta) {
      this.cuentaLogueada = cuenta;
      //AQUÍ DEBERÍA TOMAR EL IDIOMA DE LA CUENTA Y ESTABLECERLO
         String idioma = cuentaLogueada.getLenguaje();
         Locale locale;         
         switch (idioma) {
            case "English":
               locale = new Locale("en", "US");
               break;
            case "Français":
               locale = new Locale("fr", "FR");
               break;
            default:
               locale = Locale.ROOT;
               break;
         }
         Locale.setDefault(locale);
         cargarIdioma();
   }
   
      public void conectarConServidor(){
      String nombreUsuario = cuentaLogueada.getNombreUsuario();
      try {
         InteraccionServidor.conectar(nombreUsuario);
      } catch (URISyntaxException ex) {
         Logger.getLogger(GUI_MenuPartidaController.class.getName()).log(Level.SEVERE, null, ex);
         Utileria.cargarAviso("titleAlerta", "mensajeErrorConexion");
      } catch (UnknownHostException ex) {
         Logger.getLogger(GUI_MenuPartidaController.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
   
    /**
    * Método para cambiar de la ventana actual a otra
    * @param event evento que desencadena un cambio de ventana
    */
   public void irPrepararPartida(Event event) {
      Node node = (Node) event.getSource();
      Stage stage;
      stage = (Stage) node.getScene().getWindow();
      try {
         FXMLLoader loader;
         loader = new FXMLLoader(getClass().getResource("GUI_PrepararPartida.fxml"));
         Scene scene = new Scene(loader.load());
         GUI_PrepararPartidaController controller = loader.getController();
         controller.cargarCuenta(cuentaLogueada);
         loader.setController(controller);
         stage.setScene(scene);
         stage.setResizable(false);
         stage.show();
      } catch (IOException ex) {
         Logger.getLogger(GUI_MenuPartidaController.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   /**
    * Método para cargar el idioma seleccionado por default en etiquetas y botones
    */
   public void cargarIdioma() {
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle("navalBattle.recursos.idiomas.Idioma", locale);
      buttonReglas.setText(resources.getString("buttonReglas"));
      buttonPuntuacion.setText(resources.getString("buttonPuntuacion"));
      buttonCrearPartida.setText(resources.getString("buttonCrearPartida"));
      buttonUnirmePartida.setText(resources.getString("buttonUnirmePartida"));
      labelSalir.setText(resources.getString("labelSalir"));
      labelConfigurar.setText(resources.getString("labelConfigurar"));
      labelIniciando.setText(resources.getString("labelIniciando"));
   }
   /**
    * Método para cambiar de la ventana actual a otra
    * @param event evento que desencadena un cambio de ventana
    * @param url nombre del archivo .fxml de la ventana a cargar
    */
   public void cargarVentana(Event event, String url) {
      Node node = (Node) event.getSource();
      Stage stage;
      stage = (Stage) node.getScene().getWindow();
      try {
         Parent root = FXMLLoader.load(getClass().getResource(url));
         Scene scene = new Scene(root);
         stage.setScene(scene);
         stage.setResizable(false);
         stage.show();
      } catch (IOException ex) {
         Logger.getLogger(GUI_MenuPartidaController.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   /**
    * Método para cargar animación de barco
    */
   public void cargarAnimacion() {
      final Image IMAGE = new Image(Barco.class.getResourceAsStream("/navalBattle/recursos/imagenes/boatMove.png"));
      final int WIDTH = 464;
      final int HEIGHT = 237;
      final int COLUMNS = 3;
      final int COUNT = 6;
      final int OFFSET_X = 8;
      final int OFFSET_Y = 0;

      imageVBoat.setImage(IMAGE);
      imageVBoat.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));
      final Animation animation = new SpriteAnimation(
          imageVBoat,
          Duration.millis(4000),
          COUNT, COLUMNS,
          OFFSET_X, OFFSET_Y,
          WIDTH, HEIGHT
      );
      animation.setCycleCount(Animation.INDEFINITE);
      animation.play();
   }
}
