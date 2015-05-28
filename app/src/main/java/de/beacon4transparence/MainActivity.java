package de.beacon4transparence;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import de.beacon4transparence.slidingmenu.adapter.NavDrawerListAdapter;
import de.beacon4transparence.slidingmenu.model.NavDrawerItem;

public class MainActivity extends FragmentActivity {

    public static final String PREFS_NAME = "login_prefs";
    private static final String TAG = "Main Activity";
    static BeaconFragment beaconFragment;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        Log.d(TAG,"login string: "+settings.getString("logged", "").toString());

        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Photos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Communities, Will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        //Example with count near text
        // navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));


        // Recycle the typed array
        navMenuIcons.recycle();

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ){
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent myIntent = new Intent(this, SettingsActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                this.startActivity(myIntent);
                return true;
            case R.id.action_logout:
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.remove("logged");
                editor.commit();
                finish();
                return true;
            case R.id.action_about:
                String about = "Impressum:\n" +
                        "GET-NET\n"+
                        "Andreas Foitzik & Sven Jaschkewitz\n\n" +
                        "www.get-net.eu\n" +
                        "www.betrance.de\n\n" +
                        "andreas.foitzik@get-net.eu\n"+
                        "sven.jaschkewitz@get-net.eu";
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.drawable.ic_action_about);
                builder.setTitle("About");
                builder.setPositiveButton(android.R.string.ok,null);
                builder.setMessage(about);
                builder.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        String fragmentTag="";
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                fragmentTag ="HOME_FRAGMENT";
                break;
            case 1:
                fragment = new RangingFragment();
                fragmentTag ="RANGING_FRAGMENT";
                break;
            case 2:
                fragment = new NetworkFragment();
                fragmentTag ="NETWORK_FRAGMENT";
                break;
            case 3:
                fragment = new MonitoringFragment();
                fragmentTag ="MONITORING_FRAGMENT";
                break;
            case 4:
                //fragment = new PagesFragment();
                break;
            case 5:
               // fragment = new WhatsHotFragment();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment, fragmentTag).commit();



            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e(TAG, "Error in creating fragment");
        }
    }
    public void onScanClick(View view) {

        registerReceiver(gpsBRec, new IntentFilter(
                "updateUI"));

        RangingFragment.aList.clear();
        RangingFragment.beaconName.clear();

        beaconFragment =
                (BeaconFragment)
                        getSupportFragmentManager().findFragmentById(R.id.activity_ranging);
        beaconFragment.showContent(false);

        Log.d(TAG, "Delete beacons in list");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RangingFragment.listViewAdapter.notifyDataSetChanged();
            }
        });
        Log.d(TAG, "Starting backgroundservice Ranging");
        startService(new Intent(this, RangingService.class));
    }

    private BroadcastReceiver gpsBRec = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            int msgID = intent.getIntExtra("updateUI",0);
            Log.d(TAG, "Receive Broadcast with message ID: "+msgID);
            if(msgID==1) {
                beaconFragment =
                        (BeaconFragment)
                                getSupportFragmentManager().findFragmentById(R.id.activity_ranging);
                beaconFragment.showContent(true);
                try {
                    unregisterReceiver(gpsBRec);
                } catch (IllegalArgumentException e) {

                }
            }
            else if(msgID==2) {
                Log.d(TAG, "Update ListView");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RangingFragment.listViewAdapter.notifyDataSetChanged();
                        beaconFragment =
                                (BeaconFragment)
                                        getSupportFragmentManager().findFragmentById(R.id.activity_ranging);
                        beaconFragment.showContent(true);
                    }
                });
            }
            else if(msgID==3) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RangingFragment.listViewAdapter.notifyDataSetChanged();
                    }
                });
            }
        }
    };

    public void logToDisplay(final String line){

        final MonitoringFragment myFragment = (MonitoringFragment)getFragmentManager().findFragmentByTag("MONITORING_FRAGMENT");

        if (myFragment != null && myFragment.isVisible()) {
            runOnUiThread(new Runnable() {
                public void run() {
                    TextView edit = (TextView) myFragment.getActivity().findViewById(R.id.monitoringText);
                    edit.append(line+"\n");
                }});
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        ((BeaconNotificationApplication) this.getApplicationContext()).setMainActivity(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        try {unregisterReceiver(gpsBRec);} catch(IllegalArgumentException e){}

        ((BeaconNotificationApplication) this.getApplicationContext()).setMainActivity(null);
    }

}
