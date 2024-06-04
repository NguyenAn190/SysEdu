package org.example.sysedu.dto.topics;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AvgScoreInTopicDTO {
    private String id;
    private Float averageScore;
}
