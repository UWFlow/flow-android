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

/**
 * Created by jasperfung on 2/23/14.
 */
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

        flowImageLoader.loadImageInto(user.getProfilePicUrls().getLarge(), image);

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



}

