package br.com.geizon.wagner.wit_weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ListWeather extends Activity {
    private final String url_img = "https://openweathermap.org/img/wn/";
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "25f62380376fd074b7fbd207b575e2e2";
    private String cidade, meulocal;
    private Intent intent;
    ImageView img_clima;
    LinearLayout llAtual;
    TextView local, temperatura, clima, tempmin, tempmax, sensacao, umidade, vento, presao, visibilidade;
    DecimalFormat df = new DecimalFormat("#");
    DecimalFormat df_desc = new DecimalFormat("#.###");

    private WeatherAdapter weatherAdapter;
    private final ArrayList<Weather> weathers = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_weather);

        weatherAdapter = new WeatherAdapter(this, weathers);
        llAtual = findViewById(R.id.ll_Local_Atual);
        intent = getIntent();

        local = findViewById(R.id.txtmeulocalLista);
        temperatura = findViewById(R.id.txtTemperaturaList);
        clima = findViewById(R.id.txtClimalist);
        tempmax = findViewById(R.id.txtMaxList);
        tempmin = findViewById(R.id.txtMinList);
        img_clima = findViewById(R.id.img_climaLocal);

        llAtual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getBaseContext(), MainActivity.class);
                it.putExtra("cicade", (String) intent.getSerializableExtra("cicade"));
                startActivity(it);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        meuLocal();
        carregaLista();

        ListView lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(weatherAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int position, long id) {

                Intent it = new Intent(getBaseContext(), MainActivity.class);
                it.putExtra("cicade", weathers.get(position).getCidade());
                startActivity(it);
            }
        });

    }

    private void carregaLista() {
        /* Lisbon, Madrid, Paris, Berlin, Copenhagen, Rome, London, Dublin, Prague, Vienna */

        for (int i = 0; i < 10; i++) {
            if (i == 0) {
                atualizaTempo("Lisbon");
            }
            if (i == 1) {
                atualizaTempo("Madrid");
            }
            if (i == 2) {
                atualizaTempo("Paris");
            }
            if (i == 3) {
                atualizaTempo("Berlin");
            }
            if (i == 4) {
                atualizaTempo("Copenhagen");
            }
            if (i == 5) {
                atualizaTempo("Rome");
            }
            if (i == 6) {
                atualizaTempo("London");
            }
            if (i == 7) {
                atualizaTempo("Dublin");
            }
            if (i == 8) {
                atualizaTempo("Prague");
            }
            if (i == 9) {
                atualizaTempo("Vienna");
            }
        }

    }

    private void atualizaTempo(String cidade) {
        // content.setBackground(ContextCompat.getDrawable(this, R.drawable.background));
        String tempUrl = url + "?q=" + cidade + "&appid=" + appid;
        //  String tempUrl = url + "?lat=" +lat + "&lon=" + lon+ "&appid=" + appid;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    String description = jsonObjectWeather.getString("description");
                    int id = jsonObjectWeather.getInt("id");
                    String icon = jsonObjectWeather.getString("icon");
                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    double temp = jsonObjectMain.getDouble("temp") - 273.15;
                    double temp_min = jsonObjectMain.getDouble("temp_min") - 273.15;
                    double temp_max = jsonObjectMain.getDouble("temp_max") - 273.15;
                    double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;//sensação
                    double pressure = jsonObjectMain.getDouble("pressure");//preção
                    int humidity = jsonObjectMain.getInt("humidity");//umidade
                    JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                    double wind = jsonObjectWind.getDouble("speed") * 3.6;
                    JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                    int clouds = jsonObjectClouds.getInt("all");
                    JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                    String countryName = jsonObjectSys.getString("country");
                    String cityName = jsonResponse.getString("name");
                    int sunrise = jsonObjectSys.getInt("sunrise");//nascer do sol
                    int sunset = jsonObjectSys.getInt("sunset");//PÔR do sol
                    String hora = new SimpleDateFormat("HH:mm").format(sunset);

                    Weather weather = new Weather(
                            cityName,
                            cityName + " (" + countryName + ")",
                            df.format(temp) + "°",
                            description,
                            "Máx.: " + df.format(temp_max) + "°",
                            "Mín.: " + df.format(temp_min) + "°",
                            df.format(feelsLike) + "°",
                            humidity + "%",
                            "" + df.format(wind),
                            "" + df_desc.format(pressure / 1000),
                            "" + df.format(clouds / 10), url_img + icon + "@4x.png", id);
                    weathers.add(weather);
                    weatherAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //city não encontrada
                Toast.makeText(getApplicationContext(), error.toString().trim() + "não encontrada", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue resRequestQueue = Volley.newRequestQueue(getApplicationContext());
        resRequestQueue.add(stringRequest);

    }

    private void meuLocal() {
        local.setText((String) intent.getSerializableExtra("cicade_estado"));
        temperatura.setText((String) intent.getSerializableExtra("temperatira"));
        clima.setText((String) intent.getSerializableExtra("clima"));
        tempmax.setText((String) intent.getSerializableExtra("tempmax"));
        tempmin.setText((String) intent.getSerializableExtra("tempmin"));
        Picasso.get().load((String) intent.getSerializableExtra("url_img_ico")).into(img_clima);
    }
}
