package com.example.turkiyedepremharitasi;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ArrayList<Movie> resp;
    private TextView tarih;
    private TextView saat;
    private TextView buyukluk;
    private TextView derinlik;
    private TextView konum;

    private TextView yer;
    private int dk_zaman = 10000;
    private final ArrayList<Movie> depremArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);;


        tarih = findViewById(R.id.tarih);
        saat = findViewById(R.id.saat);
        buyukluk = findViewById(R.id.buyukluk);
        derinlik = findViewById(R.id.derinlik);
        yer = findViewById(R.id.yer);
        konum = findViewById(R.id.konum);

        CheckBox sec_saat = findViewById(R.id.sec_saat);
        CheckBox sec_gun = findViewById(R.id.sec_gun);
        CheckBox sec_hafta = findViewById(R.id.sec_hafta);
        sec_hafta.setChecked(true);

        ApiInterface apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
        Call<List<Movie>> call = apiInterface.getLastMovies();

        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                resp = (ArrayList<Movie>) response.body();
                haritaGuncelle();

            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.d("Error ----------- ", t.getMessage());
            }
        });
        sec_saat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dk_zaman = 60;
                if (sec_saat.isChecked()){
                    sec_gun.setChecked(false);
                    sec_hafta.setChecked(false);
                    haritaGuncelle();
                }
                else{
                    sec_saat.setChecked(true);
                }
            }
        });
        sec_gun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dk_zaman = 1440;
                if (sec_gun.isChecked()){
                    sec_saat.setChecked(false);
                    sec_hafta.setChecked(false);
                    haritaGuncelle();
                }
                else{
                    sec_gun.setChecked(true);
                }
            }
        });
        sec_hafta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dk_zaman = 10080;
                if (sec_hafta.isChecked()){
                    sec_saat.setChecked(false);
                    sec_gun.setChecked(false);
                    haritaGuncelle();
                }
                else{
                    sec_hafta.setChecked(true);
                }
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.9030394, 32.4825798), 4f));
        mMap.setOnMarkerClickListener(this);

    }

    private void haritaGuncelle(){
        if (depremArray!=null){
            depremArray.clear();
            mMap.clear();
        }


        if (resp != null){
            for (int i=0; i<resp.size();i++){
                String sDate1= resp.get(i).getTarih();
                String sDate2= resp.get(i).getSaat();
                @SuppressLint("SimpleDateFormat") Date date1= null;
                try {
                    date1 = new SimpleDateFormat("yyyy.MM.ddhh:mm:ss").parse(sDate1+sDate2);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.ddhh:mm:ss");

                    Date date = new Date();
                    long diff = date.getTime() - date1.getTime();
                    int diff1 = (int) diff;
                    int saniye = diff1 / 1000;
                    int dakika = saniye / 60;
                    if (dakika<=dk_zaman){
                        depremArray.add(resp.get(i));
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //depremArray.add(resp.get(i));
            }
        }
        haritayaYerlestir();
    }

    private void haritayaYerlestir(){
        LatLng sydney = new LatLng(-34, 151);
        double enlem;
        double boylam;
        String title;
        if (depremArray != null){
            for (int i=0; i<depremArray.size();i++){
                enlem = Double.parseDouble(depremArray.get(i).getEnlem());
                boylam = Double.parseDouble(depremArray.get(i).getBoylam());
                title = String.valueOf(i);
                mMap.addMarker(new MarkerOptions().position(new LatLng(enlem, boylam)).title(title));
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onMarkerClick(Marker marker) {
        int title = Integer.parseInt(marker.getTitle());
        double enlem = Double.parseDouble(depremArray.get(title).getEnlem());
        double boylam = Double.parseDouble(depremArray.get(title).getBoylam());


        yer.setText(depremArray.get(title).getYer());
        derinlik.setText(depremArray.get(title).getDerinlik()+ " KM");
        buyukluk.setText(depremArray.get(title).getSiddet());
        saat.setText(depremArray.get(title).getSaat());
        tarih.setText(depremArray.get(title).getTarih());
        konum.setText("E: "+depremArray.get(title).getEnlem() + " B: "+depremArray.get(title).getBoylam());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(enlem, boylam)));
        marker.hideInfoWindow();


        // Retrieve the data from the marker.
        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return true;
    }
}