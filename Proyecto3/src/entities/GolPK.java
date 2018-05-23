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
public class GolPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "COD_EQUIPO")
    private short codEquipo;
    @Basic(optional = false)
    @Column(name = "NUM_PARTIDO")
    private short numPartido;
    @Basic(optional = false)
    @Column(name = "MINUTO")
    private short minuto;
    @Basic(optional = false)
    @Column(name = "NUM_JUGADOR")
    private short numJugador;

    public GolPK() {
    }

    public GolPK(short codEquipo, short numPartido, short minuto, short numJugador) {
        this.codEquipo = codEquipo;
        this.numPartido = numPartido;
        this.minuto = minuto;
        this.numJugador = numJugador;
    }

    public short getCodEquipo() {
        return codEquipo;
    }

    public void setCodEquipo(short codEquipo) {
        this.codEquipo = codEquipo;
    }

    public short getNumPartido() {
        return numPartido;
    }

    public void setNumPartido(short numPartido) {
        this.numPartido = numPartido;
    }

    public short getMinuto() {
        return minuto;
    }

    public void setMinuto(short minuto) {
        this.minuto = minuto;
    }

    public short getNumJugador() {
        return numJugador;
    }

    public void setNumJugador(short numJugador) {
        this.numJugador = numJugador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codEquipo;
        hash += (int) numPartido;
        hash += (int) minuto;
        hash += (int) numJugador;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GolPK)) {
            return false;
        }
        GolPK other = (GolPK) object;
        if (this.codEquipo != other.codEquipo) {
            return false;
        }
        if (this.numPartido != other.numPartido) {
            return false;
        }
        if (this.minuto != other.minuto) {
            return false;
        }
        if (this.numJugador != other.numJugador) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.GolPK[ codEquipo=" + codEquipo + ", numPartido=" + numPartido + ", minuto=" + minuto + ", numJugador=" + numJugador + " ]";
    }
    
}
