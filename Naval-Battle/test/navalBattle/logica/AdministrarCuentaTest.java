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
import navalBattle.datos.exceptions.PreexistingEntityException;



/**
 *
 * @author Mari
 */
public class AdministrarCuentaTest {
   AdministracionCuenta administracionCuenta = new AdministracionCuenta();
   
   public AdministrarCuentaTest() {     
   }
   
   
   @Test
   public void testRegistrarCuenta() throws NoSuchAlgorithmException, PreexistingEntityException{
      CuentaUsuario cuenta = new CuentaUsuario("Maribel269", "1340", "English");
      administracionCuenta.registrarCuenta(cuenta);
      CuentaUsuario cuentaRegistrada = administracionCuenta.consultarCuenta("Maribel269", "1340");
      assertEquals(cuentaRegistrada.getNombreUsuario(), "Maribel269");
      assertEquals(cuentaRegistrada.getClave(), administracionCuenta.getHash("1340"));
      assertEquals(cuentaRegistrada.getLenguaje(), "English");
      assertEquals(cuentaRegistrada.getPuntaje(), 0);
   }
   
   
   @Test
   public void testConsultarCuenta() throws NoSuchAlgorithmException{
      CuentaUsuario cuentaConsultada = administracionCuenta.consultarCuenta("Maribel271", "1340");
      assertEquals(cuentaConsultada.getNombreUsuario(), "Maribel271");
      assertEquals(cuentaConsultada.getClave(), administracionCuenta.getHash("1340"));
      assertEquals(cuentaConsultada.getLenguaje(), "English");
      assertEquals(cuentaConsultada.getPuntaje(), 0);
   }   
   
   @Test
   public void testModificarCuenta() throws NoSuchAlgorithmException{
      CuentaUsuario cuenta = administracionCuenta.consultarCuenta("Maribel271", "1340");
      cuenta.setPuntaje(2000);
      administracionCuenta.modificarCuenta(cuenta);      
      CuentaUsuario cuentaModificada = administracionCuenta.consultarCuenta("Maribel271", "1340");
      assertEquals(cuentaModificada.getPuntaje(), 2000);
   }
   
   @Test
   public void testEliminarCuenta() throws NoSuchAlgorithmException{
      administracionCuenta.desactivarCuenta("Maribel270");
      CuentaUsuario cuentaNoExistente = administracionCuenta.consultarCuenta("Maribel270", "1340");
      assertEquals(cuentaNoExistente, null);
   }
   
   
   @Before
   public void setUp() throws PreexistingEntityException, NoSuchAlgorithmException {
      CuentaUsuario cuenta = new CuentaUsuario("Maribel270", "1340", "English");
      administracionCuenta.registrarCuenta(cuenta);
      cuenta = new CuentaUsuario("Maribel271", "1340", "English");
      administracionCuenta.registrarCuenta(cuenta);
      cuenta = new CuentaUsuario("Maribel269", "1340", "English");
      administracionCuenta.registrarCuenta(cuenta);
   }
   
   @After
   public void tearDown() {
      administracionCuenta.desactivarCuenta("Maribel270");
      administracionCuenta.desactivarCuenta("Maribel271");
      administracionCuenta.desactivarCuenta("Maribel269");      
   }

   // TODO add test methods here.
   // The methods must be annotated with annotation @Test. For example:
   //
   // @Test
   // public void hello() {}
}
