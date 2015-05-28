package de.beacon4transparence;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.beacon4transparence.R;

public class BeaconActivity extends Activity {

    GridView gridView;

    private static int MENU_EDIT = 1;
    private static int MENU_DETAILS = 2;
    private static int MENU_DELETE = 3;
    private static String TAG ="BeaconActivity";

    static final ArrayList<String> beaconName = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);

        beaconName.add("Küche");
        beaconName.add("Bad");
        beaconName.add("Klo");

        gridView = (GridView) findViewById(R.id.beaconGridView);
        GridViewAdapter gridViewAdapter = new GridViewAdapter(this,beaconName,"beacon");
        gridView.setAdapter(gridViewAdapter);

        registerForContextMenu(gridView);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                String nameOfBeacon =  ((TextView) v.findViewById(R.id.network_grid_item_label)).getText().toString();

                v.setLongClickable(false);
                openContextMenu(v);
            }
        });

    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(0, MENU_EDIT, 0, "Edit");
        menu.add(0, MENU_DELETE, 1, "Delete");
        menu.add(0, MENU_DETAILS, 2, "Details");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int index = info.position;


        if (item.getItemId() == MENU_EDIT) {


            GridView gridview = (GridView) findViewById(R.id.beaconGridView);
            String details =(gridview.getItemAtPosition(index).toString());


 ;
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Edit Beacon");
            builder.setMessage("Location");

            // Set an EditText view to get user input
            final EditText input = new EditText(this);
            input.setText(details);
            builder.setView(input);

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String value = input.getText().toString();
                    beaconName.set(index, value);

                    Toast.makeText(getApplicationContext(),
                            "New Beaconname " + value, Toast.LENGTH_LONG)
                            .show();

                }
            });
            builder.setNegativeButton(android.R.string.cancel,null);
            builder.show();




        } else if (item.getItemId() == MENU_DELETE) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete Beacon");
            builder.setMessage("Really delete Beacon?");
            builder.setIcon(R.drawable.ic_action_about);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    beaconName.remove(index);
                    Toast.makeText(getApplicationContext(),
                            "Beacon deleted!", Toast.LENGTH_LONG)
                            .show();
                }
            });
            builder.setNegativeButton("cancel",null);
            builder.show();
        }
        else if (item.getItemId() == MENU_DETAILS) {

            GridView gridview = (GridView) this.findViewById(R.id.beaconGridView);
            String details =(gridview.getItemAtPosition(index).toString());

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(details);
            builder.setIcon(R.drawable.ic_action_about);
            builder.setPositiveButton(android.R.string.ok,null);
            builder.setMessage("Beacon details coming soon...");
            builder.show();
        }
        return super.onContextItemSelected(item);
    }
}
