package com.example.loginpage.oop;

import com.example.loginpage.models.CarpoolManagement;
import com.example.loginpage.oop.RestAPI.OkHttpGet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class CarpoolManagementMethod {


        OkHttpGet okHttpGet = new OkHttpGet();

        public ObservableList<CarpoolManagement> getCarpoolRequestByVisa (String globalVisa) throws IOException {

            String url = "http://localhost:8080/prc/getRequestByVisa/"+globalVisa;
            String response = okHttpGet.run(url);
            ObservableList<CarpoolManagement> CarpoolRequestReturn = FXCollections.observableArrayList();

            try {
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(response);
                JSONArray jsonArray = (JSONArray) obj;

                ObservableList<CarpoolManagement> CarpoolRequestData = FXCollections.observableArrayList();
                String visa = "";
                String date = "";
                String reservationStatus = "";
                String seat = "";
                String comment = "";
                String poolId = "";
                String userRequestId = "";


                for (var i = 0; i < jsonArray.toArray().length; i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    JSONObject jsonObjectPooling = (JSONObject) jsonObject.get("pooling");
                    JSONObject jsonObjectUser = (JSONObject) jsonObject.get("user");


                    visa = jsonObjectUser.get("visa").toString();
                    date = jsonObjectPooling.get("date").toString().substring(0, 10);
                    reservationStatus = jsonObject.get("reservationStatus").toString();
                    seat = jsonObjectPooling.get("seat").toString();
                    comment = jsonObject.get("comment").toString();
                    poolId = jsonObjectPooling.get("poolId").toString();
                    userRequestId = jsonObject.get("userRequestId").toString();
                    CarpoolRequestData.add(new CarpoolManagement(visa, date, reservationStatus, seat, comment,poolId, userRequestId));
                    CarpoolRequestReturn = CarpoolRequestData;

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return CarpoolRequestReturn;
        }
    }

