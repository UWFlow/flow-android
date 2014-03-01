package com.uwflow.flow_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.entities.Exam;
import com.uwflow.flow_android.entities.Friend;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Chinmay on 3/1/14.
 */
public class ProfileExamAdapter extends BaseAdapter {

    private ArrayList<Exam> mExamList;
    private Context mContext;

    public ProfileExamAdapter(ArrayList<Exam> exams, Context context) {
        mExamList = exams;
        mContext = context;
    }

    public int getCount() {
        return mExamList.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // verify that convertView is not null
        if (convertView == null) {
            // inflate a new view
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.profile_exams_row_item, parent, false);
        }

        // Fill view with appropriate data
        TextView first, second, third;

        first = (TextView) convertView.findViewById(R.id.first);
        second = (TextView) convertView.findViewById(R.id.second);
        third = (TextView) convertView.findViewById(R.id.third);

        first.setText(mExamList.get(position).getFirst());
        second.setText(mExamList.get(position).getSecond());
        third.setText(mExamList.get(position).getThird());


        return convertView;
    }

    public Object getItem(int arg0) {
        return mExamList.get(arg0);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

}
