package com.example.loginpage;

import com.example.loginpage.models.FreeParking;
import com.example.loginpage.models.FreeParkingUserCarOwners;
import com.example.loginpage.oop.Login;
import com.example.loginpage.oop.MethodClass;
import com.example.loginpage.oop.RestAPI.OkHttpGet;
import com.example.loginpage.oop.RestAPI.OkHttpPost;
import com.example.loginpage.oop.SendEmail;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;


import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class parkingApplyForParkingController {
    //OOP
    OkHttpGet okHttpGet = new OkHttpGet();
    MethodClass methodClass = new MethodClass();
    OkHttpPost okHttpPost = new OkHttpPost();
    SendEmail sendEmail = new SendEmail();
    @FXML
    private TableColumn tablecolumnDateFree, tablecolumnDriverVisa, tablecolumnParkingSlot;
    @FXML
    private TableView tableviewFreeSlots;
    @FXML
    private Label labelSelectedDay, labelSelectedParkingslot, labelCurrentStatus, labelGlobalvisa;
    @FXML
    private CheckBox checkboxConfirmApplyParking;
    @FXML
    private ImageView imageviewUser, sidemenuApplyParking, sidemenuCredits, sidemenuManageParking, sidemenuParkingDashboard;
    @FXML
    private TextArea textareaUserInfo;
    @FXML
    private TextField textfieldFilterFreeParking;

    private Stage stage;
    private Scene scene;
    private Parent root;

    String globalVisa;
    Image globaluserImage;
    Boolean hasCarBoolean;
    String globalTextAreaData;
    ObservableList<FreeParkingUserCarOwners> globalData = FXCollections.observableArrayList();

    public void setup(String globalvisa, Boolean phasCarBoolean, Image visaImage, String sglobalTextAreaData) throws IOException {
        if(phasCarBoolean)
        {
            sidemenuApplyParking.setDisable(true);
            labelCurrentStatus.setText("You are a car owner!");
        }
        else
        {
            sidemenuManageParking.setDisable(true);
            labelCurrentStatus.setText("You do not havea car!");
        }

        labelGlobalvisa.setText(globalvisa);
        globalTextAreaData = sglobalTextAreaData;
        globalVisa = labelGlobalvisa.getText();
        textareaUserInfo.setText(sglobalTextAreaData);
        imageviewUser.setImage(visaImage);

        //populating tableview
        ObservableList<FreeParkingUserCarOwners> data = methodClass.getAllFreeParking();
        globalData = data;
        tablecolumnDateFree.setCellValueFactory(new PropertyValueFactory<FreeParking,String>("date"));
        tablecolumnDriverVisa.setCellValueFactory(new PropertyValueFactory<FreeParking,String>("visa"));


        tableviewFreeSlots.setItems(data);


        FilteredList<FreeParkingUserCarOwners> filteredData = new FilteredList(this.globalData, (b) -> {
            return true;
        });

        this.textfieldFilterFreeParking.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((freeParking) -> {
                if (newValue != null && !newValue.isEmpty()) {
                    String lowerCaseFilter = newValue.toLowerCase();

                    if (freeParking.getDate().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                        return true;
                    } else if (freeParking.getVisa().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                        return true;
                    } else {
                        return String.valueOf(freeParking.getDate()).indexOf(lowerCaseFilter) != -1;
                    }
                } else {
                    return true;
                }
            });
        });
        SortedList<FreeParkingUserCarOwners> sortedData = new SortedList(filteredData);
        sortedData.comparatorProperty().bind(this.tableviewFreeSlots.comparatorProperty());
        this.tableviewFreeSlots.setItems(sortedData);
        labelCurrentStatus.setText("Your apply parking dashboard loaded successfully!");
    }



    public void switchToLoginPageLogOut(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginPage.fxml"));
        root = loader.load();
        loginPageController loginPageController = loader.getController();

        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        loginPageController.setup("");
        stage.show();
    }

    public void switchToLoginPageReturn(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginPage.fxml"));
        root = loader.load();
        loginPageController loginPageController = loader.getController();

        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        loginPageController.setup(globalVisa);
        stage.show();
    }

    @FXML
    protected void initialize() throws IOException {



    }

    String drivervisa = "";
    public void getSelectedRecord(MouseEvent mouseEvent) throws IOException {
        FreeParkingUserCarOwners freeParkingUserCarOwners = (FreeParkingUserCarOwners) tableviewFreeSlots.getSelectionModel().getSelectedItem();
        labelSelectedDay.setText(freeParkingUserCarOwners.getDate());
        drivervisa = freeParkingUserCarOwners.getVisa();
    }

    public void RequestFreeParking() throws IOException {
        if(checkboxConfirmApplyParking.isSelected() && labelSelectedDay.getText() != "")
        {
            String newFreeParkingjson = "    {\n        \"date\": \"" + labelSelectedDay.getText() + "\",\n" +
                    "        \"status\": \"" + "Applied" + "\",\n" +
                    "        \"driverVisa\": \"" + drivervisa + "\",\n" +
                    "        \"user\": {\"visa\":\"" + globalVisa + "\"}\n" +
                    "    }";

            System.out.println(newFreeParkingjson);
            String response = okHttpPost.post("http://localhost:8080/cppk/addARequest/", newFreeParkingjson);

            String sendEmailTitle = "Hello " + drivervisa + ", You got a new request from " + globalVisa;
            String sendEmailMsg = "You have a new parking request from " + globalVisa + ". Please check your iPension Carpool&Parking Application. - Elcademy";
            sendEmail.sendEmailNotfication(sendEmailTitle, sendEmailMsg, drivervisa);
            labelCurrentStatus.setText("Email sent to car owner.");
        }
        else
        {
            methodClass.messageBox("Please check the Checkbox before Requesting a parking slot", "User error");
            labelCurrentStatus.setText("Checkbox error");
        }
    }


    public void switchToParkingDashboard(MouseEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("parkingDashboard.fxml"));
        root = loader.load();
        parkingDashboardController parkingDashboardController = loader.getController();

        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        parkingDashboardController.setup(globalVisa);
        stage.show();
    }

    public void switchToParkingApply(MouseEvent e) throws IOException {
        labelCurrentStatus.setText("Already in Apply Parking");
    }


    public void switchToManageParking(MouseEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("parkingManageParking.fxml"));
        root = loader.load();
        parkingManageParkingController parkingManageParkingController = loader.getController();

        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        parkingManageParkingController.setup(globalVisa, hasCarBoolean, globaluserImage, globalTextAreaData);
        stage.show();
    }



    public void SendFeedBack() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Feedback.fxml"));
        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.setTitle("Send feedback");
        stage.setScene(scene);

        stage.show();
    }

    public void ShowCredit() throws IOException, URISyntaxException {
        Desktop.getDesktop().browse(new URI("https://github.com/IpenElcademy2021"));
    }

    public void Exit() {
        methodClass.Exit();
    }
}
