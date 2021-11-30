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
import com.example.reto_one.MainActivity;
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
    private AdaptadorPublicacion adaptadorPublicacion;
    private ImageButton btn_regresar_pub;
    private FloatingActionButton floatingActionButton;

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

        recycler_publicaciones=(RecyclerView) view.findViewById(R.id.recycler_publicaciones);
        configurateRecycler();
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

        cargarDatosSharedPreferences();

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
                                Thread.sleep(500);

                                Gson gson =  new Gson();
                                Publicaciones p = gson.fromJson(jsonPublicaciones,Publicaciones.class);
                                 for(Publicacion publicacion: p.getPublicaciones()){
                                     adaptadorPublicacion.addEvento(publicacion);
                                 }

                            }
                            getActivity().runOnUiThread(

                                    ()->{
                                        adaptAddButtons();
                                        cargandoDatosShared=false;
                                    }
                            );
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
            for(Publicacion publicacion: p.getPublicaciones()){
                adaptadorPublicacion.addEvento(publicacion);
            }
        }

        adaptAddButtons();
    }

    /**
     * Método encargado de guardar los datos de las publicaciones que hayan en ese momento en el fragment, en un shared preferences.
     */
    private void guardarDatosSharedPreferences(){
        if(!adaptadorPublicacion.getPublicaciones().isEmpty()){
            Gson gson = new Gson();
            Publicaciones p = new Publicaciones(adaptadorPublicacion.getPublicaciones());
            String json = gson.toJson(p);

            SharedPreferences preferences = requireContext().getSharedPreferences("SerializacionJSON", Context.MODE_PRIVATE);
            preferences.edit().putString("publicaciones",json).apply();
        }
    }

    /**
     * Método para colocar visible el boton flotante si ya hay eventos agregados.
     */
    private void adaptAddButtons() {

        if(adaptadorPublicacion.getPublicaciones().isEmpty()){
            cv_no_publicaciones.setVisibility(View.VISIBLE);
            cv_crear_publicacion.setVisibility(View.VISIBLE);
            return;
        }else {
            cv_no_publicaciones.setVisibility(View.GONE);
            cv_crear_publicacion.setVisibility(View.GONE);
            floatingActionButton.setVisibility(View.VISIBLE);
        }

    }

    /**
     * Metodo encargado de configurar el recyclerview con su respectivo adapter y el evento que se realiza al darle click a un item del recycler.
     */
    public void configurateRecycler(){
        recycler_publicaciones.setLayoutManager(new LinearLayoutManager(getContext()));
        adaptadorPublicacion=new AdaptadorPublicacion(getContext());
        adaptadorPublicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapsFragment fr=new MapsFragment();
                Bundle bundle = new Bundle();
                int position = recycler_publicaciones.getChildAdapterPosition(v);
                bundle.putSerializable("Evento",adaptadorPublicacion.getPublicaciones().get(position));
                fr.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container,fr)
                        .addToBackStack(null)
                        .commit();

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
            adaptadorPublicacion.addEvento(p);
            adaptAddButtons();
        }
    }

    @Override
    public void onPause() {

        guardarDatosSharedPreferences();

        super.onPause();
    }





}