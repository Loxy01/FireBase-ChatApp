package com.loxy01.firebasechatapp.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.loxy01.firebasechatapp.R;
import com.loxy01.firebasechatapp.userpackage.UserListActivity;

public class signInActivity extends AppCompatActivity {

    /** This code was written by Loxy01.
     *  Anyone can use the entire functionality of the application
     */

    EditText forgotPassword;
    EditText emailEditTxt;
    EditText passwordEditTxt;
    ImageButton continueButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();

        forgotPassword = findViewById(R.id.ForgotPassword);
        emailEditTxt = findViewById(R.id.EmailEditTxt);
        passwordEditTxt = findViewById(R.id.PasswordEditTxt);
        continueButton = findViewById(R.id.ContinueButton);

        emailEditTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    emailEditTxt.setCompoundDrawables(null, null, null, null);
                }
                else {
                    if(emailEditTxt.getText().length()==0)
                        emailEditTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.svg_email, 0, 0, 0);
                }
            }
        });
        passwordEditTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    passwordEditTxt.setCompoundDrawables(null, null, null, null);
                }
                else {
                    if(passwordEditTxt.getText().length()==0)
                        passwordEditTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.svg_password_hint, 0, 0, 0);
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder resetPasswordDialog = new AlertDialog.Builder(signInActivity.this);
                resetPasswordDialog
                        .setTitle("Do you want to reset your password?")
                        .setMessage("A link to reset your password will be sent to your account's email")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(new Intent(signInActivity.this, forgotPasswordActivity.class));
                    }
                }).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(emailEditTxt.getText().length()<1||passwordEditTxt.getText().length()<1)){
                    signInUser(emailEditTxt.getText().toString(), passwordEditTxt.getText().toString());
                }else{
                    Toast.makeText(signInActivity.this, "There are empty input fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void signInUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("signInActivity", "signInWithEmail:success");
                    Intent i = new Intent(signInActivity.this, UserListActivity.class);
                    i.putExtra("userName", mAuth.getCurrentUser().getDisplayName());
                    startActivity(i);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("signInActivity", "signInWithEmail:failure", task.getException());
                    Toast.makeText(signInActivity.this, "The password or email address is incorrect",
                            Toast.LENGTH_SHORT).show();
                    //updateUI(null);
                }
            }
        });
    }

}