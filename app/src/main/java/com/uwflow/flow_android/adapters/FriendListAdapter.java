package com.uwflow.flow_android.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.db_object.User;
import com.uwflow.flow_android.entities.CourseFriend;
import com.uwflow.flow_android.fragment.ProfileFragment;
import com.uwflow.flow_android.network.FlowImageLoader;

import java.util.ArrayList;

public class FriendListAdapter extends BaseAdapter {
    private ArrayList<CourseFriend> mList;
    private Context mContext;
    private FragmentManager mFragmentManager;
    private FlowImageLoader flowImageLoader;

    public FriendListAdapter(ArrayList<CourseFriend> list, Context context, FragmentManager fragmentManager) {
        mList = list;
        mContext = context;
	    mFragmentManager = fragmentManager;
        flowImageLoader = new FlowImageLoader(context);
    }

    public int getCount() {
        return mList.size();
    }

    public Object getItem(int position) {
        return mList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        // verify that convertView is not null
        if (convertView == null) {
            holder = new ViewHolder();
            // inflate a new view
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.profile_friends_row_item, parent, false);
            holder.first = (TextView) convertView.findViewById(R.id.first);
            holder.second = (TextView) convertView.findViewById(R.id.second);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        final User user = mList.get(position).getUser();

        // Set text fields
        holder.first.setText(user.getName());
        holder.second.setText(mList.get(position).getTermName());

        flowImageLoader.loadImageForList(user.getProfilePicUrls().getLarge(), holder.imageView);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentManager.beginTransaction()
                        .replace(R.id.content_frame, ProfileFragment.newInstance(user.getId()))
                        .addToBackStack(null)
                        .commit();
            }
        };
        convertView.setOnClickListener(onClickListener);

        return convertView;
    }


    public static class ViewHolder {
        public TextView first;
        public TextView second;
        public ImageView imageView;
    }
}

