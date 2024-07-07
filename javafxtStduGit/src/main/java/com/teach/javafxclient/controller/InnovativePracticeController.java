package com.teach.javafxclient.controller;

import com.teach.javafxclient.controller.base.LocalDateStringConverter;
import com.teach.javafxclient.controller.base.MessageDialog;
import com.teach.javafxclient.controller.base.ToolController;
import com.teach.javafxclient.request.DataRequest;
import com.teach.javafxclient.request.DataResponse;
import com.teach.javafxclient.request.HttpRequestUtil;
import com.teach.javafxclient.request.OptionItem;
import com.teach.javafxclient.util.CommonMethod;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InnovativePracticeController extends ToolController {
    @FXML
    private TableView<Map> dataTableView;//创新实践信息表
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
    private TableColumn<Map,String> ipcontentColumn;
    @FXML
    private TableColumn<Map,String> iphonorColumn;
    @FXML
    private TableColumn<Map,String> numColumn;
    @FXML
    private TableColumn<Map,String> nameColumn;

    @FXML
    private TextField ipnumField; //创新实践信息
    @FXML
    private ComboBox<OptionItem> ipkindsComboBox;//创新实践信息
    @FXML
    private TextField ipnameField;
    @FXML
    private DatePicker iptimePick;//创新实践信息
    @FXML
    private TextField ipplaceField;//创新实践信息
    @FXML
    private TextField numField;//创新实践信息
    @FXML
    private TextField nameField;//创新实践信息
    @FXML
    private TextField iporgField;
    @FXML
    private TextField iphonorField;
    @FXML
    private TextField ipcontentField;
    @FXML
    private TextField ipnumIpnameField;  //查询 输入域
    private Integer innovativePracticeId = null;  //当前编辑修改的创新实践的主键
    private ArrayList<Map> innovativePracticeList = new ArrayList();  // 创新实践信息列表数据
    private List<OptionItem> ipkindsList;
    private ObservableList<Map> observableList= FXCollections.observableArrayList();  // TableView渲染列表
    private Stage stage = null;

    private void setTableViewData(){
        observableList.clear();
        for(int j = 0;j<innovativePracticeList.size();j++){
            observableList.addAll(FXCollections.observableArrayList(innovativePracticeList.get(j)));
        }
        dataTableView.setItems(observableList);
    }
    @FXML
    public  void initialize(){
        DataResponse res;
        DataRequest req = new DataRequest();
        req.put("ipnumIpname","");
        res = HttpRequestUtil.request("/api/innovativePractice/getInnovativePracticeList",req);
        if(res !=null&&res.getCode()== 0){
            innovativePracticeList = (ArrayList<Map>) res.getData();
        }
        ipnumColumn.setCellValueFactory(new MapValueFactory("ipnum"));
        ipkindsColumn.setCellValueFactory(new MapValueFactory<>("ipkindsName"));
        ipnameColumn.setCellValueFactory(new MapValueFactory<>("ipname"));
        iptimeColumn.setCellValueFactory(new MapValueFactory<>("iptime"));
        ipplaceColumn.setCellValueFactory(new MapValueFactory<>("ipplace"));
        iporgColumn.setCellValueFactory(new MapValueFactory<>("iporg"));
        iphonorColumn.setCellValueFactory(new MapValueFactory<>("iphonor"));
        ipcontentColumn.setCellValueFactory(new MapValueFactory<>("ipcontent"));
        numColumn.setCellValueFactory(new MapValueFactory<>("num"));
        nameColumn.setCellValueFactory(new MapValueFactory<>("name"));
        TableView.TableViewSelectionModel<Map> tsm = dataTableView.getSelectionModel();
        ObservableList<Integer> list = tsm.getSelectedIndices();
        list.addListener(this::onTableRowSelect);
        setTableViewData();
        ipkindsList = HttpRequestUtil.getDictionaryOptionItemList("CRE");
        ipkindsComboBox.getItems().addAll(ipkindsList);
        iptimePick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
    }
    public  void clearPanel() {
        innovativePracticeId = null;
        ipnumField.setText("");
        ipkindsComboBox.getSelectionModel().select(-1);
        ipnameField.setText("");
        iptimePick.getEditor().setText("");
        ipplaceField.setText("");
        numField.setText("");
        nameField.setText("");
    }
    /**
     * 点击创新实践列表的某一行，根据innovativePracticeId ,从后台查询创新实践的基本信息，切换创新实践的编辑信息
     */

    public void onTableRowSelect(ListChangeListener.Change<? extends Integer> change){
        changeInnovativePracticeInfo();
    }

    protected void changeInnovativePracticeInfo() {
        Map form = dataTableView.getSelectionModel().getSelectedItem();
        if(form == null){
            clearPanel();
            return;
        }
        innovativePracticeId = CommonMethod.getInteger(form,"innovativePracticeId");
        DataRequest req = new DataRequest();
        req.put("innovativePracticeId",innovativePracticeId);
        DataResponse res = HttpRequestUtil.request("/api/innovativePractice/getInnovativePracticeInfo",req);
        if(res.getCode()!=0){
            MessageDialog.showDialog(res.getMsg());
            return;
        }
        form = (Map)res.getData();
        ipnumField.setText(CommonMethod.getString(form, "ipnum"));
        ipkindsComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(ipkindsList,CommonMethod.getString(form,"ipkinds")));
        ipnameField.setText(CommonMethod.getString(form, "ipname"));
        iptimePick.getEditor().setText(CommonMethod.getString(form, "iptime"));
        ipplaceField.setText(CommonMethod.getString(form, "ipplace"));
        iporgField.setText(CommonMethod.getString(form, "iporg"));
        iphonorField.setText(CommonMethod.getString(form, "iphonor"));
        ipcontentField.setText(CommonMethod.getString(form, "ipcontent"));
        numField.setText(CommonMethod.getString(form, "num"));
        nameField.setText(CommonMethod.getString(form, "name"));
    }
    @FXML
    protected void onQueryButtonClick(){
        String ipnumIpname = ipnumIpnameField.getText();
        DataRequest req = new DataRequest();
        req.put("ipnumIpname",ipnumIpname);
        DataResponse res = HttpRequestUtil.request("/api/innovativePractice/getInnovativePracticeList",req);
        if(res != null && res.getCode()== 0) {
            innovativePracticeList = (ArrayList<Map>)res.getData();
            setTableViewData();
        }
    }
    @FXML
    protected void onAddButtonClick(){
        clearPanel();
    }
    /**
     *  添加新创新实践， 清空输入信息， 输入相关信息，点击保存即可添加新的信息
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
        Integer innovativePracticeId = CommonMethod.getInteger(form,"innovativePracticeId");
        DataRequest req = new DataRequest();
        req.put("innovativePracticeId", innovativePracticeId);
        DataResponse res = HttpRequestUtil.request("/api/innovativePractice/innovativePracticeDelete",req);
        if(res.getCode() == 0) {
            MessageDialog.showDialog("删除成功！");
            onQueryButtonClick();
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
    }

    @FXML
    protected void onSaveButtonClick() {
        if( numField.getText().equals("")) {
            MessageDialog.showDialog("学号为空，不能修改");
            return;
        }
        Map form = new HashMap();
        form.put("ipnum",ipnumField.getText());
        if(ipkindsComboBox.getSelectionModel() != null && ipkindsComboBox.getSelectionModel().getSelectedItem() != null)
            form.put("ipkinds",ipkindsComboBox.getSelectionModel().getSelectedItem().getValue());
        form.put("ipname",ipnameField.getText());
        form.put("iptime",iptimePick.getEditor().getText());
        form.put("ipplace",ipplaceField.getText());
        form.put("iporg",iporgField.getText());
        form.put("iphonor",iphonorField.getText());
        form.put("ipcontent",ipcontentField.getText());
        form.put("num",numField.getText());
        form.put("name",nameField.getText());
        DataRequest req = new DataRequest();
        req.put("innovativePracticeId", innovativePracticeId);
        req.put("form", form);
        DataResponse res = HttpRequestUtil.request("/api/innovativePractice/innovativePracticeEditSave",req);
        if(res.getCode() == 0) {
            innovativePracticeId = CommonMethod.getIntegerFromObject(res.getData());
            MessageDialog.showDialog("提交成功！");
            onQueryButtonClick();
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
    }

    public void doNew(){
        clearPanel();
    }
    public void doSave(){
        onSaveButtonClick();
    }
    public void doDelete(){
        onDeleteButtonClick();
    }



}


