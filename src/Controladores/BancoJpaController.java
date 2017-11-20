/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.IllegalOrphanException;
import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import Entidades.Banco;
import Entidades.BancoPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.RentaSolicitud;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Asus-pc
 */
public class BancoJpaController implements Serializable {

    public BancoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Banco banco) throws PreexistingEntityException, Exception {
        if (banco.getBancoPK() == null) {
            banco.setBancoPK(new BancoPK());
        }
        if (banco.getRentaSolicitudCollection() == null) {
            banco.setRentaSolicitudCollection(new ArrayList<RentaSolicitud>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<RentaSolicitud> attachedRentaSolicitudCollection = new ArrayList<RentaSolicitud>();
            for (RentaSolicitud rentaSolicitudCollectionRentaSolicitudToAttach : banco.getRentaSolicitudCollection()) {
                rentaSolicitudCollectionRentaSolicitudToAttach = em.getReference(rentaSolicitudCollectionRentaSolicitudToAttach.getClass(), rentaSolicitudCollectionRentaSolicitudToAttach.getIdRegistroRenta());
                attachedRentaSolicitudCollection.add(rentaSolicitudCollectionRentaSolicitudToAttach);
            }
            banco.setRentaSolicitudCollection(attachedRentaSolicitudCollection);
            em.persist(banco);
            for (RentaSolicitud rentaSolicitudCollectionRentaSolicitud : banco.getRentaSolicitudCollection()) {
                Banco oldBancoOfRentaSolicitudCollectionRentaSolicitud = rentaSolicitudCollectionRentaSolicitud.getBanco();
                rentaSolicitudCollectionRentaSolicitud.setBanco(banco);
                rentaSolicitudCollectionRentaSolicitud = em.merge(rentaSolicitudCollectionRentaSolicitud);
                if (oldBancoOfRentaSolicitudCollectionRentaSolicitud != null) {
                    oldBancoOfRentaSolicitudCollectionRentaSolicitud.getRentaSolicitudCollection().remove(rentaSolicitudCollectionRentaSolicitud);
                    oldBancoOfRentaSolicitudCollectionRentaSolicitud = em.merge(oldBancoOfRentaSolicitudCollectionRentaSolicitud);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findBanco(banco.getBancoPK()) != null) {
                throw new PreexistingEntityException("Banco " + banco + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Banco banco) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Banco persistentBanco = em.find(Banco.class, banco.getBancoPK());
            Collection<RentaSolicitud> rentaSolicitudCollectionOld = persistentBanco.getRentaSolicitudCollection();
            Collection<RentaSolicitud> rentaSolicitudCollectionNew = banco.getRentaSolicitudCollection();
            List<String> illegalOrphanMessages = null;
            for (RentaSolicitud rentaSolicitudCollectionOldRentaSolicitud : rentaSolicitudCollectionOld) {
                if (!rentaSolicitudCollectionNew.contains(rentaSolicitudCollectionOldRentaSolicitud)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RentaSolicitud " + rentaSolicitudCollectionOldRentaSolicitud + " since its banco field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<RentaSolicitud> attachedRentaSolicitudCollectionNew = new ArrayList<RentaSolicitud>();
            for (RentaSolicitud rentaSolicitudCollectionNewRentaSolicitudToAttach : rentaSolicitudCollectionNew) {
                rentaSolicitudCollectionNewRentaSolicitudToAttach = em.getReference(rentaSolicitudCollectionNewRentaSolicitudToAttach.getClass(), rentaSolicitudCollectionNewRentaSolicitudToAttach.getIdRegistroRenta());
                attachedRentaSolicitudCollectionNew.add(rentaSolicitudCollectionNewRentaSolicitudToAttach);
            }
            rentaSolicitudCollectionNew = attachedRentaSolicitudCollectionNew;
            banco.setRentaSolicitudCollection(rentaSolicitudCollectionNew);
            banco = em.merge(banco);
            for (RentaSolicitud rentaSolicitudCollectionNewRentaSolicitud : rentaSolicitudCollectionNew) {
                if (!rentaSolicitudCollectionOld.contains(rentaSolicitudCollectionNewRentaSolicitud)) {
                    Banco oldBancoOfRentaSolicitudCollectionNewRentaSolicitud = rentaSolicitudCollectionNewRentaSolicitud.getBanco();
                    rentaSolicitudCollectionNewRentaSolicitud.setBanco(banco);
                    rentaSolicitudCollectionNewRentaSolicitud = em.merge(rentaSolicitudCollectionNewRentaSolicitud);
                    if (oldBancoOfRentaSolicitudCollectionNewRentaSolicitud != null && !oldBancoOfRentaSolicitudCollectionNewRentaSolicitud.equals(banco)) {
                        oldBancoOfRentaSolicitudCollectionNewRentaSolicitud.getRentaSolicitudCollection().remove(rentaSolicitudCollectionNewRentaSolicitud);
                        oldBancoOfRentaSolicitudCollectionNewRentaSolicitud = em.merge(oldBancoOfRentaSolicitudCollectionNewRentaSolicitud);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BancoPK id = banco.getBancoPK();
                if (findBanco(id) == null) {
                    throw new NonexistentEntityException("The banco with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BancoPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Banco banco;
            try {
                banco = em.getReference(Banco.class, id);
                banco.getBancoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The banco with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<RentaSolicitud> rentaSolicitudCollectionOrphanCheck = banco.getRentaSolicitudCollection();
            for (RentaSolicitud rentaSolicitudCollectionOrphanCheckRentaSolicitud : rentaSolicitudCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Banco (" + banco + ") cannot be destroyed since the RentaSolicitud " + rentaSolicitudCollectionOrphanCheckRentaSolicitud + " in its rentaSolicitudCollection field has a non-nullable banco field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(banco);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Banco> findBancoEntities() {
        return findBancoEntities(true, -1, -1);
    }

    public List<Banco> findBancoEntities(int maxResults, int firstResult) {
        return findBancoEntities(false, maxResults, firstResult);
    }

    private List<Banco> findBancoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Banco.class));
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

    public Banco findBanco(BancoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Banco.class, id);
        } finally {
            em.close();
        }
    }

    public int getBancoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Banco> rt = cq.from(Banco.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
