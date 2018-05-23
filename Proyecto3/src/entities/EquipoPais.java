/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
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

/**
 *
 * @author doria
 */
@Entity
@Table(name = "EQUIPO_PAIS")
@NamedQueries({
    @NamedQuery(name = "EquipoPais.findAll", query = "SELECT e FROM EquipoPais e")
    , @NamedQuery(name = "EquipoPais.findByCodEquipo", query = "SELECT e FROM EquipoPais e WHERE e.codEquipo = :codEquipo")
    , @NamedQuery(name = "EquipoPais.findByNombre", query = "SELECT e FROM EquipoPais e WHERE e.nombre = :nombre")
    , @NamedQuery(name = "EquipoPais.findByPuntos", query = "SELECT e FROM EquipoPais e WHERE e.puntos = :puntos")
    , @NamedQuery(name = "EquipoPais.findByAbreviacion", query = "SELECT e FROM EquipoPais e WHERE e.abreviacion = :abreviacion")})
public class EquipoPais implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "COD_EQUIPO")
    private Short codEquipo;
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "PUNTOS")
    private Short puntos;
    @Basic(optional = false)
    @Column(name = "ABREVIACION")
    private String abreviacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "equipoPais")
    private List<Jugador> jugadorList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codEquipoLocal")
    private List<Partido> partidoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codEquipoVisitante")
    private List<Partido> partidoList1;
    @JoinColumn(name = "COD_ENTRENADOR_AUXILIAR", referencedColumnName = "COD_ENTRENADOR")
    @ManyToOne
    private Entrenador codEntrenadorAuxiliar;
    @JoinColumn(name = "COD_ENTRENADOR_PRINCIPAL", referencedColumnName = "COD_ENTRENADOR")
    @ManyToOne
    private Entrenador codEntrenadorPrincipal;
    @JoinColumn(name = "COD_GRUPO", referencedColumnName = "COD_GRUPO")
    @ManyToOne
    private Grupo codGrupo;
    @JoinColumn(name = "COD_PAIS", referencedColumnName = "COD_PAIS")
    @ManyToOne
    private Pais codPais;

    public EquipoPais() {
    }

    public EquipoPais(Short codEquipo) {
        this.codEquipo = codEquipo;
    }

    public EquipoPais(Short codEquipo, String abreviacion) {
        this.codEquipo = codEquipo;
        this.abreviacion = abreviacion;
    }

    public Short getCodEquipo() {
        return codEquipo;
    }

    public void setCodEquipo(Short codEquipo) {
        this.codEquipo = codEquipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Short getPuntos() {
        return puntos;
    }

    public void setPuntos(Short puntos) {
        this.puntos = puntos;
    }

    public String getAbreviacion() {
        return abreviacion;
    }

    public void setAbreviacion(String abreviacion) {
        this.abreviacion = abreviacion;
    }

    public List<Jugador> getJugadorList() {
        return jugadorList;
    }

    public void setJugadorList(List<Jugador> jugadorList) {
        this.jugadorList = jugadorList;
    }

    public List<Partido> getPartidoList() {
        return partidoList;
    }

    public void setPartidoList(List<Partido> partidoList) {
        this.partidoList = partidoList;
    }

    public List<Partido> getPartidoList1() {
        return partidoList1;
    }

    public void setPartidoList1(List<Partido> partidoList1) {
        this.partidoList1 = partidoList1;
    }

    public Entrenador getCodEntrenadorAuxiliar() {
        return codEntrenadorAuxiliar;
    }

    public void setCodEntrenadorAuxiliar(Entrenador codEntrenadorAuxiliar) {
        this.codEntrenadorAuxiliar = codEntrenadorAuxiliar;
    }

    public Entrenador getCodEntrenadorPrincipal() {
        return codEntrenadorPrincipal;
    }

    public void setCodEntrenadorPrincipal(Entrenador codEntrenadorPrincipal) {
        this.codEntrenadorPrincipal = codEntrenadorPrincipal;
    }

    public Grupo getCodGrupo() {
        return codGrupo;
    }

    public void setCodGrupo(Grupo codGrupo) {
        this.codGrupo = codGrupo;
    }

    public Pais getCodPais() {
        return codPais;
    }

    public void setCodPais(Pais codPais) {
        this.codPais = codPais;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codEquipo != null ? codEquipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EquipoPais)) {
            return false;
        }
        EquipoPais other = (EquipoPais) object;
        if ((this.codEquipo == null && other.codEquipo != null) || (this.codEquipo != null && !this.codEquipo.equals(other.codEquipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.EquipoPais[ codEquipo=" + codEquipo + " ]";
    }
    
}
