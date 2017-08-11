package com.togather;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

import static com.togather.Togather.user;
import static com.togather.UXService.UXService.hideKeyboard;
import static com.togather.user.UserService.saveNameOfUser;
import static com.togather.user.UserService.saveQuestionAndAnswer;


/**
 * Created by ander on 2017-07-30.
 */

public class ProfileActivity extends BaseActivity {

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TypefaceProvider.registerDefaultIconSets();

        loadProfile();
    }

    public void loadProfile() {
        EditText name = (EditText) findViewById(R.id.name);
        name.setOnFocusChangeListener(nameListener);
        if (user.getName() != null) {
            name.setText(user.getName());
        }

        loadQuestions();
    }

    public void loadQuestions() {
        for (int i = 1; i < 6; i++) {
            String editStringId = "answer" + i;
            int editId = getResources().getIdentifier(editStringId, "id", getPackageName());

            String spinnerStringId = "question" + i;
            int spinnerId = getResources().getIdentifier(spinnerStringId, "id", getPackageName());

            if (user.getProfile().size() > i - 1) {
                Map<String, String> response = user.getProfile().get(i - 1);
                String question = response.keySet().iterator().next();
                String answer = response.values().iterator().next();
                loadQuestion(spinnerId, question, editId, answer, i);
            } else {
                loadQuestion(spinnerId, null, editId, null, i);
            }
        }
    }

    public void loadQuestion(int spinnerId, String question, int editId, String answer, int number) {
        loadSpinner(spinnerId, question);
        loadEdit(editId, answer, number);
    }

    public void loadSpinner(int id, String question) {

        Spinner spinner = (Spinner) findViewById(id);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Togather.questions);
        spinner.setAdapter(spinnerArrayAdapter);

        int index = spinnerArrayAdapter.getPosition(question);
        if (index >= 0) {
            spinner.setSelection(index);
        }
    }

    public void loadEdit(int id, String answer, int number) {
        EditText editText = (EditText) findViewById(id);
        if (answer != null) {
            editText.setText(answer);
        }

        editText.setTag(Integer.toString(number));
        editText.setOnFocusChangeListener(questionListener);
    }

    // Listener for the name input, upon change of focus, update backend and current User
    View.OnFocusChangeListener nameListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                hideKeyboard(ProfileActivity.this, v);

                EditText nameEditText = (EditText) v;
                String name = nameEditText.getText().toString();
                user.setName(name);
                saveNameOfUser(ref, uid, name);
            }
        }
    };

    // Listener for the questions, same idea as the nameListener
    View.OnFocusChangeListener questionListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                hideKeyboard(ProfileActivity.this, v);

                EditText answerEditText = (EditText) v;
                String answer = answerEditText.getText().toString();

                Integer number = Integer.parseInt((String) answerEditText.getTag());
                int id = getResources().getIdentifier("question" + number, "id", getPackageName());
                Spinner questionSpinner = (Spinner) findViewById(id);
                String question = questionSpinner.getSelectedItem().toString();

                user.addToProfile(number, question, answer);
                saveQuestionAndAnswer(ref, uid, user.getProfile());
            }
        }
    };
}
