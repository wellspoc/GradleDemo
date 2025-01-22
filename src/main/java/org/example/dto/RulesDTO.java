package org.example.dto;


import java.time.LocalDateTime;


public class RulesDTO {

    private Long ruleId;
    private String ruleName;
    private String status;
    private Integer createdBy;
    private LocalDateTime createdDate;
    private Integer updatedBy;
    private LocalDateTime updatedDate;
    private Boolean active= true;

    public Long getRuleId() {
        return ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public String getStatus() {
        return status;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
