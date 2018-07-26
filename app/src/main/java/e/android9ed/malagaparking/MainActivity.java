package e.android9ed.malagaparking;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Data position in CSV file.
    private final int ID = 0;
    private final int NAME = 1;
    private final int ADRESS = 2;
    private final int LATITUDE = 5;
    private final int LONGITUDE = 6;
    private final int CAPACITY = 8;
    private final int LAST_UPDATE = 10;
    private final int FREE_PARKS = 11;

    TextView lastUpdate;

    ArrayList<Parking> list_parking = new ArrayList<>();
    private  URL url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lastUpdate = findViewById(R.id.txtUltimaAct);

        try {
            url = new URL("http://datosabiertos.malaga.eu/recursos/aparcamientos/ocupappublicosmun/ocupappublicosmun.csv");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        loadParking();
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btGrafico:

                break;
            case R.id.btMaps:
                Intent intent = new Intent(this, MapActivity.class);
                intent.putExtra("listaParkings", list_parking);
                startActivity(intent);
                break;
            case R.id.btLoad:
                loadParking();
                break;
        }
    }

    private void loadParking(){

        new DownloadData().execute(url);
//        Parking salitre = new Parking(1,"Salitre");
//        Parking cervantes = new Parking(2,"Cervantes");
//        Parking elpalo = new Parking(3,"El Palo");
//        Parking andalucia = new Parking(4,"Av. de Andalucía");
//        Parking camas = new Parking(5,"Camas");
//        Parking alcazaba = new Parking(6,"Alcazaba");
//        salitre.setDir("Calle Salitre"); salitre.setLatitude(36.7132149);
//        salitre.setLongitude(-4.4276681);salitre.setCapacidad(342);
//        salitre.setFechaAct("25/07/17 16:00"); salitre.setLibres(308);
//        cervantes.setDir("Calle Cervantes"); cervantes.setLatitude(36.7208633);
//        cervantes.setLongitude(-4.4119148);cervantes.setCapacidad(414);
//        cervantes.setFechaAct("25/07/17 16:00"); cervantes.setLibres(349);
//        elpalo.setDir("Calle Alonso Carrillo De Albornoz");
//        elpalo.setLatitude(36.7210350); elpalo.setLongitude(-4.3607192);
//        elpalo.setCapacidad(129); elpalo.setFechaAct("25/07/17 16:00");
//        elpalo.setLibres(93);
//        andalucia.setDir("Avenida De Andalucía");
//        andalucia.setLatitude(36.7171364); andalucia.setLongitude(-4.4277549);
//        andalucia.setCapacidad(517); andalucia.setFechaAct("25/07/17 16:00");
//        andalucia.setLibres(495);
//        camas.setDir("Calle Camas"); camas.setLatitude(36.7193031);
//        camas.setLongitude(-4.4249320);camas.setCapacidad(308);
//        camas.setFechaAct("25/07/17 16:00"); camas.setLibres(232);
//        alcazaba.setDir("Plaza La Alcazaba"); alcazaba.setLatitude(36.7224312);
//        alcazaba.setLongitude(-4.4165168); alcazaba.setCapacidad(344);
//        alcazaba.setFechaAct("25/07/17 16:00"); alcazaba.setLibres(96);
//        list_parking.add(salitre);
//        list_parking.add(cervantes);
//        list_parking.add(elpalo);
//        list_parking.add(andalucia);
//        list_parking.add(camas);
//        list_parking.add(alcazaba);
    }

    private class DownloadData extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            CSVReader reader;
            BufferedReader input = null;
            Parking parking = null;
            try {
                input = new BufferedReader(new InputStreamReader(url.openStream()));
                reader = new CSVReader(input);
                String[] line = reader.readNext();
                list_parking = new ArrayList<>();
                while ((line = reader.readNext()) != null) {
                    try {

                        parking = new Parking(Integer.parseInt(line[ID]), line[NAME]);
                        parking.setDir(line[ADRESS]);
                        parking.setLatitude(Double.parseDouble(line[LATITUDE]));
                        parking.setLongitude(Double.parseDouble(line[LONGITUDE]));
                        parking.setCapacidad(Integer.parseInt(line[CAPACITY]));
                        parking.setLibres(Integer.parseInt(line[FREE_PARKS]));
                        parking.setFechaAct(line[LAST_UPDATE]);
                        Log.d("DownloadedData", parking.toString());
                        list_parking.add(parking);
                    } catch (NumberFormatException e){
                        Log.e("IncompleteData", parking.toString());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return list_parking.get(0).getFechaAct();
        }

        @Override
        protected void onPostExecute(String s) {
            lastUpdate.setText(s);
        }
    }
}
