package com.multipz.bloodbook.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.multipz.bloodbook.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BloodTypeCompabilityFragment extends Fragment {


    public BloodTypeCompabilityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blood_type_compability, container, false);
        getActivity().setTitle("Blood Type Compatibility");

        return view;
    }
    

}
