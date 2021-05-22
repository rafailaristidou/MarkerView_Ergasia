package com.example.markerview_ergasia;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    List<Marker> list = new ArrayList<Marker>();
    String desc, type_metritis;
    double val_metr;
    Marker m;
    public static FirebaseFirestore db;//Vasi FireBase

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Vasi FireBase
        db = FirebaseFirestore.getInstance();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


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
    public void onMapReady(GoogleMap googleMap) {//Emfanisi tou xarti
        mMap = googleMap;

        // move the camera
        LatLng greece = new LatLng(35, 41);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(greece));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {//Otan o xristis pata se kapio Marker na emfanizete to popupactivity me tis plirofories tou sigkekrimenou Market apo to Firebase
            @Override
            public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {




                Marker m = getMarkerByID((marker.getId()));//Methodos p dimiourgisame gia na pernoume vasi tou id tou marker pou patisame kai na to vriskoume kai na to antistixoume me to Marker
                                                        // tis listas me ta apothikevmena Marker pou pirame apo to firebase

                desc = m.getDesc();
                type_metritis = m.getTypeMetrisis();
                val_metr = m.getValueMetrisis();
                Intent intent = new Intent(getApplicationContext(), popupactivity.class);

                // Apostoli ton stixion tou Marker apo tin lista mas sto popupactivity

                Bundle args = new Bundle();
                args.putString("desc", desc);
                args.putString("type_metritis", type_metritis);
                args.putDouble("val_metr", val_metr);
                intent.putExtras(args);
                startActivity(intent);
                return false;


            }
        });
    }


    public void ShowSaveLocation(View v) {//Piezontas to koumpi na perni oles tis egrafes me ta Marker apo to Firebase
        db.collection("MapsMarker")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Marker marker = document.toObject(Marker.class);

                                GeoPoint Location = marker.getLocation();
                                float Color = marker.getColor();
                                String TypeMetrisis = marker.getTypeMetrisis();
                                float ValueMetrisis = marker.getValueMetrisis();
                                String desc = marker.getDesc();
                                list.add(marker);                                   //topothetoume to kathe marker p pirame apo tin vasi se mia lista antikimenon Marker
                            }
                            Toast.makeText(getBaseContext(), "Markers Successful Appear.", Toast.LENGTH_LONG).show();
                            for (int i = 0; i < list.size(); i++) {                 //Gia kathe ena Marker apo tin lista ta stelnoume stin creatMarker gia na ta dimiourgisoume kai na ta emfanisoume ston xarti
                                m = list.get(i);
                                creatMarker(m);
                            }
                        } else {
                            // Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public void creatMarker(Marker m) { //Dimiourgia kai emfanisi kathe Marker stin lista

        desc = m.getDesc();
        type_metritis = m.getTypeMetrisis();
        val_metr = m.getValueMetrisis();
        GeoPoint location = m.getLocation();
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        float color = m.getColor();
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext());
            List<Address> addressesList = geocoder.getFromLocation(lat, lon, 1);
            String locality = addressesList.get(0).getLocality();
            String countryname = addressesList.get(0).getCountryName();
            String markettxt = locality + " , " + countryname;

            com.google.android.gms.maps.model.Marker x = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lon))
                    .title(markettxt)
                    .icon(BitmapDescriptorFactory.defaultMarker(color)));
            m.setMarkerID(x.getId());//Pernoume to Marker id tou Marker pou dimoiourgisame gia na to antisitxisoume stin lista me ta Markers

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public Marker getMarkerByID(String id) {//Psaxnoume stin lista me ta Marker gia na vroume to Marker p exi patisi o xristis
        for (Marker _item : list) {
            if (_item.getMarkerID().equals(id)) {
                return list.get(list.indexOf(_item));
            }

        }
        return null;
    }
}

