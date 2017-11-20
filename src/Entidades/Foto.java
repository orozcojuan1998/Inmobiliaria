/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
 * @author Asus-pc
 */
@Entity
@Table(name = "FOTO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Foto.findAll", query = "SELECT f FROM Foto f")
    , @NamedQuery(name = "Foto.findByUrlFoto", query = "SELECT f FROM Foto f WHERE f.urlFoto = :urlFoto")
    , @NamedQuery(name = "Foto.findByFecha", query = "SELECT f FROM Foto f WHERE f.fecha = :fecha")
    , @NamedQuery(name = "Foto.findByPais", query = "SELECT f FROM Foto f WHERE f.pais = :pais")
    , @NamedQuery(name = "Foto.findByDescripcion", query = "SELECT f FROM Foto f WHERE f.descripcion = :descripcion")})
public class Foto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "URL_FOTO")
    private String urlFoto;
    @Basic(optional = false)
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "PAIS")
    private String pais;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @JoinColumns({
        @JoinColumn(name = "PROPIEDAD_ID_PROPIEDAD", referencedColumnName = "ID_PROPIEDAD")
        , @JoinColumn(name = "USUARIO", referencedColumnName = "USUARIO")})
    @ManyToOne(optional = false)
    private Propiedad propiedad;

    public Foto() {
    }

    public Foto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public Foto(String urlFoto, Date fecha) {
        this.urlFoto = urlFoto;
        this.fecha = fecha;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Propiedad getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(Propiedad propiedad) {
        this.propiedad = propiedad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (urlFoto != null ? urlFoto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Foto)) {
            return false;
        }
        Foto other = (Foto) object;
        if ((this.urlFoto == null && other.urlFoto != null) || (this.urlFoto != null && !this.urlFoto.equals(other.urlFoto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Foto[ urlFoto=" + urlFoto + " ]";
    }
    
}
