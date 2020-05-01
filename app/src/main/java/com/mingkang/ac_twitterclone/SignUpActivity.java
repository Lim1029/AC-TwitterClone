//package com.mingkang.ac_twitterclone;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.parse.LogInCallback;
//import com.parse.ParseException;
//import com.parse.ParseInstallation;
//import com.parse.ParseUser;
//
//public class SignUpActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sign_up);
//
//    }
//}
package com.mingkang.ac_twitterclone;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.text.Editable;
        import android.text.TextWatcher;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.accessibility.AccessibilityManager;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.parse.LogInCallback;
        import com.parse.ParseException;
        import com.parse.ParseQuery;
        import com.parse.ParseUser;
        import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private Button btnSignUp;
    private EditText edtUsername, edtPassword, edtEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("");

        btnSignUp = findViewById(R.id.btnSignUp);
        edtUsername = findViewById(R.id.edtUsernameSignUp);
        edtEmail = findViewById(R.id.edtEmailSignUp);
        edtPassword = findViewById(R.id.edtPasswordSignUp);

        btnSignUp.setOnClickListener(this);
        edtPassword.addTextChangedListener(this);
        edtEmail.addTextChangedListener(this);
        edtUsername.addTextChangedListener(this);

        if(ParseUser.getCurrentUser()!=null) {
            startActivity(new Intent(SignUpActivity.this, AllUserActivity.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_in_sign_up, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(this,LogInActivity.class));
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
            ParseUser newUser = new ParseUser();
            newUser.setPassword(edtPassword.getText().toString());
            newUser.setUsername(edtUsername.getText().toString());
            newUser.setEmail(edtEmail.getText().toString());
            newUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null) {
                        Toast.makeText(SignUpActivity.this, "Account is created", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, AllUserActivity.class));
                        finish();
                    }
                    else
                        Toast.makeText(SignUpActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(!edtUsername.getText().toString().isEmpty()&&!edtPassword.getText().toString().isEmpty()&&!edtEmail.getText().toString().isEmpty())
            btnSignUp.setEnabled(true);
        else
            btnSignUp.setEnabled(false);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}

