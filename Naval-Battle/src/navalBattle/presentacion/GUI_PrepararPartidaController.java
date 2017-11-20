/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.presentacion;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import navalBattle.logica.Casilla;
import navalBattle.logica.CuentaUsuario;
import navalBattle.logica.Nave;
import navalBattle.logica.Tablero;

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
   
   private boolean esHorizontal = true;
   VBox columna = new VBox();
   int tamanioNave = 0;
   int[] numeroNaves = {3, 2, 2, 1, 1};
   CuentaUsuario cuentaLogueada;

   /**
    * Initializes the controller class.
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {
      
      cargarIdioma();
      cargarTablero();
      buttonContinuar.setOnAction(event -> {
         Tablero tableroEnemigo = recibirTablero();
         if (tableroEnemigo != null) {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Tablero tablero = recuperarTablero();
            try {
               FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI_JugarPartida.fxml"));
               Scene scene = new Scene(loader.load());
               GUI_JugarPartidaController controller = loader.getController();
               controller.cargarCuenta(cuentaLogueada);
               controller.setTableroJugador(tablero);
               controller.setTableroEnemigo(tableroEnemigo);
               loader.setController(controller);
               stage.setScene(scene);
               stage.setResizable(false);
               stage.show();
            } catch (IOException ex) {
               Logger.getLogger(GUI_PrepararPartidaController.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
      });
      buttonRegresar.setOnAction(event -> {
         cargarAviso("titleCancelarPartida", "mensajeCancelarPartida");
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
         Casilla casilla = (Casilla) event.getTarget();
         if (tamanioNave != 0) {
            Nave nave = new Nave(tamanioNave, esHorizontal);
            if (!colocarNave(casilla, nave)) {
               cargarAviso("tittlePosicion", "mensajePosicion");
            } else {
               actualizarNaves();
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
    * @param cuenta la CuentaUsuario con la que se ha iniciado sesión
    */
   public void cargarCuenta(CuentaUsuario cuenta) {
      this.cuentaLogueada = cuenta;
   }

   /**
    * Método para cargar el idioma seleccionado por default en etiquetas y botones
    */
   public void cargarIdioma() {
      
      Locale locale = Locale.getDefault();
      ResourceBundle resources = ResourceBundle.getBundle("navalBattle.recursos.idiomas.Idioma", locale);
      labelColocaNaves.setText(resources.getString("labelColocaNaves"));
      labelRotar.setText(resources.getString("labelRotar"));
      buttonContinuar.setText(resources.getString("buttonContinuar"));
      labelHorizontal.setText(resources.getString("labelHorizontal"));
      labelVertical.setText(resources.getString("labelVertical"));
      labelVertical.setVisible(false);
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
    * Método para verificar que la posición del tablero es válida para colocar una nave con tales
    * dimensiones
    *
    * @param nave Nave seleccionada
    * @param x int Posición x de la casilla seleccionada
    * @param y int Posición y de la casilla selecionada
    * @return Si es posible colocar la nave
    */
   public boolean puedeColocarNave(Nave nave, int x, int y) {
      int tamanio = nave.getTamanio();
      boolean horizontal = nave.isHorizontal();
      if (horizontal) {
         for (int i = x; i < x + tamanio; i++) {
            if (!posicionValida(i, y)) {
               return false;
            }
            Casilla casilla = getCasilla(i, y);
            if (casilla.getNave() != null) {
               return false;
            }
            for (Casilla colindante : getColindantes(i, y)) {
               if (!posicionValida(i, y)) {
                  return false;
               }
               if (colindante.getNave() != null) {
                  return false;
               }
            }
         }

      } else {
         for (int i = y; i < y + tamanio; i++) {
            if (!posicionValida(x, i)) {
               return false;
            }
            Casilla casilla = getCasilla(x, i);
            if (casilla.getNave() != null) {
               return false;
            }
            for (Casilla colindante : getColindantes(x, i)) {
               if (!posicionValida(x, i)) {
                  return false;
               }
               if (colindante.getNave() != null) {
                  return false;
               }
            }
         }
      }
      return true;
   }

   /**
    * Método para verificar si la posición seleccionada es válida
    *
    * @param x int Posición de x de la casilla
    * @param y int Posición de y de la casilla
    * @return Si es posible
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
      return posicionValida(point.getX(), point.getY());
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
      return colindantes.toArray(new Casilla[0]);
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
      if (puedeColocarNave(nave, x, y)) {
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
    * la continuación
    */
   public void actualizarNaves() {
      int restante = 0;
      int sumaRestantes = 0;
      for (int i = 1; i < 6; i++) {
         if (tamanioNave == i) {
            numeroNaves[i - 1]--;
            restante = numeroNaves[i - 1];
         }
         sumaRestantes = sumaRestantes + numeroNaves[i - 1];
      }
      refrescarNumNaves();
      if (restante <= 0) {
         String separator = System.getProperty("file.separator");
         Image image = new Image(getClass().getResourceAsStream(separator + "navalBattle" + separator
             + "recursos" + separator + "imagenes" + separator + "lineaRoja.png"));
         switch (tamanioNave) {
            case 1:
               tamanioNave = 0;
               buttonNave1.setGraphic(new ImageView(image));
               buttonNave1.setDisable(true);
               break;
            case 2:
               tamanioNave = 1;
               buttonNave2.setGraphic(new ImageView(image));
               buttonNave2.setDisable(true);
               break;
            case 3:
               tamanioNave = 2;
               buttonNave3.setGraphic(new ImageView(image));
               buttonNave3.setDisable(true);
               break;
            case 4:
               tamanioNave = 3;
               buttonNave4.setGraphic(new ImageView(image));
               buttonNave4.setDisable(true);
               break;
            case 5:
               tamanioNave = 4;
               buttonNave5.setGraphic(new ImageView(image));
               buttonNave5.setDisable(true);
               break;
         }
      }
      if (sumaRestantes == 0) {
         //Aquí debería enviar al contrincante el tablero que ha creado el jugador
         buttonContinuar.setDisable(false);
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
    * Método para recuperar todas las naves colocadas en un tablero
    *
    * @return tablero con naves
    */
   public Tablero recuperarTablero() {
      int z = 0;
      Tablero tableroJugador = new Tablero(paneTablero, false);
      ArrayList<Casilla> casillas = new ArrayList<>();
      for (int i = 0; i < 10; i++) {
         for (int j = 0; j < 10; j++) {
            casillas.add(getCasilla(j, i));
            z++;
         }
      }
      tableroJugador.setCasillas(casillas);
      return tableroJugador;
   }

   /**
    * Método para recibir el tablero del contrincante
    *
    * @return tablero del contrincante con naves colocadas
    */
   public Tablero recibirTablero() {
      //Aquí debería recibir el objeto tablero del contricante
      Tablero tablero = recuperarTablero();
      return tablero;
   }
}
