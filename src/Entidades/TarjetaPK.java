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
public class TarjetaPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "ID_TARJETA")
    private BigInteger idTarjeta;
    @Basic(optional = false)
    @Column(name = "CLIENTE_ID_CLIENTE")
    private String clienteIdCliente;

    public TarjetaPK() {
    }

    public TarjetaPK(BigInteger idTarjeta, String clienteIdCliente) {
        this.idTarjeta = idTarjeta;
        this.clienteIdCliente = clienteIdCliente;
    }

    public BigInteger getIdTarjeta() {
        return idTarjeta;
    }

    public void setIdTarjeta(BigInteger idTarjeta) {
        this.idTarjeta = idTarjeta;
    }

    public String getClienteIdCliente() {
        return clienteIdCliente;
    }

    public void setClienteIdCliente(String clienteIdCliente) {
        this.clienteIdCliente = clienteIdCliente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTarjeta != null ? idTarjeta.hashCode() : 0);
        hash += (clienteIdCliente != null ? clienteIdCliente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TarjetaPK)) {
            return false;
        }
        TarjetaPK other = (TarjetaPK) object;
        if ((this.idTarjeta == null && other.idTarjeta != null) || (this.idTarjeta != null && !this.idTarjeta.equals(other.idTarjeta))) {
            return false;
        }
        if ((this.clienteIdCliente == null && other.clienteIdCliente != null) || (this.clienteIdCliente != null && !this.clienteIdCliente.equals(other.clienteIdCliente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.TarjetaPK[ idTarjeta=" + idTarjeta + ", clienteIdCliente=" + clienteIdCliente + " ]";
    }
    
}
