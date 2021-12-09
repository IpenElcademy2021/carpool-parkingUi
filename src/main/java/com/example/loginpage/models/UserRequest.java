package com.example.loginpage.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
public class UserRequest {

    private @Getter @Setter Long userRequestId;
    private @Getter @Setter String reservationStatus;
    private @Getter @Setter String comment;
    private @Getter @Setter String userVisa;
    private @Getter @Setter String date;
    private @Getter @Setter String region;
    private @Getter @Setter String pickUpPoint;
    private @Getter @Setter String pickUpTime;
    private @Getter @Setter String departureTime;

    public UserRequest(String comment, String userVisa, String date, String region, String pickUpPoint, String pickUpTime, String departureTime) {
        this.comment = comment;
        this.userVisa = userVisa;
        this.date = date;
        this.region = region;
        this.pickUpPoint = pickUpPoint;
        this.pickUpTime = pickUpTime;
        this.departureTime = departureTime;
    }
}
