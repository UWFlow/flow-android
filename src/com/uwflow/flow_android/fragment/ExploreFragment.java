package com.uwflow.flow_android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.adapters.SearchResultAdapter;
import com.uwflow.flow_android.db_object.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wentaoji on 2014-02-18.
 */
public class ExploreFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final int SORT_POPULAR = 0;
    private static final int SORT_FRIENDS_TAKEN = 1;
    private static final int SORT_EASY = 2;
    private static final int SORT_HARD = 3;
    private static final int SORT_COURSE_CODE = 4;

    private EditText mSearchBox;
    private Spinner mSortSpinner;
    private CheckBox mIncludeTakenCheckBox;
    private ListView mResultsListView;

    private View rootView;

    private List<Course> mSearchResultList;

    private SearchResultAdapter mSearchResultAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.explore_layout, container, false);
        mSearchBox = (EditText)rootView.findViewById(R.id.search_box);
        mSortSpinner = (Spinner)rootView.findViewById(R.id.sort_spinner);
        mIncludeTakenCheckBox = (CheckBox)rootView.findViewById(R.id.checkbox_include_taken);
        mResultsListView = (ListView)rootView.findViewById(R.id.results_list);

        mSearchResultList = new ArrayList<Course>();
        mSearchResultAdapter = new SearchResultAdapter(mSearchResultList, getActivity());
        mResultsListView.setAdapter(mSearchResultAdapter);

        mSortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO: implement result ordering / re-searching here, based on new sort preference
                switch (position) {
                    case SORT_POPULAR:
                        break;
                    case SORT_FRIENDS_TAKEN:
                        break;
                    case SORT_EASY:
                        break;
                    case SORT_HARD:
                        break;
                    case SORT_COURSE_CODE:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mIncludeTakenCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO: implement result hiding / re-searching here, based on isChecked state
            }
        });

        Button searchButton = (Button)rootView.findViewById(R.id.temp_search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchString = mSearchBox.getText().toString();

                // TODO: fetch results for searchString, then save List<Course> result into mSearchResultList and call mSearchResultAdapter.notifyDataSetChanged()
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        // TEMPORARY while the login isn't working:
        Button openCourseButton = (Button) getView().findViewById(R.id.btn_open_course);
        openCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create new CourseFragment for clicked course
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new CourseFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO: open course page by passing CourseFragment a Bundle with the course_id specified
    }
}
