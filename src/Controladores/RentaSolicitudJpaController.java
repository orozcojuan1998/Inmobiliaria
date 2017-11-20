/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Banco;
import Entidades.Propiedad;
import Entidades.RentaSolicitud;
import Entidades.Sucursal;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author COMERCIAL02
 */
public class RentaSolicitudJpaController implements Serializable {

    public RentaSolicitudJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RentaSolicitud rentaSolicitud) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Banco banco = rentaSolicitud.getBanco();
            if (banco != null) {
                banco = em.getReference(banco.getClass(), banco.getBancoPK());
                rentaSolicitud.setBanco(banco);
            }
            Propiedad propiedad = rentaSolicitud.getPropiedad();
            if (propiedad != null) {
                propiedad = em.getReference(propiedad.getClass(), propiedad.getPropiedadPK());
                rentaSolicitud.setPropiedad(propiedad);
            }
            Sucursal sucursalNumeroSucursal = rentaSolicitud.getSucursalNumeroSucursal();
            if (sucursalNumeroSucursal != null) {
                sucursalNumeroSucursal = em.getReference(sucursalNumeroSucursal.getClass(), sucursalNumeroSucursal.getNumeroSucursal());
                rentaSolicitud.setSucursalNumeroSucursal(sucursalNumeroSucursal);
            }
            em.persist(rentaSolicitud);
            if (banco != null) {
                banco.getRentaSolicitudCollection().add(rentaSolicitud);
                banco = em.merge(banco);
            }
            if (propiedad != null) {
                propiedad.getRentaSolicitudCollection().add(rentaSolicitud);
                propiedad = em.merge(propiedad);
            }
            if (sucursalNumeroSucursal != null) {
                sucursalNumeroSucursal.getRentaSolicitudCollection().add(rentaSolicitud);
                sucursalNumeroSucursal = em.merge(sucursalNumeroSucursal);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRentaSolicitud(rentaSolicitud.getIdRegistroRenta()) != null) {
                throw new PreexistingEntityException("RentaSolicitud " + rentaSolicitud + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RentaSolicitud rentaSolicitud) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RentaSolicitud persistentRentaSolicitud = em.find(RentaSolicitud.class, rentaSolicitud.getIdRegistroRenta());
            Banco bancoOld = persistentRentaSolicitud.getBanco();
            Banco bancoNew = rentaSolicitud.getBanco();
            Propiedad propiedadOld = persistentRentaSolicitud.getPropiedad();
            Propiedad propiedadNew = rentaSolicitud.getPropiedad();
            Sucursal sucursalNumeroSucursalOld = persistentRentaSolicitud.getSucursalNumeroSucursal();
            Sucursal sucursalNumeroSucursalNew = rentaSolicitud.getSucursalNumeroSucursal();
            if (bancoNew != null) {
                bancoNew = em.getReference(bancoNew.getClass(), bancoNew.getBancoPK());
                rentaSolicitud.setBanco(bancoNew);
            }
            if (propiedadNew != null) {
                propiedadNew = em.getReference(propiedadNew.getClass(), propiedadNew.getPropiedadPK());
                rentaSolicitud.setPropiedad(propiedadNew);
            }
            if (sucursalNumeroSucursalNew != null) {
                sucursalNumeroSucursalNew = em.getReference(sucursalNumeroSucursalNew.getClass(), sucursalNumeroSucursalNew.getNumeroSucursal());
                rentaSolicitud.setSucursalNumeroSucursal(sucursalNumeroSucursalNew);
            }
            rentaSolicitud = em.merge(rentaSolicitud);
            if (bancoOld != null && !bancoOld.equals(bancoNew)) {
                bancoOld.getRentaSolicitudCollection().remove(rentaSolicitud);
                bancoOld = em.merge(bancoOld);
            }
            if (bancoNew != null && !bancoNew.equals(bancoOld)) {
                bancoNew.getRentaSolicitudCollection().add(rentaSolicitud);
                bancoNew = em.merge(bancoNew);
            }
            if (propiedadOld != null && !propiedadOld.equals(propiedadNew)) {
                propiedadOld.getRentaSolicitudCollection().remove(rentaSolicitud);
                propiedadOld = em.merge(propiedadOld);
            }
            if (propiedadNew != null && !propiedadNew.equals(propiedadOld)) {
                propiedadNew.getRentaSolicitudCollection().add(rentaSolicitud);
                propiedadNew = em.merge(propiedadNew);
            }
            if (sucursalNumeroSucursalOld != null && !sucursalNumeroSucursalOld.equals(sucursalNumeroSucursalNew)) {
                sucursalNumeroSucursalOld.getRentaSolicitudCollection().remove(rentaSolicitud);
                sucursalNumeroSucursalOld = em.merge(sucursalNumeroSucursalOld);
            }
            if (sucursalNumeroSucursalNew != null && !sucursalNumeroSucursalNew.equals(sucursalNumeroSucursalOld)) {
                sucursalNumeroSucursalNew.getRentaSolicitudCollection().add(rentaSolicitud);
                sucursalNumeroSucursalNew = em.merge(sucursalNumeroSucursalNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = rentaSolicitud.getIdRegistroRenta();
                if (findRentaSolicitud(id) == null) {
                    throw new NonexistentEntityException("The rentaSolicitud with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RentaSolicitud rentaSolicitud;
            try {
                rentaSolicitud = em.getReference(RentaSolicitud.class, id);
                rentaSolicitud.getIdRegistroRenta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rentaSolicitud with id " + id + " no longer exists.", enfe);
            }
            Banco banco = rentaSolicitud.getBanco();
            if (banco != null) {
                banco.getRentaSolicitudCollection().remove(rentaSolicitud);
                banco = em.merge(banco);
            }
            Propiedad propiedad = rentaSolicitud.getPropiedad();
            if (propiedad != null) {
                propiedad.getRentaSolicitudCollection().remove(rentaSolicitud);
                propiedad = em.merge(propiedad);
            }
            Sucursal sucursalNumeroSucursal = rentaSolicitud.getSucursalNumeroSucursal();
            if (sucursalNumeroSucursal != null) {
                sucursalNumeroSucursal.getRentaSolicitudCollection().remove(rentaSolicitud);
                sucursalNumeroSucursal = em.merge(sucursalNumeroSucursal);
            }
            em.remove(rentaSolicitud);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RentaSolicitud> findRentaSolicitudEntities() {
        return findRentaSolicitudEntities(true, -1, -1);
    }

    public List<RentaSolicitud> findRentaSolicitudEntities(int maxResults, int firstResult) {
        return findRentaSolicitudEntities(false, maxResults, firstResult);
    }

    private List<RentaSolicitud> findRentaSolicitudEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RentaSolicitud.class));
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

    public RentaSolicitud findRentaSolicitud(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RentaSolicitud.class, id);
        } finally {
            em.close();
        }
    }

    public int getRentaSolicitudCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RentaSolicitud> rt = cq.from(RentaSolicitud.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
