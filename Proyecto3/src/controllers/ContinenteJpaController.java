/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.IllegalOrphanException;
import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import entities.Continente;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Pais;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author linam
 */
public class ContinenteJpaController implements Serializable {

    public ContinenteJpaController()
    {
        
    }
    public ContinenteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("ControladorPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Continente continente) throws PreexistingEntityException, Exception {
        if (continente.getPaisList() == null) {
            continente.setPaisList(new ArrayList<Pais>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Pais> attachedPaisList = new ArrayList<Pais>();
            for (Pais paisListPaisToAttach : continente.getPaisList()) {
                paisListPaisToAttach = em.getReference(paisListPaisToAttach.getClass(), paisListPaisToAttach.getCodPais());
                attachedPaisList.add(paisListPaisToAttach);
            }
            continente.setPaisList(attachedPaisList);
            em.persist(continente);
            for (Pais paisListPais : continente.getPaisList()) {
                Continente oldCodContinenteOfPaisListPais = paisListPais.getCodContinente();
                paisListPais.setCodContinente(continente);
                paisListPais = em.merge(paisListPais);
                if (oldCodContinenteOfPaisListPais != null) {
                    oldCodContinenteOfPaisListPais.getPaisList().remove(paisListPais);
                    oldCodContinenteOfPaisListPais = em.merge(oldCodContinenteOfPaisListPais);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findContinente(continente.getCodContinente()) != null) {
                throw new PreexistingEntityException("Continente " + continente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Continente continente) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Continente persistentContinente = em.find(Continente.class, continente.getCodContinente());
            List<Pais> paisListOld = persistentContinente.getPaisList();
            List<Pais> paisListNew = continente.getPaisList();
            List<String> illegalOrphanMessages = null;
            for (Pais paisListOldPais : paisListOld) {
                if (!paisListNew.contains(paisListOldPais)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pais " + paisListOldPais + " since its codContinente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Pais> attachedPaisListNew = new ArrayList<Pais>();
            for (Pais paisListNewPaisToAttach : paisListNew) {
                paisListNewPaisToAttach = em.getReference(paisListNewPaisToAttach.getClass(), paisListNewPaisToAttach.getCodPais());
                attachedPaisListNew.add(paisListNewPaisToAttach);
            }
            paisListNew = attachedPaisListNew;
            continente.setPaisList(paisListNew);
            continente = em.merge(continente);
            for (Pais paisListNewPais : paisListNew) {
                if (!paisListOld.contains(paisListNewPais)) {
                    Continente oldCodContinenteOfPaisListNewPais = paisListNewPais.getCodContinente();
                    paisListNewPais.setCodContinente(continente);
                    paisListNewPais = em.merge(paisListNewPais);
                    if (oldCodContinenteOfPaisListNewPais != null && !oldCodContinenteOfPaisListNewPais.equals(continente)) {
                        oldCodContinenteOfPaisListNewPais.getPaisList().remove(paisListNewPais);
                        oldCodContinenteOfPaisListNewPais = em.merge(oldCodContinenteOfPaisListNewPais);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = continente.getCodContinente();
                if (findContinente(id) == null) {
                    throw new NonexistentEntityException("The continente with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Short id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Continente continente;
            try {
                continente = em.getReference(Continente.class, id);
                continente.getCodContinente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The continente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Pais> paisListOrphanCheck = continente.getPaisList();
            for (Pais paisListOrphanCheckPais : paisListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Continente (" + continente + ") cannot be destroyed since the Pais " + paisListOrphanCheckPais + " in its paisList field has a non-nullable codContinente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(continente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Continente> findContinenteEntities() {
        return findContinenteEntities(true, -1, -1);
    }

    public List<Continente> findContinenteEntities(int maxResults, int firstResult) {
        return findContinenteEntities(false, maxResults, firstResult);
    }

    private List<Continente> findContinenteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Continente.class));
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

    public Continente findContinente(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Continente.class, id);
        } finally {
            em.close();
        }
    }

    public int getContinenteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Continente> rt = cq.from(Continente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
