/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.IllegalOrphanException;
import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import entities.Juez;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Pais;
import entities.Rol;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author linam
 */
public class JuezJpaController implements Serializable {

    public JuezJpaController()
    {
        
    }
    public JuezJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("ControladorPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Juez juez) throws PreexistingEntityException, Exception {
        if (juez.getRolList() == null) {
            juez.setRolList(new ArrayList<Rol>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pais codPais = juez.getCodPais();
            if (codPais != null) {
                codPais = em.getReference(codPais.getClass(), codPais.getCodPais());
                juez.setCodPais(codPais);
            }
            List<Rol> attachedRolList = new ArrayList<Rol>();
            for (Rol rolListRolToAttach : juez.getRolList()) {
                rolListRolToAttach = em.getReference(rolListRolToAttach.getClass(), rolListRolToAttach.getRolPK());
                attachedRolList.add(rolListRolToAttach);
            }
            juez.setRolList(attachedRolList);
            em.persist(juez);
            if (codPais != null) {
                codPais.getJuezList().add(juez);
                codPais = em.merge(codPais);
            }
            for (Rol rolListRol : juez.getRolList()) {
                Juez oldJuezOfRolListRol = rolListRol.getJuez();
                rolListRol.setJuez(juez);
                rolListRol = em.merge(rolListRol);
                if (oldJuezOfRolListRol != null) {
                    oldJuezOfRolListRol.getRolList().remove(rolListRol);
                    oldJuezOfRolListRol = em.merge(oldJuezOfRolListRol);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findJuez(juez.getCodJuez()) != null) {
                throw new PreexistingEntityException("Juez " + juez + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Juez juez) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Juez persistentJuez = em.find(Juez.class, juez.getCodJuez());
            Pais codPaisOld = persistentJuez.getCodPais();
            Pais codPaisNew = juez.getCodPais();
            List<Rol> rolListOld = persistentJuez.getRolList();
            List<Rol> rolListNew = juez.getRolList();
            List<String> illegalOrphanMessages = null;
            for (Rol rolListOldRol : rolListOld) {
                if (!rolListNew.contains(rolListOldRol)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Rol " + rolListOldRol + " since its juez field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (codPaisNew != null) {
                codPaisNew = em.getReference(codPaisNew.getClass(), codPaisNew.getCodPais());
                juez.setCodPais(codPaisNew);
            }
            List<Rol> attachedRolListNew = new ArrayList<Rol>();
            for (Rol rolListNewRolToAttach : rolListNew) {
                rolListNewRolToAttach = em.getReference(rolListNewRolToAttach.getClass(), rolListNewRolToAttach.getRolPK());
                attachedRolListNew.add(rolListNewRolToAttach);
            }
            rolListNew = attachedRolListNew;
            juez.setRolList(rolListNew);
            juez = em.merge(juez);
            if (codPaisOld != null && !codPaisOld.equals(codPaisNew)) {
                codPaisOld.getJuezList().remove(juez);
                codPaisOld = em.merge(codPaisOld);
            }
            if (codPaisNew != null && !codPaisNew.equals(codPaisOld)) {
                codPaisNew.getJuezList().add(juez);
                codPaisNew = em.merge(codPaisNew);
            }
            for (Rol rolListNewRol : rolListNew) {
                if (!rolListOld.contains(rolListNewRol)) {
                    Juez oldJuezOfRolListNewRol = rolListNewRol.getJuez();
                    rolListNewRol.setJuez(juez);
                    rolListNewRol = em.merge(rolListNewRol);
                    if (oldJuezOfRolListNewRol != null && !oldJuezOfRolListNewRol.equals(juez)) {
                        oldJuezOfRolListNewRol.getRolList().remove(rolListNewRol);
                        oldJuezOfRolListNewRol = em.merge(oldJuezOfRolListNewRol);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = juez.getCodJuez();
                if (findJuez(id) == null) {
                    throw new NonexistentEntityException("The juez with id " + id + " no longer exists.");
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
            Juez juez;
            try {
                juez = em.getReference(Juez.class, id);
                juez.getCodJuez();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The juez with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Rol> rolListOrphanCheck = juez.getRolList();
            for (Rol rolListOrphanCheckRol : rolListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Juez (" + juez + ") cannot be destroyed since the Rol " + rolListOrphanCheckRol + " in its rolList field has a non-nullable juez field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Pais codPais = juez.getCodPais();
            if (codPais != null) {
                codPais.getJuezList().remove(juez);
                codPais = em.merge(codPais);
            }
            em.remove(juez);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Juez> findJuezEntities() {
        return findJuezEntities(true, -1, -1);
    }

    public List<Juez> findJuezEntities(int maxResults, int firstResult) {
        return findJuezEntities(false, maxResults, firstResult);
    }

    private List<Juez> findJuezEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Juez.class));
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

    public Juez findJuez(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Juez.class, id);
        } finally {
            em.close();
        }
    }

    public int getJuezCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Juez> rt = cq.from(Juez.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
