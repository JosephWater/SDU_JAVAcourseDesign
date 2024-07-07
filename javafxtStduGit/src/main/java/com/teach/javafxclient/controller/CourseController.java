package com.teach.javafxclient.controller;

import com.teach.javafxclient.controller.base.MessageDialog;
import com.teach.javafxclient.controller.base.ToolController;
import com.teach.javafxclient.model.Course;
import com.teach.javafxclient.model.Student;
import com.teach.javafxclient.request.DataRequest;
import com.teach.javafxclient.request.DataResponse;
import com.teach.javafxclient.request.HttpRequestUtil;
import com.teach.javafxclient.request.SQLiteJDBC;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CourseController 登录交互控制类 对应 course-panel.fxml
 *  @FXML  属性 对应fxml文件中的
 *  @FXML 方法 对应于fxml文件中的 on***Click的属性
 */
public class CourseController extends ToolController {
    @FXML
    public TextField numNameTextField;
    @FXML
    private TableView<Course> dataTableView;
    @FXML
    private TableColumn<Course,String> courseCenterNumColumn;
    @FXML
    private Button upButton;

    @FXML
    private TableColumn<Course,String> courseCenterNameColumn;

    @FXML
    private TableColumn<Course,String> courseCenterCreditColumn;

    @FXML
    private TableColumn<Course,String> courseCenterTimeColumn;

    @FXML
    private TableColumn<Course,String> courseCenterPlaceColumn;

    @FXML
    private TextField courseCenterIdField;

    @FXML
    private TextField courseCenterNumField;

    @FXML
    private TextField courseCenterNameField;

    @FXML
    private TextField courseCenterCreditField;

    @FXML
    private DatePicker courseCenterTimePick;

    @FXML
    private TextField courseCenterPlaceField;

    private List<Course> courseList = new ArrayList();

    private void setTableViewData() {
        dataTableView.getItems().clear();
        for (int j = 0; j < courseList.size(); j++) {
            dataTableView.getItems().add(courseList.get(j));
        }
    }

