package com.example.nhtha.homeworkoutversion2.view.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nhtha.homeworkoutversion2.callback.OnClick;
import com.example.nhtha.homeworkoutversion2.R;
import com.example.nhtha.homeworkoutversion2.model.Movement;

import java.util.List;

/**
 * Created by nhtha on 16-Jan-18.
 */

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<Movement> exercise;
    private OnClick onClick;

    public ExerciseAdapter(Context context, List<Movement> exercise) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.exercise = exercise;
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_exercise,parent,false);
        ExerciseViewHolder viewHolder = new ExerciseViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ExerciseViewHolder holder, int position) {
        String mvName = exercise.get(position).getExName();
        String mvTurn = exercise.get(position).getExTurn();
        AnimationDrawable drawable = exercise.get(position).getMovementAnimation();

        holder.txtExName.setText(mvName);
        holder.txtExTurn.setText(mvTurn);
        holder.imgIntro.setBackground(drawable);
        drawable.start();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemCLick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return exercise.size();
    }

    class ExerciseViewHolder extends RecyclerView.ViewHolder {

        private TextView txtExName;
        private TextView txtExTurn;
        private ImageView imgIntro;

        public ExerciseViewHolder(View itemView) {
            super(itemView);
            txtExName = itemView.findViewById(R.id.txt_ex_name);
            txtExTurn = itemView.findViewById(R.id.txt_ex_turn);
            imgIntro = itemView.findViewById(R.id.img_ex_intro);
        }
    }

    public Movement getItem(int position){
        return exercise.get(position);
    }

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

}
