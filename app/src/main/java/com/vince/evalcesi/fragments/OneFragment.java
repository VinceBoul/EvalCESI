package com.vince.evalcesi.fragments;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vince.evalcesi.R;
import com.vince.evalcesi.WriteMsgDialog;
import com.vince.evalcesi.adapter.MessagesAdapter;
import com.vince.evalcesi.helper.JsonParser;
import com.vince.evalcesi.helper.NetworkHelper;
import com.vince.evalcesi.model.HttpResult;
import com.vince.evalcesi.model.Message;
import com.vince.evalcesi.model.Note;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class OneFragment extends Fragment {

    RecyclerView listView;
    FloatingActionButton fab;
    MessagesAdapter adapter;
    private GetMessagesAsyncTask messagesAsyncTask;
    String token;

    private LinearLayoutManager mLayoutManager;

    Timer timer;
    Boolean timerIsOn = true;

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            refresh();
        }
    };

    private void refresh() {
        if (timerIsOn){
            if (messagesAsyncTask == null || messagesAsyncTask.getStatus() != AsyncTask.Status.RUNNING){
                messagesAsyncTask = new GetMessagesAsyncTask(getContext());
                messagesAsyncTask.execute();
            }
        }else{
            timer.cancel();
        }
        //  swipeRefreshLayout.setRefreshing(true);
    }

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // On récupère le token dans les arguments du fragment
        token= getArguments().getString("theToken");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one, container, false);

        listView = (RecyclerView) view.findViewById(R.id.tchat_list);
        listView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getContext());
        listView.setLayoutManager(mLayoutManager);


        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WriteMsgDialog d = WriteMsgDialog.getInstance(token);
                d.show(getActivity().getFragmentManager(), "write");
            }
        });

        adapter = new MessagesAdapter(this.getContext());
        listView.setAdapter(adapter);

        messagesAsyncTask = new GetMessagesAsyncTask(getContext());
        messagesAsyncTask.execute();
        return view;
    }
    /**
     * AsyncTask for sign-in
     */
    protected class GetMessagesAsyncTask extends AsyncTask<Void, Void, List<Note>> {

        Context context;

        public GetMessagesAsyncTask(final Context context) {
            this.context = context;
        }

        @Override
        protected List<Note> doInBackground(Void... params) {
            if(!NetworkHelper.isInternetAvailable(context)){
                /*MessagesDAO dao = new MessagesDAO(context);
                return dao.readMessages();*/
            }

            InputStream inputStream = null;

            try {
                HttpResult result = NetworkHelper.doGet(context.getString(R.string.url_notes), null, token);

                if(result.code == 200) {
                    // Convert the InputStream into a string
                    List<Note> notes = JsonParser.getNotes(result.json);
                    return notes;
                    //return JsonParser.getMessages(result.json);
                    /*MessagesDAO dao = new MessagesDAO(context);

                    dao.writeMessages(messages);*/
                }
                return null;

                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } catch (Exception e) {
                Log.e("NetworkHelper", e.getMessage());
                return null;
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        Log.e("NetworkHelper", e.getMessage());
                    }
                }
            }
        }

        @Override
        public void onPostExecute(final List<Note> notes){
            int nb = 0;
            if(notes != null){
                nb = notes.size();
            }

            Toast.makeText(getContext(),"Il y a "+nb+" messages", Toast.LENGTH_SHORT);
            adapter.addMessage(notes);
        }
    }

}