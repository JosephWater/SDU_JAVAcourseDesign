package com.teach.javafxclient.controller;

import com.teach.javafxclient.AppStore;
import com.teach.javafxclient.controller.base.MessageDialog;
import com.teach.javafxclient.model.Course;
import com.teach.javafxclient.request.DataRequest;
import com.teach.javafxclient.request.DataResponse;
import com.teach.javafxclient.request.HttpRequestUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StuMyCourseController {
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
    private Label courseCenterNumLabel;

    @FXML
    private Label courseCenterNameLabel;

    @FXML
    private Label courseCenterCreditLabel;

    @FXML
    private Label courseCenterTimeLabel;

    @FXML
    private Label courseCenterPlaceLabel;

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
        DataResponse res;
        DataRequest req = new DataRequest();
        req.put("userId", AppStore.getJwt().getId());
        res = HttpRequestUtil.request("/api/course/select/get", req);
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


    @FXML
    protected void onRefreshButtonClick() {
        DataResponse res;
        courseList.clear();
        DataRequest req = new DataRequest();
        req.put("userId", AppStore.getJwt().getId());
        res = HttpRequestUtil.request("/api/course/select/get", req);
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
}
