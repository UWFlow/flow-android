package com.uwflow.flow_android.fragment;

//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import com.uwflow.flow_android.R;

/**
 * Created by wentaoji on 2014-02-18.
 */
public class ExploreFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.explore_layout, container, false);
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
}
