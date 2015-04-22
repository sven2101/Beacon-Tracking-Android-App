package de.beacon4transparence;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class HeadFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.activity_ranging_head, container, false);


        /*Button newPage = (Button)v.findViewById(R.id.Button01);
        newPage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



            }
        });*/

        return v;

    }

}
