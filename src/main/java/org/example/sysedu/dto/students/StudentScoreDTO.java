package org.example.sysedu.dto.students;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class StudentScoreDTO {
    private Integer id;
    private String fullName;
    private Float score;
    private String ranks;

}
