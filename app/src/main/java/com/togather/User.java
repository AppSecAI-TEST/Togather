package com.togather;

import android.location.Location;

import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ander on 2017-07-30.
 */

public class User {

    private Location location;
    private FirebaseUser firebaseUser;
    private String name;
    private Map<String, String> profile = new HashMap<>();

    public User() {

    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public void setFirebaseUser(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getProfile() {
        return profile;
    }

    public void addToProfile(String question, String answer) {
        profile.put(question, answer);
    }
}
