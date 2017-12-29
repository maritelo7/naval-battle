/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navalBattle.datos;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Maribel Tello Rodriguez
 * @author José Alí Valdivia Ruiz
 */
@Entity
@Table(name = "cuenta")
@XmlRootElement
@NamedQueries({   
   @NamedQuery(name = "Cuenta.findAll", query = "SELECT c FROM Cuenta c"),
   @NamedQuery(name = "Cuenta.findByNombreUsuario", query = "SELECT c FROM Cuenta c WHERE c.nombreUsuario = :nombreUsuario"),
   @NamedQuery(name = "Cuenta.findByClave", query = "SELECT c FROM Cuenta c WHERE c.clave = :clave"),
   @NamedQuery(name = "Cuenta.findByLenguaje", query = "SELECT c FROM Cuenta c WHERE c.lenguaje = :lenguaje"),
   @NamedQuery(name = "Cuenta.iniciarSesion", query = "SELECT c FROM Cuenta c WHERE c.nombreUsuario = :nombreUsuario AND c.clave =:clave"),
   @NamedQuery(name = "Cuenta.obtenerPuntaje", query = "SELECT c FROM Cuenta c ORDER BY c.puntaje DESC"),
   @NamedQuery(name = "Cuenta.findByPuntaje", query = "SELECT c FROM Cuenta c WHERE c.puntaje = :puntaje")})
public class Cuenta implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @Basic(optional = false)
   @Column(name = "nombreUsuario")
   private String nombreUsuario;
   @Column(name = "clave")
   private String clave;
   @Column(name = "lenguaje")
   private String lenguaje;
   @Column(name = "puntaje")
   private Integer puntaje;

   public Cuenta() {
   }

   public Cuenta(String nombreUsuario) {
      this.nombreUsuario = nombreUsuario;
   }
   
   public Cuenta(String nombreUsuario, String clave, String lenguaje) {
     this.nombreUsuario = nombreUsuario;
     this.clave = clave;
     this.lenguaje = lenguaje;
   }
   
   public Cuenta(String nombreUsuario, String clave, String lenguaje, int puntaje) {
     this.nombreUsuario = nombreUsuario;
     this.clave = clave;
     this.lenguaje = lenguaje;
     this.puntaje = puntaje;
   }

   public String getNombreUsuario() {
      return nombreUsuario;
   }

   public void setNombreUsuario(String nombreUsuario) {
      this.nombreUsuario = nombreUsuario;
   }

   public String getClave() {
      return clave;
   }

   public void setClave(String clave) {
      this.clave = clave;
   }

   public String getLenguaje() {
      return lenguaje;
   }

   public void setLenguaje(String lenguaje) {
      this.lenguaje = lenguaje;
   }

   public Integer getPuntaje() {
      return puntaje;
   }

   public void setPuntaje(Integer puntaje) {
      this.puntaje = puntaje;
   }

   @Override
   public int hashCode() {
      int hash = 0;
      hash += (nombreUsuario != null ? nombreUsuario.hashCode() : 0);
      return hash;
   }

   @Override
   public boolean equals(Object object) {
      // TODO: Warning - this method won't work in the case the id fields are not set
      if (!(object instanceof Cuenta)) {
         return false;
      }
      Cuenta other = (Cuenta) object;
      return !((this.nombreUsuario == null && other.nombreUsuario != null) || (this.nombreUsuario != null &&
          !this.nombreUsuario.equals(other.nombreUsuario)));
   }

   @Override
   public String toString() {
      return "navalBattle.datos.Cuenta[ nombreUsuario=" + nombreUsuario + " ]";
   }
   
}
