package com.example.nhtha.homeworkoutversion2.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nhtha.homeworkoutversion2.R;
import com.example.nhtha.homeworkoutversion2.model.Movement;
import com.example.nhtha.homeworkoutversion2.model.Post;
import com.example.nhtha.homeworkoutversion2.view.fragment.start.CommentFragment;
import com.example.nhtha.homeworkoutversion2.view.fragment.start.CompleteExerciseFragment;
import com.example.nhtha.homeworkoutversion2.view.fragment.start.DoingExerciseFragment;
import com.example.nhtha.homeworkoutversion2.view.fragment.start.ExerciseFragment;
import com.example.nhtha.homeworkoutversion2.view.fragment.start.HomePageFragment;
import com.example.nhtha.homeworkoutversion2.view.fragment.start.PostFragment;
import com.example.nhtha.homeworkoutversion2.view.fragment.start.PostOpenFragment;
import com.example.nhtha.homeworkoutversion2.view.fragment.start.ReminderFragment;
import com.example.nhtha.homeworkoutversion2.view.fragment.start.ReportFragment;
import com.example.nhtha.homeworkoutversion2.view.fragment.start.SettingFragment;
import com.example.nhtha.homeworkoutversion2.view.fragment.start.StartFragment;
import com.example.nhtha.homeworkoutversion2.view.fragment.start.WallFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StartActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private static final String STACK = "stack";
    private static final String TAG = "StartActivity";

    private ExerciseFragment exerciseFragment;
    private StartFragment startFragment;
    private DoingExerciseFragment doingExerciseFragment;
    private WallFragment wallFragment;
    private ReminderFragment reminderFragment;
    private ReportFragment reportFragment;
    private PostFragment postFragment;
    private SettingFragment settingFragment;
    private PostOpenFragment postOpenFragment;
    private CommentFragment commentFragment;
    private CompleteExerciseFragment completeExerciseFragment;
    private HomePageFragment homePageFragment;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View navHeader;
    private CircleImageView civUserAvatar;
    private TextView txtUserName;

    private FirebaseAuth myAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        init();
    }

    private void init() {

        myAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = myAuth.getCurrentUser();
        if (currentUser == null) {
            sendToLogin();
        }

        firebaseUser = myAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
        drawerLayout = findViewById(R.id.dl_info);

        navigationView = findViewById(R.id.nav_info);
        ViewGroup.LayoutParams layoutParams = navigationView.getLayoutParams();
        layoutParams.width = getWindowManager().getDefaultDisplay().getWidth() - getActionBarHeight();

        navigationView.setLayoutParams(layoutParams);
        navigationView.setNavigationItemSelectedListener(this);

        navHeader = navigationView.getHeaderView(0);
        civUserAvatar = navHeader.findViewById(R.id.civ_avatar);
        txtUserName = navHeader.findViewById(R.id.txt_user_name);

        exerciseFragment = new ExerciseFragment();
        startFragment = new StartFragment();
        wallFragment = new WallFragment();
        reminderFragment = new ReminderFragment();
        reportFragment = new ReportFragment();
        postFragment = new PostFragment();
        settingFragment = new SettingFragment();
        homePageFragment = new HomePageFragment();

        showStartFragment();
        setUserData();
    }

    private void setUserData() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    txtUserName.setText(dataSnapshot.child("name").getValue().toString());
                    Picasso.with(getBaseContext()).load(dataSnapshot.child("avatar").getValue().toString()).into(civUserAvatar);
                } else {
                    txtUserName.setText(firebaseUser.getEmail());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void showStartFragment() {

        unlockDrawer();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, startFragment)
                .addToBackStack(null)
                .commit();
    }


    public void showExerciseFragment(String exName, String exCode, int imageID) {
        lockDrawer();
        exerciseFragment.setExName(exName);
        exerciseFragment.setExCode(exCode);
        exerciseFragment.setExImageID(imageID);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                        R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.content_frame, exerciseFragment)
                .addToBackStack(null)
                .commit();

    }

    public void showDoingExerciseFragment(List<Movement> movementList, int itemCount) {
        lockDrawer();
        doingExerciseFragment = new DoingExerciseFragment(movementList);
        //  doingExerciseFragment.setMovementList(movementList);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                        R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.content_frame, doingExerciseFragment)
                .addToBackStack(null)
                .commit();
        Log.d(TAG, "showDoingExerciseFragment: " + getFragmentManager().getBackStackEntryCount());
    }

    public void showWallFragment() {
        unlockDrawer();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, wallFragment)
                .addToBackStack(null)
                .commit();
        Log.d(TAG, "showWallFragment: " + getFragmentManager().getBackStackEntryCount());
    }

    public void showReminderFragment() {
        unlockDrawer();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, reminderFragment)
                .addToBackStack(null)
                .commit();
    }

    public void showReportFragment() {
        unlockDrawer();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, reportFragment)
                .addToBackStack(null)
                .commit();
    }

    public void showPostFragment() {
        lockDrawer();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                        R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.content_frame, postFragment)
                .addToBackStack(null)
                .commit();
    }

    public void showSettingFragment() {
        unlockDrawer();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, settingFragment)
                .addToBackStack(null)
                .commit();
    }

    public void showPostOpenFragment(Post post){
        lockDrawer();
        postOpenFragment = new PostOpenFragment(post);
        postOpenFragment.notifyDataChanged();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                .replace(R.id.content_frame, postOpenFragment)
                .addToBackStack(null)
                .commit();
    }

    public void showAddCommentFragment(String postID){
        lockDrawer();
        commentFragment = new CommentFragment(postID);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.exit_slide_out,R.anim.enter_slide_up)
                .replace(R.id.content_frame, commentFragment)
                .addToBackStack(null)
                .commit();
    }

    public void showCompleteExFragment(int kcal){
        lockDrawer();
        completeExerciseFragment = new CompleteExerciseFragment(kcal);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.exit_slide_out,R.anim.enter_slide_up)
                .replace(R.id.content_frame, completeExerciseFragment)
                .addToBackStack(null)
                .commit();
    }

    public void showHomePageFragment(){
        unlockDrawer();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, homePageFragment)
                .addToBackStack(null)
                .commit();
    }

    public void lockDrawer(){
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void unlockDrawer(){
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public void openDrawer() {
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        Log.d(TAG, "onBackPressed: " + count);

        if (count == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    private int getActionBarHeight() {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());

        if (actionBarHeight == 0 && getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_plan:
                showStartFragment();
                drawerLayout.closeDrawer(Gravity.LEFT);
                break;

            case R.id.nav_report:
                showReportFragment();
                drawerLayout.closeDrawer(Gravity.LEFT);
                break;

            case R.id.nav_remind:
                showReminderFragment();
                drawerLayout.closeDrawer(Gravity.LEFT);
                break;

            case R.id.nav_wall:
                showWallFragment();
                drawerLayout.closeDrawer(Gravity.LEFT);
                break;

            case R.id.nav_sign_out:
                logout();
                break;

            case R.id.nav_setting:
                showSettingFragment();
                drawerLayout.closeDrawer(Gravity.LEFT);
                break;

            case R.id.nav_home:
                showHomePageFragment();
                drawerLayout.closeDrawer(Gravity.LEFT);
                break;

            default:
                break;
        }
        item.setCheckable(true);

        return true;
    }

    private void logout() {
        myAuth.signOut();
        sendToLogin();
    }

    private void sendToLogin() {
        Intent intent = new Intent(StartActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = myAuth.getCurrentUser();
        if (currentUser == null) {
            sendToLogin();
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        settingFragment.onActivityResult(requestCode, resultCode, data);
        postFragment.onActivityResult(requestCode, resultCode, data);
    }

    public void setUserAvatar(Bitmap bitmap) {
        civUserAvatar.setImageBitmap(bitmap);
    }

    public void setTxtUserName(String name) {
        txtUserName.setText(name);
    }
}
