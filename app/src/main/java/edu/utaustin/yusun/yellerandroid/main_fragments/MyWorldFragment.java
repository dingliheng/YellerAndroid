package edu.utaustin.yusun.yellerandroid.main_fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import edu.utaustin.yusun.yellerandroid.function_activities.PullToRefreshListView;
import edu.utaustin.yusun.yellerandroid.R;
import edu.utaustin.yusun.yellerandroid.adapter.PullToRefreshListViewAdapter;
import edu.utaustin.yusun.yellerandroid.friends_activities.FriendListActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link MyWorldFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyWorldFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private PullToRefreshListView listView;
    private PullToRefreshListViewAdapter adapter;


    //Data to show
    ArrayList<String> items = new ArrayList<>();
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyWorldFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyWorldFragment newInstance(String param1, String param2) {
        MyWorldFragment fragment = new MyWorldFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_world, container, false);
        listView = (PullToRefreshListView) rootView.findViewById(R.id.pull_to_refresh_listview);

        listView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {

            @Override
            public void onRefresh() {

                adapter.loadData();
                listView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listView.onRefreshComplete();
                    }
                }, 2000);
            }
        });

        adapter = new PullToRefreshListViewAdapter(getActivity(), items) {};
        listView.setAdapter(adapter);

        // Request the adapter to load the data
        adapter.loadData();

        // click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                PullToRefreshListViewAdapter.ViewHolder viewHolder = (PullToRefreshListViewAdapter.ViewHolder) arg1.getTag();
                if (viewHolder.name != null) {
                    Toast.makeText(getContext(), viewHolder.name.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        final ImageButton friendlist_button = (ImageButton) rootView.findViewById(R.id.connections_btn);
        friendlist_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FriendListActivity.class);
                startActivity(intent);

            }
        });

        // Register the context menu for actions
        registerForContextMenu(listView);
        return rootView;
    }

}
