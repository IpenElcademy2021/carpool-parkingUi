package com.example.loginpage;

import com.example.loginpage.models.PoolingPropose;
import com.example.loginpage.models.User;
import com.example.loginpage.oop.PoolingMethodClass;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.swing.*;
import java.io.IOException;
import java.util.Date;

@Slf4j
public class CarpoolUserRequestController {

    @FXML
    private TableView tableView_request;

    @FXML
    private TableColumn column_visa,column_date,column_region,column_pickup_point,column_pickup_time,column_departure_time,column_seat,column_poolId;

    @FXML
    private Label label_visa,label_date,label_region,label_pickup_point,label_pickup_time,label_departure_time;

    @FXML
    private HBox HBoxDashboard, HBoxPropose, HBoxRequest, HBoxManage;



    @FXML
    String globalVisa;

    int seat = 0;


    @FXML
    int poolingID =0;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    String reservationStatus = "Pending";

    String comment = "No comment";

    Boolean hasCarBoolean;


    PoolingMethodClass poolingMethodClass = new PoolingMethodClass();



    OkHttpClient okHttpClient = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

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

        ObservableList<PoolingPropose> data = poolingMethodClass.getAllProposePooling();
        column_visa.setCellValueFactory(new PropertyValueFactory<User,String>("visa"));
        column_date.setCellValueFactory(new PropertyValueFactory<PoolingPropose, Date>("date"));
        column_region.setCellValueFactory(new PropertyValueFactory<PoolingPropose,String>("region"));
        column_pickup_point.setCellValueFactory(new PropertyValueFactory<PoolingPropose,String>("pickUpPoint"));
        column_pickup_time.setCellValueFactory(new PropertyValueFactory<PoolingPropose,String>("pickUpTime"));
        column_departure_time.setCellValueFactory(new PropertyValueFactory<PoolingPropose,String>("departureTime"));
        column_seat.setCellValueFactory(new PropertyValueFactory<PoolingPropose,String>("seat"));
        column_poolId.setCellValueFactory(new PropertyValueFactory<PoolingPropose,String>("poolId"));


        tableView_request.setItems(data);

    }


    public void getSelectedRecord(MouseEvent e) throws IOException{
        PoolingPropose poolingPropose = (PoolingPropose) tableView_request.getSelectionModel().getSelectedItem();
        label_visa.setText(poolingPropose.getVisa());
        label_date.setText(poolingPropose.getDate());
        label_region.setText(poolingPropose.getRegion());
        label_pickup_point.setText(poolingPropose.getPickUpPoint());
        label_pickup_time.setText(poolingPropose.getPickUpTime());
        label_departure_time.setText(poolingPropose.getDepartureTime());
        poolingID = Integer.parseInt(poolingPropose.getPoolId());
        seat = Integer.parseInt(poolingPropose.getSeat());
    }

    public void createUserRequest(ActionEvent actionEvent) throws IOException{

        if (poolingID >0){
            if (seat>0) {

                String json = "    {\n        \"reservationStatus\": \"" + reservationStatus + "\",\n" +
                        "        \"pooling\": {\"poolId\": " + poolingID + "},\n" +
                        "        \"user\": {\"visa\":\"" + globalVisa + "\"},\n" +
                        "        \"comment\": \"" + comment + "\"\n" +
                        "    }";

                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder().url("http://localhost:8080/cppk/createUserRequest").post(body).build();

                log.debug(json);

                try (Response response = okHttpClient.newCall(request).execute()) {
                    log.debug(response.body().string());
                }

                MessageBox("New user request added", "User Request ");
                log.info("New user request added");
            }else {
                MessageBox("No seat available", "User Request ");
                log.info("No seat available");
            }
        }else {
            MessageBox("Please select a proposal","No User Request ");
            log.error("Please select a proposal");
        }

        setup(globalVisa, hasCarBoolean);
    }

    private void MessageBox(String message, String title) {
        JOptionPane.showMessageDialog(null,message,"" +title,JOptionPane.INFORMATION_MESSAGE);
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

    public void btnClear(MouseEvent e) throws IOException {
        label_date.setText("");
        label_departure_time.setText("");
        label_pickup_point.setText("");
        label_region.setText("");
        label_pickup_time.setText("");
        label_visa.setText("");

        poolingID = 0;
    }

}
