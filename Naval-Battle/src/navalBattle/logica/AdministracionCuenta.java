/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.logica;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import navalBattle.datos.Cuenta;
import navalBattle.datos.CuentaJpaController;

/**
 * Clase para implementar las gestiones de la entidad cuenta
 * @author Maribel Tello Rodriguez
 * @author José Alí Valdivia Ruiz
 */
public class AdministracionCuenta implements I_AdministracionCuenta {
   EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Naval-BattlePU", null);
   EntityManager entity = entityManagerFactory.createEntityManager();
   CuentaJpaController controller = new CuentaJpaController(entityManagerFactory);
   
   public boolean registrarCuenta(CuentaUsuario cuentaUsuario) throws NoSuchAlgorithmException{
      boolean registroExitoso = true;
      Cuenta cuenta = new Cuenta(cuentaUsuario.getNombreUsuario(), getHash(cuentaUsuario.getClave()), cuentaUsuario.getLenguaje(), 0);
      try {
         controller.create(cuenta);
      } catch (Exception ex) {
         registroExitoso = false;
         Logger.getLogger(AdministracionCuenta.class.getName()).log(Level.SEVERE, null, ex);         
      }
      return registroExitoso;
   }
   
    public CuentaUsuario consultarCuenta(String nombreUsuario, String clave) throws NoSuchAlgorithmException {
     CuentaUsuario cuentaUsuario = null;
     Cuenta cuentaRecuperada;
     String claveHasheada = getHash(clave);
     try{
     cuentaRecuperada = (Cuenta) entity.createNamedQuery("Cuenta.iniciarSesion").setParameter("nombreUsuario", nombreUsuario).setParameter("clave", claveHasheada).getSingleResult();
     cuentaUsuario = new CuentaUsuario(cuentaRecuperada.getNombreUsuario(), cuentaRecuperada.getClave(), cuentaRecuperada.getLenguaje(), cuentaRecuperada.getPuntaje());
     } catch (Exception ex){
        Logger.getLogger(AdministracionCuenta.class.getName()).log(Level.SEVERE, null, ex);
     }
     return cuentaUsuario;
   }
    
   public boolean modificarCuenta(CuentaUsuario cuentaUsuario) throws NoSuchAlgorithmException{
      boolean modificacionExitosa = true;
      Cuenta cuenta = new Cuenta(cuentaUsuario.getNombreUsuario(), getHash(cuentaUsuario.getClave()), cuentaUsuario.getLenguaje());
      try {
         controller.edit(cuenta);
      } catch (Exception ex) {
         modificacionExitosa = false;
         Logger.getLogger(AdministracionCuenta.class.getName()).log(Level.SEVERE, null, ex);         
      }
      return modificacionExitosa;
   }
   
   public boolean desactivarCuenta(String nombreUsuario) throws NoSuchAlgorithmException{
      boolean cuentaDesactivada = true;
      try {
         controller.destroy(nombreUsuario);
      } catch (Exception ex) {
         cuentaDesactivada = false;
         Logger.getLogger(AdministracionCuenta.class.getName()).log(Level.SEVERE, null, ex);         
      }
      return cuentaDesactivada;
   }
   
    
   public boolean registrarPuntajeMasAlto(CuentaUsuario cuenta, int puntajeObtenido) throws NoSuchAlgorithmException{
      boolean puntajeRegistrado = true;
      if (cuenta.getPuntaje() < puntajeObtenido){
      try {
         cuenta.setPuntaje(puntajeObtenido);
         modificarCuenta(cuenta);
      } catch (Exception ex) {
         puntajeRegistrado = false;
         Logger.getLogger(AdministracionCuenta.class.getName()).log(Level.SEVERE, null, ex);         
      }
      }
      return puntajeRegistrado;
   }      
      
   
   public String getHash(String string) throws NoSuchAlgorithmException {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
      byte[] hash = messageDigest.digest(string.getBytes());
      StringBuilder stringBuilder = new StringBuilder();
      for (int i = 0; i < hash.length; i++) {
         stringBuilder.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
      }
      return stringBuilder.toString();
   }
     
   
}
