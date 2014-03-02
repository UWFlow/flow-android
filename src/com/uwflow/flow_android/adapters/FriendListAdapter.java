package com.uwflow.flow_android.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.entities.Friend;
import utility.FacebookUtilities;

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
            convertView = inflater.inflate(R.layout.profile_friends_row_item, parent, false);
        }

        // Fill view with appropriate data
        TextView first, second;
        ImageView image;

        first = (TextView) convertView.findViewById(R.id.first);
        second = (TextView) convertView.findViewById(R.id.second);
        image = (ImageView) convertView.findViewById(R.id.image);

        // Set text fields
        first.setText(mList.get(position).getFirst());
        second.setText(mList.get(position).getSecond());

        // Set photo resource
        if (mList.get(position).getImage() == null) {
            image.setImageResource(R.drawable.photo_profile_empty);
        } else {
            image.setImageBitmap(mList.get(position).getImage());
        }


        final Friend f = mList.get(position);

        // Make this View clickable to open a dialog for Facebook/Flow profile links
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUserDialog(mContext, f).show();
            }
        };
        convertView.setOnClickListener(onClickListener);

        return convertView;
    }

    private String getFirstName(String fullname) {
        if (fullname.contains(" ")) {
            return fullname.substring(0, fullname.indexOf(" "));
        }
        return fullname;
    }

    private AlertDialog createUserDialog(Context context, final Friend friend) {
        String firstname = getFirstName(friend.getFirst());

        final String[] dialogOptions = {
                String.format("View %s on Flow", firstname),
                String.format("View %s on Facebook", firstname)};

        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("View Profile")
                .setItems(dialogOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 1:
                                // View friend on Facebook
                                Intent profileIntent =
                                        FacebookUtilities.getOpenFBProfileIntent(mContext, friend.getFbid());
                                mContext.startActivity(profileIntent);
                                break;
                            case 0:
                            default:
                                // View friend on Flow
                                // TODO: route to a user's profile on flow
                                break;
                        }
                        dialog.dismiss();
                    }
                }).create();

        return dialog;
    }

}

