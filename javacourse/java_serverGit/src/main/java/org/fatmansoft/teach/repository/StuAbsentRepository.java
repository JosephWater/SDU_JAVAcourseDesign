package org.fatmansoft.teach.repository;
import org.fatmansoft.teach.models.StuAbsent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface StuAbsentRepository extends JpaRepository<StuAbsent,Integer> {
    @Query(value = "select max(studentabsentId) from StuAbsent")
    Integer getMaxId();
    @Query(value = "from StuAbsent sl where ?1='' or sl.student.person.num like %?1% ")
    List<StuAbsent> findByStudentNum(String num);
    @Query(value = "from StuAbsent sl where ?1='' or sl.student.person.num like %?1% ")
    Optional<StuAbsent> findByStudentStudentNum(String num);
    @Query(value = "from StuAbsent sl where ?1='' or sl.student.person.num like %?1% or sl.student.person.name like %?1% ")
    List<StuAbsent> findStuAbsentListByNumName(String numName);
    @Query(value = "from StuAbsent sl  where ?1='' or sl.absent.absentReason like %?1% ")
    Optional<StuAbsent> findStuAbsentByAbsentReason(String absentReason);
    @Query("SELECT s.absent.absentId FROM StuAbsent s WHERE s.studentabsentId = ?1")
    Integer findAbsentIDByStudentabsentID(Integer studentabsentID);


}
