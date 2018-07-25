package e.android9ed.malagaparking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ParkingAdapter extends BaseAdapter {

    Context context;
    int recurso;
    ArrayList<Parking> datos;

    public ParkingAdapter(Context context, int recurso, ArrayList<Parking> datos) {
        this.context = context;
        this.recurso = recurso;
        this.datos = datos;
    }

    @Override
    public int getCount() {
        return datos.size();
    }

    @Override
    public Object getItem(int position) {
        return datos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return datos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView id, nombre, libres;
        View view;
        Parking parking;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null){
            view = inflater.inflate(R.layout.parking_item, null);
        } else {
            view = (View) convertView;
        }

        id = view.findViewById(R.id.txtId);
        nombre = view.findViewById(R.id.txtNombre);
        libres = view.findViewById(R.id.txtLibres);

        parking = datos.get(position);

        id.setText(Integer.toString(parking.getId()));
        nombre.setText(parking.getNombre());
        libres.setText(Integer.toString(parking.getLibres()));

        return view;
    }
}
