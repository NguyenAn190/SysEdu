package org.example.sysedu.utils;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@ToString
@Builder
public class StatusItem {
    private final String displayValue;
    private final Boolean actualValue;

}
