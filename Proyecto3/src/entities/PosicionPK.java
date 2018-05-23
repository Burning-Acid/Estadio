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
public class PosicionPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "COD_EQUIPO")
    private short codEquipo;
    @Basic(optional = false)
    @Column(name = "NUM_JUGADOR")
    private short numJugador;
    @Basic(optional = false)
    @Column(name = "NUM_PARTIDO")
    private short numPartido;

    public PosicionPK() {
    }

    public PosicionPK(short codEquipo, short numJugador, short numPartido) {
        this.codEquipo = codEquipo;
        this.numJugador = numJugador;
        this.numPartido = numPartido;
    }

    public short getCodEquipo() {
        return codEquipo;
    }

    public void setCodEquipo(short codEquipo) {
        this.codEquipo = codEquipo;
    }

    public short getNumJugador() {
        return numJugador;
    }

    public void setNumJugador(short numJugador) {
        this.numJugador = numJugador;
    }

    public short getNumPartido() {
        return numPartido;
    }

    public void setNumPartido(short numPartido) {
        this.numPartido = numPartido;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEquipo;
        hash += (int) numJugador;
        hash += (int) numPartido;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PosicionPK)) {
            return false;
        }
        PosicionPK other = (PosicionPK) object;
        if (this.codEquipo != other.codEquipo) {
            return false;
        }
        if (this.numJugador != other.numJugador) {
            return false;
        }
        if (this.numPartido != other.numPartido) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PosicionPK[ codEquipo=" + codEquipo + ", numJugador=" + numJugador + ", numPartido=" + numPartido + " ]";
    }
    
}
