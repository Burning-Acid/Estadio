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
public class SillaPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "NUM_FILA")
    private int numFila;
    @Basic(optional = false)
    @Column(name = "NUM_ASIENTO")
    private int numAsiento;
    @Basic(optional = false)
    @Column(name = "NUM_PARTIDO")
    private short numPartido;

    public SillaPK() {
    }

    public SillaPK(int numFila, int numAsiento, short numPartido) {
        this.numFila = numFila;
        this.numAsiento = numAsiento;
        this.numPartido = numPartido;
    }

    public int getNumFila() {
        return numFila;
    }

    public void setNumFila(int numFila) {
        this.numFila = numFila;
    }

    public int getNumAsiento() {
        return numAsiento;
    }

    public void setNumAsiento(int numAsiento) {
        this.numAsiento = numAsiento;
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
        hash += (int) numFila;
        hash += (int) numAsiento;
        hash += (int) numPartido;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SillaPK)) {
            return false;
        }
        SillaPK other = (SillaPK) object;
        if (this.numFila != other.numFila) {
            return false;
        }
        if (this.numAsiento != other.numAsiento) {
            return false;
        }
        if (this.numPartido != other.numPartido) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.SillaPK[ numFila=" + numFila + ", numAsiento=" + numAsiento + ", numPartido=" + numPartido + " ]";
    }
    
}
