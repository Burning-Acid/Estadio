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
@Table(name = "PAIS")
@NamedQueries({
    @NamedQuery(name = "Pais.findAll", query = "SELECT p FROM Pais p")
    , @NamedQuery(name = "Pais.findByCodPais", query = "SELECT p FROM Pais p WHERE p.codPais = :codPais")
    , @NamedQuery(name = "Pais.findByNombrePais", query = "SELECT p FROM Pais p WHERE p.nombrePais = :nombrePais")
    , @NamedQuery(name = "Pais.findByNumHabitantes", query = "SELECT p FROM Pais p WHERE p.numHabitantes = :numHabitantes")})
public class Pais implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "COD_PAIS")
    private Short codPais;
    @Basic(optional = false)
    @Column(name = "NOMBRE_PAIS")
    private String nombrePais;
    @Basic(optional = false)
    @Column(name = "NUM_HABITANTES")
    private long numHabitantes;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codPais")
    private List<Juez> juezList;
    @OneToMany(mappedBy = "codPais")
    private List<EquipoPais> equipoPaisList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codPais")
    private List<Usuario> usuarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codPais")
    private List<Entrenador> entrenadorList;
    @JoinColumn(name = "COD_CONTINENTE", referencedColumnName = "COD_CONTINENTE")
    @ManyToOne(optional = false)
    private Continente codContinente;

    public Pais() {
    }

    public Pais(Short codPais) {
        this.codPais = codPais;
    }

    public Pais(Short codPais, String nombrePais, long numHabitantes) {
        this.codPais = codPais;
        this.nombrePais = nombrePais;
        this.numHabitantes = numHabitantes;
    }

    public Short getCodPais() {
        return codPais;
    }

    public void setCodPais(Short codPais) {
        this.codPais = codPais;
    }

    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }

    public long getNumHabitantes() {
        return numHabitantes;
    }

    public void setNumHabitantes(long numHabitantes) {
        this.numHabitantes = numHabitantes;
    }

    public List<Juez> getJuezList() {
        return juezList;
    }

    public void setJuezList(List<Juez> juezList) {
        this.juezList = juezList;
    }

    public List<EquipoPais> getEquipoPaisList() {
        return equipoPaisList;
    }

    public void setEquipoPaisList(List<EquipoPais> equipoPaisList) {
        this.equipoPaisList = equipoPaisList;
    }

    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    public List<Entrenador> getEntrenadorList() {
        return entrenadorList;
    }

    public void setEntrenadorList(List<Entrenador> entrenadorList) {
        this.entrenadorList = entrenadorList;
    }

    public Continente getCodContinente() {
        return codContinente;
    }

    public void setCodContinente(Continente codContinente) {
        this.codContinente = codContinente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codPais != null ? codPais.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pais)) {
            return false;
        }
        Pais other = (Pais) object;
        if ((this.codPais == null && other.codPais != null) || (this.codPais != null && !this.codPais.equals(other.codPais))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Pais[ codPais=" + codPais + " ]";
    }
    
}
