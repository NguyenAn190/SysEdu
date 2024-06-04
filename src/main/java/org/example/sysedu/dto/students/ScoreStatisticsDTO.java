package org.example.sysedu.dto.students;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ScoreStatisticsDTO {
    private Integer count;
    private Integer score;
}
