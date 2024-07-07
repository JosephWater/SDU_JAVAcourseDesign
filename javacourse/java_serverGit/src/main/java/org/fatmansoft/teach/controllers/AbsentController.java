package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.payload.response.OptionItem;
import org.fatmansoft.teach.payload.response.OptionItemList;
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
@RequestMapping("/api/absent")
public class AbsentController {
    //学生准备申请的请假
    @Autowired
    private AbsentRepository absentRepository;  //家庭数据操作自动注入
    @Autowired
    private StudentRepository studentRepository;  //学生数据操作自动注入
    public synchronized Integer getNewAbsentId(){
        Integer  id = absentRepository.getMaxId();  // 查询最大的id
        if(id == null)
            id = 1;
        else
            id = id+1;
        return id;
    };
    /**
     * getMapFromAbsent 将请假表属性数据转换复制MAp集合里
     * @param
     * @return
     */
    public Map getMapFromAbsent(Absent e) {
        Map m = new HashMap();
        Student s;
        if(e == null)
            return m;
        m.put("startDate",e.getStartDate());
        m.put("endDate",e.getEndDate());
        m.put("absentReason",e.getAbsentReason());
        String absentType = e.getAbsentType();
        m.put("absentType", absentType);
        m.put("absentTypeName", ComDataUtil.getInstance().getDictionaryLabelByValue("LBM", absentType)); //请假类型类型的值转换成数据类型名
        m.put("absentreq", e.getAbsentReq());
        m.put("absentreqName", ComDataUtil.getInstance().getDictionaryLabelByValue("ARN", e.getAbsentReq()));
        s = e.getStudent();
        if(s == null)
            return m;
        m.put("absentId", e.getAbsentId());
        m.put("studentId", s.getStudentId());
        m.put("num",s.getPerson().getNum());
        return m;
    }
    /**
     *  getAbsentMapList 根据输入参数查询得到请假数据的 Map List集合 参数为空 查出说有学生， 参数不为空，查出人员编号或人员名称 包含输入字符串的学生
     * @param numName 输入参数
     * @return  Map List 集合
     */
    public List getAbsentMapList(String numName) {
        List dataList = new ArrayList();
        List<Absent> eList = absentRepository.findAbsentListByNumName(numName);  //数据库查询操作
        if(eList == null || eList.size() == 0)
            return dataList;
        for(int i = 0; i < eList.size();i++) {
            dataList.add(getMapFromAbsent(eList.get(i)));
        }
        return dataList;
    }
    public List getAbsent1MapList(String num) {
        List dataList = new ArrayList();
        List<Absent> eList = absentRepository.findByStudentStudentNum(num);  //数据库查询操作
        if(eList == null || eList.size() == 0)
            return dataList;
        for(int i = 0; i < eList.size();i++) {
            dataList.add(getMapFromAbsent(eList.get(i)));
        }
        return dataList;
    }
    /**
     *  getAbsentList 学生管理 点击查询按钮请求
     *  前台请求参数 numName 学号或名称的 查询串
     * 返回前端 存储学生信息的 MapList 框架会自动将Map转换程用于前后台传输数据的Json对象，Map的嵌套结构和Json的嵌套结构类似
     * @return
     */

    @PostMapping("/getAbsentItemOptionList")
    public OptionItemList getAbsentItemOptionList(@Valid @RequestBody DataRequest dataRequest) {
        List<Absent> sList = absentRepository.findAbsentListByNumName("");  //数据库查询操作
        OptionItem item;
        List<OptionItem> itemList = new ArrayList();
        for (Absent e : sList) {
            itemList.add(new OptionItem(e.getAbsentId(), e.getStudent().getPerson().getNum(), e.getStudent().getPerson().getNum()+"-"+e.getStudent().getPerson().getName()));
        }
        return new OptionItemList(0, itemList);
    }
    @PostMapping("/getAbsentList")
    public DataResponse getEducationList(@Valid @RequestBody DataRequest dataRequest) {
        String numName= dataRequest.getString("numName");
        List dataList = getAbsentMapList(numName);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }
    //学生端
    /**
     *  getAbsent1List 学生管理 点击查询按钮请求
     *  前台请求参数 num 学号 查询串
     * 返回前端 存储学生信息的 MapList 框架会自动将Map转换程用于前后台传输数据的Json对象，Map的嵌套结构和Json的嵌套结构类似
     * @return
     */
    @PostMapping("/getAbsent1List")
    public DataResponse getAbsent1List(@Valid @RequestBody DataRequest dataRequest) {
        String num= dataRequest.getString("num");
        List dataList = getAbsent1MapList(num);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }
    /**
     * getAbsentInfo 前端点击请假列表时前端获取请假详细信息请求服务
     * @param dataRequest 从前端获取 absentId 查询请假信息的主键 absent_id
     * @return  根据absentId从数据库中查出数据，存在Map对象里，并返回前端
     */

