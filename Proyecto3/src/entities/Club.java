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
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author doria
 */
@Entity
@Table(name = "CLUB")
@NamedQueries({
    @NamedQuery(name = "Club.findAll", query = "SELECT c FROM Club c")
    , @NamedQuery(name = "Club.findByCodClub", query = "SELECT c FROM Club c WHERE c.codClub = :codClub")
    , @NamedQuery(name = "Club.findByNombreClub", query = "SELECT c FROM Club c WHERE c.nombreClub = :nombreClub")})
public class Club implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "COD_CLUB")
    private Integer codClub;
    @Column(name = "NOMBRE_CLUB")
    private String nombreClub;
    @ManyToMany(mappedBy = "clubList")
    private List<Jugador> jugadorList;

    public Club() {
    }

    public Club(Integer codClub) {
        this.codClub = codClub;
    }

    public Integer getCodClub() {
        return codClub;
    }

    public void setCodClub(Integer codClub) {
        this.codClub = codClub;
    }

    public String getNombreClub() {
        return nombreClub;
    }

    public void setNombreClub(String nombreClub) {
        this.nombreClub = nombreClub;
    }

    public List<Jugador> getJugadorList() {
        return jugadorList;
    }

    public void setJugadorList(List<Jugador> jugadorList) {
        this.jugadorList = jugadorList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codClub != null ? codClub.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Club)) {
            return false;
        }
        Club other = (Club) object;
        if ((this.codClub == null && other.codClub != null) || (this.codClub != null && !this.codClub.equals(other.codClub))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Club[ codClub=" + codClub + " ]";
    }
    
}
