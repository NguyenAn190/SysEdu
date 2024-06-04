package org.example.sysedu.dto.topics;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CountStudentInTopicDTO {
    private Integer countStudent;
    private String topicName;
}
