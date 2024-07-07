package com.teach.javafxclient.controller;

import com.teach.javafxclient.controller.base.MessageDialog;
import com.teach.javafxclient.request.DataRequest;
import com.teach.javafxclient.request.DataResponse;
import com.teach.javafxclient.request.HttpRequestUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ExcelController {
    @FXML
    private Button uploadButton;
    private File selectedFile;

    @FXML
    private void handleChooseFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        selectedFile = fileChooser.showOpenDialog(stage);
        System.out.println(selectedFile);
        if (selectedFile != null) {
            uploadButton.setDisable(false);
        }

    }
    @FXML
    private void handleUploadFile(ActionEvent event) {
        try (InputStream inputStream = new FileInputStream(selectedFile);
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0); // 读取第一个工作表

            for (Row row : sheet) {
                System.out.println(row.getCell(0));
                System.out.println(row.getCell(1));
                System.out.println(row.getCell(2));
                ///////////////////
                Map form = new HashMap();
                form.put("num",row.getCell(0));
                form.put("name",row.getCell(1));
                form.put("dept",row.getCell(2));
                form.put("major",row.getCell(3));
                form.put("className",row.getCell(4));
                form.put("card",row.getCell(5));
                form.put("gender",row.getCell(6));
                form.put("birthday",row.getCell(7));
                form.put("email",row.getCell(8));
                form.put("phone",row.getCell(9));
                form.put("address",row.getCell(10));
                //System.out.println(form);

                DataRequest req = new DataRequest();
                req.put("studentId", 0);
                req.put("form", form);
                //System.out.println(req);

                DataResponse res = HttpRequestUtil.request("/api/student/studentEditSave",req);
                System.out.println(res);

                if(res.getCode() == 0) {
                    //studentId = CommonMethod.getIntegerFromObject(res.getData());
                    MessageDialog.showDialog("提交成功！");

                }
                else {
                    MessageDialog.showDialog(res.getMsg());
                }
                /*for (Cell cell : row) {
                    System.out.print(cell.getColumnIndex());
                    printCellValue(cell);
                }
                System.out.println();*/
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                System.out.print(cell.getStringCellValue() + "\t");
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    System.out.print(cell.getDateCellValue() + "\t");
                } else {
                    System.out.print(cell.getNumericCellValue() + "\t");
                }
                break;
            case BOOLEAN:
                System.out.print(cell.getBooleanCellValue() + "\t");
                break;
            case FORMULA:
                System.out.print(cell.getCellFormula() + "\t");
                break;
            case BLANK:
                System.out.print("\t");
                break;
            default:
                System.out.print("Unknown Cell Type\t");
                break;
        }
    }
}
