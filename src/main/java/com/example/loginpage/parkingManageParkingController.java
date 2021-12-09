package com.example.loginpage;

import com.example.loginpage.models.FreeParking;
import com.example.loginpage.models.FreeParkingUserCarOwners;
import com.example.loginpage.models.RequestUserCarOwners;
import com.example.loginpage.oop.MethodClass;
import com.example.loginpage.oop.RestAPI.OkHttpDelete;
import com.example.loginpage.oop.RestAPI.OkHttpPost;
import com.example.loginpage.oop.RestAPI.OkHttpPut;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;


import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class parkingManageParkingController {
    //OOP
    OkHttpPut okHttpPut = new OkHttpPut();
    OkHttpDelete okHttpDelete = new OkHttpDelete();
    OkHttpPost okHttpPost = new OkHttpPost();
    MethodClass methodClass = new MethodClass();

    @FXML
    private DatePicker dpApplyFreeParking;
    @FXML
    private CheckBox checkboxProposeFreeParking, checkboxConfirmUpdateRequest;
    @FXML
    private TableColumn tablecolumnManageDate, tablecolumnManageVisa, tablecolumnRequestId, tablecolumnManageDateA, tablecolumnManageVisaA, tablecolumnRequestIdA, tablecolumnManageDateD, tablecolumnManageVisaD, tablecolumnRequestIdD;
    @FXML
    private TableView tableviewDemand, tableviewDemandA, tableviewDemandD;
    @FXML
    private Label labelVisaSelected, labelDateSelected, labelCurrentStatus, labelLoggedVisa;
    @FXML
    private RadioButton radioApprove, radioDecline;
    @FXML
    private ImageView imageviewUser, sidemenuApplyParking, sidemenuCredits, sidemenuManageParking, sidemenuParkingDashboard;
    @FXML
    private TextArea textareaUserInfo;

    private Stage stage;
    private Scene scene;
    private Parent root;

    String globalVisa, globalTextAreaData;
    Image globaluserImage;
    Boolean hasCarBoolean;
    List<String> dataArray = new ArrayList<String>();
    List<String> dataArrayA = new ArrayList<String>();
    List<String> dataArrayD = new ArrayList<String>();
    List<String> requestDataArray = new ArrayList<String>();
    private final Logger logger = LoggerFactory.getLogger(parkingManageParkingController.class);


    public void setup(String globalvisa, Boolean phasCarBoolean, Image visaImage, String sglobalTextAreaData) throws IOException {
        //Check if user has car
        if(phasCarBoolean)
        {
            sidemenuApplyParking.setDisable(true);
            labelCurrentStatus.setText("CarOwner Detected, Apply parking disabled.");
        }
        else
        {
            sidemenuManageParking.setDisable(true);
            labelCurrentStatus.setText("Non-CarOwner Detected, Manage parking disabled.");
        }

        //Initial setup
        labelLoggedVisa.setText(globalvisa);
        imageviewUser.setImage(visaImage);
        globalTextAreaData = sglobalTextAreaData;
        textareaUserInfo.setText(sglobalTextAreaData);
        hasCarBoolean = phasCarBoolean;
        globalVisa = labelLoggedVisa.getText();

        //Populating tableview
        ObservableList<RequestUserCarOwners> data = methodClass.getMyParkingDemandsByStatus(globalVisa, "applied");
        ObservableList<RequestUserCarOwners> dataA = methodClass.getMyParkingDemandsByStatus(globalVisa, "approved");
        ObservableList<RequestUserCarOwners> dataD = methodClass.getMyParkingDemandsByStatus(globalVisa, "declined");

        tablecolumnRequestId.setCellValueFactory(new PropertyValueFactory<RequestUserCarOwners,String>("requestId"));
        tablecolumnManageVisa.setCellValueFactory(new PropertyValueFactory<RequestUserCarOwners,String>("visa"));
        tablecolumnManageDate.setCellValueFactory(new PropertyValueFactory<RequestUserCarOwners,String>("date"));

        tablecolumnRequestIdA.setCellValueFactory(new PropertyValueFactory<FreeParkingUserCarOwners,String>("requestId"));
        tablecolumnManageVisaA.setCellValueFactory(new PropertyValueFactory<FreeParkingUserCarOwners,String>("visa"));
        tablecolumnManageDateA.setCellValueFactory(new PropertyValueFactory<FreeParkingUserCarOwners,String>("date"));

        tablecolumnRequestIdD.setCellValueFactory(new PropertyValueFactory<FreeParking,String>("requestId"));
        tablecolumnManageVisaD.setCellValueFactory(new PropertyValueFactory<FreeParking,String>("visa"));
        tablecolumnManageDateD.setCellValueFactory(new PropertyValueFactory<FreeParking,String>("date"));

        tableviewDemand.setItems(data);
        tableviewDemandA.setItems(dataA);
        tableviewDemandD.setItems(dataD);

        //Adding data to an Array (Applied, Accept and Declined)
        dataArray.clear();
        Iterator<RequestUserCarOwners> iterator = data.iterator();
        while (iterator.hasNext()) {
            dataArray.add(iterator.next().getDate());
        }
        dataArrayA.clear();
        Iterator<RequestUserCarOwners> iteratorA = dataA.iterator();
        while (iteratorA.hasNext()) {
            dataArrayA.add(iteratorA.next().getDate());

        }
        dataArrayD.clear();
        Iterator<RequestUserCarOwners> iteratorD = dataD.iterator();
        while (iteratorD.hasNext()) {
            dataArrayD.add(iteratorD.next().getDate());
        }

        //logging
        labelCurrentStatus.setText("Your manage parking loaded successfully!");
        logger.info("Your manage parking loaded successfully!");
    }

    //Listening and getting the current selected record (Applied)
    Integer requestId;
    public void getSelectedRecord(MouseEvent mouseEvent) throws IOException {
        RequestUserCarOwners requestUserCarOwners = (RequestUserCarOwners) tableviewDemand.getSelectionModel().getSelectedItem();
        labelVisaSelected.setText(requestUserCarOwners.getVisa());
        labelDateSelected.setText(requestUserCarOwners.getDate());
        requestId = Integer.parseInt(requestUserCarOwners.getRequestId());
    }

    //Listening and getting the current selected record (Approved and Declined)
    Integer deleterequestId;
    public void getSelectedRecordA(MouseEvent mouseEvent) throws IOException {
        RequestUserCarOwners requestUserCarOwners = (RequestUserCarOwners) tableviewDemandA.getSelectionModel().getSelectedItem();
        deleterequestId = Integer.parseInt(requestUserCarOwners.getRequestId());
    }
    public void getSelectedRecordD(MouseEvent mouseEvent) throws IOException {
        RequestUserCarOwners requestUserCarOwners = (RequestUserCarOwners) tableviewDemandD.getSelectionModel().getSelectedItem();
        deleterequestId = Integer.parseInt(requestUserCarOwners.getRequestId());
    }

    //Setting a status to a leave
    public void setLeaveStatus() throws IOException {
        if(checkboxConfirmUpdateRequest.isSelected()) {
            if(radioApprove.isSelected()) {
                if(dataArrayA.contains(labelDateSelected.getText()))
                {
                    methodClass.messageBox("You already have this date booked", "Date already booked!");
                    labelCurrentStatus.setText("User error");
                }
                else
                {
                    String url = "http://localhost:8080/cppk/update/" + requestId;
                    String updateRequestJson = "    {\n        \"status\": \"" + "Approved" + "\"\n" +
                            "    }";

                    okHttpPut.post(url, updateRequestJson);
                    checkboxConfirmUpdateRequest.setSelected(false);
                    radioDecline.setSelected(false);
                    radioApprove.setSelected(false);
                    labelCurrentStatus.setText("Request approved!");
                    setup(globalVisa, hasCarBoolean, globaluserImage, globalTextAreaData);
                }
            }

            else if (radioDecline.isSelected()) {
                String url = "http://localhost:8080/cppk/update/" + requestId;
                String updateRequestJson = "    {\n        \"status\": \"" + "Declined" + "\"\n" +
                        "    }";
                checkboxConfirmUpdateRequest.setSelected(false);
                radioDecline.setSelected(false);
                radioApprove.setSelected(false);
                okHttpPut.post(url, updateRequestJson);
                labelCurrentStatus.setText("Request declined!");
                setup(globalVisa, hasCarBoolean, globaluserImage, globalTextAreaData);
            }

            else {
                methodClass.messageBox("Please select a Radio Button (Accept/Decline)", "User Error");

                //logging
                logger.debug("Radiobutton error");
                labelCurrentStatus.setText("Radiobutton error");
            }
        }

        else {
            methodClass.messageBox("Please check the Checkbox before Requesting a parking slot", "User error");                labelCurrentStatus.setText("User error");
            logger.debug("Checkbox error");
            labelCurrentStatus.setText("Checkbox error");
        }
    }


    //Delete selected Applied/Declined request
    public void DeleteRequest() throws IOException {
        if(deleterequestId == null)
        {
            methodClass.messageBox("Please select a record to delete!", "Error no record selected");
            labelCurrentStatus.setText("Select a record first.");
            logger.info("Select a record first.");
        }
        else
        {
            String url = "http://localhost:8080/cppk/deleteParkingRequest/" + deleterequestId;
            okHttpDelete.run(url);
            methodClass.messageBox("Request with ID: " + deleterequestId + " has been deleted", "Request deleted");
            setup(globalVisa, hasCarBoolean, globaluserImage, globalTextAreaData);
            deleterequestId = null;
            labelCurrentStatus.setText("Request deleted successfully!");
            logger.info("Request deleted successfully!");
        }
    }

    //Proposing a free parking on a date
    public void ApplyFreeParking() throws IOException {
        if(checkboxProposeFreeParking.isSelected())
        {
            String newFreeParkingjson;
            ObservableList<FreeParking> requestData = methodClass.searchAllRequest();

            //Checking if date is already in database
            //Putting all date of Table free parking to an Array
            requestDataArray.clear();
            Iterator<FreeParking> requestIterator = requestData.iterator();
            while (requestIterator.hasNext()) {
                requestDataArray.add(requestIterator.next().getDate());
            }

            if(requestDataArray.contains(dpApplyFreeParking.getValue().toString()))
            {
                methodClass.messageBox("You already proposed a parking slot for this day", "Duplication error");
                labelCurrentStatus.setText("Date already proposed.");
            }
            else
            {
                newFreeParkingjson = "    {\n        \"date\": \"" + dpApplyFreeParking.getValue() + "\",\n" +
                        "        \"user\": {\"visa\":\"" + globalVisa + "\"}\n" +
                        "    }";

                String response = okHttpPost.post("http://localhost:8080/cppk/addFreeParking/", newFreeParkingjson);
                labelCurrentStatus.setText(response);
                logger.debug(response);
            }
            checkboxProposeFreeParking.setSelected(false);
        }
        else
        {
            methodClass.messageBox("Please check the Checkbox before proposing a free parking slot", "User error");
            logger.debug("Checkbox error");
            labelCurrentStatus.setText("Checkbox error");
        }
    }

    public void ExportToExcel() {
        methodClass.exportToExcel(tableviewDemandA, tableviewDemandD);
        labelCurrentStatus.setText("An excel file has been created!");
        logger.debug("Excel file created");
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



    //Switching and Communicating with other Scenes/FXMLs
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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("parkingApplyForParking.fxml"));
        root = loader.load();
        parkingApplyForParkingController parkingApplyForParkingController = loader.getController();

        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        parkingApplyForParkingController.setup(globalVisa, hasCarBoolean, globaluserImage, globalTextAreaData);
        stage.show();
    }

    public void switchToManageParking(MouseEvent e) throws IOException {
        labelCurrentStatus.setText("Already in Manage Parking");
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
}
