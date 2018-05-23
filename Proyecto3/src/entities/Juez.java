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
@Table(name = "JUEZ")
@NamedQueries({
    @NamedQuery(name = "Juez.findAll", query = "SELECT j FROM Juez j")
    , @NamedQuery(name = "Juez.findByCodJuez", query = "SELECT j FROM Juez j WHERE j.codJuez = :codJuez")
    , @NamedQuery(name = "Juez.findByNombres", query = "SELECT j FROM Juez j WHERE j.nombres = :nombres")
    , @NamedQuery(name = "Juez.findByApellidos", query = "SELECT j FROM Juez j WHERE j.apellidos = :apellidos")})
public class Juez implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "COD_JUEZ")
    private Short codJuez;
    @Column(name = "NOMBRES")
    private String nombres;
    @Column(name = "APELLIDOS")
    private String apellidos;
    @JoinColumn(name = "COD_PAIS", referencedColumnName = "COD_PAIS")
    @ManyToOne(optional = false)
    private Pais codPais;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "juez")
    private List<Rol> rolList;

    public Juez() {
    }

    public Juez(Short codJuez) {
        this.codJuez = codJuez;
    }

    public Short getCodJuez() {
        return codJuez;
    }

    public void setCodJuez(Short codJuez) {
        this.codJuez = codJuez;
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

    public Pais getCodPais() {
        return codPais;
    }

    public void setCodPais(Pais codPais) {
        this.codPais = codPais;
    }

    public List<Rol> getRolList() {
        return rolList;
    }

    public void setRolList(List<Rol> rolList) {
        this.rolList = rolList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codJuez != null ? codJuez.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Juez)) {
            return false;
        }
        Juez other = (Juez) object;
        if ((this.codJuez == null && other.codJuez != null) || (this.codJuez != null && !this.codJuez.equals(other.codJuez))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Juez[ codJuez=" + codJuez + " ]";
    }
    
}
