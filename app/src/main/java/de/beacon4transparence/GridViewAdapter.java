package de.beacon4transparence;

/**
 * Created by Sven on 18.05.2015.
 */
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private final ArrayList<String> networkName;
    private String typ;

    public GridViewAdapter(Context context, ArrayList<String> networkName,String typ) {
        this.context = context;
        this.networkName = networkName;
        this.typ = typ;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        RecordHolder holder = null;

        if (row == null) { LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.network_layout, parent, false);
            holder = new RecordHolder();
            holder.txtTitle = (TextView)row.findViewById(R.id.network_grid_item_label);
            holder.imageItem = (ImageView) row.findViewById(R.id.network_grid_item_image);

            row.setTag(holder);
        }
        else {
            holder = (RecordHolder) row.getTag();
        }
        holder.txtTitle.setText(networkName.get(position));
        if (this.typ.equals("network"))
        {
            holder.imageItem.setImageResource(R.drawable.network);
        }
        else if(this.typ.equals("beacon"))
        {
            holder.imageItem.setImageResource(R.drawable.beacon);
        }
        else
        {
            holder.imageItem.setImageResource(R.drawable.ic_home
            );
        }


        return row;
    }
    static class RecordHolder { TextView txtTitle; ImageView imageItem; }


    @Override
    public int getCount() {
        return networkName.size();
    }

    @Override
    public Object getItem(int position) {
        return networkName.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

}