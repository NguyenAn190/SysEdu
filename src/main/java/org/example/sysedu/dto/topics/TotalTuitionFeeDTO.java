package org.example.sysedu.dto.topics;

import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TotalTuitionFeeDTO {
    private BigDecimal tuitionFee;
}
