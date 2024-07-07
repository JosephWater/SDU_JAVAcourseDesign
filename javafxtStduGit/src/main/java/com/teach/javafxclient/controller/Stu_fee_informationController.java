package com.teach.javafxclient.controller;

import com.teach.javafxclient.controller.base.LocalDateStringConverter;
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
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * StudentController 登录交互控制类 对应 student_panel.fxml  对应于学生管理的后台业务处理的控制器，主要获取数据和保存数据的方法不同
 *  @FXML  属性 对应fxml文件中的
 *  @FXML 方法 对应于fxml文件中的 on***Click的属性
 */
public class Stu_fee_informationController extends ToolController {
    private ObservableList<Map> observableList= FXCollections.observableArrayList();
    @FXML
    private TableView<Map> feeTable;


    @FXML
    private TableColumn<Map,String> itemColumn; //体育活动表 组织单位列
    @FXML
    private TableColumn<Map,String> dayColumn; //体育活动表 时间列
    @FXML
    private TableColumn<Map,Double> moneyColumn; //体育活动表 成果列
    @FXML
    private TableColumn<Map,String> placeColumn;//体育活动表 备注列
    @FXML
    private TableColumn<Map,String> wayColumn;//体育活动表 备注列



    @FXML
    private TextField itemField; //学生信息  活动名称输入域
    @FXML
    private DatePicker dayPick;  //学生信息  时间选择域
    @FXML
    private TextField placeField; //学生信息  组织单位输入域
    @FXML
    private TextField moneyField; //学生信息  成果输入域
    @FXML
    private TextField wayField; //学生信息  成果输入域

    private Integer feeId = null;  //当前编辑修改的学生的主键

    private ArrayList<Map> feeList = new ArrayList();  // 学生信息列表数据
    private Integer studentId = null;  //学生主键
    private Integer personId = null;  //学生关联人员主键






    /**
     * 页面加载对象创建完成初始化方法，页面中控件属性的设置，初始数据显示等初始操作都在这里完成，其他代码都事件处理方法里
     */

    @FXML
    public void initialize() {
        itemColumn.setCellValueFactory(new MapValueFactory<>("item"));
        placeColumn.setCellValueFactory(new MapValueFactory<>("place"));
        dayColumn.setCellValueFactory(new MapValueFactory<>("title"));
        moneyColumn.setCellValueFactory(new MapValueFactory<>("value"));
        wayColumn.setCellValueFactory(new MapValueFactory<>("way"));
        getStuFeeInformationData();
    }
    private void getStuFeeInformationData() {
        DataRequest req = new DataRequest();
        DataResponse res;
        res = HttpRequestUtil.request("/api/student/getStuFeeInformationData",req);
        if(res.getCode() != 0)
            return;
        Map data =(Map)res.getData();
        Map info = (Map)data.get("info");
        List<Map> feeList = (List)data.get("feeList");
        for (Map m: feeList) {
            observableList.addAll(FXCollections.observableArrayList(m));
        }
        feeTable.setItems(observableList);  // 表数据显示
    }

    public void clearPanel() {
        feeId = null;
        itemField.setText("");
        placeField.setText("");
        dayPick.getEditor().setText("");
        moneyField.setText("");
        wayField.setText("");
    }


    public void onTableRowSelect(ListChangeListener.Change<? extends Integer> change){
        changeFeeInfo();
    }
    protected void changeFeeInfo() {
        Map form = feeTable.getSelectionModel().getSelectedItem();
        if (form == null) {
            clearPanel();
            return;
        }
        feeId = CommonMethod.getInteger(form, "feeId");
        DataRequest req = new DataRequest();
        req.put("feeId", feeId);
        DataResponse res = HttpRequestUtil.request("/api/fee/getFeeInfo", req);
        if (res.getCode() != 0) {
            MessageDialog.showDialog(res.getMsg());
            return;
        }
        form = (Map) res.getData();
        itemField.setText(CommonMethod.getString(form, "item"));
        placeField.setText(CommonMethod.getString(form, "place"));
        dayPick.getEditor().setText(CommonMethod.getString(form, "day"));
        moneyField.setText(CommonMethod.getString(form, "money"));
        wayField.setText(CommonMethod.getString(form, "way"));

    }
    @FXML
    protected void onDeleteButtonClick() {

    }
    /**
     * 点击保存按钮，保存当前编辑的学生信息，如果是新添加的学生，后台添加学生
     */
    @FXML
    protected void onSaveButtonClick() {

    }

}


