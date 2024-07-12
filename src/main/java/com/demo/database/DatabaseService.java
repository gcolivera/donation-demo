package com.demo.database;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {
    @Value("${app.db.url}")
    private String url;
    @Value("${app.db.username}")
    private String user;
    @Value("${app.db.password}")
    private String password;

    public String getUrl() {
        return url;
    }
    public String getUser() {
        return user;
    }
    public String getPassword() {
        return password;
    }    

}
