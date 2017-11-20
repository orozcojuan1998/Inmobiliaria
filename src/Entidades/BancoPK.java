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
import javax.persistence.Embeddable;

/**
 *
 * @author Asus-pc
 */
@Embeddable
public class BancoPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "ID_REGISTRO_RENTA")
    private BigInteger idRegistroRenta;

    public BancoPK() {
    }

    public BancoPK(String nombre, BigInteger idRegistroRenta) {
        this.nombre = nombre;
        this.idRegistroRenta = idRegistroRenta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigInteger getIdRegistroRenta() {
        return idRegistroRenta;
    }

    public void setIdRegistroRenta(BigInteger idRegistroRenta) {
        this.idRegistroRenta = idRegistroRenta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nombre != null ? nombre.hashCode() : 0);
        hash += (idRegistroRenta != null ? idRegistroRenta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BancoPK)) {
            return false;
        }
        BancoPK other = (BancoPK) object;
        if ((this.nombre == null && other.nombre != null) || (this.nombre != null && !this.nombre.equals(other.nombre))) {
            return false;
        }
        if ((this.idRegistroRenta == null && other.idRegistroRenta != null) || (this.idRegistroRenta != null && !this.idRegistroRenta.equals(other.idRegistroRenta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.BancoPK[ nombre=" + nombre + ", idRegistroRenta=" + idRegistroRenta + " ]";
    }
    
}
