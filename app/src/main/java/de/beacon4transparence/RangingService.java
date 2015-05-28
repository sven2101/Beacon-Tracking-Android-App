package de.beacon4transparence;

import android.app.Service;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.HashMap;


public class RangingService extends Service implements BeaconConsumer {
    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
    //private final IBinder mBinder = new LocalBinder();
    private static int msgID = 0;
    private static final String TAG = "RangingService";


    public void addItem(String beacon, String distance, String address, int typecode, int manufacturer)
    {
        HashMap<String, String> hm = new HashMap<String,String>();
        hm.put("name",beacon);
        hm.put("distance","Distance : " + distance+" meter");
        hm.put("address","Bluetooth address : " + address);
        hm.put("typecode","Beacon type code: " + typecode);
        hm.put("manufacturer","Beacon ManufacturerID: " + manufacturer);
        hm.put("flag", Integer.toString(R.drawable.ic_action_bluetooth_connected) );
        RangingFragment.aList.add(hm);

        msgID = 2;
        sendBroadcastNotification(Bundle.EMPTY);
    }
    public void modifyItem(String beacon, String distance)
    {
        Log.d(TAG, "looking for latest distance...");
        for (int i = 0; i < RangingFragment.aList.size(); i++)
        {
            if(RangingFragment.aList.get(i).get("name").equals(beacon)) {

                Log.d(TAG, "Update distance with: " + distance);
                RangingFragment.aList.get(i).put("distance", "Distance : " + distance+" meter");

                msgID = 3;
                sendBroadcastNotification(Bundle.EMPTY);
            }
        }
    }

    @Override
    public void onBeaconServiceConnect() {
        Log.d(TAG, "Looking for Beacons");
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    //EditText editText = (EditText)RangingActivity.this
                    //.findViewById(R.id.rangingText);
                    Beacon beacon = beacons.iterator().next();
                    Log.d(TAG, "There's a beacon: "+beacon.getBluetoothName());
                    if(!RangingFragment.beaconName.contains(beacon.getBluetoothName()))
                    {
                        beacon.getBeaconTypeCode();
                        beacon.getBluetoothAddress();
                        beacon.getManufacturer();

                        Log.d(TAG, "Adding beacon: "+beacon.getBluetoothName());
                        addItem(beacon.getBluetoothName(), String.format("%.2f", beacon.getDistance()), beacon.getBluetoothAddress(),beacon.getBeaconTypeCode(), beacon.getManufacturer());
                        RangingFragment.beaconName.add(beacon.getBluetoothName());
                    }
                    else
                    {
                        modifyItem(beacon.getBluetoothName(), String.format("%.2f", beacon.getDistance()));
                    }
                    //maybe to log...
                    //logToDisplay("The Beacon "+beacon.getBluetoothName()+" is about "+beacon.getDistance()+" meters away.");
                }
            }

        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {   }
    }

  /*  public class LocalBinder extends Binder {
        backgroundService getService() {
            return backgroundService.this;
        }
    }
*/

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Starting service...");
        onBeaconServiceConnect();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "stopping service");
                try {
                      beaconManager.stopRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
                    msgID = 1;
                    sendBroadcastNotification(Bundle.EMPTY);
                    stopSelf();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }, 45000);

        return START_STICKY;
    }

    public void sendBroadcastNotification(Bundle extras) {
        Log.d(TAG, "Sending broadcast notification" + msgID);
        Intent intentBroadcast = new Intent("updateUI");
        intentBroadcast.putExtra("updateUI",
                msgID);

        sendBroadcast(intentBroadcast);
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}