package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.service.BaseService;
import org.fatmansoft.teach.util.ComDataUtil;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/DailyActivities")
public class DailyActivitiesController extends StudentController{
    // StudentController中的方法可以直接使用
    @Autowired
    private PersonRepository personRepository;  //人员数据操作自动注入
    @Autowired
    private StudentRepository studentRepository;  //学生数据操作自动注入
    @Autowired
    private UserRepository userRepository;  //学生数据操作自动注入
    @Autowired
    private UserTypeRepository userTypeRepository; //用户类型数据操作自动注入

    @Autowired
    private BaseService baseService;   //基本数据处理数据操作自动注入
    @Autowired
    private DailyActivitiesRepository dailyActivitiesRepository;

    public synchronized Integer getNewDailyActivitiesId() {
        Integer id = dailyActivitiesRepository.getMaxId();
        if (id == null)
            id = 1;
        else
            id = id + 1;
        return id;
    }

    public Map getMapFromDailyActivities(DailyActivities ip) {
        Map m = new HashMap();
        Person p;
        Student s;
        if (ip == null) {
            return m;
        }
        m.put("DailyActivitiesId", ip.getDailyActivitiesId());
        m.put("studentId", ip.getStudent().getStudentId());
        m.put("ipnum", ip.getIpnum());
        String ipkinds = ip.getIpkinds();
        m.put("ipkinds", ip.getIpkinds());
        m.put("ipkindsName", ComDataUtil.getInstance().getDictionaryLabelByValue("ACT", ipkinds));
        m.put("ipname", ip.getIpname());
        m.put("iptime",ip.getIptime());
        m.put("ipplace", ip.getIpplace());
        s = ip.getStudent();
        p = s.getPerson();
        m.put("num", p.getNum());
        m.put("name", p.getName());
        return m;

    }

    public List getInnovativePracticeMapList(String ipnumIpname) {
        List dataList = new ArrayList();
        List<DailyActivities> ipList = dailyActivitiesRepository.findCreateListByIpnumIpname(ipnumIpname);//数据库查询操作
        if (ipList == null || ipList.size() == 0)
            return dataList;
        for (int i = 0; i < ipList.size(); i++) {
            dataList.add(getMapFromDailyActivities(ipList.get(i)));
        }
        return dataList;
    }

    /**
     * getInnovativePracticeList 创新实践管理 点击查询按钮请求
     * 前台请求参数 ipnumIpname 查询串
     * 返回前端 存储创新实践信息的 MapList 框架会自动将Map转换程用于前后台传输数据的Json对象，Map的嵌套结构和Json的嵌套结构类似
     *
     * @return
     */
    @PostMapping("/getInnovativePracticeList")
    public DataResponse getInnovativePracticeList(@Valid @RequestBody DataRequest dataRequest) {
        String ipnumIpname = dataRequest.getString("ipnumIpname");
        List dataList = getInnovativePracticeMapList(ipnumIpname);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }

    @PostMapping("/innovativePracticeDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse innovativePracticeDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer innovativePracticeId = dataRequest.getInteger("DailyActivitiesId");//获取innovativePractice—_id值
        Optional<DailyActivities> op;
        DailyActivities ip = null;
        if ((innovativePracticeId != null)) {
            op = dailyActivitiesRepository.findById(innovativePracticeId);//查询获得实体对象
            if (op.isPresent()) {
                ip = op.get();
            }
        }
        if (ip != null) {
            dailyActivitiesRepository.delete(ip);
        }
        return CommonMethod.getReturnMessageOK();//通知前端操作正常
    }

    @PostMapping("/getInnovativePracticeInfo")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getInnovativePracticeInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer innovativePracticeId = dataRequest.getInteger("DailyActivitiesId");
        DailyActivities ip = null;
        Optional<DailyActivities> op;
        if (innovativePracticeId != null) {
            op = dailyActivitiesRepository.findById(innovativePracticeId); //根据创新实践主键从数据库查询学生的信息
            if (op.isPresent()) {
                ip = op.get();
            }
        }
        return CommonMethod.getReturnData(getMapFromDailyActivities(ip)); //这里回传包含创新实践信息的Map对象
    }


    @PostMapping("/innovativePracticeEditSave")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse innovativePracticeEditSave(@Valid @RequestBody DataRequest dataRequest) {
        Integer innovativePracticeId  = dataRequest.getInteger("DailyActivitiesId");
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        String num = CommonMethod.getString(form,"num");
        String name = CommonMethod.getString(form,"name"); //Map 获取属性的值
        Optional<Student> os= studentRepository.findByPersonNum(num);
        Optional<Person> opp = personRepository.findByNum(num);
        if(!os.isPresent()){return CommonMethod.getReturnMessageError("学生不存在，不能添加或修改！");}
        Person p = opp.get();
        if(!name.equals(p.getName())){return CommonMethod.getReturnMessageError("学生与学号不匹配！"+"学生姓名为: "+p.getName());}
        DailyActivities ip = null;
        Student s = os.get();
        Optional<DailyActivities> op;
        if( innovativePracticeId!= null) {
            op= dailyActivitiesRepository.findById(innovativePracticeId);//查询对应数据库中主键为id的值的实体对象
            if(op.isPresent()) {
                ip = op.get();
            }
        }
        if(ip == null) {
            ip =new DailyActivities();
            ip.setStudent(s);
        }
//        p.setName(CommonMethod.getString(form,"name"));
        ip.getStudent().getPerson().setNum(CommonMethod.getString(form,"num"));
        ip.getStudent().getPerson().setName(CommonMethod.getString(form,"name"));
        ip.setIpnum(CommonMethod.getString(form,"ipnum"));
        ip.setIpkinds(CommonMethod.getString(form,"ipkinds"));
        ip.setIpname(CommonMethod.getString(form,"ipname"));
        ip.setIptime(CommonMethod.getString(form,"iptime"));
        ip.setIpplace(CommonMethod.getString(form,"ipplace"));
        dailyActivitiesRepository.save(ip);
        return CommonMethod.getReturnData(ip.getDailyActivitiesId());  // 将studentId返回前端

    }

}


