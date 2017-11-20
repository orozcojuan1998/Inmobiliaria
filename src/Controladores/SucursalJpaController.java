/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.IllegalOrphanException;
import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Agencia;
//import Entidades.RentaSolicitud;
import java.util.ArrayList;
import java.util.Collection;
import Entidades.Agente;
import Entidades.Sucursal;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Asus-pc
 */
public class SucursalJpaController implements Serializable {

    public SucursalJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Sucursal sucursal) throws PreexistingEntityException, Exception {
        /*if (sucursal.getRentaSolicitudCollection() == null) {
            sucursal.setRentaSolicitudCollection(new ArrayList<RentaSolicitud>());
        }*/
        if (sucursal.getAgenteCollection() == null) {
            sucursal.setAgenteCollection(new ArrayList<Agente>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Agencia agenciaNombreAgencia = sucursal.getAgenciaNombreAgencia();
            if (agenciaNombreAgencia != null) {
                agenciaNombreAgencia = em.getReference(agenciaNombreAgencia.getClass(), agenciaNombreAgencia.getNombreAgencia());
                sucursal.setAgenciaNombreAgencia(agenciaNombreAgencia);
            }
            /*Collection<RentaSolicitud> attachedRentaSolicitudCollection = new ArrayList<RentaSolicitud>();
            for (RentaSolicitud rentaSolicitudCollectionRentaSolicitudToAttach : sucursal.getRentaSolicitudCollection()) {
                rentaSolicitudCollectionRentaSolicitudToAttach = em.getReference(rentaSolicitudCollectionRentaSolicitudToAttach.getClass(), rentaSolicitudCollectionRentaSolicitudToAttach.getIdRegistroRenta());
                attachedRentaSolicitudCollection.add(rentaSolicitudCollectionRentaSolicitudToAttach);
            }*/
            //sucursal.setRentaSolicitudCollection(attachedRentaSolicitudCollection);
            Collection<Agente> attachedAgenteCollection = new ArrayList<Agente>();
            for (Agente agenteCollectionAgenteToAttach : sucursal.getAgenteCollection()) {
                agenteCollectionAgenteToAttach = em.getReference(agenteCollectionAgenteToAttach.getClass(), agenteCollectionAgenteToAttach.getUsuario());
                attachedAgenteCollection.add(agenteCollectionAgenteToAttach);
            }
            sucursal.setAgenteCollection(attachedAgenteCollection);
            em.persist(sucursal);
            if (agenciaNombreAgencia != null) {
                agenciaNombreAgencia.getSucursalCollection().add(sucursal);
                agenciaNombreAgencia = em.merge(agenciaNombreAgencia);
            }
            /*for (RentaSolicitud rentaSolicitudCollectionRentaSolicitud : sucursal.getRentaSolicitudCollection()) {
                Sucursal oldSucursalNumeroSucursalOfRentaSolicitudCollectionRentaSolicitud = rentaSolicitudCollectionRentaSolicitud.getSucursalNumeroSucursal();
                rentaSolicitudCollectionRentaSolicitud.setSucursalNumeroSucursal(sucursal);
                rentaSolicitudCollectionRentaSolicitud = em.merge(rentaSolicitudCollectionRentaSolicitud);
                if (oldSucursalNumeroSucursalOfRentaSolicitudCollectionRentaSolicitud != null) {
                    oldSucursalNumeroSucursalOfRentaSolicitudCollectionRentaSolicitud.getRentaSolicitudCollection().remove(rentaSolicitudCollectionRentaSolicitud);
                    oldSucursalNumeroSucursalOfRentaSolicitudCollectionRentaSolicitud = em.merge(oldSucursalNumeroSucursalOfRentaSolicitudCollectionRentaSolicitud);
                }
            }*/
            for (Agente agenteCollectionAgente : sucursal.getAgenteCollection()) {
                Sucursal oldSucursalNumeroSucursalOfAgenteCollectionAgente = agenteCollectionAgente.getSucursalNumeroSucursal();
                agenteCollectionAgente.setSucursalNumeroSucursal(sucursal);
                agenteCollectionAgente = em.merge(agenteCollectionAgente);
                if (oldSucursalNumeroSucursalOfAgenteCollectionAgente != null) {
                    oldSucursalNumeroSucursalOfAgenteCollectionAgente.getAgenteCollection().remove(agenteCollectionAgente);
                    oldSucursalNumeroSucursalOfAgenteCollectionAgente = em.merge(oldSucursalNumeroSucursalOfAgenteCollectionAgente);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSucursal(sucursal.getNumeroSucursal()) != null) {
                throw new PreexistingEntityException("Sucursal " + sucursal + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Sucursal sucursal) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sucursal persistentSucursal = em.find(Sucursal.class, sucursal.getNumeroSucursal());
            Agencia agenciaNombreAgenciaOld = persistentSucursal.getAgenciaNombreAgencia();
            Agencia agenciaNombreAgenciaNew = sucursal.getAgenciaNombreAgencia();
            //Collection<RentaSolicitud> rentaSolicitudCollectionOld = persistentSucursal.getRentaSolicitudCollection();
            //Collection<RentaSolicitud> rentaSolicitudCollectionNew = sucursal.getRentaSolicitudCollection();
            Collection<Agente> agenteCollectionOld = persistentSucursal.getAgenteCollection();
            Collection<Agente> agenteCollectionNew = sucursal.getAgenteCollection();
            List<String> illegalOrphanMessages = null;
            /*for (RentaSolicitud rentaSolicitudCollectionOldRentaSolicitud : rentaSolicitudCollectionOld) {
                if (!rentaSolicitudCollectionNew.contains(rentaSolicitudCollectionOldRentaSolicitud)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RentaSolicitud " + rentaSolicitudCollectionOldRentaSolicitud + " since its sucursalNumeroSucursal field is not nullable.");
                }
            }*/
            for (Agente agenteCollectionOldAgente : agenteCollectionOld) {
                if (!agenteCollectionNew.contains(agenteCollectionOldAgente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Agente " + agenteCollectionOldAgente + " since its sucursalNumeroSucursal field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (agenciaNombreAgenciaNew != null) {
                agenciaNombreAgenciaNew = em.getReference(agenciaNombreAgenciaNew.getClass(), agenciaNombreAgenciaNew.getNombreAgencia());
                sucursal.setAgenciaNombreAgencia(agenciaNombreAgenciaNew);
            }
            /*Collection<RentaSolicitud> attachedRentaSolicitudCollectionNew = new ArrayList<RentaSolicitud>();
            for (RentaSolicitud rentaSolicitudCollectionNewRentaSolicitudToAttach : rentaSolicitudCollectionNew) {
                rentaSolicitudCollectionNewRentaSolicitudToAttach = em.getReference(rentaSolicitudCollectionNewRentaSolicitudToAttach.getClass(), rentaSolicitudCollectionNewRentaSolicitudToAttach.getIdRegistroRenta());
                attachedRentaSolicitudCollectionNew.add(rentaSolicitudCollectionNewRentaSolicitudToAttach);
            }*/
            //rentaSolicitudCollectionNew = attachedRentaSolicitudCollectionNew;
            //sucursal.setRentaSolicitudCollection(rentaSolicitudCollectionNew);
            Collection<Agente> attachedAgenteCollectionNew = new ArrayList<Agente>();
            for (Agente agenteCollectionNewAgenteToAttach : agenteCollectionNew) {
                agenteCollectionNewAgenteToAttach = em.getReference(agenteCollectionNewAgenteToAttach.getClass(), agenteCollectionNewAgenteToAttach.getUsuario());
                attachedAgenteCollectionNew.add(agenteCollectionNewAgenteToAttach);
            }
            agenteCollectionNew = attachedAgenteCollectionNew;
            sucursal.setAgenteCollection(agenteCollectionNew);
            sucursal = em.merge(sucursal);
            if (agenciaNombreAgenciaOld != null && !agenciaNombreAgenciaOld.equals(agenciaNombreAgenciaNew)) {
                agenciaNombreAgenciaOld.getSucursalCollection().remove(sucursal);
                agenciaNombreAgenciaOld = em.merge(agenciaNombreAgenciaOld);
            }
            if (agenciaNombreAgenciaNew != null && !agenciaNombreAgenciaNew.equals(agenciaNombreAgenciaOld)) {
                agenciaNombreAgenciaNew.getSucursalCollection().add(sucursal);
                agenciaNombreAgenciaNew = em.merge(agenciaNombreAgenciaNew);
            }
            /*for (RentaSolicitud rentaSolicitudCollectionNewRentaSolicitud : rentaSolicitudCollectionNew) {
                if (!rentaSolicitudCollectionOld.contains(rentaSolicitudCollectionNewRentaSolicitud)) {
                    Sucursal oldSucursalNumeroSucursalOfRentaSolicitudCollectionNewRentaSolicitud = rentaSolicitudCollectionNewRentaSolicitud.getSucursalNumeroSucursal();
                    rentaSolicitudCollectionNewRentaSolicitud.setSucursalNumeroSucursal(sucursal);
                    rentaSolicitudCollectionNewRentaSolicitud = em.merge(rentaSolicitudCollectionNewRentaSolicitud);
                    if (oldSucursalNumeroSucursalOfRentaSolicitudCollectionNewRentaSolicitud != null && !oldSucursalNumeroSucursalOfRentaSolicitudCollectionNewRentaSolicitud.equals(sucursal)) {
                        oldSucursalNumeroSucursalOfRentaSolicitudCollectionNewRentaSolicitud.getRentaSolicitudCollection().remove(rentaSolicitudCollectionNewRentaSolicitud);
                        oldSucursalNumeroSucursalOfRentaSolicitudCollectionNewRentaSolicitud = em.merge(oldSucursalNumeroSucursalOfRentaSolicitudCollectionNewRentaSolicitud);
                    }
                }
            }*/
            for (Agente agenteCollectionNewAgente : agenteCollectionNew) {
                if (!agenteCollectionOld.contains(agenteCollectionNewAgente)) {
                    Sucursal oldSucursalNumeroSucursalOfAgenteCollectionNewAgente = agenteCollectionNewAgente.getSucursalNumeroSucursal();
                    agenteCollectionNewAgente.setSucursalNumeroSucursal(sucursal);
                    agenteCollectionNewAgente = em.merge(agenteCollectionNewAgente);
                    if (oldSucursalNumeroSucursalOfAgenteCollectionNewAgente != null && !oldSucursalNumeroSucursalOfAgenteCollectionNewAgente.equals(sucursal)) {
                        oldSucursalNumeroSucursalOfAgenteCollectionNewAgente.getAgenteCollection().remove(agenteCollectionNewAgente);
                        oldSucursalNumeroSucursalOfAgenteCollectionNewAgente = em.merge(oldSucursalNumeroSucursalOfAgenteCollectionNewAgente);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = sucursal.getNumeroSucursal();
                if (findSucursal(id) == null) {
                    throw new NonexistentEntityException("The sucursal with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sucursal sucursal;
            try {
                sucursal = em.getReference(Sucursal.class, id);
                sucursal.getNumeroSucursal();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sucursal with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            //Collection<RentaSolicitud> rentaSolicitudCollectionOrphanCheck = sucursal.getRentaSolicitudCollection();
            /*for (RentaSolicitud rentaSolicitudCollectionOrphanCheckRentaSolicitud : rentaSolicitudCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Sucursal (" + sucursal + ") cannot be destroyed since the RentaSolicitud " + rentaSolicitudCollectionOrphanCheckRentaSolicitud + " in its rentaSolicitudCollection field has a non-nullable sucursalNumeroSucursal field.");
            }*/
            Collection<Agente> agenteCollectionOrphanCheck = sucursal.getAgenteCollection();
            for (Agente agenteCollectionOrphanCheckAgente : agenteCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Sucursal (" + sucursal + ") cannot be destroyed since the Agente " + agenteCollectionOrphanCheckAgente + " in its agenteCollection field has a non-nullable sucursalNumeroSucursal field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Agencia agenciaNombreAgencia = sucursal.getAgenciaNombreAgencia();
            if (agenciaNombreAgencia != null) {
                agenciaNombreAgencia.getSucursalCollection().remove(sucursal);
                agenciaNombreAgencia = em.merge(agenciaNombreAgencia);
            }
            em.remove(sucursal);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Sucursal> findSucursalEntities() {
        return findSucursalEntities(true, -1, -1);
    }

    public List<Sucursal> findSucursalEntities(int maxResults, int firstResult) {
        return findSucursalEntities(false, maxResults, firstResult);
    }

    private List<Sucursal> findSucursalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Sucursal.class));
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

    public Sucursal findSucursal(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Sucursal.class, id);
        } finally {
            em.close();
        }
    }

    public int getSucursalCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Sucursal> rt = cq.from(Sucursal.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
