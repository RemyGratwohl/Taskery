package com.remygratwohl.taskery.models;

/**
 * Created by Remy on 3/1/2017.
 */

public class ApiError {
    private String name;
    private String description;

    public ApiError(){
    }
    public ApiError(String errorName, String errorMessage) {
        this.name = errorName;
        this.description = errorMessage;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
