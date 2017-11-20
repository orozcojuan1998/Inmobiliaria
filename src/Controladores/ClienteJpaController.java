/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.IllegalOrphanException;
import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import Entidades.Cliente;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.WaitList;
import Entidades.Cuenta;
import Entidades.Propiedad;
import java.util.ArrayList;
import java.util.Collection;
import Entidades.Tarjeta;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Asus-pc
 */
public class ClienteJpaController implements Serializable {

    public ClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cliente cliente) throws IllegalOrphanException, PreexistingEntityException, Exception {
        if (cliente.getPropiedadCollection() == null) {
            cliente.setPropiedadCollection(new ArrayList<Propiedad>());
        }
        if (cliente.getTarjetaCollection() == null) {
            cliente.setTarjetaCollection(new ArrayList<Tarjeta>());
        }
        List<String> illegalOrphanMessages = null;
        Cuenta cuentaOrphanCheck = cliente.getCuenta();
        if (cuentaOrphanCheck != null) {
            Cliente oldClienteOfCuenta = cuentaOrphanCheck.getCliente();
            if (oldClienteOfCuenta != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Cuenta " + cuentaOrphanCheck + " already has an item of type Cliente whose cuenta column cannot be null. Please make another selection for the cuenta field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            WaitList waitList = cliente.getWaitList();
            if (waitList != null) {
                waitList = em.getReference(waitList.getClass(), waitList.getWaitListPK());
                cliente.setWaitList(waitList);
            }
            Cuenta cuenta = cliente.getCuenta();
            if (cuenta != null) {
                cuenta = em.getReference(cuenta.getClass(), cuenta.getUsuario());
                cliente.setCuenta(cuenta);
            }
            Collection<Propiedad> attachedPropiedadCollection = new ArrayList<Propiedad>();
            for (Propiedad propiedadCollectionPropiedadToAttach : cliente.getPropiedadCollection()) {
                propiedadCollectionPropiedadToAttach = em.getReference(propiedadCollectionPropiedadToAttach.getClass(), propiedadCollectionPropiedadToAttach.getPropiedadPK());
                attachedPropiedadCollection.add(propiedadCollectionPropiedadToAttach);
            }
            cliente.setPropiedadCollection(attachedPropiedadCollection);
            Collection<Tarjeta> attachedTarjetaCollection = new ArrayList<Tarjeta>();
            for (Tarjeta tarjetaCollectionTarjetaToAttach : cliente.getTarjetaCollection()) {
                tarjetaCollectionTarjetaToAttach = em.getReference(tarjetaCollectionTarjetaToAttach.getClass(), tarjetaCollectionTarjetaToAttach.getTarjetaPK());
                attachedTarjetaCollection.add(tarjetaCollectionTarjetaToAttach);
            }
            cliente.setTarjetaCollection(attachedTarjetaCollection);
            em.persist(cliente);
            if (waitList != null) {
                Cliente oldClienteOfWaitList = waitList.getCliente();
                if (oldClienteOfWaitList != null) {
                    oldClienteOfWaitList.setWaitList(null);
                    oldClienteOfWaitList = em.merge(oldClienteOfWaitList);
                }
                waitList.setCliente(cliente);
                waitList = em.merge(waitList);
            }
            if (cuenta != null) {
                cuenta.setCliente(cliente);
                cuenta = em.merge(cuenta);
            }
            for (Propiedad propiedadCollectionPropiedad : cliente.getPropiedadCollection()) {
                propiedadCollectionPropiedad.getClienteCollection().add(cliente);
                propiedadCollectionPropiedad = em.merge(propiedadCollectionPropiedad);
            }
            for (Tarjeta tarjetaCollectionTarjeta : cliente.getTarjetaCollection()) {
                Cliente oldClienteOfTarjetaCollectionTarjeta = tarjetaCollectionTarjeta.getCliente();
                tarjetaCollectionTarjeta.setCliente(cliente);
                tarjetaCollectionTarjeta = em.merge(tarjetaCollectionTarjeta);
                if (oldClienteOfTarjetaCollectionTarjeta != null) {
                    oldClienteOfTarjetaCollectionTarjeta.getTarjetaCollection().remove(tarjetaCollectionTarjeta);
                    oldClienteOfTarjetaCollectionTarjeta = em.merge(oldClienteOfTarjetaCollectionTarjeta);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCliente(cliente.getUsuario()) != null) {
                throw new PreexistingEntityException("Cliente " + cliente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cliente cliente) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getUsuario());
            WaitList waitListOld = persistentCliente.getWaitList();
            WaitList waitListNew = cliente.getWaitList();
            Cuenta cuentaOld = persistentCliente.getCuenta();
            Cuenta cuentaNew = cliente.getCuenta();
            Collection<Propiedad> propiedadCollectionOld = persistentCliente.getPropiedadCollection();
            Collection<Propiedad> propiedadCollectionNew = cliente.getPropiedadCollection();
            Collection<Tarjeta> tarjetaCollectionOld = persistentCliente.getTarjetaCollection();
            Collection<Tarjeta> tarjetaCollectionNew = cliente.getTarjetaCollection();
            List<String> illegalOrphanMessages = null;
            if (waitListOld != null && !waitListOld.equals(waitListNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain WaitList " + waitListOld + " since its cliente field is not nullable.");
            }
            if (cuentaNew != null && !cuentaNew.equals(cuentaOld)) {
                Cliente oldClienteOfCuenta = cuentaNew.getCliente();
                if (oldClienteOfCuenta != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Cuenta " + cuentaNew + " already has an item of type Cliente whose cuenta column cannot be null. Please make another selection for the cuenta field.");
                }
            }
            for (Tarjeta tarjetaCollectionOldTarjeta : tarjetaCollectionOld) {
                if (!tarjetaCollectionNew.contains(tarjetaCollectionOldTarjeta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tarjeta " + tarjetaCollectionOldTarjeta + " since its cliente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (waitListNew != null) {
                waitListNew = em.getReference(waitListNew.getClass(), waitListNew.getWaitListPK());
                cliente.setWaitList(waitListNew);
            }
            if (cuentaNew != null) {
                cuentaNew = em.getReference(cuentaNew.getClass(), cuentaNew.getUsuario());
                cliente.setCuenta(cuentaNew);
            }
            Collection<Propiedad> attachedPropiedadCollectionNew = new ArrayList<Propiedad>();
            for (Propiedad propiedadCollectionNewPropiedadToAttach : propiedadCollectionNew) {
                propiedadCollectionNewPropiedadToAttach = em.getReference(propiedadCollectionNewPropiedadToAttach.getClass(), propiedadCollectionNewPropiedadToAttach.getPropiedadPK());
                attachedPropiedadCollectionNew.add(propiedadCollectionNewPropiedadToAttach);
            }
            propiedadCollectionNew = attachedPropiedadCollectionNew;
            cliente.setPropiedadCollection(propiedadCollectionNew);
            Collection<Tarjeta> attachedTarjetaCollectionNew = new ArrayList<Tarjeta>();
            for (Tarjeta tarjetaCollectionNewTarjetaToAttach : tarjetaCollectionNew) {
                tarjetaCollectionNewTarjetaToAttach = em.getReference(tarjetaCollectionNewTarjetaToAttach.getClass(), tarjetaCollectionNewTarjetaToAttach.getTarjetaPK());
                attachedTarjetaCollectionNew.add(tarjetaCollectionNewTarjetaToAttach);
            }
            tarjetaCollectionNew = attachedTarjetaCollectionNew;
            cliente.setTarjetaCollection(tarjetaCollectionNew);
            cliente = em.merge(cliente);
            if (waitListNew != null && !waitListNew.equals(waitListOld)) {
                Cliente oldClienteOfWaitList = waitListNew.getCliente();
                if (oldClienteOfWaitList != null) {
                    oldClienteOfWaitList.setWaitList(null);
                    oldClienteOfWaitList = em.merge(oldClienteOfWaitList);
                }
                waitListNew.setCliente(cliente);
                waitListNew = em.merge(waitListNew);
            }
            if (cuentaOld != null && !cuentaOld.equals(cuentaNew)) {
                cuentaOld.setCliente(null);
                cuentaOld = em.merge(cuentaOld);
            }
            if (cuentaNew != null && !cuentaNew.equals(cuentaOld)) {
                cuentaNew.setCliente(cliente);
                cuentaNew = em.merge(cuentaNew);
            }
            for (Propiedad propiedadCollectionOldPropiedad : propiedadCollectionOld) {
                if (!propiedadCollectionNew.contains(propiedadCollectionOldPropiedad)) {
                    propiedadCollectionOldPropiedad.getClienteCollection().remove(cliente);
                    propiedadCollectionOldPropiedad = em.merge(propiedadCollectionOldPropiedad);
                }
            }
            for (Propiedad propiedadCollectionNewPropiedad : propiedadCollectionNew) {
                if (!propiedadCollectionOld.contains(propiedadCollectionNewPropiedad)) {
                    propiedadCollectionNewPropiedad.getClienteCollection().add(cliente);
                    propiedadCollectionNewPropiedad = em.merge(propiedadCollectionNewPropiedad);
                }
            }
            for (Tarjeta tarjetaCollectionNewTarjeta : tarjetaCollectionNew) {
                if (!tarjetaCollectionOld.contains(tarjetaCollectionNewTarjeta)) {
                    Cliente oldClienteOfTarjetaCollectionNewTarjeta = tarjetaCollectionNewTarjeta.getCliente();
                    tarjetaCollectionNewTarjeta.setCliente(cliente);
                    tarjetaCollectionNewTarjeta = em.merge(tarjetaCollectionNewTarjeta);
                    if (oldClienteOfTarjetaCollectionNewTarjeta != null && !oldClienteOfTarjetaCollectionNewTarjeta.equals(cliente)) {
                        oldClienteOfTarjetaCollectionNewTarjeta.getTarjetaCollection().remove(tarjetaCollectionNewTarjeta);
                        oldClienteOfTarjetaCollectionNewTarjeta = em.merge(oldClienteOfTarjetaCollectionNewTarjeta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = cliente.getUsuario();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
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
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            WaitList waitListOrphanCheck = cliente.getWaitList();
            if (waitListOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cliente (" + cliente + ") cannot be destroyed since the WaitList " + waitListOrphanCheck + " in its waitList field has a non-nullable cliente field.");
            }
            Collection<Tarjeta> tarjetaCollectionOrphanCheck = cliente.getTarjetaCollection();
            for (Tarjeta tarjetaCollectionOrphanCheckTarjeta : tarjetaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cliente (" + cliente + ") cannot be destroyed since the Tarjeta " + tarjetaCollectionOrphanCheckTarjeta + " in its tarjetaCollection field has a non-nullable cliente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Cuenta cuenta = cliente.getCuenta();
            if (cuenta != null) {
                cuenta.setCliente(null);
                cuenta = em.merge(cuenta);
            }
            Collection<Propiedad> propiedadCollection = cliente.getPropiedadCollection();
            for (Propiedad propiedadCollectionPropiedad : propiedadCollection) {
                propiedadCollectionPropiedad.getClienteCollection().remove(cliente);
                propiedadCollectionPropiedad = em.merge(propiedadCollectionPropiedad);
            }
            em.remove(cliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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

    public Cliente findCliente(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
