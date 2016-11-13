package com.vince.evalcesi.fragments;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import com.vince.evalcesi.R;
import com.vince.evalcesi.WriteNoteDialog;
import com.vince.evalcesi.adapter.NotesAdapter;
import com.vince.evalcesi.helper.JsonParser;
import com.vince.evalcesi.helper.NetworkHelper;
import com.vince.evalcesi.model.HttpResult;
import com.vince.evalcesi.model.Note;
import com.vince.evalcesi.util.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class OneFragment extends Fragment {

    RecyclerView listView;
    FloatingActionButton fab;
    NotesAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    private GetNotesAsyncTask messagesAsyncTask;
    private SendNoteDoneAsyncTask sendNoteDoneAsyncTask;

    private SwipeRefreshLayout swipeLayout;
    String token;

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
                messagesAsyncTask = new GetNotesAsyncTask(getContext());
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

        listView = (RecyclerView) view.findViewById(R.id.notes_list);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.notes_swiperefresh);

        listView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getContext());
        listView.setLayoutManager(mLayoutManager);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WriteNoteDialog d = WriteNoteDialog.getInstance(token);
                d.show(getActivity().getFragmentManager(), "write");
            }
        });

        sendNoteDoneAsyncTask = new SendNoteDoneAsyncTask(getContext());

        adapter = new NotesAdapter(this.getContext());
        listView.setAdapter(adapter);

        setupRefreshLayout();

        messagesAsyncTask = new GetNotesAsyncTask(getContext());
        messagesAsyncTask.execute();

        return view;
    }

    private void loading() {
        swipeLayout.setRefreshing(true);
        new GetNotesAsyncTask(getContext()).execute();
    }

    /**
     * Setup refresh layout
     */
    private void setupRefreshLayout() {
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loading();
            }
        });
        swipeLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorPrimary);

    }

    /**
     * AsyncTask for sign-in
     */
    protected class GetNotesAsyncTask extends AsyncTask<Void, Void, List<Note>> {

        Context context;

        public GetNotesAsyncTask(final Context context) {
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
                }
                return null;

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
            swipeLayout.setRefreshing(false);
            Toast.makeText(getContext(),"Il y a "+nb+" notes", Toast.LENGTH_SHORT);
            adapter.addNotes(notes);
        }
    }

    public class SendNoteDoneAsyncTask extends AsyncTask<String, Void, Integer> {

        Context context;

        public SendNoteDoneAsyncTask(final Context context) {
            this.context = context;
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                Map<String, String> p = new HashMap<>();
                p.put("id", params[0]);
                //p.put("done", n.isDone() ? "false" : "true");

                HttpResult result = NetworkHelper.doPost(context.getString(R.string.url_notes) + "/" + params[0], p, token);

                //HttpResult result = NetworkHelper.doPost(context.getString(R.string.url_notes), p, token);

                return result.code;
            } catch (Exception e) {
                Log.d(Constants.TAG, "Error occured in your AsyncTask : ", e);
                return 500;
            }
        }

        @Override
        public void onPostExecute(Integer status) {
            if (status != 200) {
                Toast.makeText(context, context.getString(R.string.error_send_msg), Toast.LENGTH_SHORT).show();
            }else{
            }
        }
    }

}