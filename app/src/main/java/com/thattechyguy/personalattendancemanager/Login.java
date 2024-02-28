package com.thattechyguy.personalattendancemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText login_email_edittext, login_password_edittext;
    private Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        login_email_edittext = findViewById(R.id.login_email_edittext);
        login_password_edittext = findViewById(R.id.login_password_edittext);
        loginBtn = findViewById(R.id.loginBtn);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
//        if (user != null) {
//            Intent i = new Intent(this, homeBottomNav.class);
//        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = login_email_edittext.getText().toString();
                String password = login_password_edittext.getText().toString();

                if (email.isEmpty() || password.isEmpty()){
                    Toast.makeText(Login.this, "Email or Password cannot be Empty!!!", Toast.LENGTH_SHORT).show();
                    return;
                }else if (password.length()<=6){
                    Toast.makeText(Login.this, "Password should have atleast 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(Login.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                                    Log.d("harsh", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
//                                    Toast.makeText(Login.this, user.getEmail(), Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(Login.this, homeBottomNav.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.d("harsh", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(Login.this, "Incorrect email/password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
}