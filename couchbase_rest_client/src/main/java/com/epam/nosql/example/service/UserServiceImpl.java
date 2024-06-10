package com.epam.nosql.example.service;

import com.epam.nosql.example.dto.Sport;
import com.epam.nosql.example.dto.User;
import com.epam.nosql.example.repositories.UserRepository;
import com.epam.nosql.example.exceptions.EntityNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public User get(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public User getByEmail(String email) {
        return repository.getUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));
    }

    @Override
    public void create(User user) {
        repository.save(user);
    }

    @Override
    public User addSports(String id, List<Sport> sports) {
        User user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
        user.setSports(sports);
        return repository.save(user);
    }

    @Override
    public List<User> getUsersBySportName(String sportName) {
        return repository.findUserBySportName(sportName);
    }

}