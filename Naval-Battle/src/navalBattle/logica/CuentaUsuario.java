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
public class CuentaUsuario {
   private String nombreUsuario;
   private String clave;
   private String lenguaje;
   private int puntaje;
   
   
   public CuentaUsuario(String nombreUsuario, String clave, String lenguaje) {
      this.nombreUsuario = nombreUsuario;
      this.clave = clave;
      this.lenguaje = lenguaje;
   }
   
   public CuentaUsuario(String nombreUsuario, String clave, String lenguaje, int puntaje) {
      this.nombreUsuario = nombreUsuario;
      this.clave = clave;
      this.lenguaje = lenguaje;
      this.puntaje = puntaje;
   }

   public CuentaUsuario(String nombreUsuario, String clave) {
      this.nombreUsuario = nombreUsuario;
      this.clave = clave;
   }

   public String getLenguaje() {
      return lenguaje;
   }

   public void setLenguaje(String lenguaje) {
      this.lenguaje = lenguaje;
   }

   public String getNombreUsuario() {
      return nombreUsuario;
   }

   public void setNombreUsuario(String nombreUsuario) {
      this.nombreUsuario = nombreUsuario;
   }

   public String getClave() {
      return clave;
   }

   public void setClave(String clave) {
      this.clave = clave;
   }

   public int getPuntaje() {
      return puntaje;
   }

   public void setPuntaje(int puntaje) {
      this.puntaje = puntaje;
   }
   
   
   
   
   
}
