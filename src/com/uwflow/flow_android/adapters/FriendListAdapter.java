package com.uwflow.flow_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.entities.Friend;

import java.util.ArrayList;

/**
 * Created by jasperfung on 2/23/14.
 */
public class FriendListAdapter extends BaseAdapter {
    private ArrayList<Friend> mList;
    private Context mContext;

    public FriendListAdapter(ArrayList<Friend> list, Context context) {
        mList = list;
        mContext = context;
    }

    public int getCount() {
        return mList.size();
    }

    public Object getItem(int position) {
        return mList.get(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // verify that convertView is not null
        if (convertView == null) {
            // inflate a new view
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.friend_list_row_item, parent, false);
        }

        // Fill view with appropriate data
        TextView first, second;
        ImageView image;

        first = (TextView) convertView.findViewById(R.id.first);
        second = (TextView) convertView.findViewById(R.id.second);
        image = (ImageView) convertView.findViewById(R.id.image);

        first.setText(mList.get(position).getFirst());
        second.setText(mList.get(position).getSecond());

        if (mList.get(position).getImage() == null) {
            image.setImageResource(R.drawable.photo_profile_empty);
        } else {
            image.setImageBitmap(mList.get(position).getImage());
        }

        return convertView;
    }
}

