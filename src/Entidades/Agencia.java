/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "AGENCIA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Agencia.findAll", query = "SELECT a FROM Agencia a")
    , @NamedQuery(name = "Agencia.findByNombreAgencia", query = "SELECT a FROM Agencia a WHERE a.nombreAgencia = :nombreAgencia")})
public class Agencia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "NOMBRE_AGENCIA")
    private String nombreAgencia;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agenciaNombreAgencia")
    private Collection<Sucursal> sucursalCollection;

    public Agencia() {
    }

    public Agencia(String nombreAgencia) {
        this.nombreAgencia = nombreAgencia;
    }

    public String getNombreAgencia() {
        return nombreAgencia;
    }

    public void setNombreAgencia(String nombreAgencia) {
        this.nombreAgencia = nombreAgencia;
    }

    @XmlTransient
    public Collection<Sucursal> getSucursalCollection() {
        return sucursalCollection;
    }

    public void setSucursalCollection(Collection<Sucursal> sucursalCollection) {
        this.sucursalCollection = sucursalCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nombreAgencia != null ? nombreAgencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Agencia)) {
            return false;
        }
        Agencia other = (Agencia) object;
        if ((this.nombreAgencia == null && other.nombreAgencia != null) || (this.nombreAgencia != null && !this.nombreAgencia.equals(other.nombreAgencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Agencia[ nombreAgencia=" + nombreAgencia + " ]";
    }
    
}
