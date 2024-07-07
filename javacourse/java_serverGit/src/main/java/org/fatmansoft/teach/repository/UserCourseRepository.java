package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.StuAbsent;
import org.fatmansoft.teach.models.User;
import org.fatmansoft.teach.models.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, Integer> {
    @Transactional
    int deleteByUserIdAndCourseId(Integer userId,Integer courseId);
    List<UserCourse> findAllByUserId(Integer userId);
    @Query(value = "from UserCourse sl where ?1='' or sl.student.person.num like %?1% or sl.student.person.name like %?1% ")
    List<UserCourse> findUserCourseListByNumName(String numName);
    UserCourse findByUserIdAndCourseId(Integer userId,Integer courseId);
    @Query("from UserCourse")
    List<UserCourse> findAllUserCourses();
}
