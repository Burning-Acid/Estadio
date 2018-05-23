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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author doria
 */
@Entity
@Table(name = "ESTADIO")
@NamedQueries({
    @NamedQuery(name = "Estadio.findAll", query = "SELECT e FROM Estadio e")
    , @NamedQuery(name = "Estadio.findByCodEstadio", query = "SELECT e FROM Estadio e WHERE e.codEstadio = :codEstadio")
    , @NamedQuery(name = "Estadio.findByNombre", query = "SELECT e FROM Estadio e WHERE e.nombre = :nombre")
    , @NamedQuery(name = "Estadio.findByCiudad", query = "SELECT e FROM Estadio e WHERE e.ciudad = :ciudad")
    , @NamedQuery(name = "Estadio.findByCapacidad", query = "SELECT e FROM Estadio e WHERE e.capacidad = :capacidad")})
public class Estadio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "COD_ESTADIO")
    private Long codEstadio;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "CIUDAD")
    private String ciudad;
    @Basic(optional = false)
    @Column(name = "CAPACIDAD")
    private int capacidad;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codEstadio")
    private List<Partido> partidoList;

    public Estadio() {
    }

    public Estadio(Long codEstadio) {
        this.codEstadio = codEstadio;
    }

    public Estadio(Long codEstadio, String nombre, String ciudad, int capacidad) {
        this.codEstadio = codEstadio;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.capacidad = capacidad;
    }

    public Long getCodEstadio() {
        return codEstadio;
    }

    public void setCodEstadio(Long codEstadio) {
        this.codEstadio = codEstadio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public List<Partido> getPartidoList() {
        return partidoList;
    }

    public void setPartidoList(List<Partido> partidoList) {
        this.partidoList = partidoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codEstadio != null ? codEstadio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Estadio)) {
            return false;
        }
        Estadio other = (Estadio) object;
        if ((this.codEstadio == null && other.codEstadio != null) || (this.codEstadio != null && !this.codEstadio.equals(other.codEstadio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Estadio[ codEstadio=" + codEstadio + " ]";
    }
    
}
