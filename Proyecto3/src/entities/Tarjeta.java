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
@Table(name = "TARJETA")
@NamedQueries({
    @NamedQuery(name = "Tarjeta.findAll", query = "SELECT t FROM Tarjeta t")
    , @NamedQuery(name = "Tarjeta.findByTipo", query = "SELECT t FROM Tarjeta t WHERE t.tipo = :tipo")
    , @NamedQuery(name = "Tarjeta.findByMinuto", query = "SELECT t FROM Tarjeta t WHERE t.tarjetaPK.minuto = :minuto")
    , @NamedQuery(name = "Tarjeta.findByNumPartido", query = "SELECT t FROM Tarjeta t WHERE t.tarjetaPK.numPartido = :numPartido")
    , @NamedQuery(name = "Tarjeta.findByNumJugador", query = "SELECT t FROM Tarjeta t WHERE t.tarjetaPK.numJugador = :numJugador")
    , @NamedQuery(name = "Tarjeta.findByCodEquipo", query = "SELECT t FROM Tarjeta t WHERE t.tarjetaPK.codEquipo = :codEquipo")})
public class Tarjeta implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TarjetaPK tarjetaPK;
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

    public Tarjeta() {
    }

    public Tarjeta(TarjetaPK tarjetaPK) {
        this.tarjetaPK = tarjetaPK;
    }

    public Tarjeta(short minuto, short numPartido, short numJugador, short codEquipo) {
        this.tarjetaPK = new TarjetaPK(minuto, numPartido, numJugador, codEquipo);
    }

    public TarjetaPK getTarjetaPK() {
        return tarjetaPK;
    }

    public void setTarjetaPK(TarjetaPK tarjetaPK) {
        this.tarjetaPK = tarjetaPK;
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
        hash += (tarjetaPK != null ? tarjetaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tarjeta)) {
            return false;
        }
        Tarjeta other = (Tarjeta) object;
        if ((this.tarjetaPK == null && other.tarjetaPK != null) || (this.tarjetaPK != null && !this.tarjetaPK.equals(other.tarjetaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Tarjeta[ tarjetaPK=" + tarjetaPK + " ]";
    }
    
}
