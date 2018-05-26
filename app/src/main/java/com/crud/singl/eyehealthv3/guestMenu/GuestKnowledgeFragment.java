package com.crud.singl.eyehealthv3.guestMenu;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.crud.singl.eyehealthv3.R;
import com.crud.singl.eyehealthv3.json.JSONDownloaderOne;

/**
 * A simple {@link Fragment} subclass.
 */
public class GuestKnowledgeFragment extends Fragment{
    String jsonURL="http://eyehealthimpact.com/android_knowlage_newapi/knowledge_one.php";
    GridView gv;

    Callback mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (Callback) context;
        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement Callback");
        }
    }

    public GuestKnowledgeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.eye_fragment_guest_knowledge, container, false);

        gv= (GridView) view.findViewById(R.id.knowledge_gv);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.knowledge_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new JSONDownloaderOne(GuestKnowledgeFragment.this.getActivity(),jsonURL,gv).execute();

            }
        });

        return view;
    }
}
