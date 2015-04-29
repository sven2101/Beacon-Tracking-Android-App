package de.beacon4transparence;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devspark.progressfragment.ProgressFragment;



public class BeaconFragment extends ProgressFragment {

    //private static TextView textview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_ranging, container, false);


        //textview = (TextView) v.findViewById(R.id.test);
        return v;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(R.layout.activity_ranging);
        setContentShown(true);

    }

    public void showContent(boolean show)
    {
        if(show == true)
            setContentShown(true);
        else
            setContentShown(false);
    }

}
