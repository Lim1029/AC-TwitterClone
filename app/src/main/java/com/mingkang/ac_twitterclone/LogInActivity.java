package com.mingkang.ac_twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private Button btnLogIn;
    private EditText edtUsername, edtPassword, edtEmail;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        setTitle("");

        btnLogIn = findViewById(R.id.btnLogIn);
        edtUsername = findViewById(R.id.edtUsernameLogIn);
        edtEmail = findViewById(R.id.edtEmailLogIn);
        edtPassword = findViewById(R.id.edtPasswordLogIn);
        progressBar = findViewById(R.id.progressBar);
        linearLayout = findViewById(R.id.linearLayout);

        btnLogIn.setOnClickListener(this);
        edtUsername.addTextChangedListener(this);
        edtEmail.addTextChangedListener(this);
        edtPassword.addTextChangedListener(this);

        if(ParseUser.getCurrentUser()!=null) {
            startActivity(new Intent(LogInActivity.this, AllUserActivity.class));
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_in_log_in, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(this,SignUpActivity.class));
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        progressBar.setVisibility(View.VISIBLE);
        ParseUser.logInInBackground(edtUsername.getText().toString(), edtPassword.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null && user != null) {
                        Toast.makeText(LogInActivity.this, "Log in successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LogInActivity.this,AllUserActivity.class));
                        finish();
                    }
                    else
                        Toast.makeText(LogInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(!edtUsername.getText().toString().isEmpty()&&!edtPassword.getText().toString().isEmpty()&&!edtEmail.getText().toString().isEmpty())
            btnLogIn.setEnabled(true);
        else
            btnLogIn.setEnabled(false);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
