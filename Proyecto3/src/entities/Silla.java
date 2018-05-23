/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author doria
 */
@Entity
@Table(name = "SILLA")
@NamedQueries({
    @NamedQuery(name = "Silla.findAll", query = "SELECT s FROM Silla s")
    , @NamedQuery(name = "Silla.findByNumFila", query = "SELECT s FROM Silla s WHERE s.sillaPK.numFila = :numFila")
    , @NamedQuery(name = "Silla.findByNumAsiento", query = "SELECT s FROM Silla s WHERE s.sillaPK.numAsiento = :numAsiento")
    , @NamedQuery(name = "Silla.findByNumPartido", query = "SELECT s FROM Silla s WHERE s.sillaPK.numPartido = :numPartido")
    , @NamedQuery(name = "Silla.findByEstado", query = "SELECT s FROM Silla s WHERE s.estado = :estado")
    , @NamedQuery(name = "Silla.findByPrecio", query = "SELECT s FROM Silla s WHERE s.precio = :precio")
    , @NamedQuery(name = "Silla.findByCategoria", query = "SELECT s FROM Silla s WHERE s.categoria = :categoria")})
public class Silla implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SillaPK sillaPK;
    @Column(name = "ESTADO")
    private String estado;
    @Column(name = "PRECIO")
    private Integer precio;
    @Basic(optional = false)
    @Column(name = "CATEGORIA")
    private short categoria;
    @JoinColumn(name = "NUM_PARTIDO", referencedColumnName = "NUM_PARTIDO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Partido partido;
    @JoinColumn(name = "IDENTIFICACION", referencedColumnName = "IDENTIFICACION")
    @ManyToOne
    private Usuario identificacion;

    public Silla() {
    }

    public Silla(SillaPK sillaPK) {
        this.sillaPK = sillaPK;
    }

    public Silla(SillaPK sillaPK, short categoria) {
        this.sillaPK = sillaPK;
        this.categoria = categoria;
    }

    public Silla(int numFila, int numAsiento, short numPartido) {
        this.sillaPK = new SillaPK(numFila, numAsiento, numPartido);
    }

    public SillaPK getSillaPK() {
        return sillaPK;
    }

    public void setSillaPK(SillaPK sillaPK) {
        this.sillaPK = sillaPK;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public short getCategoria() {
        return categoria;
    }

    public void setCategoria(short categoria) {
        this.categoria = categoria;
    }

    public Partido getPartido() {
        return partido;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }

    public Usuario getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(Usuario identificacion) {
        this.identificacion = identificacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sillaPK != null ? sillaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Silla)) {
            return false;
        }
        Silla other = (Silla) object;
        if ((this.sillaPK == null && other.sillaPK != null) || (this.sillaPK != null && !this.sillaPK.equals(other.sillaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Silla[ sillaPK=" + sillaPK + " ]";
    }
    
}
