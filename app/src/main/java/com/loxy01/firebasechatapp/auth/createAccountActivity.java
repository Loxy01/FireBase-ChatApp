package com.loxy01.firebasechatapp.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loxy01.firebasechatapp.R;
import com.loxy01.firebasechatapp.userpackage.UserListActivity;
import com.loxy01.firebasechatapp.userpackage.user;

import android.util.Patterns;

public class createAccountActivity extends AppCompatActivity {

    /** This code was written by Loxy01.
     *  Anyone can use the entire functionality of the application
     */

    EditText nameEditTxt;
    EditText emailEditTxt;
    EditText passwordEditTxt;
    TextView signIn_hyperlink;
    ImageButton continueRegistration;

    private FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference usersDbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        db = FirebaseDatabase.getInstance();
        usersDbReference = db.getReference().child("users");
        mAuth = FirebaseAuth.getInstance();

        nameEditTxt = findViewById(R.id.NameEditTxt);
        emailEditTxt = findViewById(R.id.EmailEditTxt);
        passwordEditTxt = findViewById(R.id.PasswordEditTxt);
        signIn_hyperlink = findViewById(R.id.sign_in_hyperlink);
        continueRegistration = findViewById(R.id.ContinueButton);

        nameEditTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    nameEditTxt.setCompoundDrawables(null,null,null,null);
                }else{
                    if(nameEditTxt.getText().length()==0){
                        nameEditTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.svg_username, 0, 0, 0);
                    }
                }
            }
        });
        emailEditTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    emailEditTxt.setCompoundDrawables(null,null,null,null);
                }else{
                    if(emailEditTxt.getText().length()==0){
                        emailEditTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.svg_email, 0, 0, 0);
                    }
                }
            }
        });
        passwordEditTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean hasFocus) {
                if(hasFocus){
                    passwordEditTxt.setCompoundDrawables(null, null, null, null);
                }
                else {
                    if(passwordEditTxt.getText().length()==0)
                        passwordEditTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.svg_password_hint, 0, 0, 0);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        
        continueRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if (!(nameEditTxt.getText().length()<1||emailEditTxt.getText().length()<1||passwordEditTxt.getText().length()<1)){
                    if(nameEditTxt.getText().length()<=1){
                        Toast.makeText(createAccountActivity.this, "Incorrect User Name!", Toast.LENGTH_SHORT).show();
                    }
                    //E-mail validate
                    else if(!(!emailEditTxt.getText().toString().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailEditTxt.getText().toString()).matches())){
                        Toast.makeText(createAccountActivity.this, "Incorrect email!", Toast.LENGTH_SHORT).show();
                    }
                    else if(passwordEditTxt.getText().length()<8){
                        Toast.makeText(createAccountActivity.this, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        signUpUser(emailEditTxt.getText().toString().trim(), passwordEditTxt.getText().toString().trim());
                    }
                }
                else {
                    Toast.makeText(createAccountActivity.this, "There are empty input fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        signIn_hyperlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccountActivity.super.onBackPressed();
            }
        });
    }

    public void signUpUser(String email,String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("createAccountActivity", "createUserWithEmail:success");
                    FirebaseUser mUser = mAuth.getCurrentUser();
                    createUser(mUser);
                    Intent i = new Intent(new Intent(createAccountActivity.this, UserListActivity.class));
                    i.putExtra("userName", mUser.getDisplayName());
                    startActivity(i);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("createAccountActivity", "createUserWithEmail:failure", task.getException());

                    mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            if(task.isSuccessful()){
                                SignInMethodQueryResult result = task.getResult();
                                if(result.getSignInMethods().size() > 0){
                                    Toast.makeText(createAccountActivity.this, "Email address is already created!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(createAccountActivity.this, "Email address is not registered", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void createUser(FirebaseUser mUser) {
        user user = new user();
        user.setUserId(mUser.getUid());
        user.setUserEmail(mUser.getEmail());
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(nameEditTxt.getText().toString())
                .build();
        mUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("createAccountActivity", "User profile updated.");
                        }
                    }
                });
        user.setUserName(nameEditTxt.getText().toString());

        usersDbReference.push().setValue(user);
    }
}