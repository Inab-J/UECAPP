package net.inab_j.uecapp.view.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.inab_j.uecapp.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SyllabusActivityFragment extends Fragment {

    public SyllabusActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_syllabus, container, false);
    }
}
