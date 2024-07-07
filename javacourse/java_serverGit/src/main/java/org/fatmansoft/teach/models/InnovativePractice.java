package org.fatmansoft.teach.models;
import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * InnovativePractice 创新实践表实体类  保存创新实践的基本信息信息，
 * Integer innovativePracticeId 人员表 innovativePractice 主键 innovativePractice_id
 * Student student 关联学生 student_id 关联学生的主键 student_id
 * String ipnum 创新实践编号
 *
 */
@Entity
@Table(	name = "innovativePractice",
        uniqueConstraints = {
        })
public class InnovativePractice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer innovativePracticeId;

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
    private String ipnum;//返回创新实践编号
    @Size(max = 7)
    private String ipkinds;//创新实践种类
    private String ipname;//创新实践名称
    private String ipplace;//创新实践地点
    private String iptime;//创新实践时间
    private String iporg;//创新活动组织单位
    private String iphonor;//获得奖项
    private String ipcontent;//活动内容

    public String getIphonor() {
        return iphonor;
    }

    public void setIphonor(String iphonor) {
        this.iphonor = iphonor;
    }

    public String getIpcontent() {
        return ipcontent;
    }

    public void setIpcontent(String ipcontent) {
        this.ipcontent = ipcontent;
    }

    public Integer getInnovativePracticeId() {
        return innovativePracticeId;
    }

    public void setInnovativePracticeId(Integer innovativePracticeId) {
        this.innovativePracticeId = innovativePracticeId;
    }

    public String getIporg() {
        return iporg;
    }

    public void setIporg(String iporg) {
        this.iporg = iporg;
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