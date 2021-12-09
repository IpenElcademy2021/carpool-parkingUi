package com.example.loginpage.oop;


import com.example.loginpage.models.FreeParking;
import com.example.loginpage.models.FreeParkingUserCarOwners;
import com.example.loginpage.models.RequestUserCarOwners;
import com.example.loginpage.oop.RestAPI.OkHttpDelete;
import com.example.loginpage.oop.RestAPI.OkHttpGet;
import com.example.loginpage.oop.RestAPI.OkHttpPost;
import com.example.loginpage.oop.RestAPI.OkHttpPut;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.formula.functions.T;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javafx.scene.control.TableView;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

public class MethodClass {
    //OOP
    OkHttpGet okHttpGet = new OkHttpGet();

    private Stage stage;

    public void Exit() {
        messageBox("Goodbye", "Exit");
        System.exit(0);
    }

    public static void messageBox(String infoMessage, String titleBar) {
        JOptionPane.showMessageDialog(null, infoMessage, "" + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }


    public ObservableList<FreeParkingUserCarOwners> getAllFreeParking() throws IOException {
        String url = "http://localhost:8080/cppk/getAllFreeParking/";
        System.out.println(okHttpGet.run(url));
        String response = okHttpGet.run(url);
        ObservableList<FreeParkingUserCarOwners> datareturn = FXCollections.observableArrayList();

        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(response);
            JSONArray jsonArray = (JSONArray) obj;

            ObservableList<FreeParkingUserCarOwners> data = FXCollections.observableArrayList();

            String freeParkingID = "", date = "", visa = "", password = "", name = "", address = "", phoneNumber = "", carPlate = "", parkingSlot = "";
            for (var i = 0; i < jsonArray.toArray().length; i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                JSONObject jsonUsersObject = (JSONObject)jsonObject.get("user");
                JSONObject jsonCarownersObject = (JSONObject)jsonUsersObject.get("carOwners");

                freeParkingID = jsonObject.get("freeParkingID").toString();
                date = jsonObject.get("date").toString().substring(0,10);
                visa = jsonUsersObject.get("visa").toString();
                password = jsonUsersObject.get("password").toString();
                name = jsonUsersObject.get("name").toString();
                address = jsonUsersObject.get("address").toString();
                phoneNumber = jsonUsersObject.get("phoneNumber").toString();
//                 carPlate = jsonCarownersObject.get("carPlate").toString();
//                 parkingSlot = jsonCarownersObject.get("parkingSlot").toString();

                data.add(new FreeParkingUserCarOwners(freeParkingID, date, visa, password,name, address, phoneNumber));

                datareturn = data;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return datareturn;
    }


    public ObservableList<RequestUserCarOwners> getMyParkingDemands(String drivervisa) throws IOException {

        String url = "http://localhost:8080/cppk/searchRequestByDriverVisa/" + drivervisa;
        System.out.println(okHttpGet.run(url));
        String response = okHttpGet.run(url);
        ObservableList<RequestUserCarOwners> datareturn = FXCollections.observableArrayList();

        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(response);
            JSONArray jsonArray = (JSONArray) obj;

            ObservableList<RequestUserCarOwners> data = FXCollections.observableArrayList();

            String requestId = "", date = "", driverVisa = "", status = "", visa = "";
            for (var i = 0; i < jsonArray.toArray().length; i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                JSONObject jsonUsersObject = (JSONObject)jsonObject.get("user");

                requestId = jsonObject.get("requestId").toString();
                date = jsonObject.get("date").toString().substring(0,10);
                driverVisa = jsonObject.get("driverVisa").toString();
                status = jsonObject.get("status").toString();
                visa = jsonUsersObject.get("visa").toString();

                data.add(new RequestUserCarOwners(requestId, date, driverVisa, status, visa));

                datareturn = data;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return datareturn;
    }


    public ObservableList<RequestUserCarOwners> getMyParkingDemandsByStatus(String drivervisa, String status1) throws IOException {
        String url = "http://localhost:8080/cppk/searchRequestByDriverVisaAndStatus/" + drivervisa + "/" + status1;
        System.out.println(okHttpGet.run(url));

        String response = okHttpGet.run(url);
        ObservableList<RequestUserCarOwners> datareturn = FXCollections.observableArrayList();

        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(response);
            JSONArray jsonArray = (JSONArray) obj;

            ObservableList<RequestUserCarOwners> data = FXCollections.observableArrayList();

            String requestId = "", date = "", driverVisa = "", status = "", visa = "";
            for (var i = 0; i < jsonArray.toArray().length; i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                JSONObject jsonUsersObject = (JSONObject)jsonObject.get("user");

                requestId = jsonObject.get("requestId").toString();
                date = jsonObject.get("date").toString().substring(0,10);
                driverVisa = jsonObject.get("driverVisa").toString();
                status = jsonObject.get("status").toString();
                visa = jsonUsersObject.get("visa").toString();

                data.add(new RequestUserCarOwners(requestId, date, driverVisa, status, visa));

                datareturn = data;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return datareturn;
    }


    public ObservableList<RequestUserCarOwners> searchAllUsers() throws IOException {
        String url = "http://localhost:8080/cppk/getAllUser/";
        System.out.println(" HERE" + okHttpGet.run(url));
        String response = okHttpGet.run(url);
        ObservableList<RequestUserCarOwners> datareturn = FXCollections.observableArrayList();

        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(response);
            JSONArray jsonArray = (JSONArray) obj;

            ObservableList<RequestUserCarOwners> data = FXCollections.observableArrayList();

            String visa = "", password = "", name = "", address = "", phoneNumber = "", carPlate = "", parkingSlot = "", carOwners = "";
            for (var i = 0; i < jsonArray.toArray().length; i++) {

                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                visa = jsonObject.get("visa").toString();
                password = jsonObject.get("password").toString();
                name = jsonObject.get("name").toString();
                address = jsonObject.get("address").toString();
                phoneNumber = jsonObject.get("phoneNumber").toString();

                if(jsonObject.get("carOwners") != null)
                {
                    JSONObject jsonCarOwnerObject = (JSONObject)jsonObject.get("carOwners");
                    carPlate = jsonCarOwnerObject.get("carPlate").toString();
                    parkingSlot = jsonCarOwnerObject.get("parkingSlot").toString();

                }

                data.add(new RequestUserCarOwners(visa, password, name, address, phoneNumber, carPlate, parkingSlot, carOwners));
                carPlate = ""; parkingSlot = "";
                datareturn = data;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return datareturn;
    }


    public ObservableList<RequestUserCarOwners> getMyRequestByUser(String user) throws IOException {

        String url = "http://localhost:8080/cppk/searchRequestByUser/" + user;
        System.out.println(okHttpGet.run(url));
        String response = okHttpGet.run(url);
        ObservableList<RequestUserCarOwners> datareturn = FXCollections.observableArrayList();
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(response);
            JSONArray jsonArray = (JSONArray) obj;

            ObservableList<RequestUserCarOwners> data = FXCollections.observableArrayList();

            String date = "", driverVisa = "", status = "";
            for (var i = 0; i < jsonArray.toArray().length; i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                date = jsonObject.get("date").toString().substring(0,10);
                driverVisa = jsonObject.get("driverVisa").toString();
                status = jsonObject.get("status").toString();

                data.add(new RequestUserCarOwners(date, status, driverVisa));

                datareturn = data;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return datareturn;
    }


    public JSONObject findUserbyVisa(String providedvisa) throws IOException {
        JSONObject rjsonObject = null;
        String url = "http://localhost:8080/cppk/findbyvisa/" + providedvisa;
        String response = okHttpGet.run(url);
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(response);
            JSONArray jsonArray = (JSONArray) obj;

            for (var i = 0; i < jsonArray.toArray().length; i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                rjsonObject = jsonObject;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rjsonObject;
    }


    public ObservableList<RequestUserCarOwners> searchAllCarOwners() throws IOException {
        String url = "http://localhost:8080/cppk/getAllCarOwners/";
        System.out.println(" 247" + okHttpGet.run(url));
        String response = okHttpGet.run(url);
        ObservableList<RequestUserCarOwners> datareturn = FXCollections.observableArrayList();

        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(response);
            JSONArray jsonArray = (JSONArray) obj;

            ObservableList<RequestUserCarOwners> data = FXCollections.observableArrayList();

            String carPlate = "", parkingSlot = "", address = "", visa = "", name = "", phoneNumber = "";
            for (var i = 0; i < jsonArray.toArray().length; i++) {

                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                JSONObject jsonUsersObject = (JSONObject)jsonObject.get("user");

                carPlate = jsonObject.get("carPlate").toString();
                parkingSlot = jsonObject.get("parkingSlot").toString();
                name = jsonUsersObject.get("name").toString();
                address = jsonUsersObject.get("address").toString();
                phoneNumber = jsonUsersObject.get("phoneNumber").toString();
                visa = jsonUsersObject.get("visa").toString();

                data.add(new RequestUserCarOwners(carPlate, parkingSlot, name, address, phoneNumber, visa));

                datareturn = data;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return datareturn;
    }


    public ObservableList<FreeParking> searchAllRequest() throws IOException {
        String url = "http://localhost:8080/cppk/getAllFreeParking/";
        System.out.println(" SD" + okHttpGet.run(url));
        String response = okHttpGet.run(url);
        ObservableList<FreeParking> datareturn = FXCollections.observableArrayList();

        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(response);
            JSONArray jsonArray = (JSONArray) obj;

            ObservableList<FreeParking> data = FXCollections.observableArrayList();

            String date = "";
            for (var i = 0; i < jsonArray.toArray().length; i++) {

                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                date = jsonObject.get("date").toString().substring(0,10);;
                data.add(new FreeParking(date));

                datareturn = data;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return datareturn;
    }

    public void exportToExcel(TableView<T> tableViewA, TableView<T> tableViewD){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as an .xls file");
        fileChooser.setInitialFileName("Parking export.xls");
        File selectedFile = fileChooser.showSaveDialog(stage);

        HSSFWorkbook hssfWorkbook=new HSSFWorkbook();
        HSSFSheet hssfSheetA =  hssfWorkbook.createSheet("Approved");
        HSSFSheet hssfSheetD =  hssfWorkbook.createSheet("Declined");

        HSSFRow firstRowA= hssfSheetA.createRow(0);
        HSSFRow firstRowD= hssfSheetD.createRow(0);

        ///set titles of columns
        for (int i=0; i<tableViewA.getColumns().size();i++){
            firstRowA.createCell(i).setCellValue(tableViewA.getColumns().get(i).getText());
        }
        for (int i=0; i<tableViewD.getColumns().size();i++){
            firstRowD.createCell(i).setCellValue(tableViewD.getColumns().get(i).getText());
        }

        for (int row=0; row<tableViewA.getItems().size();row++){
            HSSFRow hssfRow= hssfSheetA.createRow(row+1);
            for (int col=0; col<tableViewA.getColumns().size(); col++){
                Object celValue = tableViewA.getColumns().get(col).getCellObservableValue(row).getValue();
                try {
                    if (celValue != null && Double.parseDouble(celValue.toString()) != 0.0) {
                        hssfRow.createCell(col).setCellValue(Double.parseDouble(celValue.toString()));
                    }
                } catch (  NumberFormatException e ){
                    hssfRow.createCell(col).setCellValue(celValue.toString());
                }
            }
        }

        for (int row=0; row<tableViewD.getItems().size();row++){
            HSSFRow hssfRow= hssfSheetD.createRow(row+1);
            for (int col=0; col<tableViewD.getColumns().size(); col++){
                Object celValue = tableViewD.getColumns().get(col).getCellObservableValue(row).getValue();
                try {
                    if (celValue != null && Double.parseDouble(celValue.toString()) != 0.0) {
                        hssfRow.createCell(col).setCellValue(Double.parseDouble(celValue.toString()));
                    }
                } catch (  NumberFormatException e ){
                    hssfRow.createCell(col).setCellValue(celValue.toString());
                }
            }
        }
        //save excel file and close the workbook
        try {
            hssfWorkbook.write(new FileOutputStream(selectedFile));
            hssfWorkbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
