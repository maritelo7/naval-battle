package navalBattle.logica;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;
import navalBattle.datos.exceptions.NonexistentEntityException;
import navalBattle.datos.exceptions.PreexistingEntityException;



/**
 *
 * @author Maribel Tello Rodriguez
 * @author José Alí Valdivia Ruiz
 */
public class AdministrarCuentaTest {
   AdministracionCuenta administracionCuenta = new AdministracionCuenta();
   
   public AdministrarCuentaTest() {     
   }
      
   @Test
   public void testRegistrarCuenta() throws NoSuchAlgorithmException, PreexistingEntityException, NonexistentEntityException{
      CuentaUsuario cuenta = new CuentaUsuario("Maribel268", "1340", "English");
      administracionCuenta.registrarCuenta(cuenta);
      CuentaUsuario cuentaRegistrada = administracionCuenta.consultarCuenta("Maribel268", "1340");
      assertEquals(cuentaRegistrada.getNombreUsuario(), "Maribel268");
      assertEquals(cuentaRegistrada.getClave(), administracionCuenta.getHash("1340"));
      assertEquals(cuentaRegistrada.getLenguaje(), "English");
      assertEquals(cuentaRegistrada.getPuntaje(), 0);
      administracionCuenta.desactivarCuenta("Maribel268");
   }
   
   
   @Test
   public void testConsultarCuenta() throws NoSuchAlgorithmException, PersistenceException {
      CuentaUsuario cuentaConsultada = administracionCuenta.consultarCuenta("Maribel271", "1340");
      assertEquals(cuentaConsultada.getNombreUsuario(), "Maribel271");
      assertEquals(cuentaConsultada.getClave(), administracionCuenta.getHash("1340"));
      assertEquals(cuentaConsultada.getLenguaje(), "English");
      assertEquals(cuentaConsultada.getPuntaje(), 0);
      
   }   
   
   @Test
   public void testModificarCuenta() throws NoSuchAlgorithmException, PersistenceException, NonexistentEntityException{
      CuentaUsuario cuenta = administracionCuenta.consultarCuenta("Maribel271", "1340");
      cuenta.setPuntaje(2000);            
      administracionCuenta.modificarCuenta(cuenta);     
      CuentaUsuario cuentaModificada = administracionCuenta.consultarCuenta("Maribel271", "1340");
      assertEquals(cuentaModificada.getPuntaje(), 2000);
   }
   
   @Test
   public void testEliminarCuenta() throws NonexistentEntityException, PersistenceException, PreexistingEntityException, NoSuchAlgorithmException {
      CuentaUsuario cuenta = new CuentaUsuario("Maribel270", "1340", "English");      
      administracionCuenta.registrarCuenta(cuenta);
      administracionCuenta.desactivarCuenta("Maribel270");     
      CuentaUsuario cuentaNoExistente;
      try {
         cuentaNoExistente = administracionCuenta.consultarCuenta("Maribel270", "1340");  
         assertEquals(cuentaNoExistente, null);
      } catch (NoSuchAlgorithmException | ArrayIndexOutOfBoundsException | PersistenceException ex) {
         Logger.getLogger(AdministrarCuentaTest.class.getName()).log(Level.SEVERE, null, ex);
      }      
   }
   
    @Test
   public void testObtenerPuntajesMasAltos() throws NonexistentEntityException {
      List<CuentaUsuario> cuentasConMejoresPuntajes = administracionCuenta.obtenerMejoresPuntajes(); 
      assertEquals(cuentasConMejoresPuntajes.get(0).getPuntaje(), 2000);      
   }   
   
   @Before
   public void setUp() throws PreexistingEntityException, NoSuchAlgorithmException, PersistenceException, NonexistentEntityException {
      CuentaUsuario cuenta = new CuentaUsuario("Maribel271", "1340", "English");
      administracionCuenta.registrarCuenta(cuenta);
      cuenta = new CuentaUsuario("Maribel269", "1340", "English");
      administracionCuenta.registrarCuenta(cuenta);    
   }
   
   @After
   public void tearDown() throws PersistenceException, NonexistentEntityException {    
         administracionCuenta.desactivarCuenta("Maribel271");      
         administracionCuenta.desactivarCuenta("Maribel269");           
   }

   // TODO add test methods here.
   // The methods must be annotated with annotation @Test. For example:
   //
   // @Test
   // public void hello() {}
}
