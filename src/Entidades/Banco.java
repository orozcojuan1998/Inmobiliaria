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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Asus-pc
 */
@Entity
@Table(name = "BANCO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Banco.findAll", query = "SELECT b FROM Banco b")
    , @NamedQuery(name = "Banco.findByNombre", query = "SELECT b FROM Banco b WHERE b.bancoPK.nombre = :nombre")
    , @NamedQuery(name = "Banco.findByEstado", query = "SELECT b FROM Banco b WHERE b.estado = :estado")
    , @NamedQuery(name = "Banco.findByIdRegistroRenta", query = "SELECT b FROM Banco b WHERE b.bancoPK.idRegistroRenta = :idRegistroRenta")})
public class Banco implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "banco")
    private Collection<RentaSolicitud> rentaSolicitudCollection;

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BancoPK bancoPK;
    @Basic(optional = false)
    @Column(name = "ESTADO")
    private String estado;
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "banco")
    //private Collection<RentaSolicitud> rentaSolicitudCollection;

    public Banco() {
    }

    public Banco(BancoPK bancoPK) {
        this.bancoPK = bancoPK;
    }

    public Banco(BancoPK bancoPK, String estado) {
        this.bancoPK = bancoPK;
        this.estado = estado;
    }

    public Banco(String nombre, BigInteger idRegistroRenta) {
        this.bancoPK = new BancoPK(nombre, idRegistroRenta);
    }

    public BancoPK getBancoPK() {
        return bancoPK;
    }

    public void setBancoPK(BancoPK bancoPK) {
        this.bancoPK = bancoPK;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    //@XmlTransient
    //public Collection<RentaSolicitud> getRentaSolicitudCollection() {
    //    return rentaSolicitudCollection;
    //}

    //public void setRentaSolicitudCollection(Collection<RentaSolicitud> rentaSolicitudCollection) {
    //    this.rentaSolicitudCollection = rentaSolicitudCollection;
    //}

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bancoPK != null ? bancoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Banco)) {
            return false;
        }
        Banco other = (Banco) object;
        if ((this.bancoPK == null && other.bancoPK != null) || (this.bancoPK != null && !this.bancoPK.equals(other.bancoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Banco[ bancoPK=" + bancoPK + " ]";
    }

    @XmlTransient
    public Collection<RentaSolicitud> getRentaSolicitudCollection() {
        return rentaSolicitudCollection;
    }

    public void setRentaSolicitudCollection(Collection<RentaSolicitud> rentaSolicitudCollection) {
        this.rentaSolicitudCollection = rentaSolicitudCollection;
    }
    
}
