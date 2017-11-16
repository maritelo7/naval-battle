/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle;


import java.security.NoSuchAlgorithmException;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author javr
 */
public class NavalBattle extends Application {

   @Override
   public void start(Stage primaryStage) throws Exception {
      Parent root = FXMLLoader.load(getClass().getResource("presentacion/GUI_IniciarSesion.fxml"));
      FadeTransition ft = new FadeTransition(Duration.millis(5000), root);
      ft.setFromValue(0.0);
      ft.setToValue(1.0);
      ft.play();
      Scene scene = new Scene(root);
      primaryStage.setScene(scene);
      primaryStage.setResizable(false);
      primaryStage.show();
   }

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) throws NoSuchAlgorithmException {
      launch(args);
   }
   
}
