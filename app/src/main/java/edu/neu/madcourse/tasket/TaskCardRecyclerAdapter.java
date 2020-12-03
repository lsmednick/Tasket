package edu.neu.madcourse.tasket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TaskCardRecyclerAdapter extends RecyclerView.Adapter<TaskCardRecyclerAdapter.MyViewHolder> {
    private ArrayList<String> namesList;
    private ArrayList<String> deadlinesList;
    private ArrayList<String> imageList;
    private ArrayList<String> categoryList;
    private ArrayList<String> priorityList;
    private ArrayList<String> typeList;
    private Context myContext;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, deadline, category, priority, type;
        public ImageView image;
        public MyViewHolder(View item) {
            super(item);
            name = item.findViewById(R.id.cardName);
            deadline = item.findViewById(R.id.cardDeadline);
            image = item.findViewById(R.id.cardImage);
            category = item.findViewById(R.id.cardCategory);
            priority = item.findViewById(R.id.cardPriority);
            type = item.findViewById(R.id.cardType);
        }
    }

    public TaskCardRecyclerAdapter(Context mContext, ArrayList<String> names, ArrayList<String> deadlines,
                                   ArrayList<String> images, ArrayList<String> categories,
                                   ArrayList<String> priorities, ArrayList<String> types) {
        setHasStableIds(true);
        namesList = names;
        deadlinesList = deadlines;
        imageList = images;
        categoryList = categories;
        priorityList = priorities;
        typeList = types;
        myContext = mContext;
    }

    @NonNull
    @Override
    public TaskCardRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_card, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (!imageList.get(position).equals("")) {
            Picasso.get().load(imageList.get(position)).fit().into(holder.image);
        }
        holder.name.setText(namesList.get(position));
        holder.deadline.setText(deadlinesList.get(position));
        holder.category.setText(categoryList.get(position));
        holder.priority.setText(priorityList.get(position));
        holder.category.setText(categoryList.get(position));
        holder.type.setText(typeList.get(position));
    }

    @Override
    public int getItemCount() {
        return namesList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
