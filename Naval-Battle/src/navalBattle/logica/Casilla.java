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
public class Casilla {
   private int idCasilla;
   private int x;
   private int y;
   private boolean ocupado;
   private boolean atacado;

   public int getIdCasilla() {
      return idCasilla;
   }

   public void setIdCasilla(int idCasilla) {
      this.idCasilla = idCasilla;
   }

   public int getX() {
      return x;
   }

   public void setX(int x) {
      this.x = x;
   }

   public int getY() {
      return y;
   }

   public void setY(int y) {
      this.y = y;
   }

   public boolean isOcupado() {
      return ocupado;
   }

   public void setOcupado(boolean ocupado) {
      this.ocupado = ocupado;
   }

   public boolean isAtacado() {
      return atacado;
   }

   public void setAtacado(boolean atacado) {
      this.atacado = atacado;
   }
   
}
