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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.reto_one.R;
import com.example.reto_one.RegistrarNegocioActivity;
import com.example.reto_one.model.Negocio;
import com.google.gson.Gson;


public class PerfilFragment extends Fragment {

    private ImageButton btn_editar_negocio;
    private ImageView img_foto_negocio_main;
    private TextView tv_nombre_negocio_main,tv_descripcion_negocio_main,tv_ninguna_imagen;

    private Uri foto_negocio;
    private Negocio negocio;
    private boolean cargandoDatosShared ;
    private ConstraintLayout progressBarPerfil;
    private CardView cv_cerrar_sesion;

    public PerfilFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        btn_editar_negocio=(ImageButton) view.findViewById(R.id.btn_editar_negocio);
        img_foto_negocio_main=(ImageView) view.findViewById(R.id.img_foto_negocio_main);
        tv_nombre_negocio_main=(TextView) view.findViewById(R.id.tv_nombre_negocio_main);
        tv_descripcion_negocio_main=(TextView) view.findViewById(R.id.tv_descripcion_negocio_main);
        tv_ninguna_imagen=(TextView) view.findViewById(R.id.tv_ninguna_imagen);
        progressBarPerfil=(ConstraintLayout) view.findViewById(R.id.progressBarPerfil);
        cv_cerrar_sesion=(CardView) view.findViewById(R.id.cv_cerrar_sesion);
        tv_ninguna_imagen.setVisibility(View.VISIBLE);


        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new StartActivityForResult(), this::OnResult//Aquí vemos como colocar una función como parámetro a partir de java 8
        );

        btn_editar_negocio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle= new Bundle();

                Intent editarNegocio = new Intent(PerfilFragment.this.getActivity(), RegistrarNegocioActivity.class);
                if(foto_negocio!=null){
                    bundle.putString("nombre",tv_nombre_negocio_main.getText().toString());
                    bundle.putString("descripcion",tv_descripcion_negocio_main.getText().toString());
                    bundle.putString("foto", foto_negocio.toString());
                    editarNegocio.putExtras(bundle);
                }
                //startActivity(editarNegocio); //Para el callback no usamos start activity si no el launcher creado arriba
                launcher.launch(editarNegocio);
            }
        });


        cv_cerrar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Negocio negocioVacio = new Negocio("","","");
                Gson gson = new Gson();
                String json = gson.toJson(negocioVacio);

                SharedPreferences preferences = requireContext().getSharedPreferences("SerializacionJSON",Context.MODE_PRIVATE);
                preferences.edit().putString("negocioActual",json).apply();
                Toast.makeText(getContext(),"Sesión Cerrada",Toast.LENGTH_SHORT).show();
                cargarDatosSharedPreferences();
            }
        });

        cargarDatosSharedPreferences();

        return view;
    }

    /**
     * Este metodo es para obtener el callback
     * @param result
     */
    public void OnResult(ActivityResult result){

        if(result.getResultCode() == -1){
            String nombre_negocio=result.getData().getExtras().getString("nombre");
            String descripcion_negocio=result.getData().getExtras().getString("descripcion");
            String foto_negocio_string= result.getData().getExtras().getString("foto");

            this.negocio=new Negocio(nombre_negocio,descripcion_negocio,foto_negocio_string);

            foto_negocio = Uri.parse(negocio.getFoto());
            tv_nombre_negocio_main.setText(negocio.getNombre());
            tv_descripcion_negocio_main.setText(negocio.getDescripcion());
            img_foto_negocio_main.setImageURI(foto_negocio);
            tv_ninguna_imagen.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {

        if(this.negocio!=null){
            Gson gson = new Gson();
            String json = gson.toJson(this.negocio);

            SharedPreferences preferences = requireContext().getSharedPreferences("SerializacionJSON",Context.MODE_PRIVATE);
            preferences.edit().putString("negocioActual",json).apply();
        }

        super.onPause();
    }


    @Override
    public void onResume() {

        super.onResume();
    }

    private void cargarDatosSharedPreferences(){
        cargandoDatosShared=true;
        new Thread(
                ()->{
                    try {

                        while(cargandoDatosShared) {
                            SharedPreferences preferences = requireContext().getSharedPreferences("SerializacionJSON", Context.MODE_PRIVATE);
                            String json = preferences.getString("negocioActual", "");
                            if (!json.equals("")) {

                                Gson gson = new Gson();
                                this.negocio = gson.fromJson(json, Negocio.class);
                                Thread.sleep(200);

                                getActivity().runOnUiThread(
                                        () -> {

                                            if(!negocio.getFoto().equals("")){
                                                foto_negocio = Uri.parse(negocio.getFoto());
                                                tv_nombre_negocio_main.setText(negocio.getNombre());
                                                tv_descripcion_negocio_main.setText(negocio.getDescripcion());
                                                img_foto_negocio_main.setImageURI(foto_negocio);

                                            }else{
                                                tv_nombre_negocio_main.setText("Negocio por defecto");
                                                tv_descripcion_negocio_main.setText("Inserte aquí una descripción apropiada para su negocio. El autor de las publicaciones que haga el fragmento \"publicaciones\" será negocio que se registre en este fragment.");
                                                img_foto_negocio_main.setImageResource(R.drawable.ic_baseline_camera_alt_24);
                                                tv_ninguna_imagen.setVisibility(View.GONE);

                                            }
                                            progressBarPerfil.setVisibility(View.GONE);
                                            cargandoDatosShared=false;
                                        }
                                );
                            }
                        }
                    }catch (Exception e){

                    }
                }

        ).start();


    }


}