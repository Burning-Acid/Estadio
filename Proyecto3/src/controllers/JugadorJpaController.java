/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.IllegalOrphanException;
import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.EquipoPais;
import entities.Club;
import java.util.ArrayList;
import java.util.List;
import entities.Gol;
import entities.Jugador;
import entities.JugadorPK;
import entities.Posicion;
import entities.Tarjeta;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author linam
 */
public class JugadorJpaController implements Serializable {

    public JugadorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Jugador jugador) throws PreexistingEntityException, Exception {
        if (jugador.getJugadorPK() == null) {
            jugador.setJugadorPK(new JugadorPK());
        }
        if (jugador.getClubList() == null) {
            jugador.setClubList(new ArrayList<Club>());
        }
        if (jugador.getGolList() == null) {
            jugador.setGolList(new ArrayList<Gol>());
        }
        if (jugador.getPosicionList() == null) {
            jugador.setPosicionList(new ArrayList<Posicion>());
        }
        if (jugador.getTarjetaList() == null) {
            jugador.setTarjetaList(new ArrayList<Tarjeta>());
        }
        jugador.getJugadorPK().setCodEquipo(jugador.getEquipoPais().getCodEquipo());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EquipoPais equipoPais = jugador.getEquipoPais();
            if (equipoPais != null) {
                equipoPais = em.getReference(equipoPais.getClass(), equipoPais.getCodEquipo());
                jugador.setEquipoPais(equipoPais);
            }
            List<Club> attachedClubList = new ArrayList<Club>();
            for (Club clubListClubToAttach : jugador.getClubList()) {
                clubListClubToAttach = em.getReference(clubListClubToAttach.getClass(), clubListClubToAttach.getCodClub());
                attachedClubList.add(clubListClubToAttach);
            }
            jugador.setClubList(attachedClubList);
            List<Gol> attachedGolList = new ArrayList<Gol>();
            for (Gol golListGolToAttach : jugador.getGolList()) {
                golListGolToAttach = em.getReference(golListGolToAttach.getClass(), golListGolToAttach.getGolPK());
                attachedGolList.add(golListGolToAttach);
            }
            jugador.setGolList(attachedGolList);
            List<Posicion> attachedPosicionList = new ArrayList<Posicion>();
            for (Posicion posicionListPosicionToAttach : jugador.getPosicionList()) {
                posicionListPosicionToAttach = em.getReference(posicionListPosicionToAttach.getClass(), posicionListPosicionToAttach.getPosicionPK());
                attachedPosicionList.add(posicionListPosicionToAttach);
            }
            jugador.setPosicionList(attachedPosicionList);
            List<Tarjeta> attachedTarjetaList = new ArrayList<Tarjeta>();
            for (Tarjeta tarjetaListTarjetaToAttach : jugador.getTarjetaList()) {
                tarjetaListTarjetaToAttach = em.getReference(tarjetaListTarjetaToAttach.getClass(), tarjetaListTarjetaToAttach.getTarjetaPK());
                attachedTarjetaList.add(tarjetaListTarjetaToAttach);
            }
            jugador.setTarjetaList(attachedTarjetaList);
            em.persist(jugador);
            if (equipoPais != null) {
                equipoPais.getJugadorList().add(jugador);
                equipoPais = em.merge(equipoPais);
            }
            for (Club clubListClub : jugador.getClubList()) {
                clubListClub.getJugadorList().add(jugador);
                clubListClub = em.merge(clubListClub);
            }
            for (Gol golListGol : jugador.getGolList()) {
                Jugador oldJugadorOfGolListGol = golListGol.getJugador();
                golListGol.setJugador(jugador);
                golListGol = em.merge(golListGol);
                if (oldJugadorOfGolListGol != null) {
                    oldJugadorOfGolListGol.getGolList().remove(golListGol);
                    oldJugadorOfGolListGol = em.merge(oldJugadorOfGolListGol);
                }
            }
            for (Posicion posicionListPosicion : jugador.getPosicionList()) {
                Jugador oldJugadorOfPosicionListPosicion = posicionListPosicion.getJugador();
                posicionListPosicion.setJugador(jugador);
                posicionListPosicion = em.merge(posicionListPosicion);
                if (oldJugadorOfPosicionListPosicion != null) {
                    oldJugadorOfPosicionListPosicion.getPosicionList().remove(posicionListPosicion);
                    oldJugadorOfPosicionListPosicion = em.merge(oldJugadorOfPosicionListPosicion);
                }
            }
            for (Tarjeta tarjetaListTarjeta : jugador.getTarjetaList()) {
                Jugador oldJugadorOfTarjetaListTarjeta = tarjetaListTarjeta.getJugador();
                tarjetaListTarjeta.setJugador(jugador);
                tarjetaListTarjeta = em.merge(tarjetaListTarjeta);
                if (oldJugadorOfTarjetaListTarjeta != null) {
                    oldJugadorOfTarjetaListTarjeta.getTarjetaList().remove(tarjetaListTarjeta);
                    oldJugadorOfTarjetaListTarjeta = em.merge(oldJugadorOfTarjetaListTarjeta);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findJugador(jugador.getJugadorPK()) != null) {
                throw new PreexistingEntityException("Jugador " + jugador + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Jugador jugador) throws IllegalOrphanException, NonexistentEntityException, Exception {
        jugador.getJugadorPK().setCodEquipo(jugador.getEquipoPais().getCodEquipo());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jugador persistentJugador = em.find(Jugador.class, jugador.getJugadorPK());
            EquipoPais equipoPaisOld = persistentJugador.getEquipoPais();
            EquipoPais equipoPaisNew = jugador.getEquipoPais();
            List<Club> clubListOld = persistentJugador.getClubList();
            List<Club> clubListNew = jugador.getClubList();
            List<Gol> golListOld = persistentJugador.getGolList();
            List<Gol> golListNew = jugador.getGolList();
            List<Posicion> posicionListOld = persistentJugador.getPosicionList();
            List<Posicion> posicionListNew = jugador.getPosicionList();
            List<Tarjeta> tarjetaListOld = persistentJugador.getTarjetaList();
            List<Tarjeta> tarjetaListNew = jugador.getTarjetaList();
            List<String> illegalOrphanMessages = null;
            for (Gol golListOldGol : golListOld) {
                if (!golListNew.contains(golListOldGol)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Gol " + golListOldGol + " since its jugador field is not nullable.");
                }
            }
            for (Posicion posicionListOldPosicion : posicionListOld) {
                if (!posicionListNew.contains(posicionListOldPosicion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Posicion " + posicionListOldPosicion + " since its jugador field is not nullable.");
                }
            }
            for (Tarjeta tarjetaListOldTarjeta : tarjetaListOld) {
                if (!tarjetaListNew.contains(tarjetaListOldTarjeta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tarjeta " + tarjetaListOldTarjeta + " since its jugador field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (equipoPaisNew != null) {
                equipoPaisNew = em.getReference(equipoPaisNew.getClass(), equipoPaisNew.getCodEquipo());
                jugador.setEquipoPais(equipoPaisNew);
            }
            List<Club> attachedClubListNew = new ArrayList<Club>();
            for (Club clubListNewClubToAttach : clubListNew) {
                clubListNewClubToAttach = em.getReference(clubListNewClubToAttach.getClass(), clubListNewClubToAttach.getCodClub());
                attachedClubListNew.add(clubListNewClubToAttach);
            }
            clubListNew = attachedClubListNew;
            jugador.setClubList(clubListNew);
            List<Gol> attachedGolListNew = new ArrayList<Gol>();
            for (Gol golListNewGolToAttach : golListNew) {
                golListNewGolToAttach = em.getReference(golListNewGolToAttach.getClass(), golListNewGolToAttach.getGolPK());
                attachedGolListNew.add(golListNewGolToAttach);
            }
            golListNew = attachedGolListNew;
            jugador.setGolList(golListNew);
            List<Posicion> attachedPosicionListNew = new ArrayList<Posicion>();
            for (Posicion posicionListNewPosicionToAttach : posicionListNew) {
                posicionListNewPosicionToAttach = em.getReference(posicionListNewPosicionToAttach.getClass(), posicionListNewPosicionToAttach.getPosicionPK());
                attachedPosicionListNew.add(posicionListNewPosicionToAttach);
            }
            posicionListNew = attachedPosicionListNew;
            jugador.setPosicionList(posicionListNew);
            List<Tarjeta> attachedTarjetaListNew = new ArrayList<Tarjeta>();
            for (Tarjeta tarjetaListNewTarjetaToAttach : tarjetaListNew) {
                tarjetaListNewTarjetaToAttach = em.getReference(tarjetaListNewTarjetaToAttach.getClass(), tarjetaListNewTarjetaToAttach.getTarjetaPK());
                attachedTarjetaListNew.add(tarjetaListNewTarjetaToAttach);
            }
            tarjetaListNew = attachedTarjetaListNew;
            jugador.setTarjetaList(tarjetaListNew);
            jugador = em.merge(jugador);
            if (equipoPaisOld != null && !equipoPaisOld.equals(equipoPaisNew)) {
                equipoPaisOld.getJugadorList().remove(jugador);
                equipoPaisOld = em.merge(equipoPaisOld);
            }
            if (equipoPaisNew != null && !equipoPaisNew.equals(equipoPaisOld)) {
                equipoPaisNew.getJugadorList().add(jugador);
                equipoPaisNew = em.merge(equipoPaisNew);
            }
            for (Club clubListOldClub : clubListOld) {
                if (!clubListNew.contains(clubListOldClub)) {
                    clubListOldClub.getJugadorList().remove(jugador);
                    clubListOldClub = em.merge(clubListOldClub);
                }
            }
            for (Club clubListNewClub : clubListNew) {
                if (!clubListOld.contains(clubListNewClub)) {
                    clubListNewClub.getJugadorList().add(jugador);
                    clubListNewClub = em.merge(clubListNewClub);
                }
            }
            for (Gol golListNewGol : golListNew) {
                if (!golListOld.contains(golListNewGol)) {
                    Jugador oldJugadorOfGolListNewGol = golListNewGol.getJugador();
                    golListNewGol.setJugador(jugador);
                    golListNewGol = em.merge(golListNewGol);
                    if (oldJugadorOfGolListNewGol != null && !oldJugadorOfGolListNewGol.equals(jugador)) {
                        oldJugadorOfGolListNewGol.getGolList().remove(golListNewGol);
                        oldJugadorOfGolListNewGol = em.merge(oldJugadorOfGolListNewGol);
                    }
                }
            }
            for (Posicion posicionListNewPosicion : posicionListNew) {
                if (!posicionListOld.contains(posicionListNewPosicion)) {
                    Jugador oldJugadorOfPosicionListNewPosicion = posicionListNewPosicion.getJugador();
                    posicionListNewPosicion.setJugador(jugador);
                    posicionListNewPosicion = em.merge(posicionListNewPosicion);
                    if (oldJugadorOfPosicionListNewPosicion != null && !oldJugadorOfPosicionListNewPosicion.equals(jugador)) {
                        oldJugadorOfPosicionListNewPosicion.getPosicionList().remove(posicionListNewPosicion);
                        oldJugadorOfPosicionListNewPosicion = em.merge(oldJugadorOfPosicionListNewPosicion);
                    }
                }
            }
            for (Tarjeta tarjetaListNewTarjeta : tarjetaListNew) {
                if (!tarjetaListOld.contains(tarjetaListNewTarjeta)) {
                    Jugador oldJugadorOfTarjetaListNewTarjeta = tarjetaListNewTarjeta.getJugador();
                    tarjetaListNewTarjeta.setJugador(jugador);
                    tarjetaListNewTarjeta = em.merge(tarjetaListNewTarjeta);
                    if (oldJugadorOfTarjetaListNewTarjeta != null && !oldJugadorOfTarjetaListNewTarjeta.equals(jugador)) {
                        oldJugadorOfTarjetaListNewTarjeta.getTarjetaList().remove(tarjetaListNewTarjeta);
                        oldJugadorOfTarjetaListNewTarjeta = em.merge(oldJugadorOfTarjetaListNewTarjeta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                JugadorPK id = jugador.getJugadorPK();
                if (findJugador(id) == null) {
                    throw new NonexistentEntityException("The jugador with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(JugadorPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jugador jugador;
            try {
                jugador = em.getReference(Jugador.class, id);
                jugador.getJugadorPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The jugador with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Gol> golListOrphanCheck = jugador.getGolList();
            for (Gol golListOrphanCheckGol : golListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Jugador (" + jugador + ") cannot be destroyed since the Gol " + golListOrphanCheckGol + " in its golList field has a non-nullable jugador field.");
            }
            List<Posicion> posicionListOrphanCheck = jugador.getPosicionList();
            for (Posicion posicionListOrphanCheckPosicion : posicionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Jugador (" + jugador + ") cannot be destroyed since the Posicion " + posicionListOrphanCheckPosicion + " in its posicionList field has a non-nullable jugador field.");
            }
            List<Tarjeta> tarjetaListOrphanCheck = jugador.getTarjetaList();
            for (Tarjeta tarjetaListOrphanCheckTarjeta : tarjetaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Jugador (" + jugador + ") cannot be destroyed since the Tarjeta " + tarjetaListOrphanCheckTarjeta + " in its tarjetaList field has a non-nullable jugador field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            EquipoPais equipoPais = jugador.getEquipoPais();
            if (equipoPais != null) {
                equipoPais.getJugadorList().remove(jugador);
                equipoPais = em.merge(equipoPais);
            }
            List<Club> clubList = jugador.getClubList();
            for (Club clubListClub : clubList) {
                clubListClub.getJugadorList().remove(jugador);
                clubListClub = em.merge(clubListClub);
            }
            em.remove(jugador);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Jugador> findJugadorEntities() {
        return findJugadorEntities(true, -1, -1);
    }

    public List<Jugador> findJugadorEntities(int maxResults, int firstResult) {
        return findJugadorEntities(false, maxResults, firstResult);
    }

    private List<Jugador> findJugadorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Jugador.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Jugador findJugador(JugadorPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Jugador.class, id);
        } finally {
            em.close();
        }
    }

    public int getJugadorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Jugador> rt = cq.from(Jugador.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
