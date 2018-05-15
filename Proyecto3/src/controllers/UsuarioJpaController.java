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
import entities.Pais;
import entities.Silla;
import entities.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author linam
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, Exception {
        if (usuario.getSillaList() == null) {
            usuario.setSillaList(new ArrayList<Silla>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pais codPais = usuario.getCodPais();
            if (codPais != null) {
                codPais = em.getReference(codPais.getClass(), codPais.getCodPais());
                usuario.setCodPais(codPais);
            }
            List<Silla> attachedSillaList = new ArrayList<Silla>();
            for (Silla sillaListSillaToAttach : usuario.getSillaList()) {
                sillaListSillaToAttach = em.getReference(sillaListSillaToAttach.getClass(), sillaListSillaToAttach.getSillaPK());
                attachedSillaList.add(sillaListSillaToAttach);
            }
            usuario.setSillaList(attachedSillaList);
            em.persist(usuario);
            if (codPais != null) {
                codPais.getUsuarioList().add(usuario);
                codPais = em.merge(codPais);
            }
            for (Silla sillaListSilla : usuario.getSillaList()) {
                Usuario oldIdentificacionOfSillaListSilla = sillaListSilla.getIdentificacion();
                sillaListSilla.setIdentificacion(usuario);
                sillaListSilla = em.merge(sillaListSilla);
                if (oldIdentificacionOfSillaListSilla != null) {
                    oldIdentificacionOfSillaListSilla.getSillaList().remove(sillaListSilla);
                    oldIdentificacionOfSillaListSilla = em.merge(oldIdentificacionOfSillaListSilla);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuario(usuario.getIdentificacion()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdentificacion());
            Pais codPaisOld = persistentUsuario.getCodPais();
            Pais codPaisNew = usuario.getCodPais();
            List<Silla> sillaListOld = persistentUsuario.getSillaList();
            List<Silla> sillaListNew = usuario.getSillaList();
            if (codPaisNew != null) {
                codPaisNew = em.getReference(codPaisNew.getClass(), codPaisNew.getCodPais());
                usuario.setCodPais(codPaisNew);
            }
            List<Silla> attachedSillaListNew = new ArrayList<Silla>();
            for (Silla sillaListNewSillaToAttach : sillaListNew) {
                sillaListNewSillaToAttach = em.getReference(sillaListNewSillaToAttach.getClass(), sillaListNewSillaToAttach.getSillaPK());
                attachedSillaListNew.add(sillaListNewSillaToAttach);
            }
            sillaListNew = attachedSillaListNew;
            usuario.setSillaList(sillaListNew);
            usuario = em.merge(usuario);
            if (codPaisOld != null && !codPaisOld.equals(codPaisNew)) {
                codPaisOld.getUsuarioList().remove(usuario);
                codPaisOld = em.merge(codPaisOld);
            }
            if (codPaisNew != null && !codPaisNew.equals(codPaisOld)) {
                codPaisNew.getUsuarioList().add(usuario);
                codPaisNew = em.merge(codPaisNew);
            }
            for (Silla sillaListOldSilla : sillaListOld) {
                if (!sillaListNew.contains(sillaListOldSilla)) {
                    sillaListOldSilla.setIdentificacion(null);
                    sillaListOldSilla = em.merge(sillaListOldSilla);
                }
            }
            for (Silla sillaListNewSilla : sillaListNew) {
                if (!sillaListOld.contains(sillaListNewSilla)) {
                    Usuario oldIdentificacionOfSillaListNewSilla = sillaListNewSilla.getIdentificacion();
                    sillaListNewSilla.setIdentificacion(usuario);
                    sillaListNewSilla = em.merge(sillaListNewSilla);
                    if (oldIdentificacionOfSillaListNewSilla != null && !oldIdentificacionOfSillaListNewSilla.equals(usuario)) {
                        oldIdentificacionOfSillaListNewSilla.getSillaList().remove(sillaListNewSilla);
                        oldIdentificacionOfSillaListNewSilla = em.merge(oldIdentificacionOfSillaListNewSilla);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = usuario.getIdentificacion();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdentificacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            Pais codPais = usuario.getCodPais();
            if (codPais != null) {
                codPais.getUsuarioList().remove(usuario);
                codPais = em.merge(codPais);
            }
            List<Silla> sillaList = usuario.getSillaList();
            for (Silla sillaListSilla : sillaList) {
                sillaListSilla.setIdentificacion(null);
                sillaListSilla = em.merge(sillaListSilla);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
