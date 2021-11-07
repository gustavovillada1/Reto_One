package com.example.reto_one.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reto_one.model.Negocio;
import com.example.reto_one.model.Publicacion;
import com.example.reto_one.model.Publicaciones;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.reto_one.R;
import com.google.gson.Gson;

import java.util.ArrayList;


public class MapsFragment extends Fragment implements OnMapReadyCallback{

    private ArrayList<Publicacion> publicaciones;
    private GoogleMap mMap;
    private Marker markerDraw;
    private LatLng ubicacion_negocio;
    private double lat,lng;
    private Button btn_como_llegar;
    private TextView tv_f_inicio,tv_f_fin;
    private CardView cv_horario_evento;
    private ImageView img_evento_negocio;

    private boolean cargandoShared;

    Animation login;


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        login= AnimationUtils.loadAnimation(getContext(),R.anim.login);

        lat = 3.342028232124455;
        lng = -76.530660280504;

        cargarDatosSharedPreferences();

        ubicacion_negocio= new LatLng(lat,lng);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                btn_como_llegar.animate().translationY(200).setDuration(100).setStartDelay(100);

                cv_horario_evento.animate().translationX(2000).setDuration(100).setStartDelay(100);
            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {


                    login= AnimationUtils.loadAnimation(getContext(),R.anim.login);
                    btn_como_llegar.animate().translationY(-200).setDuration(100).setStartDelay(200);

                    String[] horarios0y1foto2 = obtenerHorariosEvento(marker.getSnippet()+marker.getTitle());
                    tv_f_inicio.setText(horarios0y1foto2[0]);
                    tv_f_fin.setText(horarios0y1foto2[1]);
                    img_evento_negocio.setImageURI(Uri.parse(horarios0y1foto2[2]));
                    cv_horario_evento.animate().translationX(-5).setDuration(400).setStartDelay(200);

                    btn_como_llegar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&"+"origin="+lat+","+lng+""+"&destination="+marker.getPosition().latitude+","+marker.getPosition().longitude);

                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                            mapIntent.setPackage("com.google.android.apps.maps");

                            startActivity(mapIntent);
                        }
                    });
                return false;
            }
        });

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion_negocio, 15));

    }





    /**
     * Método encargado de cargar los datos del shared preferences.
     */
    private void cargarDatosSharedPreferences(){
        cargandoShared=true;

        new Thread(
                ()->{
                    try {
                        SharedPreferences preferences = requireContext().getSharedPreferences("SerializacionJSON", Context.MODE_PRIVATE);
                        String jsonPublicaciones = preferences.getString("publicaciones","");


                        if(!jsonPublicaciones.equals("")) {
                            Gson gson = new Gson();
                            Publicaciones p = gson.fromJson(jsonPublicaciones, Publicaciones.class);
                            this.publicaciones = p.getPublicaciones();

                            Thread.sleep(200);
                            getActivity().runOnUiThread(
                                    ()->{
                                        colocarMarcadores();
                                    }
                            );
                            cargandoShared=false;
                        }
                    }catch (Exception e){

                    }
                }
        ).start();
    }

    private void colocarMarcadores() {

        for(int i=0;i<this.publicaciones.size();i++){
            Publicacion p = this.publicaciones.get(i);

            LatLng ubication = new LatLng(p.getLat(), p.getLng());
            markerDraw = mMap.addMarker(new MarkerOptions()
                    .position(ubication)
                    .title(p.getNombreNegocio())
                    .snippet(p.getNombreEvento())
                    .draggable(true)
            );
        }
    }

    private String[] obtenerHorariosEvento(String nEvento_nNegocio){
        String[] horarios = {"No","No",""};
        for(int i=0;i<this.publicaciones.size();i++){
            Publicacion p = this.publicaciones.get(i);
            if((p.getNombreEvento()+p.getNombreNegocio()).equals(nEvento_nNegocio)){
                String[] hor = {p.getFechaInicio(),p.getFechaFin(),p.getFotoNegocio()};
                return  hor;
            }
        }
        return horarios;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_maps2, container, false);
        btn_como_llegar=(Button) view.findViewById(R.id.btn_como_llegar);
        tv_f_inicio = (TextView) view.findViewById(R.id.tv_f_inicio);
        tv_f_fin=(TextView) view.findViewById(R.id.tv_f_fin);
        cv_horario_evento=(CardView) view.findViewById(R.id.cv_horario_evento);
        img_evento_negocio = (ImageView) view.findViewById(R.id.img_evento_negocio);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapF2);
        mapFragment.getMapAsync(this);

    }



}