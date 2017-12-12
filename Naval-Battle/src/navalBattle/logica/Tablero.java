/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.logica;

import java.util.ArrayList;


/**
 *
 * @author javr
 */
public class Tablero {
   //private final Pane paneTablero;
   private boolean enemigo;
   private ArrayList<CasillaSimple> casillasSimples = new ArrayList<>();
   private ArrayList<Casilla> casillas = new ArrayList<>();

   public Tablero(boolean enemigo) {
      //this.paneTablero = paneTablero;
      this.enemigo = enemigo;
   }

   public ArrayList<Casilla> getCasillas() {
      return casillas;
   }

   public void setCasillas(ArrayList<Casilla> casillas) {
      this.casillas = casillas;
   }


   public boolean isEnemigo() {
      return enemigo;
   }

   public void setEnemigo(boolean enemigo) {
      this.enemigo = enemigo;
   }

   public ArrayList<CasillaSimple> getCasillasSimples() {
      return casillasSimples;
   }

   public void setCasillasSimples(ArrayList<CasillaSimple> casillasSimples) {
      this.casillasSimples = casillasSimples;
   }
   
   
   
}
