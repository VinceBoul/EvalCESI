package com.vince.evalcesi.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.vince.evalcesi.R;

import com.vince.evalcesi.helper.DateHelper;
import com.vince.evalcesi.model.Note;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Vince on 11/11/2016.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private final Context context;

    public NotesAdapter(Context ctx) {
        this.context = ctx;
    }

    List<Note> notes = new LinkedList<>();

    public void addNotes(List<Note> notes) {

        this.notes = Lists.reverse(notes);
        this.notifyDataSetChanged();
    }

    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, final int i) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View convertView = inflater.inflate(R.layout.item_message, parent, false);
        ViewHolder vh = new ViewHolder(convertView);
        return vh;
    }

    @Override
    public void onBindViewHolder(final NotesAdapter.ViewHolder vh, final int position) {
        vh.username.setText(notes.get(position).getUsername());
        vh.note.setText(notes.get(position).getNote());
        vh.check_done.setChecked(notes.get(position).getDone());

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
        TextView username, note, date, note_id;
        CheckBox check_done;

        public ViewHolder(final View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.msg_user);
            note = (TextView) itemView.findViewById(R.id.msg_message);
            check_done = (CheckBox) itemView.findViewById(R.id.check_done);
            note_id = (TextView) itemView.findViewById(R.id.note_id);
            check_done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //UpdateNoteDialog d = UpdateNoteDialog.getInstance(token);
                    //d.show(v.getContext().getFragmentManager(), "write");
                }
            });
            date = (TextView) itemView.findViewById(R.id.msg_date);
        }
    }


}
