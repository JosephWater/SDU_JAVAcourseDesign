package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.InnovativePractice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InnovativePracticeRepository extends JpaRepository<InnovativePractice,Integer> {

    @Query(value = "select max(innovativePracticeId) from InnovativePractice  ")
    Integer getMaxId();
//    Optional<InnovativePractice> findByPersonPersonId(Integer personId);
//    Optional<InnovativePractice> findByPersonNum(String num);
//    List<InnovativePractice> findPersonName(String name);
    List<InnovativePractice> findByIpkinds(String ipkinds);
    Optional<InnovativePractice> findByIpnum(String ipnum);
    List<InnovativePractice> findByStudentStudentId(Integer studentId);
    @Query(value = "from InnovativePractice where ?1='' or ipnum like %?1% or ipname like %?1% ")
    List<InnovativePractice> findInnovativePracticeListByIpnumIpname(String ipnumIpname);
    @Query(value= "from InnovativePractice where studentId=?1 ")
    List<InnovativePractice> findListByStudent(Integer studentId);

//     @Query(value = "select t.*, d.label as ipkinds from innovative_practice t left join dictionary d on d.pid = 4 and d.value = t.ipkinds where '?1' = '' or ipnum like '%?1%' or ipname like '%?1%' ", nativeQuery = true)
//     List<InnovativePractice> findInnovativePracticeListByIpnumIpname2(String ipnumIpname);

//    @Query(value = "from InnovativePractice  where ?1='' or person.num like %?1% or person.name like %?1% ")
//    List<InnovativePractice> findInnovativePracticeListByNumName(String numName);



//     @Modifying
//     @Transactional
//     @Query(value = "", nativeQuery = true)
//     void DeleteStudentInnovativePracticeRelation(Integer innovativePracticeId, Integer studentId);


}
