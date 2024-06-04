package org.example.sysedu.service;

import org.example.sysedu.entity.Topics;
import org.example.sysedu.entity.Users;
import org.example.sysedu.enums.Role;
import org.example.sysedu.repository.UsersRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsersService {
    UsersRepository usersRepository = new UsersRepository();

    public void save(Users users) {
        usersRepository.create(users);
    }

    public void update(Users users) {
        usersRepository.update(users);
    }

    public void delete(String id) {
        usersRepository.delete(id);
    }

    public List<Users> findAll() {
        return usersRepository.findAll();
    }

    public Integer findCountUsers() {
        return usersRepository.findCountUsers();
    }

    public Integer findCountUserByYearCreate() {
        int year = LocalDate.now().getYear();
        return usersRepository.findCountUsersByYear(year);
    }

    public List<Integer> statisticsOnThisYearUsers() {
        int year = LocalDate.now().getYear();
        List<Users> users = usersRepository.findAllUsersByYearCreated(year);

        List<Integer> countsUsersByMonth = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            countsUsersByMonth.add(0);
        }

        for (Users user : users) {
            LocalDate dateCreated = user.getDateCreated().toLocalDateTime().toLocalDate();
            int monthValue = dateCreated.getMonthValue();
            countsUsersByMonth.set(monthValue - 1, countsUsersByMonth.get(monthValue - 1) + 1);
        }

        return countsUsersByMonth;
    }

    public List<Users> findAllByFilter(String searchText, String status) {
        Boolean isDelete = null;
        if (status.equals("Đang hoạt động")) {
            isDelete = false;
        } else {
            isDelete = true;
        }
        return usersRepository.findAllByFilter(searchText, isDelete);
    }

    public String findMaxId() {
        Optional<Users> topics = usersRepository.findMaxId();
        return topics.map(Users::getId).orElse(null);
    }

    public void updateStatusDeleteTrue(String id) {
        usersRepository.updateUsersIsDeleteTrue(id);
    }

    public void updateStatusDeleteFalse(String id) {
        usersRepository.updateUsersIsDeleteFalse(id);
    }

    public Optional<Users> findByUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    public List<Users> findAllByRole(Role role) {
        return usersRepository.findAllByRole(role);
    }
}
