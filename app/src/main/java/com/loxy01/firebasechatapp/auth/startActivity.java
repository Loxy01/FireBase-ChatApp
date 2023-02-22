package com.loxy01.firebasechatapp.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.loxy01.firebasechatapp.R;
import com.loxy01.firebasechatapp.userpackage.UserListActivity;

public class startActivity extends AppCompatActivity {

    /** This code was written by Loxy01.
     *  Anyone can use the entire functionality of the application
     */

    ImageButton buttonSignIn;
    ImageButton buttonCreateAccount;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(startActivity.this, UserListActivity.class));
        }

        buttonSignIn = findViewById(R.id.sign_button);
        buttonCreateAccount = findViewById(R.id.login_button);
    }
    @Override
    protected void onStart() {
        super.onStart();

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startSignInActivity = new Intent(startActivity.this, signInActivity.class);
                startActivity(startSignInActivity);
            }
        });
        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startCreateAccountActivity = new Intent(startActivity.this, createAccountActivity.class);
                startActivity(startCreateAccountActivity);
            }
        });
    }
}