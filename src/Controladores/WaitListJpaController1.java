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
import Entidades.Cliente;
import Entidades.WaitList;
import Entidades.WaitListPK;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author COMERCIAL02
 */
public class WaitListJpaController1 implements Serializable {

    public WaitListJpaController1(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(WaitList waitList) throws IllegalOrphanException, PreexistingEntityException, Exception {
        if (waitList.getWaitListPK() == null) {
            waitList.setWaitListPK(new WaitListPK());
        }
        waitList.getWaitListPK().setClienteIdCliente(waitList.getCliente().getUsuario());
        List<String> illegalOrphanMessages = null;
        Cliente clienteOrphanCheck = waitList.getCliente();
        if (clienteOrphanCheck != null) {
            WaitList oldWaitListOfCliente = clienteOrphanCheck.getWaitList();
            if (oldWaitListOfCliente != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Cliente " + clienteOrphanCheck + " already has an item of type WaitList whose cliente column cannot be null. Please make another selection for the cliente field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente cliente = waitList.getCliente();
            if (cliente != null) {
                cliente = em.getReference(cliente.getClass(), cliente.getUsuario());
                waitList.setCliente(cliente);
            }
            em.persist(waitList);
            if (cliente != null) {
                cliente.setWaitList(waitList);
                cliente = em.merge(cliente);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findWaitList(waitList.getWaitListPK()) != null) {
                throw new PreexistingEntityException("WaitList " + waitList + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(WaitList waitList) throws IllegalOrphanException, NonexistentEntityException, Exception {
        waitList.getWaitListPK().setClienteIdCliente(waitList.getCliente().getUsuario());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            WaitList persistentWaitList = em.find(WaitList.class, waitList.getWaitListPK());
            Cliente clienteOld = persistentWaitList.getCliente();
            Cliente clienteNew = waitList.getCliente();
            List<String> illegalOrphanMessages = null;
            if (clienteNew != null && !clienteNew.equals(clienteOld)) {
                WaitList oldWaitListOfCliente = clienteNew.getWaitList();
                if (oldWaitListOfCliente != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Cliente " + clienteNew + " already has an item of type WaitList whose cliente column cannot be null. Please make another selection for the cliente field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (clienteNew != null) {
                clienteNew = em.getReference(clienteNew.getClass(), clienteNew.getUsuario());
                waitList.setCliente(clienteNew);
            }
            waitList = em.merge(waitList);
            if (clienteOld != null && !clienteOld.equals(clienteNew)) {
                clienteOld.setWaitList(null);
                clienteOld = em.merge(clienteOld);
            }
            if (clienteNew != null && !clienteNew.equals(clienteOld)) {
                clienteNew.setWaitList(waitList);
                clienteNew = em.merge(clienteNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                WaitListPK id = waitList.getWaitListPK();
                if (findWaitList(id) == null) {
                    throw new NonexistentEntityException("The waitList with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(WaitListPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            WaitList waitList;
            try {
                waitList = em.getReference(WaitList.class, id);
                waitList.getWaitListPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The waitList with id " + id + " no longer exists.", enfe);
            }
            Cliente cliente = waitList.getCliente();
            if (cliente != null) {
                cliente.setWaitList(null);
                cliente = em.merge(cliente);
            }
            em.remove(waitList);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<WaitList> findWaitListEntities() {
        return findWaitListEntities(true, -1, -1);
    }

    public List<WaitList> findWaitListEntities(int maxResults, int firstResult) {
        return findWaitListEntities(false, maxResults, firstResult);
    }

    private List<WaitList> findWaitListEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(WaitList.class));
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

    public WaitList findWaitList(WaitListPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(WaitList.class, id);
        } finally {
            em.close();
        }
    }

    public int getWaitListCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<WaitList> rt = cq.from(WaitList.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
