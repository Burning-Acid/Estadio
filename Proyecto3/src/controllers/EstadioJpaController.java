/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.IllegalOrphanException;
import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import entities.Estadio;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Partido;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author linam
 */
public class EstadioJpaController implements Serializable {

    public EstadioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Estadio estadio) throws PreexistingEntityException, Exception {
        if (estadio.getPartidoList() == null) {
            estadio.setPartidoList(new ArrayList<Partido>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Partido> attachedPartidoList = new ArrayList<Partido>();
            for (Partido partidoListPartidoToAttach : estadio.getPartidoList()) {
                partidoListPartidoToAttach = em.getReference(partidoListPartidoToAttach.getClass(), partidoListPartidoToAttach.getNumPartido());
                attachedPartidoList.add(partidoListPartidoToAttach);
            }
            estadio.setPartidoList(attachedPartidoList);
            em.persist(estadio);
            for (Partido partidoListPartido : estadio.getPartidoList()) {
                Estadio oldCodEstadioOfPartidoListPartido = partidoListPartido.getCodEstadio();
                partidoListPartido.setCodEstadio(estadio);
                partidoListPartido = em.merge(partidoListPartido);
                if (oldCodEstadioOfPartidoListPartido != null) {
                    oldCodEstadioOfPartidoListPartido.getPartidoList().remove(partidoListPartido);
                    oldCodEstadioOfPartidoListPartido = em.merge(oldCodEstadioOfPartidoListPartido);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEstadio(estadio.getCodEstadio()) != null) {
                throw new PreexistingEntityException("Estadio " + estadio + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Estadio estadio) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estadio persistentEstadio = em.find(Estadio.class, estadio.getCodEstadio());
            List<Partido> partidoListOld = persistentEstadio.getPartidoList();
            List<Partido> partidoListNew = estadio.getPartidoList();
            List<String> illegalOrphanMessages = null;
            for (Partido partidoListOldPartido : partidoListOld) {
                if (!partidoListNew.contains(partidoListOldPartido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Partido " + partidoListOldPartido + " since its codEstadio field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Partido> attachedPartidoListNew = new ArrayList<Partido>();
            for (Partido partidoListNewPartidoToAttach : partidoListNew) {
                partidoListNewPartidoToAttach = em.getReference(partidoListNewPartidoToAttach.getClass(), partidoListNewPartidoToAttach.getNumPartido());
                attachedPartidoListNew.add(partidoListNewPartidoToAttach);
            }
            partidoListNew = attachedPartidoListNew;
            estadio.setPartidoList(partidoListNew);
            estadio = em.merge(estadio);
            for (Partido partidoListNewPartido : partidoListNew) {
                if (!partidoListOld.contains(partidoListNewPartido)) {
                    Estadio oldCodEstadioOfPartidoListNewPartido = partidoListNewPartido.getCodEstadio();
                    partidoListNewPartido.setCodEstadio(estadio);
                    partidoListNewPartido = em.merge(partidoListNewPartido);
                    if (oldCodEstadioOfPartidoListNewPartido != null && !oldCodEstadioOfPartidoListNewPartido.equals(estadio)) {
                        oldCodEstadioOfPartidoListNewPartido.getPartidoList().remove(partidoListNewPartido);
                        oldCodEstadioOfPartidoListNewPartido = em.merge(oldCodEstadioOfPartidoListNewPartido);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = estadio.getCodEstadio();
                if (findEstadio(id) == null) {
                    throw new NonexistentEntityException("The estadio with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estadio estadio;
            try {
                estadio = em.getReference(Estadio.class, id);
                estadio.getCodEstadio();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estadio with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Partido> partidoListOrphanCheck = estadio.getPartidoList();
            for (Partido partidoListOrphanCheckPartido : partidoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Estadio (" + estadio + ") cannot be destroyed since the Partido " + partidoListOrphanCheckPartido + " in its partidoList field has a non-nullable codEstadio field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(estadio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Estadio> findEstadioEntities() {
        return findEstadioEntities(true, -1, -1);
    }

    public List<Estadio> findEstadioEntities(int maxResults, int firstResult) {
        return findEstadioEntities(false, maxResults, firstResult);
    }

    private List<Estadio> findEstadioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Estadio.class));
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

    public Estadio findEstadio(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Estadio.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Estadio> rt = cq.from(Estadio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
