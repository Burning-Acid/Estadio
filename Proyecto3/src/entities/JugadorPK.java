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
public class JugadorPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "NUM_JUGADOR")
    private short numJugador;
    @Basic(optional = false)
    @Column(name = "COD_EQUIPO")
    private short codEquipo;

    public JugadorPK() {
    }

    public JugadorPK(short numJugador, short codEquipo) {
        this.numJugador = numJugador;
        this.codEquipo = codEquipo;
    }

    public short getNumJugador() {
        return numJugador;
    }

    public void setNumJugador(short numJugador) {
        this.numJugador = numJugador;
    }

    public short getCodEquipo() {
        return codEquipo;
    }

    public void setCodEquipo(short codEquipo) {
        this.codEquipo = codEquipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) numJugador;
        hash += (int) codEquipo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JugadorPK)) {
            return false;
        }
        JugadorPK other = (JugadorPK) object;
        if (this.numJugador != other.numJugador) {
            return false;
        }
        if (this.codEquipo != other.codEquipo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.JugadorPK[ numJugador=" + numJugador + ", codEquipo=" + codEquipo + " ]";
    }
    
}
