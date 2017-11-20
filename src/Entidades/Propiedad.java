/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
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
@Table(name = "PROPIEDAD")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Propiedad.findAll", query = "SELECT p FROM Propiedad p")
    , @NamedQuery(name = "Propiedad.findByIdPropiedad", query = "SELECT p FROM Propiedad p WHERE p.propiedadPK.idPropiedad = :idPropiedad")
    , @NamedQuery(name = "Propiedad.findByTipo", query = "SELECT p FROM Propiedad p WHERE p.tipo = :tipo")
    , @NamedQuery(name = "Propiedad.findByDireccion", query = "SELECT p FROM Propiedad p WHERE p.direccion = :direccion")
    , @NamedQuery(name = "Propiedad.findByNumeroCuartos", query = "SELECT p FROM Propiedad p WHERE p.numeroCuartos = :numeroCuartos")
    , @NamedQuery(name = "Propiedad.findByRenta", query = "SELECT p FROM Propiedad p WHERE p.renta = :renta")
    , @NamedQuery(name = "Propiedad.findByUsuario", query = "SELECT p FROM Propiedad p WHERE p.propiedadPK.usuario = :usuario")
    , @NamedQuery(name = "Propiedad.findByDeleted", query = "SELECT p FROM Propiedad p WHERE p.deleted = :deleted")})
public class Propiedad implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "propiedad")
    private Collection<RentaSolicitud> rentaSolicitudCollection;

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PropiedadPK propiedadPK;
    @Column(name = "TIPO")
    private String tipo;
    @Column(name = "DIRECCION")
    private String direccion;
    @Column(name = "NUMERO_CUARTOS")
    private BigInteger numeroCuartos;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "RENTA")
    private BigDecimal renta;
    @Column(name = "DELETED")
    private Character deleted;
    @ManyToMany(mappedBy = "propiedadCollection")
    private Collection<Cliente> clienteCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "propiedad")
    private Collection<Foto> fotoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "propiedad")
    private Collection<Ubicacion> ubicacionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "propiedad")
   // private Collection<RentaSolicitud> rentaSolicitudCollection;
    @JoinColumn(name = "USUARIO", referencedColumnName = "USUARIO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Propietario propietario;

    public Propiedad() {
    }

    public Propiedad(PropiedadPK propiedadPK) {
        this.propiedadPK = propiedadPK;
    }

    public Propiedad(BigInteger idPropiedad, String usuario) {
        this.propiedadPK = new PropiedadPK(idPropiedad, usuario);
    }

    public PropiedadPK getPropiedadPK() {
        return propiedadPK;
    }

    public void setPropiedadPK(PropiedadPK propiedadPK) {
        this.propiedadPK = propiedadPK;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public BigInteger getNumeroCuartos() {
        return numeroCuartos;
    }

    public void setNumeroCuartos(BigInteger numeroCuartos) {
        this.numeroCuartos = numeroCuartos;
    }

    public BigDecimal getRenta() {
        return renta;
    }

    public void setRenta(BigDecimal renta) {
        this.renta = renta;
    }

    public Character getDeleted() {
        return deleted;
    }

    public void setDeleted(Character deleted) {
        this.deleted = deleted;
    }

    @XmlTransient
    public Collection<Cliente> getClienteCollection() {
        return clienteCollection;
    }

    public void setClienteCollection(Collection<Cliente> clienteCollection) {
        this.clienteCollection = clienteCollection;
    }

    @XmlTransient
    public Collection<Foto> getFotoCollection() {
        return fotoCollection;
    }

    public void setFotoCollection(Collection<Foto> fotoCollection) {
        this.fotoCollection = fotoCollection;
    }

    @XmlTransient
    public Collection<Ubicacion> getUbicacionCollection() {
        return ubicacionCollection;
    }

    public void setUbicacionCollection(Collection<Ubicacion> ubicacionCollection) {
        this.ubicacionCollection = ubicacionCollection;
    }

    @XmlTransient
   // public Collection<RentaSolicitud> getRentaSolicitudCollection() {
     //   return rentaSolicitudCollection;
    //}

    //public void setRentaSolicitudCollection(Collection<RentaSolicitud> rentaSolicitudCollection) {
    //    this.rentaSolicitudCollection = rentaSolicitudCollection;
    //}

    public Propietario getPropietario() {
        return propietario;
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (propiedadPK != null ? propiedadPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Propiedad)) {
            return false;
        }
        Propiedad other = (Propiedad) object;
        if ((this.propiedadPK == null && other.propiedadPK != null) || (this.propiedadPK != null && !this.propiedadPK.equals(other.propiedadPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Propiedad[ propiedadPK=" + propiedadPK + " ]";
    }

    @XmlTransient
    public Collection<RentaSolicitud> getRentaSolicitudCollection() {
        return rentaSolicitudCollection;
    }

    public void setRentaSolicitudCollection(Collection<RentaSolicitud> rentaSolicitudCollection) {
        this.rentaSolicitudCollection = rentaSolicitudCollection;
    }
    
}
