package com.evanhayes.evanhayes.Security;

public interface SecurityService {
    String findLoggedInUsername();

    void autologin(String username, String password);
}