package com.togather.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ander on 2017-07-30.
 */

public class User {

    private String name;
    private List<Map<String, String>> profile = new ArrayList<>(5);

    public User() {

    }

    public User(String name, List<Map<String, String>> profile) {
        this.name = name;
        this.profile = profile;
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

        if (profile.size() < number) {
            profile.add(number - 1, response);
        } else {
            profile.set(number - 1, response);
        }
    }
}
