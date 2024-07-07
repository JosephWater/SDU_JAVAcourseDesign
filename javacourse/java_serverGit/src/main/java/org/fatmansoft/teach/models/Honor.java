package org.fatmansoft.teach.models;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(    name = "honor" ,
        uniqueConstraints = {
        })

public class Honor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer honorId;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private String honorName;
    private String honorGrade;
    private String honorGetTime;
    private String honorType;




    public Integer getHonorId() { return honorId; }
    public void setHonorId(Integer honorId) {
        this.honorId = honorId;
    }

    public Student getStudent() {
        return student;
    }
    public void setStudent(Student student) {
        this.student = student;
    }

    public String getHonorName() { return honorName; }
    public void setHonorName(String honorName) {
        this.honorName = honorName;
    }

    public String getHonorGrade() {
        return honorGrade;
    }
    public void setHonorGrade(String honorGrade) {
        this.honorGrade = honorGrade;
    }

    public String getHonorGetTime() { return honorGetTime; }
    public void setHonorGetTime(String honorDay) { this.honorGetTime = honorDay; }

    public String getHonorType() {
        return honorType;
    }

    public void setHonorType(String honorType) {
        this.honorType = honorType;
    }
}

