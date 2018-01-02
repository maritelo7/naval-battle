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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import navalBattle.logica.Casilla;
import navalBattle.logica.CasillaSimple;
import navalBattle.logica.CuentaUsuario;
import navalBattle.logica.InteraccionServidor;
import navalBattle.logica.Nave;
import navalBattle.logica.Tablero;
import navalBattle.logica.TableroSimple;
import navalBattle.recursos.Utileria;

/**
 * FXML Controller class
 *
 * @author Maribel Tello Rodriguez
 * @author José Alí Valdivia Ruiz
 */
public class GUI_PrepararPartidaController implements Initializable {

   @FXML
   private Label labelColocaNaves;
   @FXML
   private Label labelRotar;
   @FXML
   private JFXButton buttonContinuar;
   @FXML
   private JFXButton buttonRegresar;
   @FXML
   private Pane paneTablero;
   @FXML
   private JFXButton buttonNave5;
   @FXML
   private JFXButton buttonNave4;
   @FXML
   private JFXButton buttonNave3;
   @FXML
   private JFXButton buttonNave2;
   @FXML
   private JFXButton buttonNave1;
   @FXML
   private JFXButton buttonRotar;
   @FXML
   private JFXButton buttonLimpiar;
   @FXML
   private Label labelNumNave5;
   @FXML
   private Label labelNumNave4;
   @FXML
   private Label labelNumNave3;
   @FXML
   private Label labelNumNave2;
   @FXML
   private Label labelNumNave1;
   @FXML
   private Label labelHorizontal;
   @FXML
   private Label labelVertical;
   @FXML
   private Label labelEspera;

