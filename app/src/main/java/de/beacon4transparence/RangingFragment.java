package de.beacon4transparence;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.altbeacon.beacon.BeaconManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RangingFragment extends Fragment {

    private static final String TAG = "RangingFragment";
    static List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
    static SimpleAdapter listViewAdapter;
    static ArrayList<String> beaconName = new ArrayList<String>();
    int positionNetwork;
    private static int MENU_ADD = 1;
    private static int MENU_DETAILS = 2;
    private static View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        verifyBluetooth();
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fragments_layout, container, false);
        } catch (InflateException e) {
        }

        return rootView;
    }

    private void verifyBluetooth() {

        try {
            if (!BeaconManager.getInstanceForApplication(getActivity()).checkAvailability()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Bluetooth not enabled");
                builder.setMessage("Please enable bluetooth in settings and retry");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.show();
            }
        }
        catch (RuntimeException e) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Bluetooth LE not available");
            builder.setMessage("Sorry, this device does not support Bluetooth LE.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.show();

        }

    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] from = { "flag","name","distance" };

        // Ids of views in listview_layout
        int[] to = { R.id.flag,R.id.name,R.id.distance};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        listViewAdapter = new SimpleAdapter(getActivity(), aList, R.layout.listview_layout, from, to);

        // Getting a reference to listview of main.xml layout file
        ListView listView = ( ListView ) getActivity().findViewById(R.id.listview);


        //registerContextMenu
        registerForContextMenu(listView);


        // Setting the adapter to the listView
        listView.setAdapter(listViewAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                v.setLongClickable(false);
                getActivity().openContextMenu(v);

            }
        });
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(0, MENU_ADD, 0, "Add Beacon to network...");
        menu.add(0, MENU_DETAILS, 1, "Show details");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;


        if (item.getItemId() == MENU_ADD) {

            positionNetwork = -1;

            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Networks");
            builder.setSingleChoiceItems(R.array.details, -1, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    positionNetwork = arg1;
                }
            });

            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Resources res = getResources();
                    String[] networks = res.getStringArray(R.array.details);
                    if (positionNetwork >= 0) {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Beacon " + networks[positionNetwork] + " added to Network", Toast.LENGTH_LONG)
                                .show();
                    }else {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "No network selected", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });
            builder.setNegativeButton(android.R.string.cancel, null);
            builder.show();



        } else if (item.getItemId() == MENU_DETAILS) {

            ListView lv = (ListView) getActivity().findViewById(R.id.listview);
            String details =(lv.getItemAtPosition(index).toString());


            Log.d(TAG, "Get details from beacon"+details);


            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Details");
            builder.setIcon(R.drawable.ic_action_about);
            builder.setPositiveButton(android.R.string.ok,null);
            builder.setMessage(details);
            builder.show();
        }
        return super.onContextItemSelected(item);
    }


}
