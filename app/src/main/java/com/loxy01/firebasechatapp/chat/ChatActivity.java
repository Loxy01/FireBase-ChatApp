package com.loxy01.firebasechatapp.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.loxy01.firebasechatapp.R;
import com.loxy01.firebasechatapp.auth.startActivity;
import com.loxy01.firebasechatapp.userpackage.profileActivity;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    /** This code was written by Loxy01.
     *  Anyone can use the entire functionality of the application
     */

    ListView messageListView;
    chatMessageAdapter adapter;
    ProgressBar progressBar;
    Button sendButton;
    EditText messEditText;
    ImageButton sendImageButton;

    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;

    FirebaseDatabase db;
    DatabaseReference messDbReference;
    ChildEventListener messChildEventListener;

    FirebaseAuth mAuth;
    String userName;
    String recipientUserId;

    FirebaseStorage cloudStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        Intent getI = getIntent();
        if(getI!=null){
            userName = getI.getStringExtra("UserName");
            recipientUserId = getI.getStringExtra("recipientUserId");

            setTitle(userName);
        }

        db = FirebaseDatabase.getInstance();
        messDbReference = db.getReference().child("messages");

        cloudStorage = FirebaseStorage.getInstance();
        storageReference = cloudStorage.getReference().child("/chatImages");

        messageListView = findViewById(R.id.messageList);
        progressBar = findViewById(R.id.progressBar_Download);
        sendButton = findViewById(R.id.sendButtonID);
        sendImageButton = findViewById(R.id.sendPhotoImgView);
        messEditText = findViewById(R.id.editTextMessage);

        List<chatMessage> messageList = new ArrayList<>();
        adapter = new chatMessageAdapter(this, R.layout.your_message_item, messageList);
        messageListView.setAdapter(adapter);

        progressBar.setVisibility(ProgressBar.INVISIBLE);
        messEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1024)});

        messEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if(charSequence.toString().trim().length() > 0){
                    sendButton.setEnabled(true);
                } else {
                    sendButton.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        sendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatMessage chatMessage = new chatMessage();
                chatMessage.setText(messEditText.getText().toString());
                chatMessage.setName(mAuth.getCurrentUser().getDisplayName());
                chatMessage.setSender(mAuth.getCurrentUser().getUid());
                chatMessage.setRecipient(recipientUserId);
                chatMessage.setImageUrl(null);

                messDbReference.push().setValue(chatMessage);

                messEditText.setText("");
            }
        });

        messChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                chatMessage addMessInChat = snapshot.getValue(chatMessage.class);

                if(addMessInChat.getSender().equals(mAuth.getCurrentUser().getUid())&&addMessInChat.getRecipient().equals(recipientUserId))
                {
                    addMessInChat.setIsMine(true);
                    adapter.add(addMessInChat);
                    messageListView.smoothScrollToPosition(messageListView.getCount() - 1);
                } else if(addMessInChat.getRecipient().equals(mAuth.getCurrentUser().getUid())&&addMessInChat.getSender().equals(recipientUserId))
                {
                    addMessInChat.setIsMine(false);
                    adapter.add(addMessInChat);
                    messageListView.smoothScrollToPosition(messageListView.getCount() - 1);
                }

            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };

        messDbReference.addChildEventListener(messChildEventListener);
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
                startActivity(new Intent(ChatActivity.this, startActivity.class));
            case R.id.Profile:
                startActivity(new Intent(ChatActivity.this, profileActivity.class));
            default: return super.onOptionsItemSelected(item);
        }
    }

    // this function is triggered when
    // the Select Image Button is clicked
    void imageChooser() {
        // create an instance of the
        // intent of the type image
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the uri of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    final StorageReference imageReference = storageReference.child(selectedImageUri.getLastPathSegment());
                    UploadTask task = imageReference.putFile(selectedImageUri);
                    Task<Uri> urlTask = task.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            // Continue with the task to get the download URL
                            return imageReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                chatMessage chatMessage = new chatMessage();
                                chatMessage.setName(mAuth.getCurrentUser().getDisplayName());
                                chatMessage.setImageUrl(downloadUri.toString());
                                chatMessage.setSender(mAuth.getCurrentUser().getUid());
                                chatMessage.setRecipient(recipientUserId);

                                messDbReference.push().setValue(chatMessage);
                                messageListView.smoothScrollToPosition(messageListView.getCount() - 1);
                            }
                        }
                    });

                    //someImage.setImageURI(selectedImageUri);
                }
            }
        }
    }
}