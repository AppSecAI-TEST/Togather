package com.togather.locationService;

import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.database.DatabaseError;
import com.togather.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ander on 2017-08-10.
 */

public class LocationService {

    public static final String TAG = "LocationService";

    public static GeoQuery getNearbyUsers(GeoFire geofire, GeoLocation geoLocation, Double distance) {
        return geofire.queryAtLocation(geoLocation, distance);
    }

    public static GeoQueryEventListener geoQueryEventListener = new GeoQueryEventListener() {
        List<String> nearbyUsers = new ArrayList<>();

        @Override
        public void onKeyEntered(String key, GeoLocation location) {
            nearbyUsers.add(key);
            Log.d(TAG, "Nearby users: " + nearbyUsers);
        }

        @Override
        public void onKeyExited(String key) {

        }

        @Override
        public void onKeyMoved(String key, GeoLocation location) {

        }

        @Override
        public void onGeoQueryReady() {

        }

        @Override
        public void onGeoQueryError(DatabaseError error) {

        }
    };
}
