package org.example.sysedu.dto.topics;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RevenueStatisticsDTO {
    private String id;
    private String topicName;
    private Integer countStudent;
    private Integer courseCount;
    private BigDecimal sumTuitionFee;
    private BigDecimal minTuitionFee;
    private BigDecimal averageTuitionFee;
    private BigDecimal maxTuitionFee;
}
