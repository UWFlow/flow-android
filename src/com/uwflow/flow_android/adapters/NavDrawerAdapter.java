package com.uwflow.flow_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.entities.NavDrawerItem;

import java.util.ArrayList;

/**
 * Created by jasperfung on 2/28/14.
 */
public class NavDrawerAdapter extends BaseAdapter {
    ArrayList<NavDrawerItem> navDrawerItemList;
    Context context;

    public NavDrawerAdapter(ArrayList<NavDrawerItem> items, Context context) {
        navDrawerItemList = items;
        this.context = context;
    }

    public int getCount() {
        return navDrawerItemList.size();
    }

    public Object getItem(int i) {
        return navDrawerItemList.get(i);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // verify that convertView is not null
        if (convertView == null) {
            // inflate a new view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.nav_drawer_item, parent, false);
        }

        TextView label;
        ImageView icon;

        label = (TextView) convertView.findViewById(R.id.drawer_item_name);
        icon = (ImageView) convertView.findViewById(R.id.drawer_item_icon);

        label.setText(navDrawerItemList.get(position).getName());

        icon.setImageResource(navDrawerItemList.get(position).getIconResID());

        return convertView;
    }


}
