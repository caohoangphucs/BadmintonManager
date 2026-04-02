package com.example.winfinal.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class BaseDAO<T, ID extends Serializable> {
    private final Class<T> clazz;

    protected BaseDAO(Class<T> clazz) {
        this.clazz = clazz;
    }

    public List<T> findAll() {
        try (EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager()) {
            return em.createQuery("from " + clazz.getName(), clazz).getResultList();
        }
    }

    public Optional<T> findById(ID id) {
        try (EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager()) {
            T entity = em.find(clazz, id);
            return Optional.ofNullable(entity);
        }
    }

    public void save(T entity) {
        executeInTransaction(em -> em.persist(entity));
    }

    public void update(T entity) {
        executeInTransaction(em -> em.merge(entity));
    }

    public void delete(T entity) {
        executeInTransaction(em -> em.remove(em.contains(entity) ? entity : em.merge(entity)));
    }

    public void deleteById(ID id) {
        executeInTransaction(em -> {
            T entity = em.find(clazz, id);
            if (entity != null) {
                em.remove(entity);
            }
        });
    }

    protected void executeInTransaction(java.util.function.Consumer<EntityManager> action) {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            action.accept(em);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
