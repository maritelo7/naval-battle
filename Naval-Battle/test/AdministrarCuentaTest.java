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
   
   public AdministrarCuentaTest() {     
   }
   
   
   @Test
   public void testRegistrarCuenta() throws NoSuchAlgorithmException{
      CuentaUsuario cuenta = new CuentaUsuario("Maribel267", "1234", "Español");
      AdministracionCuenta administracionCuenta = new AdministracionCuenta();
      administracionCuenta.registrarCuenta(cuenta);
      assertEquals(cuenta, administracionCuenta.consultarCuenta("Maribel267"));     
   }
   
   @Test
   public void testConsultarCuentaNoExistente() throws NoSuchAlgorithmException{
      CuentaUsuario cuenta = new CuentaUsuario("Maribel267", "1234", "Español");
      AdministracionCuenta administracionCuenta = new AdministracionCuenta();
      failNotEquals("Cuentas no iguales", cuenta, administracionCuenta.consultarCuenta("Maribel26"));     
   }
   
   @Test
   public void testConsultarCuentaExistente() throws NoSuchAlgorithmException{
      CuentaUsuario cuenta = new CuentaUsuario("Maribel267", "1234", "Español");
      AdministracionCuenta administracionCuenta = new AdministracionCuenta();
      failNotEquals("Cuentas no iguales", cuenta, administracionCuenta.consultarCuenta("Maribel267"));     
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
