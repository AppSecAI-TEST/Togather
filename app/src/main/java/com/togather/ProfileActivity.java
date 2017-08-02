package com.togather;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.beardedhen.androidbootstrap.TypefaceProvider;

import java.util.List;
import java.util.Map;

import static com.togather.Togather.firebaseUser;
import static com.togather.Togather.getQuestions;
import static com.togather.Togather.hideKeyboard;
import static com.togather.Togather.user;


/**
 * Created by ander on 2017-07-30.
 */

public class ProfileActivity extends BaseActivity {

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
                Map<String, String> response = user.getProfile().get(i);
                String question = response.keySet().iterator().next();
                String answer = response.values().iterator().next();
                loadQuestion(spinnerId, question, editId, answer, i);
            }

            loadQuestion(spinnerId, null, editId, null, i);
        }
    }

    public void loadQuestion(int spinnerId, String question, int editId, String answer, int number) {
        loadSpinner(spinnerId, question, number);
        loadEdit(editId, answer, number);
    }

    public void loadSpinner(int id, String question, int number) {
        List<String> spinnerArray = getQuestions();

        Spinner spinner = (Spinner) findViewById(id);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
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

    View.OnFocusChangeListener nameListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                hideKeyboard(ProfileActivity.this, v);

                EditText name = (EditText) v;
                user.setName(name.getText().toString());
            }
        }
    };

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
            }
        }
    };
}
