package com.example.loginpage.models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RequestUserCarOwners {

    private @Getter @Setter String requestId;
    private @Getter @Setter String date;
    private @Getter @Setter String driverVisa;
    private @Getter @Setter String phoneNumber;
    private @Getter @Setter String address;
    private @Getter @Setter String password;
    private @Getter @Setter String name;
    private @Getter @Setter String status;
    private @Getter @Setter String visa;
    private @Getter @Setter String carPlate;
    private @Getter @Setter String parkingSlot;
    private @Getter @Setter String carOwners;
    public RequestUserCarOwners(String date, String status, String driverVisa) {
        this.date = date;
        this.driverVisa = driverVisa;
        this.status = status;
    }

    public RequestUserCarOwners(String requestId, String date, String driverVisa, String status, String visa) {
        this.requestId = requestId;
        this.date = date;
        this.driverVisa = driverVisa;
        this.status = status;
        this.visa = visa;
    }

    public RequestUserCarOwners(String phoneNumber, String address, String password, String name, String visa, String carPlate, String parkingSlot) {
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.password = password;
        this.name = name;
        this.visa = visa;
        this.carPlate = carPlate;
        this.parkingSlot = parkingSlot;
    }



    public RequestUserCarOwners(String visa, String password, String name, String address, String phoneNumber, String carPlate, String parkingSlot, String carOwners) {
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.password = password;
        this.name = name;
        this.visa = visa;
        this.carPlate = carPlate;
        this.parkingSlot = parkingSlot;
        this.carOwners = carOwners;
    }

    public RequestUserCarOwners(String carPlate, String parkingSlot, String name, String address, String phoneNumber, String visa) {
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.name = name;
        this.visa = visa;
        this.carPlate = carPlate;
        this.parkingSlot = parkingSlot;
    }
}
