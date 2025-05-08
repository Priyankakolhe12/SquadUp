package com.example.squadup;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TeamMemberAdapter extends ArrayAdapter<User> {

    private final Activity context;
    private final ArrayList<User> users;

    public TeamMemberAdapter(Activity context, ArrayList<User> users) {
        super(context, R.layout.item_team_member, users);
        this.context = context;
        this.users = users;
    }

    static class ViewHolder {
        TextView name, role;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.item_team_member, null, true);
            holder = new ViewHolder();
            holder.name = rowView.findViewById(R.id.tvName);
            holder.role = rowView.findViewById(R.id.tvRole);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        User user = users.get(position);
        holder.name.setText(user.getFullName());
        holder.role.setText(user.getRole());

        return rowView;
    }
}
