package org.example.sysedu.service;

import org.example.sysedu.dto.topics.*;
import org.example.sysedu.repository.TopicsRepository;
import org.example.sysedu.entity.Topics;
import org.example.sysedu.exception.NotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TopicsService {
    TopicsRepository topicsRepository = new TopicsRepository();
    public void save(Topics topic) {
        topicsRepository.save(topic);
    }

    public void update(Topics topic) {
        topicsRepository.update(topic);
    }

    public void delete(Topics topic) {
        topicsRepository.delete(topic);
    }

    public Topics findById(String id) {
        return topicsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy mã chuyên dề"));
    }

    public List<Topics> findAll() {
        return topicsRepository.findAll();
    }

    public List<Topics> findAllByFilter(String search, Boolean isDetele) {
        return topicsRepository.findAllByFilter(search, isDetele);
    }

    public List<Integer> statisticsOnThisYearCreatedTopics() {
        int year = LocalDate.now().getYear();
        List<Topics> topics = topicsRepository.findAllTopicsByYearCreated(year);

        List<Integer> countsLearnersByMonth = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            countsLearnersByMonth.add(0);
        }

        for (Topics topic : topics) {
            LocalDate dateCreated = topic.getDateCreated().toLocalDateTime().toLocalDate();
            int monthValue = dateCreated.getMonthValue();
            countsLearnersByMonth.set(monthValue - 1, countsLearnersByMonth.get(monthValue - 1) + 1);
        }

        return countsLearnersByMonth;
    }

    public Integer findCountTopicsByYear(){
        int year = LocalDate.now().getYear();
        return topicsRepository.findCountTopicsByYear(year);
    }

    public Integer findCountTopics() {
        return topicsRepository.findCountTopics();
    }


    public String findMaxId() {
        Optional<Topics> topics = topicsRepository.findMaxId();
        return topics.map(Topics::getId).orElse(null);
    }

    public void updateStatusDeleteTrueById(String id) {
        topicsRepository.updateStatusDeleteTrueById(id);
    }

    public void updateStatusDeleteFalseById(String id) {
        topicsRepository.updateStatusDeleteFalseById(id);
    }

    public String findByName(String topicName) {
        Topics courses = topicsRepository.findByName(topicName).orElseThrow(() -> new NotFoundException("Không tìm thấy chuyên đề " + topicName));;
        return courses.getId();
    }

    public Optional<Topics> findTopicByName(String topicName) {
        return topicsRepository.findByName(topicName);
    }

    public Topics findTopicsByName(String topicName){
        return topicsRepository.findByName(topicName).orElseThrow(() -> new NotFoundException("Không tìm thấy chuyên đề" + topicName));
    }

    public List<String> findAllByIsDeleteFalse() {
        List<Topics> topics = topicsRepository.findAllByFilter("", false);

        return topics.stream().map(Topics::getTopicName).toList();
    }

    public List<TopicScoreDTO> getScoreTopics(String searchParam) {
        return topicsRepository.getScoreTopics(searchParam);
    }

    public List<HighestTopicScoreDTO> getTopScoreTopic(String searchParam) {
        return topicsRepository.getTopScoreTopic(searchParam);
    }

    public List<RevenueStatisticsDTO> getRevenueStatistic(LocalDate timeStart, LocalDate timeEnd) {
        return topicsRepository.getRevenueStatistic(timeStart, timeEnd);
    }

    public List<TopTopicHotDTO> getTop5TopicHot() {
        return topicsRepository.getTop5TopicHot();
    }

    public List<TotalTuitionFeeDTO> getTotalTuitionFeeInYear(Integer year) {
        return topicsRepository.getTotalTuitionFeeInYear(year);
    }
}
