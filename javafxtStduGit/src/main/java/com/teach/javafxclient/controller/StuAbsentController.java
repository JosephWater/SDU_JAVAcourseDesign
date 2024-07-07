package com.teach.javafxclient.controller;
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
public class StuAbsentController {
    @FXML
    private TableView<Map> dataTableView;  //学生请假信息表
    @FXML
    private TableColumn<Map,String> numColumn; // 学号列
    @FXML
    private TableColumn<Map,String> nameColumn; // 姓名列
    @FXML
    private TableColumn<Map,String> absentTypeColumn; //请假类型列
    @FXML
    private TableColumn<Map,String> examineColumn; //审批列
    @FXML
    private TableColumn<Map,String> absentReasonColumn; //请假理由列
    @FXML
    private TableColumn<Map,String> startDateColumn;//请假开始日期列
    @FXML
    private TableColumn<Map,String> endDateColumn;//请假结束日期列
    @FXML
    private TableColumn<Map,String> reqColumn;//请假结束日期列
    @FXML
    private Label num;//学号
    @FXML
    private Label name;//姓名
    @FXML
    private Label startDate;//开始日期
    @FXML
    private Label endDate;//结束日期
    @FXML
    private Label absentReason;//请假原因
    @FXML
    private Label absentType;  //请假类型输入域
    @FXML
    private Label examine;  //请假类型输入域
    @FXML
    private ComboBox<OptionItem> absentreqComboBox;
    @FXML
    private TextField numNameTextField;  //查询 姓名学号输入域
    private TextField numField;
    private TextField nameField;
    private Integer studentAbsentId = null;  //当前编辑修改的请假的主键
    private Integer absentId = null;
    private ArrayList<Map> studentAbsentList = new ArrayList();  //

    private List<OptionItem> absentreqList;
    private ObservableList<Map> observableList= FXCollections.observableArrayList();  // TableView渲染列表
    private void setTableViewData() {
        observableList.clear();
        for (int j = 0; j < studentAbsentList.size(); j++) {
            observableList.addAll(FXCollections.observableArrayList(studentAbsentList.get(j)));
        }
        dataTableView.setItems(observableList);
    }
    @FXML
    public void initialize() {
        DataResponse res;
        DataRequest req = new DataRequest();
        req.put("num","");
        res = HttpRequestUtil.request("/api/studentAbsent/getStuAbsent1List", req); //从后台获取所有学生信息列表集合
        if (res != null && res.getCode() == 0) {
            studentAbsentList = (ArrayList<Map>) res.getData();
        }
        numColumn.setCellValueFactory(new MapValueFactory<>("num"));
        nameColumn.setCellValueFactory(new MapValueFactory<>("name"));
        absentTypeColumn.setCellValueFactory(new MapValueFactory<>("absentTypeName"));
        absentReasonColumn.setCellValueFactory(new MapValueFactory<>("absentReason"));
        startDateColumn.setCellValueFactory(new MapValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new MapValueFactory<>("endDate"));
        reqColumn.setCellValueFactory(new  MapValueFactory<>("absentreqName"));
        TableView.TableViewSelectionModel<Map> tsm = dataTableView.getSelectionModel();
        ObservableList<Integer> list = tsm.getSelectedIndices();
        list.addListener(this::onTableRowSelect);
        setTableViewData();
        absentreqList = HttpRequestUtil.getDictionaryOptionItemList("ARN");
        absentreqComboBox.getItems().addAll(absentreqList);

    }
    protected void changeStudentAbsentInfo() {
        Map form = dataTableView.getSelectionModel().getSelectedItem();
        if (form == null) {
            return;
        }
        studentAbsentId = CommonMethod.getInteger(form, "studentAbsentId");
        System.out.println(studentAbsentId);
        DataRequest req = new DataRequest();
        req.put("studentAbsentId", studentAbsentId);
        DataResponse res = HttpRequestUtil.request("/api/studentAbsent/getStuAbsentInfo", req);
        if (res.getCode() != 0) {
            MessageDialog.showDialog(res.getMsg());
            return;
        }
        form = (Map) res.getData();
        num.setText(CommonMethod.getString(form,"num"));
        name.setText(CommonMethod.getString(form,"name"));
        absentType.setText(CommonMethod.getString(form, "absentTypeName"));
        startDate.setText(CommonMethod.getString(form, "startDate"));
        endDate.setText(CommonMethod.getString(form, "endDate"));
        absentReason.setText(CommonMethod.getString(form, "absentReason"));
        //examine.setText(CommonMethod.getString(form, "examine"));
        absentreqComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(absentreqList,CommonMethod.getString(form,"absentreq")));
    }

    /**
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
        DataResponse res = HttpRequestUtil.request("/api/studentAbsent/getStuAbsentList",req);
        if(res != null && res.getCode()== 0) {
            studentAbsentList = (ArrayList<Map>)res.getData();
            setTableViewData();
        }
    }
    //刷新
    @FXML
    protected void onFreshButtonClick(){initialize();}




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
        studentAbsentId = CommonMethod.getInteger(form1, "studentAbsentId");
        //studentAbsentId = CommonMethod.getInteger(form, "studentAbsentId");
        System.out.println(studentAbsentId);
        form.put("num",num.getText());
        form.put("name",name.getText());
        //System.out.println(num.getText());
/*        form.put("dept",deptField.getText());
        form.put("major",majorField.getText());
        form.put("className",classNameField.getText());
        form.put("card",cardField.getText());*/
        if(absentreqComboBox.getSelectionModel() != null && absentreqComboBox.getSelectionModel().getSelectedItem() != null)
            form.put("absentreq",absentreqComboBox.getSelectionModel().getSelectedItem().getValue());
        /*form.put("birthday",birthdayPick.getEditor().getText());
        form.put("email",emailField.getText());
        form.put("phone",phoneField.getText());
        form.put("address",addressField.getText());*/
        DataRequest req = new DataRequest();
        req.put("studentAbsentId", studentAbsentId);
        req.put("form", form);
        DataResponse res = HttpRequestUtil.request("/api/studentAbsent/absentreq",req);
        if (res != null) {
            if (res.getCode() == 0) {
                absentId = CommonMethod.getIntegerFromObject(res.getData());
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
