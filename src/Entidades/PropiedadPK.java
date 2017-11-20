/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;


/**
 *
 * @author Asus-pc
 */
@Embeddable
public class PropiedadPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "ID_PROPIEDAD")
    @SequenceGenerator(name="Pro_Gen", sequenceName="Pro_Seq")
    @GeneratedValue(generator="Pro_Gen")
    private BigInteger idPropiedad;
    @Basic(optional = false)
    @Column(name = "USUARIO")
    private String usuario;

    public PropiedadPK() {
    }

    public PropiedadPK(BigInteger idPropiedad, String usuario) {
        this.idPropiedad = idPropiedad;
        this.usuario = usuario;
    }

    public BigInteger getIdPropiedad() {
        return idPropiedad;
    }

    public void setIdPropiedad(BigInteger idPropiedad) {
        this.idPropiedad = idPropiedad;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPropiedad != null ? idPropiedad.hashCode() : 0);
        hash += (usuario != null ? usuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PropiedadPK)) {
            return false;
        }
        PropiedadPK other = (PropiedadPK) object;
        if ((this.idPropiedad == null && other.idPropiedad != null) || (this.idPropiedad != null && !this.idPropiedad.equals(other.idPropiedad))) {
            return false;
        }
        if ((this.usuario == null && other.usuario != null) || (this.usuario != null && !this.usuario.equals(other.usuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.PropiedadPK[ idPropiedad=" + idPropiedad + ", usuario=" + usuario + " ]";
    }
    
}
