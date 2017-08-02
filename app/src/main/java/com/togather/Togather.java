package com.togather;

import android.app.Activity;
import android.app.Application;
import android.location.Location;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ander on 2017-07-29.
 */

public class Togather extends Application {

    public static FirebaseUser firebaseUser;
    public static FirebaseAuth auth;
    public static FirebaseDatabase database;
    public static DatabaseReference ref;
    public static UserUpdater user;

    private static List<String> questions;

    public static List<String> getQuestions() {
        return questions;
    }

    public static void addUserListener() {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = new UserUpdater(dataSnapshot.getValue(User.class));
                System.out.println("user: " + user.getProfile().size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        ref.child("users").child(firebaseUser.getUid()).addValueEventListener(listener);
    }

    public static void addQuestionListener() {
        ref.child("public").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("dataSnapshot: " + dataSnapshot.getValue());
                updateQuestions((List<String>) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static void updateQuestions(List<String> list) {
        questions = new ArrayList<>();

        for (String question : list) {
            questions.add(question);
        }
    }

    public static void saveUser() {
        ref.child("users").child(firebaseUser.getUid()).setValue(user);
    }

    public static void hideKeyboard(Activity activity, View v) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
