/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Asus-pc
 */
@Entity
@Table(name = "UBICACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ubicacion.findAll", query = "SELECT u FROM Ubicacion u")
    , @NamedQuery(name = "Ubicacion.findByCountry", query = "SELECT u FROM Ubicacion u WHERE u.country = :country")
    , @NamedQuery(name = "Ubicacion.findByState", query = "SELECT u FROM Ubicacion u WHERE u.state = :state")
    , @NamedQuery(name = "Ubicacion.findByCity", query = "SELECT u FROM Ubicacion u WHERE u.city = :city")
    , @NamedQuery(name = "Ubicacion.findByUbicacionId", query = "SELECT u FROM Ubicacion u WHERE u.ubicacionId = :ubicacionId")})
public class Ubicacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "COUNTRY")
    private String country;
    @Basic(optional = false)
    @Column(name = "STATE")
    private String state;
    @Basic(optional = false)
    @Column(name = "CITY")
    private String city;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue
    @Basic(optional = false)
    @Column(name = "UBICACION_ID")
    private BigDecimal ubicacionId;
    @JoinColumns({
        @JoinColumn(name = "PROPIEDAD_ID_PROPIEDAD", referencedColumnName = "ID_PROPIEDAD")
        , @JoinColumn(name = "USUARIO", referencedColumnName = "USUARIO")})
    @ManyToOne(optional = false)
    private Propiedad propiedad;

    public Ubicacion() {
    }

    public Ubicacion(BigDecimal ubicacionId) {
        this.ubicacionId = ubicacionId;
    }

    public Ubicacion(BigDecimal ubicacionId, String country, String state, String city) {
        this.ubicacionId = ubicacionId;
        this.country = country;
        this.state = state;
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public BigDecimal getUbicacionId() {
        return ubicacionId;
    }

    public void setUbicacionId(BigDecimal ubicacionId) {
        this.ubicacionId = ubicacionId;
    }

    public Propiedad getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(Propiedad propiedad) {
        this.propiedad = propiedad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ubicacionId != null ? ubicacionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ubicacion)) {
            return false;
        }
        Ubicacion other = (Ubicacion) object;
        if ((this.ubicacionId == null && other.ubicacionId != null) || (this.ubicacionId != null && !this.ubicacionId.equals(other.ubicacionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Ubicacion[ ubicacionId=" + ubicacionId + " ]";
    }
    
}
