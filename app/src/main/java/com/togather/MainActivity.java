package com.togather;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.togather.user.User;
import com.togather.locationService.LocationService;

public class MainActivity extends BaseActivity {

    private FusedLocationProviderClient mFusedLocationClient;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = auth.getCurrentUser();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference();
    private GeoFire geoFire = new GeoFire(database.getReference("locations"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TypefaceProvider.registerDefaultIconSets();

        loadUserData(ref, firebaseUser.getUid());

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }

        updateLocation();
    }

    @Override
    public void onRequestPermissionsResult ( int requestCode, String permissions[], int[] grantResults){
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    System.out.println("Permission granted");
                    updateLocation();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    System.out.println("Permission denied");
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public static void loadUserData(DatabaseReference ref, String uid) {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Togather.user = dataSnapshot.getValue(User.class);
                //loadProfile();
                //loadConversations();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        ref.child("users").child(uid).addValueEventListener(listener);
    }

    public void updateLocation() {
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // ...
                                GeoLocation geoLocation = new GeoLocation(location.getLatitude(), location.getLongitude());
                                geoFire.setLocation(firebaseUser.getUid(), geoLocation, new GeoFire.CompletionListener() {
                                    @Override
                                    public void onComplete(String key, DatabaseError error) {
                                        if (error != null) {
                                            System.err.println("There was an error saving the location to GeoFire: " + error);
                                        } else {
                                            System.out.println("Location saved on server successfully!");
                                        }
                                    }
                                });
                                GeoQuery query = LocationService.getNearbyUsers(geoFire, geoLocation, 15.0);
                                query.addGeoQueryEventListener(LocationService.geoQueryEventListener);
                            }
                        }
                    });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void updateLocation(View view) {
        updateLocation();
    }

    public void logout(View view) {
        auth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void loadProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}