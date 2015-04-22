package de.beacon4transparence;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class BeaconFragment extends Fragment {

    //private static TextView textview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_ranging, container, false);


        //textview = (TextView) v.findViewById(R.id.test);
        return v;
    }

   /* public void changeTextProperties(int fontsize, String text)
    {
        textview.setText(text);
    }*/
}
