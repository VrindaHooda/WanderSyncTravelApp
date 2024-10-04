package com.example.sprintproject.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sprintproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavigationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavigationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NavigationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ButtonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NavigationFragment newInstance(String param1, String param2) {
        NavigationFragment fragment = new NavigationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_button, container, false);

        // Find the button in the layout
        Button logisticsButton = view.findViewById(R.id.logistics);

        // Set an onClickListener to the button
        logisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to open the LogisticsActivity
                Intent intent = new Intent(getActivity(), LogisticsActivity.class);
                startActivity(intent);
            }
        });

        Button transportationButton = view.findViewById(R.id.transportation);

        // Set an onClickListener to the button
        transportationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to open the TransportationActivity
                Intent intent = new Intent(getActivity(), TransportationActivity.class);
                startActivity(intent);
            }
        });

        Button travelButton = view.findViewById(R.id.travel);

        // Set an onClickListener to the button
        travelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to open the TravelCommunityActivity
                Intent intent = new Intent(getActivity(), TravelCommunityActivity.class);
                startActivity(intent);
            }
        });

        Button destinationButton = view.findViewById(R.id.destination);

        // Set an onClickListener to the button
        destinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to open the DestinationActivity
                Intent intent = new Intent(getActivity(), DestinationActivity.class);
                startActivity(intent);
            }
        });

        Button accommodationsButton = view.findViewById(R.id.accommodations);

        // Set an onClickListener to the button
        accommodationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to open the AccommodationsActivity
                Intent intent = new Intent(getActivity(), AccomodationActivity.class);
                startActivity(intent);
            }
        });

        Button diningButton = view.findViewById(R.id.dining);

        // Set an onClickListener to the button
        diningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to open the DiningActivity
                Intent intent = new Intent(getActivity(), DiningActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
