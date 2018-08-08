package com.evanhayes.evanhayes.Security;


import com.evanhayes.evanhayes.models.User.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}