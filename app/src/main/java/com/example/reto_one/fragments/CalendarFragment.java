package com.example.reto_one.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reto_one.AdaptadorPublicacion;
import com.example.reto_one.R;
import com.example.reto_one.RegistrarNegocioActivity;
import com.example.reto_one.RegistrarPublicacionActivity;
import com.example.reto_one.model.Negocio;
import com.example.reto_one.model.Publicacion;
import com.example.reto_one.model.Publicaciones;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;


public class CalendarFragment extends Fragment {


    private CardView cv_no_publicaciones, cv_crear_publicacion;
    private RecyclerView recycler_publicaciones;
    private ImageButton btn_regresar_pub;
    private FloatingActionButton floatingActionButton;
    private ProgressBar progressBarPublicaciones;

    private ArrayList<Publicacion> publicaciones;
    private Negocio negocio;

    private boolean cargandoDatosShared ;

    public CalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), this::OnResult//Aquí vemos como colocar una función como parámetro a partir de java 8
        );

        cv_crear_publicacion=(CardView) view.findViewById(R.id.cv_crear_publicacion);
        cv_no_publicaciones=(CardView) view.findViewById(R.id.cv_no_publicaciones);
        progressBarPublicaciones=(ProgressBar) view.findViewById(R.id.progressBarPublicaciones);

        publicaciones= new ArrayList<>();
        recycler_publicaciones=(RecyclerView) view.findViewById(R.id.recycler_publicaciones);
        recycler_publicaciones.setLayoutManager(new LinearLayoutManager(getContext()));

        btn_regresar_pub=(ImageButton) view.findViewById(R.id.btn_regresar_pub);
        floatingActionButton=(FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setVisibility(View.GONE);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle= new Bundle();

                Intent agregarEvento = new Intent(CalendarFragment.this.getActivity(), RegistrarPublicacionActivity.class);

                if(negocio!=null){
                    bundle.putSerializable("negocio",negocio);
                    agregarEvento.putExtras(bundle);
                    launcher.launch(agregarEvento);

                }else {
                    Toast.makeText(getContext(), "No hay un negocio logeado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cv_crear_publicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle= new Bundle();

                Intent agregarEvento = new Intent(CalendarFragment.this.getActivity(), RegistrarPublicacionActivity.class);

                if(negocio!=null){
                    bundle.putSerializable("negocio",negocio);
                    agregarEvento.putExtras(bundle);
                    launcher.launch(agregarEvento);

                }else {
                    Toast.makeText(getContext(), "No hay un negocio logeado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cargarDatosSharedPreferencesThread();

        return view;
    }


    private void cargarDatosSharedPreferencesThread(){
        cargandoDatosShared=true;

        new Thread(
                    ()->{
                    while(cargandoDatosShared){
                        try{

                            SharedPreferences preferences = requireContext().getSharedPreferences("SerializacionJSON",Context.MODE_PRIVATE);
                            String jsonNegocioActual = preferences.getString("negocioActual","");
                            String jsonPublicaciones = preferences.getString("publicaciones","");

                            if(!jsonNegocioActual.equals("")){

                                Gson gson = new Gson();
                                this.negocio = gson.fromJson(jsonNegocioActual, Negocio.class);

                            }

                            if(!jsonPublicaciones.equals("")){
                                Gson gson =  new Gson();
                                Publicaciones p = gson.fromJson(jsonPublicaciones,Publicaciones.class);
                                this.publicaciones = p.getPublicaciones();
                                Thread.sleep(500);
                                getActivity().runOnUiThread(

                                        ()->{
                                            llenarPublicaciones();
                                            cargandoDatosShared=false;
                                        }
                                );
                            }
                        }catch (Exception e){

                            e.printStackTrace();
                        }
                    }

                }

        ).start();

    }


    /**
     * Método encargado de cargar los datos del shared preferences.
     */
    private void cargarDatosSharedPreferences(){
                            SharedPreferences preferences = requireContext().getSharedPreferences("SerializacionJSON",Context.MODE_PRIVATE);
                            String jsonNegocioActual = preferences.getString("negocioActual","");
                            String jsonPublicaciones = preferences.getString("publicaciones","");

                            if(!jsonNegocioActual.equals("")){

                                Gson gson = new Gson();
                                this.negocio = gson.fromJson(jsonNegocioActual, Negocio.class);

                            }

                            if(!jsonPublicaciones.equals("")) {
                                Gson gson = new Gson();
                                Publicaciones p = gson.fromJson(jsonPublicaciones, Publicaciones.class);
                                this.publicaciones = p.getPublicaciones();

                                llenarPublicaciones();

                            }

    }

    /**
     * Método encargado de guardar los datos de las publicaciones que hayan en ese momento en el fragment, en un shared preferences.
     */
    private void guardarDatosSharedPreferences(){
        if(!this.publicaciones.isEmpty()){
            Gson gson = new Gson();
            Publicaciones p = new Publicaciones(this.publicaciones);
            String json = gson.toJson(p);

            SharedPreferences preferences = requireContext().getSharedPreferences("SerializacionJSON", Context.MODE_PRIVATE);
            preferences.edit().putString("publicaciones",json).apply();
        }
    }

    /**
     * Método para cargar el recyclerview
     */
    private void llenarPublicaciones() {

        new Thread(
                ()->{
                    try {
                        Thread.sleep(500);
                    }catch (Exception e){

                    }
                }
        ).start();

        if(publicaciones.isEmpty()){
            cv_no_publicaciones.setVisibility(View.VISIBLE);
            cv_crear_publicacion.setVisibility(View.VISIBLE);
            progressBarPublicaciones.setVisibility(View.GONE);
            return;
        }else {
            cv_no_publicaciones.setVisibility(View.GONE);
            cv_crear_publicacion.setVisibility(View.GONE);
            floatingActionButton.setVisibility(View.VISIBLE);
            progressBarPublicaciones.setVisibility(View.GONE);
        }

        AdaptadorPublicacion adaptadorPublicacion=new AdaptadorPublicacion(publicaciones,getContext());

        adaptadorPublicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  AQUI SE METE EL CODIGO PARA CUANDO DEN CLICK EN EL ITEM
            }
        });
        recycler_publicaciones.setAdapter(adaptadorPublicacion);
    }

    /**
     * Este metodo es para recibir el callback hecho en RegistrarPublicacionActivity
     * @param result
     */
    public void OnResult(ActivityResult result){

        if(result.getResultCode() == -1){

            Publicacion p = (Publicacion) result.getData().getExtras().getSerializable("Publicacion");
            this.publicaciones.add(p);
            Toast.makeText(getContext(),publicaciones.size()+" <- tamaño", Toast.LENGTH_SHORT).show();

            llenarPublicaciones();
        }
    }

    @Override
    public void onPause() {

        guardarDatosSharedPreferences();

        super.onPause();
    }





}