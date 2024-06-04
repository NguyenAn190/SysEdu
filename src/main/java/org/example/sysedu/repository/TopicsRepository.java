package org.example.sysedu.repository;

import org.example.sysedu.dto.topics.*;
import org.example.sysedu.entity.Topics;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TopicsRepository {
    private EntityManagerFactory emf;

    public TopicsRepository() {
        this.emf = Persistence.createEntityManagerFactory("myPersistenceUnit");
    }


    public void save(Topics topic) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(topic);
        em.getTransaction().commit();
        em.close();
    }

    public void update(Topics topic) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(topic);
        em.getTransaction().commit();
        em.close();
    }

    public void delete(Topics topic) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.remove(topic);
        em.getTransaction().commit();
        em.close();
    }

    public Optional<Topics> findById(String id) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Topics> query = em.createQuery("SELECT t FROM Topics t WHERE t.id = :id", Topics.class);
        query.setParameter("id", id);
        List<Topics> resultList = query.getResultList();
        em.close();
        if (!resultList.isEmpty()) {
            return Optional.of(resultList.get(0));
        } else {
            return Optional.empty();
        }
    }

    public List<Topics> findAll() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Topics> query = em.createQuery("SELECT t FROM Topics t ORDER BY t.dateCreated", Topics.class);
        List<Topics> results = query.getResultList();
        em.close();
        return results;
    }

    public List<Topics> findAllByFilter(String search, Boolean isDelete) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT t FROM Topics t WHERE (t.id LIKE :search OR t.topicName LIKE :search) AND t.isDelete = :isDelete ORDER BY t.dateCreated";
            TypedQuery<Topics> query = em.createQuery(jpql, Topics.class);
            query.setParameter("search", "%" + search + "%");
            query.setParameter("isDelete", isDelete);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Topics> findAllTopicsByYearCreated(Integer year) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Topics> query = em.createQuery("SELECT t FROM Topics t WHERE year(t.dateCreated) = :year", Topics.class);
        query.setParameter("year", year);
        List<Topics> results = query.getResultList();
        em.close();
        return results;
    }

    public boolean existsId(String id) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(t) FROM Topics t WHERE t.id = :id", Long.class);
        query.setParameter("id", id);
        Long result = query.getSingleResult();
        em.close();
        return result > 0;
    }

    public Integer findCountTopicsByYear(Integer year) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(t) FROM Topics t WHERE year(t.dateCreated) = :year", Long.class);
        query.setParameter("year", year);
        Long result = query.getSingleResult();
        em.close();
        return result.intValue();
    }

    public Integer findCountTopics() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(t) FROM Topics t", Long.class);
        Long result = query.getSingleResult();
        em.close();
        return result.intValue();
    }

    public void updateStatusDeleteTrueById(String id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Topics topic = em.find(Topics.class, id);
        topic.setIsDelete(true);
        em.getTransaction().commit();
        em.close();
    }

    public void updateStatusDeleteFalseById(String id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Topics topic = em.find(Topics.class, id);
        topic.setIsDelete(false);
        em.getTransaction().commit();
        em.close();
    }

    public Optional<Topics> findMaxId() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Topics> query = em.createQuery("SELECT t\n" +
                "FROM Topics t\n" +
                "WHERE\n" +
                "    CAST(SUBSTRING(t.id, 4) AS int) = (SELECT MAX(CAST(SUBSTRING(l2.id, 4) AS int)) FROM Topics l2 WHERE l2.id LIKE 'T%')", Topics.class);
        List<Topics> resultList = query.getResultList();
        em.close();

        if (!resultList.isEmpty()) {
            return Optional.of(resultList.get(0));
        } else {
            return Optional.empty();
        }
    }

    public Optional<Topics> findByName(String topicName) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Topics> query = em.createQuery("SELECT t FROM Topics t WHERE t.topicName = :topicName", Topics.class);
        query.setParameter("topicName", topicName);
        List<Topics> resultList = query.getResultList();
        em.close();

        if (!resultList.isEmpty()) {
            return Optional.of(resultList.get(0));
        } else {
            return Optional.empty();
        }
    }

    public List<TopicScoreDTO> getScoreTopics(String searchParam) {
        EntityManager em = emf.createEntityManager();
        List<TopicScoreDTO> topicScoreDTOS = new ArrayList<>();

        try {
            em.getTransaction().begin();

            StoredProcedureQuery storedProcedureQuery = em.createStoredProcedureQuery("GetScoreTopics");
            storedProcedureQuery.registerStoredProcedureParameter("searchParam", String.class, ParameterMode.IN);
            storedProcedureQuery.setParameter("searchParam", searchParam);
            boolean isResultSet = storedProcedureQuery.execute();

            if (isResultSet) {
                List<Object[]> resultList = storedProcedureQuery.getResultList();
                for (Object[] result : resultList) {
                    String id = (String) result[0];
                    String topicName = (String) result[1];
                    Integer countStudent = Integer.parseInt(result[2].toString());
                    Float averageScore = Float.parseFloat(result[3].toString());
                    Float higestScore = Float.parseFloat(result[4].toString());
                    Float lowestScore = Float.parseFloat(result[5].toString());

                    TopicScoreDTO studentScoreDTO = new TopicScoreDTO(id, topicName, countStudent, averageScore, higestScore, lowestScore);
                    topicScoreDTOS.add(studentScoreDTO);
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

        return topicScoreDTOS;
    }

    public List<HighestTopicScoreDTO> getTopScoreTopic(String searchParam) {
        EntityManager em = emf.createEntityManager();
        List<HighestTopicScoreDTO> highestTopicScoreDTOS = new ArrayList<>();

        try {
            em.getTransaction().begin();

            StoredProcedureQuery storedProcedureQuery = em.createStoredProcedureQuery("GetTopScoreTopic");
            boolean isResultSet = storedProcedureQuery.execute();

            if (isResultSet) {
                List<Object[]> resultList = storedProcedureQuery.getResultList();
                for (Object[] result : resultList) {
                    String id = result[0].toString();
                    String topicName = result[1].toString();
                    Integer countStudent = Integer.parseInt(result[2].toString());
                    Float averageScore = Float.parseFloat(result[3].toString());

                    HighestTopicScoreDTO studentScoreDTO = new HighestTopicScoreDTO(id, topicName, countStudent, averageScore);
                    highestTopicScoreDTOS.add(studentScoreDTO);
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

        return highestTopicScoreDTOS;
    }

    public List<RevenueStatisticsDTO> getRevenueStatistic(LocalDate timeStart, LocalDate timeEnd) {
        EntityManager em = emf.createEntityManager();
        List<RevenueStatisticsDTO> revenueStatisticsDTOS = new ArrayList<>();

        try {
            em.getTransaction().begin();

            StoredProcedureQuery storedProcedureQuery = em.createStoredProcedureQuery("GetRevenueStatistic");
            storedProcedureQuery.registerStoredProcedureParameter("startDate", LocalDate.class, ParameterMode.IN);
            storedProcedureQuery.registerStoredProcedureParameter("endDate", LocalDate.class, ParameterMode.IN);

            if (timeStart != null) {
                storedProcedureQuery.setParameter("startDate", timeStart);
            } else {
                storedProcedureQuery.setParameter("startDate", LocalDate.parse("1970-01-01"));
            }

            if (timeEnd != null) {
                storedProcedureQuery.setParameter("endDate", timeEnd);
            } else {
                storedProcedureQuery.setParameter("endDate", LocalDate.parse("9999-12-31"));
            }

            boolean isResultSet = storedProcedureQuery.execute();

            if (isResultSet) {
                List<Object[]> resultList = storedProcedureQuery.getResultList();
                for (Object[] result : resultList) {
                    String id = result[0].toString();
                    String topicName = result[1].toString();
                    Integer countStudent = Integer.parseInt(result[2].toString());
                    Integer courseCount = Integer.parseInt(result[3].toString());
                    BigDecimal sumTuitionFee = BigDecimal.valueOf(Integer.parseInt(result[4].toString()));
                    BigDecimal minTuitionFee = BigDecimal.valueOf(Integer.parseInt(result[5].toString()));
                    BigDecimal averageTuitionFee = BigDecimal.valueOf(Integer.parseInt(result[6].toString()));
                    BigDecimal maxTuitionFee = BigDecimal.valueOf(Integer.parseInt(result[7].toString()));

                    RevenueStatisticsDTO revenueStatisticsDTO = new RevenueStatisticsDTO(id, topicName, countStudent, courseCount, sumTuitionFee, minTuitionFee, averageTuitionFee, maxTuitionFee);
                    revenueStatisticsDTOS.add(revenueStatisticsDTO);
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

        return revenueStatisticsDTOS;
    }

    public List<TopTopicHotDTO> getTop5TopicHot() {
        EntityManager em = emf.createEntityManager();
        List<TopTopicHotDTO> topTopicHotDTOS = new ArrayList<>();

        try {
            em.getTransaction().begin();

            StoredProcedureQuery storedProcedureQuery = em.createStoredProcedureQuery("getTop5TopicHot");
            boolean isResultSet = storedProcedureQuery.execute();

            if (isResultSet) {
                List<Object[]> resultList = storedProcedureQuery.getResultList();
                for (Object[] result : resultList) {
                    String id = result[0].toString();
                    String topicName = result[1].toString();
                    Integer countStudent = Integer.parseInt(result[2].toString());
                    Integer countCourse = Integer.parseInt(result[3].toString());
                    BigDecimal totalTuitionFee =BigDecimal.valueOf(Integer.parseInt(result[4].toString()));

                    TopTopicHotDTO topTopicHotDTO = new TopTopicHotDTO(id, topicName, countStudent, countCourse, totalTuitionFee);
                    topTopicHotDTOS.add(topTopicHotDTO);
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

        return topTopicHotDTOS;
    }

    public List<TotalTuitionFeeDTO> getTotalTuitionFeeInYear(Integer year) {
        EntityManager em = emf.createEntityManager();
        List<TotalTuitionFeeDTO> totalTuitionFeeDTOS = new ArrayList<>();

        try {
            em.getTransaction().begin();

            StoredProcedureQuery storedProcedureQuery = em.createStoredProcedureQuery("GetTotalTuitionFeeInYear");
            storedProcedureQuery.registerStoredProcedureParameter("year", Integer.class, ParameterMode.IN);
            storedProcedureQuery.setParameter("year", year);
            boolean isResultSet = storedProcedureQuery.execute();

            if (isResultSet) {
                List<BigDecimal> resultList = storedProcedureQuery.getResultList();
                for (BigDecimal result : resultList) {
                    TotalTuitionFeeDTO totalTuitionFeeDTO = new TotalTuitionFeeDTO(result);
                    totalTuitionFeeDTOS.add(totalTuitionFeeDTO);
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

        return totalTuitionFeeDTOS;
    }

}
