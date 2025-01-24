package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RulesDTO {

    private Long ruleId;
    private String ruleName;
    private String status;
    private Integer createdBy;
    private LocalDateTime createdDate;
    private Integer updatedBy;
    private LocalDateTime updatedDate;
    private Boolean active = true;
}
