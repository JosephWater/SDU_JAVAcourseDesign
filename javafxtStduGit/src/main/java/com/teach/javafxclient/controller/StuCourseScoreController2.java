package com.teach.javafxclient.controller;
import com.teach.javafxclient.AppStore;
import com.teach.javafxclient.controller.base.ToolController;
import com.teach.javafxclient.request.*;
import com.teach.javafxclient.util.CommonMethod;
import com.teach.javafxclient.controller.base.MessageDialog;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * StudentAbsentController 登录交互控制类 对应 adminAbsent_panel.fxml  对应于管理员端管理学生请假的后台业务处理的控制器，主要获取数据和保存数据的方法不同
 *  @FXML  属性 对应fxml文件中的
 *  @FXML 方法 对应于fxml文件中的 on***Click的属性
 */
public class StuCourseScoreController2 {
    @FXML
    private TableView<Map> dataTableView;  //学生请假信息表
    @FXML
    private TableColumn<Map,String> NumColumn; // 学号列
    @FXML
    private TableColumn<Map,String> NameColumn; // 姓名列
    @FXML
    private TableColumn<Map,String> CourseNameColumn; //课程名称
    @FXML
    private TableColumn<Map,Integer> CourseScoreColumn; //成绩
    @FXML
    private TableColumn<Map,Integer> gpaColumn; //成绩
    /*    @FXML
        private TableColumn<Map,String> absentReasonColumn; //请假理由列
        @FXML
        private TableColumn<Map,String> startDateColumn;//请假开始日期列
        @FXML
        private TableColumn<Map,String> endDateColumn;//请假结束日期列
        @FXML
        private TableColumn<Map,String> reqColumn;//请假结束日期列*/
    @FXML
    private Label num;//学号
    @FXML
    private Label name;//姓名
    @FXML
    private Label CourseName;//课程名称
    @FXML
    private Label CourseScore;//成绩
    @FXML
    private Label gpa;
/*    @FXML
    private Label absentReason;//请假原因
    @FXML
    private Label absentType;  //请假类型输入域
    @FXML
    private Label examine;  //请假类型输入域
    @FXML
    private ComboBox<OptionItem> absentreqComboBox;
    @FXML*/
    @FXML
    private TextField numNameTextField;  //查询 姓名学号输入域
    private TextField numField;
    private TextField nameField;
    //@FXML
    //private TextField CourseScore;
    private Integer id = null;  //当前编辑修改的请假的主键
    //private Integer absentId = null;
    private ArrayList<Map> CourseScoreList = new ArrayList();  //

    //private List<OptionItem> absentreqList;
    private ObservableList<Map> observableList= FXCollections.observableArrayList();  // TableView渲染列表
    private void setTableViewData() {
        observableList.clear();
        for (int j = 0; j < CourseScoreList.size(); j++) {
            observableList.addAll(FXCollections.observableArrayList(CourseScoreList.get(j)));
        }
        dataTableView.setItems(observableList);
    }
    @FXML
    public void initialize() {
        DataResponse res;
        DataRequest req = new DataRequest();
        req.put("num",2);
        req.put("userId", AppStore.getJwt().getId());
        res = HttpRequestUtil.request("/api/course/getStuCourseScoreList", req); //从后台获取所有学生信息列表集合
        //System.out.println(res);
        if (res != null && res.getCode() == 0) {
            CourseScoreList = (ArrayList<Map>) res.getData();
        }
        NumColumn.setCellValueFactory(new MapValueFactory<>("num"));
        NameColumn.setCellValueFactory(new MapValueFactory<>("studentName"));
        CourseNameColumn.setCellValueFactory(new MapValueFactory<>("courseName"));
        CourseScoreColumn.setCellValueFactory(new MapValueFactory<>("courseScore"));
        //gpaColumn.setCellValueFactory(new MapValueFactory<>("courseScore"));
        TableView.TableViewSelectionModel<Map> tsm = dataTableView.getSelectionModel();
        ObservableList<Integer> list = tsm.getSelectedIndices();
        list.addListener(this::onTableRowSelect);
        setTableViewData();
    }
    protected void changeStudentAbsentInfo() {
        Map form = dataTableView.getSelectionModel().getSelectedItem();
        if (form == null) {
            return;
        }
        id = CommonMethod.getInteger(form, "id");
        //System.out.println(id);
        DataRequest req = new DataRequest();
        req.put("id", id);
        DataResponse res = HttpRequestUtil.request("/api/course/getUserCourseInfo", req);
        //System.out.println(res);
        if (res.getCode() != 0) {
            MessageDialog.showDialog(res.getMsg());
            return;
        }
        form = (Map) res.getData();
        num.setText(CommonMethod.getString(form,"num"));
        name.setText(CommonMethod.getString(form,"studentName"));
        CourseName.setText(CommonMethod.getString(form, "courseName"));
        CourseScore.setText(CommonMethod.getString(form, "courseScore"));
        /*endDate.setText(CommonMethod.getString(form, "endDate"));
        absentReason.setText(CommonMethod.getString(form, "absentReason"));
        //examine.setText(CommonMethod.getString(form, "examine"));
        absentreqComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(absentreqList,CommonMethod.getString(form,"absentreq")));*/
    }

