package com.example.loginpage;

import com.example.loginpage.models.*;
import com.example.loginpage.oop.MethodClass;
import com.example.loginpage.oop.PoolingMethodClass;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class CarpoolDashboardController {

    @FXML
    private TableView tableView_information;

    @FXML
    private TableColumn column_driver_id,column_region,column_date,column_pickup_point,column_pickup_time,column_departure_time,column_status,column_comment;

    @FXML
    private HBox HBoxDashboard, HBoxPropose, HBoxRequest, HBoxManage;

    PoolingMethodClass poolingMethodClass = new PoolingMethodClass();

    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    String globalVisa;

    List<String> carUsersArray = new ArrayList<String>();
    Boolean hasCarBoolean;
    MethodClass methodClass = new MethodClass();

    public void setup(String visa) throws IOException {


        globalVisa = visa;


        ObservableList<UserRequestPoolingProposeUser> data = poolingMethodClass.getUserRequestByVisa(visa);
        column_driver_id.setCellValueFactory(new PropertyValueFactory<PoolingPropose,String>("visa"));
        column_region.setCellValueFactory(new PropertyValueFactory<PoolingPropose, Date>("region"));
        column_date.setCellValueFactory(new PropertyValueFactory<PoolingPropose,String>("date"));
        column_pickup_point.setCellValueFactory(new PropertyValueFactory<PoolingPropose,String>("pickUpPoint"));
        column_pickup_time.setCellValueFactory(new PropertyValueFactory<PoolingPropose,String>("pickUpTime"));
        column_departure_time.setCellValueFactory(new PropertyValueFactory<PoolingPropose,String>("departureTime"));
        column_status.setCellValueFactory(new PropertyValueFactory<UserRequestPoolingProposeUser,String>("reservationStatus"));
        column_comment.setCellValueFactory(new PropertyValueFactory<UserRequestPoolingProposeUser,String>("comment"));

        tableView_information.setItems(data);

        ObservableList<RequestUserCarOwners> carusersdata = methodClass.searchAllCarOwners();

        Iterator<RequestUserCarOwners> iterator = carusersdata.iterator();
        while (iterator.hasNext()) {
            carUsersArray.add(iterator.next().getVisa());
        }

        for (int counter = 0; counter < carUsersArray.size(); counter++) {
            log.debug(carUsersArray.get(counter));
        }

        if(carUsersArray.contains(globalVisa.toUpperCase()))
        {
            hasCarBoolean = true;
            HBoxRequest.setDisable(true);
            log.info("You are a driver");
        }
        else
        {
            hasCarBoolean = false;
            HBoxPropose.setDisable(true);
            HBoxManage.setDisable(true);
            log.info("You are not a driver");
        }
        carUsersArray.clear();
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

}
