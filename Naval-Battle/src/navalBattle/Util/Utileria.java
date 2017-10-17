/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.Util;

import java.security.MessageDigest;
import java.util.Locale;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author javr
 */
public class Utileria {
   
   public String getSHA256Hash(String data) {

    String result = null;

    try {

      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(data.getBytes("UTF-8"));
      return bytesToHex(hash); // make it printable
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return result;
  }

  private String bytesToHex(byte[] hash) {
    return DatatypeConverter.printHexBinary(hash);

  }   
   
}
