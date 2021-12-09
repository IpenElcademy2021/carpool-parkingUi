package com.example.loginpage;

import com.example.loginpage.models.FreeParking;
import com.example.loginpage.models.FreeParkingUserCarOwners;
import com.example.loginpage.models.RequestUserCarOwners;
import com.example.loginpage.oop.MethodClass;
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
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.*;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class parkingDashboardController {
    //OOP
    MethodClass methodClass = new MethodClass();

    @FXML
    private TableColumn tablecolumnDate, tablecolumnParkingSlot, tablecolumnStatus, tablecolumnCourtesyof, tablecolumnParkingOParkingSlot, tablecolumnParkingOName, tablecolumnParkingOPhoneNumber, tablecolumnParkingOVISA;
    @FXML
    private TableView tableviewDashboard, tableviewParkingOwners;
    @FXML
    private Label labelLoggedVisa, labelCurrentStatus, labelAboveTVDashboard;
    @FXML
    private ImageView imageviewUser, sidemenuApplyParking, sidemenuCredits, sidemenuManageParking, sidemenuParkingDashboard;
    @FXML
    private TextArea textareaUserInfo;
    @FXML
    private TextField textfieldFilterFreeParking;

    private Stage stage;
    private Scene scene;
    private Parent root;

    String globalVisa, globalTextAreaData;
    Boolean hasCarBoolean;
    Image globaluserImage;
    List<String> carUsersArray = new ArrayList<String>();
    ObservableList<RequestUserCarOwners> globalData = FXCollections.observableArrayList();
    private final Logger logger = LoggerFactory.getLogger(parkingDashboardController.class);


    public void setup(String globalvisa) throws IOException {

        labelLoggedVisa.setText(globalvisa);
        globalVisa = labelLoggedVisa.getText();

        //Setting image
        globaluserImage = new Image("file:src/main/resources/com/example/loginpage/visaImages/" + globalVisa + ".png");
        //Changes Image of User and his Manager
        imageviewUser.setImage(globaluserImage);

        //Retrieving Table User data
        JSONObject jsonUserObject = methodClass.findUserbyVisa(globalvisa);

        String nom = jsonUserObject.get("name").toString();
        String address = jsonUserObject.get("address").toString();
        String phoneNumber = jsonUserObject.get("phoneNumber").toString();

        //Setting Table User data to Textfield
        globalTextAreaData = "Nom: " + nom + "\n" +
                "Address: " + address + "\n" +
                "Mobile: "  + phoneNumber + "\n";

        textareaUserInfo.setText(globalTextAreaData);

        //Populating tableview
        ObservableList<RequestUserCarOwners> data = methodClass.getMyRequestByUser(labelLoggedVisa.getText());
        ObservableList<RequestUserCarOwners> carusersdata = methodClass.searchAllCarOwners();

        //Adding CarOwner's Users to an Array and Check if Logged User has car
        Iterator<RequestUserCarOwners> iterator = carusersdata.iterator();
        while (iterator.hasNext()) {
            carUsersArray.add(iterator.next().getVisa());
        }

        if(carUsersArray.contains(globalvisa.toUpperCase()))
        {
            hasCarBoolean = true;
            sidemenuApplyParking.setDisable(true);
            tableviewDashboard.setVisible(false);
            tableviewDashboard.setManaged(false);
            labelAboveTVDashboard.setVisible(false);
            labelAboveTVDashboard.setManaged(false);
            labelCurrentStatus.setText("CarOwner Detected, Apply parking disabled.");
        }
        else
        {
            hasCarBoolean = false;
            sidemenuManageParking.setDisable(true);
            labelCurrentStatus.setText("Non-CarOwner Detected, Manage parking disabled.");
        }
        carUsersArray.clear();


        //Populating table
        tablecolumnDate.setCellValueFactory(new PropertyValueFactory<RequestUserCarOwners,String>("date"));
        tablecolumnStatus.setCellValueFactory(new PropertyValueFactory<RequestUserCarOwners,String>("status"));
        tablecolumnCourtesyof.setCellValueFactory(new PropertyValueFactory<RequestUserCarOwners,String>("driverVisa"));
        tableviewDashboard.setItems(data);

        tablecolumnParkingOName.setCellValueFactory(new PropertyValueFactory<RequestUserCarOwners,String>("name"));
        tablecolumnParkingOParkingSlot.setCellValueFactory(new PropertyValueFactory<RequestUserCarOwners,String>("parkingSlot"));
        tablecolumnParkingOPhoneNumber.setCellValueFactory(new PropertyValueFactory<RequestUserCarOwners,String>("phoneNumber"));
        tablecolumnParkingOVISA.setCellValueFactory(new PropertyValueFactory<RequestUserCarOwners,String>("visa"));
        tableviewParkingOwners.setItems(carusersdata);

        //Filtering table
        globalData = data;
        FilteredList<RequestUserCarOwners> filteredData = new FilteredList(this.globalData, (b) -> {
            return true;
        });

        this.textfieldFilterFreeParking.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((myrequest) -> {
                if (newValue != null && !newValue.isEmpty()) {
                    String lowerCaseFilter = newValue.toLowerCase();

                    if (myrequest.getDate().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                        return true;
                    } else if (myrequest.getStatus().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                        return true;
                    } else if (myrequest.getDriverVisa().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                        return true;
                    } else {
                        return String.valueOf(myrequest.getDate()).indexOf(lowerCaseFilter) != -1;
                    }
                } else {
                    return true;
                }
            });
        });
        SortedList<RequestUserCarOwners> sortedData = new SortedList(filteredData);
        sortedData.comparatorProperty().bind(this.tableviewDashboard.comparatorProperty());
        this.tableviewDashboard.setItems(sortedData);

        //Logging
        labelCurrentStatus.setText("Your parking dashboard loaded successfully!");
        logger.info("Dashboard loaded successfully");
    }

    public void clearFilter() {
        textfieldFilterFreeParking.clear();
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
        logger.info("Opening browser");
    }

    public void Exit() {
        methodClass.Exit();
        logger.debug("Exit method executed from methodClass");
    }




    //Switching and Communicating with other Scenes/FXMLs
    public void switchToParkingDashboard(MouseEvent e) throws IOException {
        labelCurrentStatus.setText("Already in Parking Dashboard");
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("parkingManageParking.fxml"));
        root = loader.load();
        parkingManageParkingController parkingManageParkingController = loader.getController();

        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        parkingManageParkingController.setup(globalVisa, hasCarBoolean, globaluserImage, globalTextAreaData);
        stage.show();
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
