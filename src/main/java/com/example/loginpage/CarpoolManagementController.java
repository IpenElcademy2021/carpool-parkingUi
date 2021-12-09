package com.example.loginpage;

import com.example.loginpage.models.CarpoolManagement;
import com.example.loginpage.oop.CarpoolManagementMethod;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.swing.*;
import java.io.IOException;
import java.util.Date;

@Slf4j
public class CarpoolManagementController {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    @FXML
    String globalVisa;
    Boolean hasCarBoolean;
    String selectedPoolId;
    String userRequestId;
    String date;
    int seat = 0;
    CarpoolManagementMethod carpoolManagementMethod = new CarpoolManagementMethod();
    OkHttpClient okHttpClient = new OkHttpClient();
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TableView<CarpoolManagement> tableView_management;
    @FXML
    private TableColumn column_visa, column_date, column_status, column_seat, column_comment, column_poolId, column_userRequestId;
    @FXML
    private TextField textField_visa, textField_date;
    @FXML
    private ComboBox comboBox_status;
    @FXML
    private TextArea textArea_comment;
    @FXML
    private HBox HBoxDashboard, HBoxPropose, HBoxRequest, HBoxManage;

    public void setup(String visa, Boolean hasCar) throws IOException {

        globalVisa = visa;
        hasCarBoolean = hasCar;
        if(hasCar == true)
        {
            HBoxRequest.setDisable(true);
            log.info("You are a driver");
        }
        else
        {
            HBoxPropose.setDisable(true);
            HBoxManage.setDisable(true);
            log.info("You are not a driver");
        }


        ObservableList<CarpoolManagement> data = carpoolManagementMethod.getCarpoolRequestByVisa(globalVisa);

        column_visa.setCellValueFactory(new PropertyValueFactory<CarpoolManagement, String>("visa"));
        column_date.setCellValueFactory(new PropertyValueFactory<CarpoolManagement, Date>("date"));
        column_status.setCellValueFactory(new PropertyValueFactory<CarpoolManagement, String>("reservationStatus"));
        column_seat.setCellValueFactory(new PropertyValueFactory<CarpoolManagement, String>("seat"));
        column_comment.setCellValueFactory(new PropertyValueFactory<CarpoolManagement, String>("comment"));
        column_poolId.setCellValueFactory(new PropertyValueFactory<CarpoolManagement, String>("poolId"));
        column_userRequestId.setCellValueFactory(new PropertyValueFactory<CarpoolManagement, String>("userRequestId"));


        tableView_management.setItems(data);


        log.debug(String.valueOf(data));
    }

    @FXML
    public void initialize() throws IOException {

    }

    public void switchToPoolingDashboard(MouseEvent e) throws IOException{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("carpoolDashBoard.fxml"));
        root = loader.load();

        CarpoolDashboardController carpoolDashboardController = loader.getController();

        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        carpoolDashboardController.setup(globalVisa);
        stage.show();

    }

    public void switchToPoolingPropose(MouseEvent e) throws IOException{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("carpoolPropose.fxml"));
        root = loader.load();
        PoolingProposeController poolingProposeController = loader.getController();
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        poolingProposeController.setup(globalVisa,hasCarBoolean);
        stage.show();
    }

    public void switchToPoolingUserRequest(MouseEvent e) throws IOException{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("carpoolRequest.fxml"));
        root = loader.load();
        CarpoolUserRequestController carpoolUserRequestController = loader.getController();
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        carpoolUserRequestController.setup(globalVisa,hasCarBoolean);
        stage.show();

    }

    public void switchToPoolingManage(MouseEvent e) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("carpoolManagement.fxml"));
        root = loader.load();
        CarpoolManagementController carpoolManagementController = loader.getController();
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        carpoolManagementController.setup(globalVisa,hasCarBoolean);
        stage.show();
    }

    public void switchToMainMenu (MouseEvent e) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginPage.fxml"));
        root = loader.load();
        loginPageController loginPageController = loader.getController();
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        loginPageController.setup(globalVisa);
        stage.show();
    }

    public void getCellValue(MouseEvent e) throws IOException {

        CarpoolManagement carpoolManagement = tableView_management.getSelectionModel().getSelectedItem();
        textField_visa.setText(carpoolManagement.getVisa());
        date = carpoolManagement.getDate();
        textField_date.setText(date);
        comboBox_status.setPromptText(carpoolManagement.getReservationStatus());
        selectedPoolId = carpoolManagement.getPoolId();
        userRequestId = carpoolManagement.getUserRequestId();
        seat = Integer.parseInt(carpoolManagement.getSeat());

    }

    public void cancelBtn(MouseEvent e) throws IOException {
        textField_visa.setText("");
        textField_date.setText("");
        comboBox_status.setPromptText("Pending");
        textArea_comment.setText("");

    }

    public void confirmBtn(MouseEvent e) throws IOException {

        if (seat > 0) {

            if (comboBox_status.getValue().toString().equals("Accept")) {
                seat = seat - 1;
                update();
                MessageBox("Pooling Accepted", "Requested Pooling");
                log.info("Pooling Accepted");
            } else {
                update();
                MessageBox("Pooling Rejected", "Requested Pooling");
                log.error("Pooling Rejected");
            }

        } else {
            MessageBox("No places left on date " + date, "Requested Pooling");
            log.info("No places left on date " + date);
        }

    }


    public void update() throws IOException {
        String seat1 = String.valueOf(seat);
        System.out.println(seat1);
        String json = "    {\n        \"reservationStatus\": \"" + comboBox_status.getValue().toString() + "\",\n" +
                "        \"comment\": \"" + textArea_comment.getText() + "\"\n" +
                "    }";

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url("http://localhost:8080/prc/updateRequest/" + userRequestId).put(body).build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            log.debug(response.body().string());

        }

        String json1 = "    {\n        \"seat\": \"" + seat1 + "\"\n" +

                "    }";
        RequestBody body1 = RequestBody.create(JSON, json1);
        Request request1 = new Request.Builder().url("http://localhost:8080/cppk/updateSeat/" + selectedPoolId).put(body1).build();
        try (Response response1 = okHttpClient.newCall(request1).execute()) {
            log.debug(response1.body().string());
        }


        setup(globalVisa, hasCarBoolean);

        textField_visa.setText("");
        textField_date.setText("");
        comboBox_status.setPromptText("Pending");
        textArea_comment.setText("");
    }


    private void MessageBox(String message, String title) {

        JOptionPane.showMessageDialog(null, message, "" + title, JOptionPane.INFORMATION_MESSAGE);
    }

    public void logout(MouseEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginPage.fxml"));
        root = loader.load();
        loginPageController loginPageController = loader.getController();
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        loginPageController.setup("");
        stage.show();
    }
}
