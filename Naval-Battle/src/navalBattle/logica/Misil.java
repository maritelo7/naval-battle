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
public class Misil {
   private final int xDestino;
   private final int yDestino;  

   public Misil(int xDestino, int yDestino) {
      this.xDestino = xDestino;
      this.yDestino = yDestino;
   }

   public int getxDestino() {
      return xDestino;
   }

   public int getyDestino() {
      return yDestino;
   }
   
}
