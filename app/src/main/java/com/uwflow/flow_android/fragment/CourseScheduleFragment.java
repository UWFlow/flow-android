package com.uwflow.flow_android.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.crashlytics.android.Crashlytics;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.adapters.CourseClassListAdapter;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.db_object.Section;
import com.uwflow.flow_android.db_object.Sections;
import com.uwflow.flow_android.network.FlowApiRequestCallbackAdapter;
import com.uwflow.flow_android.network.FlowApiRequests;
import com.uwflow.flow_android.util.CalendarHelper;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by jasperfung on 2/21/14.
 */
public class CourseScheduleFragment extends TrackedFragment {
    private static final String TAG = "CourseScheduleFragment";
    private String mCourseID;

    private LinearLayout mScheduleContainer;
    private TableLayout mClassListContainer;
    private TextView mEmptyScheduleView;
    private BaseAdapter mCourseClassListAdapter;
    private TextView mLastUpdatedTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
	Bundle args = getArguments();
	if (args != null) {
	    mCourseID = getArguments().getString(Constants.COURSE_ID_KEY);
    } else {
        // TODO(david): Add a FlowLogger class that auto-logs to Crashlytics
        String errorLog = "Attempted instantiation without bundle: cannot fetch course details.";
        Log.e(TAG, errorLog);
        Crashlytics.log(Log.ERROR, TAG, errorLog);
    }

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.course_schedule, container, false);
        mScheduleContainer = (LinearLayout)rootView.findViewById(R.id.schedule);
        mClassListContainer = (TableLayout)rootView.findViewById(R.id.class_list);
        mEmptyScheduleView = (TextView)rootView.findViewById(R.id.empty_schedule);
        mLastUpdatedTextView = (TextView)rootView.findViewById(R.id.sections_last_updated);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

	fetchCourseSections(mCourseID);
    }

    private void fetchCourseSections(String course){
        FlowApiRequests.getCourseSections(
                course,
                new FlowApiRequestCallbackAdapter() {
                    @Override
                    public void getCourseSectionsCallback(Sections sections) {
                        List<Section> sectionList = sections.getSections();

                        mCourseClassListAdapter = new CourseClassListAdapter(sectionList, getActivity());

                        if (mCourseClassListAdapter.getCount() > 0) {
                            mEmptyScheduleView.setVisibility(View.GONE);
                            mScheduleContainer.setVisibility(View.VISIBLE);

                            // Clear any existing class listings
                            for (int i = 2; i < mClassListContainer.getChildCount(); i++) {
                                mClassListContainer.removeViewAt(i);
                            }

                            // Generate LinearLayouts for every list item
                            String lastTerm = "";
                            for (int i = 0; i < mCourseClassListAdapter.getCount(); i++) {
				String currentTerm = CalendarHelper.formatTermNicely(((Section) mCourseClassListAdapter.getItem(i)).getTermId());
                                if (!currentTerm.equals(lastTerm)) {
                                    // Insert a header for a new term
                                    lastTerm = currentTerm;
                                    mClassListContainer.addView(createScheduleTermHeader(currentTerm));
                                    mClassListContainer.addView(createScheduleDivider(1));
                                }

                                View item = mCourseClassListAdapter.getView(i, null, null);
                                mClassListContainer.addView(item, new TableLayout.LayoutParams(
                                        TableRow.LayoutParams.MATCH_PARENT,
                                        TableRow.LayoutParams.WRAP_CONTENT));

                                mClassListContainer.addView(createScheduleDivider(1));
                            }

                            // Set last updated date
                            Section firstSection = sectionList.get(0);
                            Timestamp lastUpdated =  firstSection.getLastUpdated();
                            String lastUpdatedString = new SimpleDateFormat("MMM d 'at' h:mm a").format(lastUpdated);
                            mLastUpdatedTextView.setText("Last updated " + lastUpdatedString);
                        } else {
                            // No classes to show. Hide the schedule table.
                            mEmptyScheduleView.setVisibility(View.VISIBLE);
                            mScheduleContainer.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private View createScheduleDivider(int thickness) {
        View dividerView = new View(getActivity());
        dividerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, thickness));
        dividerView.setBackgroundColor(0xff717171);
        return dividerView;
    }

    private View createScheduleTermHeader(String heading) {
        TextView textView = new TextView(getActivity());
        textView.setText(heading);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1f));
        float scale = getResources().getDisplayMetrics().density; // scale for converting dp to px
        textView.setPadding((int)(10 * scale + 0.5f), 0, 0, 0);
        textView.setBackgroundResource(R.color.flow_light_blue);

        return textView;
    }
}
