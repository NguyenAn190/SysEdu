package org.example.sysedu.dto.students;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class StudentRankDTO {
    private Integer rankCount;
    private String ranks;
}
