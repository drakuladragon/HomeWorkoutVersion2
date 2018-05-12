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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.nhtha.homeworkoutversion2.R;
import com.example.nhtha.homeworkoutversion2.dto.UserDto;
import com.example.nhtha.homeworkoutversion2.view.activity.StartActivity;
import com.example.nhtha.homeworkoutversion2.view.dialog.PasswordChangeDialog;
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
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by nhtha on 07-Mar-18.
 */

public class SettingFragment extends Fragment implements View.OnClickListener {

    private CircleImageView civAvatar;
    private EditText edtUsername;
    private Button btnSave;
    private View rootView;

    private Uri avatarUri = null;
    private StorageReference myStorageReference;
    private FirebaseAuth myFirebaseAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference userReference;
    private ProgressDialog myProgressDialog;

    private ImageButton imgPopupMenu;

    private ImageView imgMenu;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return rootView = inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        registerListener();
    }

    private void registerListener() {
        imgMenu = getView().findViewById(R.id.img_menu);

        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((StartActivity) getActivity()).openDrawer();
            }
        });
    }

    private void init() {

        myFirebaseAuth = FirebaseAuth.getInstance();
        userReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(myFirebaseAuth.getCurrentUser().getUid());
        myStorageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        civAvatar = rootView.findViewById(R.id.civ_avatar);
        edtUsername = rootView.findViewById(R.id.edt_username);
        btnSave = rootView.findViewById(R.id.btn_save);
        imgPopupMenu = rootView.findViewById(R.id.img_popup_menu);

        btnSave.setOnClickListener(this);
        civAvatar.setOnClickListener(this);
        imgPopupMenu.setOnClickListener(this);

        myProgressDialog = new ProgressDialog(getContext());

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    edtUsername.setText(dataSnapshot.child("name").getValue().toString());
                    Picasso.with(getContext()).load(dataSnapshot.child("avatar").getValue().toString()).into(civAvatar);
                } else {
                    edtUsername.setText(myFirebaseAuth.getCurrentUser().getEmail());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                save();
                break;

            case R.id.civ_avatar:
                pickAvatar();
                break;

            case R.id.img_popup_menu:
                ShowPopupMenu();
                break;

            default:
                break;

        }
    }

    private void ShowPopupMenu() {
        PopupMenu menu = new PopupMenu(getContext(),imgPopupMenu);
        menu.getMenuInflater().inflate(R.menu.menu,menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                PasswordChangeDialog passwordChangeDialog = new PasswordChangeDialog(getContext());
                passwordChangeDialog.show();
                return false;
            }
        });
        menu.show();
    }

    private void pickAvatar() {
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

    private void pickImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(getActivity());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == ((StartActivity) getActivity()).RESULT_OK) {

                avatarUri = result.getUri();
                Log.d("ReSult", "onActivityResult: " + avatarUri);
                civAvatar.setImageURI(avatarUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void save() {
        final String username = edtUsername.getText().toString().trim();

        if (!TextUtils.isEmpty(username) && avatarUri != null) {
            myProgressDialog.setMessage("Saving ...");
            myProgressDialog.show();

            final String userID = myFirebaseAuth.getCurrentUser().getUid();

            StorageReference image_path = myStorageReference.child("profiles_image").child(userID + ".jpg");
            image_path.putFile(avatarUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if (task.isSuccessful()) {

                        Uri download_uri = task.getResult().getDownloadUrl();

                        UserDto userDto = new UserDto();
                        userDto.setName(username);
                        userDto.setAvatar(download_uri.toString());

                        databaseReference.child("users").child(userID).setValue(userDto);

//                        ((StartActivity) getActivity()).setUserAvatar(civAvatar.getDrawingCache());
//                        ((StartActivity) getActivity()).setTxtUserName(edtUsername.getText().toString().trim());

                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

                    }

                    myProgressDialog.dismiss();
                }
            });

        }

    }
}
