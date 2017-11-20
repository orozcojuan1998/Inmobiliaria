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
import Entidades.Cuenta;
import Entidades.Propiedad;
import Entidades.Propietario;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Asus-pc
 */
public class PropietarioJpaController implements Serializable {

    public PropietarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Propietario propietario) throws IllegalOrphanException, PreexistingEntityException, Exception {
        if (propietario.getPropiedadCollection() == null) {
            propietario.setPropiedadCollection(new ArrayList<Propiedad>());
        }
        List<String> illegalOrphanMessages = null;
        Cuenta cuentaOrphanCheck = propietario.getCuenta();
        if (cuentaOrphanCheck != null) {
            Propietario oldPropietarioOfCuenta = cuentaOrphanCheck.getPropietario();
            if (oldPropietarioOfCuenta != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Cuenta " + cuentaOrphanCheck + " already has an item of type Propietario whose cuenta column cannot be null. Please make another selection for the cuenta field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cuenta cuenta = propietario.getCuenta();
            if (cuenta != null) {
                cuenta = em.getReference(cuenta.getClass(), cuenta.getUsuario());
                propietario.setCuenta(cuenta);
            }
            Collection<Propiedad> attachedPropiedadCollection = new ArrayList<Propiedad>();
            for (Propiedad propiedadCollectionPropiedadToAttach : propietario.getPropiedadCollection()) {
                propiedadCollectionPropiedadToAttach = em.getReference(propiedadCollectionPropiedadToAttach.getClass(), propiedadCollectionPropiedadToAttach.getPropiedadPK());
                attachedPropiedadCollection.add(propiedadCollectionPropiedadToAttach);
            }
            propietario.setPropiedadCollection(attachedPropiedadCollection);
            em.persist(propietario);
            if (cuenta != null) {
                cuenta.setPropietario(propietario);
                cuenta = em.merge(cuenta);
            }
            for (Propiedad propiedadCollectionPropiedad : propietario.getPropiedadCollection()) {
                Propietario oldPropietarioOfPropiedadCollectionPropiedad = propiedadCollectionPropiedad.getPropietario();
                propiedadCollectionPropiedad.setPropietario(propietario);
                propiedadCollectionPropiedad = em.merge(propiedadCollectionPropiedad);
                if (oldPropietarioOfPropiedadCollectionPropiedad != null) {
                    oldPropietarioOfPropiedadCollectionPropiedad.getPropiedadCollection().remove(propiedadCollectionPropiedad);
                    oldPropietarioOfPropiedadCollectionPropiedad = em.merge(oldPropietarioOfPropiedadCollectionPropiedad);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPropietario(propietario.getUsuario()) != null) {
                throw new PreexistingEntityException("Propietario " + propietario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Propietario propietario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Propietario persistentPropietario = em.find(Propietario.class, propietario.getUsuario());
            Cuenta cuentaOld = persistentPropietario.getCuenta();
            Cuenta cuentaNew = propietario.getCuenta();
            Collection<Propiedad> propiedadCollectionOld = persistentPropietario.getPropiedadCollection();
            Collection<Propiedad> propiedadCollectionNew = propietario.getPropiedadCollection();
            List<String> illegalOrphanMessages = null;
            if (cuentaNew != null && !cuentaNew.equals(cuentaOld)) {
                Propietario oldPropietarioOfCuenta = cuentaNew.getPropietario();
                if (oldPropietarioOfCuenta != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Cuenta " + cuentaNew + " already has an item of type Propietario whose cuenta column cannot be null. Please make another selection for the cuenta field.");
                }
            }
            for (Propiedad propiedadCollectionOldPropiedad : propiedadCollectionOld) {
                if (!propiedadCollectionNew.contains(propiedadCollectionOldPropiedad)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Propiedad " + propiedadCollectionOldPropiedad + " since its propietario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (cuentaNew != null) {
                cuentaNew = em.getReference(cuentaNew.getClass(), cuentaNew.getUsuario());
                propietario.setCuenta(cuentaNew);
            }
            Collection<Propiedad> attachedPropiedadCollectionNew = new ArrayList<Propiedad>();
            for (Propiedad propiedadCollectionNewPropiedadToAttach : propiedadCollectionNew) {
                propiedadCollectionNewPropiedadToAttach = em.getReference(propiedadCollectionNewPropiedadToAttach.getClass(), propiedadCollectionNewPropiedadToAttach.getPropiedadPK());
                attachedPropiedadCollectionNew.add(propiedadCollectionNewPropiedadToAttach);
            }
            propiedadCollectionNew = attachedPropiedadCollectionNew;
            propietario.setPropiedadCollection(propiedadCollectionNew);
            propietario = em.merge(propietario);
            if (cuentaOld != null && !cuentaOld.equals(cuentaNew)) {
                cuentaOld.setPropietario(null);
                cuentaOld = em.merge(cuentaOld);
            }
            if (cuentaNew != null && !cuentaNew.equals(cuentaOld)) {
                cuentaNew.setPropietario(propietario);
                cuentaNew = em.merge(cuentaNew);
            }
            for (Propiedad propiedadCollectionNewPropiedad : propiedadCollectionNew) {
                if (!propiedadCollectionOld.contains(propiedadCollectionNewPropiedad)) {
                    Propietario oldPropietarioOfPropiedadCollectionNewPropiedad = propiedadCollectionNewPropiedad.getPropietario();
                    propiedadCollectionNewPropiedad.setPropietario(propietario);
                    propiedadCollectionNewPropiedad = em.merge(propiedadCollectionNewPropiedad);
                    if (oldPropietarioOfPropiedadCollectionNewPropiedad != null && !oldPropietarioOfPropiedadCollectionNewPropiedad.equals(propietario)) {
                        oldPropietarioOfPropiedadCollectionNewPropiedad.getPropiedadCollection().remove(propiedadCollectionNewPropiedad);
                        oldPropietarioOfPropiedadCollectionNewPropiedad = em.merge(oldPropietarioOfPropiedadCollectionNewPropiedad);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = propietario.getUsuario();
                if (findPropietario(id) == null) {
                    throw new NonexistentEntityException("The propietario with id " + id + " no longer exists.");
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
            Propietario propietario;
            try {
                propietario = em.getReference(Propietario.class, id);
                propietario.getUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The propietario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Propiedad> propiedadCollectionOrphanCheck = propietario.getPropiedadCollection();
            for (Propiedad propiedadCollectionOrphanCheckPropiedad : propiedadCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Propietario (" + propietario + ") cannot be destroyed since the Propiedad " + propiedadCollectionOrphanCheckPropiedad + " in its propiedadCollection field has a non-nullable propietario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Cuenta cuenta = propietario.getCuenta();
            if (cuenta != null) {
                cuenta.setPropietario(null);
                cuenta = em.merge(cuenta);
            }
            em.remove(propietario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Propietario> findPropietarioEntities() {
        return findPropietarioEntities(true, -1, -1);
    }

    public List<Propietario> findPropietarioEntities(int maxResults, int firstResult) {
        return findPropietarioEntities(false, maxResults, firstResult);
    }

    private List<Propietario> findPropietarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Propietario.class));
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

    public Propietario findPropietario(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Propietario.class, id);
        } finally {
            em.close();
        }
    }

    public int getPropietarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Propietario> rt = cq.from(Propietario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
