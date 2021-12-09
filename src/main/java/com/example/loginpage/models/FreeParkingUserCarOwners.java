package com.example.loginpage.models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FreeParkingUserCarOwners {

    private @Getter @Setter String freeParkingID;
    private @Getter @Setter String date;
    private @Getter @Setter String visa;
    private @Getter @Setter String password;
    private @Getter @Setter String name;
    private @Getter @Setter String address;
    private @Getter @Setter String phoneNumber;
    private @Getter @Setter String carPlate;
    private @Getter @Setter String parkingSlot;

    public FreeParkingUserCarOwners(String freeParkingID, String date, String visa, String password, String name, String address, String phoneNumber) {
        this.freeParkingID = freeParkingID;
        this.date = date;
        this.visa = visa;
        this.password = password;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public FreeParkingUserCarOwners(String carPlate, String parkingSlot, String name, String address, String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.name = name;
        this.carPlate = carPlate;
        this.parkingSlot = parkingSlot;
    }


}
