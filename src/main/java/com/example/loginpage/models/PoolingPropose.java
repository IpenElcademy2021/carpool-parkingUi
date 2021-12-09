package com.example.loginpage.models;

import lombok.*;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PoolingPropose {

    private @Getter @Setter String date;
    private @Getter @Setter String region;
    private @Getter @Setter String pickUpPoint;
    private @Getter @Setter String pickUpTime;
    private @Getter @Setter String departureTime;
    private @Getter @Setter String seat;
    private @Getter @Setter String visa;
    private @Getter @Setter String poolId;

}
