/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Asus-pc
 */
@Entity
@Table(name = "PROPIETARIO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Propietario.findAll", query = "SELECT p FROM Propietario p")
    , @NamedQuery(name = "Propietario.findByUsuario", query = "SELECT p FROM Propietario p WHERE p.usuario = :usuario")
    , @NamedQuery(name = "Propietario.findByCedula", query = "SELECT p FROM Propietario p WHERE p.cedula = :cedula")
    , @NamedQuery(name = "Propietario.findByNumeroPropiedades", query = "SELECT p FROM Propietario p WHERE p.numeroPropiedades = :numeroPropiedades")})
public class Propietario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "USUARIO")
    private String usuario;
    @Basic(optional = false)
    @Column(name = "CEDULA")
    private BigInteger cedula;
    @Column(name = "NUMERO_PROPIEDADES")
    private BigInteger numeroPropiedades;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "propietario")
    private Collection<Propiedad> propiedadCollection;
    @JoinColumn(name = "USUARIO", referencedColumnName = "USUARIO", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Cuenta cuenta;

    public Propietario() {
    }

    public Propietario(String usuario) {
        this.usuario = usuario;
    }

    public Propietario(String usuario, BigInteger cedula) {
        this.usuario = usuario;
        this.cedula = cedula;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public BigInteger getCedula() {
        return cedula;
    }

    public void setCedula(BigInteger cedula) {
        this.cedula = cedula;
    }

    public BigInteger getNumeroPropiedades() {
        return numeroPropiedades;
    }

    public void setNumeroPropiedades(BigInteger numeroPropiedades) {
        this.numeroPropiedades = numeroPropiedades;
    }

    @XmlTransient
    public Collection<Propiedad> getPropiedadCollection() {
        return propiedadCollection;
    }

    public void setPropiedadCollection(Collection<Propiedad> propiedadCollection) {
        this.propiedadCollection = propiedadCollection;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuario != null ? usuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Propietario)) {
            return false;
        }
        Propietario other = (Propietario) object;
        if ((this.usuario == null && other.usuario != null) || (this.usuario != null && !this.usuario.equals(other.usuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Propietario[ usuario=" + usuario + " ]";
    }
    
}
