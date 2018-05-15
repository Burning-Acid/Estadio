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
import entities.EquipoPais;
import entities.Grupo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author linam
 */
public class GrupoJpaController implements Serializable {

    public GrupoJpaController()
    {
        
    }
    public GrupoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("ControladorPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Grupo grupo) throws PreexistingEntityException, Exception {
        if (grupo.getEquipoPaisList() == null) {
            grupo.setEquipoPaisList(new ArrayList<EquipoPais>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<EquipoPais> attachedEquipoPaisList = new ArrayList<EquipoPais>();
            for (EquipoPais equipoPaisListEquipoPaisToAttach : grupo.getEquipoPaisList()) {
                equipoPaisListEquipoPaisToAttach = em.getReference(equipoPaisListEquipoPaisToAttach.getClass(), equipoPaisListEquipoPaisToAttach.getCodEquipo());
                attachedEquipoPaisList.add(equipoPaisListEquipoPaisToAttach);
            }
            grupo.setEquipoPaisList(attachedEquipoPaisList);
            em.persist(grupo);
            for (EquipoPais equipoPaisListEquipoPais : grupo.getEquipoPaisList()) {
                Grupo oldCodGrupoOfEquipoPaisListEquipoPais = equipoPaisListEquipoPais.getCodGrupo();
                equipoPaisListEquipoPais.setCodGrupo(grupo);
                equipoPaisListEquipoPais = em.merge(equipoPaisListEquipoPais);
                if (oldCodGrupoOfEquipoPaisListEquipoPais != null) {
                    oldCodGrupoOfEquipoPaisListEquipoPais.getEquipoPaisList().remove(equipoPaisListEquipoPais);
                    oldCodGrupoOfEquipoPaisListEquipoPais = em.merge(oldCodGrupoOfEquipoPaisListEquipoPais);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findGrupo(grupo.getCodGrupo()) != null) {
                throw new PreexistingEntityException("Grupo " + grupo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Grupo grupo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grupo persistentGrupo = em.find(Grupo.class, grupo.getCodGrupo());
            List<EquipoPais> equipoPaisListOld = persistentGrupo.getEquipoPaisList();
            List<EquipoPais> equipoPaisListNew = grupo.getEquipoPaisList();
            List<EquipoPais> attachedEquipoPaisListNew = new ArrayList<EquipoPais>();
            for (EquipoPais equipoPaisListNewEquipoPaisToAttach : equipoPaisListNew) {
                equipoPaisListNewEquipoPaisToAttach = em.getReference(equipoPaisListNewEquipoPaisToAttach.getClass(), equipoPaisListNewEquipoPaisToAttach.getCodEquipo());
                attachedEquipoPaisListNew.add(equipoPaisListNewEquipoPaisToAttach);
            }
            equipoPaisListNew = attachedEquipoPaisListNew;
            grupo.setEquipoPaisList(equipoPaisListNew);
            grupo = em.merge(grupo);
            for (EquipoPais equipoPaisListOldEquipoPais : equipoPaisListOld) {
                if (!equipoPaisListNew.contains(equipoPaisListOldEquipoPais)) {
                    equipoPaisListOldEquipoPais.setCodGrupo(null);
                    equipoPaisListOldEquipoPais = em.merge(equipoPaisListOldEquipoPais);
                }
            }
            for (EquipoPais equipoPaisListNewEquipoPais : equipoPaisListNew) {
                if (!equipoPaisListOld.contains(equipoPaisListNewEquipoPais)) {
                    Grupo oldCodGrupoOfEquipoPaisListNewEquipoPais = equipoPaisListNewEquipoPais.getCodGrupo();
                    equipoPaisListNewEquipoPais.setCodGrupo(grupo);
                    equipoPaisListNewEquipoPais = em.merge(equipoPaisListNewEquipoPais);
                    if (oldCodGrupoOfEquipoPaisListNewEquipoPais != null && !oldCodGrupoOfEquipoPaisListNewEquipoPais.equals(grupo)) {
                        oldCodGrupoOfEquipoPaisListNewEquipoPais.getEquipoPaisList().remove(equipoPaisListNewEquipoPais);
                        oldCodGrupoOfEquipoPaisListNewEquipoPais = em.merge(oldCodGrupoOfEquipoPaisListNewEquipoPais);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = grupo.getCodGrupo();
                if (findGrupo(id) == null) {
                    throw new NonexistentEntityException("The grupo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Short id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grupo grupo;
            try {
                grupo = em.getReference(Grupo.class, id);
                grupo.getCodGrupo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The grupo with id " + id + " no longer exists.", enfe);
            }
            List<EquipoPais> equipoPaisList = grupo.getEquipoPaisList();
            for (EquipoPais equipoPaisListEquipoPais : equipoPaisList) {
                equipoPaisListEquipoPais.setCodGrupo(null);
                equipoPaisListEquipoPais = em.merge(equipoPaisListEquipoPais);
            }
            em.remove(grupo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Grupo> findGrupoEntities() {
        return findGrupoEntities(true, -1, -1);
    }

    public List<Grupo> findGrupoEntities(int maxResults, int firstResult) {
        return findGrupoEntities(false, maxResults, firstResult);
    }

    private List<Grupo> findGrupoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Grupo.class));
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

    public Grupo findGrupo(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Grupo.class, id);
        } finally {
            em.close();
        }
    }

    public int getGrupoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Grupo> rt = cq.from(Grupo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
