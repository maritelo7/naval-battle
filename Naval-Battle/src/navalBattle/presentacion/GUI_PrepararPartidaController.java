/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.presentacion;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import javafx.scene.text.Font;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import navalBattle.logica.Casilla;
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
   private Font x1; //No tengo idea qué hace :l
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
   int naves5 = 1;
   int naves4 = 1;
   int naves3 = 2;
   int naves2 = 2;
   int naves1 = 3;
   private boolean horizontal = true;
   VBox columna = new VBox();
   Nave nave = new Nave();

   /**
    * Initializes the controller class.
    */
   @Override
   public void initialize(URL url, ResourceBundle rb) {

      cargarIdioma();
      cargarTablero();
      buttonContinuar.setOnAction(event -> {
         try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Parent root;
            root = FXMLLoader.load(getClass().getResource("GUI_JugarPartida.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
         } catch (IOException ex) {
            Logger.getLogger(GUI_PrepararPartidaController.class.getName()).log(Level.SEVERE, null, ex);
         }
      });
      buttonRegresar.setOnAction(event -> {
         //Aquí se debe cerrar la conexión y avisar al otro jugador
         try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Parent root;
            root = FXMLLoader.load(getClass().getResource("GUI_MenuPartida.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
         } catch (IOException ex) {
            Logger.getLogger(GUI_PrepararPartidaController.class.getName()).log(Level.SEVERE, null, ex);
         }
      });
      buttonNave5.setOnAction(event -> {
         if (naves5 > 0) {
            nave.setTamanio(5);
         }
      });
      buttonNave4.setOnAction(event -> {
         if (naves4 > 0) {
            nave.setTamanio(4);
         }

      });
      buttonNave3.setOnAction(event -> {
         if (naves3 > 0) {
            nave.setTamanio(3);
         }

      });
      buttonNave2.setOnAction(event -> {
         if (naves2 > 0) {
            nave.setTamanio(2);
         }

      });
      buttonNave1.setOnAction(event -> {
         if (naves1 > 0) {
            nave.setTamanio(1);
         }

      });
      paneTablero.setOnMouseClicked(event -> {
         Casilla casilla = (Casilla) event.getTarget();
//         casilla.setFill(Color.DARKCYAN);
//         System.out.println("X "+casilla.getX());
//         System.out.println("Y "+casilla.getY());
         if (nave != null) {
            if (!colocarNave(casilla)) {
               //Aviso de posición incorrecta
               cargarAviso("tittlePosicion", "mensajePosicion");
            } else {
               actualizarNaves();
            }
         }

      });
      buttonRotar.setOnAction(event -> {
         nave.setHorizontal(!horizontal);
         if (nave.isHorizontal()) {
            labelHorizontal.setVisible(true);
            labelVertical.setVisible(false);
         } else {
            labelVertical.setVisible(true);
            labelHorizontal.setVisible(false);
         }
      });
      buttonLimpiar.setOnAction(event -> {
         limpiarTablero();
      });

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

   public void cargarTablero() {

      Tablero tablero = new Tablero(paneTablero, false);
      //EventHandler<? super MouseEvent> handler = null;
      for (int i = 0; i < 10; i++) {
         HBox fila = new HBox();
         for (int j = 0; j < 10; j++) {
            Casilla casilla = new Casilla(j, i, tablero);
            casilla.setX(j);
            casilla.setY(i);
            //casilla.setOnMouseClicked(handler);
            fila.getChildren().add(casilla);
         }
         columna.getChildren().add(fila);
      }
      paneTablero.getChildren().add(columna);
      labelNumNave5.setText(String.valueOf(naves5));
      labelNumNave4.setText(String.valueOf(naves4));
      labelNumNave3.setText(String.valueOf(naves3));
      labelNumNave2.setText(String.valueOf(naves2));
      labelNumNave1.setText(String.valueOf(naves1));
      buttonContinuar.setDisable(true);
   }

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
      Optional<ButtonType> eleccion = confirmacion.showAndWait();
   }

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

   public boolean posicionValida(double x, double y) {
      if (x >= 0 && x < 10 && y >= 0 && y < 10) {
         return true;
      }
      return false;
   }

   public boolean posicionValida(Point2D point) {
      return posicionValida(point.getX(), point.getY());
   }

   public Casilla getCasilla(int x, int y) {
      return (Casilla) ((HBox) columna.getChildren().get(y)).getChildren().get(x);
   }

   public Casilla[] getColindantes(int x, int y) {
      Point2D[] points = new Point2D[]{
         new Point2D(x - 1, y),
         new Point2D(x + 1, y),
         new Point2D(x, y - 1),
         new Point2D(x, y + 1)
      };
      List<Casilla> colindantes = new ArrayList<Casilla>();
      for (Point2D p : points) {
         if (posicionValida(p)) {
            colindantes.add(getCasilla((int) p.getX(), (int) p.getY()));
         }
      }
      return colindantes.toArray(new Casilla[0]);
   }

   public boolean colocarNave(Casilla casillaInicio) {
      int x = (int) casillaInicio.getX();
      int y = (int) casillaInicio.getY();
      if (puedeColocarNave(nave, x, y)) {
         boolean horizontal = nave.isHorizontal();
         int tamanio = nave.getTamanio();
         if (horizontal) {
            for (int i = x; i < x + tamanio; i++) {
               Casilla casilla = getCasilla(i, y);
               casilla.setNave(nave);
               casilla.setFill(Color.AQUAMARINE);
            }
         } else {
            for (int i = y; i < y + tamanio; i++) {
               Casilla casilla = getCasilla(x, i);
               casilla.setNave(nave);
               casilla.setFill(Color.AQUAMARINE);
            }
         }
         return true;
      }
      return false;
   }

   public void actualizarNaves() {
      int restante = 0;
      switch (nave.getTamanio()) {
         case 1:
            naves1--;
            labelNumNave1.setText(String.valueOf(naves1));
            restante = naves1;
            break;
         case 2:
            naves2--;
            labelNumNave2.setText(String.valueOf(naves2));
            restante = naves2;
            break;
         case 3:
            naves3--;
            labelNumNave3.setText(String.valueOf(naves3));
            restante = naves3;
            break;
         case 4:
            naves4--;
            labelNumNave4.setText(String.valueOf(naves4));
            restante = naves4;
            break;
         case 5:
            naves5--;
            labelNumNave5.setText(String.valueOf(naves5));
            restante = naves5;
            break;
      }
      System.out.println("Resta " + restante);
      if (restante <= 0) {
         switch (nave.getTamanio()) {
            case 1:
               nave = null;
               buttonNave1.setDisable(true);
               break;
            case 2:
               nave = null;
               buttonNave2.setDisable(true);
               break;
            case 3:
               nave = null;
               buttonNave3.setDisable(true);
               break;
            case 4:
               nave = null;
               buttonNave4.setDisable(true);
               break;
            case 5:
               nave = null;
               buttonNave5.setDisable(true);
               break;
         }
      }
      if ((naves5 + naves4 + naves3 + naves2 + naves1) == 0) {
         buttonContinuar.setDisable(false);
      }
   }

   public void limpiarTablero() {
      for (int i = 0; i < 10; i++) {
         for (int j = 0; j < 10; j++) {
            Casilla casilla = getCasilla(i, j);
            casilla.setFill(Color.LIGHTGRAY);
            casilla.setNave(null);
         }
      }
   }
}
