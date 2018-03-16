package com.rajramchandani.hikersapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView latitude;
    TextView lontitude;
    TextView accuracy;
    TextView address;
    TextView altitude;
     String add="";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.length > 0) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            }
        }
    }

    public void updatelocation(Location location)
    {
        add="";
        latitude.setText("LATITUDE: "+Double.toString((double) location.getLatitude()));
        lontitude.setText("LONGITUDE: "+Double.toString((double) location.getLongitude()));
        altitude.setText("ALTITUDE: "+Double.toString((double) location.getAltitude()));
        accuracy.setText("ACCURACY: "+Double.toString((double) location.getAccuracy()));

        //used for getting address only
        Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());


        try {
            List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if(addresses!=null && addresses.size()>0)
            {
                if(addresses.get(0).getSubThoroughfare()!=null)
                {
                    add+=addresses.get(0).getSubThoroughfare()+", ";
                }
                if(addresses.get(0).getThoroughfare()!=null)
                {
                    add+=addresses.get(0).getThoroughfare()+", ";
                }
                if(addresses.get(0).getSubLocality()!=null)
                {
                    add+=addresses.get(0).getSubLocality()+", ";
                }
                if(addresses.get(0).getAddressLine(1)!=null)
                {
                    add+=addresses.get(0).getAddressLine(1)+", ";
                }
                if(addresses.get(0).getSubAdminArea()!=null)
                {
                    add+=addresses.get(0).getSubAdminArea()+", ";
                }
                if(addresses.get(0).getLocality()!=null)
                {
                    add+=addresses.get(0).getLocality()+", ";
                }
                if(addresses.get(0).getAdminArea()!=null)
                {
                    add+=addresses.get(0).getAdminArea()+", ";
                }

                if(addresses.get(0).getCountryName()!=null)
                {
                    add+=addresses.get(0).getCountryName()+", ";
                }
                if(addresses.get(0).getPostalCode()!=null)
                {
                    add+=addresses.get(0).getPostalCode();
                }
            }

            address.setText("ADDRESS:\n "+add.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         latitude=(TextView)findViewById(R.id.latitude);
         lontitude=(TextView)findViewById(R.id.longtitude);
         accuracy=(TextView)findViewById(R.id.Accuracy);
         address=(TextView)findViewById(R.id.Address);
         altitude=(TextView)findViewById(R.id.Altitude);



        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("Location", location.toString());
                updatelocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        else
        {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }

            else
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                //identifying users current location when app gets started

                Location last=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                //latitude.setText(Integer.toString((int) last.getLatitude()));
                updatelocation(last);



            }

        }



    }
}
