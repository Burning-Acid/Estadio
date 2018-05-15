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
import entities.Tarjeta;
import entities.TarjetaPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author linam
 */
public class TarjetaJpaController implements Serializable {

    public TarjetaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tarjeta tarjeta) throws PreexistingEntityException, Exception {
        if (tarjeta.getTarjetaPK() == null) {
            tarjeta.setTarjetaPK(new TarjetaPK());
        }
        tarjeta.getTarjetaPK().setNumPartido(tarjeta.getPartido().getNumPartido());
        tarjeta.getTarjetaPK().setNumJugador(tarjeta.getJugador().getJugadorPK().getNumJugador());
        tarjeta.getTarjetaPK().setCodEquipo(tarjeta.getJugador().getJugadorPK().getCodEquipo());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jugador jugador = tarjeta.getJugador();
            if (jugador != null) {
                jugador = em.getReference(jugador.getClass(), jugador.getJugadorPK());
                tarjeta.setJugador(jugador);
            }
            Partido partido = tarjeta.getPartido();
            if (partido != null) {
                partido = em.getReference(partido.getClass(), partido.getNumPartido());
                tarjeta.setPartido(partido);
            }
            em.persist(tarjeta);
            if (jugador != null) {
                jugador.getTarjetaList().add(tarjeta);
                jugador = em.merge(jugador);
            }
            if (partido != null) {
                partido.getTarjetaList().add(tarjeta);
                partido = em.merge(partido);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTarjeta(tarjeta.getTarjetaPK()) != null) {
                throw new PreexistingEntityException("Tarjeta " + tarjeta + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tarjeta tarjeta) throws NonexistentEntityException, Exception {
        tarjeta.getTarjetaPK().setNumPartido(tarjeta.getPartido().getNumPartido());
        tarjeta.getTarjetaPK().setNumJugador(tarjeta.getJugador().getJugadorPK().getNumJugador());
        tarjeta.getTarjetaPK().setCodEquipo(tarjeta.getJugador().getJugadorPK().getCodEquipo());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarjeta persistentTarjeta = em.find(Tarjeta.class, tarjeta.getTarjetaPK());
            Jugador jugadorOld = persistentTarjeta.getJugador();
            Jugador jugadorNew = tarjeta.getJugador();
            Partido partidoOld = persistentTarjeta.getPartido();
            Partido partidoNew = tarjeta.getPartido();
            if (jugadorNew != null) {
                jugadorNew = em.getReference(jugadorNew.getClass(), jugadorNew.getJugadorPK());
                tarjeta.setJugador(jugadorNew);
            }
            if (partidoNew != null) {
                partidoNew = em.getReference(partidoNew.getClass(), partidoNew.getNumPartido());
                tarjeta.setPartido(partidoNew);
            }
            tarjeta = em.merge(tarjeta);
            if (jugadorOld != null && !jugadorOld.equals(jugadorNew)) {
                jugadorOld.getTarjetaList().remove(tarjeta);
                jugadorOld = em.merge(jugadorOld);
            }
            if (jugadorNew != null && !jugadorNew.equals(jugadorOld)) {
                jugadorNew.getTarjetaList().add(tarjeta);
                jugadorNew = em.merge(jugadorNew);
            }
            if (partidoOld != null && !partidoOld.equals(partidoNew)) {
                partidoOld.getTarjetaList().remove(tarjeta);
                partidoOld = em.merge(partidoOld);
            }
            if (partidoNew != null && !partidoNew.equals(partidoOld)) {
                partidoNew.getTarjetaList().add(tarjeta);
                partidoNew = em.merge(partidoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                TarjetaPK id = tarjeta.getTarjetaPK();
                if (findTarjeta(id) == null) {
                    throw new NonexistentEntityException("The tarjeta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(TarjetaPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarjeta tarjeta;
            try {
                tarjeta = em.getReference(Tarjeta.class, id);
                tarjeta.getTarjetaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tarjeta with id " + id + " no longer exists.", enfe);
            }
            Jugador jugador = tarjeta.getJugador();
            if (jugador != null) {
                jugador.getTarjetaList().remove(tarjeta);
                jugador = em.merge(jugador);
            }
            Partido partido = tarjeta.getPartido();
            if (partido != null) {
                partido.getTarjetaList().remove(tarjeta);
                partido = em.merge(partido);
            }
            em.remove(tarjeta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tarjeta> findTarjetaEntities() {
        return findTarjetaEntities(true, -1, -1);
    }

    public List<Tarjeta> findTarjetaEntities(int maxResults, int firstResult) {
        return findTarjetaEntities(false, maxResults, firstResult);
    }

    private List<Tarjeta> findTarjetaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tarjeta.class));
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

    public Tarjeta findTarjeta(TarjetaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tarjeta.class, id);
        } finally {
            em.close();
        }
    }

    public int getTarjetaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tarjeta> rt = cq.from(Tarjeta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
