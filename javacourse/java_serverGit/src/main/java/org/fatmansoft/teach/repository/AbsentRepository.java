package org.fatmansoft.teach.repository;
import org.fatmansoft.teach.models.Absent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface AbsentRepository extends JpaRepository<Absent,Integer> {
    @Query(value = "select max(absentId) from Absent ")
    Integer getMaxId();
    @Query(value = "from Absent l where ?1='' or l.student.person.num like %?1% or l.student.person.name like %?1% ")
    List<Absent> findAbsentListByNumName(String numName);
    @Query(value = "from Absent  where ?1='' or absentReason like %?1% ")
    Optional<Absent> findByAbsentReason(String absentReason);
    @Query(value = "from Absent l where ?1='' or l.student.person.num like %?1% ")
    List<Absent> findByStudentStudentNum(String num);
/*    @Query(value = "from Absent l where ?1='' or l.StuAbsent. like %?1% ")
    List<Absent> findByStudentAbsentID(Integer StudentAbsentID);*/

}