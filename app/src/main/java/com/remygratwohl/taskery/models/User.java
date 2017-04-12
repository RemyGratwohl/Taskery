package com.remygratwohl.taskery.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Remy on 3/1/2017.
 */

public class User {

    private String email;
    private String jwtToken;
    private Date createdAt;
    private Date updatedAt;
    private Character character;

    public User(String email, String jwtToken, Date createdAt, Date updatedAt, Character character) {
        this.email = email;
        this.jwtToken = jwtToken;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.character = character;
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

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    private Date convertStringtoDate(String dateString){

        try{
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
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
                ", character" + (character == null ? "" : character.toString()) +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
