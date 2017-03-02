package com.remygratwohl.taskery.models;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Remy on 3/1/2017.
 */

public class User {

    private String email;
    private String jwtToken;
    private Date createdAt;
    private Date updatedAt;

    public User(String email, String jwtToken, String cDateString, String uDateString) {
        this.email = email;
        this.jwtToken = jwtToken;

        this.createdAt = convertStringtoDate(cDateString);
        this.updatedAt = convertStringtoDate(uDateString);

    }

    public String getEmail() {
        return email;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    private Date convertStringtoDate(String dateString){

        try{
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            String dts = dateString.replaceAll("([\\+\\-]\\d\\d):(\\d\\d)","$1$2");
            return formatter.parse(dts);
        }catch (Exception e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", jwtToken='" + jwtToken + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}