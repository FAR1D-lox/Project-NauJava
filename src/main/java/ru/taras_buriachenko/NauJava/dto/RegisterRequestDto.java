package ru.taras_buriachenko.NauJava.dto;

public class RegisterRequestDto {

    private String username;
    private String phoneNubmer;
    private String password;

    public RegisterRequestDto(String username, String phoneNumber, String password) {
        this.username = username;
        this.phoneNubmer = phoneNumber;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPhoneNumber() {
        return phoneNubmer;
    }

    public String getPassword() {
        return password;
    }
}
