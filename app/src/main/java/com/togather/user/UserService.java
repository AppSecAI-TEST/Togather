package com.togather.user;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.togather.Togather.questions;
import static com.togather.Togather.user;

/**
 * Created by ander on 2017-08-10.
 */

public class UserService {

    public static void addQuestionListener(DatabaseReference ref) {
        ref.child("public").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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

    public static void addUserListener(DatabaseReference ref, String uid) {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                //loadProfile();
                //loadConversations();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        ref.child("users").child(uid).addValueEventListener(listener);
    }

    public static void saveNameOfUser(DatabaseReference ref, String uid, String name) {
        ref.child("users").child(uid).child("name").setValue(name);
    }

    public static void saveQuestionAndAnswer(DatabaseReference ref, String uid, List<Map<String, String>> profile) {
        ref.child("users").child(uid).child("profile").setValue(profile);
    }

    public static void saveUser(DatabaseReference ref, String uid, User user) {
        ref.child("users").child(uid).setValue(user);
    }
}
