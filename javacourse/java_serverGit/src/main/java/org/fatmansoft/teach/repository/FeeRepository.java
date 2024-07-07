package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Fee;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FeeRepository extends JpaRepository<Fee,Integer> {
    List<Fee> findByStudentStudentId(Integer studentId) ;

    @Query(value = "select max(feeId) from Fee  ")
    Integer getMaxId();

    Optional<Fee>findByNum(String num);
    List<Fee>findByName(String name);

//    @Query(value = "from Fee c  where ?1='' or c.student.person.num like %?1% or c.student.person.name like %?1% ")
//    List<Fee> findFeeListByNumName(String numName);



    Optional<Fee> findByStudentStudentIdAndDay(Integer studentId, String day);
    @Query(value = "from Fee where ?1='' or num like %?1% or name like %?1%")
    List<Fee> findFeeListByNumName(String numName);
}