    @PostMapping("/getAbsentInfo")
    public DataResponse getAbsentInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer absentId = dataRequest.getInteger("absentId");
        Absent e= null;
        Optional<Absent> op;
        if(absentId != null) {
            op= absentRepository.findById(absentId); //根据主键从数据库查询信息
            if(op.isPresent()) {
                e = op.get();
            }
        }
        return CommonMethod.getReturnData(getMapFromAbsent(e)); //这里回传包含请假信息的Map对象
    }
    /**
     * AbsentEditSave 前端请假信息提交服务
     * 前端把所有数据打包成一个Json对象作为参数传回后端，后端直接可以获得对应的Map对象form, 再从form里取出所有属性，复制到
     * 实体对象里，保存到数据库里即可，如果是添加一条记录， id 为空，这是先 new Absent 计算新的id， 复制相关属性，保存，如果是编辑原来的信息，
     * absentId不为空。则查询出实体对象，复制相关属性，保存后修改数据库信息，永久修改
     * 审批状态自动设为未审批
     * @return  新建修改学生的主键 student_id 返回前端
     */
    @PostMapping("/absentEditSave")
    public DataResponse absentEditSave(@Valid @RequestBody DataRequest dataRequest) {
        Integer absentId = dataRequest.getInteger("absentId");
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        String num = CommonMethod.getString(form, "num");  //Map 获取属性的值
        String name = CommonMethod.getString(form, "name");
        Calendar c1 = Calendar.getInstance();
        Absent f = null;
        Student s ;
        Optional<Absent> op;
        Integer studentId;
        Optional<Student> nOp = studentRepository.findByPersonNum(num); //查询是否存在num的人员
        List<Student> sList = studentRepository.findByPersonName(name);
        if (absentId != null) {
            op = absentRepository.findById(absentId);  //查询对应数据库中主键为id的值的实体对象
            if (op.isPresent()) {
                f = op.get();//获得family
            }
        }
        if (f == null) {
            s = nOp.get();
            studentId = s.getStudentId();
            f = new Absent();   // 创建实体对象
            f.setAbsentId(getNewAbsentId());//获取新的absent主键
            f.setStudent(studentRepository.findById(studentId).get());

        }
        //判断填写日期是否正确
        if (CommonMethod.getDate(form,"endDate").before(CommonMethod.getDate(form,"startDate"))){
            return CommonMethod.getReturnMessageError("开始日期应早于结束日期！");
        }
        if(CommonMethod.getDate(form,"startDate").before(c1.getTime())
                ||CommonMethod.getDate(form,"endDate").before(c1.getTime())){
            return CommonMethod.getReturnMessageError("日期不能早于今天！");
        }
        f.setAbsentReason((String) form.get("absentReason"));
        f.setAbsentType((String) form.get("absentType"));
        f.setStartDate((String) form.get("startDate"));
        f.setEndDate((String) form.get("endDate"));
        f.setExamine((String) form.get("examine"));
        absentRepository.save(f);  // 修改保存请假信息
        return CommonMethod.getReturnData(f.getAbsentId());  // 将absentId返回前端

    }
    /**
     * absentDelete 删除学生信息Web服务 Absent页面的列表里点击删除按钮则可以删除已经存在的请假信息， 前端会将该记录的id 回传到后端，方法从参数获取id，查出相关记录，调用delete方法删除
     * 这里注意删除顺序，删除absent同时,与之关联的studentAbsent,approveAbsent,rejectAbsent均会删除信息
     * @param dataRequest  前端absentId 删除的学生的主键 absent_id
     * @return  正常操作
     */
    @PostMapping("/absentDelete")
    public DataResponse absentDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer absentId = dataRequest.getInteger("absentId");  //获取student_id值
        Absent s = null;
        Optional<Absent> op;
        if (absentId != null) {
            op = absentRepository.findById(absentId);   //查询获得实体对象
            if (op.isPresent()) {
                s = op.get();
            }
        }
        if (s != null) {
            absentRepository.delete(s);
        }
        return CommonMethod.getReturnMessageOK();  //通知前端操作正常
    }

}