    /**
     * 页面加载对象创建完成初始话方法，页面中控件属性的设置，初始数据显示等初始操作都在这里完成，其他代码都事件处理方法里
     */
    @FXML
    public void initialize() {
        courseCenterNumColumn.setCellValueFactory(new PropertyValueFactory("num"));  //设置列值工程属性
        courseCenterNameColumn.setCellValueFactory(new PropertyValueFactory("name"));
        courseCenterCreditColumn.setCellValueFactory(new PropertyValueFactory("credit"));
        courseCenterTimeColumn.setCellValueFactory(new PropertyValueFactory("courseTime"));
        courseCenterPlaceColumn.setCellValueFactory(new PropertyValueFactory("coursePlace"));
        TableView.TableViewSelectionModel<Course> tsm = dataTableView.getSelectionModel();
        ObservableList<Integer> list = tsm.getSelectedIndices();
        list.addListener(this::onTableRowSelect);

        Integer courseId = 0;
        DataResponse res;
        DataRequest req =new DataRequest();
        req.put("studentId",courseId);
        res = HttpRequestUtil.request("/api/course/getCourseList",req);
        if(res != null && res.getCode()== 0) {
            ArrayList<Map> dataList = (ArrayList<Map>)res.getData();
            for (Map map : dataList) {

                Course course = new Course();
                double credit = (Double) map.get("credit");
                double courseIdValue = (Double) map.get("courseId");
                course.setCourseId(map.get("courseId") == null ? null : (int) courseIdValue);
                course.setNum(map.get("num") == null ? "" : (String) map.get("num"));
                course.setName(map.get("name") == null ? "" : (String) map.get("name"));
                course.setCredit(map.get("credit") == null ? null : (int) credit);
                course.setCoursePlace(map.get("coursePlace") == null ? "" : (String) map.get("coursePlace"));
                course.setCourseTime(map.get("courseTime") == null ? "" : (String) map.get("courseTime"));

                courseList.add(course);
            }
        }
        setTableViewData();
    }
    protected void changeCourseInfo() {
        Course form = dataTableView.getSelectionModel().getSelectedItem();
        upButton.setText("修改");
        if(form == null) {
            clearPanel();
            return;
        }
        courseCenterIdField.setText(String.valueOf(form.getCourseId()));
        courseCenterCreditField.setText(String.valueOf(form.getCredit()));
        courseCenterNumField.setText(String.valueOf(form.getNum()));
        courseCenterNameField.setText(String.valueOf(form.getName()));
        courseCenterPlaceField.setText(String.valueOf(form.getCoursePlace()));
        courseCenterTimePick.getEditor().setText((String.valueOf(form.getCourseTime())));
    }
    public void onTableRowSelect(ListChangeListener.Change<? extends Integer> change){
        changeCourseInfo();
    }
    public void clearPanel(){
        courseCenterIdField.setText("");
        courseCenterCreditField.setText("");
        courseCenterNumField.setText("");
        courseCenterNameField.setText("");
        courseCenterPlaceField.setText("");
        courseCenterTimePick.getEditor().setText("");
        upButton.setText("新增");
    }
    @FXML
    protected void onQueryButtonClick() {
       /* String numName = numNameTextField.getText();
        DataRequest req = new DataRequest();
        req.put("numName",numName);*/
        //courseList = SQLiteJDBC.getInstance().getCourseList(numName);
        for (Course item : dataTableView.getItems()) {
            if (item.getNum().equals(numNameTextField.getText())){
                dataTableView.getSelectionModel().select(item);
                courseCenterIdField.setText(String.valueOf(item.getCourseId()));
                courseCenterCreditField.setText(String.valueOf(item.getCredit()));
                courseCenterNumField.setText(String.valueOf(item.getNum()));
                courseCenterNameField.setText(String.valueOf(item.getName()));
                courseCenterPlaceField.setText(String.valueOf(item.getCoursePlace()));
                courseCenterTimePick.getEditor().setText((String.valueOf(item.getCourseTime())));
            }
        }

    }
    @FXML
    protected void onAddButtonClick() {
        clearPanel();
    }
    @FXML
    protected void onDeleteButtonClick() {
        Course form = dataTableView.getSelectionModel().getSelectedItem();
        if(form == null) {
            MessageDialog.showDialog("没有选择删除项！");
            return;
        }
        int ret = MessageDialog.choiceDialog("确认要删除吗?");
        if(ret == MessageDialog.CHOICE_YES) {
            DataResponse res;
            DataRequest req =new DataRequest();
            HashMap<String,Integer> data = new HashMap<>();
            data.put("courseId",form.getCourseId());
            req.setData(data);
            res = HttpRequestUtil.request("/api/course/delete",req);
            if (0 == res.getCode()){
                dataTableView.getItems().remove(form);
                MessageDialog.showDialog(res.getMsg());
            }
        }
    }
    @FXML
    protected void onSaveButtonClick() {
        Course form = dataTableView.getSelectionModel().getSelectedItem();
        if (checkFiled()){
            DataResponse res;
            DataRequest req =new DataRequest();
            HashMap<String,String> data = new HashMap<>();
            if (upButton.getText().equals("修改")){
                data.put("courseId",courseCenterIdField.getText());
            }
            data.put("num",courseCenterNumField.getText());
            data.put("name",courseCenterNameField.getText());
            data.put("credit",courseCenterCreditField.getText());
            data.put("coursePlace",courseCenterPlaceField.getText());
            data.put("courseTime",courseCenterTimePick.getEditor().getText());
            req.setData(data);
            res = HttpRequestUtil.request("/api/course/save",req);
            if (res.getCode() == 0){
                Map course = (Map) res.getData();
                dataTableView.getItems().remove(form);
                dataTableView.getItems().add(new Course(
                                Float.parseFloat(course.get("courseId").toString()),
                                course.get("num").toString(),
                                course.get("name").toString(),
                                Float.parseFloat(course.get("credit").toString()),
                                course.get("coursePlace").toString(),
                                course.get("courseTime").toString()
                        ));
                dataTableView.sort();
                MessageDialog.showDialog(res.getMsg());
            }
        }

    }
    private boolean checkFiled(){
        if(courseCenterNumField.getText().equals("")) {
            MessageDialog.showDialog("课程编号不能为空");
            return false;
        }
        if(courseCenterNameField.getText().equals("")) {
            MessageDialog.showDialog("课程名不能为空");
            return false;
        }
        if(courseCenterCreditField.getText().equals("")) {
            MessageDialog.showDialog("课程学分不能为空");
            return false;
        }
        if(courseCenterTimePick.getEditor().getText().equals("")) {
            MessageDialog.showDialog("上课时间不能为空");
            return false;
        }
        if(courseCenterPlaceField.getText().equals("")) {
            MessageDialog.showDialog("上课地点不能为空");
            return false;
        }
        return true;
    }

}
