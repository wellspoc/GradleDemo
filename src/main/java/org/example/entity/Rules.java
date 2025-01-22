package org.example.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "Rules")
@Getter
@Setter
public class Rules {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RuleId")
    private Long ruleId;

    @Column(name = "RuleName", nullable = false, length = 255)
    private String ruleName;

    @Column(name = "Status", nullable = false)
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "CreatedBy", referencedColumnName = "userId")
    private Users createdBy;

    @Column(name = "CreatedDate", nullable = false)
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "UpdatedBy", referencedColumnName = "userId")
    private Users updatedBy;

    @Column(name = "UpdatedDate")
    private LocalDateTime updatedDate;

    @Column(name = "IsActive", nullable = false)
    private Boolean isActive = true;

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Users getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Users createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Users getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Users updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
