package com.edipo.uni7java;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.edipo.uni7java.remote.IOpenWeatherMap;
import com.edipo.uni7java.remote.WeatherRsp;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class WeatherInfoActivity extends AppCompatActivity {

    private static final String GPS_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String EXTRA_USERNAME = "usernameExtra";
    private static final int RC_PERMISSION = 234;

    public static Intent getIntent(Context context, String username) {
        Intent intent = new Intent(context, WeatherInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_USERNAME, username);
        return intent;
    }

    private ImageView ivWeather;
    private TextView tvCity;
    private TextView tvDesc;
    private TextView tvTemp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ivWeather = findViewById(R.id.image_weather);
        tvCity = findViewById(R.id.text_weather_city);
        tvDesc = findViewById(R.id.text_weather_desc);
        tvTemp = findViewById(R.id.text_weather_temp);
        TextView tvUserName = findViewById(R.id.text_username);
        tvUserName.setText(getIntent().getStringExtra(EXTRA_USERNAME));
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, GPS_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
            requestUserLocation();
        } else {
            requestGPSPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissions.length == 1 && TextUtils.equals(permissions[0], GPS_PERMISSION)) {
            if (ContextCompat.checkSelfPermission(this, GPS_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
                requestUserLocation();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, GPS_PERMISSION)) {
                    View rootView = findViewById(android.R.id.content);
                    Snackbar.make(rootView, R.string.weather_gps_permission_rationale, Snackbar.LENGTH_INDEFINITE)
                            .setAction(android.R.string.ok, v -> requestGPSPermission())
                            .show();
                } else {
                    Toast.makeText(this, R.string.weather_gps_permission_denied, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void requestGPSPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        ActivityCompat.requestPermissions(this, permissions, RC_PERMISSION);
    }

    @SuppressLint("MissingPermission")
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    private void requestUserLocation() {
        LocationServices.getFusedLocationProviderClient(this)
                .getLastLocation()
                .addOnSuccessListener(this, location -> {
                    requestTemperatureFromLocation(location.getLatitude(), location.getLongitude());
                    try {
                        List<Address> addresses = new Geocoder(getApplicationContext())
                                .getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addresses.size() > 0) {
                            Address address = addresses.get(0);
                            tvCity.setText(String.format(Locale.getDefault(), "%s - %s",
                                    address.getLocality(), address.getAdminArea()));
                        } else {
                            showLocationError();
                        }
                    } catch (Exception e) {
                        showLocationError();
                    }
                });
    }

    private void requestTemperatureFromLocation(double lat, double lon) {
        new Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl("https://api.openweathermap.org/")
                .build()
                .create(IOpenWeatherMap.class)
                .getWeather(lat, lon, "pt", "metric", BuildConfig.WEATHER_APPID)
                .enqueue(new Callback<WeatherRsp>() {
                    @Override
                    public void onResponse(Call<WeatherRsp> call, Response<WeatherRsp> response) {
                        WeatherRsp rsp = response.body();
                        if (response.isSuccessful() && rsp != null) {
                            tvTemp.setText(getString(R.string.weather_temperature, rsp.main.temp));
                            if (rsp.weather.size() > 0) {
                                tvDesc.setText(rsp.weather.get(0).description);
                                Glide.with(WeatherInfoActivity.this)
                                        .load(String.format("http://openweathermap.org/img/w/%s.png",
                                                rsp.weather.get(0).icon))
                                        .into(ivWeather);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.error_weather, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherRsp> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), R.string.error_weather, Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                    }
                });
    }

    private void showLocationError() {
        Toast.makeText(this, R.string.error_location, Toast.LENGTH_SHORT).show();
    }

}
