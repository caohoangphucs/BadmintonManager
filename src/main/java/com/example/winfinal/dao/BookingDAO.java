package com.example.winfinal.dao;

import com.example.winfinal.entity.Booking;
import jakarta.persistence.EntityManager;
import java.util.List;

public class BookingDAO extends BaseDAO<Booking, Integer> {
    public BookingDAO() {
        super(Booking.class);
    }
    
    public List<Booking> findByStatus(String status) {
        try (EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager()) {
            return em.createQuery("select b from Booking b where b.status = :status", Booking.class)
                     .setParameter("status", status)
                     .getResultList();
        }
    }

    public boolean existsByCourtId(Integer courtId) {
        try (EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager()) {
            Long count = em.createQuery("select count(b) from Booking b where b.court.courtId = :courtId", Long.class)
                     .setParameter("courtId", courtId)
                     .getSingleResult();
            return count > 0;
        }
    }

    public boolean existsByCustomerId(Integer customerId) {
        try (EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager()) {
            Long count = em.createQuery("select count(b) from Booking b where b.customer.customerId = :customerId", Long.class)
                     .setParameter("customerId", customerId)
                     .getSingleResult();
            return count > 0;
        }
    }

    public boolean existsByPromotionId(Integer promoId) {
        try (EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager()) {
            Long count = em.createQuery("select count(b) from Booking b join b.promotions p where p.promoId = :promoId", Long.class)
                     .setParameter("promoId", promoId)
                     .getSingleResult();
            return count > 0;
        }
    }
}
