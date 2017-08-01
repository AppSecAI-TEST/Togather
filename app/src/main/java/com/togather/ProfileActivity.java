package com.togather;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.beardedhen.androidbootstrap.TypefaceProvider;

import java.util.List;

import static com.togather.Togather.getQuestions;
import static com.togather.Togather.hideKeyboard;
import static com.togather.Togather.updateResponse;


/**
 * Created by ander on 2017-07-30.
 */

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TypefaceProvider.registerDefaultIconSets();

        loadQuestions();
    }

    public void loadQuestions() {
        int lastId = findViewById(R.id.questionText).getId();
        for (int i = 1; i < 6; i++) {
            String layoutStringId = "questionField" + i;
            int layoutId = getResources().getIdentifier(layoutStringId, "id", getPackageName());

            String editStringId = "answer" + i;
            int editId = getResources().getIdentifier(editStringId, "id", getPackageName());

            String spinnerStringId = "question" + i;
            int spinnerId = getResources().getIdentifier(spinnerStringId, "id", getPackageName());

            loadQuestion(layoutId, spinnerId, editId, lastId, i);

            lastId = layoutId;
        }
    }

    public void loadQuestion(int layoutId, int spinnerId, int editId, int lastId, int number) {
        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(params);
        layout.setId(layoutId);

        loadSpinner(layout, spinnerId);
        loadEdit(layout, editId, number);

        ConstraintLayout cl = (ConstraintLayout) findViewById(R.id.profile);
        cl.addView(layout);

        ConstraintSet set = new ConstraintSet();
        set.clone(cl);
        set.connect(layoutId, ConstraintSet.TOP, lastId, ConstraintSet.BOTTOM);
        set.applyTo(cl);
    }

    public void loadSpinner(LinearLayout layout, int id) {
        List<String> spinnerArray = getQuestions();
        System.out.println(spinnerArray);

        Spinner spinner = new Spinner(this);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setId(id);

        layout.addView(spinner);
    }

    public void loadEdit(LinearLayout layout, int id, int number) {
        EditText editText = new EditText(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        editText.setId(id);
        editText.setTag(Integer.toString(number));
        editText.setLayoutParams(params);
        editText.setOnFocusChangeListener(listener);

        layout.addView(editText);
    }

    View.OnFocusChangeListener listener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                hideKeyboard(ProfileActivity.this, v);

                EditText response = (EditText) v;
                System.out.println(response.getTag());
                Integer number = Integer.parseInt((String) response.getTag());
                int id = getResources().getIdentifier("question" + number, "id", getPackageName());
                Spinner question = (Spinner) findViewById(id);

                updateResponse(question, response, number);
            }
        }
    };
}
