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
import entities.Entrenador;
import entities.EquipoPais;
import entities.Grupo;
import entities.Pais;
import entities.Jugador;
import java.util.ArrayList;
import java.util.List;
import entities.Partido;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author linam
 */
public class EquipoPaisJpaController implements Serializable {

    public EquipoPaisJpaController()
    {
        
    }
    public EquipoPaisJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("ControladorPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EquipoPais equipoPais) throws PreexistingEntityException, Exception {
        if (equipoPais.getJugadorList() == null) {
            equipoPais.setJugadorList(new ArrayList<Jugador>());
        }
        if (equipoPais.getPartidoList() == null) {
            equipoPais.setPartidoList(new ArrayList<Partido>());
        }
        if (equipoPais.getPartidoList1() == null) {
            equipoPais.setPartidoList1(new ArrayList<Partido>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Entrenador codEntrenadorAuxiliar = equipoPais.getCodEntrenadorAuxiliar();
            if (codEntrenadorAuxiliar != null) {
                codEntrenadorAuxiliar = em.getReference(codEntrenadorAuxiliar.getClass(), codEntrenadorAuxiliar.getCodEntrenador());
                equipoPais.setCodEntrenadorAuxiliar(codEntrenadorAuxiliar);
            }
            Entrenador codEntrenadorPrincipal = equipoPais.getCodEntrenadorPrincipal();
            if (codEntrenadorPrincipal != null) {
                codEntrenadorPrincipal = em.getReference(codEntrenadorPrincipal.getClass(), codEntrenadorPrincipal.getCodEntrenador());
                equipoPais.setCodEntrenadorPrincipal(codEntrenadorPrincipal);
            }
            Grupo codGrupo = equipoPais.getCodGrupo();
            if (codGrupo != null) {
                codGrupo = em.getReference(codGrupo.getClass(), codGrupo.getCodGrupo());
                equipoPais.setCodGrupo(codGrupo);
            }
            Pais codPais = equipoPais.getCodPais();
            if (codPais != null) {
                codPais = em.getReference(codPais.getClass(), codPais.getCodPais());
                equipoPais.setCodPais(codPais);
            }
            List<Jugador> attachedJugadorList = new ArrayList<Jugador>();
            for (Jugador jugadorListJugadorToAttach : equipoPais.getJugadorList()) {
                jugadorListJugadorToAttach = em.getReference(jugadorListJugadorToAttach.getClass(), jugadorListJugadorToAttach.getJugadorPK());
                attachedJugadorList.add(jugadorListJugadorToAttach);
            }
            equipoPais.setJugadorList(attachedJugadorList);
            List<Partido> attachedPartidoList = new ArrayList<Partido>();
            for (Partido partidoListPartidoToAttach : equipoPais.getPartidoList()) {
                partidoListPartidoToAttach = em.getReference(partidoListPartidoToAttach.getClass(), partidoListPartidoToAttach.getNumPartido());
                attachedPartidoList.add(partidoListPartidoToAttach);
            }
            equipoPais.setPartidoList(attachedPartidoList);
            List<Partido> attachedPartidoList1 = new ArrayList<Partido>();
            for (Partido partidoList1PartidoToAttach : equipoPais.getPartidoList1()) {
                partidoList1PartidoToAttach = em.getReference(partidoList1PartidoToAttach.getClass(), partidoList1PartidoToAttach.getNumPartido());
                attachedPartidoList1.add(partidoList1PartidoToAttach);
            }
            equipoPais.setPartidoList1(attachedPartidoList1);
            em.persist(equipoPais);
            if (codEntrenadorAuxiliar != null) {
                codEntrenadorAuxiliar.getEquipoPaisList().add(equipoPais);
                codEntrenadorAuxiliar = em.merge(codEntrenadorAuxiliar);
            }
            if (codEntrenadorPrincipal != null) {
                codEntrenadorPrincipal.getEquipoPaisList().add(equipoPais);
                codEntrenadorPrincipal = em.merge(codEntrenadorPrincipal);
            }
            if (codGrupo != null) {
                codGrupo.getEquipoPaisList().add(equipoPais);
                codGrupo = em.merge(codGrupo);
            }
            if (codPais != null) {
                codPais.getEquipoPaisList().add(equipoPais);
                codPais = em.merge(codPais);
            }
            for (Jugador jugadorListJugador : equipoPais.getJugadorList()) {
                EquipoPais oldEquipoPaisOfJugadorListJugador = jugadorListJugador.getEquipoPais();
                jugadorListJugador.setEquipoPais(equipoPais);
                jugadorListJugador = em.merge(jugadorListJugador);
                if (oldEquipoPaisOfJugadorListJugador != null) {
                    oldEquipoPaisOfJugadorListJugador.getJugadorList().remove(jugadorListJugador);
                    oldEquipoPaisOfJugadorListJugador = em.merge(oldEquipoPaisOfJugadorListJugador);
                }
            }
            for (Partido partidoListPartido : equipoPais.getPartidoList()) {
                EquipoPais oldCodEquipoLocalOfPartidoListPartido = partidoListPartido.getCodEquipoLocal();
                partidoListPartido.setCodEquipoLocal(equipoPais);
                partidoListPartido = em.merge(partidoListPartido);
                if (oldCodEquipoLocalOfPartidoListPartido != null) {
                    oldCodEquipoLocalOfPartidoListPartido.getPartidoList().remove(partidoListPartido);
                    oldCodEquipoLocalOfPartidoListPartido = em.merge(oldCodEquipoLocalOfPartidoListPartido);
                }
            }
            for (Partido partidoList1Partido : equipoPais.getPartidoList1()) {
                EquipoPais oldCodEquipoVisitanteOfPartidoList1Partido = partidoList1Partido.getCodEquipoVisitante();
                partidoList1Partido.setCodEquipoVisitante(equipoPais);
                partidoList1Partido = em.merge(partidoList1Partido);
                if (oldCodEquipoVisitanteOfPartidoList1Partido != null) {
                    oldCodEquipoVisitanteOfPartidoList1Partido.getPartidoList1().remove(partidoList1Partido);
                    oldCodEquipoVisitanteOfPartidoList1Partido = em.merge(oldCodEquipoVisitanteOfPartidoList1Partido);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEquipoPais(equipoPais.getCodEquipo()) != null) {
                throw new PreexistingEntityException("EquipoPais " + equipoPais + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EquipoPais equipoPais) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EquipoPais persistentEquipoPais = em.find(EquipoPais.class, equipoPais.getCodEquipo());
            Entrenador codEntrenadorAuxiliarOld = persistentEquipoPais.getCodEntrenadorAuxiliar();
            Entrenador codEntrenadorAuxiliarNew = equipoPais.getCodEntrenadorAuxiliar();
            Entrenador codEntrenadorPrincipalOld = persistentEquipoPais.getCodEntrenadorPrincipal();
            Entrenador codEntrenadorPrincipalNew = equipoPais.getCodEntrenadorPrincipal();
            Grupo codGrupoOld = persistentEquipoPais.getCodGrupo();
            Grupo codGrupoNew = equipoPais.getCodGrupo();
            Pais codPaisOld = persistentEquipoPais.getCodPais();
            Pais codPaisNew = equipoPais.getCodPais();
            List<Jugador> jugadorListOld = persistentEquipoPais.getJugadorList();
            List<Jugador> jugadorListNew = equipoPais.getJugadorList();
            List<Partido> partidoListOld = persistentEquipoPais.getPartidoList();
            List<Partido> partidoListNew = equipoPais.getPartidoList();
            List<Partido> partidoList1Old = persistentEquipoPais.getPartidoList1();
            List<Partido> partidoList1New = equipoPais.getPartidoList1();
            List<String> illegalOrphanMessages = null;
            for (Jugador jugadorListOldJugador : jugadorListOld) {
                if (!jugadorListNew.contains(jugadorListOldJugador)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Jugador " + jugadorListOldJugador + " since its equipoPais field is not nullable.");
                }
            }
            for (Partido partidoListOldPartido : partidoListOld) {
                if (!partidoListNew.contains(partidoListOldPartido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Partido " + partidoListOldPartido + " since its codEquipoLocal field is not nullable.");
                }
            }
            for (Partido partidoList1OldPartido : partidoList1Old) {
                if (!partidoList1New.contains(partidoList1OldPartido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Partido " + partidoList1OldPartido + " since its codEquipoVisitante field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (codEntrenadorAuxiliarNew != null) {
                codEntrenadorAuxiliarNew = em.getReference(codEntrenadorAuxiliarNew.getClass(), codEntrenadorAuxiliarNew.getCodEntrenador());
                equipoPais.setCodEntrenadorAuxiliar(codEntrenadorAuxiliarNew);
            }
            if (codEntrenadorPrincipalNew != null) {
                codEntrenadorPrincipalNew = em.getReference(codEntrenadorPrincipalNew.getClass(), codEntrenadorPrincipalNew.getCodEntrenador());
                equipoPais.setCodEntrenadorPrincipal(codEntrenadorPrincipalNew);
            }
            if (codGrupoNew != null) {
                codGrupoNew = em.getReference(codGrupoNew.getClass(), codGrupoNew.getCodGrupo());
                equipoPais.setCodGrupo(codGrupoNew);
            }
            if (codPaisNew != null) {
                codPaisNew = em.getReference(codPaisNew.getClass(), codPaisNew.getCodPais());
                equipoPais.setCodPais(codPaisNew);
            }
            List<Jugador> attachedJugadorListNew = new ArrayList<Jugador>();
            for (Jugador jugadorListNewJugadorToAttach : jugadorListNew) {
                jugadorListNewJugadorToAttach = em.getReference(jugadorListNewJugadorToAttach.getClass(), jugadorListNewJugadorToAttach.getJugadorPK());
                attachedJugadorListNew.add(jugadorListNewJugadorToAttach);
            }
            jugadorListNew = attachedJugadorListNew;
            equipoPais.setJugadorList(jugadorListNew);
            List<Partido> attachedPartidoListNew = new ArrayList<Partido>();
            for (Partido partidoListNewPartidoToAttach : partidoListNew) {
                partidoListNewPartidoToAttach = em.getReference(partidoListNewPartidoToAttach.getClass(), partidoListNewPartidoToAttach.getNumPartido());
                attachedPartidoListNew.add(partidoListNewPartidoToAttach);
            }
            partidoListNew = attachedPartidoListNew;
            equipoPais.setPartidoList(partidoListNew);
            List<Partido> attachedPartidoList1New = new ArrayList<Partido>();
            for (Partido partidoList1NewPartidoToAttach : partidoList1New) {
                partidoList1NewPartidoToAttach = em.getReference(partidoList1NewPartidoToAttach.getClass(), partidoList1NewPartidoToAttach.getNumPartido());
                attachedPartidoList1New.add(partidoList1NewPartidoToAttach);
            }
            partidoList1New = attachedPartidoList1New;
            equipoPais.setPartidoList1(partidoList1New);
            equipoPais = em.merge(equipoPais);
            if (codEntrenadorAuxiliarOld != null && !codEntrenadorAuxiliarOld.equals(codEntrenadorAuxiliarNew)) {
                codEntrenadorAuxiliarOld.getEquipoPaisList().remove(equipoPais);
                codEntrenadorAuxiliarOld = em.merge(codEntrenadorAuxiliarOld);
            }
            if (codEntrenadorAuxiliarNew != null && !codEntrenadorAuxiliarNew.equals(codEntrenadorAuxiliarOld)) {
                codEntrenadorAuxiliarNew.getEquipoPaisList().add(equipoPais);
                codEntrenadorAuxiliarNew = em.merge(codEntrenadorAuxiliarNew);
            }
            if (codEntrenadorPrincipalOld != null && !codEntrenadorPrincipalOld.equals(codEntrenadorPrincipalNew)) {
                codEntrenadorPrincipalOld.getEquipoPaisList().remove(equipoPais);
                codEntrenadorPrincipalOld = em.merge(codEntrenadorPrincipalOld);
            }
            if (codEntrenadorPrincipalNew != null && !codEntrenadorPrincipalNew.equals(codEntrenadorPrincipalOld)) {
                codEntrenadorPrincipalNew.getEquipoPaisList().add(equipoPais);
                codEntrenadorPrincipalNew = em.merge(codEntrenadorPrincipalNew);
            }
            if (codGrupoOld != null && !codGrupoOld.equals(codGrupoNew)) {
                codGrupoOld.getEquipoPaisList().remove(equipoPais);
                codGrupoOld = em.merge(codGrupoOld);
            }
            if (codGrupoNew != null && !codGrupoNew.equals(codGrupoOld)) {
                codGrupoNew.getEquipoPaisList().add(equipoPais);
                codGrupoNew = em.merge(codGrupoNew);
            }
            if (codPaisOld != null && !codPaisOld.equals(codPaisNew)) {
                codPaisOld.getEquipoPaisList().remove(equipoPais);
                codPaisOld = em.merge(codPaisOld);
            }
            if (codPaisNew != null && !codPaisNew.equals(codPaisOld)) {
                codPaisNew.getEquipoPaisList().add(equipoPais);
                codPaisNew = em.merge(codPaisNew);
            }
            for (Jugador jugadorListNewJugador : jugadorListNew) {
                if (!jugadorListOld.contains(jugadorListNewJugador)) {
                    EquipoPais oldEquipoPaisOfJugadorListNewJugador = jugadorListNewJugador.getEquipoPais();
                    jugadorListNewJugador.setEquipoPais(equipoPais);
                    jugadorListNewJugador = em.merge(jugadorListNewJugador);
                    if (oldEquipoPaisOfJugadorListNewJugador != null && !oldEquipoPaisOfJugadorListNewJugador.equals(equipoPais)) {
                        oldEquipoPaisOfJugadorListNewJugador.getJugadorList().remove(jugadorListNewJugador);
                        oldEquipoPaisOfJugadorListNewJugador = em.merge(oldEquipoPaisOfJugadorListNewJugador);
                    }
                }
            }
            for (Partido partidoListNewPartido : partidoListNew) {
                if (!partidoListOld.contains(partidoListNewPartido)) {
                    EquipoPais oldCodEquipoLocalOfPartidoListNewPartido = partidoListNewPartido.getCodEquipoLocal();
                    partidoListNewPartido.setCodEquipoLocal(equipoPais);
                    partidoListNewPartido = em.merge(partidoListNewPartido);
                    if (oldCodEquipoLocalOfPartidoListNewPartido != null && !oldCodEquipoLocalOfPartidoListNewPartido.equals(equipoPais)) {
                        oldCodEquipoLocalOfPartidoListNewPartido.getPartidoList().remove(partidoListNewPartido);
                        oldCodEquipoLocalOfPartidoListNewPartido = em.merge(oldCodEquipoLocalOfPartidoListNewPartido);
                    }
                }
            }
            for (Partido partidoList1NewPartido : partidoList1New) {
                if (!partidoList1Old.contains(partidoList1NewPartido)) {
                    EquipoPais oldCodEquipoVisitanteOfPartidoList1NewPartido = partidoList1NewPartido.getCodEquipoVisitante();
                    partidoList1NewPartido.setCodEquipoVisitante(equipoPais);
                    partidoList1NewPartido = em.merge(partidoList1NewPartido);
                    if (oldCodEquipoVisitanteOfPartidoList1NewPartido != null && !oldCodEquipoVisitanteOfPartidoList1NewPartido.equals(equipoPais)) {
                        oldCodEquipoVisitanteOfPartidoList1NewPartido.getPartidoList1().remove(partidoList1NewPartido);
                        oldCodEquipoVisitanteOfPartidoList1NewPartido = em.merge(oldCodEquipoVisitanteOfPartidoList1NewPartido);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = equipoPais.getCodEquipo();
                if (findEquipoPais(id) == null) {
                    throw new NonexistentEntityException("The equipoPais with id " + id + " no longer exists.");
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
            EquipoPais equipoPais;
            try {
                equipoPais = em.getReference(EquipoPais.class, id);
                equipoPais.getCodEquipo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The equipoPais with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Jugador> jugadorListOrphanCheck = equipoPais.getJugadorList();
            for (Jugador jugadorListOrphanCheckJugador : jugadorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EquipoPais (" + equipoPais + ") cannot be destroyed since the Jugador " + jugadorListOrphanCheckJugador + " in its jugadorList field has a non-nullable equipoPais field.");
            }
            List<Partido> partidoListOrphanCheck = equipoPais.getPartidoList();
            for (Partido partidoListOrphanCheckPartido : partidoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EquipoPais (" + equipoPais + ") cannot be destroyed since the Partido " + partidoListOrphanCheckPartido + " in its partidoList field has a non-nullable codEquipoLocal field.");
            }
            List<Partido> partidoList1OrphanCheck = equipoPais.getPartidoList1();
            for (Partido partidoList1OrphanCheckPartido : partidoList1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EquipoPais (" + equipoPais + ") cannot be destroyed since the Partido " + partidoList1OrphanCheckPartido + " in its partidoList1 field has a non-nullable codEquipoVisitante field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Entrenador codEntrenadorAuxiliar = equipoPais.getCodEntrenadorAuxiliar();
            if (codEntrenadorAuxiliar != null) {
                codEntrenadorAuxiliar.getEquipoPaisList().remove(equipoPais);
                codEntrenadorAuxiliar = em.merge(codEntrenadorAuxiliar);
            }
            Entrenador codEntrenadorPrincipal = equipoPais.getCodEntrenadorPrincipal();
            if (codEntrenadorPrincipal != null) {
                codEntrenadorPrincipal.getEquipoPaisList().remove(equipoPais);
                codEntrenadorPrincipal = em.merge(codEntrenadorPrincipal);
            }
            Grupo codGrupo = equipoPais.getCodGrupo();
            if (codGrupo != null) {
                codGrupo.getEquipoPaisList().remove(equipoPais);
                codGrupo = em.merge(codGrupo);
            }
            Pais codPais = equipoPais.getCodPais();
            if (codPais != null) {
                codPais.getEquipoPaisList().remove(equipoPais);
                codPais = em.merge(codPais);
            }
            em.remove(equipoPais);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EquipoPais> findEquipoPaisEntities() {
        return findEquipoPaisEntities(true, -1, -1);
    }

    public List<EquipoPais> findEquipoPaisEntities(int maxResults, int firstResult) {
        return findEquipoPaisEntities(false, maxResults, firstResult);
    }

    private List<EquipoPais> findEquipoPaisEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EquipoPais.class));
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

    public EquipoPais findEquipoPais(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EquipoPais.class, id);
        } finally {
            em.close();
        }
    }

    public int getEquipoPaisCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EquipoPais> rt = cq.from(EquipoPais.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
