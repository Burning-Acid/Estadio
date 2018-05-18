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
import entities.Partido;
import entities.Silla;
import entities.SillaPK;
import entities.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author linam
 */
public class SillaJpaController implements Serializable {

    public SillaJpaController()
    {
        
    }
    public SillaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("ControladorPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Silla silla) throws PreexistingEntityException, Exception {
        if (silla.getSillaPK() == null) {
            silla.setSillaPK(new SillaPK());
        }
        silla.getSillaPK().setNumPartido(silla.getPartido().getNumPartido());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Partido partido = silla.getPartido();
            if (partido != null) {
                partido = em.getReference(partido.getClass(), partido.getNumPartido());
                silla.setPartido(partido);
            }
            Usuario identificacion = silla.getIdentificacion();
            if (identificacion != null) {
                identificacion = em.getReference(identificacion.getClass(), identificacion.getIdentificacion());
                silla.setIdentificacion(identificacion);
            }
            em.persist(silla);
            if (partido != null) {
                partido.getSillaList().add(silla);
                partido = em.merge(partido);
            }
            if (identificacion != null) {
                identificacion.getSillaList().add(silla);
                identificacion = em.merge(identificacion);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSilla(silla.getSillaPK()) != null) {
                throw new PreexistingEntityException("Silla " + silla + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Silla silla) throws NonexistentEntityException, Exception {
        silla.getSillaPK().setNumPartido(silla.getPartido().getNumPartido());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Silla persistentSilla = em.find(Silla.class, silla.getSillaPK());
            Partido partidoOld = persistentSilla.getPartido();
            Partido partidoNew = silla.getPartido();
            Usuario identificacionOld = persistentSilla.getIdentificacion();
            Usuario identificacionNew = silla.getIdentificacion();
            if (partidoNew != null) {
                partidoNew = em.getReference(partidoNew.getClass(), partidoNew.getNumPartido());
                silla.setPartido(partidoNew);
            }
            if (identificacionNew != null) {
                identificacionNew = em.getReference(identificacionNew.getClass(), identificacionNew.getIdentificacion());
                silla.setIdentificacion(identificacionNew);
            }
            silla = em.merge(silla);
            if (partidoOld != null && !partidoOld.equals(partidoNew)) {
                partidoOld.getSillaList().remove(silla);
                partidoOld = em.merge(partidoOld);
            }
            if (partidoNew != null && !partidoNew.equals(partidoOld)) {
                partidoNew.getSillaList().add(silla);
                partidoNew = em.merge(partidoNew);
            }
            if (identificacionOld != null && !identificacionOld.equals(identificacionNew)) {
                identificacionOld.getSillaList().remove(silla);
                identificacionOld = em.merge(identificacionOld);
            }
            if (identificacionNew != null && !identificacionNew.equals(identificacionOld)) {
                identificacionNew.getSillaList().add(silla);
                identificacionNew = em.merge(identificacionNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                SillaPK id = silla.getSillaPK();
                if (findSilla(id) == null) {
                    throw new NonexistentEntityException("The silla with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(SillaPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Silla silla;
            try {
                silla = em.getReference(Silla.class, id);
                silla.getSillaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The silla with id " + id + " no longer exists.", enfe);
            }
            Partido partido = silla.getPartido();
            if (partido != null) {
                partido.getSillaList().remove(silla);
                partido = em.merge(partido);
            }
            Usuario identificacion = silla.getIdentificacion();
            if (identificacion != null) {
                identificacion.getSillaList().remove(silla);
                identificacion = em.merge(identificacion);
            }
            em.remove(silla);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Silla> findSillaEntities() {
        return findSillaEntities(true, -1, -1);
    }

    public List<Silla> findSillaEntities(int maxResults, int firstResult) {
        return findSillaEntities(false, maxResults, firstResult);
    }

    private List<Silla> findSillaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Silla.class));
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

    public Silla findSilla(SillaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Silla.class, id);
        } finally {
            em.close();
        }
    }

    public int getSillaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Silla> rt = cq.from(Silla.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
