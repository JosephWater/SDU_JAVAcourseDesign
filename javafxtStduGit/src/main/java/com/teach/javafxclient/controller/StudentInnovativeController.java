package com.teach.javafxclient.controller;
import com.teach.javafxclient.controller.base.ToolController;
import com.teach.javafxclient.request.DataRequest;
import com.teach.javafxclient.request.DataResponse;
import com.teach.javafxclient.request.HttpRequestUtil;
import com.teach.javafxclient.util.CommonMethod;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.image.ImageView;

import java.util.List;
import java.util.Map;

public class StudentInnovativeController extends ToolController{
    private ObservableList<Map> observableList= FXCollections.observableArrayList();
    @FXML
    private TableView<Map> innovativePracticeTable;//创新实践信息表
    @FXML
    private TableColumn<Map,String> ipnumColumn;
    @FXML
    private TableColumn<Map,String> ipkindsColumn;
    @FXML
    private TableColumn<Map,String> ipnameColumn;
    @FXML
    private TableColumn<Map,String> iptimeColumn;
    @FXML
    private TableColumn<Map,String> ipplaceColumn;
    @FXML
    private TableColumn<Map,String> iporgColumn;
    @FXML
    private TableColumn<Map,String> iphonorColumn;
    @FXML
    private TableColumn<Map,String> ipcontentColumn;

    private Integer studentId = null;  //学生主键
    private Integer personId = null;  //学生关联人员主键
    @FXML
    public void initialize() {
        ipnumColumn.setCellValueFactory(new MapValueFactory("ipnum"));
        ipkindsColumn.setCellValueFactory(new MapValueFactory<>("ipkindsName"));
        ipnameColumn.setCellValueFactory(new MapValueFactory<>("ipname"));
        iptimeColumn.setCellValueFactory(new MapValueFactory<>("iptime"));
        ipplaceColumn.setCellValueFactory(new MapValueFactory<>("ipplace"));
        iporgColumn.setCellValueFactory(new MapValueFactory<>("iporg"));
        iphonorColumn.setCellValueFactory(new MapValueFactory<>("iphonor"));
        ipcontentColumn.setCellValueFactory(new MapValueFactory<>("ipcontent"));

        getStuIpInformationData();
    }

    private void getStuIpInformationData() {
        DataRequest req = new DataRequest();
        DataResponse res;
        res = HttpRequestUtil.request("/api/student/getStuIpInformationData",req);
        if(res.getCode() != 0)
            return;
        Map data =(Map)res.getData();
        Map info = (Map)data.get("info");
        studentId = CommonMethod.getInteger(info,"studentId");
        personId = CommonMethod.getInteger(info,"personId");
        List<Map> innovativePracticeList = (List)data.get("innovativePracticeList");
        for (Map m: innovativePracticeList) {
            observableList.addAll(FXCollections.observableArrayList(m));
        }
        innovativePracticeTable.setItems(observableList);  // 表数据显示
    }

}
