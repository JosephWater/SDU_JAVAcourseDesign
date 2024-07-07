package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.payload.response.OptionItem;
import org.fatmansoft.teach.payload.response.OptionItemList;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.service.BaseService;
import org.fatmansoft.teach.util.ComDataUtil;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.DateFormatter;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/honor")

public class HonorController {
    //荣誉信息
    @Autowired
    private StudentRepository studentRepository;  //学生数据操作自动注入
    @Autowired
    private HonorRepository honorRepository;  //荣誉信息操作自动注入
    /**
     *  获取 honor 表的新的Id StringBoot 对SqLite 主键自增支持不好  插入记录是需要设置主键ID，编写方法获取新的 honora_id
     * @return
     */
    public synchronized Integer getNewHonorId() {
        Integer id = honorRepository.getMaxId();  // 查询最大的id
        if (id == null)
            id = 1;
        else
            id = id + 1;
        return id;
    }

    /**
     * getMapFromHonor 将学生荣誉信息表属性数据转换复制MAp集合里
     * @param
     * @return
     */
    public Map getMapFromHonor(Honor h) {
        Map m = new HashMap();
        Student s;
        if(h == null)
            return m;
        m.put("honorId", h.getHonorId());
        m.put("honorName",h.getHonorName());
        m.put("honorGetTime",h.getHonorGetTime());
        m.put("honorGrade",h.getHonorGrade());
        m.put("honorType", h.getHonorType());
        s=h.getStudent();
        m.put("studentId",s.getStudentId());
        m.put("num",s.getPerson().getNum());
        m.put("name",s.getPerson().getName());
        return m;
    }
    /**
     *  getHonorMapList 根据输入参数查询得到学生荣誉信息数据的 Map List集合 参数为空 查出说有学生， 参数不为空，查出人员编号或人员名称 包含输入字符串的学生
     * @param numName 输入参数
     * @return  Map List 集合
     */
    public List getHonorMapList(String numName) {
        List dataList = new ArrayList();
        List<Honor> hList = honorRepository.findHonorListByNumName(numName);  //数据库查询操作
        if(hList == null || hList.size() == 0)
            return dataList;
        for(int i = 0; i < hList.size();i++) {
            dataList.add(getMapFromHonor(hList.get(i)));
        }
        return dataList;
    }
    public List getHonor1MapList(String num) {
        List dataList = new ArrayList();
        List<Honor> hList = honorRepository.findByStudentNum(num);  //数据库查询操作
        if(hList == null || hList.size() == 0)
            return dataList;
        for(int i = 0; i < hList.size();i++) {
            dataList.add(getMapFromHonor(hList.get(i)));
        }
        return dataList;
    }
    /**
     *  getHonorList 荣誉信息管理 点击查询按钮请求
     *  前台请求参数 numName 学号或名称的 查询串
     * 返回前端 存储学生荣誉信息的 MapList 框架会自动将Map转换程用于前后台传输数据的Json对象，Map的嵌套结构和Json的嵌套结构类似
     * @return
     */

    @PostMapping("/getHonorItemOptionList")
    public OptionItemList getHonorItemOptionList(@Valid @RequestBody DataRequest dataRequest) {
        List<Honor> hList = honorRepository.findHonorListByNumName("");  //数据库查询操作
        OptionItem item;
        List<OptionItem> itemList = new ArrayList();
        for (Honor h : hList) {
            itemList.add(new OptionItem(h.getHonorId(),h.getStudent().getPerson().getNum(),h.getStudent().getPerson().getNum()+"-"+h.getStudent().getPerson().getName()));
        }
        return new OptionItemList(0, itemList);
    }


    @PostMapping("/getHonorList")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getHonorList(@Valid @RequestBody DataRequest dataRequest) {
        String numName= dataRequest.getString("numName");
        List dataList = getHonorMapList(numName);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }
    @PostMapping("/getHonor1List")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public DataResponse getHonor1List(@Valid @RequestBody DataRequest dataRequest) {
        String num= dataRequest.getString("num");
        List dataList = getHonor1MapList(num);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }

    @PostMapping("/honorDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse honorDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer honorId = dataRequest.getInteger("honorId");  //获取student_id值
        Honor h= null;
        Optional<Honor> op;
        if(honorId != null) {
            op= honorRepository.findById(honorId);   //查询获得实体对象
            if(op.isPresent()) {
                h = op.get();
            }
        }
        if(h != null) {

            honorRepository.delete(h);    //首先数据库永久删除学生荣誉信息
        } return CommonMethod.getReturnMessageOK();  //通知前端操作正常
    }

    @PostMapping("/honorEditSave")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse honorEditSave(@Valid @RequestBody DataRequest dataRequest) {
        Integer honorId = dataRequest.getInteger("honorId");
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        String num = CommonMethod.getString(form,"num");
        String name = CommonMethod.getString(form,"name");//Map 获取属性的值
        Calendar c1 = Calendar.getInstance();
        Honor h= null;
        Student s;
        Optional<Honor> op;
        Integer studentId;
        Optional<Student> nOp = studentRepository.findByPersonNum(num); //查询是否存在num的人员
        List<Student> sList = studentRepository.findByPersonName(name);
        if (honorId != null) {
            op = honorRepository.findById(honorId);  //查询对应数据库中主键为id的值的实体对象
            if (op.isPresent()) {
                h = op.get();//获得family
            }
        }

        if (!nOp.isPresent()) {
            return CommonMethod.getReturnMessageError("该学生学号不存在，不能添加或修改荣誉信息！");
        }

        if (sList.size() == 0||sList == null) {
            return CommonMethod.getReturnMessageError("该学生姓名不存在，不能添加或修改荣誉信息！");
        }
        if(CommonMethod.getDate(form,"honorGetTime").after(c1.getTime())){
            return CommonMethod.getReturnMessageError("获奖日期不能晚于今天！");
        }
        else {
            if (h == null) {
                s = nOp.get();
                studentId = s.getStudentId();
                h = new Honor();   // 创建实体对象
                h.setHonorId(getNewHonorId());//获取新的family主键
                h.setStudent(studentRepository.findById(studentId).get());
            }
            else{

            }
        }


        h.setHonorName((String) form.get("honorName"));
        h.setHonorGrade((String) form.get("honorGrade"));
        h.setHonorGetTime((String) form.get("honorGetTime"));
        h.setHonorType((String) form.get("honorType"));
        honorRepository.save(h);  // 修改保存荣誉信息
        return CommonMethod.getReturnData(h.getHonorId());  // 将honorId返回前端

    }
    @PostMapping("/getHonorInfo")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getHonorInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer honorId = dataRequest.getInteger("honorId");
        Honor h= null;
        Optional<Honor> op;
        if(honorId != null) {
            op= honorRepository.findById(honorId); //根据学生主键从数据库查询学生的信息
            if(op.isPresent()) {
                h = op.get();
            }
        }
        return CommonMethod.getReturnData(getMapFromHonor(h)); //这里回传包含学生信息的Map对象
    }
}


