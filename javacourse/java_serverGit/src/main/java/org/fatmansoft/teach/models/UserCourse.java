package org.fatmansoft.teach.models;

import org.fatmansoft.teach.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Map;
import java.util.Optional;

@Entity
@Table(	name = "user_course",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id","course_id"}),
        })
public class UserCourse {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "course_id")
    private Integer courseId;
    @Column(name = "student_id")
    private Integer studentId;
    @Column(name = "person_id")
    private Integer personId;
    @Column(name = "course_score")
    private String courseScore;
    @ManyToOne
    @JoinColumn(name = "course_id", insertable = false, updatable = false)
    private Course course;
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "student_id",insertable = false, updatable = false)
    private Student student;
    @ManyToOne
    @JoinColumn(name = "person_id",insertable = false, updatable = false)
    private Person person;
    public UserCourse(Map data ) {

        this.userId = Integer.parseInt(data.get("userId").toString());
        //this.studentId = Integer.parseInt(data.get("studentId").toString());
        if (data.get("courseId")!=null){
            this.courseId = Integer.parseInt(data.get("courseId").toString());
            //personId = person.getPersonId();
        this.student = student;
        }
    }
    public Student getStudent() {
        return student;
    }
    public Person getPerson() {

        person = user.getPerson();
        return person;
    }
    public UserCourse() {

    }
    public Course getCourse() {
        return course;
    }

    public Integer getUserId() {
        return userId;
    }
    public User getUser(){
        return user;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getCourseId() {
        return courseId;
    }
    public void setcourseScore(String courseScore) {
        this.courseScore = courseScore;
    }
    public String getcourseScore() {
        return courseScore;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }
}
