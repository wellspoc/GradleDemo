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
public class RuleDataDTO {

    private Long id;
    private String tableName;
    private String columnName;
    private String whereClause;
    private String sqlQuery;
    private Long ruleId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}