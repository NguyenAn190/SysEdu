package org.example.sysedu.repository;

import org.example.sysedu.dto.courses.CoursesDTO;
import org.example.sysedu.dto.topics.TopTopicHotDTO;
import org.example.sysedu.dto.topics.TotalTuitionFeeDTO;
import org.example.sysedu.entity.Courses;
import org.example.sysedu.entity.Topics;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseRepository {
    private EntityManagerFactory emf;

    public CourseRepository() {
        this.emf = Persistence.createEntityManagerFactory("myPersistenceUnit");
    }

    public void save(Courses courses) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(courses);
        em.getTransaction().commit();
        em.close();
    }

    public void update(Courses courses) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(courses);
        em.getTransaction().commit();
        em.close();
    }

    public void delete(Courses courses) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.remove(courses);
        em.getTransaction().commit();
        em.close();
    }

    public Optional<Courses> findById(String id) {
        EntityManager em = emf.createEntityManager();
        Courses courses = em.find(Courses.class, id);
        em.close();
        if (courses != null) {
            return Optional.of(courses);
        } else {
            return Optional.empty();
        }
    }

    public List<Courses> findAll() {
        EntityManager em = emf.createEntityManager();
        List<Courses> results = em.createQuery("SELECT c FROM Courses c ORDER BY c.dateCreated DESC", Courses.class).getResultList();
        em.close();
        return results;
    }

    public List<Courses> findAllByFilter(String search, Boolean isDelete, String topicId) {
        EntityManager em = emf.createEntityManager();
        try {
            String queryStr = "SELECT c\n" +
                    "FROM Courses c\n" +
                    "WHERE (:search IS NULL OR c.id LIKE :search OR c.topicId LIKE :search)\n" +
                    "  AND (:isDelete IS NULL OR c.isDelete = :isDelete)\n" +
                    "  AND (:topicId IS NULL OR c.topicId = :topicId)\n" +
                    "ORDER BY c.dateCreated DESC";
            TypedQuery<Courses> query = em.createQuery(queryStr, Courses.class);
            query.setParameter("search", "%" + search + "%");
            query.setParameter("isDelete", isDelete);
            query.setParameter("topicId", topicId);
            List<Courses> results = query.getResultList();
            em.close();
            return results;
        } finally {
            em.close();
        }
    }

    public List<Courses> findAllCourseByYearCreate(Integer year) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Courses> query = em.createQuery("SELECT c FROM Courses c WHERE year(c.dateCreated) = :year", Courses.class);
        query.setParameter("year", year);
        List<Courses> results = query.getResultList();
        em.close();
        return results;
    }

    public Integer findCountCourersByYear(Integer year) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(c) FROM Courses c WHERE year(c.dateCreated) = :year", Long.class);
        query.setParameter("year", year);
        Long result = query.getSingleResult();
        em.close();
        return result.intValue();
    }

    public Integer findCountCourers() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(c) FROM Courses c", Long.class);
        Long result = query.getSingleResult();
        em.close();
        return result.intValue();
    }

    public Optional<Courses> findMaxId() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Courses> query = em.createQuery("SELECT c\n" +
                "FROM Courses c\n" +
                "WHERE\n" +
                "    CAST(SUBSTRING(c.id, 4) AS int) = (SELECT MAX(CAST(SUBSTRING(c2.id, 4) AS int)) FROM Courses c2 WHERE c2.id LIKE 'C%')", Courses.class);
        List<Courses> resultList = query.getResultList();
        em.close();

        if (!resultList.isEmpty()) {
            return Optional.of(resultList.get(0));
        } else {
            return Optional.empty();
        }
    }

    public void updateStatusDeleteTrue(String id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Courses courses = em.find(Courses.class, id);
        courses.setIsDelete(true);
        em.getTransaction().commit();
        em.close();
    }

    public void updateStatusDeleteFalse(String id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Courses courses = em.find(Courses.class, id);
        courses.setIsDelete(false);
        em.getTransaction().commit();
        em.close();
    }

    public List<Courses> findAllByTopicNameAndIsDelete(String topicName, boolean isDelete) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Courses> query = em.createQuery("SELECT c FROM Courses c\n" +
                "INNER JOIN Topics t on c.topicId = t.id\n" +
                "WHERE t.topicName = :topicName AND c.isDelete = :isDelete", Courses.class);
        query.setParameter("topicName", topicName);
        query.setParameter("isDelete", isDelete);
        List<Courses> results = query.getResultList();
        em.close();
        return results;
    }

    public Optional<Courses> findByCourseName(String courseName) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Courses> query = em.createQuery("SELECT c FROM Courses c WHERE c.courseName = :courseName", Courses.class);
        query.setParameter("courseName", courseName);
        List<Courses> results = query.getResultList();
        em.close();
        if (!results.isEmpty()) {
            return Optional.of(results.get(0));
        } else {
            return Optional.empty();
        }
    }

    public List<Courses> findCourseByTopicId(String topicId) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Courses> query = em.createQuery("SELECT c FROM Courses c WHERE c.topicId = :topicId", Courses.class);
        query.setParameter("topicId", topicId);
        List<Courses> results = query.getResultList();
        em.close();
        return results;
    }

    public List<CoursesDTO> getCourse(Integer year) {
        EntityManager em = emf.createEntityManager();
        List<CoursesDTO> coursesDTOS = new ArrayList<>();

        try {
            em.getTransaction().begin();

            StoredProcedureQuery storedProcedureQuery = em.createStoredProcedureQuery("GetCourse");
            storedProcedureQuery.registerStoredProcedureParameter("year", Integer.class, ParameterMode.IN);
            storedProcedureQuery.setParameter("year", year);
            boolean isResultSet = storedProcedureQuery.execute();

            if (isResultSet) {
                List<Object[]> resultList = storedProcedureQuery.getResultList();
                for (Object[] result : resultList) {
                    String id = result[0].toString();
                    String courseName = result[1].toString();
                    Integer yearCreated = Integer.parseInt(result[2].toString());
                    Integer countStudent = Integer.parseInt(result[3].toString());
                    Timestamp minDate = Timestamp.valueOf(result[4].toString());
                    Timestamp maxDate = Timestamp.valueOf(result[5].toString());

                    CoursesDTO coursesDTO = new CoursesDTO(id, courseName, yearCreated, countStudent, minDate, maxDate);
                    coursesDTOS.add(coursesDTO);
                }
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }

        return coursesDTOS;
    }

    public List<Integer> getCourseInYear(Integer year) {
        EntityManager em = emf.createEntityManager();
        List<Integer> countCourse = new ArrayList<>();

        try {
            em.getTransaction().begin();

            StoredProcedureQuery storedProcedureQuery = em.createStoredProcedureQuery("GetCountCourseInYear");
            storedProcedureQuery.registerStoredProcedureParameter("year", Integer.class, ParameterMode.IN);
            storedProcedureQuery.setParameter("year", year);
            boolean isResultSet = storedProcedureQuery.execute();

            if (isResultSet) {
                List<BigInteger> resultList = storedProcedureQuery.getResultList();
                for(BigInteger result : resultList) {
                    countCourse.add(result.intValue());
                }
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }

        return countCourse;
    }
}
