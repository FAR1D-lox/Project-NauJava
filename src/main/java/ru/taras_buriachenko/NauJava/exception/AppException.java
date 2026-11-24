package ru.taras_buriachenko.NauJava.exception;

public class AppException {
    private String message;

    private AppException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static AppException create(Throwable e) {
        return new AppException(e.getMessage());
    }

    public static AppException create(String message) {
        return new AppException(message);
    }
}