package e.android9ed.malagaparking;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ListView listaParkings;
    private ArrayList<Parking> list_parking;
    private GoogleMap map;
    private final int  MAPS_PERMISSIONS = 20;
    private Location deviceLocation;
    private
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Intent intent = getIntent();
        list_parking = (ArrayList<Parking>) intent.getExtras().get("listaParkings");

        listaParkings = findViewById(R.id.lvParkings);

        ParkingAdapter adapter = new ParkingAdapter(getApplicationContext(), R.layout.parking_item, list_parking);
        listaParkings.setAdapter(adapter);
        listaParkings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setMarker((Parking) parent.getItemAtPosition(position));
            }
        });

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Sin permisos", Toast.LENGTH_SHORT).show();

            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, MAPS_PERMISSIONS);
        } else {
            Toast.makeText(this, "Permisos concedidos", Toast.LENGTH_SHORT).show();
        }
    }

    private void setMarker(Parking parking){
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
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permisos concedidos",
                            Toast.LENGTH_LONG).show();

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
                } else
                    Toast.makeText(this,
                            "SIN permisos", Toast.LENGTH_LONG).show();

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
                Location greenRay = new Location("Green Ray");
                greenRay.setLatitude(36.71853911463124);
                greenRay.setLongitude(-4.496980905532837);
                Parking masCercano = list_parking.get(0);
                Location locationParking = new Location("ParkingActual");
                Location locationCercano = new Location("MasCercano");
                locationCercano.setLatitude(masCercano.getLatitude());
                locationCercano.setLongitude(masCercano.getLongitude());
                for (Parking parking :
                        list_parking) {
                    locationParking.setLatitude(parking.getLatitude());
                    locationParking.setLongitude(parking.getLongitude());
                    if (greenRay.distanceTo(locationParking) < greenRay.distanceTo(locationCercano)){
                        masCercano = parking;
                    }
                }
                setMarker(masCercano);
                break;
        }
    }
}
