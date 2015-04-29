package de.beacon4transparence;

import android.app.Service;
import android.content.Intent;

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


    public void addItem(String beacon, String distance, String address, int typecode, int manufacturer)
    {
        //BeaconShowActivity.progress.dismiss();
        HashMap<String, String> hm = new HashMap<String,String>();
        hm.put("name",beacon);
        hm.put("distance","Distanz : " + distance+" meter");
        hm.put("address","Bluetooth Adresse : " + address);
        hm.put("typecode","Beacon Type Code: " + typecode);
        hm.put("manufacturer","Beacon HerstellerID: " + manufacturer);
        hm.put("flag", Integer.toString(R.drawable.ic_action_bluetooth_connected) );
        BeaconShowActivity.aList.add(hm);

        Log.d("BackgroundService", "Aktualisiere meine ListView");

        BeaconShowActivity.ac.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BeaconShowActivity.adapter.notifyDataSetChanged();
                BeaconShowActivity.beaconFragment.showContent(true);
            }
        });
    }
    public void modifyItem(String beacon, String distance)
    {
        Log.d("BackgroundService", "suche Distanz zum aktualisieren...");
        for (int i = 0; i < BeaconShowActivity.aList.size(); i++)
        {
            Log.d("BackgroundService", ""+BeaconShowActivity.aList.get(i).get("name").toString()+" = "+beacon);
            if(BeaconShowActivity.aList.get(i).get("name").equals(beacon)) {

                Log.d("BackgroundService", "Aktualisiere meine Distanz mit: " + distance);
                BeaconShowActivity.aList.get(i).put("distance", "Distanz : " + distance+" meter");

                BeaconShowActivity.ac.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BeaconShowActivity.adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }

    @Override
    public void onBeaconServiceConnect() {
        Log.d("BackgroundService", "Ich schaue nach Beacons");
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    //EditText editText = (EditText)RangingActivity.this
                    //.findViewById(R.id.rangingText);
                    Beacon beacon = beacons.iterator().next();
                    Log.d("BackgroundService", "Beacon gefunden: "+beacon.getBluetoothName());
                    if(!BeaconShowActivity.beaconName.contains(beacon.getBluetoothName()))
                    {
                        beacon.getBeaconTypeCode();
                        beacon.getBluetoothAddress();
                        beacon.getManufacturer();

                        Log.d("BackgroundService", "Beacon hinzufügen: "+beacon.getBluetoothName());
                        addItem(beacon.getBluetoothName(), String.format("%.2f", beacon.getDistance()), beacon.getBluetoothAddress(),beacon.getBeaconTypeCode(), beacon.getManufacturer());
                        BeaconShowActivity.beaconName.add(beacon.getBluetoothName());
                    }
                    else
                    {
                        modifyItem(beacon.getBluetoothName(), String.format("%.2f", beacon.getDistance()));
                    }
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
        Log.d("BackgroundService", "Ich möchte meinen Service starten");
        onBeaconServiceConnect();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("BackgroundService", "Ich möchte aufhören zu suchen und den service stoppen");
                try {
                    beaconManager.stopRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
                    stopSelf();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }, 45000);

        return START_STICKY;
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}