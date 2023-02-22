package com.loxy01.firebasechatapp.userpackage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.HashMap;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.loxy01.firebasechatapp.R;
import com.loxy01.firebasechatapp.chat.chatMessage;

public class profileActivity extends AppCompatActivity {

    /** This code was written by Loxy01.
     *  Anyone can use the entire functionality of the application
     */

    ImageView backgroundPhoto;
    ImageView accountPhoto;
    TextView name;
    chatMessage chatMessage;

    private static final int SELECT_PICTURE = 100;
    public static final String file = "save_background.txt";

    FirebaseAuth mAuth;
    private String backgroundUri = "";
    private String myUri = "";

    FirebaseStorage cloudStorage;
    StorageReference storageReference;
    FirebaseDatabase db;
    DatabaseReference photosDbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        cloudStorage = FirebaseStorage.getInstance();
        storageReference = cloudStorage.getReference().child(mAuth.getCurrentUser().getUid());

        db = FirebaseDatabase.getInstance();
        photosDbReference = db.getReference().child("photos");

        backgroundPhoto = findViewById(R.id.UserBackgroundImgProfile);
        accountPhoto = findViewById(R.id.UserPhotoProfile);

        name = findViewById(R.id.UserNameProfile);
        name.setText(mAuth.getCurrentUser().getDisplayName());
        chatMessage = new chatMessage();
    }

    @Override
    protected void onStart() {
        super.onStart();

        displayIcon();

        photosDbReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()&&snapshot.getChildrenCount()>0){
                    if(snapshot.hasChild("imageBackground")){
                        String imageIcon = snapshot.child("imageBackground").getValue().toString();
                        backgroundPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        Glide.with(backgroundPhoto.getContext())
                                .load(imageIcon)
                                .into(backgroundPhoto);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
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
                                user user = new user();
                                chatMessage.setName(mAuth.getCurrentUser().getDisplayName());

                                if(chatMessage.getIsIcon()){

                                    accountPhoto.setImageURI(selectedImageUri);
                                    myUri = downloadUri.toString();

                                    HashMap<String, Object> iconStringObjectHashMap = new HashMap<>();
                                    iconStringObjectHashMap.put("imageIcon", myUri);

                                    photosDbReference.child(mAuth.getCurrentUser().getUid()).updateChildren(iconStringObjectHashMap);
                                } else {
                                    backgroundUri = downloadUri.toString();
                                    chatMessage.setBackgroundIconAccount(downloadUri.toString());
                                    backgroundPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                    backgroundPhoto.setImageURI(selectedImageUri);

                                    HashMap<String, Object> iconStringObjectHashMap = new HashMap<>();
                                    iconStringObjectHashMap.put("imageBackground", backgroundUri);

                                    photosDbReference.child(mAuth.getCurrentUser().getUid()).updateChildren(iconStringObjectHashMap);
                                }
                            }
                        }
                    });
                }
            }
        }
    }

    public void accountPhotoClick(View view) {
        chatMessage.setIsIcon(true);
        imageChooser();
    }

    public void backgroundImgClick(View view) {
        chatMessage.setIsIcon(false);
        imageChooser();
    }

    private void displayIcon(){
        photosDbReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()&&snapshot.getChildrenCount()>0){
                    if(snapshot.hasChild("imageIcon")){
                        String imageIcon = snapshot.child("imageIcon").getValue().toString();
                        Glide.with(accountPhoto.getContext())
                                .load(imageIcon)
                                .into(accountPhoto);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // this function is triggered when
    // the Select Image Button is clicked
    private void imageChooser() {
        // create an instance of the
        // intent of the type image
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void hyperlinkToGitHub(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Loxy01"));
        startActivity(intent);
    }
}