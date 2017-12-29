/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle;

import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import navalBattle.presentacion.GUI_IniciarSesionController;

/**
 *
 * @author Maribel Tello Rodriguez
 * @author José Alí Valdivia Ruiz
 */
public class NavalBattle extends Application {

   @Override
   public void start(Stage primaryStage) throws IOException  {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("presentacion/GUI_IniciarSesion.fxml"));
      Scene scene = new Scene(loader.load());
      GUI_IniciarSesionController controller = loader.getController();
      controller.cargarSonido(true);
      loader.setController(controller);
      FadeTransition ft = new FadeTransition(Duration.millis(5000), scene.getRoot());
      ft.setFromValue(0.0);
      ft.setToValue(1.0);
      ft.play();
      primaryStage.setScene(scene);
      primaryStage.setResizable(false);
      primaryStage.show();
   }

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) {
      launch(args);
   }

}
