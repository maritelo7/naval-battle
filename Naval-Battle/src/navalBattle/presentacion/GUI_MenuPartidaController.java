/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.presentacion;

import com.jfoenix.controls.JFXButton;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.Optional;
import java.util.Properties;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
   private CuentaUsuario cuentaLogueada;
   final static String RECURSO_IDIOMA = "navalBattle.recursos.idiomas.Idioma";
   private final InteraccionServidor interaccionServidor = new InteraccionServidor();

   /**
    * Initializes the controller class.
    *
    * @param url
    * @param rb
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {

      cargarAnimacion();
      labelIniciando.setVisible(false);
      
      buttonCrearPartida.setOnAction(event -> {
         labelIniciando.setVisible(true);
         if (cambiarConfiguracion()) {
            cargarConfiguracionIp("titleRed", "mensajeConfServ");
         }
         try {
            if (conectarServidor()) {
               prepararPartidaHost(event);
            } else {
               labelIniciando.setVisible(false);
               Utileria.cargarAviso("titleAlerta", "mensajeErrorConexion");
            }
         } catch (InterruptedException ex) {
            Logger.getLogger(GUI_MenuPartidaController.class.getName()).log(Level.SEVERE, null, ex);
         }

      });

      buttonUnirmePartida.setOnAction(event -> {
         labelIniciando.setVisible(true);
         try {
            if (cambiarConfiguracion()) {
               cargarConfiguracionIp("titleRed", "mensajeConfServ");
            }
            String nickARetar = cargarConfiguracionNick("titleRed", "mensajeConfClien");
            if (nickARetar != null) {
               if (conectarServidor()) {
                  if (conectarInvitado(nickARetar)) {
                     prepararPartidaCliente(event, nickARetar);
                  }
               } else {
                  labelIniciando.setVisible(false);
                  Utileria.cargarAviso("titleAlerta", "mensajeErrorConexion");
               }
            }
         } catch (InterruptedException ex) {
            Logger.getLogger(GUI_MenuPartidaController.class.getName()).log(Level.SEVERE, null, ex);
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
            controller.cargarInformacionTabla();
         } catch (IOException ex) {
            Logger.getLogger(GUI_MenuPartidaController.class.getName()).log(Level.SEVERE, null, ex);
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
      String idioma = cuentaLogueada.getLenguaje();
      Locale locale;
         switch (idioma) {
            case "English":
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
   }

   /**
    * Método para conectar con el servidor
    *
    * @return boolean en caso de ser exitoso
    * @throws InterruptedException ocurre cuando el hilo es interrumpido
    */
   public boolean conectarServidor() throws InterruptedException {
      Utileria bandera = new Utileria(false);
      if (InteraccionServidor.socket == null) {
         String nombreUsuario = cuentaLogueada.getNombreUsuario();
         interaccionServidor.conectarServidor(nombreUsuario, bandera);
         synchronized (bandera) {
            bandera.wait();
         }
      }else{
         bandera.setBandera(true);
      }
      return bandera.isBandera();

   }

   /**
    * Método para conectar con un adversario en especial
    *
    * @param nickARetar nombre del jugador a ser retado
    * @return boolean que regresa true si la conexión es exitosa
    */
   public boolean conectarInvitado(String nickARetar) {
      Utileria bandera = new Utileria(false);
      String nombreUsuario = cuentaLogueada.getNombreUsuario();
      try {
         interaccionServidor.conectarInvitado(nombreUsuario, nickARetar, bandera);
         synchronized (bandera) {
            bandera.wait();
         }
         if (bandera.isBandera() == false) {
            labelIniciando.setVisible(false);
            Utileria.cargarAviso("titleAlerta", "mensajeSinJugador");
         }
      } catch (InterruptedException ex) {
         Logger.getLogger(GUI_MenuPartidaController.class.getName()).log(Level.SEVERE, null, ex);
      }
      return bandera.isBandera();
   }

   /**
    * Método para cambiar de la ventana actual a PrepararPartida. Exclusivo para el cliente
    *
    * @param event evento que desencadena un cambio de ventana
    * @param nick nombre del adversario para usarlo en la siguiente ventana
    */
   public void prepararPartidaCliente(Event event, String nick) {
      Node node = (Node) event.getSource();
      Stage stage = (Stage) node.getScene().getWindow();
      try {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI_PrepararPartida.fxml"));
         Scene scene = new Scene(loader.load());
         GUI_PrepararPartidaController controller = loader.getController();
         controller.cargarCuenta(cuentaLogueada);
         controller.cargarInteraccionServidor(interaccionServidor);
         controller.setNombreAdversario(nick);
         controller.activarReady();
         controller.cargarController(controller);
         controller.activarRecibirTablero();
         loader.setController(controller);
         stage.setScene(scene);
         stage.setResizable(false);
         stage.show();
      } catch (IOException ex) {
         Logger.getLogger(GUI_MenuPartidaController.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   /**
    * Método para cambiar de la ventana actual a PrepararPartida. Exclusivo para el host
    *
    * @param event evento que desencadena un cambio de ventana
    */
   public void prepararPartidaHost(Event event) {
      Node node = (Node) event.getSource();
      Stage stage = (Stage) node.getScene().getWindow();
      try {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI_PrepararPartida.fxml"));
         Scene scene = new Scene(loader.load());
         GUI_PrepararPartidaController controller = loader.getController();
         controller.cargarCuenta(cuentaLogueada);
         controller.cargarInteraccionServidor(interaccionServidor);
         controller.cargarController(controller);
         loader.setController(controller);
         controller.activarEspera();
         stage.setScene(scene);
         stage.setResizable(false);
         stage.show();
      } catch (IOException ex) {
         Logger.getLogger(GUI_MenuPartidaController.class.getName()).log(Level.SEVERE, null, ex);
   }
   }

   /**
    * Método para cargar la configuración de IP del servidor
    *
    * @param title key del titulo del pop-up
    * @param body key del mensaje del pop-up
    */
   public void cargarConfiguracionIp(String title, String body) {
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle(RECURSO_IDIOMA, locale);
      String titulo = resources.getString(title);
      String mensaje = resources.getString(body);
      Alert confirmacion = new Alert(Alert.AlertType.INFORMATION);
      TextField tfNumIP = new TextField();
      confirmacion.setTitle(titulo);
      confirmacion.setHeaderText(mensaje);
      confirmacion.setGraphic(tfNumIP);
      ButtonType btAceptar = new ButtonType("OK", ButtonBar.ButtonData.CANCEL_CLOSE);
      confirmacion.getButtonTypes().setAll(btAceptar);
      confirmacion.showAndWait();
      String numIp =  tfNumIP.getText();
      if (numIp.trim().isEmpty()) {
         Utileria.cargarAviso("titleAlerta", "mensajeCamposLlenos");
         cargarConfiguracionIp(title, body);
      } else {
         guardarPropiedad(numIp);
      }
   }

   /**
    * Método para cargar el nombre del jugador a retar
    *
    * @param title key del título del pop-up
    * @param body key del mensaje del pop-up
    * @return regresa el nombre ingresado en el pop-up
    */
   public String cargarConfiguracionNick(String title, String body)  {
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle(RECURSO_IDIOMA, locale);
      String titulo = resources.getString(title);
      String mensaje = resources.getString(body);
      String btnNomCancelar = resources.getString("buttonCancelar");
      Alert confirmacion = new Alert(Alert.AlertType.INFORMATION);
      TextField tfNombre = new TextField();
      confirmacion.setTitle(titulo);
      confirmacion.setHeaderText(mensaje);
      confirmacion.setGraphic(tfNombre);
      ButtonType btnCancelar = new ButtonType(btnNomCancelar, ButtonBar.ButtonData.CANCEL_CLOSE);
      ButtonType btnAceptar = new ButtonType("OK");
      confirmacion.getButtonTypes().setAll(btnAceptar, btnCancelar);
      Optional<ButtonType> eleccion = confirmacion.showAndWait();
      String nick = tfNombre.getText();
      if (eleccion.get() == btnAceptar) {
         if (nick.trim().isEmpty()) {
            Utileria.cargarAviso("titleAlerta", "mensajeCamposLlenos");
            cargarConfiguracionIp(title, body);
         }
      }
      return nick;
   }

   /**
    * Método para confirmar la configuración de red
    *
    * @return boolean de decisión que regresa true si se va a cambiar la configuración del servidor
    */
   public boolean cambiarConfiguracion() {
      boolean check = false;
      Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle(RECURSO_IDIOMA, locale);
      String titulo = resources.getString("titleAlerta");
      String mensaje = resources.getString("mensajeCambiarConfRed");
      String btnNomCancelar = resources.getString("buttonCancelar");
      String btnNomCambiar = resources.getString("titleRed");
      confirmacion.setTitle(titulo);
      confirmacion.setHeaderText(mensaje);
      ButtonType btnCambiar = new ButtonType(btnNomCambiar);
      ButtonType btnCancelar = new ButtonType(btnNomCancelar, ButtonBar.ButtonData.CANCEL_CLOSE);
      confirmacion.getButtonTypes().setAll(btnCambiar, btnCancelar);
      Optional<ButtonType> eleccion = confirmacion.showAndWait();
      if (eleccion.get() == btnCambiar) {
         check = true;
      }
      return check;
   }

   /**
    * Método para guardar la configuración de red
    *
    * @param numIp ip del servidor
    */
   public void guardarPropiedad(String numIp) {
      String dir = "src/navalBattle/recursos/ConfiguracionServidor.properties";
      try {
         FileInputStream in = new FileInputStream(dir);
         Properties proper = new Properties();
         proper.load(in);
         in.close();
         FileOutputStream out = new FileOutputStream(dir);
         proper.setProperty("ipServidor", numIp);
         proper.store(out, null);
         //in.close();
      } catch (FileNotFoundException ex) {
         Utileria.cargarAviso("titleAlerta", "mensajeErrorConexion");
         Logger.getLogger(GUI_MenuPartidaController.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
         Utileria.cargarAviso("titleAlerta", "mensajeErrorConexion");
         Logger.getLogger(GUI_MenuPartidaController.class.getName()).log(Level.SEVERE, null, ex);
      }
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
         Logger.getLogger(GUI_MenuPartidaController.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   /**
    * Método para cargar el idioma seleccionado por default en etiquetas y botones
    */
   public void cargarIdioma() {
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle(RECURSO_IDIOMA, locale);
      buttonReglas.setText(resources.getString("buttonReglas"));
      buttonPuntuacion.setText(resources.getString("buttonPuntuacion"));
      buttonCrearPartida.setText(resources.getString("buttonCrearPartida"));
      buttonUnirmePartida.setText(resources.getString("buttonUnirmePartida"));
      labelSalir.setText(resources.getString("labelSalir"));
      labelConfigurar.setText(resources.getString("labelConfigurar"));
      labelIniciando.setText(resources.getString("labelIniciando"));
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
