package org.fatmansoft.teach.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.util.Map;

/**
 * Course 课程表实体类  保存课程的的基本信息信息，
 * Integer courseId 人员表 course 主键 course_id
 * String num 课程编号
 * String name 课程名称
 * Integer credit 学分
 * Course preCourse 前序课程 pre_course_id 关联前序课程的主键 course_id
 */
@Entity
@Table(	name = "course",
        uniqueConstraints = {
        })
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseId;
    @NotBlank
    @Size(max = 20)
    private String num;

    @Size(max = 50)
    private String name;
    private Integer credit;

    private String coursePlace;

    private String courseTime;
    private Integer courseYes ;
    private String courseScore ;
    @ManyToOne
    @JoinColumn(name="pre_course_id")
    private Course preCourse;
    public Course(){
    }
    public Course(Map data) {
        if (data.get("courseId")!=null){
            this.courseId = Integer.parseInt(data.get("courseId").toString());
        }
        if (data.get("num")!=null){
            this.num = String.valueOf(data.get("num"));
        }
        if (data.get("name")!=null){
            this.name = String.valueOf(data.get("name"));
        }
        if (data.get("credit")!=null){
            this.credit = Integer.parseInt(data.get("credit").toString());
        }
        if (data.get("coursePlace")!=null){
            this.coursePlace = String.valueOf(data.get("coursePlace"));
        }
        if (data.get("courseTime")!=null){
            this.courseTime = String.valueOf(data.get("courseTime"));
        }
        if (data.get("courseYes")!=null){
            this.courseYes = Integer.parseInt(data.get("courseYes").toString());
        }

    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public String getCoursePlace() {
        return coursePlace;
    }

    public void setCoursePlace(String coursePlace) {
        this.coursePlace = coursePlace;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    public Course getPreCourse() {
        return preCourse;
    }

    public void setPreCourse(Course preCourse) {
        this.preCourse = preCourse;
    }
    public Integer getcourseYes() {
        return courseYes;
    }

    public void setcourseYes(Integer courseYes) {
        this.credit = courseYes;
    }
    public String getCourseScore() {
        return courseScore;
    }

    public void setCourseScore(String courseScore) {
        this.courseScore = courseScore;
    }


    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", num='" + num + '\'' +
                ", name='" + name + '\'' +
                ", credit=" + credit +
                ", coursePlace='" + coursePlace + '\'' +
                ", courseTime='" + courseTime + '\'' +
                ", preCourse=" + preCourse +'\'' +
                ", courseScore=" + courseScore +
                '}';
    }
}
