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
public class Nave {
   private String nombre;
   private int tamanio;
   private boolean horizontal;
   private int salud;


   /**
    * Constructor principal de Nave. Se asigna la salud de la nave del mismo valor del tamaño para poder
    * calcular posteriormente si no todas sus casillas han sido destruidas
    * @param tamanio
    * @param horizontal
    */
   public Nave(int tamanio, boolean horizontal) {
      this.tamanio = tamanio;
      this.horizontal = horizontal;
      this.salud = tamanio;
   }
   

   public String getNombre() {
      return nombre;
   }

   public void setNombre(String nombre) {
      this.nombre = nombre;
   }

   public int getTamanio() {
      return tamanio;
   }
   
   public boolean isHorizontal() {
      return horizontal;
   }

   /**
    * Método para comprobar que la salud (numero de casillas de la nave sin ser atacadas) aún permanece
    * @return bandera si aún tiene casillas sin atacar
    */
   public boolean isViva() {
      return salud > 0;
   }

   /**
    * Método para reducir la salud de la nave
    */
   public void atacada(){
      salud--;
   }

}
