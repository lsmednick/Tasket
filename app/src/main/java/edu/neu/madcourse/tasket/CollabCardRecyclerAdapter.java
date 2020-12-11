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

public class CollabCardRecyclerAdapter extends RecyclerView.Adapter<CollabCardRecyclerAdapter.MyViewHolder> {
    private ArrayList<String> namesList;
    private ArrayList<String> imgList;
    private ArrayList<String> emailList;
    private Context myContext;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, email;
        public String id;
        public ImageView image, clicky;
        public MyViewHolder(View item) {
            super(item);
            name = item.findViewById(R.id.collabName);
            email = item.findViewById(R.id.collabMail);
            image = item.findViewById(R.id.collabPic);
            //clicky = item.findViewById(R.id.clickyclik);
        }
    }

    public CollabCardRecyclerAdapter(Context mContext, ArrayList<String> names,
                                     ArrayList<String> imgs, ArrayList<String> emails) {
        setHasStableIds(true);
        namesList = names;
        imgList = imgs;
        emailList = emails;
        myContext = mContext;
    }

    @NonNull
    @Override
    public CollabCardRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collaborator_card, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CollabCardRecyclerAdapter.MyViewHolder holder, int position) {
        if (!imgList.get(position).equals("")) {
            Picasso.get().load(imgList.get(position)).fit().into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.tasket_logo);
        }
        holder.name.setText(namesList.get(position));
        holder.email.setText(emailList.get(position));
    }

    @Override
    public int getItemCount() {
        return namesList.size();
    }
}
