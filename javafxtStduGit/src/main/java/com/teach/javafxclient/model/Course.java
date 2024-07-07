package com.teach.javafxclient.model;

public class Course {

    private Integer courseId;
    private String num;

    private String name;
    private Integer credit;

    private String coursePlace;

    private String courseTime;
    private String courseScore;

    public Integer getCourseId() {
        return courseId;
    }

    public Course() {
    }

    public Course(Float courseId, String num, String name, Float credit, String coursePlace, String courseTime) {
        this.courseId = courseId.intValue();
        this.num = num;
        this.name = name;
        this.credit = credit.intValue();
        this.coursePlace = coursePlace;
        this.courseTime = courseTime;
        //this.courseScore = courseScore;

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
                '}';
    }

}
