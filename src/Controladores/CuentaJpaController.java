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
import Entidades.Propietario;
import Entidades.Agente;
import Entidades.Cuenta;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Asus-pc
 */
public class CuentaJpaController implements Serializable {

    public CuentaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cuenta cuenta) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente cliente = cuenta.getCliente();
            if (cliente != null) {
                cliente = em.getReference(cliente.getClass(), cliente.getUsuario());
                cuenta.setCliente(cliente);
            }
            Propietario propietario = cuenta.getPropietario();
            if (propietario != null) {
                propietario = em.getReference(propietario.getClass(), propietario.getUsuario());
                cuenta.setPropietario(propietario);
            }
            Agente agente = cuenta.getAgente();
            if (agente != null) {
                agente = em.getReference(agente.getClass(), agente.getUsuario());
                cuenta.setAgente(agente);
            }
            em.persist(cuenta);
            if (cliente != null) {
                Cuenta oldCuentaOfCliente = cliente.getCuenta();
                if (oldCuentaOfCliente != null) {
                    oldCuentaOfCliente.setCliente(null);
                    oldCuentaOfCliente = em.merge(oldCuentaOfCliente);
                }
                cliente.setCuenta(cuenta);
                cliente = em.merge(cliente);
            }
            if (propietario != null) {
                Cuenta oldCuentaOfPropietario = propietario.getCuenta();
                if (oldCuentaOfPropietario != null) {
                    oldCuentaOfPropietario.setPropietario(null);
                    oldCuentaOfPropietario = em.merge(oldCuentaOfPropietario);
                }
                propietario.setCuenta(cuenta);
                propietario = em.merge(propietario);
            }
            if (agente != null) {
                Cuenta oldCuentaOfAgente = agente.getCuenta();
                if (oldCuentaOfAgente != null) {
                    oldCuentaOfAgente.setAgente(null);
                    oldCuentaOfAgente = em.merge(oldCuentaOfAgente);
                }
                agente.setCuenta(cuenta);
                agente = em.merge(agente);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCuenta(cuenta.getUsuario()) != null) {
                throw new PreexistingEntityException("Cuenta " + cuenta + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cuenta cuenta) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cuenta persistentCuenta = em.find(Cuenta.class, cuenta.getUsuario());
            Cliente clienteOld = persistentCuenta.getCliente();
            Cliente clienteNew = cuenta.getCliente();
            Propietario propietarioOld = persistentCuenta.getPropietario();
            Propietario propietarioNew = cuenta.getPropietario();
            Agente agenteOld = persistentCuenta.getAgente();
            Agente agenteNew = cuenta.getAgente();
            List<String> illegalOrphanMessages = null;
            if (clienteOld != null && !clienteOld.equals(clienteNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Cliente " + clienteOld + " since its cuenta field is not nullable.");
            }
            if (propietarioOld != null && !propietarioOld.equals(propietarioNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Propietario " + propietarioOld + " since its cuenta field is not nullable.");
            }
            if (agenteOld != null && !agenteOld.equals(agenteNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Agente " + agenteOld + " since its cuenta field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (clienteNew != null) {
                clienteNew = em.getReference(clienteNew.getClass(), clienteNew.getUsuario());
                cuenta.setCliente(clienteNew);
            }
            if (propietarioNew != null) {
                propietarioNew = em.getReference(propietarioNew.getClass(), propietarioNew.getUsuario());
                cuenta.setPropietario(propietarioNew);
            }
            if (agenteNew != null) {
                agenteNew = em.getReference(agenteNew.getClass(), agenteNew.getUsuario());
                cuenta.setAgente(agenteNew);
            }
            cuenta = em.merge(cuenta);
            if (clienteNew != null && !clienteNew.equals(clienteOld)) {
                Cuenta oldCuentaOfCliente = clienteNew.getCuenta();
                if (oldCuentaOfCliente != null) {
                    oldCuentaOfCliente.setCliente(null);
                    oldCuentaOfCliente = em.merge(oldCuentaOfCliente);
                }
                clienteNew.setCuenta(cuenta);
                clienteNew = em.merge(clienteNew);
            }
            if (propietarioNew != null && !propietarioNew.equals(propietarioOld)) {
                Cuenta oldCuentaOfPropietario = propietarioNew.getCuenta();
                if (oldCuentaOfPropietario != null) {
                    oldCuentaOfPropietario.setPropietario(null);
                    oldCuentaOfPropietario = em.merge(oldCuentaOfPropietario);
                }
                propietarioNew.setCuenta(cuenta);
                propietarioNew = em.merge(propietarioNew);
            }
            if (agenteNew != null && !agenteNew.equals(agenteOld)) {
                Cuenta oldCuentaOfAgente = agenteNew.getCuenta();
                if (oldCuentaOfAgente != null) {
                    oldCuentaOfAgente.setAgente(null);
                    oldCuentaOfAgente = em.merge(oldCuentaOfAgente);
                }
                agenteNew.setCuenta(cuenta);
                agenteNew = em.merge(agenteNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = cuenta.getUsuario();
                if (findCuenta(id) == null) {
                    throw new NonexistentEntityException("The cuenta with id " + id + " no longer exists.");
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
            Cuenta cuenta;
            try {
                cuenta = em.getReference(Cuenta.class, id);
                cuenta.getUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cuenta with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Cliente clienteOrphanCheck = cuenta.getCliente();
            if (clienteOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cuenta (" + cuenta + ") cannot be destroyed since the Cliente " + clienteOrphanCheck + " in its cliente field has a non-nullable cuenta field.");
            }
            Propietario propietarioOrphanCheck = cuenta.getPropietario();
            if (propietarioOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cuenta (" + cuenta + ") cannot be destroyed since the Propietario " + propietarioOrphanCheck + " in its propietario field has a non-nullable cuenta field.");
            }
            Agente agenteOrphanCheck = cuenta.getAgente();
            if (agenteOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cuenta (" + cuenta + ") cannot be destroyed since the Agente " + agenteOrphanCheck + " in its agente field has a non-nullable cuenta field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(cuenta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cuenta> findCuentaEntities() {
        return findCuentaEntities(true, -1, -1);
    }

    public List<Cuenta> findCuentaEntities(int maxResults, int firstResult) {
        return findCuentaEntities(false, maxResults, firstResult);
    }

    private List<Cuenta> findCuentaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cuenta.class));
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

    public Cuenta findCuenta(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cuenta.class, id);
        } finally {
            em.close();
        }
    }

    public int getCuentaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cuenta> rt = cq.from(Cuenta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
