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
public class WaitListPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "CLIENTE_ID_CLIENTE")
    private String clienteIdCliente;
    @Basic(optional = false)
    @Column(name = "ID_PROPIEDAD")
    private BigInteger idPropiedad;

    public WaitListPK() {
    }

    public WaitListPK(String clienteIdCliente, BigInteger idPropiedad) {
        this.clienteIdCliente = clienteIdCliente;
        this.idPropiedad = idPropiedad;
    }

    public String getClienteIdCliente() {
        return clienteIdCliente;
    }

    public void setClienteIdCliente(String clienteIdCliente) {
        this.clienteIdCliente = clienteIdCliente;
    }

    public BigInteger getIdPropiedad() {
        return idPropiedad;
    }

    public void setIdPropiedad(BigInteger idPropiedad) {
        this.idPropiedad = idPropiedad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clienteIdCliente != null ? clienteIdCliente.hashCode() : 0);
        hash += (idPropiedad != null ? idPropiedad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WaitListPK)) {
            return false;
        }
        WaitListPK other = (WaitListPK) object;
        if ((this.clienteIdCliente == null && other.clienteIdCliente != null) || (this.clienteIdCliente != null && !this.clienteIdCliente.equals(other.clienteIdCliente))) {
            return false;
        }
        if ((this.idPropiedad == null && other.idPropiedad != null) || (this.idPropiedad != null && !this.idPropiedad.equals(other.idPropiedad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.WaitListPK[ clienteIdCliente=" + clienteIdCliente + ", idPropiedad=" + idPropiedad + " ]";
    }
    
}