   private boolean esHorizontal = true;
   private final VBox columna = new VBox();
   private int tamanioNave = 0;
   private final int[] numeroNaves = {3, 2, 2, 1, 1};
   private CuentaUsuario cuentaLogueada;
   private final static String RECURSO_IDIOMA = "navalBattle.recursos.idiomas.Idioma";
   private InteraccionServidor interaccionServidor = new InteraccionServidor();
   private GUI_PrepararPartidaController controller;
   private Tablero tableroEnemigo = new Tablero(true);
   private final TableroSimple tableroEnemigoSimple = new TableroSimple(true);
   private boolean ready = false;
   private boolean soyHost = false;
   private String nombreAdversario;
   private Event ultimoEvent;
   /**
    * Initializes the controller class.
    * 
    * @param url
    * @param rb
    */
   @Override
   public void initialize(URL url, ResourceBundle rb)  {
      cargarIdioma();
      cargarTablero();
      buttonContinuar.setOnAction(event -> {
         Node node = (Node) event.getSource();
         Stage stage = (Stage) node.getScene().getWindow();
         Tablero tablero = recuperarTableroGrafico();
         try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI_JugarPartida.fxml"));
            Scene scene = new Scene(loader.load());
            GUI_JugarPartidaController controller = loader.getController();
            controller.cargarCuenta(cuentaLogueada);
            controller.setTableroJugador(tablero);
            controller.setTableroEnemigo(tableroEnemigo);
            controller.ajustarMiTurno(soyHost);
            controller.setInteraccionServidor(interaccionServidor);
            controller.setNombreAdversario(nombreAdversario);
            controller.cargarController(controller);
            controller.activarServicios();
            loader.setController(controller);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
         } catch (IOException ex) {
            Logger.getLogger(GUI_PrepararPartidaController.class.getName()).log(Level.SEVERE, null, ex);
         }

      });
      buttonRegresar.setOnAction(event -> {
         Utileria.cargarAviso("titleAlerta", "mensajeCancelarPartida");
         if (nombreAdversario != null) {
            interaccionServidor.dejarAdversario(cuentaLogueada.getNombreUsuario(),nombreAdversario);
            interaccionServidor.notificarAbandonoAdversario(nombreAdversario);
         }
         Node node = (Node) event.getSource();
         Stage stage = (Stage) node.getScene().getWindow();
         try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI_MenuPartida.fxml"));
            Scene scene = new Scene(loader.load());
            GUI_MenuPartidaController controller = loader.getController();
            controller.cargarCuenta(cuentaLogueada);
            loader.setController(controller);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
         } catch (IOException ex) {
            Logger.getLogger(GUI_PrepararPartidaController.class.getName()).log(Level.SEVERE, null, ex);
         }
      });
      buttonNave5.setOnAction(event -> {
         tamanioNave = 5;
      });
      buttonNave4.setOnAction(event -> {
         tamanioNave = 4;
      });
      buttonNave3.setOnAction(event -> {
         tamanioNave = 3;
      });
      buttonNave2.setOnAction(event -> {
         tamanioNave = 2;
      });
      buttonNave1.setOnAction(event -> {
         tamanioNave = 1;
      });
      paneTablero.setOnMouseClicked(event -> {
         ultimoEvent = event;
         if ( tamanioNave != 0) {
            Casilla casilla = (Casilla) event.getTarget();
            Nave nave = new Nave(tamanioNave, esHorizontal);
            if (!colocarNave(casilla, nave)) {
               Utileria.cargarAviso("titlePosicion", "mensajePosicion");
            } else {
               try {
                  actualizarNaves();
               } catch (InterruptedException ex) {
                  Logger.getLogger(GUI_PrepararPartidaController.class.getName()).log(Level.SEVERE, null, ex);
               }
   
            }
         }

      });
      buttonRotar.setOnAction(event -> {
         intercambiarLabelOrientacion();
      });
      buttonLimpiar.setOnAction(event -> {
         limpiarTablero();
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
    * Método para cargar la instancia de la interacción del servidor creada en Menu partida
    * @param interaccionServidor instancia de la clase InteraccionServidor
    */
   public void cargarInteraccionServidor(InteraccionServidor interaccionServidor) {
      this.interaccionServidor = interaccionServidor;
   }

   /**
    * Método para cargar el controller de la ventana y así acceder dicha clase desde un evento externo
    * @param controller controlador de esta clase GUI_PrepararPartidaController
    */
   public void cargarController(GUI_PrepararPartidaController controller) {
      this.controller = controller;
   }

   /**
    * Método para asignar el tablero enemigo
    * @param tableroEnemigo tablero del adversario
    */
   public void setTableroEnemigo(Tablero tableroEnemigo){
      this.tableroEnemigo = tableroEnemigo;
   }

   /**
    * Método para cargar el tablero con casillas y las etiquetas de las naves
    */
    public void cargarTablero() {
      for (int i = 0; i < 10; i++) {
         HBox fila = new HBox();
         for (int j = 0; j < 10; j++) {
            Casilla casilla = new Casilla(j, i);
            casilla.setX(j);
            casilla.setY(i);
            fila.getChildren().add(casilla);
         }
         columna.getChildren().add(fila);
      }
      paneTablero.getChildren().add(columna);
      labelNumNave5.setText(String.valueOf(numeroNaves[4]));
      labelNumNave4.setText(String.valueOf(numeroNaves[3]));
      labelNumNave3.setText(String.valueOf(numeroNaves[2]));
      labelNumNave2.setText(String.valueOf(numeroNaves[1]));
      labelNumNave1.setText(String.valueOf(numeroNaves[0]));
      buttonContinuar.setDisable(true);
   }

   /**
    * Método para verificar que la posición del tablero es válida para colocar una nave con tales
    * dimensiones
    *
    * @param nave Nave seleccionada
    * @param casilla Casilla seleccionada para colocar nave
    * @return Si es posible colocar la nave
    */
   public boolean puedeColocarNave(Nave nave, Casilla casilla) {
      int tamanio = nave.getTamanio();
      boolean horizontal = nave.isHorizontal();
      if (horizontal) {
         return colocarHorizontal(casilla, tamanio);
      }
      return colocarVertical(casilla, tamanio);
   }

   /**
    * Método para comprobar si es posible colocar una nave de forma horizontal
    *
    * @param casillaInicio casilla en donde inicia la nave
    * @param tamanio tamaño de la nave
    * @return boolean que es true si se puede colocar la nave
    */
   public boolean colocarHorizontal(Casilla casillaInicio, int tamanio) {
      int x = (int) casillaInicio.getX();
      int y = (int) casillaInicio.getY();
      Casilla casilla;
      for (int i = x; i < x + tamanio; i++) {
         if (!posicionValida(i, y)) {
            return false;
         }
         casilla = getCasilla(i, y);
         if (casilla.getNave() != null) {
            return false;
         }
         if (!comprobarColindantesHorizontal(i, y)) {
            return false;
         }
      }
      
      return true;
   }

   /**
    * Método para comprobar si los colindantes de la casilla son aceptables
    *
    * @param i valor variable de la posición de X
    * @param y valor de y de la casilla
    * @return regresa los colindantes son aptos para colocar la nave
    */
   public boolean comprobarColindantesHorizontal(int i, int y) {   
      for (Casilla colindante : getColindantes(i, y)) {
         if (!posicionValida(i, y)) {
            return false;   
         }
         if (colindante.getNave() != null) {
            return false;
         }
      }  
      return true;
   }

   /**
    * Método para comprobar si se puede colocar una nave en vertical
    *
    * @param casillaInicio casilla en donde inicia la nave a colocar
    * @param tamanio tamaño de la nave
    * @return regresa se se puede colocar la nave en dicha posición
    */
   public boolean colocarVertical(Casilla casillaInicio, int tamanio) {
      int x = (int) casillaInicio.getX();
      int y = (int) casillaInicio.getY();
      for (int i = y; i < y + tamanio; i++) {
         if (!posicionValida(x, i)) {
            return false;
         }
         Casilla casilla = getCasilla(x, i);
         if (casilla.getNave() != null) {
            return false;
         }
         if (!comprobarColindantesVertical(x, i)) {
            return false;
         }
      }
      return true;
   }

   /**
    * Método para comprobar los colindantes de una casilla
    *
    * @param x valor de X de la casilla
    * @param i valor varibale de Y de la casilla
    * @return si las casillas colindantes son aptas para colocar la nave
    */
   public boolean comprobarColindantesVertical(int x, int i) {     
      for (Casilla colindante : getColindantes(x, i)) {
         if (!posicionValida(x, i)) {
            return false;
         }
         if (colindante.getNave() != null) {
            return false;
         }
      }
      return true;
   }

   /**
    * Método para verificar si la posición seleccionada es válida
    *
    * @param x Posición de x de la casilla
    * @param y Posición de y de la casilla
    * @return regresa true si la la posición es válida
    */
   public boolean posicionValida(double x, double y) {
      return x >= 0 && x < 10 && y >= 0 && y < 10;
   }

   /**
    * Método para verificar si los puntos que se dan son posiciones válidas
    *
    * @param point Punto con valores X y Y en tablero
    * @return regresa si es posible
    */
   public boolean posicionValida(Point2D point) {
      return point.getX() >= 0 && point.getX() < 10 && point.getY() >= 0 && point.getY() < 10;
   }

   /**
    * Método para obtener una casilla del tablero
    *
    * @param x posición x de la casilla
    * @param y posición y de la casilla
    * @return Casilla con tales posiciones
    */
   public Casilla getCasilla(int x, int y) {
      return (Casilla) ((HBox) columna.getChildren().get(y)).getChildren().get(x);
   }

   /**
    * Método para obtener las casillas colidantes de un punto
    *
    * @param x posición de x de la casilla
    * @param y posición de y de la casilla
    * @return Arreglo de casillas colindantes
    */
   public Casilla[] getColindantes(int x, int y) {
      Point2D[] points = new Point2D[]{
         new Point2D(x - 1, y),
         new Point2D(x + 1, y),
         new Point2D(x + 1, y + 1),
         new Point2D(x - 1, y + 1),
         new Point2D(x, y - 1),
         new Point2D(x, y + 1),
         new Point2D(x + 1, y - 1),
         new Point2D(x - 1, y - 1),};
      List<Casilla> colindantes = new ArrayList<>();
      for (Point2D p : points) {
         if (posicionValida(p)) {
            colindantes.add(getCasilla((int) p.getX(), (int) p.getY()));
         }
      }
      return colindantes.toArray(new Casilla[colindantes.size()]);
   }

   /**
    * Método para colocar la nave en el tablero
    *
    * @param casillaInicio Casilla en donde inicia la nave
    * @param nave nave a colocar
    * @return Si ha logrado colocarla
    */
   public boolean colocarNave(Casilla casillaInicio, Nave nave) {
      int x = (int) casillaInicio.getX();
      int y = (int) casillaInicio.getY();
      if (puedeColocarNave(nave, casillaInicio)) {
         boolean horizontal = nave.isHorizontal();
         int tamanio = nave.getTamanio();
         if (horizontal) {
            for (int i = x; i < x + tamanio; i++) {
               Casilla casilla = getCasilla(i, y);
               casilla.setNave(nave);
               casilla.setFill(Color.ORANGE);
            }
         } else {
            for (int i = y; i < y + tamanio; i++) {
               Casilla casilla = getCasilla(x, i);
               casilla.setNave(nave);
               casilla.setFill(Color.ORANGE);
            }
         }
         return true;
      }
      return false;
   }

   /**
    * Método para actualizar los números de naves disposibles y en caso de colocar todas habilitar
    * el botón de continuación para jugar la partida
    * @throws java.lang.InterruptedException ocurre cuando el hilo es interrumpido
    */
   public void actualizarNaves() throws InterruptedException {
      int restante = 0;
      for (int i = 1; i < 6; i++) {
         if (tamanioNave == i) {
            numeroNaves[i - 1]--;
            restante = numeroNaves[i - 1];
         }
      }      
      refrescarNumNaves();
      if (restante == 0) {
         Image image = new Image(getClass().getResourceAsStream("/navalBattle/recursos/imagenes/lineaRoja.png"));
         switch (tamanioNave) {
            case 1:
               tamanioNave = 0;
               buttonNave1.setGraphic(new ImageView(image));
               buttonNave1.setDisable(true);
               break;
            case 2:
               tamanioNave = 0;
               buttonNave2.setGraphic(new ImageView(image));
               buttonNave2.setDisable(true);
               break;
            case 3:
               tamanioNave = 0;
               buttonNave3.setGraphic(new ImageView(image));
               buttonNave3.setDisable(true);
               break;
            case 4:
               tamanioNave = 0;
               buttonNave4.setGraphic(new ImageView(image));
               buttonNave4.setDisable(true);
               break;
            case 5:
               tamanioNave = 0;
               buttonNave5.setGraphic(new ImageView(image));
               buttonNave5.setDisable(true);
               break;
            default:
               tamanioNave = 0;
         }
      }
      checkListo();
   }

   /**
    * Método para verificar si ya se encuentra el tablero listo para avanzar
    * @throws InterruptedException ocurre cuando el hilo es interrumpido
    */
   public void checkListo() throws InterruptedException {
      int sumaNavesRestantes = 0;
      for (int i = 1; i < 6; i++) {
         sumaNavesRestantes = sumaNavesRestantes + numeroNaves[i - 1];
      }
      if (sumaNavesRestantes == 0) {
         if (!soyHost) {
            notificarHost();
         }
         if (ready) {
            enviarTablero();
            buttonContinuar.setDisable(false);
         }
      }
   }

   /**
    * Método para recuperar todas las naves colocadas en un tablero
    *
    * @return tablero con naves
    */
   public TableroSimple recuperarTablero() {      
      TableroSimple tableroJugadorSimple = new TableroSimple(false);
      ArrayList<CasillaSimple> casillasSimples = new ArrayList<>();
      Casilla casilla;
      CasillaSimple casillaSimple;
      for (int i = 0; i < 10; i++) {
         for (int j = 0; j < 10; j++) {
            casilla = (getCasilla(j, i));
            casillaSimple = new CasillaSimple(j,i);
            casillaSimple.setNave(casilla.getNave());
            casillasSimples.add(casillaSimple);
         }
      }
      tableroJugadorSimple.setCasillasSimples(casillasSimples);
      return tableroJugadorSimple;
   }

   /**
    * Método para recuperar el tablero con todos sus elementos gráficos
    * @return el tablero del jugador
    */
   public Tablero recuperarTableroGrafico(){
      Tablero tableroJugador = new Tablero(false);
      ArrayList<Casilla> casillas = new ArrayList<>();
      Casilla casilla;
      for (int i = 0; i < 10; i++) {
         for (int j = 0; j < 10; j++) {
            casilla = getCasilla(j, i);
            casillas.add(casilla);
         }
      }
      tableroJugador.setCasillas(casillas);
      return tableroJugador;
   }

   /**
    * Método para activar la espera del servicio para recibir un adversario
    */
   public void activarEspera() {
      interaccionServidor.esperarAInvitado(controller);
      labelEspera.setVisible(true);
      Utileria.fadeConteo(labelEspera);
      soyHost = true;
   }

   /**
    * Método para activar la espera del tablero enemigo
    */
   public void activarRecibirTablero(){
      interaccionServidor.esperarTablero(controller);
   }
   
   /**
    * Método para notificar al adversario que el jugador ya terminó de preparar el tablero
    */
   public void notificarHost(){
      interaccionServidor.adversarioListo(cuentaLogueada.getNombreUsuario());
   }
   public void notificarAbandono() {
      Utileria.cargarAviso("titleAlerta", "mensajeAbandono");
      regresarAMenu(ultimoEvent);
   }
   /**
    * Método para envíar el tablero al adversario
    */
   public void enviarTablero() {
      TableroSimple tableroJugador = recuperarTablero();
      interaccionServidor.enviarTablero(cuentaLogueada.getNombreUsuario(), tableroJugador);
   }

   /**
    * Método para describir la orientación actual de la nave
    */
   public void intercambiarLabelOrientacion() {
      if (esHorizontal) {
         labelHorizontal.setVisible(false);
         labelVertical.setVisible(true);
         esHorizontal = false;
      } else {
         labelVertical.setVisible(false);
         labelHorizontal.setVisible(true);
         esHorizontal = true;
      }
   }

   /**
    * Método para refrescar todos los valores de las etiquetas de las naves
    */
   public void refrescarNumNaves() {
      labelNumNave1.setText(String.valueOf(numeroNaves[0]));
      labelNumNave2.setText(String.valueOf(numeroNaves[1]));
      labelNumNave3.setText(String.valueOf(numeroNaves[2]));
      labelNumNave4.setText(String.valueOf(numeroNaves[3]));
      labelNumNave5.setText(String.valueOf(numeroNaves[4]));
   }

   /**
    * Método para limpiar el tablero, permite volver a colocar todas las naves
    */
   public void limpiarTablero() {
      for (int i = 0; i < 10; i++) {
         for (int j = 0; j < 10; j++) {
            Casilla casilla = getCasilla(i, j);
            casilla.setFill(Color.LIGHTGRAY);
            casilla.setNave(null);
         }
      }
      numeroNaves[4] = 1;
      numeroNaves[3] = 1;
      numeroNaves[2] = 2;
      numeroNaves[1] = 2;
      numeroNaves[0] = 3;
      buttonContinuar.setDisable(true);
      refrescarNumNaves();
      activarBotonesNaves();
   }

   /**
    * Método para activar los botones y volver a colocar las naves en el tablero
    */
   public void activarBotonesNaves() {
      buttonNave5.setGraphic(null);
      buttonNave4.setGraphic(null);
      buttonNave3.setGraphic(null);
      buttonNave2.setGraphic(null);
      buttonNave1.setGraphic(null);
      buttonNave5.setDisable(false);
      buttonNave4.setDisable(false);
      buttonNave3.setDisable(false);
      buttonNave2.setDisable(false);
      buttonNave1.setDisable(false);
   }

   /**
    * Método para cargar el idioma seleccionado por default en etiquetas y botones
    */
   public void cargarIdioma() {
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle(RECURSO_IDIOMA, locale);
      labelColocaNaves.setText(resources.getString("labelColocaNaves"));
      labelRotar.setText(resources.getString("labelRotar"));
      buttonContinuar.setText(resources.getString("buttonContinuar"));
      buttonLimpiar.setText(resources.getString("buttonLimpiar"));
      labelHorizontal.setText(resources.getString("labelHorizontal"));
      labelVertical.setText(resources.getString("labelVertical"));
      labelVertical.setVisible(false);
      labelEspera.setText(resources.getString("labelEspera"));
      labelEspera.setVisible(false);
   }

   /**
    * Método para activar si se puede avanzar de ventana
    */
   public void activarReady(){
      this.ready = true;
   }
   /**
    * Método para regresar al menú de la partida en caso de finalizar la partida
    *
    * @param event evento que desencadena un cambio de ventana
    */
   public void regresarAMenu(Event event) {
      Node node = (Node) event.getSource();
      Stage stage = (Stage) node.getScene().getWindow();
      try {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI_MenuPartida.fxml"));
         Scene scene = new Scene(loader.load());
         GUI_MenuPartidaController controller = loader.getController();
         controller.cargarCuenta(cuentaLogueada);
         loader.setController(controller);
         stage.setScene(scene);
         stage.setResizable(false);
         stage.show();
      } catch (IOException ex) {
         Logger.getLogger(GUI_JugarPartidaController.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
   /**
    * Método para asignar el nombre del jugador adversario
    * @param nombre nickname del jugador adversario
    */
   public void setNombreAdversario(String nombre){
      this.nombreAdversario = nombre;
   }

}
