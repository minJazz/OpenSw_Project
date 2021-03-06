package com.please.opensw_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

public class InfoActivity extends AppCompatActivity implements LocationListener {


    private DatabaseReference databaseReference;

    Location location;

    public static ArrayList<Product> information = new ArrayList<>();
    private Context mContext;
    LinearLayout listv2;
    String key;

    double latitude;
    double longitude;

    public static Location myLocation = new Location("myLocation");


    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        listv2 = findViewById(R.id.listv2);
        mContext = getApplicationContext();
        key = getIntent().getStringExtra("key");
        getLocation();

        ArrayList<String> mcodeList = (ArrayList<String>) MainActivity.infoList.get(key).get("mLocation");

        for (int i = 0; i < mcodeList.size(); i++) {
            System.out.println(MainActivity.location.get(mcodeList.get(i)));
        }

        ArrayList<String> locat = new ArrayList<>();

        ArrayList<Long> price = (ArrayList<Long>) MainActivity.infoList.get(key).get("price");


        //?????? ????????? ????????? ??????.
        for (int i = 0; i < mcodeList.size(); i++) {
            locat.add(MainActivity.location.get(mcodeList.get(i)).get("location"));
        }

        information.clear();
        //????????? ?????? ??????
        for (int i = 0; i < mcodeList.size(); i++) {
            Product p = new Product(
                    locat.get(i)
                    , price.get(i)
                    , Double.parseDouble(MainActivity.location.get(mcodeList.get(i)).get("x"))
                    , Double.parseDouble(MainActivity.location.get(mcodeList.get(i)).get("y")));
            information.add(p);

        }

        Collections.sort(information, new infoSort());

        for (int i = 0; i < information.size(); i++) {

            listv2.addView(addProductView(information.get(i)));
        }
    }

    public LinearLayout addProductView(Product p) {
        /*
             info key = price, mLocation / value ?????? (mLocation value ?????? addr map??? ?????????.)
         */
        String ??????;
        Long ??????;

        float ?????? = p.distance;
        String ??????2 = new String("" + (p.distance / 1000));
        ??????2 = ??????2.substring(0, 5);
        ??????2 += "Km";
        if (?????? > 10000000) {
            ??????2 = "??????";
        }

        ?????? = p.mLocation;
        ?????? = p.price;

        LinearLayout v = new LinearLayout(mContext);

        v.setPadding(40, 40, 40, 40);
        MainActivity.params1.weight = 1f;
        MainActivity.params3.weight = 3f;

        TextView t?????? = new TextView(mContext);
        t??????.setLayoutParams(MainActivity.params1);
        t??????.setGravity(Gravity.CENTER);

        TextView t?????? = new TextView(mContext);
        t??????.setLayoutParams(MainActivity.params3);
        t??????.setPadding(10, 0, 0, 0);


        TextView t?????? = new TextView(mContext);
        t??????.setLayoutParams(MainActivity.params1);
        t??????.setGravity(Gravity.CENTER);

        t??????.setText(??????2);
        t??????.setText(??????);
        t??????.setText(??????.toString() + "???");

        v.addView(t??????);
        v.addView(t??????);
        v.addView(t??????);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Map.class);
                intent.putExtra("latitude", p.latitude);
                intent.putExtra("longitude", p.longitude);
                startActivity(intent);
            }
        });
        return v;
    }

    public Location getLocation() {
        try {
            LocationManager locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {

                int hasFineLocationPermission = ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION);


                if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                        hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

                    ;
                } else
                    return null;


                if (isNetworkEnabled) {


                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }


                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.d("@@@", "" + e.toString());
        }
        myLocation.setLatitude(latitude);
        myLocation.setLongitude(longitude);

        return location;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
}

class infoSort implements Comparator<Product> {

    @Override
    public int compare(Product o1, Product o2) {
        if (o1.distance > o2.distance) return 1;
        if (o1.distance < o2.distance) return -1;

        return 0;
    }
}