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
@Table(name = "process_instance")
@Getter
@Setter
public class ProcessInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "processId")
    public Long processId;

    @Column(name = "status")
    public Integer status;

    @ManyToOne
    @JoinColumn(name = "ruleId", referencedColumnName = "ruleId")
    public Rules rule;

    @Column(name = "CreatedDate", nullable = false)
    public LocalDateTime createdDate;

    @Column(name = "UpdatedDate")
    public LocalDateTime updatedDate;
}