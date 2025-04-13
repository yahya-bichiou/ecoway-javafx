package controllers;

import models.user;
import services.userService;

import java.util.List;

public class userController {

    private final userService userService = new userService();

    public void addUser(String name, String email, String password, String imageProfile) {
        user u = new user(name, email, password, imageProfile);
        userService.add(u);
    }

    public void updateUser(int id, String name, String email, String password, String imageProfile) {
        user u = new user((long) id, name, email, password, imageProfile);
        u.setId(id);
        userService.update(u);
    }

    public void deleteUser(int id) {
        user u = new user();
        u.setId(id);
        userService.delete(u);
    }

    public void listUsers() {
        List<user> users = userService.getAll();
        for (user u : users) {
            System.out.println(u);
        }
    }
}
