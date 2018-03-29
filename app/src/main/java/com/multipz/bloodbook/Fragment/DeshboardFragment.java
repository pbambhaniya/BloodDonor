package com.multipz.bloodbook.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.multipz.bloodbook.Activity.ActiveDonersActivity;
import com.multipz.bloodbook.Activity.CurrentBloodRequestActivity;
import com.multipz.bloodbook.Activity.DonateBloodActivity;
import com.multipz.bloodbook.Activity.GetRequestAcceptedListActivity;
import com.multipz.bloodbook.Activity.MyDonationHistory;
import com.multipz.bloodbook.Activity.RequestAcceptedActivity;
import com.multipz.bloodbook.Activity.RequestBloodActivity;
import com.multipz.bloodbook.Activity.SaveBloodRequestActivity;
import com.multipz.bloodbook.Activity.SaveLaterAcceptActivity;
import com.multipz.bloodbook.Activity.TotalBloodHistory;
import com.multipz.bloodbook.R;
import com.multipz.bloodbook.Utils.Application;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeshboardFragment extends Fragment {

    private CardView cv_req_blood, cv_donate_blood, cv_current_blood, cv_save_blood, cv_current_donor, cv_news, cv_my_donate_history, cv_total_donate_history;

    public DeshboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_deshboard, container, false);
        getActivity().setTitle("Dashboard");
        cv_req_blood = (CardView) view.findViewById(R.id.cv_req_blood);
        cv_donate_blood = (CardView) view.findViewById(R.id.cv_donate_blood);
        cv_current_blood = (CardView) view.findViewById(R.id.cv_current_blood);
        cv_save_blood = (CardView) view.findViewById(R.id.cv_save_blood);
        cv_current_donor = (CardView) view.findViewById(R.id.cv_current_donor);
        cv_news = (CardView) view.findViewById(R.id.cv_news);
        cv_my_donate_history = (CardView) view.findViewById(R.id.cv_my_donate_history);
        cv_total_donate_history = (CardView) view.findViewById(R.id.cv_total_donate_history);
        Application.setFontDefault((RelativeLayout) view.findViewById(R.id.rel_root));


        cv_req_blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RequestBloodActivity.class);
                startActivity(intent);
            }
        });

        cv_donate_blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DonateBloodActivity.class);
                startActivity(intent);

            }
        });
        cv_current_blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CurrentBloodRequestActivity.class);
                startActivity(intent);
            }
        });
        cv_current_donor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActiveDonersActivity.class);
                startActivity(intent);
            }
        });
        cv_save_blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SaveBloodRequestActivity.class);
                startActivity(intent);
            }
        });
        cv_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GetRequestAcceptedListActivity.class);
                startActivity(intent);
            }
        });
        cv_my_donate_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyDonationHistory.class);
                startActivity(intent);
            }
        });
        cv_total_donate_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TotalBloodHistory.class);
                startActivity(intent);
            }
        });


        return view;
    }

}