    /*
     * 点击请假列表的某一行，根据studentAbsentId ,从后台查询学生的请假信息息
     */

    public void onTableRowSelect(ListChangeListener.Change<? extends Integer> change){
        changeStudentAbsentInfo();
    }
    @FXML
    protected void onQueryButtonClick() {
        String numName = numNameTextField.getText();
        DataRequest req = new DataRequest();
        req.put("numName",numName);
        DataResponse res = HttpRequestUtil.request("/api/course/getStuCourseList",req);
        if(res != null && res.getCode()== 0) {
            CourseScoreList = (ArrayList<Map>)res.getData();
            setTableViewData();
        }
    }
    //刷新
    @FXML
    protected void onFreshButtonClick(){initialize();}

    /**
     * 管理员端批准通过请假信息
     */
    /*protected void onApplyButtonClick(){
        Map form = new HashMap();
        DataRequest req = new DataRequest();
        req.put("absentId",absentId);
        DataResponse res;
        res = HttpRequestUtil.request("/api/student/getStudentIntroduceData", req); //从后台获取所有学生信息列表集合
        Map data =(Map)res.getData();
        Map info = (Map)data.get("info");
        String num=CommonMethod.getString(info,"num");
        String examine ="未审批";
        form.put("absentReason",absentReasonField.getText());
        if(absentTypeComboBox.getSelectionModel() != null && absentTypeComboBox.getSelectionModel().getSelectedItem() != null)
            form.put("absentType",absentTypeComboBox.getSelectionModel().getSelectedItem().getValue());
        form.put("startDate",startDatePick.getEditor().getText());
        form.put("endDate",endDatePick.getEditor().getText());
        form.put("examine",examine);
        req.put("form",form);
        form.put("num",num);
        form = dataTableView.getSelectionModel().getSelectedItem();
        res = HttpRequestUtil.request("/api/absent/absentEditSave",req);
        if(res.getCode() == 0) {
            absentId = CommonMethod.getIntegerFromObject(res.getData());;
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
        res = HttpRequestUtil.request("/api/studentAbsent/studentAbsentEditSave", req); //从后台获取所有学生信息列表集合
        int ret = MessageDialog.choiceDialog("确认要申请请假吗?");
        if(ret != MessageDialog.CHOICE_YES) {
            return;
        }
        if(res.getCode() == 0) {
            absentId = CommonMethod.getIntegerFromObject(res.getData());
            MessageDialog.showDialog("申请成功！");
            initialize();
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
    }*/
    @FXML
    protected void onSaveButtonClick() {
        Map form = new HashMap();
        Map form1 = dataTableView.getSelectionModel().getSelectedItem();
        if (form1 == null) {
            return;
        }
        id = CommonMethod.getInteger(form1, "id");
        //studentAbsentId = CommonMethod.getInteger(form, "studentAbsentId");
        //System.out.println(studentAbsentId);
        form.put("num",num.getText());
        form.put("name",name.getText());
        form.put("courseScore",CourseScore.getText());
        //System.out.println(num.getText());
/*        form.put("dept",deptField.getText());
        form.put("major",majorField.getText());
        form.put("className",classNameField.getText());
        form.put("card",cardField.getText());*/
       /* if(absentreqComboBox.getSelectionModel() != null && absentreqComboBox.getSelectionModel().getSelectedItem() != null)
            form.put("absentreq",absentreqComboBox.getSelectionModel().getSelectedItem().getValue());*/
        /*form.put("birthday",birthdayPick.getEditor().getText());
        form.put("email",emailField.getText());
        form.put("phone",phoneField.getText());
        form.put("address",addressField.getText());*/
        DataRequest req = new DataRequest();
        req.put("id", id);
        req.put("form", form);
        DataResponse res = HttpRequestUtil.request("/api/course/saveScore",req);
        if (res != null) {
            if (res.getCode() == 0) {
                id = CommonMethod.getIntegerFromObject(res.getData());
                MessageDialog.showDialog("提交成功！");
            } else {
                MessageDialog.showDialog(res.getMsg());
            }
        } else {
            MessageDialog.showDialog("服务器响应为空，请稍后再试。");
        }
        onQueryButtonClick();
    }


}
