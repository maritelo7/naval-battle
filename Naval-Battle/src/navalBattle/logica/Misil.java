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
   private int idMisil; //Necesario?
   private int xDestino;
   private int yDestino;  

   public Misil(int xDestino, int yDestino) {
      this.xDestino = xDestino;
      this.yDestino = yDestino;
   }

   public int getIdMisil() {
      return idMisil;
   }

   public void setIdMisil(int idMisil) {
      this.idMisil = idMisil;
   }

   public int getxDestino() {
      return xDestino;
   }

   public void setxDestino(int xDestino) {
      this.xDestino = xDestino;
   }

   public int getyDestino() {
      return yDestino;
   }

   public void setyDestino(int yDestino) {
      this.yDestino = yDestino;
   }
   
   
}
