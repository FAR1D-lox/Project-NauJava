package ru.taras_buriachenko.NauJava.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AppException {
    private String message;

    private AppException(String message) {
        this.message = message;
    }

    public static AppException create(Throwable e) {
        return new AppException(e.getMessage());
    }

    public static AppException create(String message) {
        return new AppException(message);
    }
}