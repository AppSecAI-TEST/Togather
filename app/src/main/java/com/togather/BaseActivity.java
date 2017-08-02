package com.togather;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

/**
 * Created by ander on 2017-08-01.
 */

public class BaseActivity extends AppCompatActivity {

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    public void profile(View view) {
        if (this.getClass() == ProfileActivity.class) {
            return;
        }
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void home(View view) {
        if (this.getClass() == MainActivity.class) {
            return;
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void conversations(View view) {
        //if (this.getClass() == ConversationsActivity.class) {
        //    return;
        //}
        //Intent intent = new Intent(this, ConversationsActivity.class);
        //startActivity(intent);
    }
}
