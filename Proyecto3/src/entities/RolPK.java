/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author doria
 */
@Embeddable
public class RolPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "NUM_PARTIDO")
    private short numPartido;
    @Basic(optional = false)
    @Column(name = "COD_JUEZ")
    private short codJuez;

    public RolPK() {
    }

    public RolPK(short numPartido, short codJuez) {
        this.numPartido = numPartido;
        this.codJuez = codJuez;
    }

    public short getNumPartido() {
        return numPartido;
    }

    public void setNumPartido(short numPartido) {
        this.numPartido = numPartido;
    }

    public short getCodJuez() {
        return codJuez;
    }

    public void setCodJuez(short codJuez) {
        this.codJuez = codJuez;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) numPartido;
        hash += (int) codJuez;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RolPK)) {
            return false;
        }
        RolPK other = (RolPK) object;
        if (this.numPartido != other.numPartido) {
            return false;
        }
        if (this.codJuez != other.codJuez) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.RolPK[ numPartido=" + numPartido + ", codJuez=" + codJuez + " ]";
    }
    
}
