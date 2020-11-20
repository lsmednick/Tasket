package edu.neu.madcourse.tasket;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

public class TeamParentViewHolder extends ParentViewHolder {
    public TextView teamTitleTextView;
    public ImageButton parentDropDownArrow;

    public TeamParentViewHolder(View itemView) {
        super(itemView);

        teamTitleTextView = itemView.findViewById(R.id.parent_list_item_team_text_view);
        parentDropDownArrow = itemView.findViewById(R.id.parent_list_item_expand_arrow);

    }
}
