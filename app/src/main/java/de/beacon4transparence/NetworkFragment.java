package de.beacon4transparence;

/**
 * Created by Sven on 30.04.2015.
 */
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class NetworkFragment extends Fragment {
    GridView gridView;
    ImageButton imageButton;
    View rootView;

    private static int MENU_EDIT = 1;
    private static int MENU_DETAILS = 2;
    private static int MENU_DELETE = 3;
    private static String TAG ="NetworkFragment";

    static final ArrayList<String> networkName = new ArrayList<>();

    public NetworkFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_network, container, false);
        } catch (InflateException e) {

        }
        return rootView;
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        addListenerOnButton();

        gridView = (GridView) getActivity().findViewById(R.id.networkGridVew);
        registerForContextMenu(gridView);
        GridViewAdapter gridViewAdapter = new GridViewAdapter(getActivity(),networkName,"network");
        gridView.setAdapter(gridViewAdapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                String nameOfNetwork =  ((TextView) v.findViewById(R.id.network_grid_item_label)).getText().toString();

                Intent myIntent = new Intent(getActivity(), BeaconActivity.class);
                myIntent.putExtra("network", nameOfNetwork); //Optional parameters
                getActivity().startActivity(myIntent);
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


        GridView gridview = (GridView) getActivity().findViewById(R.id.networkGridVew);
        String details =(gridview.getItemAtPosition(index).toString());


         LayoutInflater linf = LayoutInflater.from(getActivity());
         final View inflator = linf.inflate(R.layout.add_network_layout, null);
         final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
         builder.setTitle("Edit Network");
         builder.setView(inflator);

         final AutoCompleteTextView network = (AutoCompleteTextView) inflator.findViewById(R.id.networkName);
         final EditText password = (EditText) inflator.findViewById(R.id.networkPassword);
         network.setText(details);


            builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    String networkStr = network.getText().toString();
                    String password1 = password.getText().toString();

                    networkName.set(index, networkStr);

                    Toast.makeText(getActivity().getApplicationContext(),
                            "New network " + networkStr + " Password: " + password1, Toast.LENGTH_LONG)
                            .show();
                }


            });
            builder.setNegativeButton(android.R.string.cancel,null);
            builder.show();




        } else if (item.getItemId() == MENU_DELETE) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Delete Network");
            builder.setMessage("Really delete network?");
            builder.setIcon(R.drawable.ic_action_about);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    networkName.remove(index);
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Network with all beacons deleted!", Toast.LENGTH_LONG)
                            .show();
                }
             });
            builder.setNegativeButton("cancel",null);
            builder.show();
        }
         else if (item.getItemId() == MENU_DETAILS) {

            GridView gridview = (GridView) getActivity().findViewById(R.id.networkGridVew);
            String details =(gridview.getItemAtPosition(index).toString());

            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(details);
            builder.setIcon(R.drawable.ic_action_about);
            builder.setPositiveButton(android.R.string.ok,null);
            builder.setMessage("how much beacons...");
            builder.show();
        }
        return super.onContextItemSelected(item);
    }

    public void addListenerOnButton() {

        imageButton = (ImageButton) getActivity().findViewById(R.id.addNetworkButton);

        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                LayoutInflater linf = LayoutInflater.from(getActivity());
                final View inflator = linf.inflate(R.layout.add_network_layout, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Add new network");
                builder.setView(inflator);

                final AutoCompleteTextView network = (AutoCompleteTextView) inflator.findViewById(R.id.networkName);
                final EditText password = (EditText) inflator.findViewById(R.id.networkPassword);



                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String networkStr = network.getText().toString();
                        String password1=password.getText().toString();

                        networkName.add(networkStr);
                                Toast.makeText(getActivity().getApplicationContext(),
                                        "Network "+ networkStr + " Password: " + password1, Toast.LENGTH_LONG)
                                        .show();
                            }



                });
                builder.setNegativeButton(android.R.string.cancel,null);
                builder.show();
            }
        });

    }


}