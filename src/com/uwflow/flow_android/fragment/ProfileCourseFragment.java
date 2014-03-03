package com.uwflow.flow_android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.uwflow.flow_android.MainFlowActivity;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.adapters.ProfileCoursesAdapter;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.db_object.Course;
import com.uwflow.flow_android.db_object.UserCourseDetail;
import com.uwflow.flow_android.loaders.UserCoursesLoader;

import java.util.List;

public class ProfileCourseFragment extends Fragment implements LoaderManager.LoaderCallbacks<UserCourseDetail>{

    protected ListView mCoursesListView;
    protected View rootView;
    protected ProfileCoursesAdapter profileListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.profile_course_layout, container, false);
        mCoursesListView = (ListView)rootView.findViewById(R.id.course_list);

        getLoaderManager().initLoader(Constants.LoaderManagerId.PROFILE_COURSES_LOADER_ID, null, this);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<UserCourseDetail> onCreateLoader(int i, Bundle bundle) {
        return new UserCoursesLoader(getActivity(), ((MainFlowActivity)getActivity()).getHelper());
    }

    @Override
    public void onLoadFinished(Loader<UserCourseDetail> listLoader, UserCourseDetail userCourseDetail) {
        profileListAdapter = new ProfileCoursesAdapter(userCourseDetail.getCourses(), getActivity());
        mCoursesListView.setAdapter(profileListAdapter);
    }

    @Override
    public void onLoaderReset(Loader<UserCourseDetail> listLoader) {
        mCoursesListView.setAdapter(null);
    }
}
