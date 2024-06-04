package org.example.sysedu.dto.topics;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TopicScoreDTO {
    private String id;
    private String topicName;
    private Integer countStudent;
    private Float averageScore;
    private Float highestScore;
    private Float lowestScore;
}
