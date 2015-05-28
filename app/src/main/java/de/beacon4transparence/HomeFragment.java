package de.beacon4transparence;

/**
 * Created by Sven on 30.04.2015.
 */
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

public class HomeFragment extends Fragment {
    public static final String PREFS_NAME = "login_prefs";
    public HomeFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, 0);
        String userName = prefs.getString("userName", "").toString();

        TextView welcomeLabel = (TextView) getActivity().findViewById(R.id.welcomeLabel);
        welcomeLabel.setText("Welcome to Betrance");

    }
}