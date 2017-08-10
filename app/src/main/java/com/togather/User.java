package com.togather;

import android.location.Location;

import com.firebase.geofire.GeoLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.togather.Togather.firebaseUser;
import static com.togather.Togather.ref;

/**
 * Created by ander on 2017-07-30.
 */

public class User {

    //private GeoLocation location;
    private String name;
    private List<Map<String, String>> profile = new ArrayList<>(5);

    public User() {
        System.out.println("PROFILE SIZE: " + this.profile.size());
    }

    public User(GeoLocation location, String name, List<Map<String, String>> profile) {
        //this.location = location;
        this.name = name;
        this.profile = profile;
    }
/*
    public GeoLocation getLocation() {
        return location;
    }

    public void setLocation(GeoLocation location) {
        this.location = location;
    }
*/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        ref.child("users").child(firebaseUser.getUid()).child("name").setValue(name);
    }

    public List<Map<String, String>> getProfile() {
        return profile;
    }

    public void addToProfile(Integer number, String question, String answer) {
        Map<String, String> response = new HashMap<>();
        response.put(question, answer);
        profile.add(number - 1, response);
        ref.child("users").child(firebaseUser.getUid()).child("profile").setValue(profile);
    }

    public void save() {
        ref.child("users").child(firebaseUser.getUid()).setValue(this);
    }
}
