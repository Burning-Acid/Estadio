/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import entities.Entrenador;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Pais;
import entities.EquipoPais;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author linam
 */
public class EntrenadorJpaController implements Serializable {

    public EntrenadorJpaController()
    {
        
    }
    public EntrenadorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("ControladorPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Entrenador entrenador) throws PreexistingEntityException, Exception {
        if (entrenador.getEquipoPaisList() == null) {
            entrenador.setEquipoPaisList(new ArrayList<EquipoPais>());
        }
        if (entrenador.getEquipoPaisList1() == null) {
            entrenador.setEquipoPaisList1(new ArrayList<EquipoPais>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pais codPais = entrenador.getCodPais();
            if (codPais != null) {
                codPais = em.getReference(codPais.getClass(), codPais.getCodPais());
                entrenador.setCodPais(codPais);
            }
            List<EquipoPais> attachedEquipoPaisList = new ArrayList<EquipoPais>();
            for (EquipoPais equipoPaisListEquipoPaisToAttach : entrenador.getEquipoPaisList()) {
                equipoPaisListEquipoPaisToAttach = em.getReference(equipoPaisListEquipoPaisToAttach.getClass(), equipoPaisListEquipoPaisToAttach.getCodEquipo());
                attachedEquipoPaisList.add(equipoPaisListEquipoPaisToAttach);
            }
            entrenador.setEquipoPaisList(attachedEquipoPaisList);
            List<EquipoPais> attachedEquipoPaisList1 = new ArrayList<EquipoPais>();
            for (EquipoPais equipoPaisList1EquipoPaisToAttach : entrenador.getEquipoPaisList1()) {
                equipoPaisList1EquipoPaisToAttach = em.getReference(equipoPaisList1EquipoPaisToAttach.getClass(), equipoPaisList1EquipoPaisToAttach.getCodEquipo());
                attachedEquipoPaisList1.add(equipoPaisList1EquipoPaisToAttach);
            }
            entrenador.setEquipoPaisList1(attachedEquipoPaisList1);
            em.persist(entrenador);
            if (codPais != null) {
                codPais.getEntrenadorList().add(entrenador);
                codPais = em.merge(codPais);
            }
            for (EquipoPais equipoPaisListEquipoPais : entrenador.getEquipoPaisList()) {
                Entrenador oldCodEntrenadorAuxiliarOfEquipoPaisListEquipoPais = equipoPaisListEquipoPais.getCodEntrenadorAuxiliar();
                equipoPaisListEquipoPais.setCodEntrenadorAuxiliar(entrenador);
                equipoPaisListEquipoPais = em.merge(equipoPaisListEquipoPais);
                if (oldCodEntrenadorAuxiliarOfEquipoPaisListEquipoPais != null) {
                    oldCodEntrenadorAuxiliarOfEquipoPaisListEquipoPais.getEquipoPaisList().remove(equipoPaisListEquipoPais);
                    oldCodEntrenadorAuxiliarOfEquipoPaisListEquipoPais = em.merge(oldCodEntrenadorAuxiliarOfEquipoPaisListEquipoPais);
                }
            }
            for (EquipoPais equipoPaisList1EquipoPais : entrenador.getEquipoPaisList1()) {
                Entrenador oldCodEntrenadorPrincipalOfEquipoPaisList1EquipoPais = equipoPaisList1EquipoPais.getCodEntrenadorPrincipal();
                equipoPaisList1EquipoPais.setCodEntrenadorPrincipal(entrenador);
                equipoPaisList1EquipoPais = em.merge(equipoPaisList1EquipoPais);
                if (oldCodEntrenadorPrincipalOfEquipoPaisList1EquipoPais != null) {
                    oldCodEntrenadorPrincipalOfEquipoPaisList1EquipoPais.getEquipoPaisList1().remove(equipoPaisList1EquipoPais);
                    oldCodEntrenadorPrincipalOfEquipoPaisList1EquipoPais = em.merge(oldCodEntrenadorPrincipalOfEquipoPaisList1EquipoPais);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEntrenador(entrenador.getCodEntrenador()) != null) {
                throw new PreexistingEntityException("Entrenador " + entrenador + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Entrenador entrenador) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Entrenador persistentEntrenador = em.find(Entrenador.class, entrenador.getCodEntrenador());
            Pais codPaisOld = persistentEntrenador.getCodPais();
            Pais codPaisNew = entrenador.getCodPais();
            List<EquipoPais> equipoPaisListOld = persistentEntrenador.getEquipoPaisList();
            List<EquipoPais> equipoPaisListNew = entrenador.getEquipoPaisList();
            List<EquipoPais> equipoPaisList1Old = persistentEntrenador.getEquipoPaisList1();
            List<EquipoPais> equipoPaisList1New = entrenador.getEquipoPaisList1();
            if (codPaisNew != null) {
                codPaisNew = em.getReference(codPaisNew.getClass(), codPaisNew.getCodPais());
                entrenador.setCodPais(codPaisNew);
            }
            List<EquipoPais> attachedEquipoPaisListNew = new ArrayList<EquipoPais>();
            for (EquipoPais equipoPaisListNewEquipoPaisToAttach : equipoPaisListNew) {
                equipoPaisListNewEquipoPaisToAttach = em.getReference(equipoPaisListNewEquipoPaisToAttach.getClass(), equipoPaisListNewEquipoPaisToAttach.getCodEquipo());
                attachedEquipoPaisListNew.add(equipoPaisListNewEquipoPaisToAttach);
            }
            equipoPaisListNew = attachedEquipoPaisListNew;
            entrenador.setEquipoPaisList(equipoPaisListNew);
            List<EquipoPais> attachedEquipoPaisList1New = new ArrayList<EquipoPais>();
            for (EquipoPais equipoPaisList1NewEquipoPaisToAttach : equipoPaisList1New) {
                equipoPaisList1NewEquipoPaisToAttach = em.getReference(equipoPaisList1NewEquipoPaisToAttach.getClass(), equipoPaisList1NewEquipoPaisToAttach.getCodEquipo());
                attachedEquipoPaisList1New.add(equipoPaisList1NewEquipoPaisToAttach);
            }
            equipoPaisList1New = attachedEquipoPaisList1New;
            entrenador.setEquipoPaisList1(equipoPaisList1New);
            entrenador = em.merge(entrenador);
            if (codPaisOld != null && !codPaisOld.equals(codPaisNew)) {
                codPaisOld.getEntrenadorList().remove(entrenador);
                codPaisOld = em.merge(codPaisOld);
            }
            if (codPaisNew != null && !codPaisNew.equals(codPaisOld)) {
                codPaisNew.getEntrenadorList().add(entrenador);
                codPaisNew = em.merge(codPaisNew);
            }
            for (EquipoPais equipoPaisListOldEquipoPais : equipoPaisListOld) {
                if (!equipoPaisListNew.contains(equipoPaisListOldEquipoPais)) {
                    equipoPaisListOldEquipoPais.setCodEntrenadorAuxiliar(null);
                    equipoPaisListOldEquipoPais = em.merge(equipoPaisListOldEquipoPais);
                }
            }
            for (EquipoPais equipoPaisListNewEquipoPais : equipoPaisListNew) {
                if (!equipoPaisListOld.contains(equipoPaisListNewEquipoPais)) {
                    Entrenador oldCodEntrenadorAuxiliarOfEquipoPaisListNewEquipoPais = equipoPaisListNewEquipoPais.getCodEntrenadorAuxiliar();
                    equipoPaisListNewEquipoPais.setCodEntrenadorAuxiliar(entrenador);
                    equipoPaisListNewEquipoPais = em.merge(equipoPaisListNewEquipoPais);
                    if (oldCodEntrenadorAuxiliarOfEquipoPaisListNewEquipoPais != null && !oldCodEntrenadorAuxiliarOfEquipoPaisListNewEquipoPais.equals(entrenador)) {
                        oldCodEntrenadorAuxiliarOfEquipoPaisListNewEquipoPais.getEquipoPaisList().remove(equipoPaisListNewEquipoPais);
                        oldCodEntrenadorAuxiliarOfEquipoPaisListNewEquipoPais = em.merge(oldCodEntrenadorAuxiliarOfEquipoPaisListNewEquipoPais);
                    }
                }
            }
            for (EquipoPais equipoPaisList1OldEquipoPais : equipoPaisList1Old) {
                if (!equipoPaisList1New.contains(equipoPaisList1OldEquipoPais)) {
                    equipoPaisList1OldEquipoPais.setCodEntrenadorPrincipal(null);
                    equipoPaisList1OldEquipoPais = em.merge(equipoPaisList1OldEquipoPais);
                }
            }
            for (EquipoPais equipoPaisList1NewEquipoPais : equipoPaisList1New) {
                if (!equipoPaisList1Old.contains(equipoPaisList1NewEquipoPais)) {
                    Entrenador oldCodEntrenadorPrincipalOfEquipoPaisList1NewEquipoPais = equipoPaisList1NewEquipoPais.getCodEntrenadorPrincipal();
                    equipoPaisList1NewEquipoPais.setCodEntrenadorPrincipal(entrenador);
                    equipoPaisList1NewEquipoPais = em.merge(equipoPaisList1NewEquipoPais);
                    if (oldCodEntrenadorPrincipalOfEquipoPaisList1NewEquipoPais != null && !oldCodEntrenadorPrincipalOfEquipoPaisList1NewEquipoPais.equals(entrenador)) {
                        oldCodEntrenadorPrincipalOfEquipoPaisList1NewEquipoPais.getEquipoPaisList1().remove(equipoPaisList1NewEquipoPais);
                        oldCodEntrenadorPrincipalOfEquipoPaisList1NewEquipoPais = em.merge(oldCodEntrenadorPrincipalOfEquipoPaisList1NewEquipoPais);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = entrenador.getCodEntrenador();
                if (findEntrenador(id) == null) {
                    throw new NonexistentEntityException("The entrenador with id " + id + " no longer exists.");
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
            Entrenador entrenador;
            try {
                entrenador = em.getReference(Entrenador.class, id);
                entrenador.getCodEntrenador();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The entrenador with id " + id + " no longer exists.", enfe);
            }
            Pais codPais = entrenador.getCodPais();
            if (codPais != null) {
                codPais.getEntrenadorList().remove(entrenador);
                codPais = em.merge(codPais);
            }
            List<EquipoPais> equipoPaisList = entrenador.getEquipoPaisList();
            for (EquipoPais equipoPaisListEquipoPais : equipoPaisList) {
                equipoPaisListEquipoPais.setCodEntrenadorAuxiliar(null);
                equipoPaisListEquipoPais = em.merge(equipoPaisListEquipoPais);
            }
            List<EquipoPais> equipoPaisList1 = entrenador.getEquipoPaisList1();
            for (EquipoPais equipoPaisList1EquipoPais : equipoPaisList1) {
                equipoPaisList1EquipoPais.setCodEntrenadorPrincipal(null);
                equipoPaisList1EquipoPais = em.merge(equipoPaisList1EquipoPais);
            }
            em.remove(entrenador);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Entrenador> findEntrenadorEntities() {
        return findEntrenadorEntities(true, -1, -1);
    }

    public List<Entrenador> findEntrenadorEntities(int maxResults, int firstResult) {
        return findEntrenadorEntities(false, maxResults, firstResult);
    }

    private List<Entrenador> findEntrenadorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Entrenador.class));
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

    public Entrenador findEntrenador(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Entrenador.class, id);
        } finally {
            em.close();
        }
    }

    public int getEntrenadorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Entrenador> rt = cq.from(Entrenador.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
