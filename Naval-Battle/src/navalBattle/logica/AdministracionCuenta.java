/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.logica;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import navalBattle.datos.Cuenta;
import navalBattle.datos.CuentaJpaController;
import navalBattle.datos.exceptions.NonexistentEntityException;
import navalBattle.datos.exceptions.PreexistingEntityException;

/**
 * Clase para implementar la gestión de la entidad cuenta
 *
 * @author Maribel Tello Rodriguez
 * @author José Alí Valdivia Ruiz
 */
public class AdministracionCuenta {
   final static String UNIDAD_PERSISTENCIA = "Naval-BattlePU";

   /**
    * Método para registrar una Cuenta en la base de datos
    *
    * @param cuentaUsuario la cuenta a registrar
    * @throws navalBattle.datos.exceptions.PreexistingEntityException ocurre cuando la Cuenta a
    * registrar ya existe
    * @throws java.security.NoSuchAlgorithmException ocurre cuando un algoritmo es requerido pero no
    * está disponible
    */
 
   public void registrarCuenta(CuentaUsuario cuentaUsuario) throws PersistenceException, PreexistingEntityException,
       NoSuchAlgorithmException {
      EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(UNIDAD_PERSISTENCIA, null);
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
    * @return la cuenta que fue solicitada, recuperada de la base de datos
    * @throws java.security.NoSuchAlgorithmException ocurre cuando un algoritmo es requerido pero no
    * está disponible
    */
   
   public CuentaUsuario consultarCuenta(String nombreUsuario, String clave) throws NoSuchAlgorithmException,
      ArrayIndexOutOfBoundsException, PersistenceException {
      Cuenta cuentaRecuperada;
      EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(UNIDAD_PERSISTENCIA, null);
      EntityManager entity = entityManagerFactory.createEntityManager();
      String claveHasheada = getHash(clave);
      cuentaRecuperada = (Cuenta) entity.createNamedQuery("Cuenta.iniciarSesion").setParameter("nombreUsuario", nombreUsuario).setParameter("clave", claveHasheada).getResultList().get(0);
      CuentaUsuario cuentaUsuario = new CuentaUsuario(cuentaRecuperada.getNombreUsuario(), cuentaRecuperada.getClave(),
      cuentaRecuperada.getLenguaje(), cuentaRecuperada.getPuntaje());
      return cuentaUsuario;
   }


   /**
    * Método para modificar los valores de una Cuenta en la base de datos
    *
    * @param cuentaUsuario la cuenta a modificar con los nuevos datos de clave o idioma
    * @throws navalBattle.datos.exceptions.NonexistentEntityException ocurre cuando el objeto que se
    * busca no es encontrado
    */

   public void modificarCuenta(CuentaUsuario cuentaUsuario) throws NonexistentEntityException, PersistenceException{
      EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(UNIDAD_PERSISTENCIA, null);
      CuentaJpaController controller = new CuentaJpaController(entityManagerFactory);
      Cuenta cuenta = new Cuenta(cuentaUsuario.getNombreUsuario(), cuentaUsuario.getClave(),
          cuentaUsuario.getLenguaje(), cuentaUsuario.getPuntaje());
      controller.edit(cuenta); 
   }

   /**
    * Método para eliminar una Cuenta de la base de datos.
    *
    * @param nombreUsuario el nickname de la cuenta que se eliminará de la base de datos
    * @throws navalBattle.datos.exceptions.NonexistentEntityException ocurre cuando el objeto que se
    * busca no es encontrado
    */

   public void desactivarCuenta(String nombreUsuario)throws NonexistentEntityException, PersistenceException {
      EntityManagerFactory entityManagerFactory;
         entityManagerFactory = Persistence.createEntityManagerFactory(UNIDAD_PERSISTENCIA, null);
         CuentaJpaController controller = new CuentaJpaController(entityManagerFactory);
         controller.destroy(nombreUsuario);
   }

   /**
    * Método para recuperar una lista de las cuentas con las 10 mejores puntuaciones
    *
    * @return la lista de las cuentas
    */
   
   public List<CuentaUsuario> obtenerMejoresPuntajes() throws PersistenceException, 
      ArrayIndexOutOfBoundsException {      
      EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(UNIDAD_PERSISTENCIA, null);
      EntityManager entity = entityManagerFactory.createEntityManager();
      List<Cuenta> cuentasRecuperadas = entity.createNamedQuery("Cuenta.obtenerPuntaje").setMaxResults(10).getResultList(); 
      List<CuentaUsuario> cuentasConMejorPuntaje = new ArrayList<>();
      for (int i = 0; i < cuentasRecuperadas.size(); i++) {
         Cuenta cuentaAux = cuentasRecuperadas.get(i);
         CuentaUsuario cuentaUsuarioAux = new CuentaUsuario(cuentaAux.getNombreUsuario(), cuentaAux.getClave(), cuentaAux.getLenguaje(), cuentaAux.getPuntaje());
         cuentasConMejorPuntaje.add(cuentaUsuarioAux);
      }
      return cuentasConMejorPuntaje;
   }

   /**
    * Método para registrar el puntaje más alto obtenido en una partida
    *
    * @param cuenta la cuenta de la cual se va a registrar el puntaje
    * @param puntajeObtenido el puntaje obtenido
    * @throws navalBattle.datos.exceptions.NonexistentEntityException ocurre cuando el objeto que se
    * busca no es encontrado
    */
   
   public void registrarPuntajeMasAlto(CuentaUsuario cuenta, int puntajeObtenido) throws PersistenceException,
       NonexistentEntityException {
      cuenta.setPuntaje(puntajeObtenido);
      modificarCuenta(cuenta);
   }

   /**
    * Método auxiliar para aplicar el hash a una cadena
    *
    * @param string cadena de la cual se obtendrá el hash
    * @return el hash producido aplicando el método a la cadena pasada
    * @throws java.security.NoSuchAlgorithmException ocurre cuando un algoritmo es requerido pero no
    * está disponible
    */
  
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
