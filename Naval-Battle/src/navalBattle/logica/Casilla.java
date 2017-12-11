/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.logica;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Maribel Tello Rodriguez
 * @author José Alí Valdivia Ruiz
 */
public class Casilla extends Rectangle {
   private final int x;
   private final int y;
   private  Nave nave;
   private  boolean atacado;

   /**
    * Constructor de Casilla con las posiciones X, Y. Utiliza un contructor del padre y además se
    * colorean para identificarse en el tablero
    * @param x
    * @param y
    */
   public Casilla(int x, int y) {
      super(50, 50);
      this.x = x;
      this.y = y;
      setFill(Color.LIGHTGRAY);
      setStroke(Color.BLACK);
   }

   /**
    * Método para liberar la casilla (colorear en azul). Esto es en caso de no estar relacionada a una
    * nave
    */
   public void liberar(){
      setFill(Color.DEEPSKYBLUE);
      setStroke(Color.BLUE);
   }

   public boolean isAtacado() {
      return atacado;
   }

   /**
    * Método para realizar las acciones (marcar como ataque a un barco o caída en agua) en caso de 
    * ser atacada la casilla
    * @return bandera en caso de haber atacado a una casilla con nave
    */
   public boolean atacadaANave() {
      this.atacado = true;
      if (this.nave != null) {
         nave.atacada();
         setFill(Color.RED);
         return true;
      } else {
         liberar();
      }
      return false;
   }
   public Nave getNave() {
      return nave;
   }
   public void setNave(Nave nave) {
      this.nave = nave;
   }
   
}
