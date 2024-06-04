package org.example.sysedu.service;

import org.example.sysedu.dto.courses.CoursesDTO;
import org.example.sysedu.exception.NotFoundException;
import org.example.sysedu.repository.CourseRepository;
import org.example.sysedu.entity.Courses;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseService {
    CourseRepository courseRepository = new CourseRepository();

    public void save(Courses courses) {
        courseRepository.save(courses);
    }

    public void update(Courses courses) {
        courseRepository.update(courses);
    }

    public void delete(Courses courses) {
        courseRepository.delete(courses);
    }

    public Optional<Courses> findById(String id) {
        return courseRepository.findById(id);
    }

    public List<Courses> findAll() {
        return courseRepository.findAll();
    }

    public List<Courses> findAllByFilter(String search, String isDelete, String topicId) {
        Boolean status = null;

        if (isDelete != null) {
            if (isDelete.equals("Đang hoạt động")) {
                status = false;
            } else if (isDelete.equals("Đã xóa")) {
                status = true;
            }
        }

        return courseRepository.findAllByFilter(search, status, topicId);
    }


    public Integer findAllCourseByYearCreate() {
        int year = LocalDate.now().getYear();
        return courseRepository.findCountCourersByYear(year);
    }

    public Integer findCountCourse() {
        return courseRepository.findCountCourers();
    }

    public String findMaxId() {
        Optional<Courses> courses = courseRepository.findMaxId();
        return courses.map(Courses::getId).orElse(null);
    }

    public List<Integer> statisticsOnThisYearLearners() {
        int year = LocalDate.now().getYear();

        List<Courses> courers = courseRepository.findAllCourseByYearCreate(year);

        List<Integer> countsLearnersByMonth = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            countsLearnersByMonth.add(0);
        }

        for (Courses item : courers) {
            LocalDate dateCreated = item.getDateCreated().toLocalDateTime().toLocalDate();
            int monthValue = dateCreated.getMonthValue();
            countsLearnersByMonth.set(monthValue - 1, countsLearnersByMonth.get(monthValue - 1) + 1);
        }

        return countsLearnersByMonth;
    }

    public void updateStatusDeleteTrue(String id) {
        courseRepository.updateStatusDeleteTrue(id);
    }

    public void updateStatusDeleteFalse(String id) {
        courseRepository.updateStatusDeleteFalse(id);
    }

    public List<String> findAllByTopicNameAndIsDelete(String topicName) {
        List<Courses> courses = courseRepository.findAllByTopicNameAndIsDelete(topicName, false);

        return courses.stream().map(Courses::getCourseName).toList();
    }

    public Courses findCourseByName(String string) {
        return courseRepository.findByCourseName(string).orElseThrow(() -> new NotFoundException("Không tìm thấy khóa học" + string));
    }

    public List<String> findCourseNameByTopicId(String topicId) {
        List<Courses> courses = courseRepository.findCourseByTopicId(topicId);
        return courses.stream().map(Courses::getCourseName).toList();
    }

    public List<CoursesDTO> getCourse(Integer year) {
        return courseRepository.getCourse(year);
    }

    public List<Integer> getCourseInYear(Integer year) {
        return courseRepository.getCourseInYear(year);
    }
}
