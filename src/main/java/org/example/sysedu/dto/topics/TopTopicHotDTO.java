package org.example.sysedu.dto.topics;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TopTopicHotDTO {
    private String id;
    private String topicName;
    private Integer countStudent;
    private Integer countCourse;
    private BigDecimal totalTuitionFee;
}
