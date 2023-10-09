package org.m7.model;

import lombok.Data;

@Data
public class Error {
    private String type = "error";
    private String message;

    public Error(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" +
                "\"type\": \"" + type + "\"," +
                "\"message\": \"" + message + "\"" +
                '}';
    }
}
