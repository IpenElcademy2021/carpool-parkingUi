package com.example.loginpage.models;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FreeParking {

    private @Getter @Setter String freeParkingID;
    private @Getter @Setter String visa;
    private @Getter @Setter String date;

    public FreeParking(String date) {
        this.date = date;
    }
}
