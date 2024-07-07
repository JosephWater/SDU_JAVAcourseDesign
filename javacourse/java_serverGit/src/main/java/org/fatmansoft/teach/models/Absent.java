package org.fatmansoft.teach.models;
import javax.persistence.*;
import java.util.List;

/**请假功能
 * absentId 主键
 * Person person 关联到该用户所用的Person对象，账户所对应的人员信息 person_id 关联 person 表主键 person_id
 * String reason 请假理由
 * LocalDate startDate 请假开始日期
 * LocalDate endDate 请假结束日期
 * String teacherName 审批教师
 * String teacherPhone 审批教师电话
 * Integer status 申请状态
 */

@Entity
@Table(	name = "Absent")
public class Absent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer absentId;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "absent_id")
    private List<StuAbsent> studentAbsent;
    private String startDate;
    private String endDate;
    private String absentType;
    private String absentReason;
    private String examine;
    public String absentreq;
    public Integer getAbsentId() {
        return absentId;
    }
    public void setAbsentId(Integer absentId) {
        this.absentId = absentId;
    }
    public String getAbsentReq() {
        return absentreq;
    }

    public void setAbsentReq(String absentreq) {
        this.absentreq = absentreq;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getAbsentType() {
        return absentType;
    }

    public void setAbsentType(String absentType) {
        this.absentType = absentType;
    }


    public String getAbsentReason() {
        return absentReason;
    }

    public void setAbsentReason(String absentReason) {
        this.absentReason = absentReason;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
    public List<StuAbsent> getStudentAbsent() {
        return studentAbsent;
    }

    public void setStudentAbsent(List<StuAbsent> studentAbsent ) {
        this.studentAbsent = studentAbsent ;
    }
    public void setExamine(String examine) {
        this.examine = examine;
    }

    public String getExamine() {
        return examine;
    }

}
