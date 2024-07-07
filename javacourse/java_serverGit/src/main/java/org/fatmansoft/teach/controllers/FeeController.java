package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.util.ComDataUtil;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

// origins： 允许可访问的域列表
// maxAge:准备响应前的缓存持续的最大时间（以秒为单位）。
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/fee")

public class FeeController {

    @Autowired
    private FeeRepository feeRepository;  //学生数据操作自动注入

    @Autowired
    private StudentRepository studentRepository;  //学生数据操作自动注入

    @Autowired
    private PersonRepository personRepository;  //人员数据操作自动注入


    public synchronized Integer getNewFeeId(){
        Integer  id = feeRepository.getMaxId();  // 查询最大的id
        if(id == null)
            id = 1;
        else
            id = id+1;
        return id;
    };

    public synchronized Integer getNewStudentId() {
        Integer id = studentRepository.getMaxId();  // 查询最大的id
        if (id == null)
            id = 1;
        else
            id = id + 1;
        return id;
    }

    /**
     * getMapFromStudent 将学生表属性数据转换复制MAp集合里
     *
     * @param
     * @return
     */
    public Map getMapFromFee(Fee c) {
        Map m = new HashMap();
        Student s;
        if (c == null)
            return m;
        s = c.getStudent();

        if (s == null)
            return m;
        m.put("feeId", c.getFeeId());
        m.put("studentId", c.getStudent().getStudentId());
        m.put("num", c.getStudent().getPerson().getNum());
        m.put("name", c.getStudent().getPerson().getName());
        m.put("place", c.getPlace());
        m.put("day", c.getDay());
        m.put("money", c.getMoney());
        m.put("item", c.getItem());
        String way = c.getWay();
        m.put("way",way);
        m.put("wayName", ComDataUtil.getInstance().getDictionaryLabelByValue("WAY", way)); //性别类型的值转换成数据类型名

        return m;
    }

    /**
     * getStudentMapList 根据输入参数查询得到学生数据的 Map List集合 参数为空 查出说有学生， 参数不为空，查出人员编号或人员名称 包含输入字符串的学生
     *
     * @param
     * @return Map List 集合
     */
    public List getFeeMapList(String numName) {
        List dataList = new ArrayList();
        List<Fee> sList = feeRepository.findFeeListByNumName(numName);  //数据库查询操作
        if (sList == null || sList.size() == 0)
            return dataList;
        for (int i = 0; i < sList.size(); i++) {
            dataList.add(getMapFromFee(sList.get(i)));
        }
        return dataList;
    }


    @PostMapping("/getFeeList")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getFeeList(@Valid @RequestBody DataRequest dataRequest) {
        String numName = dataRequest.getString("numName");
        List dataList = getFeeMapList(numName);
        return CommonMethod.getReturnData(dataList);
    }
    @PostMapping("/getSFeeList")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public DataResponse getSFeeList(@Valid @RequestBody DataRequest dataRequest) {
        String num= dataRequest.getString("num");
        List dataList = getFeeMapList(num);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }


    @PostMapping("/feeDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse feeDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer feeId = dataRequest.getInteger("feeId");  //获取student_id值
        Fee s = null;
        Optional<Fee> op;
        if (feeId != null) {
            op = feeRepository.findById(feeId);   //查询获得实体对象
            if (op.isPresent()) {
                s = op.get();
            }
        }
        if (s != null) {
            feeRepository.delete(s);
        }
        return CommonMethod.getReturnMessageOK();  //通知前端操作正常
    }

    @PostMapping("/getFeeInfo")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getFeeInfo(@Valid@RequestBody DataRequest dataRequest){
        Integer partyId= dataRequest.getInteger("feeId");
        Fee c=null;
        Optional<Fee> op;
        if(partyId!=null){
            op=feeRepository.findById(partyId);
            if(op.isPresent()){
                c=op.get();
            }
        }
        return CommonMethod.getReturnData(getMapFromFee(c));
    }

    @PostMapping("/feeEditSave")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse feeEditSave(@Valid@RequestBody DataRequest dataRequest){
        Integer feeId= dataRequest.getInteger("feeId");
        Map form=dataRequest.getMap("form");
        String num=CommonMethod.getString(form,"num");
        Fee c=null;
        if (feeId != null) {
            Optional<Fee> op;
            op= feeRepository.findById(feeId);
            if (op.isPresent()) {
                c = op.get();
                c.setNum(c.getStudent().getPerson().getNum());
                if (!c.getStudent().getPerson().getNum().equals(num)) {
                    return CommonMethod.getReturnMessageError("不可修改对应的学号");
                }
            }
        }
        if(c==null){
            Optional<Student>studentOptional=studentRepository.findByPersonNum(num);
            if(studentOptional.isEmpty()){
                return CommonMethod.getReturnMessageError("未找到相应num");
            }
            Student s=studentOptional.get();
            c=new Fee();
            c.setFeeId(getNewFeeId());
            c.setStudent(s);
            c.setNum(num);
            feeRepository.saveAndFlush(c);

        }

        c.setName((String)form.get("name"));
        c.setPlace((String)form.get("place"));
        c.setDay((String)form.get("day"));
        c.setMoney(Double.valueOf((String) form.get("money")));
        c.setItem((String)form.get("item"));
        c.setWay((String)form.get("way"));
        feeRepository.save(c);  // 修改保存课程信息
        return CommonMethod.getReturnData(c.getFeeId());  // 将courseId返回前端    }}
    }


}


