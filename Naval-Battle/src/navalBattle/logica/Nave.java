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
   private int idNave;
   private String descripcion;
   private String nombre;
   private int tamanio;
   private Casilla casillas[];

   public int getIdNave() {
      return idNave;
   }

   public void setIdNave(int idNave) {
      this.idNave = idNave;
   }

   public String getDescripcion() {
      return descripcion;
   }

   public void setDescripcion(String descripcion) {
      this.descripcion = descripcion;
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

   public void setTamanio(int tamanio) {
      this.tamanio = tamanio;
   }

   public Casilla[] getCasillas() {
      return casillas;
   }

   public void setCasillas(Casilla[] casillas) {
      this.casillas = casillas;
   }
   
   

}
