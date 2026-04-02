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
}
