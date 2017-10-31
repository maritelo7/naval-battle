/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.logica;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/**
 * Clase para implementar las gestiones de la entidad cuenta
 * @author Maribel Tello Rodriguez
 * @author José Alí Valdivia Ruiz
 */
public class AdministracionCuenta {
//   EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("NavalBattlePU", null);
//   CuentaJpaController controller = new CuentaJpaController(entityManagerFactory);
//   
//   public void registrarCuenta(CuentaUsuario cuentaUsuario) throws NoSuchAlgorithmException{
//      Cuenta cuenta = new Cuenta(cuentaUsuario.getNombreUsuario(), getHash(cuentaUsuario.getClave()), cuentaUsuario.getLenguaje());
//      try {
//         controller.create(cuenta);
//      } catch (Exception ex) {
//         Logger.getLogger(AdministracionCuenta.class.getName()).log(Level.SEVERE, null, ex);
//      }
//   }
   
   private String getHash(String string) throws NoSuchAlgorithmException {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
      byte[] hash = messageDigest.digest(string.getBytes());
      StringBuilder stringBuilder = new StringBuilder();
      for (int i = 0; i < hash.length; i++) {
         stringBuilder.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
      }
      return stringBuilder.toString();
   }
   
   
   
}
