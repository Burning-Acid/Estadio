/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.IllegalOrphanException;
import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Continente;
import entities.Juez;
import java.util.ArrayList;
import java.util.List;
import entities.EquipoPais;
import entities.Usuario;
import entities.Entrenador;
import entities.Pais;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author linam
 */
public class PaisJpaController implements Serializable {

    public PaisJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pais pais) throws PreexistingEntityException, Exception {
        if (pais.getJuezList() == null) {
            pais.setJuezList(new ArrayList<Juez>());
        }
        if (pais.getEquipoPaisList() == null) {
            pais.setEquipoPaisList(new ArrayList<EquipoPais>());
        }
        if (pais.getUsuarioList() == null) {
            pais.setUsuarioList(new ArrayList<Usuario>());
        }
        if (pais.getEntrenadorList() == null) {
            pais.setEntrenadorList(new ArrayList<Entrenador>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Continente codContinente = pais.getCodContinente();
            if (codContinente != null) {
                codContinente = em.getReference(codContinente.getClass(), codContinente.getCodContinente());
                pais.setCodContinente(codContinente);
            }
            List<Juez> attachedJuezList = new ArrayList<Juez>();
            for (Juez juezListJuezToAttach : pais.getJuezList()) {
                juezListJuezToAttach = em.getReference(juezListJuezToAttach.getClass(), juezListJuezToAttach.getCodJuez());
                attachedJuezList.add(juezListJuezToAttach);
            }
            pais.setJuezList(attachedJuezList);
            List<EquipoPais> attachedEquipoPaisList = new ArrayList<EquipoPais>();
            for (EquipoPais equipoPaisListEquipoPaisToAttach : pais.getEquipoPaisList()) {
                equipoPaisListEquipoPaisToAttach = em.getReference(equipoPaisListEquipoPaisToAttach.getClass(), equipoPaisListEquipoPaisToAttach.getCodEquipo());
                attachedEquipoPaisList.add(equipoPaisListEquipoPaisToAttach);
            }
            pais.setEquipoPaisList(attachedEquipoPaisList);
            List<Usuario> attachedUsuarioList = new ArrayList<Usuario>();
            for (Usuario usuarioListUsuarioToAttach : pais.getUsuarioList()) {
                usuarioListUsuarioToAttach = em.getReference(usuarioListUsuarioToAttach.getClass(), usuarioListUsuarioToAttach.getIdentificacion());
                attachedUsuarioList.add(usuarioListUsuarioToAttach);
            }
            pais.setUsuarioList(attachedUsuarioList);
            List<Entrenador> attachedEntrenadorList = new ArrayList<Entrenador>();
            for (Entrenador entrenadorListEntrenadorToAttach : pais.getEntrenadorList()) {
                entrenadorListEntrenadorToAttach = em.getReference(entrenadorListEntrenadorToAttach.getClass(), entrenadorListEntrenadorToAttach.getCodEntrenador());
                attachedEntrenadorList.add(entrenadorListEntrenadorToAttach);
            }
            pais.setEntrenadorList(attachedEntrenadorList);
            em.persist(pais);
            if (codContinente != null) {
                codContinente.getPaisList().add(pais);
                codContinente = em.merge(codContinente);
            }
            for (Juez juezListJuez : pais.getJuezList()) {
                Pais oldCodPaisOfJuezListJuez = juezListJuez.getCodPais();
                juezListJuez.setCodPais(pais);
                juezListJuez = em.merge(juezListJuez);
                if (oldCodPaisOfJuezListJuez != null) {
                    oldCodPaisOfJuezListJuez.getJuezList().remove(juezListJuez);
                    oldCodPaisOfJuezListJuez = em.merge(oldCodPaisOfJuezListJuez);
                }
            }
            for (EquipoPais equipoPaisListEquipoPais : pais.getEquipoPaisList()) {
                Pais oldCodPaisOfEquipoPaisListEquipoPais = equipoPaisListEquipoPais.getCodPais();
                equipoPaisListEquipoPais.setCodPais(pais);
                equipoPaisListEquipoPais = em.merge(equipoPaisListEquipoPais);
                if (oldCodPaisOfEquipoPaisListEquipoPais != null) {
                    oldCodPaisOfEquipoPaisListEquipoPais.getEquipoPaisList().remove(equipoPaisListEquipoPais);
                    oldCodPaisOfEquipoPaisListEquipoPais = em.merge(oldCodPaisOfEquipoPaisListEquipoPais);
                }
            }
            for (Usuario usuarioListUsuario : pais.getUsuarioList()) {
                Pais oldCodPaisOfUsuarioListUsuario = usuarioListUsuario.getCodPais();
                usuarioListUsuario.setCodPais(pais);
                usuarioListUsuario = em.merge(usuarioListUsuario);
                if (oldCodPaisOfUsuarioListUsuario != null) {
                    oldCodPaisOfUsuarioListUsuario.getUsuarioList().remove(usuarioListUsuario);
                    oldCodPaisOfUsuarioListUsuario = em.merge(oldCodPaisOfUsuarioListUsuario);
                }
            }
            for (Entrenador entrenadorListEntrenador : pais.getEntrenadorList()) {
                Pais oldCodPaisOfEntrenadorListEntrenador = entrenadorListEntrenador.getCodPais();
                entrenadorListEntrenador.setCodPais(pais);
                entrenadorListEntrenador = em.merge(entrenadorListEntrenador);
                if (oldCodPaisOfEntrenadorListEntrenador != null) {
                    oldCodPaisOfEntrenadorListEntrenador.getEntrenadorList().remove(entrenadorListEntrenador);
                    oldCodPaisOfEntrenadorListEntrenador = em.merge(oldCodPaisOfEntrenadorListEntrenador);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPais(pais.getCodPais()) != null) {
                throw new PreexistingEntityException("Pais " + pais + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pais pais) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pais persistentPais = em.find(Pais.class, pais.getCodPais());
            Continente codContinenteOld = persistentPais.getCodContinente();
            Continente codContinenteNew = pais.getCodContinente();
            List<Juez> juezListOld = persistentPais.getJuezList();
            List<Juez> juezListNew = pais.getJuezList();
            List<EquipoPais> equipoPaisListOld = persistentPais.getEquipoPaisList();
            List<EquipoPais> equipoPaisListNew = pais.getEquipoPaisList();
            List<Usuario> usuarioListOld = persistentPais.getUsuarioList();
            List<Usuario> usuarioListNew = pais.getUsuarioList();
            List<Entrenador> entrenadorListOld = persistentPais.getEntrenadorList();
            List<Entrenador> entrenadorListNew = pais.getEntrenadorList();
            List<String> illegalOrphanMessages = null;
            for (Juez juezListOldJuez : juezListOld) {
                if (!juezListNew.contains(juezListOldJuez)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Juez " + juezListOldJuez + " since its codPais field is not nullable.");
                }
            }
            for (Usuario usuarioListOldUsuario : usuarioListOld) {
                if (!usuarioListNew.contains(usuarioListOldUsuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Usuario " + usuarioListOldUsuario + " since its codPais field is not nullable.");
                }
            }
            for (Entrenador entrenadorListOldEntrenador : entrenadorListOld) {
                if (!entrenadorListNew.contains(entrenadorListOldEntrenador)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Entrenador " + entrenadorListOldEntrenador + " since its codPais field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (codContinenteNew != null) {
                codContinenteNew = em.getReference(codContinenteNew.getClass(), codContinenteNew.getCodContinente());
                pais.setCodContinente(codContinenteNew);
            }
            List<Juez> attachedJuezListNew = new ArrayList<Juez>();
            for (Juez juezListNewJuezToAttach : juezListNew) {
                juezListNewJuezToAttach = em.getReference(juezListNewJuezToAttach.getClass(), juezListNewJuezToAttach.getCodJuez());
                attachedJuezListNew.add(juezListNewJuezToAttach);
            }
            juezListNew = attachedJuezListNew;
            pais.setJuezList(juezListNew);
            List<EquipoPais> attachedEquipoPaisListNew = new ArrayList<EquipoPais>();
            for (EquipoPais equipoPaisListNewEquipoPaisToAttach : equipoPaisListNew) {
                equipoPaisListNewEquipoPaisToAttach = em.getReference(equipoPaisListNewEquipoPaisToAttach.getClass(), equipoPaisListNewEquipoPaisToAttach.getCodEquipo());
                attachedEquipoPaisListNew.add(equipoPaisListNewEquipoPaisToAttach);
            }
            equipoPaisListNew = attachedEquipoPaisListNew;
            pais.setEquipoPaisList(equipoPaisListNew);
            List<Usuario> attachedUsuarioListNew = new ArrayList<Usuario>();
            for (Usuario usuarioListNewUsuarioToAttach : usuarioListNew) {
                usuarioListNewUsuarioToAttach = em.getReference(usuarioListNewUsuarioToAttach.getClass(), usuarioListNewUsuarioToAttach.getIdentificacion());
                attachedUsuarioListNew.add(usuarioListNewUsuarioToAttach);
            }
            usuarioListNew = attachedUsuarioListNew;
            pais.setUsuarioList(usuarioListNew);
            List<Entrenador> attachedEntrenadorListNew = new ArrayList<Entrenador>();
            for (Entrenador entrenadorListNewEntrenadorToAttach : entrenadorListNew) {
                entrenadorListNewEntrenadorToAttach = em.getReference(entrenadorListNewEntrenadorToAttach.getClass(), entrenadorListNewEntrenadorToAttach.getCodEntrenador());
                attachedEntrenadorListNew.add(entrenadorListNewEntrenadorToAttach);
            }
            entrenadorListNew = attachedEntrenadorListNew;
            pais.setEntrenadorList(entrenadorListNew);
            pais = em.merge(pais);
            if (codContinenteOld != null && !codContinenteOld.equals(codContinenteNew)) {
                codContinenteOld.getPaisList().remove(pais);
                codContinenteOld = em.merge(codContinenteOld);
            }
            if (codContinenteNew != null && !codContinenteNew.equals(codContinenteOld)) {
                codContinenteNew.getPaisList().add(pais);
                codContinenteNew = em.merge(codContinenteNew);
            }
            for (Juez juezListNewJuez : juezListNew) {
                if (!juezListOld.contains(juezListNewJuez)) {
                    Pais oldCodPaisOfJuezListNewJuez = juezListNewJuez.getCodPais();
                    juezListNewJuez.setCodPais(pais);
                    juezListNewJuez = em.merge(juezListNewJuez);
                    if (oldCodPaisOfJuezListNewJuez != null && !oldCodPaisOfJuezListNewJuez.equals(pais)) {
                        oldCodPaisOfJuezListNewJuez.getJuezList().remove(juezListNewJuez);
                        oldCodPaisOfJuezListNewJuez = em.merge(oldCodPaisOfJuezListNewJuez);
                    }
                }
            }
            for (EquipoPais equipoPaisListOldEquipoPais : equipoPaisListOld) {
                if (!equipoPaisListNew.contains(equipoPaisListOldEquipoPais)) {
                    equipoPaisListOldEquipoPais.setCodPais(null);
                    equipoPaisListOldEquipoPais = em.merge(equipoPaisListOldEquipoPais);
                }
            }
            for (EquipoPais equipoPaisListNewEquipoPais : equipoPaisListNew) {
                if (!equipoPaisListOld.contains(equipoPaisListNewEquipoPais)) {
                    Pais oldCodPaisOfEquipoPaisListNewEquipoPais = equipoPaisListNewEquipoPais.getCodPais();
                    equipoPaisListNewEquipoPais.setCodPais(pais);
                    equipoPaisListNewEquipoPais = em.merge(equipoPaisListNewEquipoPais);
                    if (oldCodPaisOfEquipoPaisListNewEquipoPais != null && !oldCodPaisOfEquipoPaisListNewEquipoPais.equals(pais)) {
                        oldCodPaisOfEquipoPaisListNewEquipoPais.getEquipoPaisList().remove(equipoPaisListNewEquipoPais);
                        oldCodPaisOfEquipoPaisListNewEquipoPais = em.merge(oldCodPaisOfEquipoPaisListNewEquipoPais);
                    }
                }
            }
            for (Usuario usuarioListNewUsuario : usuarioListNew) {
                if (!usuarioListOld.contains(usuarioListNewUsuario)) {
                    Pais oldCodPaisOfUsuarioListNewUsuario = usuarioListNewUsuario.getCodPais();
                    usuarioListNewUsuario.setCodPais(pais);
                    usuarioListNewUsuario = em.merge(usuarioListNewUsuario);
                    if (oldCodPaisOfUsuarioListNewUsuario != null && !oldCodPaisOfUsuarioListNewUsuario.equals(pais)) {
                        oldCodPaisOfUsuarioListNewUsuario.getUsuarioList().remove(usuarioListNewUsuario);
                        oldCodPaisOfUsuarioListNewUsuario = em.merge(oldCodPaisOfUsuarioListNewUsuario);
                    }
                }
            }
            for (Entrenador entrenadorListNewEntrenador : entrenadorListNew) {
                if (!entrenadorListOld.contains(entrenadorListNewEntrenador)) {
                    Pais oldCodPaisOfEntrenadorListNewEntrenador = entrenadorListNewEntrenador.getCodPais();
                    entrenadorListNewEntrenador.setCodPais(pais);
                    entrenadorListNewEntrenador = em.merge(entrenadorListNewEntrenador);
                    if (oldCodPaisOfEntrenadorListNewEntrenador != null && !oldCodPaisOfEntrenadorListNewEntrenador.equals(pais)) {
                        oldCodPaisOfEntrenadorListNewEntrenador.getEntrenadorList().remove(entrenadorListNewEntrenador);
                        oldCodPaisOfEntrenadorListNewEntrenador = em.merge(oldCodPaisOfEntrenadorListNewEntrenador);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = pais.getCodPais();
                if (findPais(id) == null) {
                    throw new NonexistentEntityException("The pais with id " + id + " no longer exists.");
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
            Pais pais;
            try {
                pais = em.getReference(Pais.class, id);
                pais.getCodPais();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pais with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Juez> juezListOrphanCheck = pais.getJuezList();
            for (Juez juezListOrphanCheckJuez : juezListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pais (" + pais + ") cannot be destroyed since the Juez " + juezListOrphanCheckJuez + " in its juezList field has a non-nullable codPais field.");
            }
            List<Usuario> usuarioListOrphanCheck = pais.getUsuarioList();
            for (Usuario usuarioListOrphanCheckUsuario : usuarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pais (" + pais + ") cannot be destroyed since the Usuario " + usuarioListOrphanCheckUsuario + " in its usuarioList field has a non-nullable codPais field.");
            }
            List<Entrenador> entrenadorListOrphanCheck = pais.getEntrenadorList();
            for (Entrenador entrenadorListOrphanCheckEntrenador : entrenadorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pais (" + pais + ") cannot be destroyed since the Entrenador " + entrenadorListOrphanCheckEntrenador + " in its entrenadorList field has a non-nullable codPais field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Continente codContinente = pais.getCodContinente();
            if (codContinente != null) {
                codContinente.getPaisList().remove(pais);
                codContinente = em.merge(codContinente);
            }
            List<EquipoPais> equipoPaisList = pais.getEquipoPaisList();
            for (EquipoPais equipoPaisListEquipoPais : equipoPaisList) {
                equipoPaisListEquipoPais.setCodPais(null);
                equipoPaisListEquipoPais = em.merge(equipoPaisListEquipoPais);
            }
            em.remove(pais);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pais> findPaisEntities() {
        return findPaisEntities(true, -1, -1);
    }

    public List<Pais> findPaisEntities(int maxResults, int firstResult) {
        return findPaisEntities(false, maxResults, firstResult);
    }

    private List<Pais> findPaisEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pais.class));
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

    public Pais findPais(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pais.class, id);
        } finally {
            em.close();
        }
    }

    public int getPaisCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pais> rt = cq.from(Pais.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
