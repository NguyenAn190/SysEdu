package org.example.sysedu.repository;

import org.example.sysedu.entity.Learners;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;


public class LearnersRepository  {
    private final EntityManagerFactory emf;
    public LearnersRepository() {
        this.emf = Persistence.createEntityManagerFactory("myPersistenceUnit");
    }

    public Optional<Learners> findByMaxId() {
        EntityManager entityManager = emf.createEntityManager();

        TypedQuery<Learners> query = entityManager.createQuery(
                "SELECT l " +
                        "FROM Learners l " +
                        "WHERE CAST(SUBSTRING(l.id, 4) AS int) = (SELECT MAX(CAST(SUBSTRING(l2.id, 4) AS int)) FROM Learners l2 WHERE l2.id LIKE 'S%')", Learners.class);

        List<Learners> resultList = query.getResultList();
        entityManager.close();

        if (!resultList.isEmpty()) {
            return Optional.of(resultList.get(0));
        } else {
            return Optional.empty();
        }
    }

    public void save(Learners learners) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(learners);
        em.getTransaction().commit();
        em.close();
    }

    public void update(Learners learners) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(learners);
        em.getTransaction().commit();
        em.close();
    }

    public void updateStatusDeleteTrue(String learnersId) {
         EntityManager em = emf.createEntityManager();
         em.getTransaction().begin();
         Query query = em.createQuery("UPDATE Learners l SET l.isDelete = true WHERE l.id = :id");
         query.setParameter("id", learnersId);
         query.executeUpdate();
         em.getTransaction().commit();
         em.close();
    }

    public void updateStatusDeleteFalse(String learnersId) {
         EntityManager em = emf.createEntityManager();
         em.getTransaction().begin();
         Query query = em.createQuery("UPDATE Learners l SET l.isDelete = false WHERE l.id = :id");
         query.setParameter("id", learnersId);
         query.executeUpdate();
         em.getTransaction().commit();
         em.close();
    }

    public void delete(Learners learners) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.remove(learners);
        em.getTransaction().commit();
        em.close();
    }

    public Optional<Learners> findById(String id) {
        EntityManager entityManager = emf.createEntityManager();
        TypedQuery<Learners> query = entityManager.createQuery("SELECT l FROM Learners l WHERE l.id = :id", Learners.class);
        query.setParameter("id", id);
        List<Learners> resultList = query.getResultList();
        entityManager.close();
        if (!resultList.isEmpty()) {
            return Optional.of(resultList.get(0));
        } else {
            return Optional.empty();
        }
    }

    public List<Learners> findAll() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Learners> query = em.createQuery("SELECT l FROM Learners l ORDER BY l.dateCreated DESC", Learners.class);
        List<Learners> results = query.getResultList();
        em.close();
        return results;
    }

    public List<Learners> findAllByFilters(String searchFilter, Boolean isDelete) {
        EntityManager em = emf.createEntityManager();
        try {
            String queryStr = "SELECT l FROM Learners l WHERE (l.id LIKE :searchFilter OR l.fullName LIKE :searchFilter OR l.email LIKE :searchFilter OR l.phone LIKE :searchFilter) AND l.isDelete = :isDelete ORDER BY l.dateCreated DESC";
            TypedQuery<Learners> query = em.createQuery(queryStr, Learners.class);
            query.setParameter("searchFilter", "%" + searchFilter + "%");
            query.setParameter("isDelete", isDelete);
            return query.getResultList();
        } finally {
            em.close();
        }
    }


    public Integer findCountLearners() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(l) FROM Learners l", Long.class);
        Long result = query.getSingleResult();
        em.close();
        return result.intValue();
    }

    public Integer findCountLearnersByYear(Integer year) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(l) FROM Learners l WHERE year(l.dateCreated) = :year", Long.class);
        query.setParameter("year", year);
        Long result = query.getSingleResult();
        em.close();
        return result.intValue();
    }
    public List<Learners> findAllLearnersByYearCreated(Integer year) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Learners> query = em.createQuery("SELECT l FROM Learners l WHERE year(l.dateCreated) = :year", Learners.class);
        query.setParameter("year", year);
        List<Learners> results = query.getResultList();
        em.close();
        return results;
    }

    public boolean existsEmailLearners(String email) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(l) FROM Learners l WHERE l.email = :email", Long.class);
        query.setParameter("email", email);
        Long result = query.getSingleResult();
        em.close();
        return result > 0;
    }

    public boolean existsPhoneLearners(String phone) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(l) FROM Learners l WHERE l.phone = :phone", Long.class);
        query.setParameter("phone", phone);
        Long result = query.getSingleResult();
        em.close();
        return result > 0;
    }

}
