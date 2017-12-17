/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.logica;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import navalBattle.datos.Cuenta;
import navalBattle.datos.CuentaJpaController;
import navalBattle.datos.exceptions.NonexistentEntityException;
import navalBattle.datos.exceptions.PreexistingEntityException;

/**
 * Clase para implementar las gestiones de la entidad cuenta
 *
 * @author Maribel Tello Rodriguez
 * @author José Alí Valdivia Ruiz
 */
public class AdministracionCuenta implements I_AdministracionCuenta {

   final static String UNIDAD_PERSISTENCIA = "Naval-BattlePU";

   /**
    * Método para registrar una Cuenta en la base de datos
    *
    * @param cuentaUsuario la cuenta a registrar
    * @throws navalBattle.datos.exceptions.PreexistingEntityException
    * @throws java.security.NoSuchAlgorithmException
    */
   @Override
   public void registrarCuenta(CuentaUsuario cuentaUsuario) throws PreexistingEntityException, NoSuchAlgorithmException {
      EntityManagerFactory entityManagerFactory;
      entityManagerFactory = Persistence.createEntityManagerFactory(UNIDAD_PERSISTENCIA, null);
      CuentaJpaController controller = new CuentaJpaController(entityManagerFactory);
      Cuenta cuenta = new Cuenta(cuentaUsuario.getNombreUsuario(), getHash(cuentaUsuario.getClave()),
          cuentaUsuario.getLenguaje(), 0);
      controller.create(cuenta);
   }

   /**
    * Método para recuperar un objeto CuentaUsuario de la base de datos
    *
    * @param nombreUsuario el nickname de la cuenta
    * @param clave la contraseña de la cuenta de usuario
    * @return la cuenta recuperada
    * @throws java.security.NoSuchAlgorithmException
    */
   @Override
   public CuentaUsuario consultarCuenta(String nombreUsuario, String clave) throws NoSuchAlgorithmException {
      CuentaUsuario cuentaUsuario = null;
      Cuenta cuentaRecuperada;
      EntityManagerFactory entityManagerFactory;
      entityManagerFactory = Persistence.createEntityManagerFactory(UNIDAD_PERSISTENCIA, null);
      EntityManager entity = entityManagerFactory.createEntityManager();
      String claveHasheada = getHash(clave);
      cuentaRecuperada = (Cuenta) entity.createNamedQuery("Cuenta.iniciarSesion").setParameter("nombreUsuario", nombreUsuario).setParameter("clave", claveHasheada).getResultList().get(0);
      if (cuentaRecuperada != null ) {
          cuentaUsuario = new CuentaUsuario(cuentaRecuperada.getNombreUsuario(), cuentaRecuperada.getClave(),
          cuentaRecuperada.getLenguaje(), cuentaRecuperada.getPuntaje());
      } else {
         cuentaUsuario = new CuentaUsuario("0","0","0");
      }
      return cuentaUsuario;
   }

   /**
    * Método para modificar los valores de una Cuenta en la base de datos
    *
    * @param cuentaUsuario la cuenta a modificar
    * @return indica si la modificación de la cuenta fue exitosa o no
    */
   @Override
   public boolean modificarCuenta(CuentaUsuario cuentaUsuario) {
      boolean modificacionExitosa = false;
      EntityManagerFactory entityManagerFactory;
      try {
         entityManagerFactory = Persistence.createEntityManagerFactory(UNIDAD_PERSISTENCIA, null);
         CuentaJpaController controller = new CuentaJpaController(entityManagerFactory);
         Cuenta cuenta = new Cuenta(cuentaUsuario.getNombreUsuario(), cuentaUsuario.getClave(),
             cuentaUsuario.getLenguaje(), cuentaUsuario.getPuntaje());
         controller.edit(cuenta);
         modificacionExitosa = true;
      } catch (Exception ex) {
         Logger.getLogger(AdministracionCuenta.class.getName()).log(Level.SEVERE, null, ex);
      }
      return modificacionExitosa;
   }

   /**
    * Método para eliminar una Cuenta de la base de datos.
    *
    * @param nombreUsuario el nickname de la cuenta
    * @return si la cuenta fue eliminada exitosamente
    */
   @Override
   public boolean desactivarCuenta(String nombreUsuario) {
      boolean cuentaDesactivada = false;
      EntityManagerFactory entityManagerFactory;
      try {
         entityManagerFactory = Persistence.createEntityManagerFactory(UNIDAD_PERSISTENCIA, null);
         CuentaJpaController controller = new CuentaJpaController(entityManagerFactory);
         controller.destroy(nombreUsuario);
         cuentaDesactivada = true;
      } catch (NonexistentEntityException ex) {
         Logger.getLogger(AdministracionCuenta.class.getName()).log(Level.SEVERE, null, ex);
      }
      return cuentaDesactivada;
   }

   /**
    * Método para recuperar una lista de las cuentas con las 10 mejores puntuaciones
    *
    * @return la lista de las cuentas
    */
   @Override
   public List<CuentaUsuario> obtenerMejoresPuntajes() {
      EntityManagerFactory entityManagerFactory;
      List<CuentaUsuario> cuentasConMejorPuntaje = null;
      entityManagerFactory = Persistence.createEntityManagerFactory(UNIDAD_PERSISTENCIA, null);
      EntityManager entity = entityManagerFactory.createEntityManager();
      cuentasConMejorPuntaje = entity.createNamedQuery("Cuenta.obtenerPuntaje").setMaxResults(10).getResultList();
      return cuentasConMejorPuntaje;
   }

   /**
    * Método para registrar el puntaje más alto obtenido en una partida
    *
    * @param cuenta la cuenta de la cual se va a registrar el puntaje
    * @param puntajeObtenido el puntaje obtenido
    * @return si el registro del nuevo puntaje fue exitoso
    */
   @Override
   public boolean registrarPuntajeMasAlto(CuentaUsuario cuenta, int puntajeObtenido) {
      boolean puntajeRegistrado = false;
         try {
            cuenta.setPuntaje(puntajeObtenido);
            if (modificarCuenta(cuenta)) {
               puntajeRegistrado = true;
            }
         } catch (Exception ex) {
            Logger.getLogger(AdministracionCuenta.class.getName()).log(Level.SEVERE, null, ex);
         }
      
      return puntajeRegistrado;
   }

   /**
    * Método auxiliar para aplicar el hash a una cadena
    *
    * @param string cadena a hashear
    * @return el hash producido aplicando el método a la cadena pasada
    * @throws java.security.NoSuchAlgorithmException
    */
   @Override
   public String getHash(String string) throws NoSuchAlgorithmException {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
      byte[] hash = messageDigest.digest(string.getBytes(Charset.forName("UTF-8")));
      StringBuilder stringBuilder = new StringBuilder();
      for (int i = 0; i < hash.length; i++) {
         stringBuilder.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
      }
      return stringBuilder.toString();
   }

}
