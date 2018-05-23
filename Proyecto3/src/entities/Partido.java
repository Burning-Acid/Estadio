/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author doria
 */
@Entity
@Table(name = "PARTIDO")
@NamedQueries({
    @NamedQuery(name = "Partido.findAll", query = "SELECT p FROM Partido p")
    , @NamedQuery(name = "Partido.findByNumPartido", query = "SELECT p FROM Partido p WHERE p.numPartido = :numPartido")
    , @NamedQuery(name = "Partido.findByFecha", query = "SELECT p FROM Partido p WHERE p.fecha = :fecha")
    , @NamedQuery(name = "Partido.findByFase", query = "SELECT p FROM Partido p WHERE p.fase = :fase")})
public class Partido implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "NUM_PARTIDO")
    private Short numPartido;
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @Column(name = "FASE")
    private short fase;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "partido")
    private List<Gol> golList;
    @JoinColumn(name = "COD_EQUIPO_LOCAL", referencedColumnName = "COD_EQUIPO")
    @ManyToOne(optional = false)
    private EquipoPais codEquipoLocal;
    @JoinColumn(name = "COD_EQUIPO_VISITANTE", referencedColumnName = "COD_EQUIPO")
    @ManyToOne(optional = false)
    private EquipoPais codEquipoVisitante;
    @JoinColumn(name = "COD_ESTADIO", referencedColumnName = "COD_ESTADIO")
    @ManyToOne(optional = false)
    private Estadio codEstadio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "partido")
    private List<Posicion> posicionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "partido")
    private List<Rol> rolList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "partido")
    private List<Tarjeta> tarjetaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "partido")
    private List<Silla> sillaList;

    public Partido() {
    }

    public Partido(Short numPartido) {
        this.numPartido = numPartido;
    }

    public Partido(Short numPartido, short fase) {
        this.numPartido = numPartido;
        this.fase = fase;
    }

    public Short getNumPartido() {
        return numPartido;
    }

    public void setNumPartido(Short numPartido) {
        this.numPartido = numPartido;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public short getFase() {
        return fase;
    }

    public void setFase(short fase) {
        this.fase = fase;
    }

    public List<Gol> getGolList() {
        return golList;
    }

    public void setGolList(List<Gol> golList) {
        this.golList = golList;
    }

    public EquipoPais getCodEquipoLocal() {
        return codEquipoLocal;
    }

    public void setCodEquipoLocal(EquipoPais codEquipoLocal) {
        this.codEquipoLocal = codEquipoLocal;
    }

    public EquipoPais getCodEquipoVisitante() {
        return codEquipoVisitante;
    }

    public void setCodEquipoVisitante(EquipoPais codEquipoVisitante) {
        this.codEquipoVisitante = codEquipoVisitante;
    }

    public Estadio getCodEstadio() {
        return codEstadio;
    }

    public void setCodEstadio(Estadio codEstadio) {
        this.codEstadio = codEstadio;
    }

    public List<Posicion> getPosicionList() {
        return posicionList;
    }

    public void setPosicionList(List<Posicion> posicionList) {
        this.posicionList = posicionList;
    }

    public List<Rol> getRolList() {
        return rolList;
    }

    public void setRolList(List<Rol> rolList) {
        this.rolList = rolList;
    }

    public List<Tarjeta> getTarjetaList() {
        return tarjetaList;
    }

    public void setTarjetaList(List<Tarjeta> tarjetaList) {
        this.tarjetaList = tarjetaList;
    }

    public List<Silla> getSillaList() {
        return sillaList;
    }

    public void setSillaList(List<Silla> sillaList) {
        this.sillaList = sillaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numPartido != null ? numPartido.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Partido)) {
            return false;
        }
        Partido other = (Partido) object;
        if ((this.numPartido == null && other.numPartido != null) || (this.numPartido != null && !this.numPartido.equals(other.numPartido))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Partido[ numPartido=" + numPartido + " ]";
    }
    
}
