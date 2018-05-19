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
@Table(name = "ROL")
@NamedQueries({
    @NamedQuery(name = "Rol.findAll", query = "SELECT r FROM Rol r")
    , @NamedQuery(name = "Rol.findByNumPartido", query = "SELECT r FROM Rol r WHERE r.rolPK.numPartido = :numPartido")
    , @NamedQuery(name = "Rol.findByCodJuez", query = "SELECT r FROM Rol r WHERE r.rolPK.codJuez = :codJuez")
    , @NamedQuery(name = "Rol.findByRol", query = "SELECT r FROM Rol r WHERE r.rol = :rol")})
public class Rol implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RolPK rolPK;
    @Basic(optional = false)
    @Column(name = "ROL")
    private String rol;
    @JoinColumn(name = "COD_JUEZ", referencedColumnName = "COD_JUEZ", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Juez juez;
    @JoinColumn(name = "NUM_PARTIDO", referencedColumnName = "NUM_PARTIDO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Partido partido;

    public Rol() {
    }

    public Rol(RolPK rolPK) {
        this.rolPK = rolPK;
    }

    public Rol(RolPK rolPK, String rol) {
        this.rolPK = rolPK;
        this.rol = rol;
    }

    public Rol(short numPartido, short codJuez) {
        this.rolPK = new RolPK(numPartido, codJuez);
    }

    public RolPK getRolPK() {
        return rolPK;
    }

    public void setRolPK(RolPK rolPK) {
        this.rolPK = rolPK;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Juez getJuez() {
        return juez;
    }

    public void setJuez(Juez juez) {
        this.juez = juez;
    }

    public Partido getPartido() {
        return partido;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rolPK != null ? rolPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rol)) {
            return false;
        }
        Rol other = (Rol) object;
        if ((this.rolPK == null && other.rolPK != null) || (this.rolPK != null && !this.rolPK.equals(other.rolPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Rol[ rolPK=" + rolPK + " ]";
    }
    
}
