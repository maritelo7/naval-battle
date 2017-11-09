/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.logica;

import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 *
 * @author javr
 */
public class Tablero extends Parent {
   private Pane paneTablero;
   private boolean enemigo;
   private ArrayList<Casilla> casillas = new ArrayList<>();

   public Tablero(Pane paneTablero, boolean enemigo) {
      this.paneTablero = paneTablero;
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
   
   
   
}
