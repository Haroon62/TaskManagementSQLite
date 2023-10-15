package com.example.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Models.TaskModel;
import com.example.taskmanagementsqlite.R;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.Holderview> {

    ArrayList<TaskModel> list;
    Context context;
    OnUpdateClickListener onUpdateClickListener;
    OnLongPressListener onLongPressListener;
    public interface OnUpdateClickListener {
        void onUpdateClick(int position);
    }

    public interface OnLongPressListener {
        void onLongPress(int position);
    }

    public TaskAdapter(ArrayList<TaskModel> list, Context context,OnUpdateClickListener onUpdateClickListener,  OnLongPressListener onLongPressListener) {
        this.list = list;
        this.context = context;
        this.onUpdateClickListener = onUpdateClickListener;
        this.onLongPressListener = onLongPressListener;
    }

    @NonNull
    @Override
    public Holderview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.taskshow, parent, false);
        return new Holderview(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holderview holder, @SuppressLint("RecyclerView") int position) {
        TaskModel modelClass=list.get(position);
        holder.nameea.setText(modelClass.getName());
        holder.desc.setText(modelClass.getDescription());
        holder.daed.setText(modelClass.getDeadline());
        holder.priorityname.setText(modelClass.getPeriorityname());
        holder.categoryname.setText(modelClass.getCategorname());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onUpdateClickListener != null) {
                    onUpdateClickListener.onUpdateClick(position);
                }
            }
        });
        holder.delete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onLongPressListener != null) {
                    onLongPressListener.onLongPress(position);
                    Log.d("hdsjs", "pressed ");
                }
                return true; // Consume the long-press event
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holderview extends RecyclerView.ViewHolder {

        TextView nameea,desc,daed,priorityname,categoryname;
        ImageView imageView;
        RelativeLayout delete;
        public Holderview(@NonNull View itemView) {
            super(itemView);
            nameea=itemView.findViewById(R.id.namee);
            desc=itemView.findViewById(R.id.description);
            daed=itemView.findViewById(R.id.deadline);
            priorityname=itemView.findViewById(R.id.priortyname);
            categoryname=itemView.findViewById(R.id.categoryname);
            imageView=itemView.findViewById(R.id.upadte);
            delete=itemView.findViewById(R.id.delete);
        }
    }
}
