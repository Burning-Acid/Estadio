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
@Table(name = "CONTINENTE")
@NamedQueries({
    @NamedQuery(name = "Continente.findAll", query = "SELECT c FROM Continente c")
    , @NamedQuery(name = "Continente.findByCodContinente", query = "SELECT c FROM Continente c WHERE c.codContinente = :codContinente")
    , @NamedQuery(name = "Continente.findByNombContinente", query = "SELECT c FROM Continente c WHERE c.nombContinente = :nombContinente")})
public class Continente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "COD_CONTINENTE")
    private Short codContinente;
    @Basic(optional = false)
    @Column(name = "NOMB_CONTINENTE")
    private String nombContinente;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codContinente")
    private List<Pais> paisList;

    public Continente() {
    }

    public Continente(Short codContinente) {
        this.codContinente = codContinente;
    }

    public Continente(Short codContinente, String nombContinente) {
        this.codContinente = codContinente;
        this.nombContinente = nombContinente;
    }

    public Short getCodContinente() {
        return codContinente;
    }

    public void setCodContinente(Short codContinente) {
        this.codContinente = codContinente;
    }

    public String getNombContinente() {
        return nombContinente;
    }

    public void setNombContinente(String nombContinente) {
        this.nombContinente = nombContinente;
    }

    public List<Pais> getPaisList() {
        return paisList;
    }

    public void setPaisList(List<Pais> paisList) {
        this.paisList = paisList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codContinente != null ? codContinente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Continente)) {
            return false;
        }
        Continente other = (Continente) object;
        if ((this.codContinente == null && other.codContinente != null) || (this.codContinente != null && !this.codContinente.equals(other.codContinente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Continente[ codContinente=" + codContinente + " ]";
    }
    
}
