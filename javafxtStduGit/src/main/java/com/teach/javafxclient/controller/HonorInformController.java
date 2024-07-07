package com.teach.javafxclient.controller;

import com.teach.javafxclient.request.*;
import com.teach.javafxclient.util.CommonMethod;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;

import java.util.ArrayList;
import java.util.Map;


public class HonorInformController {
    @FXML
    private TableView<Map> dataTableView;
    @FXML
    private TableColumn<Map,String> honorNameColumn;
    @FXML
    private TableColumn<Map,String> honorGetTimeColumn;
    @FXML
    private TableColumn<Map,String> honorGradeColumn;
    @FXML
    private TableColumn<Map,String> honorTypeColumn;


    private Integer honorId = null;
    private ArrayList<Map> honorList = new ArrayList();  // 荣誉信息列表数据
    private ObservableList<Map> observableList= FXCollections.observableArrayList();  // TableView渲染列表
    private void setTableViewData() {
        observableList.clear();
        for (int j = 0; j < honorList.size(); j++) {
            observableList.addAll(FXCollections.observableArrayList(honorList.get(j)));
        }
        dataTableView.setItems(observableList);
    }
    @FXML
    public void initialize() {
        DataResponse res;
        DataRequest req = new DataRequest();
        res = HttpRequestUtil.request("/api/student/getStudentIntroduceData", req); //从后台获取所有学生信息列表集合
        Map data =(Map)res.getData();
        Map info = (Map)data.get("info");
        String num=CommonMethod.getString(info,"num");
        req.put("num",num);
        res = HttpRequestUtil.request("/api/honor/getHonor1List", req); //从后台获取所有学生信息列表集合
        if (res != null && res.getCode() == 0) {
            honorList = (ArrayList<Map>) res.getData();
        }
        honorGetTimeColumn.setCellValueFactory(new MapValueFactory<>("honorGetTime"));
        honorTypeColumn.setCellValueFactory(new MapValueFactory<>("honorType"));
        honorGradeColumn.setCellValueFactory(new MapValueFactory<>("honorGrade"));
        honorNameColumn.setCellValueFactory(new MapValueFactory<>("honorName"));
        setTableViewData();
    }
}
