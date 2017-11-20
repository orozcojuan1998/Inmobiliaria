/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Asus-pc
 */
@Entity
@Table(name = "WAIT_LIST")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WaitList.findAll", query = "SELECT w FROM WaitList w")
    , @NamedQuery(name = "WaitList.findByClienteIdCliente", query = "SELECT w FROM WaitList w WHERE w.waitListPK.clienteIdCliente = :clienteIdCliente")
    , @NamedQuery(name = "WaitList.findByIdPropiedad", query = "SELECT w FROM WaitList w WHERE w.waitListPK.idPropiedad = :idPropiedad")})
public class WaitList implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected WaitListPK waitListPK;
    @JoinColumn(name = "CLIENTE_ID_CLIENTE", referencedColumnName = "USUARIO", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Cliente cliente;

    public WaitList() {
    }

    public WaitList(WaitListPK waitListPK) {
        this.waitListPK = waitListPK;
    }

    public WaitList(String clienteIdCliente, BigInteger idPropiedad) {
        this.waitListPK = new WaitListPK(clienteIdCliente, idPropiedad);
    }

    public WaitListPK getWaitListPK() {
        return waitListPK;
    }

    public void setWaitListPK(WaitListPK waitListPK) {
        this.waitListPK = waitListPK;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (waitListPK != null ? waitListPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WaitList)) {
            return false;
        }
        WaitList other = (WaitList) object;
        if ((this.waitListPK == null && other.waitListPK != null) || (this.waitListPK != null && !this.waitListPK.equals(other.waitListPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.WaitList[ waitListPK=" + waitListPK + " ]";
    }
    
}
