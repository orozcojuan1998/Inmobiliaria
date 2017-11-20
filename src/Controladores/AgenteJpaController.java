/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.IllegalOrphanException;
import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import Entidades.Agente;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Cuenta;
import Entidades.Sucursal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Asus-pc
 */
public class AgenteJpaController implements Serializable {

    public AgenteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Agente agente) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Cuenta cuentaOrphanCheck = agente.getCuenta();
        if (cuentaOrphanCheck != null) {
            Agente oldAgenteOfCuenta = cuentaOrphanCheck.getAgente();
            if (oldAgenteOfCuenta != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Cuenta " + cuentaOrphanCheck + " already has an item of type Agente whose cuenta column cannot be null. Please make another selection for the cuenta field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cuenta cuenta = agente.getCuenta();
            if (cuenta != null) {
                cuenta = em.getReference(cuenta.getClass(), cuenta.getUsuario());
                agente.setCuenta(cuenta);
            }
            Sucursal sucursalNumeroSucursal = agente.getSucursalNumeroSucursal();
            if (sucursalNumeroSucursal != null) {
                sucursalNumeroSucursal = em.getReference(sucursalNumeroSucursal.getClass(), sucursalNumeroSucursal.getNumeroSucursal());
                agente.setSucursalNumeroSucursal(sucursalNumeroSucursal);
            }
            em.persist(agente);
            if (cuenta != null) {
                cuenta.setAgente(agente);
                cuenta = em.merge(cuenta);
            }
            if (sucursalNumeroSucursal != null) {
                sucursalNumeroSucursal.getAgenteCollection().add(agente);
                sucursalNumeroSucursal = em.merge(sucursalNumeroSucursal);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAgente(agente.getUsuario()) != null) {
                throw new PreexistingEntityException("Agente " + agente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Agente agente) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Agente persistentAgente = em.find(Agente.class, agente.getUsuario());
            Cuenta cuentaOld = persistentAgente.getCuenta();
            Cuenta cuentaNew = agente.getCuenta();
            Sucursal sucursalNumeroSucursalOld = persistentAgente.getSucursalNumeroSucursal();
            Sucursal sucursalNumeroSucursalNew = agente.getSucursalNumeroSucursal();
            List<String> illegalOrphanMessages = null;
            if (cuentaNew != null && !cuentaNew.equals(cuentaOld)) {
                Agente oldAgenteOfCuenta = cuentaNew.getAgente();
                if (oldAgenteOfCuenta != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Cuenta " + cuentaNew + " already has an item of type Agente whose cuenta column cannot be null. Please make another selection for the cuenta field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (cuentaNew != null) {
                cuentaNew = em.getReference(cuentaNew.getClass(), cuentaNew.getUsuario());
                agente.setCuenta(cuentaNew);
            }
            if (sucursalNumeroSucursalNew != null) {
                sucursalNumeroSucursalNew = em.getReference(sucursalNumeroSucursalNew.getClass(), sucursalNumeroSucursalNew.getNumeroSucursal());
                agente.setSucursalNumeroSucursal(sucursalNumeroSucursalNew);
            }
            agente = em.merge(agente);
            if (cuentaOld != null && !cuentaOld.equals(cuentaNew)) {
                cuentaOld.setAgente(null);
                cuentaOld = em.merge(cuentaOld);
            }
            if (cuentaNew != null && !cuentaNew.equals(cuentaOld)) {
                cuentaNew.setAgente(agente);
                cuentaNew = em.merge(cuentaNew);
            }
            if (sucursalNumeroSucursalOld != null && !sucursalNumeroSucursalOld.equals(sucursalNumeroSucursalNew)) {
                sucursalNumeroSucursalOld.getAgenteCollection().remove(agente);
                sucursalNumeroSucursalOld = em.merge(sucursalNumeroSucursalOld);
            }
            if (sucursalNumeroSucursalNew != null && !sucursalNumeroSucursalNew.equals(sucursalNumeroSucursalOld)) {
                sucursalNumeroSucursalNew.getAgenteCollection().add(agente);
                sucursalNumeroSucursalNew = em.merge(sucursalNumeroSucursalNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = agente.getUsuario();
                if (findAgente(id) == null) {
                    throw new NonexistentEntityException("The agente with id " + id + " no longer exists.");
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
            Agente agente;
            try {
                agente = em.getReference(Agente.class, id);
                agente.getUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The agente with id " + id + " no longer exists.", enfe);
            }
            Cuenta cuenta = agente.getCuenta();
            if (cuenta != null) {
                cuenta.setAgente(null);
                cuenta = em.merge(cuenta);
            }
            Sucursal sucursalNumeroSucursal = agente.getSucursalNumeroSucursal();
            if (sucursalNumeroSucursal != null) {
                sucursalNumeroSucursal.getAgenteCollection().remove(agente);
                sucursalNumeroSucursal = em.merge(sucursalNumeroSucursal);
            }
            em.remove(agente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Agente> findAgenteEntities() {
        return findAgenteEntities(true, -1, -1);
    }

    public List<Agente> findAgenteEntities(int maxResults, int firstResult) {
        return findAgenteEntities(false, maxResults, firstResult);
    }

    private List<Agente> findAgenteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Agente.class));
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

    public Agente findAgente(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Agente.class, id);
        } finally {
            em.close();
        }
    }

    public int getAgenteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Agente> rt = cq.from(Agente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
