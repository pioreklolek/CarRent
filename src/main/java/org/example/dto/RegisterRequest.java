package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;

public class RegisterRequest {
    @NotBlank
    @Size(min = 3, max = 30)
    private String login;

    @NotBlank
    @Size(min = 6 , max = 60)
    private String password;

    private  String role = "user";

    public RegisterRequest() {}

    public RegisterRequest(String login, String password,String role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
