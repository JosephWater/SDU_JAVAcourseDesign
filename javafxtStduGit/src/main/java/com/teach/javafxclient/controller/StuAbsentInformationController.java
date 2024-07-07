package com.teach.javafxclient.controller;

import com.teach.javafxclient.controller.base.ToolController;
import com.teach.javafxclient.request.*;
import com.teach.javafxclient.util.CommonMethod;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * StudentController 登录交互控制类 对应 student_panel.fxml  对应于学生管理的后台业务处理的控制器，主要获取数据和保存数据的方法不同
 *  @FXML  属性 对应fxml文件中的
 *  @FXML 方法 对应于fxml文件中的 on***Click的属性
 */
public class StuAbsentInformationController extends ToolController {
    private ObservableList<Map> observableList= FXCollections.observableArrayList();
    @FXML
    private TableView<Map> absentTable;


    @FXML
    private TableColumn<Map,String> reasonColumn; //体育活动表 组织单位列
    @FXML
    private TableColumn<Map,String> comeColumn; //体育活动表 时间列
    @FXML
    private TableColumn<Map,Double> backColumn; //体育活动表 成果列
    @FXML
    private TableColumn<Map,String> wayColumn;//体育活动表 备注列
    @FXML
    private TableColumn<Map,String> placeColumn;//体育活动表 备注列





    private Integer absentId = null;  //当前编辑修改的学生的主键

    private ArrayList<Map> absentList = new ArrayList();  // 学生信息列表数据
    private Integer studentId = null;  //学生主键
    private Integer personId = null;  //学生关联人员主键






    /**
     * 页面加载对象创建完成初始化方法，页面中控件属性的设置，初始数据显示等初始操作都在这里完成，其他代码都事件处理方法里
     */

    @FXML
    public void initialize() {
        reasonColumn.setCellValueFactory(new MapValueFactory<>("reason"));
        comeColumn.setCellValueFactory(new MapValueFactory<>("come"));
        backColumn.setCellValueFactory(new MapValueFactory<>("back"));
        wayColumn.setCellValueFactory(new MapValueFactory<>("way"));
        placeColumn.setCellValueFactory(new MapValueFactory<>("place"));
        getStuAbsentInformationData();
    }
    private void getStuAbsentInformationData() {
        DataRequest req = new DataRequest();
        DataResponse res;
        res = HttpRequestUtil.request("/api/student/getStuAbsentInformationData",req);
        if(res.getCode() != 0)
            return;
        Map data =(Map)res.getData();
        Map info = (Map)data.get("info");
        studentId = CommonMethod.getInteger(info,"studentId");
        personId = CommonMethod.getInteger(info,"personId");
        List<Map> absentList = (List)data.get("absentList");
        for (Map m: absentList) {
            observableList.addAll(FXCollections.observableArrayList(m));
        }
        absentTable.setItems(observableList);  // 表数据显示
    }


}


