package edu.neu.madcourse.tasket;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;

public class TeamExpandableAdaptor extends ExpandableRecyclerAdapter<TeamParentViewHolder, TeamChildViewHolder> {
    private static final String TAG = TeamExpandableAdaptor.class.getSimpleName();
    private LayoutInflater mInflater;


    public TeamExpandableAdaptor(Context ctx, List<ParentObject> parents) {
        super(ctx, parents);
        mInflater = LayoutInflater.from(ctx);

    }

    @Override
    public TeamParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.list_item_team_parent, viewGroup, false);
        return new TeamParentViewHolder(view);
    }

    @Override
    public TeamChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        int numChildren = viewGroup.getChildCount();

        Log.i(TAG, "CHILDREN =========== " + Integer.valueOf(numChildren).toString());
        View view = mInflater.inflate(R.layout.list_item_team_child, viewGroup, false);

        return new TeamChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(TeamParentViewHolder teamParentViewHolder, int i, Object o) {
        Team team = (Team) o;
        teamParentViewHolder.teamTitleTextView.setText(team.getTeamName());
    }

    @Override
    public void onBindChildViewHolder(TeamChildViewHolder teamChildViewHolder, int i, Object o) {
        Subteam subteam = (Subteam) o;
        teamChildViewHolder.teamSubteamText.setText(subteam.getSubteamName());
    }
}
