/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
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
@Table(name = "ENTRENADOR")
@NamedQueries({
    @NamedQuery(name = "Entrenador.findAll", query = "SELECT e FROM Entrenador e")
    , @NamedQuery(name = "Entrenador.findByCodEntrenador", query = "SELECT e FROM Entrenador e WHERE e.codEntrenador = :codEntrenador")
    , @NamedQuery(name = "Entrenador.findByNombres", query = "SELECT e FROM Entrenador e WHERE e.nombres = :nombres")
    , @NamedQuery(name = "Entrenador.findByApellidos", query = "SELECT e FROM Entrenador e WHERE e.apellidos = :apellidos")})
public class Entrenador implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "COD_ENTRENADOR")
    private Integer codEntrenador;
    @Column(name = "NOMBRES")
    private String nombres;
    @Column(name = "APELLIDOS")
    private String apellidos;
    @OneToMany(mappedBy = "codEntrenadorAuxiliar")
    private List<EquipoPais> equipoPaisList;
    @OneToMany(mappedBy = "codEntrenadorPrincipal")
    private List<EquipoPais> equipoPaisList1;
    @JoinColumn(name = "COD_PAIS", referencedColumnName = "COD_PAIS")
    @ManyToOne(optional = false)
    private Pais codPais;

    public Entrenador() {
    }

    public Entrenador(Integer codEntrenador) {
        this.codEntrenador = codEntrenador;
    }

    public Integer getCodEntrenador() {
        return codEntrenador;
    }

    public void setCodEntrenador(Integer codEntrenador) {
        this.codEntrenador = codEntrenador;
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

    public List<EquipoPais> getEquipoPaisList() {
        return equipoPaisList;
    }

    public void setEquipoPaisList(List<EquipoPais> equipoPaisList) {
        this.equipoPaisList = equipoPaisList;
    }

    public List<EquipoPais> getEquipoPaisList1() {
        return equipoPaisList1;
    }

    public void setEquipoPaisList1(List<EquipoPais> equipoPaisList1) {
        this.equipoPaisList1 = equipoPaisList1;
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
        hash += (codEntrenador != null ? codEntrenador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Entrenador)) {
            return false;
        }
        Entrenador other = (Entrenador) object;
        if ((this.codEntrenador == null && other.codEntrenador != null) || (this.codEntrenador != null && !this.codEntrenador.equals(other.codEntrenador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Entrenador[ codEntrenador=" + codEntrenador + " ]";
    }
    
}
