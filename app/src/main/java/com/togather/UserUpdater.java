package com.togather;

import android.location.Location;

import static com.togather.Togather.firebaseUser;
import static com.togather.Togather.ref;

/**
 * Created by ander on 2017-08-01.
 */

public class UserUpdater extends User {

    private User user;

    public UserUpdater(User user) {
        this.user = user;
        ref.child("users").child(firebaseUser.getUid()).setValue(user);
    }

    public void setLocation(Location location) {
        super.setLocation(location);
        ref.child("users").child(firebaseUser.getUid()).child("location").setValue(location);
    }

    public void setName(String name) {
        super.setName(name);
        ref.child("users").child(firebaseUser.getUid()).child("name").setValue(name);
    }

    public void addToProfile(Integer number, String question, String answer) {
        super.addToProfile(number, question, answer);
        ref.child("users").child(firebaseUser.getUid()).child("profile").setValue(user.getProfile());
    }

    public void save() {
        ref.child("users").child(firebaseUser.getUid()).setValue(this.user);
    }
}
