package org.example.sysedu.dto.courses;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CoursesDTO {
    private String id;
    private String courseName;
    private Integer yearCreated;
    private Integer countStudent;
    private Timestamp minDate;
    private Timestamp maxDate;
}
