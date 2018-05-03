package com.example.nhtha.homeworkoutversion2.view.fragment.start;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.nhtha.homeworkoutversion2.R;
import com.example.nhtha.homeworkoutversion2.model.Remin;
import com.example.nhtha.homeworkoutversion2.presenter.CRUD;
import com.example.nhtha.homeworkoutversion2.view.activity.StartActivity;
import com.example.nhtha.homeworkoutversion2.view.adapter.ReminderAdapter;
import com.example.nhtha.homeworkoutversion2.view.dialog.RemiderDialog;

import io.realm.RealmResults;

public class ReminderFragment extends Fragment implements View.OnClickListener, RemiderDialog.OnRemiderDialogClick, ReminderAdapter.OnDiscarIconCLick {

    private ReminderAdapter reminAdapter;
    private FloatingActionButton fabAddRemider;
    private RecyclerView rcvReminder;
    private RemiderDialog remiderDialog;
    private Context context;
    private ImageView imgMenu;

    private CRUD crud;

    private RealmResults<Remin> remins;

    private int hour;
    private int minute;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reminder, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        crud = new CRUD(getContext());

        rcvReminder = getView().findViewById(R.id.rcv_reminder);
        fabAddRemider = getView().findViewById(R.id.fab_add_reminder);

        reminAdapter = new ReminderAdapter(getContext(),remins);
        reminAdapter.setOnDiscarIconCLick(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvReminder.setLayoutManager(linearLayoutManager);
        rcvReminder.setAdapter(reminAdapter);
        fabAddRemider.setOnClickListener(this);

        loadData();


        imgMenu = getView().findViewById(R.id.img_menu);

        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((StartActivity) getActivity()).openDrawer();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add_reminder:
                remiderDialog = new RemiderDialog(getContext());
                remiderDialog.setOnClick(this);
                remiderDialog.show();
                break;

            default:
                break;
        }
    }

    @Override
    public void onButtonNextClick(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
        remiderDialog.showDateRepeat(View.VISIBLE);
        remiderDialog.showTimePicker(View.GONE);
    }

    @Override
    public void onButtonOkClick(boolean[] date) {

        String[] days = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        String day = "";
        for (int i = 0; i < date.length; i++) {
            if (date[i] != false) {
                if (i != date.length - 1) {
                    day += days[i] + ",";
                } else {
                    day += days[i];
                }
            }
        }
        
        Log.d("ReminderFragment", "onButtonOkClick: " + date.toString());
        Remin remin = new Remin();
        remin.setDate(day);
        if (minute < 10){
            remin.setTime(hour + ":0" + minute);
        } else {
            remin.setTime(hour + ":" + minute);
        }

        remin.setChecked(0);
        crud.insert(remin);
        loadData();

        remiderDialog.showDateRepeat(View.GONE);
        remiderDialog.showTimePicker(View.VISIBLE);
        remiderDialog.dismiss();


    }

    private void loadData(){
        remins = crud.getAll();
        if (remins != null){
            reminAdapter.notifyDataSetChanged(remins);
        }
    }

    @Override
    public void onButtonCancelOneClick() {
        remiderDialog.dismiss();
    }

    @Override
    public void onButtonCancelTwoClick() {
        remiderDialog.dismiss();
    }

    @Override
    public void onDiscardIconClicked(int position) {
        crud.delete(remins.get(position));
        reminAdapter.notifyDataSetChanged(remins);
    }
}
