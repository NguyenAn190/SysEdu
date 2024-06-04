package org.example.sysedu.repository;

import org.example.sysedu.dto.students.ScoreStatisticsDTO;
import org.example.sysedu.dto.students.StudentRankDTO;
import org.example.sysedu.dto.students.StudentScoreDTO;
import org.example.sysedu.entity.Students;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class StudentsRepository {
    private EntityManagerFactory emf;
    public StudentsRepository() {
        this.emf = Persistence.createEntityManagerFactory("myPersistenceUnit");
    }

    public void save(Students students) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(students);
        em.getTransaction().commit();
        em.close();
    }

    public void update(Students students) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(students);
        em.getTransaction().commit();
        em.close();
    }

    public void delete(Integer id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Students students = em.find(Students.class, id);
        em.remove(students);
        em.getTransaction().commit();
        em.close();
    }

    public Optional<Students> findById(Integer id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Students students = em.find(Students.class, id);
        em.getTransaction().commit();
        em.close();
        if (students!= null) {
            return Optional.of(students);
        } else {
            return Optional.empty();
        }
    }

    public List<Students> findByFilter(String searchParams, String topicName, String courseName) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        List<Students> students = em.createQuery("" +
                        "SELECT s\n" +
                        "FROM Students s\n" +
                        "         INNER JOIN Courses c ON s.coursesId = c.id\n" +
                        "         INNER JOIN Topics t ON c.topicId = t.id\n" +
                        "         INNER JOIN Learners l ON s.learnersId = l.id\n" +
                        "WHERE (:topicName IS NULL OR :topicName = '' OR t.topicName LIKE CONCAT('%', :topicName, '%'))\n" +
                        "  AND (:courseName IS NULL OR :courseName = '' OR c.courseName LIKE CONCAT('%', :courseName, '%'))\n" +
                        "  AND (\n" +
                        "    :searchParams IS NULL OR :searchParams = '' OR\n" +
                        "    s.id LIKE CONCAT('%', :searchParams, '%') OR\n" +
                        "    c.id LIKE CONCAT('%', :searchParams, '%') OR\n" +
                        "    c.courseName LIKE CONCAT('%', :searchParams, '%') OR\n" +
                        "    t.topicName LIKE CONCAT('%', :searchParams, '%') OR\n" +
                        "    t.id LIKE CONCAT('%', :searchParams, '%') OR\n" +
                        "    l.id LIKE CONCAT('%', :searchParams, '%') OR\n" +
                        "    l.fullName LIKE CONCAT('%', :searchParams, '%') OR\n" +
                        "    l.email LIKE CONCAT('%', :searchParams, '%') OR\n" +
                        "    l.phone LIKE CONCAT('%', :searchParams, '%')\n" +
                        "    )")
               .setParameter("searchParams", "%" + searchParams + "%")
               .setParameter("topicName", topicName)
               .setParameter("courseName", courseName)
               .getResultList();
        em.getTransaction().commit();
        em.close();
        return students;
    }

    public List<Students> findAll() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        List<Students> students = em.createQuery("SELECT s\n" +
                "FROM Students s\n" , Students.class)
               .getResultList();
        em.getTransaction().commit();
        em.close();
        return students;
    }

    public Integer countStudent() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Long count = em.createQuery("SELECT COUNT(s.id)\n" +
                "FROM Students s\n" , Long.class)
               .getSingleResult();
        em.getTransaction().commit();
        em.close();
        return count.intValue();
    }

    public Integer countStudentByYear (Integer year) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Long count = em.createQuery("SELECT COUNT(s.id)\n" +
                "FROM Students s\n" +
                "WHERE year(s.dateCreated) = :year", Long.class)
               .setParameter("year", year)
               .getSingleResult();
        em.getTransaction().commit();
        em.close();
        return count.intValue();
    }

    public List<Students> findAllStudentByYearCreated(int year) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Students> query = em.createQuery("SELECT t FROM Students t WHERE year(t.dateCreated) = :year", Students.class);
        query.setParameter("year", year);
        List<Students> results = query.getResultList();
        em.close();
        return results;
    }

    public List<StudentScoreDTO> getStudentScores(String searchParams,String topicNameParam,  String courseNameParam) {
        EntityManager em = emf.createEntityManager();
        List<StudentScoreDTO> studentScoreDTOS = new ArrayList<>();

        try {
            em.getTransaction().begin();

            StoredProcedureQuery storedProcedureQuery = em.createStoredProcedureQuery("GetStudentScores");
            storedProcedureQuery.registerStoredProcedureParameter("searchParams", String.class, ParameterMode.IN);
            storedProcedureQuery.registerStoredProcedureParameter("topicNameParam", String.class, ParameterMode.IN);
            storedProcedureQuery.registerStoredProcedureParameter("courseNameParam", String.class, ParameterMode.IN);

            storedProcedureQuery.setParameter("searchParams", searchParams);
            storedProcedureQuery.setParameter("topicNameParam", topicNameParam);
            storedProcedureQuery.setParameter("courseNameParam", courseNameParam);


            boolean isResultSet = storedProcedureQuery.execute();

            if (isResultSet) {
                List<Object[]> resultList = storedProcedureQuery.getResultList();
                for (Object[] result : resultList) {
                    Integer id = (Integer) result[0];
                    String fullName = (String) result[1];
                    Float score = (Float) result[2];
                    String ranks = (String) result[3];

                    StudentScoreDTO studentScoreDTO = new StudentScoreDTO(id, fullName, score, ranks);
                    studentScoreDTOS.add(studentScoreDTO);
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

        return studentScoreDTOS;
    }

    public List<StudentRankDTO> getStudentRank(String courseName) {
        EntityManager em = emf.createEntityManager();
        List<StudentRankDTO> studentScoreDTOS = new ArrayList<>();
        try{
            em.getTransaction().begin();
            StoredProcedureQuery storedProcedureQuery = em.createStoredProcedureQuery("GetStudentRanks");
            storedProcedureQuery.registerStoredProcedureParameter("courseNameParam", String.class, ParameterMode.IN);
            storedProcedureQuery.setParameter("courseNameParam", courseName);
            boolean isResultSet = storedProcedureQuery.execute();
            if (isResultSet) {
                List<Object[]> resultList = storedProcedureQuery.getResultList();
                for (Object[] result : resultList) {
                    Integer id = Integer.parseInt(result[0].toString());
                    String ranks = (String) result[1];

                    StudentRankDTO studentScoreDTO = new StudentRankDTO(id, ranks);
                    studentScoreDTOS.add(studentScoreDTO);
                }
            }
            em.getTransaction().commit();
        }catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
        return studentScoreDTOS;
    }

    public List<ScoreStatisticsDTO> getCountScore(String courseName) {
        EntityManager em = emf.createEntityManager();
        List<ScoreStatisticsDTO> scoreStatisticsDTOS = new ArrayList<>();
        List<ScoreStatisticsDTO> scoreStatistics = new ArrayList<>();
        try{
            em.getTransaction().begin();
            StoredProcedureQuery storedProcedureQuery = em.createStoredProcedureQuery("GetCountScore");
            storedProcedureQuery.registerStoredProcedureParameter("courseNameParam", String.class, ParameterMode.IN);
            storedProcedureQuery.setParameter("courseNameParam", courseName);
            boolean isResultSet = storedProcedureQuery.execute();
            for (int i = 0; i <= 10; i++) {
                scoreStatistics.add(new ScoreStatisticsDTO(0, i));
            }

            if (isResultSet) {
                List<Object[]> resultList = storedProcedureQuery.getResultList();
                for (Object[] result : resultList) {
                    Integer count = Integer.parseInt(result[0].toString());
                    Integer score = Integer.parseInt(result[1].toString()) ;


                    ScoreStatisticsDTO scoreStatisticsDTO = new ScoreStatisticsDTO(count, score);
                    scoreStatisticsDTOS.add(scoreStatisticsDTO);
                }
            }

            for(ScoreStatisticsDTO score : scoreStatisticsDTOS) {
                Integer scoreValue = score.getScore();
                for (ScoreStatisticsDTO statistic : scoreStatistics) {
                    if (Objects.equals(statistic.getScore(), scoreValue)) {
                        statistic.setCount(score.getCount());
                        break;
                    }
                }
            }
            em.getTransaction().commit();
        }catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
        return scoreStatistics;
    }

}
