package org.fatmansoft.teach.repository;
import org.fatmansoft.teach.models.DailyActivities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import  java.util.List;
import java.util.Optional;
public interface DailyActivitiesRepository extends JpaRepository<DailyActivities,Integer> {

    @Query(value = "select max(DailyActivitiesId) from DailyActivities  ")
    Integer getMaxId();
    //    Optional<InnovativePractice> findByPersonPersonId(Integer personId);
//    Optional<InnovativePractice> findByPersonNum(String num);
//    List<InnovativePractice> findPersonName(String name);
    List<DailyActivities> findByIpkinds(String ipkinds);
    Optional<DailyActivities> findByIpnum(String ipnum);
    List<DailyActivities> findByStudentStudentId(Integer studentId);
    @Query(value = "from DailyActivities where ?1='' or ipnum like %?1% or ipname like %?1% ")
    List<DailyActivities> findCreateListByIpnumIpname(String ipnumIpname);



}
