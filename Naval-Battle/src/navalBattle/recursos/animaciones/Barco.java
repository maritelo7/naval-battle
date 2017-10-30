/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.recursos.animaciones;

import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * Clase para crear la animación del barco
 * @author Maribel Tello Rodriguez
 * @author José Alí Valdivia Ruiz
 */
public class Barco  {

   private static final Image IMAGE = new Image(Barco.class.getResourceAsStream("/navalBattle/recursos/imagenes/boatMove.png"));

   private static final int COLUMNS = 3;
   private static final int COUNT = 6;
   private static final int OFFSET_X = 8;
   private static final int OFFSET_Y = 0;
   private static final int WIDTH = 464;
   private static final int HEIGHT = 237;

   public ImageView barcoAnimacion() {

      final ImageView imageView = new ImageView(IMAGE);
      imageView.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));

      final Animation animation = new SpriteAnimation(
          imageView,
          Duration.millis(4000),
          COUNT, COLUMNS,
          OFFSET_X, OFFSET_Y,
          WIDTH, HEIGHT
      );
      animation.setCycleCount(Animation.INDEFINITE);
      animation.play();

     return imageView;
   }
}
