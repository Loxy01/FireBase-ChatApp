package com.loxy01.firebasechatapp.userpackage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loxy01.firebasechatapp.R;
import com.loxy01.firebasechatapp.auth.startActivity;
import com.loxy01.firebasechatapp.chat.ChatActivity;

import java.util.ArrayList;
import java.util.Objects;

public class UserListActivity extends AppCompatActivity {

    /** This code was written by Loxy01.
     *  Anyone can use the entire functionality of the application
     */

    FirebaseAuth mAuth;
    FirebaseUser fUser;
    String userToChat;
    FirebaseDatabase db;
    DatabaseReference usersDbReference;
    ChildEventListener usersChildEventListener;

    protected ArrayList<user> userArrayList;
    protected RecyclerView userRecyclerView;
    protected userListAdapter userAdapter;
    protected RecyclerView.LayoutManager userLayoutManager;

    // Utils
    int backButtonClicks;
    String imageIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        backButtonClicks = 0;

        userArrayList = new ArrayList<>();
        userRecyclerView = findViewById(R.id.userRecyclerView);
        userRecyclerView.setHasFixedSize(true);

        attachUserNameDataBaseReference();
        mAuth = FirebaseAuth.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void attachUserNameDataBaseReference() {
        db = FirebaseDatabase.getInstance();
        usersDbReference = db.getReference().child("users");
        if(usersChildEventListener==null){
            usersChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    user user = snapshot.getValue(com.loxy01.firebasechatapp.userpackage.user.class);
                    //Exclude the current user
                    if(!Objects.equals(user.getUserId(), mAuth.getCurrentUser().getUid())){
                        user.getUserName();
                        userToChat = user.getUserName();
                        userArrayList.add(user);
                    }
                    userLayoutManager = new LinearLayoutManager(UserListActivity.this);
                    userAdapter = new userListAdapter(userArrayList);
                    userAdapter.notifyDataSetChanged();

                    userRecyclerView.setLayoutManager(userLayoutManager);
                    userRecyclerView.setAdapter(userAdapter);
                    userRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

                    userAdapter.setOnUserClickListener(new userListAdapter.OnUserClickListener() {
                        @Override
                        public void onUserClick(int position) {
                            goToChat(position);
                        }
                    });
                }
                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            };
        }
        usersDbReference.addChildEventListener(usersChildEventListener);
    }

    private void goToChat(int position) {
        Intent i = new Intent(UserListActivity.this, ChatActivity.class);
        i.putExtra("recipientUserId", userArrayList.get(position).getUserId());
        if(userToChat!=null){
            i.putExtra("UserName", userArrayList.get(position).userName);
        }
        startActivity(i);
    }

    public void onBackPressed() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        } else {
            Log.e("errors", "onBackPressed error");
        }
        backButtonClicks++;
        if(backButtonClicks == 3){
            Toast.makeText(this, "Going backwards is prohibited!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.avatar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.SignOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserListActivity.this, startActivity.class));
            case R.id.Profile:
                startActivity(new Intent(UserListActivity.this, profileActivity.class));
            default: return super.onOptionsItemSelected(item);
        }
    }
}