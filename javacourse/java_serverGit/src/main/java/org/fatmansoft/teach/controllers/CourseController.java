package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.payload.response.OptionItem;
import org.fatmansoft.teach.payload.response.OptionItemList;
import org.fatmansoft.teach.repository.CourseRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.repository.UserCourseRepository;
import org.fatmansoft.teach.util.ComDataUtil;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserCourseRepository userCourseRepository;
    @Autowired
    private StudentRepository studentRepository;  //学生数据操作自动注入

    @PostMapping("/getCourseList")
    public DataResponse getCourseList(@Valid @RequestBody DataRequest dataRequest) {
        //Integer studentId = dataRequest.getInteger("studentId");
        List<Course> sList = courseRepository.findAll();  //数据库查询操作
        List dataList = new ArrayList();
        Map m;
        for (Course c : sList) {
            m = new HashMap();
            m.put("courseId", c.getCourseId());
            m.put("num", c.getNum());
            m.put("coursePlace",c.getCoursePlace());
            m.put("courseTime",c.getCourseTime());
            m.put("credit",c.getCredit());
            m.put("name",c.getName());
            //m.put("courseYes",c.getcourseYes());
            dataList.add(m);
        }
        return CommonMethod.getReturnData(dataList);
    }
    @PostMapping("/save")
    public DataResponse saveCourse(@Valid @RequestBody DataRequest dataRequest){
        Map data = dataRequest.getData();
        Course course = new Course(data);
        String msg = "";
        if (course.getCourseId()!=null){
            msg = "修改成功";
        }else {
            msg = "新增成功";
        }
        Course savedCourse;
        try {
            savedCourse = courseRepository.save(course);
        } catch (Exception e) {
            // 返回一个错误响应
            return new DataResponse(-1, "保存课程时出错: " + e.getMessage(), null);
        }

        // 检查 savedCourse 是否为空
        if (savedCourse == null) {
            return new DataResponse(-1, "保存课程时出错: 返回的课程对象为空", null);
        }

        // 返回成功的响应
        return CommonMethod.getReturnData(savedCourse,msg);
    }
    @PostMapping("/delete")
    public DataResponse deleteCourse(@Valid @RequestBody DataRequest dataRequest){
        Map data = dataRequest.getData();
        Course course = new Course(data);
        courseRepository.delete(course);
        return CommonMethod.getReturnMessageOK("删除成功!");
    }
    @PostMapping("/select")
    public DataResponse pSelectCourse(@Valid @RequestBody DataRequest dataRequest){
        Map data = dataRequest.getData();
        UserCourse userCourse = new UserCourse(data);
        UserCourse find = userCourseRepository.findByUserIdAndCourseId(userCourse.getUserId(), userCourse.getCourseId());
        if (find!=null){
            return CommonMethod.getReturnMessageOK("已选择该课程!");
        }
        userCourseRepository.save(userCourse);
        return CommonMethod.getReturnMessageOK("选课成功!");
    }
    @PostMapping ("/select/delete")
    public DataResponse dSelectCourse(@Valid @RequestBody DataRequest dataRequest){
        Map data = dataRequest.getData();
        UserCourse userCourse = new UserCourse(data);
        UserCourse find = userCourseRepository.findByUserIdAndCourseId(userCourse.getUserId(), userCourse.getCourseId());
        if (find==null){
            return CommonMethod.getReturnMessageOK("未择该课程!");
        }
        userCourseRepository.deleteByUserIdAndCourseId(userCourse.getUserId(), userCourse.getCourseId());
        return CommonMethod.getReturnMessageOK("退课成功!");
    }
    @PostMapping ("/select/get")
    public DataResponse gSelectCourse(@Valid @RequestBody DataRequest dataRequest){
        Map data = dataRequest.getData();
        UserCourse userCourse = new UserCourse(data);
        List<UserCourse> all = userCourseRepository.findAllByUserId(userCourse.getUserId());
        List dataList = new ArrayList<>();
        for (UserCourse uc : all) {
            Optional<Course> byId = courseRepository.findById(uc.getCourseId());
            if (byId.isPresent()){
                Map m;
                Course c = byId.get();
                m = new HashMap();
                m.put("courseId", c.getCourseId());
                m.put("num", c.getNum());
                m.put("coursePlace",c.getCoursePlace());
                m.put("courseTime",c.getCourseTime());
                m.put("credit",c.getCredit());
                m.put("name",c.getName());
                m.put("courseYes",c.getcourseYes());
                dataList.add(m);
            }
        }
        return CommonMethod.getReturnData(dataList);

    }
    @PostMapping ("/select/Score")
    public DataResponse gCourseScore(@Valid @RequestBody DataRequest dataRequest){
        Map data = dataRequest.getData();
        UserCourse userCourse = new UserCourse(data);
        List<UserCourse> all = userCourseRepository.findAllByUserId(userCourse.getUserId());
        List dataList = new ArrayList<>();
        for (UserCourse uc : all) {
            Optional<Course> byId = courseRepository.findById(uc.getCourseId());
            if (byId.isPresent()){
                Map m;
                Course c = byId.get();
                m = new HashMap();
                m.put("courseId", c.getCourseId());
                m.put("num", c.getNum());
                m.put("coursePlace",c.getCoursePlace());
                m.put("courseTime",c.getCourseTime());
                m.put("credit",c.getCredit());
                m.put("name",c.getName());
                m.put("courseScore",c.getCourseScore());
                dataList.add(m);
            }
        }
        return CommonMethod.getReturnData(dataList);

    }
    public Map getMapFromUserCourse(UserCourse f) {
        Map m = new HashMap();
        Student s;
        User u;
        Course e;
        Person p;
        if(f == null)
            return m;
        u = f.getUser();
        //s = f.getStudent();
        e = f.getCourse();
        p = u.getPerson();

        if(u == null)
            return m;
        m.put("courseId", e.getCourseId());
        //m.put("studentId", s.getStudentId());
        m.put("studentName", p.getName());
        m.put("courseName", e.getName());
        m.put("courseScore",f.getcourseScore());
        //m.put("className",s.getClassName());
        m.put("id", f.getId());
        m.put("num",p.getNum());
        //m.put("name",s.getPerson().getName());
        //m.put("dept",s.getPerson().getDept());

       /* m.put("phone",s.getPerson().getPhone());
        m.put("startDate",e.getStartDate());
        m.put("endDate",e.getEndDate());
        m.put("absentReason",e.getAbsentReason());
        m.put("examine",e.getExamine());
        String absentType = e.getAbsentType();
        m.put("absentType", absentType);
        m.put("absentTypeName", ComDataUtil.getInstance().getDictionaryLabelByValue("LBM", absentType)); //请假类型的值转换成数据类型名
        String absentreq = e.getAbsentReq();

        m.put("absentreq", absentreq);
        m.put("absentreqName", ComDataUtil.getInstance().getDictionaryLabelByValue("ARN", absentreq));*/
        return m;
    }
    @PostMapping("/getUserCourseInfo")
    public DataResponse getUserCourseInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        System.out.println(id);
        UserCourse e= null;
        Optional<UserCourse> op;
        if(id != null) {
            op= userCourseRepository.findById(id); //根据学生主键从数据库查询学生的信息
            if(op.isPresent()) {
                e = op.get();
            }
        }
        System.out.println(getMapFromUserCourse(e));
        return CommonMethod.getReturnData(getMapFromUserCourse(e)); //这里回传包含学生信息的Map对象
    }
    public List getStuCourseScoreMapList(Integer num) {
        List dataList = new ArrayList();
        List<UserCourse> eList = userCourseRepository.findAllUserCourses();  //数据库查询操作
        //System.out.println(eList);
        if(eList == null || eList.size() == 0)
            return dataList;
        for(int i = 0; i < eList.size();i++) {
            dataList.add(getMapFromUserCourse(eList.get(i)));
        }
        return dataList;
    }
    public List getStuCourseScoreMapList2(Integer num , Integer userId) {//在学生端获得表
        List dataList = new ArrayList();
        List<UserCourse> eList = userCourseRepository.findAllByUserId(userId);  //数据库查询操作
        //System.out.println(eList);
        if(eList == null || eList.size() == 0)
            return dataList;
        for(int i = 0; i < eList.size();i++) {
            dataList.add(getMapFromUserCourse(eList.get(i)));
        }
        return dataList;
    }
    @PostMapping("/getStuCourseScoreList")
    public DataResponse getStuAbsent1List(@Valid @RequestBody DataRequest dataRequest) {
        Integer num= dataRequest.getInteger("num");
        Integer userId = dataRequest.getInteger("userId");
        List dataList;
        if(num == 1){
            dataList = getStuCourseScoreMapList(Integer.valueOf(num));
        }else {
            dataList = getStuCourseScoreMapList2(Integer.valueOf(num),userId);
        }

        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }
    public List getUserCourseList(String numName) {
        List dataList = new ArrayList();
        List<UserCourse> eList = userCourseRepository.findUserCourseListByNumName(numName) ;//数据库查询操作
        if(eList == null || eList.size() == 0)
            return dataList;
        for(int i = 0; i < eList.size();i++) {
            dataList.add(getMapFromUserCourse(eList.get(i)));
        }
        return dataList;
    }
    @PostMapping("/getStuCourseList")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getStuAbsentList(@Valid @RequestBody DataRequest dataRequest) {
        String numName= dataRequest.getString("numName");
        List dataList = getUserCourseList(numName);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }
    @PostMapping("/saveScore")
    public DataResponse absentreq(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        System.out.println(id);
        //System.out.println("studentAbsentId:"+stuabsentId);
        //Integer absentId = studentAbsentRepository.findAbsentIDByStudentabsentID(stuabsentId);
        //System.out.println("AbsentId:"+absentId);
        //System.out.println(absentId);
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        String num = CommonMethod.getString(form, "num");  //Map 获取属性的值
        String name = CommonMethod.getString(form, "name");
        //Calendar c1 = Calendar.getInstance();
        UserCourse f = null;
        Student s ;
        Optional<UserCourse> op;
        Integer studentId;
        Optional<Student> nOp = studentRepository.findByPersonNum(num); //查询是否存在num的人员
        List<Student> sList = studentRepository.findByPersonName(name);
        if (id != null) {
            op = userCourseRepository.findById(id);  //查询对应数据库中主键为id的值的实体对象
            f = op.get();//获得family

        }
/*        if (f == null) {
            s = nOp.get();
            studentId = s.getStudentId();
            f = new Absent();   // 创建实体对象
            f.setAbsentId(getNewAbsentId());//获取新的absent主键
            f.setStudent(studentRepository.findById(studentId).get());

        }*/
/*        //判断填写日期是否正确
        if (CommonMethod.getDate(form,"endDate").before(CommonMethod.getDate(form,"startDate"))){
            return CommonMethod.getReturnMessageError("开始日期应早于结束日期！");
        }
        if(CommonMethod.getDate(form,"startDate").before(c1.getTime())
                ||CommonMethod.getDate(form,"endDate").before(c1.getTime())){
            return CommonMethod.getReturnMessageError("日期不能早于今天！");
        }*/
/*        f.setAbsentReason((String) form.get("absentReason"));
        f.setAbsentType((String) form.get("absentType"));
        f.setStartDate((String) form.get("startDate"));
        f.setEndDate((String) form.get("endDate"));
        f.setExamine((String) form.get("examine"));*/
        f.setcourseScore((String) form.get("courseScore"));
        userCourseRepository.save(f);  // 修改保存请假信息
        return CommonMethod.getReturnData(f.getId());  // 将absentId返回前端

    }

}
