/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.logica;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author javr
 */
public class CuentaUsuarioTest {
   
   public CuentaUsuarioTest() {
   }
   
   @BeforeClass
   public static void setUpClass() {
   }
   
   @AfterClass
   public static void tearDownClass() {
   }
   
   @Before
   public void setUp() {
   }
   
   @After
   public void tearDown() {
   }

   /**
    * Test of getLenguaje method, of class CuentaUsuario.
    */
   @Test
   public void testGetLenguaje() {
      System.out.println("getLenguaje");
      CuentaUsuario instance = new CuentaUsuario("Mari", "Password", "English");
      instance.setLenguaje("English");
      String expResult = "English";
      String result = instance.getLenguaje();
      assertEquals(expResult, result);

   }

   /**
    * Test of setLenguaje method, of class CuentaUsuario.
    */
   @Test
   public void testSetLenguaje() {
      System.out.println("setLenguaje");
      String lenguaje = "Francais";
      CuentaUsuario instance = new CuentaUsuario("Mari", "PassWord", "Español");
      instance.setLenguaje(lenguaje);
      assertEquals(instance.getLenguaje(), lenguaje);
 
   }

   /**
    * Test of getNombreUsuario method, of class CuentaUsuario.
    */
   @Test
   public void testGetNombreUsuario() {
      System.out.println("getNombreUsuario");
      CuentaUsuario instance = new CuentaUsuario("Tello", "Password", "Español");
      String expResult = "Tello";
      String result = instance.getNombreUsuario();
      assertEquals(expResult, result);
 
   }

   /**
    * Test of setNombreUsuario method, of class CuentaUsuario.
    */
   @Test
   public void testSetNombreUsuario() {
      System.out.println("setNombreUsuario");
      String nombreUsuario = "Tello";
      CuentaUsuario instance = new CuentaUsuario("Mari", "PassworD", "Español");
      instance.setNombreUsuario(nombreUsuario);
      assertEquals(instance.getNombreUsuario(), nombreUsuario);
 
   }

   /**
    * Test of getClave method, of class CuentaUsuario.
    */
   @Test
   public void testGetClave() {
      System.out.println("getClave");
      CuentaUsuario instance = new CuentaUsuario("Mari", "PassWorD", "Francais");
      String expResult = "PassWorD";
      String result = instance.getClave();
      assertEquals(expResult, result);
   }

   /**
    * Test of setClave method, of class CuentaUsuario.
    */
   @Test
   public void testSetClave() {
      System.out.println("setClave");
      String clave = "PASSWORD";
      CuentaUsuario instance = new CuentaUsuario("Tello", "PASSword", "English");
      instance.setClave(clave);
      assertEquals(instance.getClave(), clave);

   }

   /**
    * Test of getPuntaje method, of class CuentaUsuario.
    */
   @Test
   public void testGetPuntaje() {
      System.out.println("getPuntaje");
      CuentaUsuario instance = new CuentaUsuario("Tello", "PASSword", "English");
      int expResult = 200;
      instance.setPuntaje(expResult);
      int result = instance.getPuntaje();
      assertEquals(expResult, result);
     
   }

   
   
}
