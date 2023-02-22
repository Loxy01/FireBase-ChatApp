package com.loxy01.firebasechatapp.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.loxy01.firebasechatapp.R;

import java.util.List;

public class forgotPasswordActivity extends AppCompatActivity {

    /** This code was written by Loxy01.
     *  Anyone can use the entire functionality of the application
     */

    EditText forgotPassword;
    ImageButton continueButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mAuth = FirebaseAuth.getInstance();

        forgotPassword = findViewById(R.id.ForgotEmailEditTxt);
        forgotPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    forgotPassword.setCompoundDrawables(null, null, null, null);
                }
                else {
                    if(forgotPassword.getText().length()==0)
                        forgotPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.svg_email, 0, 0, 0);
                }
            }
        });
        continueButton = findViewById(R.id.ContinueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(forgotPassword.getText().toString().isEmpty()) || Patterns.EMAIL_ADDRESS.matcher(forgotPassword.getText().toString()).matches()){
                    mAuth.fetchSignInMethodsForEmail(forgotPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            if (task.isSuccessful()) {
                                List<String> providers = task.getResult().getSignInMethods();
                                if (providers.size() > 0) {
                                    mAuth.sendPasswordResetEmail(forgotPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(forgotPasswordActivity.this, "Check your email", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(forgotPasswordActivity.this, "Email not found in database", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }else {
                    Toast.makeText(forgotPasswordActivity.this, "Incorrect E-mail", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}