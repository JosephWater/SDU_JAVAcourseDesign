
package org.fatmansoft.teach.models;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * DailyActivities 日常活动表实体类  保存日常活动的基本信息信息，
 * Integer dailyActivitiesId 人员表 dailyActivities  主键 dailyActivities _id
 * Student student 关联学生 student_id 关联学生的主键 student_id
 * String ipnum 日常活动编号
 *
 */
@Entity
@Table(	name = "DailyActivities",
        uniqueConstraints = {
        })
public class DailyActivities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer DailyActivitiesId;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @ManyToOne
    @JoinColumn(name="person_id")
    private Person person;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    private String ipnum;
    @Size(max = 7)
    private String ipkinds;
    private String ipname;
    private String ipplace;
    private String iptime;
    public Integer getDailyActivitiesId() {
        return DailyActivitiesId;
    }

    public void setDailyActivitiesId(Integer Did) {
        this.DailyActivitiesId = Did;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getIpnum() {
        return ipnum;
    }

    public void setIpnum(String ipnum) {
        this.ipnum = ipnum;
    }

    public String getIpkinds() {
        return ipkinds;
    }

    public void setIpkinds(String ipkinds) {
        this.ipkinds = ipkinds;
    }
    public String getIpname() {
        return ipname;
    }

    public void setIpname(String ipname) {
        this.ipname = ipname;
    }

    public String getIpplace() {
        return ipplace;
    }

    public void setIpplace(String ipplace) {
        this.ipplace = ipplace;
    }

    public String getIptime() {
        return iptime;
    }

    public void setIptime(String iptime) {
        this.iptime = iptime;
    }


}