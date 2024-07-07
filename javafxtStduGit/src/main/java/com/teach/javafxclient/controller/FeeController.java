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
public class FeeController extends ToolController {
    @FXML
    private TableView<Map> dataTableView;

    @FXML
    private TableColumn<Map,String> numColumn; //消费记录 学号列
    @FXML
    private TableColumn<Map,String> nameColumn;  //消费记录 姓名列
    @FXML
    private TableColumn<Map,String> itemColumn; //消费记录 消费物品列
    @FXML
    private TableColumn<Map,String> dayColumn; //消费记录 消费时间列
    @FXML
    private TableColumn<Map,Double> moneyColumn; //消费记录 消费金额列
    @FXML
    private TableColumn<Map,String> placeColumn;//消费记录 消费地点列
    @FXML
    private TableColumn<Map,String> wayColumn;//消费记录 消费方式列

    @FXML
    private TextField numField;  //学生信息  学号输入域
    @FXML
    private TextField nameField; //学生信息  姓名输入域
    @FXML
    private TextField itemField; //学生信息  消费物品输入域
    @FXML
    private DatePicker dayPick;  //学生信息  消费时间输入域
    @FXML
    private TextField moneyField; //学生信息  消费金额输入域
    @FXML
    private TextField placeField; //学生信息  消费地点输入域
    @FXML
    private ComboBox<OptionItem> wayComboBox; //学生信息  消费方式输入域


    @FXML
    private TextField numNameTextField;  //查询 姓名学号输入域

    private Integer feeId = null;  //当前编辑修改的学生的主键

    private ArrayList<Map> feeList = new ArrayList();  // 学生信息列表数据
    private List<OptionItem> wayList;   //性别选择列表数据
    private ObservableList<Map> observableList= FXCollections.observableArrayList();  // TableView渲染列表


    /**
     * 将学生数据集合设置到面板上显示
     */
    private void setTableViewData() {
        observableList.clear();
        for (int j = 0; j < feeList.size(); j++) {
            observableList.addAll(FXCollections.observableArrayList(feeList.get(j)));
        }
        dataTableView.setItems(observableList);
    }

    /**
     * 页面加载对象创建完成初始化方法，页面中控件属性的设置，初始数据显示等初始操作都在这里完成，其他代码都事件处理方法里
     */

