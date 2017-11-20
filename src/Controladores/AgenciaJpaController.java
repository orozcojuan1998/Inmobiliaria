/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.IllegalOrphanException;
import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import Entidades.Agencia;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Sucursal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Asus-pc
 */
public class AgenciaJpaController implements Serializable {

    public AgenciaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Agencia agencia) throws PreexistingEntityException, Exception {
        if (agencia.getSucursalCollection() == null) {
            agencia.setSucursalCollection(new ArrayList<Sucursal>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Sucursal> attachedSucursalCollection = new ArrayList<Sucursal>();
            for (Sucursal sucursalCollectionSucursalToAttach : agencia.getSucursalCollection()) {
                sucursalCollectionSucursalToAttach = em.getReference(sucursalCollectionSucursalToAttach.getClass(), sucursalCollectionSucursalToAttach.getNumeroSucursal());
                attachedSucursalCollection.add(sucursalCollectionSucursalToAttach);
            }
            agencia.setSucursalCollection(attachedSucursalCollection);
            em.persist(agencia);
            for (Sucursal sucursalCollectionSucursal : agencia.getSucursalCollection()) {
                Agencia oldAgenciaNombreAgenciaOfSucursalCollectionSucursal = sucursalCollectionSucursal.getAgenciaNombreAgencia();
                sucursalCollectionSucursal.setAgenciaNombreAgencia(agencia);
                sucursalCollectionSucursal = em.merge(sucursalCollectionSucursal);
                if (oldAgenciaNombreAgenciaOfSucursalCollectionSucursal != null) {
                    oldAgenciaNombreAgenciaOfSucursalCollectionSucursal.getSucursalCollection().remove(sucursalCollectionSucursal);
                    oldAgenciaNombreAgenciaOfSucursalCollectionSucursal = em.merge(oldAgenciaNombreAgenciaOfSucursalCollectionSucursal);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAgencia(agencia.getNombreAgencia()) != null) {
                throw new PreexistingEntityException("Agencia " + agencia + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Agencia agencia) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Agencia persistentAgencia = em.find(Agencia.class, agencia.getNombreAgencia());
            Collection<Sucursal> sucursalCollectionOld = persistentAgencia.getSucursalCollection();
            Collection<Sucursal> sucursalCollectionNew = agencia.getSucursalCollection();
            List<String> illegalOrphanMessages = null;
            for (Sucursal sucursalCollectionOldSucursal : sucursalCollectionOld) {
                if (!sucursalCollectionNew.contains(sucursalCollectionOldSucursal)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Sucursal " + sucursalCollectionOldSucursal + " since its agenciaNombreAgencia field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Sucursal> attachedSucursalCollectionNew = new ArrayList<Sucursal>();
            for (Sucursal sucursalCollectionNewSucursalToAttach : sucursalCollectionNew) {
                sucursalCollectionNewSucursalToAttach = em.getReference(sucursalCollectionNewSucursalToAttach.getClass(), sucursalCollectionNewSucursalToAttach.getNumeroSucursal());
                attachedSucursalCollectionNew.add(sucursalCollectionNewSucursalToAttach);
            }
            sucursalCollectionNew = attachedSucursalCollectionNew;
            agencia.setSucursalCollection(sucursalCollectionNew);
            agencia = em.merge(agencia);
            for (Sucursal sucursalCollectionNewSucursal : sucursalCollectionNew) {
                if (!sucursalCollectionOld.contains(sucursalCollectionNewSucursal)) {
                    Agencia oldAgenciaNombreAgenciaOfSucursalCollectionNewSucursal = sucursalCollectionNewSucursal.getAgenciaNombreAgencia();
                    sucursalCollectionNewSucursal.setAgenciaNombreAgencia(agencia);
                    sucursalCollectionNewSucursal = em.merge(sucursalCollectionNewSucursal);
                    if (oldAgenciaNombreAgenciaOfSucursalCollectionNewSucursal != null && !oldAgenciaNombreAgenciaOfSucursalCollectionNewSucursal.equals(agencia)) {
                        oldAgenciaNombreAgenciaOfSucursalCollectionNewSucursal.getSucursalCollection().remove(sucursalCollectionNewSucursal);
                        oldAgenciaNombreAgenciaOfSucursalCollectionNewSucursal = em.merge(oldAgenciaNombreAgenciaOfSucursalCollectionNewSucursal);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = agencia.getNombreAgencia();
                if (findAgencia(id) == null) {
                    throw new NonexistentEntityException("The agencia with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Agencia agencia;
            try {
                agencia = em.getReference(Agencia.class, id);
                agencia.getNombreAgencia();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The agencia with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Sucursal> sucursalCollectionOrphanCheck = agencia.getSucursalCollection();
            for (Sucursal sucursalCollectionOrphanCheckSucursal : sucursalCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Agencia (" + agencia + ") cannot be destroyed since the Sucursal " + sucursalCollectionOrphanCheckSucursal + " in its sucursalCollection field has a non-nullable agenciaNombreAgencia field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(agencia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Agencia> findAgenciaEntities() {
        return findAgenciaEntities(true, -1, -1);
    }

    public List<Agencia> findAgenciaEntities(int maxResults, int firstResult) {
        return findAgenciaEntities(false, maxResults, firstResult);
    }

    private List<Agencia> findAgenciaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Agencia.class));
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

    public Agencia findAgencia(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Agencia.class, id);
        } finally {
            em.close();
        }
    }

    public int getAgenciaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Agencia> rt = cq.from(Agencia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
