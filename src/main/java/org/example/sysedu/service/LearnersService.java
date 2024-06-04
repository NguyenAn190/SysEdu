package org.example.sysedu.service;

import org.example.sysedu.dto.learners.LearnersDTO;
import org.example.sysedu.repository.LearnersRepository;
import org.example.sysedu.entity.Learners;
import org.example.sysedu.exception.NotFoundException;
import org.example.sysedu.utils.GenericMapper;

import java.time.LocalDate;
import java.util.*;

public class LearnersService {
    LearnersRepository learnersRepository = new LearnersRepository();

    public boolean existsEmail(String id) {
        return learnersRepository.existsEmailLearners(id);
    }

    public boolean existsPhone(String id) {
        return learnersRepository.existsPhoneLearners(id);
    }

    public void save(Learners learners) {
        learnersRepository.save(learners);
    }


    public Integer findCountLearners() {
        return learnersRepository.findCountLearners();
    }
    public Integer findCountLearnersByYear() {
        return learnersRepository.findCountLearnersByYear(LocalDate.now().getYear());
    }
    public List<Learners> findAll() {
        return learnersRepository.findAll();
    }

    public List<Learners> findAllByFilters(String searchFilter, Boolean status) {
        return learnersRepository.findAllByFilters(searchFilter, status);
    }

    public List<Integer> statisticsOnThisYearLearners() {
        int year = LocalDate.now().getYear();

        List<Learners> learners = learnersRepository.findAllLearnersByYearCreated(year);

        List<Integer> countsLearnersByMonth = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            countsLearnersByMonth.add(0);
        }

        for (Learners learner : learners) {
            LocalDate dateCreated = learner.getDateCreated().toLocalDateTime().toLocalDate();
            int monthValue = dateCreated.getMonthValue();
            countsLearnersByMonth.set(monthValue - 1, countsLearnersByMonth.get(monthValue - 1) + 1);
        }

        return countsLearnersByMonth;
    }

    public void update(Learners learners) {
       learnersRepository.update(learners);
    }

    public void updateStatusDeleteTrue(String learnersId) {
        Learners learners = learnersRepository.findById(learnersId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người học " + learnersId));
        learnersRepository.updateStatusDeleteTrue(learnersId);
    }

    public void updateStatusDeleteFalse(String learnersId) {
        Learners learners = learnersRepository.findById(learnersId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người học " + learnersId));
        learnersRepository.updateStatusDeleteFalse(learnersId);
    }

    public List<LearnersDTO> findAllByStatusDelete() {
        List<Learners> learners = learnersRepository.findAll();
        List<LearnersDTO> learnersDTOS = GenericMapper.toListDTO(learners, LearnersDTO.class);
        return learnersDTOS.stream().filter(learner -> !learner.getIsDelete()).toList();
    }

    public String findMaxId () {
        Optional<Learners> courses = learnersRepository.findByMaxId();
        return courses.map(Learners::getId).orElse(null);
    }
}
