package br.com.geizon.wagner.wit_weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_PERMISSIONS_CODE = 128;
    FusedLocationProviderClient client;
    private MaterialDialog mMaterialDialog;

    ImageView img_clima, img_list;
    TextView local, temperatura, clima, tempmin, tempmax, sensacao, umidade, vento, presao, visibilidade, nuvens;
    LinearLayout content;
    private final String url_img = "https://openweathermap.org/img/wn/";
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "25f62380376fd074b7fbd207b575e2e2";
    private String lat = "";
    private String lon = "";
    private String cidade = "";
    private String localLista;
    private String url_img_ico;
    private int atual = 0;
    DecimalFormat df = new DecimalFormat("#");
    DecimalFormat df_desc = new DecimalFormat("#.###");
    private final ArrayList<Weather> weathers = new ArrayList<>();
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = getIntent();
        client = LocationServices.getFusedLocationProviderClient(this);
        content = findViewById(R.id.Lin_content);
        img_clima = findViewById(R.id.img_clima);
        img_list = findViewById(R.id.imgList);
        local = findViewById(R.id.txtMeuLocal);
        temperatura = findViewById(R.id.llTemperatura);
        clima = findViewById(R.id.txtClima);
        tempmax = findViewById(R.id.txtMax);
        tempmin = findViewById(R.id.txtMín);
        sensacao = findViewById(R.id.txtSensacao);
        umidade = findViewById(R.id.txtUmidade);
        vento = findViewById(R.id.txtVento);
        presao = findViewById(R.id.txtPressao);
        visibilidade = findViewById(R.id.txtVisibilidade);
        nuvens = findViewById(R.id.txtNuvens);

        img_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                localLista = null;
                atual = 1;
                alializaTempo();

            }
        });

    }

    private void alializaTempo() {
        content.setBackground(ContextCompat.getDrawable(this, R.drawable.background));

        String tempUrl;
        if (localLista != null) {
            tempUrl = url + "?q=" + localLista + "&appid=" + appid;
        } else {
            tempUrl = url + "?lat=" + lat + "&lon=" + lon + "&appid=" + appid;
            //     Toast.makeText(this, "" + localLista, Toast.LENGTH_SHORT).show();
        }
        //

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
                    int visibility = jsonResponse.getInt("visibility");
                    int sunrise = jsonObjectSys.getInt("sunrise");//nascer do sol
                    int sunset = jsonObjectSys.getInt("sunset");//PÔR do sol

                    cidade = cityName;
                    local.setText(cityName + " (" + countryName + ")");
                    temperatura.setText(df.format(temp) + "°");
                    clima.setText(description);
                    tempmax.setText("Máx.: " + df.format(temp_max) + "°");
                    tempmin.setText("Mín.: " + df.format(temp_min) + "°");
                    sensacao.setText(df.format(feelsLike) + "°");
                    umidade.setText(humidity + "%");
                    vento.setText("" + df.format(wind));
                    presao.setText("" + df_desc.format(pressure / 1000));
                    visibilidade.setText("" + df.format(visibility / 1000));
                    nuvens.setText("" + clouds + "%");
                    url_img_ico = url_img + icon + "@4x.png";

                    loadIbackgroud(id);
                    loadImagemclima(url_img + icon + "@4x.png");
                    ///pegar tempo atual no click
                    if (atual == 1) {
                        atual = 0;
                        Intent it = new Intent(getBaseContext(), ListWeather.class);
                        it.putExtra("cicade_estado", local.getText());
                        it.putExtra("cicade", cidade);
                        it.putExtra("temperatira", temperatura.getText());
                        it.putExtra("clima", clima.getText());
                        it.putExtra("tempmax", tempmax.getText());
                        it.putExtra("tempmin", tempmin.getText());
                        it.putExtra("url_img_ico", url_img_ico);
                        startActivity(it);
                    }

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

    private void location() {
        int erro = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        switch (erro) {
            case ConnectionResult.SERVICE_MISSING:
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
            case ConnectionResult.SERVICE_DISABLED:
                Log.i("Teste", "show dialog");
                GoogleApiAvailability.getInstance().getErrorDialog(this, erro, 0,
                        new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                finish();
                            }
                        }
                ).show();
                break;
            case ConnectionResult.SUCCESS:
                Log.i("Teste", "ok");
                break;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                callDialog("It is necessary to allow access to the location for the presentation of local events.", new String[]{Manifest.permission.ACCESS_FINE_LOCATION});
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_CODE);
            }
        }
        client.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            //       Log.i("teste",location.getLatitude()+" "+location.getLongitude()+" prov.-"+location.getProvider());
                            lat = "" + location.getLatitude();
                            lon = "" + location.getLongitude();

                            alializaTempo();
                        } else {
                            Log.i("teste", null);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void loadIbackgroud(int id) {
        if (id <= 699) {
            content.setBackground(ContextCompat.getDrawable(this, R.drawable.background_nublado));
        } else {
            if (id >= 700 && id <= 800) {
                content.setBackground(ContextCompat.getDrawable(this, R.drawable.background));
            } else {
                if (id >= 800) {
                    content.setBackground(ContextCompat.getDrawable(this, R.drawable.background_nuvem));
                }
            }
        }
    }

    private void loadImagemclima(String url_img) {
        Picasso.get().load(url_img).into(img_clima);
    }

    @Override
    protected void onResume() {
        super.onResume();
        localLista = (String) intent.getSerializableExtra("cicade");
        location();
    }

    // UTIL
    private void callDialog(String message, final String[] permissions) {
        mMaterialDialog = new MaterialDialog(this)
                .setTitle("Permission")
                .setMessage(message)
                .setPositiveButton("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ActivityCompat.requestPermissions(MainActivity.this, permissions, REQUEST_PERMISSIONS_CODE);
                        mMaterialDialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
        mMaterialDialog.show();
    }
}