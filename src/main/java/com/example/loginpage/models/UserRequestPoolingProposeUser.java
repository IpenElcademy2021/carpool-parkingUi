package com.example.loginpage.models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRequestPoolingProposeUser {


    private @Getter @Setter String visa;
    private @Getter @Setter String region;
    private @Getter @Setter String date;
    private @Getter @Setter String pickUpPoint;
    private @Getter @Setter String pickUpTime;
    private @Getter @Setter String departureTime;
    private @Getter @Setter String reservationStatus;
    private @Getter @Setter String comment;

}
