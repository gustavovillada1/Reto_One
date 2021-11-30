package com.example.reto_one;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.reto_one.model.Publicacion;

import java.text.DecimalFormat;
import java.util.ArrayList;

 public class AdaptadorPublicacion extends RecyclerView.Adapter<AdaptadorPublicacion.ViewHolderPublicacion> implements View.OnClickListener{
    ArrayList<Publicacion> publicaciones;
    private  View.OnClickListener listener;
    private Context c;

    public AdaptadorPublicacion( Context c) {
        this.publicaciones = new ArrayList<>();
        this.c=c;
    }

    public void addEvento(Publicacion p){
        this.publicaciones.add(p);
        notifyItemInserted(this.publicaciones.size()-1);
    }

     public ArrayList<Publicacion> getPublicaciones() {
         return publicaciones;
     }



     @NonNull
    @Override
    public ViewHolderPublicacion onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_publicaciones,null,false);

       view.setOnClickListener(this);
        return new ViewHolderPublicacion(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPublicacion holder, int position) {

        holder.tv_list_nombre_evento.setText(publicaciones.get(position).getNombreEvento());
        holder.tv_list_nombre_negocio.setText(publicaciones.get(position).getNombreNegocio());
        holder.tv_list_ubicacion_letra.setText(publicaciones.get(position).getUbicaciónLetra());
        holder.img_list_evento.setImageURI(Uri.parse(publicaciones.get(position).getFotoNegocio()));
        holder.tv_list_fecha_inicio.setText(publicaciones.get(position).getFechaInicio().toString());
        holder.tv_list_fecha_fin.setText(publicaciones.get(position).getFechaFin().toString());

    }



    @Override
    public int getItemCount() {

        return publicaciones.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }


    @Override
    public void onClick(View v) {

        if(listener!=null){

            listener.onClick(v);
        }

    }


    public class ViewHolderPublicacion extends RecyclerView.ViewHolder{
        TextView tv_list_nombre_evento,tv_list_nombre_negocio,tv_list_ubicacion_letra,tv_list_fecha_inicio,tv_list_fecha_fin;
        ImageView img_list_evento;


        public ViewHolderPublicacion(@NonNull View itemView) {

            super(itemView);
            tv_list_nombre_evento=(TextView) itemView.findViewById(R.id.tv_list_nombre_evento);
            tv_list_nombre_negocio=(TextView) itemView.findViewById(R.id.tv_list_nombre_negocio);
            tv_list_ubicacion_letra=(TextView) itemView.findViewById(R.id.tv_list_ubicacion_letra);
            tv_list_fecha_inicio=(TextView) itemView.findViewById(R.id.tv_list_fecha_inicio);
            tv_list_fecha_fin=(TextView) itemView.findViewById(R.id.tv_list_fecha_fin);
            img_list_evento=(ImageView) itemView.findViewById(R.id.img_list_evento);


        }
    }
}
