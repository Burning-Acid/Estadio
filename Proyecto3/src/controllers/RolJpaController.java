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
import entities.Juez;
import entities.Partido;
import entities.Rol;
import entities.RolPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author linam
 */
public class RolJpaController implements Serializable {

    public RolJpaController()
    {
        
    }
    public RolJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("ControladorPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Rol rol) throws PreexistingEntityException, Exception {
        if (rol.getRolPK() == null) {
            rol.setRolPK(new RolPK());
        }
        rol.getRolPK().setNumPartido(rol.getPartido().getNumPartido());
        rol.getRolPK().setCodJuez(rol.getJuez().getCodJuez());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Juez juez = rol.getJuez();
            if (juez != null) {
                juez = em.getReference(juez.getClass(), juez.getCodJuez());
                rol.setJuez(juez);
            }
            Partido partido = rol.getPartido();
            if (partido != null) {
                partido = em.getReference(partido.getClass(), partido.getNumPartido());
                rol.setPartido(partido);
            }
            em.persist(rol);
            if (juez != null) {
                juez.getRolList().add(rol);
                juez = em.merge(juez);
            }
            if (partido != null) {
                partido.getRolList().add(rol);
                partido = em.merge(partido);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRol(rol.getRolPK()) != null) {
                throw new PreexistingEntityException("Rol " + rol + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Rol rol) throws NonexistentEntityException, Exception {
        rol.getRolPK().setNumPartido(rol.getPartido().getNumPartido());
        rol.getRolPK().setCodJuez(rol.getJuez().getCodJuez());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rol persistentRol = em.find(Rol.class, rol.getRolPK());
            Juez juezOld = persistentRol.getJuez();
            Juez juezNew = rol.getJuez();
            Partido partidoOld = persistentRol.getPartido();
            Partido partidoNew = rol.getPartido();
            if (juezNew != null) {
                juezNew = em.getReference(juezNew.getClass(), juezNew.getCodJuez());
                rol.setJuez(juezNew);
            }
            if (partidoNew != null) {
                partidoNew = em.getReference(partidoNew.getClass(), partidoNew.getNumPartido());
                rol.setPartido(partidoNew);
            }
            rol = em.merge(rol);
            if (juezOld != null && !juezOld.equals(juezNew)) {
                juezOld.getRolList().remove(rol);
                juezOld = em.merge(juezOld);
            }
            if (juezNew != null && !juezNew.equals(juezOld)) {
                juezNew.getRolList().add(rol);
                juezNew = em.merge(juezNew);
            }
            if (partidoOld != null && !partidoOld.equals(partidoNew)) {
                partidoOld.getRolList().remove(rol);
                partidoOld = em.merge(partidoOld);
            }
            if (partidoNew != null && !partidoNew.equals(partidoOld)) {
                partidoNew.getRolList().add(rol);
                partidoNew = em.merge(partidoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                RolPK id = rol.getRolPK();
                if (findRol(id) == null) {
                    throw new NonexistentEntityException("The rol with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(RolPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rol rol;
            try {
                rol = em.getReference(Rol.class, id);
                rol.getRolPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rol with id " + id + " no longer exists.", enfe);
            }
            Juez juez = rol.getJuez();
            if (juez != null) {
                juez.getRolList().remove(rol);
                juez = em.merge(juez);
            }
            Partido partido = rol.getPartido();
            if (partido != null) {
                partido.getRolList().remove(rol);
                partido = em.merge(partido);
            }
            em.remove(rol);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Rol> findRolEntities() {
        return findRolEntities(true, -1, -1);
    }

    public List<Rol> findRolEntities(int maxResults, int firstResult) {
        return findRolEntities(false, maxResults, firstResult);
    }

    private List<Rol> findRolEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rol.class));
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

    public Rol findRol(RolPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rol.class, id);
        } finally {
            em.close();
        }
    }

    public int getRolCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rol> rt = cq.from(Rol.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
