package edu.neu.madcourse.tasket;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

public class TeamChildViewHolder extends ChildViewHolder {

    public TextView teamSubteamText;

    public TeamChildViewHolder(View itemView) {
        super(itemView);

        teamSubteamText = itemView.findViewById(R.id.child_list_item_team_text_view);
    }

}
