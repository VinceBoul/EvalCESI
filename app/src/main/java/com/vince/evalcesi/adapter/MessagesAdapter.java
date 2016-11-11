package com.vince.evalcesi.adapter;

/**
 * Created by Vince on 07/11/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.vince.evalcesi.R;
import com.vince.evalcesi.helper.DateHelper;
import com.vince.evalcesi.model.Message;
import com.vince.evalcesi.model.Note;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sca on 02/06/15.
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    private final Context context;

    public MessagesAdapter(Context ctx) {
        this.context = ctx;
    }

    List<Note> notes = new LinkedList<>();

    public void addMessage(List<Note> notes) {

        this.notes = Lists.reverse(notes);
        this.notifyDataSetChanged();
    }

    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, final int i) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View convertView = inflater.inflate(R.layout.item_message, parent, false);
        ViewHolder vh = new ViewHolder(convertView);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MessagesAdapter.ViewHolder vh, final int position) {
        vh.username.setText(notes.get(position).getUsername());
        vh.message.setText(notes.get(position).getMsg());
        try {
            vh.date.setText(DateHelper.getFormattedDate(notes.get(position).getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (notes == null) {
            return 0;
        }
        return notes.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView username;
        TextView message;
        TextView date;

        public ViewHolder(final View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.msg_user);
            message = (TextView) itemView.findViewById(R.id.msg_message);
            date = (TextView) itemView.findViewById(R.id.msg_date);
        }
    }
}