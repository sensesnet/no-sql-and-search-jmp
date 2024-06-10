package com.epam.nosql.example.service;

import com.epam.nosql.example.dto.Sport;
import com.epam.nosql.example.dto.User;

import java.util.List;

public interface UserService {
    User get(String id);

    User getByEmail(String email);

    void create(User user);

    User addSports(String id, List<Sport> sports);

    List<User> getUsersBySportName(String sportName);
}
