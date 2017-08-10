package com.togather;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static com.togather.Togather.addQuestionListener;
import static com.togather.Togather.addUserListener;
import static com.togather.Togather.auth;
import static com.togather.Togather.database;
import static com.togather.Togather.firebaseUser;
import static com.togather.Togather.ref;


public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TypefaceProvider.registerDefaultIconSets();

        Togather.setUp(FirebaseAuth.getInstance(), FirebaseDatabase.getInstance());

        addQuestionListener();

        String credentials = getCredentials();
        if (!credentials.isEmpty()) {
            String email = credentials.split(";")[0];
            String password = credentials.split(";")[1];
            login(email, password);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        firebaseUser = auth.getCurrentUser();
        if (firebaseUser == null) {
            return;
        }
        addUserListener();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private String getCredentials() {
        File file = new File(this.getFilesDir(), "credentials");

        if (!file.exists()) {
            System.out.println("credentials don't exist");
            return "";
        }

        String credentials = "";

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            credentials = br.readLine();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Credentials: " + credentials);
        return credentials;
    }

    public void login(View view) {
        EditText emailEditText = (EditText) findViewById(R.id.email);
        EditText passwordEditText = (EditText) findViewById(R.id.password);

        final String email = emailEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        login(email, password);
    }

    public void login(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            firebaseUser = auth.getCurrentUser();
                            addUserListener();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    public void signup(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}
