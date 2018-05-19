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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author doria
 */
@Entity
@Table(name = "GRUPO")
@NamedQueries({
    @NamedQuery(name = "Grupo.findAll", query = "SELECT g FROM Grupo g")
    , @NamedQuery(name = "Grupo.findByCodGrupo", query = "SELECT g FROM Grupo g WHERE g.codGrupo = :codGrupo")
    , @NamedQuery(name = "Grupo.findByNomGrupo", query = "SELECT g FROM Grupo g WHERE g.nomGrupo = :nomGrupo")})
public class Grupo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "COD_GRUPO")
    private Short codGrupo;
    @Column(name = "NOM_GRUPO")
    private String nomGrupo;
    @OneToMany(mappedBy = "codGrupo")
    private List<EquipoPais> equipoPaisList;

    public Grupo() {
    }

    public Grupo(Short codGrupo) {
        this.codGrupo = codGrupo;
    }

    public Short getCodGrupo() {
        return codGrupo;
    }

    public void setCodGrupo(Short codGrupo) {
        this.codGrupo = codGrupo;
    }

    public String getNomGrupo() {
        return nomGrupo;
    }

    public void setNomGrupo(String nomGrupo) {
        this.nomGrupo = nomGrupo;
    }

    public List<EquipoPais> getEquipoPaisList() {
        return equipoPaisList;
    }

    public void setEquipoPaisList(List<EquipoPais> equipoPaisList) {
        this.equipoPaisList = equipoPaisList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codGrupo != null ? codGrupo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Grupo)) {
            return false;
        }
        Grupo other = (Grupo) object;
        if ((this.codGrupo == null && other.codGrupo != null) || (this.codGrupo != null && !this.codGrupo.equals(other.codGrupo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Grupo[ codGrupo=" + codGrupo + " ]";
    }
    
}
