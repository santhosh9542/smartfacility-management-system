package com.smartfacility.user.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/profile")
    public String profile() {
        return "User Profile Data";
    }

    @GetMapping("/all")
    public String allUsers() {
        return "All Users Data - ADMIN ACCESS";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        return "Deleted User Id : " + id;
    }
}