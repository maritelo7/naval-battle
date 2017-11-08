package navalBattle.logica;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.security.NoSuchAlgorithmException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import static junit.framework.Assert.failNotEquals;
import navalBattle.datos.CuentaJpaController;
import navalBattle.logica.AdministracionCuenta;
import navalBattle.logica.CuentaUsuario;


/**
 *
 * @author Mari
 */
public class AdministrarCuentaTest {
   EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Naval-BattlePU", null);
   CuentaJpaController controller = new CuentaJpaController(entityManagerFactory);
   AdministracionCuenta administracionCuenta = new AdministracionCuenta();
   
   public AdministrarCuentaTest() {     
   }
   
   
   @Test
   public void AtestRegistrarCuenta() throws NoSuchAlgorithmException{
      CuentaUsuario cuenta = new CuentaUsuario("Maribel270", "1240", "English");
      administracionCuenta.registrarCuenta(cuenta);
      CuentaUsuario cuentaRegistrada = administracionCuenta.consultarCuenta("Maribel270");
      assertEquals(cuentaRegistrada.getNombreUsuario(), "Maribel270");
      assertEquals(cuentaRegistrada.getClave(), administracionCuenta.getHash("1240"));
      assertEquals(cuentaRegistrada.getLenguaje(), "English");
      assertEquals(cuentaRegistrada.getPuntaje(), 0);
   }
   
   
   @Test
   public void testConsultarCuenta() throws NoSuchAlgorithmException{
      CuentaUsuario cuentaConsultada = administracionCuenta.consultarCuenta("Maribel267");
      assertEquals(cuentaConsultada.getNombreUsuario(), "Maribel267");
      assertEquals(cuentaConsultada.getClave(), administracionCuenta.getHash("1234"));
      assertEquals(cuentaConsultada.getLenguaje(), "Español");
      assertEquals(cuentaConsultada.getPuntaje(), 0);
   }   
   
   @Test
   public void testModificarCuenta() throws NoSuchAlgorithmException{
      CuentaUsuario cuenta = administracionCuenta.consultarCuenta("Maribel267");
      cuenta.setPuntaje(1000);
      administracionCuenta.modificarCuenta(cuenta);
      CuentaUsuario cuentaModificada = administracionCuenta.consultarCuenta("Maribel267");
      assertEquals(cuentaModificada.getPuntaje(), 1000);
   }
   
   @Test
   public void testEliminarCuenta() throws NoSuchAlgorithmException{
      administracionCuenta.desactivarCuenta("Maribel269");
      CuentaUsuario cuentaNoExistente = administracionCuenta.consultarCuenta("Maribel269");
      assertEquals(cuentaNoExistente, null);
   }
   
   
   @Before
   public void setUp() {
      
   }
   
   @After
   public void tearDown() {
   }

   // TODO add test methods here.
   // The methods must be annotated with annotation @Test. For example:
   //
   // @Test
   // public void hello() {}
}
