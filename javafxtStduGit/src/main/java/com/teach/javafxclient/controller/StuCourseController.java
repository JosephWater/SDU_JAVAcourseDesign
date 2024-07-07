package com.teach.javafxclient.controller;

import com.teach.javafxclient.AppStore;
import com.teach.javafxclient.controller.base.MessageDialog;
import com.teach.javafxclient.model.Course;
import com.teach.javafxclient.request.DataRequest;
import com.teach.javafxclient.request.DataResponse;
import com.teach.javafxclient.request.HttpRequestUtil;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StuCourseController {
    @FXML
    public TextField numNameTextField;
    @FXML
    private TableView<Course> dataTableView;
    @FXML
    private TableColumn<Course, String> courseCenterNumColumn;

    @FXML
    private TableColumn<Course, String> courseCenterNameColumn;

    @FXML
    private TableColumn<Course, String> courseCenterCreditColumn;

    @FXML
    private TableColumn<Course, String> courseCenterTimeColumn;

    @FXML
    private TableColumn<Course, String> courseCenterPlaceColumn;

    @FXML
    private Label courseCenterIdLabel;


    @FXML
    private Label courseCenterNumLabel;

    @FXML
    private Label courseCenterNameLabel;

    @FXML
    private Label courseCenterCreditLabel;

    @FXML
    private Label courseCenterTimeLabel;

    @FXML
    private Label courseCenterPlaceLabel;
    @FXML
    private Label courseScorePlaceLabel;

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
        DataRequest req = new DataRequest();
        req.put("courseId", courseId);
        res = HttpRequestUtil.request("/api/course/getCourseList", req);
        if (res != null && res.getCode() == 0) {
            ArrayList<Map> dataList = (ArrayList<Map>) res.getData();
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
        if (form == null) {
            clearPanel();
            return;
        }
        courseCenterIdLabel.setText(String.valueOf(form.getCourseId()));
        courseCenterCreditLabel.setText(String.valueOf(form.getCredit()));
        courseCenterNumLabel.setText(String.valueOf(form.getNum()));
        courseCenterNameLabel.setText(String.valueOf(form.getName()));
        courseCenterPlaceLabel.setText(String.valueOf(form.getCoursePlace()));
        courseCenterTimeLabel.setText((String.valueOf(form.getCourseTime())));
    }

    public void onTableRowSelect(ListChangeListener.Change<? extends Integer> change) {
        changeCourseInfo();
    }

    public void clearPanel() {
        courseCenterIdLabel.setText("");
        courseCenterCreditLabel.setText("");
        courseCenterNumLabel.setText("");
        courseCenterNameLabel.setText("");
        courseCenterPlaceLabel.setText("");
        courseCenterTimeLabel.setText("");
    }

    @FXML
    protected void onQueryButtonClick() {
       /* String numName = numNameTextField.getText();
        DataRequest req = new DataRequest();
        req.put("numName",numName);*/
        //courseList = SQLiteJDBC.getInstance().getCourseList(numName);
        for (Course item : dataTableView.getItems()) {
            if (item.getNum().equals(numNameTextField.getText())) {
                dataTableView.getSelectionModel().select(item);
                courseCenterIdLabel.setText(String.valueOf(item.getCourseId()));
                courseCenterCreditLabel.setText(String.valueOf(item.getCredit()));
                courseCenterNumLabel.setText(String.valueOf(item.getNum()));
                courseCenterNameLabel.setText(String.valueOf(item.getName()));
                courseCenterPlaceLabel.setText(String.valueOf(item.getCoursePlace()));
                courseCenterTimeLabel.setText((String.valueOf(item.getCourseTime())));
            }
        }
    }


    @FXML
    protected void onDeleteButtonClick() {
        Course form = dataTableView.getSelectionModel().getSelectedItem();
        if (form == null) {
            MessageDialog.showDialog("没有选择课程！");
            return;
        }

        DataResponse res;
        DataRequest req = new DataRequest();
        HashMap<String, Integer> data = new HashMap<>();;
        data.put("userId", AppStore.getJwt().getId());
        data.put("courseId", form.getCourseId());
        req.setData(data);
        res = HttpRequestUtil.request("/api/course/select/delete", req);
        if (0 == res.getCode()) {
            MessageDialog.showDialog(res.getMsg());
        }

    }

    @FXML
    protected void onSaveButtonClick() {
        Course form = dataTableView.getSelectionModel().getSelectedItem();
        if (form == null) {
            MessageDialog.showDialog("没有选择课程！");
            return;
        }
        DataResponse res;
        DataRequest req = new DataRequest();
        HashMap<String, Integer> data = new HashMap<>();
        data.put("userId", AppStore.getJwt().getId());
        data.put("courseId", form.getCourseId());
        req.setData(data);
        res = HttpRequestUtil.request("/api/course/select", req);
        if (res.getCode() == 0) {
            MessageDialog.showDialog(res.getMsg());
        }
    }
}
