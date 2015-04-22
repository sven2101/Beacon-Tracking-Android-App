package de.beacon4transparence;


import java.util.Collection;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;



public class RangingActivity extends Activity implements BeaconConsumer {
    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_ranging);
        beaconManager.bind(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(true);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(false);
    }

    public void addItem(String beacon, double distance)
    {
        HashMap<String, String> hm = new HashMap<String,String>();
        hm.put("txt", "Beacon name : " + beacon);
        hm.put("cur","Distanz : " + distance);
        hm.put("flag", Integer.toString(R.drawable.ic_launcher) );
        BeaconShowActivity.aList.add(hm);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //BeaconShow.adapter.notifyDataSetChanged();

            }
        });

    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    //EditText editText = (EditText)RangingActivity.this
                    //.findViewById(R.id.rangingText);
                    Beacon beacon = beacons.iterator().next();
                    if(!BeaconShowActivity.beaconName.contains(beacon.getBluetoothName()))
                    {
                        addItem(beacon.getBluetoothName(), beacon.getDistance());
                        BeaconShowActivity.beaconName.add(beacon.getBluetoothName());
                    }
                    //logToDisplay("The Beacon "+beacon.getBluetoothName()+" is about "+beacon.getDistance()+" meters away.");
                }
            }

        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {   }
    }
    private void logToDisplay(final String line) {
        runOnUiThread(new Runnable() {
            public void run() {
                //EditText editText = (EditText)RangingActivity.this
                //.findViewById(R.id.rangingText);
                //editText.append(line+"\n");
            }
        });
    }
}
