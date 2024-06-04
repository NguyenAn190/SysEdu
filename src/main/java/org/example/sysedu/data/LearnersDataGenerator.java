package org.example.sysedu.data;

import org.example.sysedu.repository.LearnersRepository;
import org.example.sysedu.entity.Learners;
import org.example.sysedu.service.LearnersService;
import com.github.javafaker.Faker;
import org.example.sysedu.utils.GenerateID;

import java.sql.Timestamp;
import java.time.*;
import java.util.concurrent.ThreadLocalRandom;

public class LearnersDataGenerator {
    private static final int MIN_LEARNERS_COUNT = 100;

    public void generateFakeLearnersData(int numberOfLearners) {
        LearnersService learnersService = new LearnersService();
        int currentLearnersCount = learnersService.findCountLearners();
        if (currentLearnersCount >= MIN_LEARNERS_COUNT) {
            return;
        }

        Faker faker = new Faker();

        for (int i = 0; i < numberOfLearners; i++) {
            String fullName = faker.name().fullName();
            String gender = faker.demographic().sex();
            LocalDate birthDate = faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String phone = generateVietnamesePhoneNumber();
            String email = faker.internet().emailAddress();
            String note = faker.lorem().sentence();
            LocalDateTime randomDateCreated = generateRandomDateFrom2023ToMay2024();

            learnersService.save(Learners.builder()
                    .id(GenerateID.generateID("S", learnersService.findMaxId()))
                    .fullName(fullName)
                    .gender(gender.equals("Male"))
                    .birthDate(birthDate)
                    .phone(phone)
                    .email(email)
                    .note(note)
                    .dateCreated(Timestamp.valueOf(randomDateCreated))
                    .isDelete(false)
                    .build());
        }

        System.out.println(numberOfLearners + " Đã thêm dữ liệu");
    }

    private LocalDateTime generateRandomDateFrom2023ToMay2024() {
        LocalDate start = LocalDate.of(2023, Month.JANUARY, 1);
        LocalDate end = LocalDate.of(2024, Month.MAY, LocalDate.now().getDayOfMonth());

        long startEpochSecond = start.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
        long endEpochSecond = end.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();

        long randomEpochSecond = ThreadLocalRandom.current().nextLong(startEpochSecond, endEpochSecond);

        return LocalDateTime.ofEpochSecond(randomEpochSecond, 0, ZoneOffset.UTC);
    }


    private String generateVietnamesePhoneNumber() {
        String[] prefixes = {"09", "08", "07", "05", "03"};
        String prefix = prefixes[ThreadLocalRandom.current().nextInt(prefixes.length)];
        StringBuilder phoneNumber = new StringBuilder(prefix);
        for (int i = 0; i < 8; i++) {
            int digit = ThreadLocalRandom.current().nextInt(10);
            phoneNumber.append(digit);
        }
        return phoneNumber.toString();
    }
}
