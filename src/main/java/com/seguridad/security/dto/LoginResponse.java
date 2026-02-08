package com.seguridad.security.dto;

public class LoginResponse {
    private String accessToken;
    private long expiresIn;
    private String role;
    private String username;

    public LoginResponse() {}
    public LoginResponse(String accessToken, long expiresIn, String role, String username) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.role = role;
        this.username = username;
    }

    // getters/setters
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public long getExpiresIn() { return expiresIn; }
    public void setExpiresIn(long expiresIn) { this.expiresIn = expiresIn; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
