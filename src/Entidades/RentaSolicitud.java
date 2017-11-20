/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author COMERCIAL02
 */
@Entity
@Table(name = "RENTA_SOLICITUD")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RentaSolicitud.findAll", query = "SELECT r FROM RentaSolicitud r")
    , @NamedQuery(name = "RentaSolicitud.findByIdRegistroRenta", query = "SELECT r FROM RentaSolicitud r WHERE r.idRegistroRenta = :idRegistroRenta")
    , @NamedQuery(name = "RentaSolicitud.findByHora", query = "SELECT r FROM RentaSolicitud r WHERE r.hora = :hora")
    , @NamedQuery(name = "RentaSolicitud.findByIdTarjeta", query = "SELECT r FROM RentaSolicitud r WHERE r.idTarjeta = :idTarjeta")
    , @NamedQuery(name = "RentaSolicitud.findByIdRegistroRenta1", query = "SELECT r FROM RentaSolicitud r WHERE r.idRegistroRenta1 = :idRegistroRenta1")})
public class RentaSolicitud implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO) 
    @Basic(optional = false)
    @Column(name = "ID_REGISTRO_RENTA")
    private BigDecimal idRegistroRenta;
    @Column(name = "HORA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date hora;
    @Basic(optional = false)
    @Column(name = "ID_TARJETA")
    private BigInteger idTarjeta;
    @Basic(optional = false)
    @Column(name = "ID_REGISTRO_RENTA1")
    private BigInteger idRegistroRenta1;
    @JoinColumns({
        @JoinColumn(name = "BANCO_NOMBRE", referencedColumnName = "NOMBRE")
        , @JoinColumn(name = "BANCO_ID_REGISTRO_RENTA", referencedColumnName = "ID_REGISTRO_RENTA")})
    @ManyToOne(optional = false)
    private Banco banco;
    @JoinColumns({
        @JoinColumn(name = "PROPIEDAD_ID_PROPIEDAD", referencedColumnName = "ID_PROPIEDAD")
        , @JoinColumn(name = "USUARIO", referencedColumnName = "USUARIO")})
    @ManyToOne(optional = false)
    private Propiedad propiedad;
    @JoinColumn(name = "SUCURSAL_NUMERO_SUCURSAL", referencedColumnName = "NUMERO_SUCURSAL")
    @ManyToOne(optional = false)
    private Sucursal sucursalNumeroSucursal;

    public RentaSolicitud() {
    }

    public RentaSolicitud(BigDecimal idRegistroRenta) {
        this.idRegistroRenta = idRegistroRenta;
    }

    public RentaSolicitud(BigDecimal idRegistroRenta, BigInteger idTarjeta, BigInteger idRegistroRenta1) {
        this.idRegistroRenta = idRegistroRenta;
        this.idTarjeta = idTarjeta;
        this.idRegistroRenta1 = idRegistroRenta1;
    }

    public BigDecimal getIdRegistroRenta() {
        return idRegistroRenta;
    }

    public void setIdRegistroRenta(BigDecimal idRegistroRenta) {
        this.idRegistroRenta = idRegistroRenta;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public BigInteger getIdTarjeta() {
        return idTarjeta;
    }

    public void setIdTarjeta(BigInteger idTarjeta) {
        this.idTarjeta = idTarjeta;
    }

    public BigInteger getIdRegistroRenta1() {
        return idRegistroRenta1;
    }

    public void setIdRegistroRenta1(BigInteger idRegistroRenta1) {
        this.idRegistroRenta1 = idRegistroRenta1;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public Propiedad getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(Propiedad propiedad) {
        this.propiedad = propiedad;
    }

    public Sucursal getSucursalNumeroSucursal() {
        return sucursalNumeroSucursal;
    }

    public void setSucursalNumeroSucursal(Sucursal sucursalNumeroSucursal) {
        this.sucursalNumeroSucursal = sucursalNumeroSucursal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRegistroRenta != null ? idRegistroRenta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RentaSolicitud)) {
            return false;
        }
        RentaSolicitud other = (RentaSolicitud) object;
        if ((this.idRegistroRenta == null && other.idRegistroRenta != null) || (this.idRegistroRenta != null && !this.idRegistroRenta.equals(other.idRegistroRenta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.RentaSolicitud[ idRegistroRenta=" + idRegistroRenta + " ]";
    }
    
}
