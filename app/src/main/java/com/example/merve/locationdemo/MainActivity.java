package com.example.merve.locationdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView tvLocation;
    LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);



    }

    LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
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

    private void showLocation(Location location) {

        if(location == null)
            tvLocation.setText("Konum verisi bulunamadı");
        else
            tvLocation.setText(
                    "Provider: " + location.getProvider()
                            + "\nBearing:" + location.getBearing()
                            + "\nAltitude:" + location.getAltitude()
                            + "\nLatitude:" + location.getLatitude()
                            + "\nLongitude:" + location.getLongitude()
                            + "\nSpeed:" + location.getSpeed()
                            + "\nTime:" + location.getTime()
                            + "\nAccuracy:" + location.getAccuracy());
    }

    public void startGPS(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, listener);
        else
        {
            Toast.makeText(MainActivity.this, "Konum ayarlarınız kapalı lütfen açınız.", Toast.LENGTH_SHORT).show();
            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);

        }
    }

    public void startNetwork(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, listener);
    }

    public void stopListener(View view) {
        locationManager.removeUpdates(listener);
        tvLocation.setText("");
    }

    @Override
    protected void onPause() {
        if(locationManager != null)
            locationManager.removeUpdates(listener);
        super.onPause();
    }

    public void showLastKnownLocation(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        showLocation( locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            { //GPS i calistir
                startGPS(null);
            }
            else
                Toast.makeText(MainActivity.this, "Konum ayarlarını açmadan bu özelliği kullanamazsınız!", Toast.LENGTH_SHORT).show();

        }

    }
}
