package edu.neu.madcourse.tasket;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PermissionsCustomAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Permission> permissions;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public EditText permission_title;
        public SwitchCompat add_member_sw;
        public SwitchCompat rem_member_sw;
        public SwitchCompat add_file_sw;
        public SwitchCompat del_file_sw;
        public RecyclerView members_list_view;
        public TextView add_members_text;
        public ArrayList<SwitchCompat> switches;

        public MyViewHolder(View view) {
            super(view);
            permission_title = view.findViewById(R.id.permissions_edit_text);
            add_member_sw = view.findViewById(R.id.add_members_switch);
            rem_member_sw = view.findViewById(R.id.remove_members_switch);
            add_file_sw = view.findViewById(R.id.add_files_switch);
            del_file_sw = view.findViewById(R.id.delete_files_switch);
            members_list_view = view.findViewById(R.id.members_list_recyclerview);
            add_members_text = view.findViewById(R.id.add_members_text_view_button);
            switches = new ArrayList<>();
            switches.add(add_member_sw);
            switches.add(rem_member_sw);
            switches.add(add_file_sw);
            switches.add(del_file_sw);
        }
    }

    public PermissionsCustomAdapter(Context context, ArrayList<Permission> list) {
        this.context = context;
        this.permissions = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.permissions_row_card, viewGroup, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder myViewHolder, int i) {
        Permission myPermission = permissions.get(i);

        //set permission title
        ((MyViewHolder) myViewHolder).permission_title.setText(myPermission.getPermissionName());

        //set switches
        ArrayList<Boolean> settings = myPermission.getPermissionSettings();
        ArrayList<SwitchCompat> switches = ((MyViewHolder) myViewHolder).switches;
        for (int j = 0; j < myPermission.getPermissionSettings().size(); j++) {
            switches.get(j).setChecked(settings.get(j));
            Log.i("PCA", Integer.valueOf(j).toString() + settings.get(j).toString());
        }

        //set members
        RecyclerView recyclerView = ((MyViewHolder) myViewHolder).members_list_view;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        BasicStringCustomAdapter adapter = new BasicStringCustomAdapter(myPermission.getAssociated_members());  //might need context
        recyclerView.setAdapter(adapter);


        //set onclick


    }

    @Override
    public int getItemCount() {
        return permissions.size();
    }

}
