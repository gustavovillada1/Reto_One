package com.example.reto_one;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class RegistrarNegocioActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btn_regresar;
    private Button btn_editar_informacion;
    private EditText et_nombre_negocio,et_descripcion_negocio;
    private ImageView img_foto_negocio;

    private ActivityResultLauncher<Intent> camera_launch;//Los launchers son para recibir una respuesta
    private ActivityResultLauncher<Intent> gallery_launch;//Los launchers son para recibir una respuesta
    private File file;

    private Uri uriFoto;

    public static final int PERMISSION_CALLBACK = 1;
    private int image_resource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_negocio);

        image_resource=0;
        camera_launch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::onCameraResult);
        gallery_launch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::onGalleryResult);

        btn_regresar=(ImageButton) findViewById(R.id.btn_regresar);
        btn_editar_informacion=(Button) findViewById(R.id.btn_editar_informacion);
        et_nombre_negocio=(EditText) findViewById(R.id.et_nombre_negocio);
        et_descripcion_negocio=(EditText) findViewById(R.id.et_descripcion_negocio);
        img_foto_negocio=(ImageView) findViewById(R.id.img_foto_negocio);

        verificar_negocio_existente();

        btn_regresar.setOnClickListener(this);
        btn_editar_informacion.setOnClickListener(this);
        img_foto_negocio.setOnClickListener(this);
    }

    private void verificar_negocio_existente() {
        Bundle negocio_activo = getIntent().getExtras();
        if(negocio_activo!=null) {
            String nombre = negocio_activo.getString("nombre", "");
            String descripcion = negocio_activo.getString("descripcion", "");
            String foto = negocio_activo.getString("foto", "");
            if(!foto.equals("")){

                Uri foto_uri=Uri.parse(foto);
                this.uriFoto = foto_uri;
                img_foto_negocio.setImageURI(foto_uri);
                Toast.makeText(getApplicationContext(),"Uri"+uriFoto.toString(),Toast.LENGTH_SHORT).show();
                et_nombre_negocio.setText(nombre);
                et_descripcion_negocio.setText(descripcion);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_regresar:
                finish();
                break;

            case R.id.btn_editar_informacion:

                hacer_callback();

                break;

            case R.id.img_foto_negocio:

                cargar_foto();
                break;


        }
    }

    private void cargar_foto() {

        requestPermissions(new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
        },PERMISSION_CALLBACK);
    }

    //Este metodo comprueba si el permiso se hizo o no y se manda a cargar las fotos.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean todosPermitidos=true;

        if(requestCode == PERMISSION_CALLBACK){
            for(int r: grantResults){
                if(r == PackageManager.PERMISSION_DENIED){
                    todosPermitidos=false;
                    break;
                }
            }
        }
        
        if(todosPermitidos){
            choose_gallery_camera();
        }else{
            Toast.makeText(getApplicationContext(),"No todos los permisos fueron permitidos",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Este método lanza un cuadro de alerta para que el usuario elija si quiere abrir la galería o usar la cámara.
     */
    private void choose_gallery_camera(){

        final CharSequence[] opciones={"Galería","Cámara","Cancelar"};
        final androidx.appcompat.app.AlertDialog.Builder alertOpciones= new androidx.appcompat.app.AlertDialog.Builder(RegistrarNegocioActivity.this);

        alertOpciones.setTitle("¿Cómo quieres subir una imágen?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                if(opciones[i].equals("Galería")){
                    open_galley();
                }else if(opciones[i].equals("Cámara")) {
                    capturar_foto();
                }else{
                    dialog.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    /**
     * Método para abrir la cámara
     */
    private void capturar_foto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file =  new File(getExternalFilesDir(null)+"/photo"+System.currentTimeMillis()+".png");

        Uri uri = FileProvider.getUriForFile(this, getPackageName(), file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        camera_launch.launch(intent);
    }

    /**
     * Método para abrir la galería
     */
    private void open_galley(){
        Intent intent=new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//Cuando se coloca ACTION_PICK sale una excepción, y con OPEN_DOCUMENT no.
        intent.setType("image/*");
        gallery_launch.launch(intent);
    }

    /**
     * Método para capturar cuando se toma una foto con la camera
     * @param result
     */
    private void onCameraResult(ActivityResult result) {
        if(result.getResultCode() == RESULT_OK){
           //Foto completa
            uriFoto = Uri.fromFile(this.file);
            Bitmap bitmap2= BitmapFactory.decodeFile(this.file.getPath());
            Bitmap bitmapSmall = Bitmap.createScaledBitmap(bitmap2,bitmap2.getWidth()/4,bitmap2.getHeight()/4,true);
            img_foto_negocio.setImageBitmap(bitmapSmall);
        }else{
            Toast.makeText(getApplicationContext(),"Operación cancelada",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Método para capturar cuando se selecciona una imágen de la galería
     * @param result
     */
    private void onGalleryResult(ActivityResult result) {
        if(result.getResultCode() == RESULT_OK){
            Uri uri=result.getData().getData();
            this.uriFoto=uri;
            img_foto_negocio.setImageURI(uri);

        }else{
            Toast.makeText(getApplicationContext(),"Operación cancelada",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Este método es para validar que esté la información completa para editar el perfil.
     */
    private void hacer_callback() {
        Intent data = new Intent();

        if((et_descripcion_negocio.getText().toString().equals(""))||(et_nombre_negocio.getText().toString().equals(""))||(uriFoto==null)){
            Toast.makeText(getApplicationContext(),"Por favor, ingresa todos los datos",Toast.LENGTH_SHORT).show();
            return;
        }
        data.putExtra("nombre",et_nombre_negocio.getText().toString());
        data.putExtra("descripcion",et_descripcion_negocio.getText().toString());
        data.putExtra("foto",uriFoto.toString());

        setResult(RESULT_OK, data);
        finish();
    }


}