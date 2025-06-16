package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;

import java.util.Set;

public class RegisterRequest {
    @NotBlank
    @Size(min = 3, max = 30)
    private String login;

    @NotBlank
    @Size(min = 6 , max = 60)
    private String password;

    private Set<String> roles;

    public RegisterRequest() {}

    public RegisterRequest(String login, String password,Set<String> roles) {
        this.login = login;
        this.password = password;
        this.roles = roles;
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

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
