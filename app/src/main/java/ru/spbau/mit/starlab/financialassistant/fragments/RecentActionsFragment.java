package ru.spbau.mit.starlab.financialassistant.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import ru.spbau.mit.starlab.financialassistant.R;
import ru.spbau.mit.starlab.financialassistant.multicolumnlistview.ListViewAdapter;

import static ru.spbau.mit.starlab.financialassistant.multicolumnlistview.Constants.FIRST_COLUMN;
import static ru.spbau.mit.starlab.financialassistant.multicolumnlistview.Constants.FOURTH_COLUMN;
import static ru.spbau.mit.starlab.financialassistant.multicolumnlistview.Constants.SECOND_COLUMN;
import static ru.spbau.mit.starlab.financialassistant.multicolumnlistview.Constants.THIRD_COLUMN;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecentActionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecentActionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecentActionsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public ArrayList<HashMap<String, String>> list;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecentActionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecentActionsFragment newInstance(String param1, String param2) {
        RecentActionsFragment fragment = new RecentActionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RecentActionsFragment() {
        // Required empty public constructor
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

        View ll = inflater.inflate(R.layout.fragment_recent_actions, container, false);



        ListView listView = (ListView) ll.findViewById(R.id.listView1);

        list=new ArrayList<>();

        HashMap<String,String> temp = new HashMap<>();
        temp.put(FIRST_COLUMN, "Name1");
        temp.put(SECOND_COLUMN, "100");
        list.add(temp);

        HashMap<String,String> temp2 = new HashMap<>();
        temp2.put(FIRST_COLUMN, "Name2");
        temp2.put(SECOND_COLUMN, "200");
        list.add(temp2);

        HashMap<String,String> temp3=new HashMap<>();
        temp3.put(FIRST_COLUMN, "Name3");
        temp3.put(SECOND_COLUMN, "300");
        list.add(temp3);

        HashMap<String,String> temp4=new HashMap<>();
        temp4.put(FIRST_COLUMN, "Name4");
        temp4.put(SECOND_COLUMN, "400");
        list.add(temp4);

        ListViewAdapter adapter = new ListViewAdapter(getActivity(), list);
        listView.setAdapter(adapter);



        return ll;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
