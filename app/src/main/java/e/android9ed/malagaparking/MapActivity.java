package e.android9ed.malagaparking;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.URL;
import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    //Permissions request codes
    private final int  MAPS_PERMISSIONS = 20;

    //Views
    private ListView listaParkings;
    private GoogleMap map;


    //Util
    private ArrayList<Parking> list_parking;
    private Location deviceLocation;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //Get parkings list
        Intent intent = getIntent();
        list_parking = (ArrayList<Parking>) intent.getExtras().get("listaParkings");

        //Show parkings list
        listaParkings = findViewById(R.id.lvParkings);
        ParkingAdapter adapter = new ParkingAdapter(getApplicationContext(), R.layout.parking_item, list_parking);
        listaParkings.setAdapter(adapter);
        listaParkings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //Set marker on parking click
                setMarker((Parking) parent.getItemAtPosition(position));
            }
        });

        //Get location
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new DeviceLocationListener();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, MAPS_PERMISSIONS);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        }
        deviceLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void setMarker(Parking parking){ //Set a parking marker on the map
        map.clear();
        LatLng marker = new LatLng(parking.getLatitude(), parking.getLongitude());
        int zoom = 16;
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker, zoom));
        map.addMarker(new MarkerOptions().position(marker).title(parking.getNombre()).snippet(Integer.toString(parking.getLibres()) + " plazas libres."));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MAPS_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permisos concedidos", Toast.LENGTH_LONG).show();

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        findViewById(R.id.btNear).setVisibility(View.GONE);
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
                } else {
                    Toast.makeText(this, "SIN permisos", Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btNear:
                Parking masCercano = list_parking.get(0);
                Location locationParking = new Location("ParkingActual");
                Location locationCercano = new Location("MasCercano");
                locationCercano.setLatitude(masCercano.getLatitude());
                locationCercano.setLongitude(masCercano.getLongitude());
                for (Parking parking :
                        list_parking) {
                    locationParking.setLatitude(parking.getLatitude());
                    locationParking.setLongitude(parking.getLongitude());
                    if (deviceLocation.distanceTo(locationParking) < deviceLocation.distanceTo(locationCercano)){
                        masCercano = parking;
                    }
                }
                setMarker(masCercano);
                break;
        }
    }

    private class DeviceLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            deviceLocation = location;
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
    }
}
