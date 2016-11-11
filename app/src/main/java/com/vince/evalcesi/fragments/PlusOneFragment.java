package com.vince.evalcesi.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.vince.evalcesi.R;
import com.vince.evalcesi.adapter.UserListAdapter;
import com.vince.evalcesi.helper.JsonParser;
import com.vince.evalcesi.helper.NetworkHelper;
import com.vince.evalcesi.model.HttpResult;
import com.vince.evalcesi.util.Constants;

import java.util.Arrays;
import java.util.List;

public class PlusOneFragment extends Fragment {

    private ListView list;
    private UserListAdapter adapter;
    String token = null;
    private SwipeRefreshLayout swipeLayout;

    public PlusOneFragment() {
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
        View view = inflater.inflate(R.layout.fragment_plus_one, container, false);

        list = (ListView) view.findViewById(R.id.list);

        View header = getActivity().getLayoutInflater().inflate(R.layout.user_list_header, list, false);

        list.addHeaderView(header, null, false);
        List<String> users = Arrays.asList("sup1", "sup2", "sup3");

        adapter = new UserListAdapter(getContext(), users);
        list.setAdapter(adapter);

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.users_swiperefresh);
        setupRefreshLayout();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        loading();
    }
    private void loading() {
        swipeLayout.setRefreshing(true);
        new GetUsersAsyncTask().execute();
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
        /**
         * this is to avoid error on double scroll on listview/swipeRefreshLayout
         */
        list.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if (list != null && list.getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = list.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = list.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                swipeLayout.setEnabled(enable);
            }
        });
    }

    /**
     * AsyncTask for sign-in
     */
    protected class GetUsersAsyncTask extends AsyncTask<String, Void, List<String>> {

        @Override
        protected List<String> doInBackground(String... params) {
            if(!NetworkHelper.isInternetAvailable(getContext())){
                return null;
            }

            try {
                HttpResult result = NetworkHelper.doGet(getContext().getString(R.string.url_users), null, token);

                if(result.code != 200){
                    //error happened
                    return null;
                }
                return JsonParser.getUsers(result.json);
            } catch (Exception e){
                Log.d(Constants.TAG, "Error occured in your AsyncTask : ", e);
                return null;
            }
        }

        @Override
        public void onPostExecute(final List<String> users){
            if (users != null){
                // adapter.
                adapter.setData(users);
                //adapter.clear();
                //adapter.addAll(users);
            }

            adapter.notifyDataSetChanged();
            swipeLayout.setRefreshing(false);
        }
    }

}
