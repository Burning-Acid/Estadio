/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
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
@Table(name = "GOL")
@NamedQueries({
    @NamedQuery(name = "Gol.findAll", query = "SELECT g FROM Gol g")
    , @NamedQuery(name = "Gol.findByCodEquipo", query = "SELECT g FROM Gol g WHERE g.golPK.codEquipo = :codEquipo")
    , @NamedQuery(name = "Gol.findByNumPartido", query = "SELECT g FROM Gol g WHERE g.golPK.numPartido = :numPartido")
    , @NamedQuery(name = "Gol.findByMinuto", query = "SELECT g FROM Gol g WHERE g.golPK.minuto = :minuto")
    , @NamedQuery(name = "Gol.findByTipo", query = "SELECT g FROM Gol g WHERE g.tipo = :tipo")
    , @NamedQuery(name = "Gol.findByNumJugador", query = "SELECT g FROM Gol g WHERE g.golPK.numJugador = :numJugador")})
public class Gol implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GolPK golPK;
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

    public Gol() {
    }

    public Gol(GolPK golPK) {
        this.golPK = golPK;
    }

    public Gol(short codEquipo, short numPartido, short minuto, short numJugador) {
        this.golPK = new GolPK(codEquipo, numPartido, minuto, numJugador);
    }

    public GolPK getGolPK() {
        return golPK;
    }

    public void setGolPK(GolPK golPK) {
        this.golPK = golPK;
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
        hash += (golPK != null ? golPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Gol)) {
            return false;
        }
        Gol other = (Gol) object;
        if ((this.golPK == null && other.golPK != null) || (this.golPK != null && !this.golPK.equals(other.golPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Gol[ golPK=" + golPK + " ]";
    }
    
}
