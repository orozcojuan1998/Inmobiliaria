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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "CLIENTE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cliente.findAll", query = "SELECT c FROM Cliente c")
    , @NamedQuery(name = "Cliente.findByUsuario", query = "SELECT c FROM Cliente c WHERE c.usuario = :usuario")
    , @NamedQuery(name = "Cliente.findByIdTarjeta", query = "SELECT c FROM Cliente c WHERE c.idTarjeta = :idTarjeta")
    , @NamedQuery(name = "Cliente.findByCedula", query = "SELECT c FROM Cliente c WHERE c.cedula = :cedula")
    , @NamedQuery(name = "Cliente.findByMaxRentaOfertar", query = "SELECT c FROM Cliente c WHERE c.maxRentaOfertar = :maxRentaOfertar")})
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "USUARIO")
    private String usuario;
    @Column(name = "ID_TARJETA")
    private BigInteger idTarjeta;
    @Column(name = "CEDULA")
    private BigInteger cedula;
    @Column(name = "MAX_RENTA_OFERTAR")
    private BigInteger maxRentaOfertar;
    @JoinTable(name = "CLIENTE_PROPIEDADES", joinColumns = {
        @JoinColumn(name = "CLIENTE_USUARIO", referencedColumnName = "USUARIO")}, inverseJoinColumns = {
        @JoinColumn(name = "PROPIEDAD_ID_PROPIEDAD", referencedColumnName = "ID_PROPIEDAD")
        , @JoinColumn(name = "USUARIO", referencedColumnName = "USUARIO")})
    @ManyToMany
    private Collection<Propiedad> propiedadCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "cliente")
    private WaitList waitList;
    @JoinColumn(name = "USUARIO", referencedColumnName = "USUARIO", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Cuenta cuenta;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cliente")
    private Collection<Tarjeta> tarjetaCollection;

    public Cliente() {
    }

    public Cliente(String usuario) {
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public BigInteger getIdTarjeta() {
        return idTarjeta;
    }

    public void setIdTarjeta(BigInteger idTarjeta) {
        this.idTarjeta = idTarjeta;
    }

    public BigInteger getCedula() {
        return cedula;
    }

    public void setCedula(BigInteger cedula) {
        this.cedula = cedula;
    }

    public BigInteger getMaxRentaOfertar() {
        return maxRentaOfertar;
    }

    public void setMaxRentaOfertar(BigInteger maxRentaOfertar) {
        this.maxRentaOfertar = maxRentaOfertar;
    }

    @XmlTransient
    public Collection<Propiedad> getPropiedadCollection() {
        return propiedadCollection;
    }

    public void setPropiedadCollection(Collection<Propiedad> propiedadCollection) {
        this.propiedadCollection = propiedadCollection;
    }

    public WaitList getWaitList() {
        return waitList;
    }

    public void setWaitList(WaitList waitList) {
        this.waitList = waitList;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    @XmlTransient
    public Collection<Tarjeta> getTarjetaCollection() {
        return tarjetaCollection;
    }

    public void setTarjetaCollection(Collection<Tarjeta> tarjetaCollection) {
        this.tarjetaCollection = tarjetaCollection;
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
        if (!(object instanceof Cliente)) {
            return false;
        }
        Cliente other = (Cliente) object;
        if ((this.usuario == null && other.usuario != null) || (this.usuario != null && !this.usuario.equals(other.usuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Cliente[ usuario=" + usuario + " ]";
    }
    
}
