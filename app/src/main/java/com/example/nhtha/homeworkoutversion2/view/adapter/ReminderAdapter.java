package com.example.nhtha.homeworkoutversion2.view.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.nhtha.homeworkoutversion2.R;
import com.example.nhtha.homeworkoutversion2.model.Remin;
import com.example.nhtha.homeworkoutversion2.presenter.utils.ReminderUtils;
import com.example.nhtha.homeworkoutversion2.view.NotifiService;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by nhtha on 05-Mar-18.
 */

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminViewHolder>{

    private RealmResults<Remin> reminders;
    private Context context;
    private LayoutInflater inflater;
    private reportCallBackView callBackView;
    private Realm realm;

    public ReminderAdapter(Context context, RealmResults<Remin> reminders,Realm realm) {
        this.reminders = reminders;
        this.context = context;
        this.realm = realm;
        inflater = LayoutInflater.from(context);
    }

    public void notifyDataSetChanged( RealmResults<Remin> remin){
        reminders = remin;
        super.notifyDataSetChanged();
    }

    @Override
    public ReminViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReminViewHolder(inflater.inflate(R.layout.item_reminder,parent,false));
    }

    @Override
    public void onBindViewHolder(ReminViewHolder holder, final int position) {
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        final int min = ReminderUtils.getMinute(reminders.get(position).getTime());
        final int hour = ReminderUtils.getHour(reminders.get(position).getTime());
        final int id = position  + 1;
        final String date = reminders.get(position).getDate();
        holder.txtTime.setText(reminders.get(position).getTime());
        holder.swtRemind.setChecked(ReminderUtils.getChecked(reminders.get(position).getChecked()));
        holder.txtRepeat.setText(date);
        holder.imgDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               callBackView.onDiscardIconClicked(id - 1);
            }
        });
        holder.swtRemind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent myIntent = new Intent(context , NotifiService.class);
                myIntent.putExtra("id",id);
                myIntent.putExtra("date", date);
                PendingIntent pendingIntent = PendingIntent.getService(context, 1, myIntent, 0);

                if (isChecked){
                    realm.beginTransaction();
                    reminders.get(position).setChecked(Remin.CHECKED);
                    realm.commitTransaction();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.MINUTE, min);
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.SECOND,0);
                    alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                } else {
                    realm.beginTransaction();
                    reminders.get(position).setChecked(Remin.NOT_CHECKED);
                    realm.commitTransaction();
                    alarmManager.cancel(pendingIntent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        if (reminders != null){
            return reminders.size();
        }
        return 0;
    }

    public void setCallBackView(reportCallBackView callBackView) {
        this.callBackView = callBackView;
    }

    class ReminViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTime;
        private Switch swtRemind;
        private TextView txtRepeat;
        private ImageView imgDiscard;

        public ReminViewHolder(View itemView) {
            super(itemView);
            txtTime = itemView.findViewById(R.id.txt_time);
            swtRemind = itemView.findViewById(R.id.swt_reminder);
            txtRepeat = itemView.findViewById(R.id.txt_date_repeat);
            imgDiscard = itemView.findViewById(R.id.img_discard);
        }
    }

    public interface reportCallBackView {
        void onDiscardIconClicked(int position);
    }
}
