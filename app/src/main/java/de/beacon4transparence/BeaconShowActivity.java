package de.beacon4transparence;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;


public class BeaconShowActivity extends FragmentActivity {
    // Array of strings storing country names
    static ArrayList<String> beaconName = new ArrayList<String>();

    static Activity ac;
    static List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
    static SimpleAdapter adapter;
    static BeaconFragment beaconFragment;
    static int positionNetwork;
    //public static ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragments_layout);
       // progress = new ProgressDialog(this);

        this.ac = this;
        String[] from = { "flag","name","distance" };

        // Ids of views in listview_layout
        int[] to = { R.id.flag,R.id.name,R.id.distance};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.listview_layout, from, to);

        // Getting a reference to listview of main.xml layout file
        ListView listView = ( ListView ) findViewById(R.id.listview);

        registerForContextMenu(listView);

        // Setting the adapter to the listView
        listView.setAdapter(adapter);



    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(0, v.getId(), 0, "Add Beacon to network...");
        menu.add(0, v.getId(), 0, "Show details");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;

        ListView lv = (ListView) findViewById(R.id.listview);
        String details =(lv.getItemAtPosition(index).toString());


        Log.d("Backgroundservice",details);
        if (item.getTitle() == "Add Beacon to network...") {

            positionNetwork = -1;

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                        Toast.makeText(getApplicationContext(),
                                "Beacon " + networks[positionNetwork] + " added to Network", Toast.LENGTH_LONG)
                                .show();
                    }else {
                        Toast.makeText(getApplicationContext(),
                                "No network selected", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });
            builder.setNegativeButton(android.R.string.cancel, null);
            builder.show();



        } else if (item.getTitle() == "Show details") {


            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Details");
            builder.setPositiveButton(android.R.string.ok,null);
            builder.setMessage(details);
            builder.show();
        }
        return super.onContextItemSelected(item);
    }

    public void refreshListview()
    {
        Log.d("BackgroundService", "Lösche meine Beacons");
        beaconName.clear();
        aList.clear();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void onScanClick(View view) {

        refreshListview();
        beaconFragment =
                (BeaconFragment)
                        getSupportFragmentManager().findFragmentById(R.id.activity_ranging);
        beaconFragment.showContent(false);
        Log.d("BackgroundService", "Ich starte BackgroundService");
        startService(new Intent(this, RangingService.class));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_beacon_show_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}