package com.example.sprintproject.views;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class TravelCommunityHelper {

    private static final String TAG = "TravelCommunityHelper";

    public interface TravelPostsCallback {
        void onSuccess(ArrayList<Map<String, Object>> travelPosts);
        void onFailure(Exception e);
    }

    /**
     * Fetches travel posts from Firestore and returns them via a callback.
     *
     * @param db       the Firestore instance
     * @param callback the callback to handle the fetched data
     */
    public static void fetchTravelPosts(FirebaseFirestore db, TravelPostsCallback callback) {
        db.collection("travelCommunity")
                .orderBy("startDate")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Map<String, Object>> travelPosts = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> post = document.getData();
                            post.putIfAbsent("isBoosted", false); // Default value
                            post.putIfAbsent("userEmail", "Unknown"); // Default value
                            travelPosts.add(post);
                        }

                        // Sort by startDate
                        travelPosts.sort((post1, post2) -> {
                            try {
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                Date date1 = formatter.parse((String) post1.get("startDate"));
                                Date date2 = formatter.parse((String) post2.get("startDate"));
                                return date1.compareTo(date2);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return 0;
                            }
                        });

                        callback.onSuccess(travelPosts);
                    } else {
                        Log.w(TAG, "Error fetching travel posts.", task.getException());
                        callback.onFailure(task.getException());
                    }
                });
    }
}
