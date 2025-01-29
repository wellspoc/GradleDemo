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
@Table(name = "task")
public class Tasks {

    @Id
    @Column(name = "taskId")
    public Long taskId;

    @Column(name = "owner")
    public String owner;

    @Column(name = "creator")
    public String creator;

    @Column(name = "status")
    public String status;

    @Column(name = "role")
    public String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @ManyToOne
    @JoinColumn(name = "processId", referencedColumnName = "processId")
    public ProcessInstance process;

    @Column(name = "CreatedDate")
    public LocalDateTime createdDate;

    @Column(name = "UpdatedDate")
    public LocalDateTime updatedDate;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ProcessInstance getProcess() {
        return process;
    }

    public void setProcess(ProcessInstance process) {
        this.process = process;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }
}
