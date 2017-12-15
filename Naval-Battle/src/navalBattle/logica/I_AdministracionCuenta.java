/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.logica;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import navalBattle.datos.exceptions.PreexistingEntityException;

/**
 *
 * @author Mari
 */
public interface I_AdministracionCuenta {
   public void registrarCuenta(CuentaUsuario cuentaUsuario) throws NoSuchAlgorithmException, PreexistingEntityException;
   public CuentaUsuario consultarCuenta(String nombreUsuario, String clave) throws NoSuchAlgorithmException;
   public boolean modificarCuenta(CuentaUsuario cuentaUsuario) throws NoSuchAlgorithmException;
   public boolean desactivarCuenta(String nombreUsuario) throws NoSuchAlgorithmException;
   public boolean registrarPuntajeMasAlto(CuentaUsuario cuenta, int puntajeObtenido) throws NoSuchAlgorithmException;
   public String getHash(String string) throws NoSuchAlgorithmException;
   public List<CuentaUsuario> obtenerMejoresPuntajes();
}
