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

@Getter
@Setter
@Entity
@Table(name = "task")
public class Tasks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taskId")
    public Long taskId;

    @Column(name = "owner")
    public Integer owner;

    @Column(name = "creator")
    public Integer creator;

    @Column(name = "status")
    public Integer status;

    @ManyToOne
    @JoinColumn(name = "processId", referencedColumnName = "processId")
    public ProcessInstance process;

    @Column(name = "CreatedDate")
    public LocalDateTime createdDate;

    @Column(name = "UpdatedDate")
    public LocalDateTime updatedDate;
}
