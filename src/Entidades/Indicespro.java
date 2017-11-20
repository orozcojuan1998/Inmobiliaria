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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author COMERCIAL02
 */
@Entity
@Table(name = "INDICESPRO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Indicespro.findAll", query = "SELECT i FROM Indicespro i")
    , @NamedQuery(name = "Indicespro.findByCodigo", query = "SELECT i FROM Indicespro i WHERE i.codigo = :codigo")
    , @NamedQuery(name = "Indicespro.findByCodPropiedad", query = "SELECT i FROM Indicespro i WHERE i.codPropiedad = :codPropiedad")
    , @NamedQuery(name = "Indicespro.findByCodUbicacion", query = "SELECT i FROM Indicespro i WHERE i.codUbicacion = :codUbicacion")
    , @NamedQuery(name = "Indicespro.findByCodTarjeta", query = "SELECT i FROM Indicespro i WHERE i.codTarjeta = :codTarjeta")
    , @NamedQuery(name = "Indicespro.findByCodRentasol", query = "SELECT i FROM Indicespro i WHERE i.codRentasol = :codRentasol")})
public class Indicespro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CODIGO")
    private String codigo;
    @Column(name = "COD_PROPIEDAD")
    private BigInteger codPropiedad;
    @Column(name = "COD_UBICACION")
    private BigInteger codUbicacion;
    @Column(name = "COD_TARJETA")
    private BigInteger codTarjeta;
    @Column(name = "COD_RENTASOL")
    private BigInteger codRentasol;

    public Indicespro() {
    }

    public Indicespro(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public BigInteger getCodPropiedad() {
        return codPropiedad;
    }

    public void setCodPropiedad(BigInteger codPropiedad) {
        this.codPropiedad = codPropiedad;
    }

    public BigInteger getCodUbicacion() {
        return codUbicacion;
    }

    public void setCodUbicacion(BigInteger codUbicacion) {
        this.codUbicacion = codUbicacion;
    }

    public BigInteger getCodTarjeta() {
        return codTarjeta;
    }

    public void setCodTarjeta(BigInteger codTarjeta) {
        this.codTarjeta = codTarjeta;
    }

    public BigInteger getCodRentasol() {
        return codRentasol;
    }

    public void setCodRentasol(BigInteger codRentasol) {
        this.codRentasol = codRentasol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Indicespro)) {
            return false;
        }
        Indicespro other = (Indicespro) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Indicespro[ codigo=" + codigo + " ]";
    }
    
}
