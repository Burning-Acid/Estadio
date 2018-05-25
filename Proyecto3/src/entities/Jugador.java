/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import static com.oracle.jrockit.jfr.ContentType.Bytes;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import static sun.security.krb5.Confounder.bytes;

/**
 *
 * @author doria
 */
@Entity
@Table(name = "JUGADOR")
@NamedQueries({
    @NamedQuery(name = "Jugador.findAll", query = "SELECT j FROM Jugador j")
    , @NamedQuery(name = "Jugador.findByNumJugador", query = "SELECT j FROM Jugador j WHERE j.jugadorPK.numJugador = :numJugador")
    , @NamedQuery(name = "Jugador.findByCodEquipo", query = "SELECT j FROM Jugador j WHERE j.jugadorPK.codEquipo = :codEquipo")
    , @NamedQuery(name = "Jugador.findByNombres", query = "SELECT j FROM Jugador j WHERE j.nombres = :nombres")
    , @NamedQuery(name = "Jugador.findByApellidos", query = "SELECT j FROM Jugador j WHERE j.apellidos = :apellidos")})
public class Jugador implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected JugadorPK jugadorPK;
    @Column(name = "NOMBRES")
    private String nombres;
    @Column(name = "APELLIDOS")
    private String apellidos;
    @Lob
    @Column(name = "FOTO")
    private byte [] foto;
    @JoinTable(name = "CLUBJUGADOR", joinColumns = {
        @JoinColumn(name = "COD_EQUIPO", referencedColumnName = "NUM_JUGADOR")
        , @JoinColumn(name = "NUM_JUGADOR", referencedColumnName = "COD_EQUIPO")}, inverseJoinColumns = {
        @JoinColumn(name = "COD_CLUB", referencedColumnName = "COD_CLUB")})
    @ManyToMany
    private List<Club> clubList;
    @JoinColumn(name = "COD_EQUIPO", referencedColumnName = "COD_EQUIPO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EquipoPais equipoPais;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jugador")
    private List<Gol> golList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jugador")
    private List<Posicion> posicionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jugador")
    private List<Tarjeta> tarjetaList;

    public Jugador() {
    }

    public Jugador(JugadorPK jugadorPK) {
        this.jugadorPK = jugadorPK;
    }

    public Jugador(short numJugador, short codEquipo) {
        this.jugadorPK = new JugadorPK(numJugador, codEquipo);
    }

    public JugadorPK getJugadorPK() {
        return jugadorPK;
    }

    public void setJugadorPK(JugadorPK jugadorPK) {
        this.jugadorPK = jugadorPK;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public byte [] getFoto() {
        return foto;
    }

    public void setFoto(byte [] foto) {
        this.foto = foto;
    }

    public List<Club> getClubList() {
        return clubList;
    }

    public void setClubList(List<Club> clubList) {
        this.clubList = clubList;
    }

    public EquipoPais getEquipoPais() {
        return equipoPais;
    }

    public void setEquipoPais(EquipoPais equipoPais) {
        this.equipoPais = equipoPais;
    }

    public List<Gol> getGolList() {
        return golList;
    }

    public void setGolList(List<Gol> golList) {
        this.golList = golList;
    }

    public List<Posicion> getPosicionList() {
        return posicionList;
    }

    public void setPosicionList(List<Posicion> posicionList) {
        this.posicionList = posicionList;
    }

    public List<Tarjeta> getTarjetaList() {
        return tarjetaList;
    }

    public void setTarjetaList(List<Tarjeta> tarjetaList) {
        this.tarjetaList = tarjetaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jugadorPK != null ? jugadorPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Jugador)) {
            return false;
        }
        Jugador other = (Jugador) object;
        if ((this.jugadorPK == null && other.jugadorPK != null) || (this.jugadorPK != null && !this.jugadorPK.equals(other.jugadorPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Jugador[ jugadorPK=" + jugadorPK + " ]";
    }
    
}
