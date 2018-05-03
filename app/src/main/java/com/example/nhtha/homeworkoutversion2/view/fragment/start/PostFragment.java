package com.example.nhtha.homeworkoutversion2.view.fragment.start;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhtha.homeworkoutversion2.R;
import com.example.nhtha.homeworkoutversion2.dto.PostDto;
import com.example.nhtha.homeworkoutversion2.view.activity.StartActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Random;

/**
 * Created by nhtha on 07-Mar-18.
 */

public class PostFragment extends Fragment implements View.OnClickListener {
    public static final int REQUEST_GALLERY = 1;

    private ImageView imgPostImage;
    private EditText edtPostTitle;
    private EditText edtPostContent;
    private TextView btnPost;

    private FirebaseUser user;
    private StorageReference myStorageReference;
    private DatabaseReference postDatabaseReference;
    private DatabaseReference userReference;
    private ProgressDialog myProgressDialog;
    private Uri imageUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageUri = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        userReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        myStorageReference = FirebaseStorage.getInstance().getReference();
        postDatabaseReference = FirebaseDatabase.getInstance().getReference().child("post");
        myProgressDialog = new ProgressDialog(getContext());

        imgPostImage = getView().findViewById(R.id.img_post_image);
        edtPostTitle = getView().findViewById(R.id.edt_post_title);
        edtPostContent = getView().findViewById(R.id.edt_post_content);
        btnPost = getView().findViewById(R.id.txt_post);

        imgPostImage.setOnClickListener(this);
        btnPost.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_post:
                postBlog();
                break;

            case R.id.img_post_image:
                getPostImageFromGallery();
                break;

            default:
                break;
        }
    }

    private void getPostImageFromGallery() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);

            } else {

                pickImage();

            }

        } else {

            pickImage();

        }

    }

    private void pickImage(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    private void postBlog() {

        final String title = edtPostTitle.getText().toString().trim();
        final String content = edtPostContent.getText().toString().trim();

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content) && imageUri != null){

            myProgressDialog.setMessage("Posting ...");
            myProgressDialog.show();

            StorageReference filePath = myStorageReference.child("Post_Images").
                    child(random());

            filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
                        //get link sau khi upload thanh cong
                        final Uri downloadUri = task.getResult().getDownloadUrl();

                        //post len database firebase

                        userReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                DatabaseReference newPost = postDatabaseReference.push();

                                PostDto postDto = new PostDto();
                                postDto.setAuthorId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                postDto.setImageURL(downloadUri.toString());
                                postDto.setContent(content);
                                postDto.setTitle(title);
                                postDto.setUserName(dataSnapshot.child("name").getValue().toString());
                                postDto.setUserAvatar(dataSnapshot.child("avatar").getValue().toString());

                                newPost.setValue(postDto);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(getContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });

                        // quay về màn hình post
                        ((StartActivity) getActivity()).showWallFragment();

                        // tắt progress dialog
                        myProgressDialog.dismiss();

                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(getContext(), error,Toast.LENGTH_SHORT).show();;
                    }
                }
            });
        } else if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content) && imageUri == null){

            myProgressDialog.setMessage("Posting ...");
            myProgressDialog.show();

            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DatabaseReference newPost = postDatabaseReference.push();

                    PostDto postDto = new PostDto();
                    postDto.setAuthorId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    postDto.setContent(content);
                    postDto.setTitle(title);
                    postDto.setUserName(dataSnapshot.child("name").getValue().toString());
                    postDto.setUserAvatar(dataSnapshot.child("avatar").getValue().toString());

                    newPost.setValue(postDto);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

            // quay về màn hình post
            ((StartActivity) getActivity()).showWallFragment();

            // tắt progress dialog
            myProgressDialog.dismiss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_GALLERY:
                if (resultCode == getActivity().RESULT_OK) {
                    imageUri = data.getData();
                    imgPostImage.setImageURI(imageUri);
                }
                break;

            default:
                break;
        }

    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(13);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
