package org.example.sysedu.dto.topics;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class HighestTopicScoreDTO {
    private String id;
    private String topicName;
    private Integer countStudent;
    private Float averageScore;
}
