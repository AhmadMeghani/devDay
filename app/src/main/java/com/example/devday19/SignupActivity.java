package com.example.devday19;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference Data;
    private Toolbar mToolbar;
    private EditText RegisterUserName;
    private EditText RegisterUserEmail;
    private EditText RegisterUserPassword;
    private Button Signup;
    private TextView login;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.signuptoolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Sign Up");

        progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        progressBar.setVisibility(View.INVISIBLE);

        RegisterUserName = (EditText) findViewById(R.id.register_name);
        RegisterUserEmail = (EditText) findViewById(R.id.register_email);
        RegisterUserPassword = (EditText) findViewById(R.id.register_password);

        Signup = (Button) findViewById(R.id.signup_button);
        login = (TextView) findViewById(R.id.login_txt);

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = RegisterUserName.getText().toString();
                final String email = RegisterUserEmail.getText().toString();
                final  String password = RegisterUserPassword.getText().toString();
                
                SignUp(username,email,password);
            }
        });



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setClickable(true);
                Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void SignUp(final String username, final String email, String password) {

      if(TextUtils.isEmpty(username)){
          RegisterUserName.setError("Required Field...");
          return;
      }
      if (TextUtils.isEmpty(email)){
          RegisterUserEmail.setError("Required Field...");
          return;
      }
      if(TextUtils.isEmpty(password)){
          RegisterUserPassword.setError("Required Field...");
          return;
      }
      else{
          progressBar.setVisibility(View.VISIBLE);

          mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {

                  if(task.isSuccessful()){
                      String currentUserId = mAuth.getCurrentUser().getUid();
                      mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
                      mDatabaseReference.child("user_name").setValue(username);
                      mDatabaseReference.child("user_email").setValue(email);
                      mDatabaseReference.child("user_image").setValue("Default Profile").addOnCompleteListener(new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull Task<Void> task) {

                              startActivity(new Intent(SignupActivity.this,MainActivity.class));
                              finish();

                          }
                      });
                  }

                  else {
                      Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                  }


                  progressBar.setVisibility(View.INVISIBLE);
              }
          });
      }
    }
}
