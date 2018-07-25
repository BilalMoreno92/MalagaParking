package e.android9ed.malagaparking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {
    ListView listaParkings;
    ArrayList<Parking> list_parking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        list_parking = (ArrayList<Parking>) intent.getExtras().get("listaParkings");

        listaParkings = findViewById(R.id.lvParkings);

        ParkingAdapter adapter = new ParkingAdapter(getApplicationContext(), R.layout.parking_item, list_parking);
        listaParkings.setAdapter(adapter);
    }
}
