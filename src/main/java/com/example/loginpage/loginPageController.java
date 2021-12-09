package com.example.loginpage;

import com.example.loginpage.oop.Login;
import com.example.loginpage.oop.MethodClass;
import com.example.loginpage.oop.RestAPI.OkHttpGet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import javax.swing.text.html.ImageView;
import java.io.IOException;

public class loginPageController {
    //OOP
    OkHttpGet okHttpGet = new OkHttpGet();
    MethodClass methodClass = new MethodClass();
    Login login = new Login();


    @FXML
    private TextField textFieldUsername;

    @FXML
    private PasswordField passwordFieldUsername;

    @FXML
    private HBox HboxLogin;
    @FXML
    private Label labelLoggedVisa;

    @FXML
    private Button buttonLogout;



    String globalVisa = "";
    String name = " ";

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void setup(String logoutvisa) throws IOException {
        globalVisa = logoutvisa;

        if (globalVisa == "")
        {

            labelLoggedVisa.setText ("Please Login!");

        }
        else
        {
            HboxLogin.setVisible(false);
            HboxLogin.setManaged(false);
            labelLoggedVisa.setText ("Hello "+globalVisa);
        }
    }

    public void logout(MouseEvent e) throws IOException {
        setup("");
        HboxLogin.setVisible(true);
        HboxLogin.setManaged(true);
        textFieldUsername.setText("");
        passwordFieldUsername.setText("");
        labelLoggedVisa.setText("");
        buttonLogout.setVisible(false);
    }



    public void login(ActionEvent event) throws IOException {

        JSONObject jsonObject = login.login(textFieldUsername.getText(), passwordFieldUsername.getText());
        globalVisa = jsonObject.get("visa").toString();
        name = jsonObject.get("name").toString();

        //Changing label of User and his Manager
        HboxLogin.setVisible(false);
        HboxLogin.setManaged(false);
        labelLoggedVisa.setText ("Hello "+name);
        buttonLogout.setVisible(true);



        String visa = jsonObject.get("visa").toString();
        String password = jsonObject.get("password").toString();
        String address = jsonObject.get("address").toString();
        String phoneNumber = jsonObject.get("phoneNumber").toString();

    }




    public void switchToParkingDashboard(MouseEvent e) throws IOException {
        if (globalVisa == "")
        {
            methodClass.messageBox("Make sure you are logged in first!", "Not logged in.");
        }
        else {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("parkingDashboard.fxml"));
            root = loader.load();
            parkingDashboardController parkingDashboardController = loader.getController();

            stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            parkingDashboardController.setup(globalVisa);
            stage.show();
        }
    }

    public void switchToCarpoolDashboard(MouseEvent e) throws IOException {
        if (globalVisa == "")
        {
            methodClass.messageBox("Please loggin first!", "Not logged in.");
        }
        else {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("carpoolDashBoard.fxml"));
            root = loader.load();
            CarpoolDashboardController carpoolDashboardController = loader.getController();

            stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            carpoolDashboardController.setup(globalVisa);
            stage.show();
        }
    }
}