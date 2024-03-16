package com.thattechyguy.personalattendancemanager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class createAccount extends AppCompatActivity {

    private EditText register_email_edittext, register_password_edittext;
    private Button createAccountBtn;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        register_email_edittext = findViewById(R.id.register_email_edittext);
        register_password_edittext = findViewById(R.id.register_password_edittext);
        createAccountBtn = findViewById(R.id.createAccountBtn);

        mAuth = FirebaseAuth.getInstance();


        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = register_email_edittext.getText().toString();
                String password = register_password_edittext.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(createAccount.this, "Email or Password cannot be Empty!!!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.length() <= 6) {
                    Toast.makeText(createAccount.this, "Password should have atleast 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(createAccount.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(createAccount.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                                    Log.d("harsh", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
//                                    Toast.makeText(Login.this, user.getEmail(), Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(createAccount.this, homeBottomNav.class);
                                    startActivity(i);
                                    finish();
                                } else {

                                    // If sign in fails, display a message to the user.
                                    if (task.getException() != null) {
                                        String errorMessage = task.getException().getMessage();
                                        if (errorMessage != null) {
                                            // Check if the error message indicates that the user already exists
                                            if (errorMessage.contains("already in use")) {
                                                Toast.makeText(createAccount.this, "User already exists", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Log.d("harsh", errorMessage);
                                            }
                                        } else {
                                            Toast.makeText(createAccount.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(createAccount.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });

            }
        });
        register_password_edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    createAccountBtn.performClick();
                    return true;
                }
                return false;
            }
        });
    }
}