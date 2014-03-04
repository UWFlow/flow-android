package com.uwflow.flow_android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.uwflow.flow_android.MainFlowActivity;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.adapters.ProfileExamAdapter;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.db_object.Exam;
import com.uwflow.flow_android.loaders.UserExamsLoader;

import java.util.List;

public class ProfileExamFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Exam>> {

    protected ListView mExamsList;
    protected TextView mLastUpdatedText;
    protected View rootView;
    protected ProfileExamAdapter profileExamAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.profile_exam_layout, container, false);
        mExamsList = (ListView)rootView.findViewById(R.id.exam_list);
        mLastUpdatedText = (TextView)rootView.findViewById(R.id.text_last_updated);

	mExamsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO: fire intent to add the exam to the users calendar
		Exam exam = (Exam)profileExamAdapter.getItem(position);
	    }
	});

        getLoaderManager().initLoader(Constants.LoaderManagerId.PROFILE_EXAMS_LOADER_ID, null, this);
        return rootView;
    }

    @Override
    public Loader<List<Exam>> onCreateLoader(int i, Bundle bundle) {
        return new UserExamsLoader(getActivity(), ((MainFlowActivity)getActivity()).getHelper());
    }

    @Override
    public void onLoadFinished(Loader<List<Exam>> listLoader, List<Exam> exams) {
        profileExamAdapter = new ProfileExamAdapter(exams, getActivity());
        mExamsList.setAdapter(profileExamAdapter);
    }

    @Override
    public void onLoaderReset(Loader<List<Exam>> listLoader) {
        mExamsList.setAdapter(null);
    }
}
