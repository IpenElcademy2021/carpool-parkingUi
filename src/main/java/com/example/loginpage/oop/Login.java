package com.example.loginpage.oop;

import com.example.loginpage.oop.RestAPI.OkHttpGet;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;


public class Login {
    //OOP
    MethodClass methodClass = new MethodClass();
    OkHttpGet okHttpGet = new OkHttpGet();



    public JSONObject login(String providedvisa, String providedpassword) throws IOException {
        JSONObject jsonObject = null;

        if (providedvisa == "" || providedpassword == "") {
            methodClass.messageBox("Please ensure that the fields are not empty.", "User Error");
        }

        else {

            String url = "http://localhost:8080/cppk/login/" + providedvisa + "/" + providedpassword;
            System.out.println(okHttpGet.run(url));
            String response = okHttpGet.run(url);


            if (response.contains("error")) {
                methodClass.messageBox("Wrong login info, please try again!", "Login Failed");
            }
            else {
                try {
                    JSONParser parser = new JSONParser();
                    JSONObject jsonobj = (JSONObject) parser.parse(response);
                    jsonObject = jsonobj;
                }

                catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonObject;
    }




}