    @FXML
    public void initialize() {
        DataResponse res;
        DataRequest req =new DataRequest();
        req.put("numName","");
        res = HttpRequestUtil.request("/api/fee/getFeeList",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            feeList = (ArrayList<Map>)res.getData();
            System.out.println(feeList);
        }
        numColumn.setCellValueFactory(new MapValueFactory<>("num"));
        nameColumn.setCellValueFactory(new MapValueFactory<>("name"));
        itemColumn.setCellValueFactory(new MapValueFactory<>("item"));
        dayColumn.setCellValueFactory(new MapValueFactory<>("day"));
        moneyColumn.setCellValueFactory(new MapValueFactory<>("money"));
        placeColumn.setCellValueFactory(new MapValueFactory<>("place"));
        wayColumn.setCellValueFactory(new MapValueFactory<>("wayName"));
        TableView.TableViewSelectionModel<Map> tsm = dataTableView.getSelectionModel();
        ObservableList<Integer> list = tsm.getSelectedIndices();
        list.addListener(this::onTableRowSelect);
        setTableViewData();
        wayList = HttpRequestUtil.getDictionaryOptionItemList("WAY");

        wayComboBox.getItems().addAll(wayList);
        dayPick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));
    }
    /**
     * 清除学生表单中输入信息
     */
    public void clearPanel() {
        feeId = null;
        numField.setText("");
        nameField.setText("");
        itemField.setText("");
        dayPick.getEditor().setText("");
        moneyField.setText("");
        placeField.setText("");
        wayComboBox.getSelectionModel().select(-1);
    }
    protected void changeFeeInfo() {
        Map form = dataTableView.getSelectionModel().getSelectedItem();
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
        numField.setText(CommonMethod.getString(form, "num"));
        nameField.setText(CommonMethod.getString(form, "name"));
        itemField.setText(CommonMethod.getString(form, "item"));
        dayPick.getEditor().setText(CommonMethod.getString(form, "day"));
        moneyField.setText(CommonMethod.getString(form, "money"));
        placeField.setText(CommonMethod.getString(form, "place"));
        wayComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(wayList, CommonMethod.getString(form, "way")));
    }
    /**
     * 点击学生列表的某一行，根据studentId ,从后台查询学生的基本信息，切换学生的编辑信息
     */

    public void onTableRowSelect(ListChangeListener.Change<? extends Integer> change){
        changeFeeInfo();
    }

    /**
     * 点击查询按钮，从从后台根据输入的串，查询匹配的学生在学生列表中显示
     */
    @FXML
    protected void onQueryButtonClick() {
        String numName = numNameTextField.getText();
        DataRequest req = new DataRequest();
        req.put("numName",numName);
        DataResponse res = HttpRequestUtil.request("/api/fee/getFeeList",req);
        if(res != null && res.getCode()== 0) {
            feeList = (ArrayList<Map>)res.getData();
            setTableViewData();
        }
    }
    /**
     *  添加新学生， 清空输入信息， 输入相关信息，点击保存即可添加新的学生
     */
    @FXML
    protected void onAddButtonClick() {
        clearPanel();
    }

    /**
     * 点击删除按钮 删除当前编辑的学生的数据
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
        feeId = CommonMethod.getInteger(form,"feeId");
        DataRequest req = new DataRequest();
        req.put("feeId", feeId);
        DataResponse res = HttpRequestUtil.request("/api/fee/feeDelete",req);
        if(res.getCode() == 0) {
            MessageDialog.showDialog("删除成功！");
            onQueryButtonClick();
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
    }
    /**
     * 点击保存按钮，保存当前编辑的学生信息，如果是新添加的学生，后台添加学生
     */
    @FXML
    protected void onSaveButtonClick() {
        if (numField.getText().equals("")) {
            MessageDialog.showDialog("学号为空，不能修改");
            return;
        }
        Map form = new HashMap();
        form.put("num", numField.getText());
        form.put("name", nameField.getText());
        form.put("item", itemField.getText());
        form.put("day", dayPick.getEditor().getText());
        form.put("money", moneyField.getText());
        form.put("place", placeField.getText());
        if(wayComboBox.getSelectionModel() != null && wayComboBox.getSelectionModel().getSelectedItem() != null)
            form.put("way",wayComboBox.getSelectionModel().getSelectedItem().getValue());

        DataRequest req = new DataRequest();
        req.put("feeId", feeId);
        req.put("form", form);
        DataResponse res = HttpRequestUtil.request("/api/fee/feeEditSave",req);
        if(res.getCode() == 0) {
            feeId = CommonMethod.getIntegerFromObject(res.getData());
            MessageDialog.showDialog("提交成功！");
            onQueryButtonClick();
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
    }
    /**
     * doNew() doSave() doDelete() 重写 ToolController 中的方法， 实现选择 新建，保存，删除 对学生的增，删，改操作
     */
    public void doNew(){
        clearPanel();
    }
    public void doSave(){
        onSaveButtonClick();
    }
    public void doDelete(){
        onDeleteButtonClick();
    }

    /**
     * 导出学生信息表的示例 重写ToolController 中的doExport 这里给出了一个导出学生基本信息到Excl表的示例， 后台生成Excl文件数据，传回前台，前台将文件保存到本地
     */
    public void doExport(){
        String numName = numNameTextField.getText();
        DataRequest req = new DataRequest();
        req.put("numName",numName);
        byte[] bytes = HttpRequestUtil.requestByteData("/api/fee/getFeeListExcl", req);
        if (bytes != null) {
            try {
                FileChooser fileDialog = new FileChooser();
                fileDialog.setTitle("前选择保存的文件");
                fileDialog.setInitialDirectory(new File("C:/"));
                fileDialog.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("XLSX 文件", "*.xlsx"));
                File file = fileDialog.showSaveDialog(null);
                if(file != null) {
                    FileOutputStream out = new FileOutputStream(file);
                    out.write(bytes);
                    out.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }
    @FXML
    protected void onImportButtonClick() {
        FileChooser fileDialog = new FileChooser();
        fileDialog.setTitle("前选择学生数据表");
        fileDialog.setInitialDirectory(new File("D:/"));
        fileDialog.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XLSX 文件", "*.xlsx"));
        File file = fileDialog.showOpenDialog(null);
        String paras = "";
        DataResponse res =HttpRequestUtil.importData("/api/term/importFeeData",file.getPath(),paras);
        if(res.getCode() == 0) {
            MessageDialog.showDialog("上传成功！");
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
    }

}


