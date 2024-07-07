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
 * HonorController 登录交互控制类 对应 honor_panel.fxml  对应荣誉信息管理的后台业务处理的控制器，主要获取数据和保存数据的方法不同
 *  @FXML  属性 对应fxml文件中的
 *  @FXML 方法 对应于fxml文件中的 on***Click的属性
 */
public class HonorController extends ToolController {
    @FXML
    private TableView<Map> dataTableView;
    @FXML
    private TableColumn<Map,String> numColumn;
    @FXML
    private TableColumn<Map,String> nameColumn;
    @FXML
    private TableColumn<Map,String> honorNameColumn;
    @FXML
    private TableColumn<Map,String> honorGradeColumn;
    @FXML
    private TableColumn<Map,String> honorGetTimeColumn;
    @FXML
    private TableColumn<Map,String> honorTypeColumn;


    @FXML
    private TextField numField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField honorNameField;
    @FXML
    private TextField honorGradeField;;
    @FXML
    private DatePicker honorGetTimePick;
    @FXML
    private TextField honorTypeField;



    @FXML
    private TextField numNameTextField;
    private Integer honorId = null;
    private ArrayList<Map> honorList = new ArrayList();
    private ObservableList<Map> observableList= FXCollections.observableArrayList();

    /**
     * 将学生荣誉信息数据集合设置到面板上显示
     */
    private void setTableViewData() {
        observableList.clear();
        for (int j = 0; j < honorList.size(); j++) {
            observableList.addAll(FXCollections.observableArrayList(honorList.get(j)));
        }
        dataTableView.setItems(observableList);
    }
    /**
     * 页面加载对象创建完成初始化方法，页面中控件属性的设置，初始数据显示等初始操作都在这里完成，其他代码都事件处理方法里
     */

    @FXML
    public void initialize() {
        DataResponse res;
        DataRequest req = new DataRequest();
        req.put("numName", "");
        res = HttpRequestUtil.request("/api/honor/getHonorList", req); //从后台获取所有学生荣誉信息信息列表集合
        if (res != null && res.getCode() == 0) {
            honorList = (ArrayList<Map>) res.getData();
        }
        numColumn.setCellValueFactory(new MapValueFactory("num"));
        nameColumn.setCellValueFactory(new MapValueFactory("name"));
        honorNameColumn.setCellValueFactory(new MapValueFactory<>("honorName"));
        honorGradeColumn.setCellValueFactory(new MapValueFactory<>("honorGrade"));
        honorGetTimeColumn.setCellValueFactory(new MapValueFactory<>("honorGetTime"));
        honorTypeColumn.setCellValueFactory(new MapValueFactory<>("honorType"));
        TableView.TableViewSelectionModel<Map> tsm = dataTableView.getSelectionModel();
        ObservableList<Integer> list = tsm.getSelectedIndices();
        list.addListener(this::onTableRowSelect);
        setTableViewData();
        honorGetTimePick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
    }

    /**
     * 清除学生荣誉信息表单中输入信息
     */
    public void clearPanel(){
        honorId = null;
        numField.setText("");
        nameField.setText("");
        honorGradeField.setText("");
        honorTypeField.setText("");
        honorGetTimePick.getEditor().setText("");
        honorNameField.setText("");
    }

    protected void changeHonorInfo() {
        Map form = dataTableView.getSelectionModel().getSelectedItem();
        if (form == null) {
            clearPanel();
            return;
        }
        honorId = CommonMethod.getInteger(form, "honorId");
        DataRequest req = new DataRequest();
        req.put("honorId", honorId);
        DataResponse res = HttpRequestUtil.request("/api/honor/getHonorInfo", req);
        if (res.getCode() != 0) {
            MessageDialog.showDialog(res.getMsg());
            return;
        }
        form = (Map) res.getData();
        numField.setText(CommonMethod.getString(form, "num"));
        nameField.setText(CommonMethod.getString(form, "name"));
        honorNameField.setText(CommonMethod.getString(form, "honorName"));
        honorGradeField.setText(CommonMethod.getString(form, "honorGrade"));
        honorGradeField.setText(CommonMethod.getString(form, "honorType"));
        honorGetTimePick.getEditor().setText(CommonMethod.getString(form, "honorGetTime"));
    }
    /**
     * 点击学生列表的某一行，根据honorId ,从后台查询学生的基本信息，切换学生的编辑信息
     */
    public void onTableRowSelect(ListChangeListener.Change<? extends Integer>  change){
        changeHonorInfo();
    }

    /**
     * 点击查询按钮，从从后台根据输入的串，查询匹配的学生在学生荣誉信息列表中显示
     */
    @FXML
    protected void onQueryButtonClick() {
        String numName = numNameTextField.getText();
        DataRequest req = new DataRequest();
        req.put("numName",numName);
        DataResponse res = HttpRequestUtil.request("/api/honor/getHonorList",req);
        if(res != null && res.getCode()== 0) {
            honorList = (ArrayList<Map>)res.getData();
            setTableViewData();
        }

    }
    /**
     *  添加新荣誉信息， 清空输入信息， 输入相关信息，点击保存即可添加新的学生
     */
    @FXML
    protected void onAddButtonClick() {
        clearPanel();
    }

    /**
     * 点击删除按钮 删除当前编辑的学生荣誉信息的数据
     */
    @FXML
    protected void onDeleteButtonClick() {
        Map form = dataTableView.getSelectionModel().getSelectedItem();
        if(form == null) {
            MessageDialog.showDialog("没有选择，不能删除");
            return;
        }
        int ret = MessageDialog.choiceDialog("确认要删除吗?");
        if(ret != MessageDialog.CHOICE_YES) {
            return;
        }
        honorId = CommonMethod.getInteger(form,"honorId");
        DataRequest req = new DataRequest();
        req.put("honorId", honorId);
        DataResponse res = HttpRequestUtil.request("/api/honor/honorDelete",req);
        if(res.getCode() == 0) {
            MessageDialog.showDialog("删除成功！");
            onQueryButtonClick();
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
    }
    /**
     * 点击保存按钮，保存当前编辑的学生荣誉信息，如果是新添加的学生，后台添加学生
     */
    @FXML
    protected void onSaveButtonClick() {
        numField.getText();
        if( numField.getText().equals("")) {
            MessageDialog.showDialog("学号为空，不能修改");
            return;
        }
        Map form = new HashMap();
        form.put("num",numField.getText());
        form.put("name",nameField.getText());
        form.put("honorName",honorNameField.getText());
        form.put("honorGrade",honorGradeField.getText());
        form.put("honorType",honorTypeField.getText());
        form.put("honorGetTime",honorGetTimePick.getEditor().getText());
        DataRequest req = new DataRequest();
        req.put("honorId", honorId);
        req.put("form", form);
        DataResponse res = HttpRequestUtil.request("/api/honor/honorEditSave",req);
        if(res.getCode() == 0) {
            honorId = CommonMethod.getIntegerFromObject(res.getData());
            MessageDialog.showDialog("提交成功！");
            onQueryButtonClick();
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }

    }










}