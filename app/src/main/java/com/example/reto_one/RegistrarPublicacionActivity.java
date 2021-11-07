package com.example.reto_one;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reto_one.fragments.CreateEventFragment;
import com.example.reto_one.fragments.DateDialogFragment;
import com.example.reto_one.model.Negocio;
import com.example.reto_one.model.Publicacion;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class RegistrarPublicacionActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText et_nombre_evento,et_inicio_evento,et_fin_evento;
    private ImageView img_ubicacion_evento;
    private TextView tv_sin_ubicacion;
    private Button btn_crear_evento;
    private ImageButton btn_regresar_pub;

    private Negocio negocio;

    private String location="";
    private double latitud,longitud;
    private boolean todosPermitidos;

    public static final int PERMISSION_CALLBACK = 1;
    private CreateEventFragment createEventFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_publicacion);

        et_nombre_evento = (EditText) findViewById(R.id.et_nombre_evento);
        et_inicio_evento = (EditText) findViewById(R.id.et_inicio_evento);
        et_fin_evento = (EditText) findViewById(R.id.et_fin_evento);

        img_ubicacion_evento = (ImageView) findViewById(R.id.img_ubicacion_evento);
        tv_sin_ubicacion = (TextView) findViewById(R.id.tv_sin_ubicacion);
        btn_crear_evento = (Button) findViewById(R.id.btn_crear_evento);
        btn_regresar_pub = (ImageButton) findViewById(R.id.btn_regresar_pub);

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), this::OnResult//Aquí vemos como colocar una función como parámetro a partir de java 8
        );
        capturar_negocio();

        createEventFragment = CreateEventFragment.newInstance();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, createEventFragment);
        transaction.commit();


        btn_regresar_pub.setOnClickListener(this);
        btn_crear_evento.setOnClickListener(this);
        img_ubicacion_evento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicitar_permisos();
                if(todosPermitidos){
                    Intent abrirMapa = new Intent(getApplicationContext(), UbicacionActivity.class);
                    launcher.launch(abrirMapa);
                }

            }
        });

    }



    private void solicitar_permisos() {

        requestPermissions(new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        },PERMISSION_CALLBACK);
    }

    //Este metodo comprueba si el permiso se hizo o no y se manda a cargar las fotos.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        todosPermitidos=true;

        if(requestCode == PERMISSION_CALLBACK){
            for(int r: grantResults){
                if(r == PackageManager.PERMISSION_DENIED){
                    todosPermitidos=false;
                    break;
                }
            }
        }

        if(!todosPermitidos){
            Toast.makeText(getApplicationContext(),"No todos los permisos fueron permitidos",Toast.LENGTH_SHORT).show();
        }
    }

    private void capturar_negocio() {
        Bundle negocio_activo = getIntent().getExtras();
        if(negocio_activo!=null) {
            this.negocio = (Negocio) negocio_activo.getSerializable("negocio");
        }
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_crear_evento:
                hacer_callback();
                break;


        }

    }


    /**
     * Método para enviar el callback al CaldendarFragment
     */
    private void hacer_callback() {
        Intent data = new Intent();

        String nombre_evento = et_nombre_evento.getText().toString();
        String fecha_inicio = createEventFragment.startBtn.getText().toString();
        String fecha_fin = createEventFragment.endBtn.getText().toString();



        if((fecha_fin.equals("Inicio"))||(fecha_inicio.equals("Final"))||(nombre_evento.equals(""))||(location.equals(""))){
            Toast.makeText(getApplicationContext(),"Por favor, ingresa todos los datos",Toast.LENGTH_SHORT).show();
            return;
        }

        Publicacion p= new Publicacion(nombre_evento,negocio.getNombre(),negocio.getFoto(),location,latitud,longitud,fecha_inicio,fecha_fin);

        data.putExtra("Publicacion",p);

        setResult(RESULT_OK, data);
        finish();
    }

    /**
     * Este metodo es para recibir el callback de UbicacionActivity
     * @param result
     */
    public void OnResult(ActivityResult result){

        if(result.getResultCode() == -1){
            location=result.getData().getExtras().getString("location","");
            latitud=result.getData().getExtras().getDouble("latitud");
            longitud= result.getData().getExtras().getDouble("longitud");

            tv_sin_ubicacion.setText(location);
        }
    }

/*
    private void obtenerFecha(CreateEventFragment.OnDateSelectedListener listener) {
        CreateEventFragment dialog = new CreateEventFragment();
        dialog.setListener(listener);

    }*/
}