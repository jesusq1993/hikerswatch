package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.hikerswatch.databinding.ActivityMainBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;

    LocationManager locationManager;
    LocationListener locationListener;

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.i("Location",location.toString());

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

                    if(listAddresses != null && listAddresses.size() > 0 ){

                        activityMainBinding.txvLatitude.setText("Latitude: "+Double.toString(listAddresses.get(0).getLatitude()));
                        activityMainBinding.txvLongitude.setText("Longitude: "+Double.toString(listAddresses.get(0).getLongitude()));
                        activityMainBinding.txvAccuracy.setText("Accuracy: "+Double.toString(location.getAccuracy()));
                        activityMainBinding.txvAltitude.setText("Altitude: "+Double.toString(location.getAltitude()));

                        String address = "";

                        //Street name
                        if(listAddresses.get(0).getThoroughfare() != null){
                            address += listAddresses.get(0).getThoroughfare() + "\n";
                        }

                        //City
                        if(listAddresses.get(0).getLocality() != null){
                            address += listAddresses.get(0).getLocality() + ", ";
                        }

                        //State
                        if(listAddresses.get(0).getAdminArea() != null){
                            address += listAddresses.get(0).getAdminArea() + ", ";
                        }

                        //Zipcode
                        if(listAddresses.get(0).getPostalCode() != null){
                            address += listAddresses.get(0).getPostalCode() + " ";
                        }

                        activityMainBinding.txvAddress.setText("Address:\n"+address);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

    }



}