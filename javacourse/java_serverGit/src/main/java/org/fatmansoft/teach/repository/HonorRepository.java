package org.fatmansoft.teach.repository;
import org.fatmansoft.teach.models.Honor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
/**
 * Honor 数据操作接口，主要实现Person数据的查询操作
 * Integer getMaxId()  Honor 表中的最大的honor_id;    JPQL 注解
 * Optional<Honor> findByHonorIdAndDay(Integer studentId);  根据honor_id  查询获得Option<Honor>对象,  命名规范
 * List<Honor> findListByStudent(Integer studentId);  查询学生（student_id）所有的荣誉信息  JPQL 注解
 */

public interface HonorRepository extends JpaRepository<Honor, Integer> {
    @Query(value = "select max(honorId) from Honor  ")
    Integer getMaxId();

    @Query(value = "from Honor h where ?1='' or h.student.person.num like %?1%")
    List<Honor> findByStudentNum(String num);

    @Query(value = "from Honor h where ?1='' or h.student.person.num like %?1% or h.student.person.name like %?1% ")
    List<Honor> findHonorListByNumName(String numName);




}