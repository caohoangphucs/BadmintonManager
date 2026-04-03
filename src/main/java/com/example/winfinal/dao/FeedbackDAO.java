package com.example.winfinal.dao;

import com.example.winfinal.entity.Feedback;

import jakarta.persistence.EntityManager;

public class FeedbackDAO extends BaseDAO<Feedback, Integer> {
    public FeedbackDAO() {
        super(Feedback.class);
    }

    public boolean existsByCustomerId(Integer customerId) {
        try (EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager()) {
            Long count = em.createQuery("select count(f) from Feedback f where f.customer.customerId = :customerId", Long.class)
                     .setParameter("customerId", customerId)
                     .getSingleResult();
            return count > 0;
        }
    }
}
