package com.epam.nosql.example.api;

import com.epam.nosql.example.dto.Sport;
import com.epam.nosql.example.dto.User;
import com.epam.nosql.example.service.UserServiceImpl;
import com.epam.nosql.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        return ResponseEntity
                .ok()
                .body(userService.get(id));
    }

    @PostMapping()
    public ResponseEntity<Void> createEmployee(@RequestBody User user) {
        userService.create(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return ResponseEntity
                .ok()
                .body(userService.getByEmail(email));
    }

    @PutMapping("/{id}/sport")
    public ResponseEntity<User> addSportsToUser(@PathVariable String id, @RequestBody List<Sport> sports) {
        return ResponseEntity
                .ok()
                .body(userService.addSports(id, sports));
    }

    @GetMapping("/sport/{sportName}")
    public ResponseEntity<List<User>> getUsersBySportName(@PathVariable String sportName) {
        return ResponseEntity
                .ok()
                .body(userService.getUsersBySportName(sportName));
    }

}
