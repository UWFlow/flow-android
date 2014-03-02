package com.uwflow.flow_android.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.db_object.User;
import com.uwflow.flow_android.entities.CourseFriend;
import utility.FacebookUtilities;

import java.util.ArrayList;

/**
 * Created by jasperfung on 2/23/14.
 */
public class FriendListAdapter extends BaseAdapter {
    private ArrayList<CourseFriend> mList;
    private Context mContext;

    public FriendListAdapter(ArrayList<CourseFriend> list, Context context) {
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
	return mList.get(position).getUser().getFbid();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // verify that convertView is not null
        if (convertView == null) {
            // inflate a new view
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.profile_friends_row_item, parent, false);
        }

        // Fill view with appropriate data
        TextView first, second;
        ImageView image;

        first = (TextView) convertView.findViewById(R.id.first);
        second = (TextView) convertView.findViewById(R.id.second);
        image = (ImageView) convertView.findViewById(R.id.image);

	final User user = mList.get(position).getUser();

	// Set text fields
	first.setText(user.getName());
	second.setText(mList.get(position).getTermName());

	Picasso.with(mContext).load(user.getProfilePicUrls().getLarge()).placeholder(R.drawable.kitty).into(image);

        // Make this View clickable to open a dialog for Facebook/Flow profile links
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
		FacebookUtilities.createUserDialog(mContext, user).show();
            }
        };
        convertView.setOnClickListener(onClickListener);

        return convertView;
    }



}

