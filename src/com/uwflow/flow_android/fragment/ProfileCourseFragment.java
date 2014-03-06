package com.uwflow.flow_android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
import com.uwflow.flow_android.MainFlowActivity;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.adapters.ProfileCoursesAdapter;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.db_object.Course;
import com.uwflow.flow_android.db_object.UserCourseDetail;
import com.uwflow.flow_android.db_object.UserFriends;
import com.uwflow.flow_android.loaders.UserCoursesLoader;
import com.uwflow.flow_android.network.FlowApiRequestCallbackAdapter;
import com.uwflow.flow_android.network.FlowApiRequests;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProfileCourseFragment extends Fragment implements LoaderManager.LoaderCallbacks<UserCourseDetail>{
    private String mProfileID;

    protected ExpandableListView mCoursesListView;
    protected View rootView;
    protected ProfileCoursesAdapter profileListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mProfileID = getArguments() != null ? getArguments().getString(Constants.PROFILE_ID_KEY) : null;

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.profile_course_layout, container, false);
        mCoursesListView = (ExpandableListView)rootView.findViewById(R.id.course_list);

        if (mProfileID == null) {
            getLoaderManager().initLoader(Constants.LoaderManagerId.PROFILE_COURSES_LOADER_ID, null, this);
        } else {
            fetchCourses(mProfileID);
        }

        return rootView;
    }

    @Override
    public Loader<UserCourseDetail> onCreateLoader(int i, Bundle bundle) {
        return new UserCoursesLoader(getActivity(), ((MainFlowActivity)getActivity()).getHelper());
    }



    @Override
    public void onLoadFinished(Loader<UserCourseDetail> listLoader, UserCourseDetail userCourseDetail) {

        profileListAdapter = new ProfileCoursesAdapter(
                userCourseDetail.getCourses(),
                getActivity(),
                getActivity().getSupportFragmentManager());
        mCoursesListView.setAdapter(profileListAdapter);
    }

    @Override
    public void onLoaderReset(Loader<UserCourseDetail> listLoader) {
        //mCoursesListView.setAdapter(null);
    }


    private void fetchCourses(String id){
        if (id == null) return;

        FlowApiRequests.getUserCourses(
                id,
                new FlowApiRequestCallbackAdapter() {
                    @Override
                    public void getUserCoursesCallback(UserCourseDetail userCourseDetail) {

                        profileListAdapter = new ProfileCoursesAdapter(
                                userCourseDetail.getCourses(),
                                getActivity(),
                                getActivity().getSupportFragmentManager());
                        mCoursesListView.setAdapter(profileListAdapter);
                        profileListAdapter.notifyDataSetChanged();
                    }
                });
    }


}
