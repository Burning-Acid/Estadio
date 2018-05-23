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
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author doria
 */
@Entity
@Table(name = "POSICION")
@NamedQueries({
    @NamedQuery(name = "Posicion.findAll", query = "SELECT p FROM Posicion p")
    , @NamedQuery(name = "Posicion.findByCodEquipo", query = "SELECT p FROM Posicion p WHERE p.posicionPK.codEquipo = :codEquipo")
    , @NamedQuery(name = "Posicion.findByNumJugador", query = "SELECT p FROM Posicion p WHERE p.posicionPK.numJugador = :numJugador")
    , @NamedQuery(name = "Posicion.findByNumPartido", query = "SELECT p FROM Posicion p WHERE p.posicionPK.numPartido = :numPartido")
    , @NamedQuery(name = "Posicion.findByPosicion", query = "SELECT p FROM Posicion p WHERE p.posicion = :posicion")
    , @NamedQuery(name = "Posicion.findByTipo", query = "SELECT p FROM Posicion p WHERE p.tipo = :tipo")})
public class Posicion implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PosicionPK posicionPK;
    @Basic(optional = false)
    @Column(name = "POSICION")
    private String posicion;
    @Basic(optional = false)
    @Column(name = "TIPO")
    private String tipo;
    @JoinColumns({
        @JoinColumn(name = "NUM_JUGADOR", referencedColumnName = "NUM_JUGADOR", insertable = false, updatable = false)
        , @JoinColumn(name = "COD_EQUIPO", referencedColumnName = "COD_EQUIPO", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Jugador jugador;
    @JoinColumn(name = "NUM_PARTIDO", referencedColumnName = "NUM_PARTIDO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Partido partido;

    public Posicion() {
    }

    public Posicion(PosicionPK posicionPK) {
        this.posicionPK = posicionPK;
    }

    public Posicion(PosicionPK posicionPK, String posicion, String tipo) {
        this.posicionPK = posicionPK;
        this.posicion = posicion;
        this.tipo = tipo;
    }

    public Posicion(short codEquipo, short numJugador, short numPartido) {
        this.posicionPK = new PosicionPK(codEquipo, numJugador, numPartido);
    }

    public PosicionPK getPosicionPK() {
        return posicionPK;
    }

    public void setPosicionPK(PosicionPK posicionPK) {
        this.posicionPK = posicionPK;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
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
        hash += (posicionPK != null ? posicionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Posicion)) {
            return false;
        }
        Posicion other = (Posicion) object;
        if ((this.posicionPK == null && other.posicionPK != null) || (this.posicionPK != null && !this.posicionPK.equals(other.posicionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Posicion[ posicionPK=" + posicionPK + " ]";
    }
    
}
