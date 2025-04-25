package frontoffice.controllers;

import models.user;

public class UserSession {
    private static UserSession instance;
    private user currentUser;

    private UserSession(user currentUser) {
        this.currentUser = currentUser;
    }

    public static void initSession(user currentUser) {
        if (instance == null) {
            instance = new UserSession(currentUser);
        }
    }

    public static UserSession getInstance() {
        return instance;
    }

    public user getUser() {
        return currentUser;
    }

    public void setUser(user updatedUser) {
        this.currentUser = updatedUser;
    }

    public static void clearSession() {
        instance = null;
    }

    public static boolean isAuthenticated() {
        return instance != null;
    }

    public static boolean isAdmin() {
        return isAuthenticated() && instance.currentUser.getRoles().toLowerCase().contains("admin");
    }


    public static boolean isUser() {
        return isAuthenticated() && !isAdmin();
    }



}
