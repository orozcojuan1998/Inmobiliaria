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
import Entidades.Propietario;
import Entidades.Cliente;
import java.util.ArrayList;
import java.util.Collection;
import Entidades.Foto;
import Entidades.Propiedad;
import Entidades.PropiedadPK;
import Entidades.Ubicacion;
import Entidades.RentaSolicitud;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Asus-pc
 */
public class PropiedadJpaController implements Serializable {

    public PropiedadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Propiedad propiedad) throws PreexistingEntityException, Exception {
        if (propiedad.getPropiedadPK() == null) {
            propiedad.setPropiedadPK(new PropiedadPK());
        }
        if (propiedad.getClienteCollection() == null) {
            propiedad.setClienteCollection(new ArrayList<Cliente>());
        }
        if (propiedad.getFotoCollection() == null) {
            propiedad.setFotoCollection(new ArrayList<Foto>());
        }
        if (propiedad.getUbicacionCollection() == null) {
            propiedad.setUbicacionCollection(new ArrayList<Ubicacion>());
        }
        if (propiedad.getRentaSolicitudCollection() == null) {
            propiedad.setRentaSolicitudCollection(new ArrayList<RentaSolicitud>());
        }
        propiedad.getPropiedadPK().setUsuario(propiedad.getPropietario().getUsuario());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Propietario propietario = propiedad.getPropietario();
            if (propietario != null) {
                propietario = em.getReference(propietario.getClass(), propietario.getUsuario());
                propiedad.setPropietario(propietario);
            }
            Collection<Cliente> attachedClienteCollection = new ArrayList<Cliente>();
            for (Cliente clienteCollectionClienteToAttach : propiedad.getClienteCollection()) {
                clienteCollectionClienteToAttach = em.getReference(clienteCollectionClienteToAttach.getClass(), clienteCollectionClienteToAttach.getUsuario());
                attachedClienteCollection.add(clienteCollectionClienteToAttach);
            }
            propiedad.setClienteCollection(attachedClienteCollection);
            Collection<Foto> attachedFotoCollection = new ArrayList<Foto>();
            for (Foto fotoCollectionFotoToAttach : propiedad.getFotoCollection()) {
                fotoCollectionFotoToAttach = em.getReference(fotoCollectionFotoToAttach.getClass(), fotoCollectionFotoToAttach.getUrlFoto());
                attachedFotoCollection.add(fotoCollectionFotoToAttach);
            }
            propiedad.setFotoCollection(attachedFotoCollection);
            Collection<Ubicacion> attachedUbicacionCollection = new ArrayList<Ubicacion>();
            for (Ubicacion ubicacionCollectionUbicacionToAttach : propiedad.getUbicacionCollection()) {
                ubicacionCollectionUbicacionToAttach = em.getReference(ubicacionCollectionUbicacionToAttach.getClass(), ubicacionCollectionUbicacionToAttach.getUbicacionId());
                attachedUbicacionCollection.add(ubicacionCollectionUbicacionToAttach);
            }
            propiedad.setUbicacionCollection(attachedUbicacionCollection);
            Collection<RentaSolicitud> attachedRentaSolicitudCollection = new ArrayList<RentaSolicitud>();
            for (RentaSolicitud rentaSolicitudCollectionRentaSolicitudToAttach : propiedad.getRentaSolicitudCollection()) {
                rentaSolicitudCollectionRentaSolicitudToAttach = em.getReference(rentaSolicitudCollectionRentaSolicitudToAttach.getClass(), rentaSolicitudCollectionRentaSolicitudToAttach.getIdRegistroRenta());
                attachedRentaSolicitudCollection.add(rentaSolicitudCollectionRentaSolicitudToAttach);
            }
            propiedad.setRentaSolicitudCollection(attachedRentaSolicitudCollection);
            em.persist(propiedad);
            if (propietario != null) {
                propietario.getPropiedadCollection().add(propiedad);
                propietario = em.merge(propietario);
            }
            for (Cliente clienteCollectionCliente : propiedad.getClienteCollection()) {
                clienteCollectionCliente.getPropiedadCollection().add(propiedad);
                clienteCollectionCliente = em.merge(clienteCollectionCliente);
            }
            for (Foto fotoCollectionFoto : propiedad.getFotoCollection()) {
                Propiedad oldPropiedadOfFotoCollectionFoto = fotoCollectionFoto.getPropiedad();
                fotoCollectionFoto.setPropiedad(propiedad);
                fotoCollectionFoto = em.merge(fotoCollectionFoto);
                if (oldPropiedadOfFotoCollectionFoto != null) {
                    oldPropiedadOfFotoCollectionFoto.getFotoCollection().remove(fotoCollectionFoto);
                    oldPropiedadOfFotoCollectionFoto = em.merge(oldPropiedadOfFotoCollectionFoto);
                }
            }
            for (Ubicacion ubicacionCollectionUbicacion : propiedad.getUbicacionCollection()) {
                Propiedad oldPropiedadOfUbicacionCollectionUbicacion = ubicacionCollectionUbicacion.getPropiedad();
                ubicacionCollectionUbicacion.setPropiedad(propiedad);
                ubicacionCollectionUbicacion = em.merge(ubicacionCollectionUbicacion);
                if (oldPropiedadOfUbicacionCollectionUbicacion != null) {
                    oldPropiedadOfUbicacionCollectionUbicacion.getUbicacionCollection().remove(ubicacionCollectionUbicacion);
                    oldPropiedadOfUbicacionCollectionUbicacion = em.merge(oldPropiedadOfUbicacionCollectionUbicacion);
                }
            }
            for (RentaSolicitud rentaSolicitudCollectionRentaSolicitud : propiedad.getRentaSolicitudCollection()) {
                Propiedad oldPropiedadOfRentaSolicitudCollectionRentaSolicitud = rentaSolicitudCollectionRentaSolicitud.getPropiedad();
                rentaSolicitudCollectionRentaSolicitud.setPropiedad(propiedad);
                rentaSolicitudCollectionRentaSolicitud = em.merge(rentaSolicitudCollectionRentaSolicitud);
                if (oldPropiedadOfRentaSolicitudCollectionRentaSolicitud != null) {
                    oldPropiedadOfRentaSolicitudCollectionRentaSolicitud.getRentaSolicitudCollection().remove(rentaSolicitudCollectionRentaSolicitud);
                    oldPropiedadOfRentaSolicitudCollectionRentaSolicitud = em.merge(oldPropiedadOfRentaSolicitudCollectionRentaSolicitud);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPropiedad(propiedad.getPropiedadPK()) != null) {
                throw new PreexistingEntityException("Propiedad " + propiedad + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Propiedad propiedad) throws IllegalOrphanException, NonexistentEntityException, Exception {
        propiedad.getPropiedadPK().setUsuario(propiedad.getPropietario().getUsuario());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Propiedad persistentPropiedad = em.find(Propiedad.class, propiedad.getPropiedadPK());
            Propietario propietarioOld = persistentPropiedad.getPropietario();
            Propietario propietarioNew = propiedad.getPropietario();
            Collection<Cliente> clienteCollectionOld = persistentPropiedad.getClienteCollection();
            Collection<Cliente> clienteCollectionNew = propiedad.getClienteCollection();
            Collection<Foto> fotoCollectionOld = persistentPropiedad.getFotoCollection();
            Collection<Foto> fotoCollectionNew = propiedad.getFotoCollection();
            Collection<Ubicacion> ubicacionCollectionOld = persistentPropiedad.getUbicacionCollection();
            Collection<Ubicacion> ubicacionCollectionNew = propiedad.getUbicacionCollection();
            Collection<RentaSolicitud> rentaSolicitudCollectionOld = persistentPropiedad.getRentaSolicitudCollection();
            Collection<RentaSolicitud> rentaSolicitudCollectionNew = propiedad.getRentaSolicitudCollection();
            List<String> illegalOrphanMessages = null;
            for (Foto fotoCollectionOldFoto : fotoCollectionOld) {
                if (!fotoCollectionNew.contains(fotoCollectionOldFoto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Foto " + fotoCollectionOldFoto + " since its propiedad field is not nullable.");
                }
            }
            for (Ubicacion ubicacionCollectionOldUbicacion : ubicacionCollectionOld) {
                if (!ubicacionCollectionNew.contains(ubicacionCollectionOldUbicacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ubicacion " + ubicacionCollectionOldUbicacion + " since its propiedad field is not nullable.");
                }
            }
            for (RentaSolicitud rentaSolicitudCollectionOldRentaSolicitud : rentaSolicitudCollectionOld) {
                if (!rentaSolicitudCollectionNew.contains(rentaSolicitudCollectionOldRentaSolicitud)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RentaSolicitud " + rentaSolicitudCollectionOldRentaSolicitud + " since its propiedad field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (propietarioNew != null) {
                propietarioNew = em.getReference(propietarioNew.getClass(), propietarioNew.getUsuario());
                propiedad.setPropietario(propietarioNew);
            }
            Collection<Cliente> attachedClienteCollectionNew = new ArrayList<Cliente>();
            for (Cliente clienteCollectionNewClienteToAttach : clienteCollectionNew) {
                clienteCollectionNewClienteToAttach = em.getReference(clienteCollectionNewClienteToAttach.getClass(), clienteCollectionNewClienteToAttach.getUsuario());
                attachedClienteCollectionNew.add(clienteCollectionNewClienteToAttach);
            }
            clienteCollectionNew = attachedClienteCollectionNew;
            propiedad.setClienteCollection(clienteCollectionNew);
            Collection<Foto> attachedFotoCollectionNew = new ArrayList<Foto>();
            for (Foto fotoCollectionNewFotoToAttach : fotoCollectionNew) {
                fotoCollectionNewFotoToAttach = em.getReference(fotoCollectionNewFotoToAttach.getClass(), fotoCollectionNewFotoToAttach.getUrlFoto());
                attachedFotoCollectionNew.add(fotoCollectionNewFotoToAttach);
            }
            fotoCollectionNew = attachedFotoCollectionNew;
            propiedad.setFotoCollection(fotoCollectionNew);
            Collection<Ubicacion> attachedUbicacionCollectionNew = new ArrayList<Ubicacion>();
            for (Ubicacion ubicacionCollectionNewUbicacionToAttach : ubicacionCollectionNew) {
                ubicacionCollectionNewUbicacionToAttach = em.getReference(ubicacionCollectionNewUbicacionToAttach.getClass(), ubicacionCollectionNewUbicacionToAttach.getUbicacionId());
                attachedUbicacionCollectionNew.add(ubicacionCollectionNewUbicacionToAttach);
            }
            ubicacionCollectionNew = attachedUbicacionCollectionNew;
            propiedad.setUbicacionCollection(ubicacionCollectionNew);
            Collection<RentaSolicitud> attachedRentaSolicitudCollectionNew = new ArrayList<RentaSolicitud>();
            for (RentaSolicitud rentaSolicitudCollectionNewRentaSolicitudToAttach : rentaSolicitudCollectionNew) {
                rentaSolicitudCollectionNewRentaSolicitudToAttach = em.getReference(rentaSolicitudCollectionNewRentaSolicitudToAttach.getClass(), rentaSolicitudCollectionNewRentaSolicitudToAttach.getIdRegistroRenta());
                attachedRentaSolicitudCollectionNew.add(rentaSolicitudCollectionNewRentaSolicitudToAttach);
            }
            rentaSolicitudCollectionNew = attachedRentaSolicitudCollectionNew;
            propiedad.setRentaSolicitudCollection(rentaSolicitudCollectionNew);
            propiedad = em.merge(propiedad);
            if (propietarioOld != null && !propietarioOld.equals(propietarioNew)) {
                propietarioOld.getPropiedadCollection().remove(propiedad);
                propietarioOld = em.merge(propietarioOld);
            }
            if (propietarioNew != null && !propietarioNew.equals(propietarioOld)) {
                propietarioNew.getPropiedadCollection().add(propiedad);
                propietarioNew = em.merge(propietarioNew);
            }
            for (Cliente clienteCollectionOldCliente : clienteCollectionOld) {
                if (!clienteCollectionNew.contains(clienteCollectionOldCliente)) {
                    clienteCollectionOldCliente.getPropiedadCollection().remove(propiedad);
                    clienteCollectionOldCliente = em.merge(clienteCollectionOldCliente);
                }
            }
            for (Cliente clienteCollectionNewCliente : clienteCollectionNew) {
                if (!clienteCollectionOld.contains(clienteCollectionNewCliente)) {
                    clienteCollectionNewCliente.getPropiedadCollection().add(propiedad);
                    clienteCollectionNewCliente = em.merge(clienteCollectionNewCliente);
                }
            }
            for (Foto fotoCollectionNewFoto : fotoCollectionNew) {
                if (!fotoCollectionOld.contains(fotoCollectionNewFoto)) {
                    Propiedad oldPropiedadOfFotoCollectionNewFoto = fotoCollectionNewFoto.getPropiedad();
                    fotoCollectionNewFoto.setPropiedad(propiedad);
                    fotoCollectionNewFoto = em.merge(fotoCollectionNewFoto);
                    if (oldPropiedadOfFotoCollectionNewFoto != null && !oldPropiedadOfFotoCollectionNewFoto.equals(propiedad)) {
                        oldPropiedadOfFotoCollectionNewFoto.getFotoCollection().remove(fotoCollectionNewFoto);
                        oldPropiedadOfFotoCollectionNewFoto = em.merge(oldPropiedadOfFotoCollectionNewFoto);
                    }
                }
            }
            for (Ubicacion ubicacionCollectionNewUbicacion : ubicacionCollectionNew) {
                if (!ubicacionCollectionOld.contains(ubicacionCollectionNewUbicacion)) {
                    Propiedad oldPropiedadOfUbicacionCollectionNewUbicacion = ubicacionCollectionNewUbicacion.getPropiedad();
                    ubicacionCollectionNewUbicacion.setPropiedad(propiedad);
                    ubicacionCollectionNewUbicacion = em.merge(ubicacionCollectionNewUbicacion);
                    if (oldPropiedadOfUbicacionCollectionNewUbicacion != null && !oldPropiedadOfUbicacionCollectionNewUbicacion.equals(propiedad)) {
                        oldPropiedadOfUbicacionCollectionNewUbicacion.getUbicacionCollection().remove(ubicacionCollectionNewUbicacion);
                        oldPropiedadOfUbicacionCollectionNewUbicacion = em.merge(oldPropiedadOfUbicacionCollectionNewUbicacion);
                    }
                }
            }
            for (RentaSolicitud rentaSolicitudCollectionNewRentaSolicitud : rentaSolicitudCollectionNew) {
                if (!rentaSolicitudCollectionOld.contains(rentaSolicitudCollectionNewRentaSolicitud)) {
                    Propiedad oldPropiedadOfRentaSolicitudCollectionNewRentaSolicitud = rentaSolicitudCollectionNewRentaSolicitud.getPropiedad();
                    rentaSolicitudCollectionNewRentaSolicitud.setPropiedad(propiedad);
                    rentaSolicitudCollectionNewRentaSolicitud = em.merge(rentaSolicitudCollectionNewRentaSolicitud);
                    if (oldPropiedadOfRentaSolicitudCollectionNewRentaSolicitud != null && !oldPropiedadOfRentaSolicitudCollectionNewRentaSolicitud.equals(propiedad)) {
                        oldPropiedadOfRentaSolicitudCollectionNewRentaSolicitud.getRentaSolicitudCollection().remove(rentaSolicitudCollectionNewRentaSolicitud);
                        oldPropiedadOfRentaSolicitudCollectionNewRentaSolicitud = em.merge(oldPropiedadOfRentaSolicitudCollectionNewRentaSolicitud);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                PropiedadPK id = propiedad.getPropiedadPK();
                if (findPropiedad(id) == null) {
                    throw new NonexistentEntityException("The propiedad with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(PropiedadPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Propiedad propiedad;
            try {
                propiedad = em.getReference(Propiedad.class, id);
                propiedad.getPropiedadPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The propiedad with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Foto> fotoCollectionOrphanCheck = propiedad.getFotoCollection();
            for (Foto fotoCollectionOrphanCheckFoto : fotoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Propiedad (" + propiedad + ") cannot be destroyed since the Foto " + fotoCollectionOrphanCheckFoto + " in its fotoCollection field has a non-nullable propiedad field.");
            }
            Collection<Ubicacion> ubicacionCollectionOrphanCheck = propiedad.getUbicacionCollection();
            for (Ubicacion ubicacionCollectionOrphanCheckUbicacion : ubicacionCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Propiedad (" + propiedad + ") cannot be destroyed since the Ubicacion " + ubicacionCollectionOrphanCheckUbicacion + " in its ubicacionCollection field has a non-nullable propiedad field.");
            }
            Collection<RentaSolicitud> rentaSolicitudCollectionOrphanCheck = propiedad.getRentaSolicitudCollection();
            for (RentaSolicitud rentaSolicitudCollectionOrphanCheckRentaSolicitud : rentaSolicitudCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Propiedad (" + propiedad + ") cannot be destroyed since the RentaSolicitud " + rentaSolicitudCollectionOrphanCheckRentaSolicitud + " in its rentaSolicitudCollection field has a non-nullable propiedad field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Propietario propietario = propiedad.getPropietario();
            if (propietario != null) {
                propietario.getPropiedadCollection().remove(propiedad);
                propietario = em.merge(propietario);
            }
            Collection<Cliente> clienteCollection = propiedad.getClienteCollection();
            for (Cliente clienteCollectionCliente : clienteCollection) {
                clienteCollectionCliente.getPropiedadCollection().remove(propiedad);
                clienteCollectionCliente = em.merge(clienteCollectionCliente);
            }
            em.remove(propiedad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Propiedad> findPropiedadEntities() {
        return findPropiedadEntities(true, -1, -1);
    }

    public List<Propiedad> findPropiedadEntities(int maxResults, int firstResult) {
        return findPropiedadEntities(false, maxResults, firstResult);
    }

    private List<Propiedad> findPropiedadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Propiedad.class));
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

    public Propiedad findPropiedad(PropiedadPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Propiedad.class, id);
        } finally {
            em.close();
        }
    }

    public int getPropiedadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Propiedad> rt = cq.from(Propiedad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
