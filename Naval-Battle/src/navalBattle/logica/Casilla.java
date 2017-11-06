/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.logica;

import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Maribel Tello Rodriguez
 * @author José Alí Valdivia Ruiz
 */
public class Casilla extends Rectangle {
   private int x;
   private int y;
   private Nave nave;
   private boolean atacado;
   private Tablero tablero;

   public Casilla(int x, int y, Tablero tablero) {
      super(50, 50);
      this.tablero = tablero;
      this.x = x;
      this.y = y;
      setFill(Color.LIGHTGRAY);
      setStroke(Color.BLACK);
   }


   public boolean isAtacado() {
      return atacado;
   }

   public void setAtacado(boolean atacado) {
      this.atacado = atacado;
   }


   public Nave getNave() {
      return nave;
   }

   public Tablero getTablero() {
      return tablero;
   }

   public void setNave(Nave nave) {
      this.nave = nave;
   }

   
   
   
}
