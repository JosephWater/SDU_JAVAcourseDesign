package org.fatmansoft.teach.models;
import javax.persistence.*;

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
@Table(	name = "studentabsent")
public class StuAbsent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer studentabsentId;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    @OneToOne
    @JoinColumn(name = "absent_id")
    private Absent absent;

    public Integer getStudentabsentId() {
        return studentabsentId;
    }

    public void setStudentabsentId(Integer studentabsentId) {
        this.studentabsentId = studentabsentId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Absent getAbsent() {
        return absent;
    }

    public void setAbsent(Absent absent) {
        this.absent = absent;
    }
}
