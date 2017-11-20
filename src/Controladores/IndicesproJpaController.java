/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import Entidades.Indicespro;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author COMERCIAL02
 */
public class IndicesproJpaController implements Serializable {

    public IndicesproJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Indicespro indicespro) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(indicespro);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findIndicespro(indicespro.getCodigo()) != null) {
                throw new PreexistingEntityException("Indicespro " + indicespro + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Indicespro indicespro) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            indicespro = em.merge(indicespro);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = indicespro.getCodigo();
                if (findIndicespro(id) == null) {
                    throw new NonexistentEntityException("The indicespro with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Indicespro indicespro;
            try {
                indicespro = em.getReference(Indicespro.class, id);
                indicespro.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The indicespro with id " + id + " no longer exists.", enfe);
            }
            em.remove(indicespro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Indicespro> findIndicesproEntities() {
        return findIndicesproEntities(true, -1, -1);
    }

    public List<Indicespro> findIndicesproEntities(int maxResults, int firstResult) {
        return findIndicesproEntities(false, maxResults, firstResult);
    }

    private List<Indicespro> findIndicesproEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Indicespro.class));
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

    public Indicespro findIndicespro(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Indicespro.class, id);
        } finally {
            em.close();
        }
    }

    public int getIndicesproCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Indicespro> rt = cq.from(Indicespro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
