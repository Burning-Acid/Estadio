/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Jugador;
import entities.Partido;
import entities.Posicion;
import entities.PosicionPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author linam
 */
public class PosicionJpaController implements Serializable {

    public PosicionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Posicion posicion) throws PreexistingEntityException, Exception {
        if (posicion.getPosicionPK() == null) {
            posicion.setPosicionPK(new PosicionPK());
        }
        posicion.getPosicionPK().setCodEquipo(posicion.getJugador().getJugadorPK().getCodEquipo());
        posicion.getPosicionPK().setNumPartido(posicion.getPartido().getNumPartido());
        posicion.getPosicionPK().setNumJugador(posicion.getJugador().getJugadorPK().getNumJugador());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jugador jugador = posicion.getJugador();
            if (jugador != null) {
                jugador = em.getReference(jugador.getClass(), jugador.getJugadorPK());
                posicion.setJugador(jugador);
            }
            Partido partido = posicion.getPartido();
            if (partido != null) {
                partido = em.getReference(partido.getClass(), partido.getNumPartido());
                posicion.setPartido(partido);
            }
            em.persist(posicion);
            if (jugador != null) {
                jugador.getPosicionList().add(posicion);
                jugador = em.merge(jugador);
            }
            if (partido != null) {
                partido.getPosicionList().add(posicion);
                partido = em.merge(partido);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPosicion(posicion.getPosicionPK()) != null) {
                throw new PreexistingEntityException("Posicion " + posicion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Posicion posicion) throws NonexistentEntityException, Exception {
        posicion.getPosicionPK().setCodEquipo(posicion.getJugador().getJugadorPK().getCodEquipo());
        posicion.getPosicionPK().setNumPartido(posicion.getPartido().getNumPartido());
        posicion.getPosicionPK().setNumJugador(posicion.getJugador().getJugadorPK().getNumJugador());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Posicion persistentPosicion = em.find(Posicion.class, posicion.getPosicionPK());
            Jugador jugadorOld = persistentPosicion.getJugador();
            Jugador jugadorNew = posicion.getJugador();
            Partido partidoOld = persistentPosicion.getPartido();
            Partido partidoNew = posicion.getPartido();
            if (jugadorNew != null) {
                jugadorNew = em.getReference(jugadorNew.getClass(), jugadorNew.getJugadorPK());
                posicion.setJugador(jugadorNew);
            }
            if (partidoNew != null) {
                partidoNew = em.getReference(partidoNew.getClass(), partidoNew.getNumPartido());
                posicion.setPartido(partidoNew);
            }
            posicion = em.merge(posicion);
            if (jugadorOld != null && !jugadorOld.equals(jugadorNew)) {
                jugadorOld.getPosicionList().remove(posicion);
                jugadorOld = em.merge(jugadorOld);
            }
            if (jugadorNew != null && !jugadorNew.equals(jugadorOld)) {
                jugadorNew.getPosicionList().add(posicion);
                jugadorNew = em.merge(jugadorNew);
            }
            if (partidoOld != null && !partidoOld.equals(partidoNew)) {
                partidoOld.getPosicionList().remove(posicion);
                partidoOld = em.merge(partidoOld);
            }
            if (partidoNew != null && !partidoNew.equals(partidoOld)) {
                partidoNew.getPosicionList().add(posicion);
                partidoNew = em.merge(partidoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                PosicionPK id = posicion.getPosicionPK();
                if (findPosicion(id) == null) {
                    throw new NonexistentEntityException("The posicion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(PosicionPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Posicion posicion;
            try {
                posicion = em.getReference(Posicion.class, id);
                posicion.getPosicionPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The posicion with id " + id + " no longer exists.", enfe);
            }
            Jugador jugador = posicion.getJugador();
            if (jugador != null) {
                jugador.getPosicionList().remove(posicion);
                jugador = em.merge(jugador);
            }
            Partido partido = posicion.getPartido();
            if (partido != null) {
                partido.getPosicionList().remove(posicion);
                partido = em.merge(partido);
            }
            em.remove(posicion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Posicion> findPosicionEntities() {
        return findPosicionEntities(true, -1, -1);
    }

    public List<Posicion> findPosicionEntities(int maxResults, int firstResult) {
        return findPosicionEntities(false, maxResults, firstResult);
    }

    private List<Posicion> findPosicionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Posicion.class));
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

    public Posicion findPosicion(PosicionPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Posicion.class, id);
        } finally {
            em.close();
        }
    }

    public int getPosicionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Posicion> rt = cq.from(Posicion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
