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
import entities.EquipoPais;
import entities.Estadio;
import entities.Gol;
import entities.Partido;
import java.util.ArrayList;
import java.util.List;
import entities.Posicion;
import entities.Rol;
import entities.Tarjeta;
import entities.Silla;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author linam
 */
public class PartidoJpaController implements Serializable {

    public PartidoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Partido partido) throws PreexistingEntityException, Exception {
        if (partido.getGolList() == null) {
            partido.setGolList(new ArrayList<Gol>());
        }
        if (partido.getPosicionList() == null) {
            partido.setPosicionList(new ArrayList<Posicion>());
        }
        if (partido.getRolList() == null) {
            partido.setRolList(new ArrayList<Rol>());
        }
        if (partido.getTarjetaList() == null) {
            partido.setTarjetaList(new ArrayList<Tarjeta>());
        }
        if (partido.getSillaList() == null) {
            partido.setSillaList(new ArrayList<Silla>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EquipoPais codEquipoLocal = partido.getCodEquipoLocal();
            if (codEquipoLocal != null) {
                codEquipoLocal = em.getReference(codEquipoLocal.getClass(), codEquipoLocal.getCodEquipo());
                partido.setCodEquipoLocal(codEquipoLocal);
            }
            EquipoPais codEquipoVisitante = partido.getCodEquipoVisitante();
            if (codEquipoVisitante != null) {
                codEquipoVisitante = em.getReference(codEquipoVisitante.getClass(), codEquipoVisitante.getCodEquipo());
                partido.setCodEquipoVisitante(codEquipoVisitante);
            }
            Estadio codEstadio = partido.getCodEstadio();
            if (codEstadio != null) {
                codEstadio = em.getReference(codEstadio.getClass(), codEstadio.getCodEstadio());
                partido.setCodEstadio(codEstadio);
            }
            List<Gol> attachedGolList = new ArrayList<Gol>();
            for (Gol golListGolToAttach : partido.getGolList()) {
                golListGolToAttach = em.getReference(golListGolToAttach.getClass(), golListGolToAttach.getGolPK());
                attachedGolList.add(golListGolToAttach);
            }
            partido.setGolList(attachedGolList);
            List<Posicion> attachedPosicionList = new ArrayList<Posicion>();
            for (Posicion posicionListPosicionToAttach : partido.getPosicionList()) {
                posicionListPosicionToAttach = em.getReference(posicionListPosicionToAttach.getClass(), posicionListPosicionToAttach.getPosicionPK());
                attachedPosicionList.add(posicionListPosicionToAttach);
            }
            partido.setPosicionList(attachedPosicionList);
            List<Rol> attachedRolList = new ArrayList<Rol>();
            for (Rol rolListRolToAttach : partido.getRolList()) {
                rolListRolToAttach = em.getReference(rolListRolToAttach.getClass(), rolListRolToAttach.getRolPK());
                attachedRolList.add(rolListRolToAttach);
            }
            partido.setRolList(attachedRolList);
            List<Tarjeta> attachedTarjetaList = new ArrayList<Tarjeta>();
            for (Tarjeta tarjetaListTarjetaToAttach : partido.getTarjetaList()) {
                tarjetaListTarjetaToAttach = em.getReference(tarjetaListTarjetaToAttach.getClass(), tarjetaListTarjetaToAttach.getTarjetaPK());
                attachedTarjetaList.add(tarjetaListTarjetaToAttach);
            }
            partido.setTarjetaList(attachedTarjetaList);
            List<Silla> attachedSillaList = new ArrayList<Silla>();
            for (Silla sillaListSillaToAttach : partido.getSillaList()) {
                sillaListSillaToAttach = em.getReference(sillaListSillaToAttach.getClass(), sillaListSillaToAttach.getSillaPK());
                attachedSillaList.add(sillaListSillaToAttach);
            }
            partido.setSillaList(attachedSillaList);
            em.persist(partido);
            if (codEquipoLocal != null) {
                codEquipoLocal.getPartidoList().add(partido);
                codEquipoLocal = em.merge(codEquipoLocal);
            }
            if (codEquipoVisitante != null) {
                codEquipoVisitante.getPartidoList().add(partido);
                codEquipoVisitante = em.merge(codEquipoVisitante);
            }
            if (codEstadio != null) {
                codEstadio.getPartidoList().add(partido);
                codEstadio = em.merge(codEstadio);
            }
            for (Gol golListGol : partido.getGolList()) {
                Partido oldPartidoOfGolListGol = golListGol.getPartido();
                golListGol.setPartido(partido);
                golListGol = em.merge(golListGol);
                if (oldPartidoOfGolListGol != null) {
                    oldPartidoOfGolListGol.getGolList().remove(golListGol);
                    oldPartidoOfGolListGol = em.merge(oldPartidoOfGolListGol);
                }
            }
            for (Posicion posicionListPosicion : partido.getPosicionList()) {
                Partido oldPartidoOfPosicionListPosicion = posicionListPosicion.getPartido();
                posicionListPosicion.setPartido(partido);
                posicionListPosicion = em.merge(posicionListPosicion);
                if (oldPartidoOfPosicionListPosicion != null) {
                    oldPartidoOfPosicionListPosicion.getPosicionList().remove(posicionListPosicion);
                    oldPartidoOfPosicionListPosicion = em.merge(oldPartidoOfPosicionListPosicion);
                }
            }
            for (Rol rolListRol : partido.getRolList()) {
                Partido oldPartidoOfRolListRol = rolListRol.getPartido();
                rolListRol.setPartido(partido);
                rolListRol = em.merge(rolListRol);
                if (oldPartidoOfRolListRol != null) {
                    oldPartidoOfRolListRol.getRolList().remove(rolListRol);
                    oldPartidoOfRolListRol = em.merge(oldPartidoOfRolListRol);
                }
            }
            for (Tarjeta tarjetaListTarjeta : partido.getTarjetaList()) {
                Partido oldPartidoOfTarjetaListTarjeta = tarjetaListTarjeta.getPartido();
                tarjetaListTarjeta.setPartido(partido);
                tarjetaListTarjeta = em.merge(tarjetaListTarjeta);
                if (oldPartidoOfTarjetaListTarjeta != null) {
                    oldPartidoOfTarjetaListTarjeta.getTarjetaList().remove(tarjetaListTarjeta);
                    oldPartidoOfTarjetaListTarjeta = em.merge(oldPartidoOfTarjetaListTarjeta);
                }
            }
            for (Silla sillaListSilla : partido.getSillaList()) {
                Partido oldPartidoOfSillaListSilla = sillaListSilla.getPartido();
                sillaListSilla.setPartido(partido);
                sillaListSilla = em.merge(sillaListSilla);
                if (oldPartidoOfSillaListSilla != null) {
                    oldPartidoOfSillaListSilla.getSillaList().remove(sillaListSilla);
                    oldPartidoOfSillaListSilla = em.merge(oldPartidoOfSillaListSilla);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPartido(partido.getNumPartido()) != null) {
                throw new PreexistingEntityException("Partido " + partido + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Partido partido) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Partido persistentPartido = em.find(Partido.class, partido.getNumPartido());
            EquipoPais codEquipoLocalOld = persistentPartido.getCodEquipoLocal();
            EquipoPais codEquipoLocalNew = partido.getCodEquipoLocal();
            EquipoPais codEquipoVisitanteOld = persistentPartido.getCodEquipoVisitante();
            EquipoPais codEquipoVisitanteNew = partido.getCodEquipoVisitante();
            Estadio codEstadioOld = persistentPartido.getCodEstadio();
            Estadio codEstadioNew = partido.getCodEstadio();
            List<Gol> golListOld = persistentPartido.getGolList();
            List<Gol> golListNew = partido.getGolList();
            List<Posicion> posicionListOld = persistentPartido.getPosicionList();
            List<Posicion> posicionListNew = partido.getPosicionList();
            List<Rol> rolListOld = persistentPartido.getRolList();
            List<Rol> rolListNew = partido.getRolList();
            List<Tarjeta> tarjetaListOld = persistentPartido.getTarjetaList();
            List<Tarjeta> tarjetaListNew = partido.getTarjetaList();
            List<Silla> sillaListOld = persistentPartido.getSillaList();
            List<Silla> sillaListNew = partido.getSillaList();
            List<String> illegalOrphanMessages = null;
            for (Gol golListOldGol : golListOld) {
                if (!golListNew.contains(golListOldGol)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Gol " + golListOldGol + " since its partido field is not nullable.");
                }
            }
            for (Posicion posicionListOldPosicion : posicionListOld) {
                if (!posicionListNew.contains(posicionListOldPosicion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Posicion " + posicionListOldPosicion + " since its partido field is not nullable.");
                }
            }
            for (Rol rolListOldRol : rolListOld) {
                if (!rolListNew.contains(rolListOldRol)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Rol " + rolListOldRol + " since its partido field is not nullable.");
                }
            }
            for (Tarjeta tarjetaListOldTarjeta : tarjetaListOld) {
                if (!tarjetaListNew.contains(tarjetaListOldTarjeta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tarjeta " + tarjetaListOldTarjeta + " since its partido field is not nullable.");
                }
            }
            for (Silla sillaListOldSilla : sillaListOld) {
                if (!sillaListNew.contains(sillaListOldSilla)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Silla " + sillaListOldSilla + " since its partido field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (codEquipoLocalNew != null) {
                codEquipoLocalNew = em.getReference(codEquipoLocalNew.getClass(), codEquipoLocalNew.getCodEquipo());
                partido.setCodEquipoLocal(codEquipoLocalNew);
            }
            if (codEquipoVisitanteNew != null) {
                codEquipoVisitanteNew = em.getReference(codEquipoVisitanteNew.getClass(), codEquipoVisitanteNew.getCodEquipo());
                partido.setCodEquipoVisitante(codEquipoVisitanteNew);
            }
            if (codEstadioNew != null) {
                codEstadioNew = em.getReference(codEstadioNew.getClass(), codEstadioNew.getCodEstadio());
                partido.setCodEstadio(codEstadioNew);
            }
            List<Gol> attachedGolListNew = new ArrayList<Gol>();
            for (Gol golListNewGolToAttach : golListNew) {
                golListNewGolToAttach = em.getReference(golListNewGolToAttach.getClass(), golListNewGolToAttach.getGolPK());
                attachedGolListNew.add(golListNewGolToAttach);
            }
            golListNew = attachedGolListNew;
            partido.setGolList(golListNew);
            List<Posicion> attachedPosicionListNew = new ArrayList<Posicion>();
            for (Posicion posicionListNewPosicionToAttach : posicionListNew) {
                posicionListNewPosicionToAttach = em.getReference(posicionListNewPosicionToAttach.getClass(), posicionListNewPosicionToAttach.getPosicionPK());
                attachedPosicionListNew.add(posicionListNewPosicionToAttach);
            }
            posicionListNew = attachedPosicionListNew;
            partido.setPosicionList(posicionListNew);
            List<Rol> attachedRolListNew = new ArrayList<Rol>();
            for (Rol rolListNewRolToAttach : rolListNew) {
                rolListNewRolToAttach = em.getReference(rolListNewRolToAttach.getClass(), rolListNewRolToAttach.getRolPK());
                attachedRolListNew.add(rolListNewRolToAttach);
            }
            rolListNew = attachedRolListNew;
            partido.setRolList(rolListNew);
            List<Tarjeta> attachedTarjetaListNew = new ArrayList<Tarjeta>();
            for (Tarjeta tarjetaListNewTarjetaToAttach : tarjetaListNew) {
                tarjetaListNewTarjetaToAttach = em.getReference(tarjetaListNewTarjetaToAttach.getClass(), tarjetaListNewTarjetaToAttach.getTarjetaPK());
                attachedTarjetaListNew.add(tarjetaListNewTarjetaToAttach);
            }
            tarjetaListNew = attachedTarjetaListNew;
            partido.setTarjetaList(tarjetaListNew);
            List<Silla> attachedSillaListNew = new ArrayList<Silla>();
            for (Silla sillaListNewSillaToAttach : sillaListNew) {
                sillaListNewSillaToAttach = em.getReference(sillaListNewSillaToAttach.getClass(), sillaListNewSillaToAttach.getSillaPK());
                attachedSillaListNew.add(sillaListNewSillaToAttach);
            }
            sillaListNew = attachedSillaListNew;
            partido.setSillaList(sillaListNew);
            partido = em.merge(partido);
            if (codEquipoLocalOld != null && !codEquipoLocalOld.equals(codEquipoLocalNew)) {
                codEquipoLocalOld.getPartidoList().remove(partido);
                codEquipoLocalOld = em.merge(codEquipoLocalOld);
            }
            if (codEquipoLocalNew != null && !codEquipoLocalNew.equals(codEquipoLocalOld)) {
                codEquipoLocalNew.getPartidoList().add(partido);
                codEquipoLocalNew = em.merge(codEquipoLocalNew);
            }
            if (codEquipoVisitanteOld != null && !codEquipoVisitanteOld.equals(codEquipoVisitanteNew)) {
                codEquipoVisitanteOld.getPartidoList().remove(partido);
                codEquipoVisitanteOld = em.merge(codEquipoVisitanteOld);
            }
            if (codEquipoVisitanteNew != null && !codEquipoVisitanteNew.equals(codEquipoVisitanteOld)) {
                codEquipoVisitanteNew.getPartidoList().add(partido);
                codEquipoVisitanteNew = em.merge(codEquipoVisitanteNew);
            }
            if (codEstadioOld != null && !codEstadioOld.equals(codEstadioNew)) {
                codEstadioOld.getPartidoList().remove(partido);
                codEstadioOld = em.merge(codEstadioOld);
            }
            if (codEstadioNew != null && !codEstadioNew.equals(codEstadioOld)) {
                codEstadioNew.getPartidoList().add(partido);
                codEstadioNew = em.merge(codEstadioNew);
            }
            for (Gol golListNewGol : golListNew) {
                if (!golListOld.contains(golListNewGol)) {
                    Partido oldPartidoOfGolListNewGol = golListNewGol.getPartido();
                    golListNewGol.setPartido(partido);
                    golListNewGol = em.merge(golListNewGol);
                    if (oldPartidoOfGolListNewGol != null && !oldPartidoOfGolListNewGol.equals(partido)) {
                        oldPartidoOfGolListNewGol.getGolList().remove(golListNewGol);
                        oldPartidoOfGolListNewGol = em.merge(oldPartidoOfGolListNewGol);
                    }
                }
            }
            for (Posicion posicionListNewPosicion : posicionListNew) {
                if (!posicionListOld.contains(posicionListNewPosicion)) {
                    Partido oldPartidoOfPosicionListNewPosicion = posicionListNewPosicion.getPartido();
                    posicionListNewPosicion.setPartido(partido);
                    posicionListNewPosicion = em.merge(posicionListNewPosicion);
                    if (oldPartidoOfPosicionListNewPosicion != null && !oldPartidoOfPosicionListNewPosicion.equals(partido)) {
                        oldPartidoOfPosicionListNewPosicion.getPosicionList().remove(posicionListNewPosicion);
                        oldPartidoOfPosicionListNewPosicion = em.merge(oldPartidoOfPosicionListNewPosicion);
                    }
                }
            }
            for (Rol rolListNewRol : rolListNew) {
                if (!rolListOld.contains(rolListNewRol)) {
                    Partido oldPartidoOfRolListNewRol = rolListNewRol.getPartido();
                    rolListNewRol.setPartido(partido);
                    rolListNewRol = em.merge(rolListNewRol);
                    if (oldPartidoOfRolListNewRol != null && !oldPartidoOfRolListNewRol.equals(partido)) {
                        oldPartidoOfRolListNewRol.getRolList().remove(rolListNewRol);
                        oldPartidoOfRolListNewRol = em.merge(oldPartidoOfRolListNewRol);
                    }
                }
            }
            for (Tarjeta tarjetaListNewTarjeta : tarjetaListNew) {
                if (!tarjetaListOld.contains(tarjetaListNewTarjeta)) {
                    Partido oldPartidoOfTarjetaListNewTarjeta = tarjetaListNewTarjeta.getPartido();
                    tarjetaListNewTarjeta.setPartido(partido);
                    tarjetaListNewTarjeta = em.merge(tarjetaListNewTarjeta);
                    if (oldPartidoOfTarjetaListNewTarjeta != null && !oldPartidoOfTarjetaListNewTarjeta.equals(partido)) {
                        oldPartidoOfTarjetaListNewTarjeta.getTarjetaList().remove(tarjetaListNewTarjeta);
                        oldPartidoOfTarjetaListNewTarjeta = em.merge(oldPartidoOfTarjetaListNewTarjeta);
                    }
                }
            }
            for (Silla sillaListNewSilla : sillaListNew) {
                if (!sillaListOld.contains(sillaListNewSilla)) {
                    Partido oldPartidoOfSillaListNewSilla = sillaListNewSilla.getPartido();
                    sillaListNewSilla.setPartido(partido);
                    sillaListNewSilla = em.merge(sillaListNewSilla);
                    if (oldPartidoOfSillaListNewSilla != null && !oldPartidoOfSillaListNewSilla.equals(partido)) {
                        oldPartidoOfSillaListNewSilla.getSillaList().remove(sillaListNewSilla);
                        oldPartidoOfSillaListNewSilla = em.merge(oldPartidoOfSillaListNewSilla);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = partido.getNumPartido();
                if (findPartido(id) == null) {
                    throw new NonexistentEntityException("The partido with id " + id + " no longer exists.");
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
            Partido partido;
            try {
                partido = em.getReference(Partido.class, id);
                partido.getNumPartido();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The partido with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Gol> golListOrphanCheck = partido.getGolList();
            for (Gol golListOrphanCheckGol : golListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Partido (" + partido + ") cannot be destroyed since the Gol " + golListOrphanCheckGol + " in its golList field has a non-nullable partido field.");
            }
            List<Posicion> posicionListOrphanCheck = partido.getPosicionList();
            for (Posicion posicionListOrphanCheckPosicion : posicionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Partido (" + partido + ") cannot be destroyed since the Posicion " + posicionListOrphanCheckPosicion + " in its posicionList field has a non-nullable partido field.");
            }
            List<Rol> rolListOrphanCheck = partido.getRolList();
            for (Rol rolListOrphanCheckRol : rolListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Partido (" + partido + ") cannot be destroyed since the Rol " + rolListOrphanCheckRol + " in its rolList field has a non-nullable partido field.");
            }
            List<Tarjeta> tarjetaListOrphanCheck = partido.getTarjetaList();
            for (Tarjeta tarjetaListOrphanCheckTarjeta : tarjetaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Partido (" + partido + ") cannot be destroyed since the Tarjeta " + tarjetaListOrphanCheckTarjeta + " in its tarjetaList field has a non-nullable partido field.");
            }
            List<Silla> sillaListOrphanCheck = partido.getSillaList();
            for (Silla sillaListOrphanCheckSilla : sillaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Partido (" + partido + ") cannot be destroyed since the Silla " + sillaListOrphanCheckSilla + " in its sillaList field has a non-nullable partido field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            EquipoPais codEquipoLocal = partido.getCodEquipoLocal();
            if (codEquipoLocal != null) {
                codEquipoLocal.getPartidoList().remove(partido);
                codEquipoLocal = em.merge(codEquipoLocal);
            }
            EquipoPais codEquipoVisitante = partido.getCodEquipoVisitante();
            if (codEquipoVisitante != null) {
                codEquipoVisitante.getPartidoList().remove(partido);
                codEquipoVisitante = em.merge(codEquipoVisitante);
            }
            Estadio codEstadio = partido.getCodEstadio();
            if (codEstadio != null) {
                codEstadio.getPartidoList().remove(partido);
                codEstadio = em.merge(codEstadio);
            }
            em.remove(partido);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Partido> findPartidoEntities() {
        return findPartidoEntities(true, -1, -1);
    }

    public List<Partido> findPartidoEntities(int maxResults, int firstResult) {
        return findPartidoEntities(false, maxResults, firstResult);
    }

    private List<Partido> findPartidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Partido.class));
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

    public Partido findPartido(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Partido.class, id);
        } finally {
            em.close();
        }
    }

    public int getPartidoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Partido> rt = cq.from(Partido.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
