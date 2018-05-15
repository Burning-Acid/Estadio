/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import entities.Club;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Jugador;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author linam
 */
public class ClubJpaController implements Serializable {

    public ClubJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Club club) throws PreexistingEntityException, Exception {
        if (club.getJugadorList() == null) {
            club.setJugadorList(new ArrayList<Jugador>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Jugador> attachedJugadorList = new ArrayList<Jugador>();
            for (Jugador jugadorListJugadorToAttach : club.getJugadorList()) {
                jugadorListJugadorToAttach = em.getReference(jugadorListJugadorToAttach.getClass(), jugadorListJugadorToAttach.getJugadorPK());
                attachedJugadorList.add(jugadorListJugadorToAttach);
            }
            club.setJugadorList(attachedJugadorList);
            em.persist(club);
            for (Jugador jugadorListJugador : club.getJugadorList()) {
                jugadorListJugador.getClubList().add(club);
                jugadorListJugador = em.merge(jugadorListJugador);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findClub(club.getCodClub()) != null) {
                throw new PreexistingEntityException("Club " + club + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Club club) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Club persistentClub = em.find(Club.class, club.getCodClub());
            List<Jugador> jugadorListOld = persistentClub.getJugadorList();
            List<Jugador> jugadorListNew = club.getJugadorList();
            List<Jugador> attachedJugadorListNew = new ArrayList<Jugador>();
            for (Jugador jugadorListNewJugadorToAttach : jugadorListNew) {
                jugadorListNewJugadorToAttach = em.getReference(jugadorListNewJugadorToAttach.getClass(), jugadorListNewJugadorToAttach.getJugadorPK());
                attachedJugadorListNew.add(jugadorListNewJugadorToAttach);
            }
            jugadorListNew = attachedJugadorListNew;
            club.setJugadorList(jugadorListNew);
            club = em.merge(club);
            for (Jugador jugadorListOldJugador : jugadorListOld) {
                if (!jugadorListNew.contains(jugadorListOldJugador)) {
                    jugadorListOldJugador.getClubList().remove(club);
                    jugadorListOldJugador = em.merge(jugadorListOldJugador);
                }
            }
            for (Jugador jugadorListNewJugador : jugadorListNew) {
                if (!jugadorListOld.contains(jugadorListNewJugador)) {
                    jugadorListNewJugador.getClubList().add(club);
                    jugadorListNewJugador = em.merge(jugadorListNewJugador);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = club.getCodClub();
                if (findClub(id) == null) {
                    throw new NonexistentEntityException("The club with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Club club;
            try {
                club = em.getReference(Club.class, id);
                club.getCodClub();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The club with id " + id + " no longer exists.", enfe);
            }
            List<Jugador> jugadorList = club.getJugadorList();
            for (Jugador jugadorListJugador : jugadorList) {
                jugadorListJugador.getClubList().remove(club);
                jugadorListJugador = em.merge(jugadorListJugador);
            }
            em.remove(club);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Club> findClubEntities() {
        return findClubEntities(true, -1, -1);
    }

    public List<Club> findClubEntities(int maxResults, int firstResult) {
        return findClubEntities(false, maxResults, firstResult);
    }

    private List<Club> findClubEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Club.class));
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

    public Club findClub(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Club.class, id);
        } finally {
            em.close();
        }
    }

    public int getClubCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Club> rt = cq.from(Club.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
