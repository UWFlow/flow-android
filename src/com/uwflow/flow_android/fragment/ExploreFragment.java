package com.uwflow.flow_android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.adapters.SearchResultAdapter;
import com.uwflow.flow_android.db_object.Course;
import com.uwflow.flow_android.db_object.SearchResults;
import com.uwflow.flow_android.network.FlowApiRequestCallbackAdapter;
import com.uwflow.flow_android.network.FlowApiRequests;

import java.util.*;

/**
 * Created by wentaoji on 2014-02-18.
 */
public class ExploreFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "ExploreFragment";
    private static final int ITEMS_PER_PAGE = 20;

    // TODO(david): Convert all the search stuff to use a SearchView, which gets us things like suggestions
    //     See http://developer.android.com/guide/topics/search/search-dialog.html
    private EditText mSearchBox;
    private Spinner mSortSpinner;
    private CheckBox mIncludeTakenCheckBox;
    private ListView mResultsListView;

    private List<Course> mSearchResultList;

    private SearchResultAdapter mSearchResultAdapter;

    private static final Map<String, String> mSortModesMap;
    static {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("Popular", "popular");
        map.put("Friends taken", "friends_taken");
        map.put("Interesting", "interesting");
        map.put("Easy", "easy");
        map.put("Hard", "hard");
        map.put("Course code", "course code");
        mSortModesMap = Collections.unmodifiableMap(map);
    }

    private ArrayAdapter<CharSequence> mSortModesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO(david): Restore search results from bundle (don't re-fetch when pressing back)

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.explore_layout, container, false);
        mSearchBox = (EditText)rootView.findViewById(R.id.search_box);
        mSortSpinner = (Spinner)rootView.findViewById(R.id.sort_spinner);
        mIncludeTakenCheckBox = (CheckBox)rootView.findViewById(R.id.checkbox_include_taken);
        mResultsListView = (ListView)rootView.findViewById(R.id.results_list);

        mSearchResultList = new ArrayList<Course>();
        mSearchResultAdapter = new SearchResultAdapter(mSearchResultList, getActivity());
        mResultsListView.setAdapter(mSearchResultAdapter);
        mResultsListView.setOnItemClickListener(this);

        // Infinite scroll: load next page of results when scrolled towards the end of the list.
        mResultsListView.setOnScrollListener(new InfiniteScrollListener() {
            @Override
            public void onLoadMore(int page) {
                doSearch(page);
            }
        });

        // Populate the sort spinner
        mSortModesAdapter = new ArrayAdapter <CharSequence>(getActivity(), android.R.layout.simple_spinner_item,
                mSortModesMap.keySet().toArray(new CharSequence[0]));
        mSortModesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSortSpinner.setAdapter(mSortModesAdapter);

        // Do search on sort mode change
        mSortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                doSearch(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e(TAG, "No sort mode selected. This should never happen.");
            }
        });

        // Do search on checkbox include/exclude courses taken change
        mIncludeTakenCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                doSearch(0);
            }
        });

        // Do search on edit text submit
        mSearchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch(0);
                    return true;
                }
                return false;
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        // TODO(david): Add a "fetching results..." empty state here
        doSearch(0);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Course course = (Course)parent.getItemAtPosition(position);
        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, CourseFragment.newInstance(course.getId()))
                .addToBackStack(null)
                .commit();
    }

    /**
     * Performs a search with parameters set on the UI.
     * @param page The page to load. Pass 0 if loading search results for a different query/filters.
     */
    private void doSearch(final int page) {
        // TODO(david): We should first gray-out results and show a spinner

        // Get keywords
        Editable searchBoxText = mSearchBox.getText();
        String keywords = "";
        if (searchBoxText != null) {
            keywords = searchBoxText.toString();
        }

        // Get sort mode
        Object sortItem = mSortSpinner.getSelectedItem();
        String sortMode = sortItem == null ? "" : mSortModesMap.get(sortItem.toString());

        // Get whether we should exclude taken courses
        // TODO(david): Change this checkbox to say "exclude courses I've taken" like on the web app
        boolean excludeTakenCourses = !mIncludeTakenCheckBox.isChecked();

        FlowApiRequests.searchCourses(keywords, sortMode, excludeTakenCourses, ITEMS_PER_PAGE, page * ITEMS_PER_PAGE,
                new FlowApiRequestCallbackAdapter() {
            @Override
            public void searchCoursesCallback(SearchResults searchResults) {
                if (page == 0) {
                    mSearchResultList.clear();
                }
                mSearchResultList.addAll(searchResults.getCourses());
                mSearchResultAdapter.notifyDataSetChanged();

                if (page == 0) {
                    mResultsListView.setSelectionAfterHeaderView();
                }
            }
        });
    }

    // Adapted from http://guides.thecodepath.com/android/Endless-Scrolling-with-AdapterViews
    private abstract class InfiniteScrollListener implements AbsListView.OnScrollListener {
        private final int remainingThreshold = 5;
        private int currentPage = 0;
        private int currentTotalItems = 0;
        private boolean loading = true;

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            // If total items has somehow gone down, then search results were probably refreshed.
            if (totalItemCount < currentTotalItems) {
                currentPage = 0;
                currentTotalItems = totalItemCount;
                if (totalItemCount == 0) {
                    loading = true;
                }
            }

            // See if loading has just completed.
            if (loading && totalItemCount > currentTotalItems) {
                currentTotalItems = totalItemCount;
                currentPage++;
                loading = false;
            }

            // If we're not already loading, see if the remaining items below the fold is about to exceed the threshold.
            if (!loading && totalItemCount - (firstVisibleItem + visibleItemCount) <= remainingThreshold) {
                onLoadMore(currentPage + 1);
                loading = true;
            }
        }

        public abstract void onLoadMore(int page);
    }
}
