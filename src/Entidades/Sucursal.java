/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "SUCURSAL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sucursal.findAll", query = "SELECT s FROM Sucursal s")
    , @NamedQuery(name = "Sucursal.findByNumeroSucursal", query = "SELECT s FROM Sucursal s WHERE s.numeroSucursal = :numeroSucursal")})
public class Sucursal implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sucursalNumeroSucursal")
    private Collection<RentaSolicitud> rentaSolicitudCollection;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "NUMERO_SUCURSAL")
    private BigDecimal numeroSucursal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sucursalNumeroSucursal")
   // private Collection<RentaSolicitud> rentaSolicitudCollection;
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "sucursalNumeroSucursal")
    private Collection<Agente> agenteCollection;
    @JoinColumn(name = "AGENCIA_NOMBRE_AGENCIA", referencedColumnName = "NOMBRE_AGENCIA")
    @ManyToOne(optional = false)
    private Agencia agenciaNombreAgencia;

    public Sucursal() {
    }

    public Sucursal(BigDecimal numeroSucursal) {
        this.numeroSucursal = numeroSucursal;
    }

    public BigDecimal getNumeroSucursal() {
        return numeroSucursal;
    }

    public void setNumeroSucursal(BigDecimal numeroSucursal) {
        this.numeroSucursal = numeroSucursal;
    }

   // @XmlTransient
   // public Collection<RentaSolicitud> getRentaSolicitudCollection() {
   //     return rentaSolicitudCollection;
  //  }

   // public void setRentaSolicitudCollection(Collection<RentaSolicitud> rentaSolicitudCollection) {
   //     this.rentaSolicitudCollection = rentaSolicitudCollection;
  //  }

    @XmlTransient
    public Collection<Agente> getAgenteCollection() {
        return agenteCollection;
    }

    public void setAgenteCollection(Collection<Agente> agenteCollection) {
        this.agenteCollection = agenteCollection;
    }

    public Agencia getAgenciaNombreAgencia() {
        return agenciaNombreAgencia;
    }

    public void setAgenciaNombreAgencia(Agencia agenciaNombreAgencia) {
        this.agenciaNombreAgencia = agenciaNombreAgencia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numeroSucursal != null ? numeroSucursal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sucursal)) {
            return false;
        }
        Sucursal other = (Sucursal) object;
        if ((this.numeroSucursal == null && other.numeroSucursal != null) || (this.numeroSucursal != null && !this.numeroSucursal.equals(other.numeroSucursal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Sucursal[ numeroSucursal=" + numeroSucursal + " ]";
    }

    @XmlTransient
    public Collection<RentaSolicitud> getRentaSolicitudCollection() {
        return rentaSolicitudCollection;
    }

    public void setRentaSolicitudCollection(Collection<RentaSolicitud> rentaSolicitudCollection) {
        this.rentaSolicitudCollection = rentaSolicitudCollection;
    }
    
}
