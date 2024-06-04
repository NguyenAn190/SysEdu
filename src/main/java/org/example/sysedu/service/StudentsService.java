package org.example.sysedu.service;

import org.example.sysedu.dto.students.ScoreStatisticsDTO;
import org.example.sysedu.dto.students.StudentRankDTO;
import org.example.sysedu.dto.students.StudentScoreDTO;
import org.example.sysedu.entity.Students;
import org.example.sysedu.repository.StudentsRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentsService {
    StudentsRepository studentsRepository = new StudentsRepository();

    public void save(Students students) {
        studentsRepository.save(students);
    }

    public void update(Students students) {
        studentsRepository.update(students);
    }
    public void delete(Integer id) {
        studentsRepository.delete(id);
    }

    public Students findById(Integer id) {
        return studentsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy mã sinh viên"));
    }

    public List<Students> findByFilter(String search, String courseId, String learnersId) {
        return studentsRepository.findByFilter(search, courseId, learnersId);
    }

    public Integer findCountStudent() {
        return studentsRepository.countStudent();
    }

    public Integer findCountStudentByYear () {
        Integer year = LocalDate.now().getYear();
        return studentsRepository.countStudentByYear(year);
    }

    public List<Students> findAll() {
        return studentsRepository.findAll();
    }

    public List<Integer> statisticsOnThisYearStudent() {
        int year = LocalDate.now().getYear();

        List<Students> students = studentsRepository.findAllStudentByYearCreated(year);

        List<Integer> countsStudentsByMonth = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            countsStudentsByMonth.add(0);
        }

        for (Students learner : students) {
            LocalDate dateCreated = learner.getDateCreated().toLocalDateTime().toLocalDate();
            int monthValue = dateCreated.getMonthValue();
            countsStudentsByMonth.set(monthValue - 1, countsStudentsByMonth.get(monthValue - 1) + 1);
        }

        return countsStudentsByMonth;
    }

    public List<StudentScoreDTO> getStudentScores(String searchParams, String topicNameParam, String courseNameParam) {
        return studentsRepository.getStudentScores(searchParams, topicNameParam, courseNameParam);
    }

    public List<StudentRankDTO> getStudentRank(String courseName) {
        return studentsRepository.getStudentRank(courseName);
    }


    public List<ScoreStatisticsDTO> getCountScore(String courseName) {
        return studentsRepository.getCountScore(courseName);
    }
}
