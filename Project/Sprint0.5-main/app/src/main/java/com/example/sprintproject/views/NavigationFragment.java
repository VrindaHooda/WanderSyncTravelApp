package com.example.sprintproject.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.example.sprintproject.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavigationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavigationFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    /**
     * Default constructor for the {@code NavigationFragment} class.
     *
     * This constructor is required for the Android framework to instantiate
     * the fragment. It is intentionally left empty as no custom initialization
     * is needed during instantiation.
     */
    public NavigationFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ButtonFragment.
     */
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
        View view = inflater.inflate(R.layout.navigation_bar, container, false);

        ImageButton logisticsButton = view.findViewById(R.id.logistics);
        logisticsButton.setOnClickListener(v -> {
            if (getActivity() instanceof DestinationActivity) {
                ((DestinationActivity) getActivity()).navigateToLogisticsActivity();
            }
        });

        ImageButton logoutButton = view.findViewById(R.id.logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out the user
                FirebaseAuth.getInstance().signOut();

                // Redirect to the login screen
                Intent intent = new Intent(getActivity(), Login.class);
                if (getActivity() != null) {
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }
            }
        });


        ImageButton transportationButton = view.findViewById(R.id.transportation);
        transportationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TransportationActivity.class);
                assert getActivity() != null;
                getActivity().startActivity(intent);
            }
        });

        ImageButton travelButton = view.findViewById(R.id.travel);
        travelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TravelCommunityActivity.class);
                assert getActivity() != null;
                getActivity().startActivity(intent);
            }
        });

        ImageButton destinationButton = view.findViewById(R.id.destination);
        destinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DestinationActivity.class);
                assert getActivity() != null;
                getActivity().startActivity(intent);
            }
        });

        ImageButton accommodationsButton = view.findViewById(R.id.accommodations);
        accommodationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AccommodationActivity.class);
                assert getActivity() != null;
                getActivity().startActivity(intent);
            }
        });

        ImageButton diningButton = view.findViewById(R.id.dining);
        diningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DiningActivity.class);
                assert getActivity() != null;
                getActivity().startActivity(intent);
            }
        });
        return view;
    }
}
