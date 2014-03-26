package com.uwflow.flow_android.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.uwflow.flow_android.FlowApplication;
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
    private CheckBox mExcludeTakenCheckBox;
    private ListView mResultsListView;
    private View mFooterView;

    private boolean mSortSpinnerFired = false;
    private int mSortSpinnerPosition = -1;
    private boolean mExcludeTakenCheckBoxChecked = false;

    private List<Course> mSearchResultList = new ArrayList<Course>();

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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.explore_layout, container, false);
        mSearchBox = (EditText)rootView.findViewById(R.id.search_box);
        mSortSpinner = (Spinner)rootView.findViewById(R.id.sort_spinner);
        mExcludeTakenCheckBox = (CheckBox)rootView.findViewById(R.id.checkbox_exclude_taken);
        mResultsListView = (ListView)rootView.findViewById(R.id.results_list);

        mFooterView = inflater.inflate(R.layout.search_results_footer, null, false);
        mResultsListView.addFooterView(mFooterView);
        mSearchResultAdapter = new SearchResultAdapter(mSearchResultList, getActivity());
        mResultsListView.setAdapter(mSearchResultAdapter);
        mResultsListView.setOnItemClickListener(this);

        Set<String> sortModes = new LinkedHashSet<String>(mSortModesMap.keySet());

        // Hide various controls for logged out users
        boolean isUserLoggedIn = ((FlowApplication)getActivity().getApplication()).isUserLoggedIn();
        if (!isUserLoggedIn) {
            sortModes.remove("Friends taken");
            mExcludeTakenCheckBox.setVisibility(View.GONE);
        }

        // Populate the sort spinner
        mSortModesAdapter = new ArrayAdapter <CharSequence>(getActivity(), android.R.layout.simple_spinner_item,
                sortModes.toArray(new CharSequence[0]));
        mSortModesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSortSpinner.setAdapter(mSortModesAdapter);

        setUpListeners();

        return rootView;
    }

    private void setUpListeners() {
        mSortSpinnerFired = false;

        // Do search on sort mode change
        mSortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.white));

                // Android stupidly fires this listener on create of the spinner. This is used to ignore that.
                if (!mSortSpinnerFired) {
                    mSortSpinnerFired = true;
                    return;
                }

                // Item selection didn't actually change. This could happen when back button is pressed to return to
                // this fragment.
                if (mSortSpinnerPosition == position) {
                    return;
                }
                mSortSpinnerPosition = position;

                doSearch(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e(TAG, "No sort mode selected. This should never happen.");
            }
        });

        // Do search on checkbox include/exclude courses taken change
        mExcludeTakenCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Checkbox selection didn't actually change. This may happen when back button pressed to return to
                // this fragment.
                if (isChecked == mExcludeTakenCheckBoxChecked) {
                    return;
                }
                mExcludeTakenCheckBoxChecked = isChecked;

                doSearch(0);
            }
        });

        // Do search on edit text submit
        mSearchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch(0);

                    // Hide the keyboard. See http://stackoverflow.com/questions/1109022
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

        // Infinite scroll: load next page of results when scrolled towards the end of the list.
        mResultsListView.setOnScrollListener(new InfiniteScrollListener() {
            @Override
            public void onLoadMore(int page) {
                doSearch(page);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        if (mSearchResultList.isEmpty()) {
            doSearch(0);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object item = parent.getItemAtPosition(position);
        if (item == null) {
            return;
        }

        Course course = (Course) item;
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
        mFooterView.setVisibility(View.VISIBLE);

        if (page == 0) {
            mResultsListView.setAlpha((float)0.4);
        }

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
        boolean excludeTakenCourses = mExcludeTakenCheckBox.isChecked();

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

                mResultsListView.setAlpha((float)1.0);
                mFooterView.setVisibility(View.GONE);
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
            int totalItemsLessFooter = totalItemCount - 1;

            // If total items has somehow gone down, then search results were probably refreshed.
            if (totalItemsLessFooter< currentTotalItems) {
                currentPage = 0;
                currentTotalItems = totalItemsLessFooter;
                if (totalItemsLessFooter == 0) {
                    loading = true;
                }
            }

            // See if loading has just completed.
            if (loading && totalItemsLessFooter > currentTotalItems) {
                currentTotalItems = totalItemsLessFooter;
                currentPage++;
                loading = false;
            }

            // If we're not already loading, see if the remaining items below the fold is about to exceed the threshold.
            if (!loading && totalItemsLessFooter - (firstVisibleItem + visibleItemCount) <= remainingThreshold) {
                onLoadMore(currentPage + 1);
                loading = true;
            }
        }

        public abstract void onLoadMore(int page);
    }
}
