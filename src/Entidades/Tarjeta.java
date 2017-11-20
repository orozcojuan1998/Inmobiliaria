/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Asus-pc
 */
@Entity
@Table(name = "TARJETA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tarjeta.findAll", query = "SELECT t FROM Tarjeta t")
    , @NamedQuery(name = "Tarjeta.findByIdTarjeta", query = "SELECT t FROM Tarjeta t WHERE t.tarjetaPK.idTarjeta = :idTarjeta")
    , @NamedQuery(name = "Tarjeta.findByTipoTarjeta", query = "SELECT t FROM Tarjeta t WHERE t.tipoTarjeta = :tipoTarjeta")
    , @NamedQuery(name = "Tarjeta.findByNumeroTarjeta", query = "SELECT t FROM Tarjeta t WHERE t.numeroTarjeta = :numeroTarjeta")
    , @NamedQuery(name = "Tarjeta.findByNombreTitular", query = "SELECT t FROM Tarjeta t WHERE t.nombreTitular = :nombreTitular")
    , @NamedQuery(name = "Tarjeta.findByMesExp", query = "SELECT t FROM Tarjeta t WHERE t.mesExp = :mesExp")
    , @NamedQuery(name = "Tarjeta.findByYearExp", query = "SELECT t FROM Tarjeta t WHERE t.yearExp = :yearExp")
    , @NamedQuery(name = "Tarjeta.findByClienteIdCliente", query = "SELECT t FROM Tarjeta t WHERE t.tarjetaPK.clienteIdCliente = :clienteIdCliente")})
public class Tarjeta implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TarjetaPK tarjetaPK;
    @Basic(optional = false)
    @Column(name = "TIPO_TARJETA")
    private String tipoTarjeta;
    @Basic(optional = false)
    @Column(name = "NUMERO_TARJETA")
    private BigInteger numeroTarjeta;
    @Basic(optional = false)
    @Column(name = "NOMBRE_TITULAR")
    private String nombreTitular;
    @Basic(optional = false)
    @Column(name = "MES_EXP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date mesExp;
    @Basic(optional = false)
    @Column(name = "YEAR_EXP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date yearExp;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tarjeta")
    //private Collection<RentaSolicitud> rentaSolicitudCollection;
    @JoinColumn(name = "CLIENTE_ID_CLIENTE", referencedColumnName = "USUARIO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Cliente cliente;

    public Tarjeta() {
    }

    public Tarjeta(TarjetaPK tarjetaPK) {
        this.tarjetaPK = tarjetaPK;
    }

    public Tarjeta(TarjetaPK tarjetaPK, String tipoTarjeta, BigInteger numeroTarjeta, String nombreTitular, Date mesExp, Date yearExp) {
        this.tarjetaPK = tarjetaPK;
        this.tipoTarjeta = tipoTarjeta;
        this.numeroTarjeta = numeroTarjeta;
        this.nombreTitular = nombreTitular;
        this.mesExp = mesExp;
        this.yearExp = yearExp;
    }

    public Tarjeta(BigInteger idTarjeta, String clienteIdCliente) {
        this.tarjetaPK = new TarjetaPK(idTarjeta, clienteIdCliente);
    }

    public TarjetaPK getTarjetaPK() {
        return tarjetaPK;
    }

    public void setTarjetaPK(TarjetaPK tarjetaPK) {
        this.tarjetaPK = tarjetaPK;
    }

    public String getTipoTarjeta() {
        return tipoTarjeta;
    }

    public void setTipoTarjeta(String tipoTarjeta) {
        this.tipoTarjeta = tipoTarjeta;
    }

    public BigInteger getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(BigInteger numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getNombreTitular() {
        return nombreTitular;
    }

    public void setNombreTitular(String nombreTitular) {
        this.nombreTitular = nombreTitular;
    }

    public Date getMesExp() {
        return mesExp;
    }

    public void setMesExp(Date mesExp) {
        this.mesExp = mesExp;
    }

    public Date getYearExp() {
        return yearExp;
    }

    public void setYearExp(Date yearExp) {
        this.yearExp = yearExp;
    }

   // @XmlTransient
   // public Collection<RentaSolicitud> getRentaSolicitudCollection() {
    //    return rentaSolicitudCollection;
   // }

   // public void setRentaSolicitudCollection(Collection<RentaSolicitud> rentaSolicitudCollection) {
    //    this.rentaSolicitudCollection = rentaSolicitudCollection;
   // }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tarjetaPK != null ? tarjetaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tarjeta)) {
            return false;
        }
        Tarjeta other = (Tarjeta) object;
        if ((this.tarjetaPK == null && other.tarjetaPK != null) || (this.tarjetaPK != null && !this.tarjetaPK.equals(other.tarjetaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Tarjeta[ tarjetaPK=" + tarjetaPK + " ]";
    }
    
}
