package com.togather;

import android.location.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by ander on 2017-07-30.
 */

public class User {

    private Location location;
    private String name;
    private List<Map<String, String>> profile = new ArrayList<>(5);

    public User() {
        System.out.println("PROFILE SIZE: " + this.profile.size());
    }

    public User(Location location, String name, List<Map<String, String>> profile) {
        this.location = location;
        this.name = name;
        this.profile = profile;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Map<String, String>> getProfile() {
        return profile;
    }

    public void addToProfile(Integer number, String question, String answer) {
        Map<String, String> response = new HashMap<>();
        response.put(question, answer);
        profile.add(number - 1, response);
    }


}
