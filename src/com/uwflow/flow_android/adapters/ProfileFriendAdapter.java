package com.uwflow.flow_android.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.db_object.User;
import com.uwflow.flow_android.fragment.ProfileFragment;
import com.uwflow.flow_android.network.FlowImageLoader;
import com.uwflow.flow_android.util.FacebookUtilities;

import java.util.List;

public class ProfileFriendAdapter extends BaseAdapter {
    private List<User> mFriends;
    private Context mContext;
    private FragmentManager mFragmentManager;
    private FlowImageLoader flowImageLoader;

    public ProfileFriendAdapter(List<User> friends, Context context, FragmentManager fragmentManager) {
        mFriends = friends;
        mContext = context;
	    mFragmentManager = fragmentManager;
        flowImageLoader = new FlowImageLoader(context);
    }

    public List<User> getmFriends() {
        return mFriends;
    }

    public void setmFriends(List<User> mFriends) {
        this.mFriends = mFriends;
    }

    public int getCount() {
        return mFriends.size();
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

        first.setText(mFriends.get(position).getName());
        second.setText(mFriends.get(position).getProgramName());
        flowImageLoader.loadImageInto(mFriends.get(position).getProfilePicUrls().getLarge(), image);
        final User user = mFriends.get(position);

        // Make this View clickable to go to view that user's profile in our app
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
		        mFragmentManager.beginTransaction()
                        .replace(R.id.content_frame, ProfileFragment.newInstance(user))
                        .addToBackStack(null)
                        .commit();
            }
        };
        convertView.setOnClickListener(onClickListener);

        return convertView;
    }

    public Object getItem(int arg0) {
        return mFriends.get(arg0);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}

