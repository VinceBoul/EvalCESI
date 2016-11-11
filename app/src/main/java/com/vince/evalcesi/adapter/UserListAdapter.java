package com.vince.evalcesi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.vince.evalcesi.R;
import com.vince.evalcesi.model.User;

import java.util.List;

/**
 * Created by Vince on 07/11/2016.
 */

public class UserListAdapter extends BaseAdapter {

    Context context;
    List<String> data;
    private static LayoutInflater inflater = null;

    public UserListAdapter(Context context, List<String> data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<String> users){
        if (users.size() > 0){
            this.data = users;
        }else{
            System.out.println(users.toString());
        }
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        RecyclerView.ViewHolder vh;
        View vi = convertView;
        if (vi == null){
            vi = inflater.inflate(R.layout.user_row, null);
        }

        TextView usernameText = (TextView) vi.findViewById(R.id.username);
        TextView dateText = (TextView) vi.findViewById(R.id.userdate);

        // On créé un objet utilisateur à partir du JSON
        User user = new User(this.data.get(position));

        usernameText.setText(user.getUsername());
        dateText.setText(user.getDate().toString());
        return vi;
    }


}