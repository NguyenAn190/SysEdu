package org.example.sysedu.repository;


import org.example.sysedu.entity.Users;
import org.example.sysedu.enums.Role;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

public class UsersRepository {
    private EntityManagerFactory emf;
    public UsersRepository() {
        this.emf = Persistence.createEntityManagerFactory("myPersistenceUnit");
    }

    public void create(Users users) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(users);
        em.getTransaction().commit();
        em.close();
    }

    public void update(Users users) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(users);
        em.getTransaction().commit();
        em.close();
    }

    public void delete(String id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Users users = em.find(Users.class, id);
        em.remove(users);
        em.getTransaction().commit();
        em.close();
    }

    public Boolean existsUsername(String username) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Users> query = em.createQuery("SELECT u FROM Users u WHERE u.username = :username", Users.class);
        query.setParameter("username", username);
        List<Users> users = query.getResultList();
        if (users.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Optional<Users> findByUsername(String username) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Users> query = em.createQuery("SELECT u FROM Users u WHERE u.username = :username", Users.class);
        query.setParameter("username", username);
        List<Users> users = query.getResultList();
        em.close();
        if (!users.isEmpty()) {
            return Optional.of(users.get(0));
        } else {
            return Optional.empty();
        }
    }

    public Integer findCountUsers() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(u) FROM Users u", Long.class);
        return query.getSingleResult().intValue();
    }

    public Integer findCountUsersByYear(Integer year) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(u) FROM Users u WHERE year(u.dateCreated) = :year", Long.class);
        query.setParameter("year", year);
        Long result = query.getSingleResult();
        em.close();
        return result.intValue();
    }

    public List<Users> findAll() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Users> query = em.createQuery("SELECT u FROM Users u", Users.class);
        List<Users> users = query.getResultList();
        em.close();
        return users;
    }

    public List<Users> findAllByFilter(String search, Boolean isDelete) {
        EntityManager em = emf.createEntityManager();
        try {
            String queryStr = "SELECT u FROM Users u WHERE (u.id LIKE :search OR u.username LIKE :search) AND u.isDelete = :isDelete ORDER BY u.dateCreated DESC";
            TypedQuery<Users> query = em.createQuery(queryStr, Users.class);
            query.setParameter("search", "%" + search + "%");
            query.setParameter("isDelete", isDelete);
            List<Users> users = query.getResultList();
            em.close();
            return users;
        } finally {
            em.close();
        }
    }

    public void updateUsersIsDeleteTrue(String id){
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Query query = em.createQuery("UPDATE Users u SET u.isDelete = true WHERE u.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }

    public void updateUsersIsDeleteFalse(String id){
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Query query = em.createQuery("UPDATE Users u SET u.isDelete = false WHERE u.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }

    public List<Users> findAllUsersByYearCreated(int year) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Users> query = em.createQuery("SELECT u FROM Users u WHERE year(u.dateCreated) = :year", Users.class);
        query.setParameter("year", year);
        List<Users> results = query.getResultList();
        em.close();
        return results;
    }

    public Optional<Users> findMaxId() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Users> query = em.createQuery("SELECT u\n" +
                "FROM Users u\n" +
                "WHERE\n" +
                "    CAST(SUBSTRING(u.id, 4) AS int) = (SELECT MAX(CAST(SUBSTRING(u2.id, 4) AS int)) FROM Users u2 WHERE u2.id LIKE 'T%')", Users.class);
        List<Users> users = query.getResultList();
        em.close();
        if (!users.isEmpty()) {
            return Optional.of(users.get(0));
        } else {
            return Optional.empty();
        }
    }

    public List<Users> findAllByRole(Role role) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Users> query = em.createQuery("SELECT u FROM Users u WHERE u.role = :role", Users.class);
        query.setParameter("role", role);
        List<Users> users = query.getResultList();
        em.close();
        return users;
    }
}
