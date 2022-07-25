package br.com.geizon.wagner.wit_weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class WeatherAdapter extends ArrayAdapter<Weather> {

    private final Context context;
    private final List<Weather> elementos;

    public WeatherAdapter(Context context, ArrayList<Weather> elementos) {
        super(context, R.layout.list_weather, elementos);
        this.context = context;
        this.elementos = elementos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_weather, parent, false);
        TextView local, clima, temperatura, max, min;
        ImageView img_clima = rowView.findViewById(R.id.img_climaLista);

        LinearLayout layout;

        layout = (LinearLayout) rowView.findViewById(R.id.llTemperatura);
        local = (TextView) rowView.findViewById(R.id.txtLocallist);
        clima = (TextView) rowView.findViewById(R.id.txtClimalist);
        temperatura = (TextView) rowView.findViewById(R.id.txtTemperaturaList);
        max = (TextView) rowView.findViewById(R.id.txtMaxList);
        min = (TextView) rowView.findViewById(R.id.txtMinList);

        local.setText(elementos.get(position).getLocal());
        clima.setText(elementos.get(position).getClima());
        temperatura.setText(elementos.get(position).getTemperatura());
        max.setText(elementos.get(position).getTempmax());
        min.setText(elementos.get(position).getTempmin());

        Picasso.get().load(elementos.get(position).getImagemclima()).into(img_clima);

        return rowView;

    }


}
