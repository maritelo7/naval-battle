/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.logica;

/**
 *
 * @author Maribel Tello Rodriguez
 * @author José Alí Valdivia Ruiz
 */
public class CasillaSimple {
   private final int x;
   private final int y;
   private  Nave nave;
   //private  boolean atacado;

   /**
    * Constructor de Casilla con las posiciones X, Y. Utiliza un contructor del padre y además se
    * colorean para identificarse en el tablero
    * @param x
    * @param y
    */
   public CasillaSimple(int x, int y) {
      this.x = x;
      this.y = y;
   }

   public int getX() {
      return x;
   }

   public int getY() {
      return y;
   }


   public Nave getNave() {
      return nave;
   }
   public void setNave(Nave nave) {
      this.nave = nave;
   }

   @Override
   public String toString() {
      return "CasillaSimple{" + "x=" + x + ", y=" + y + '}';
   }
   
}
