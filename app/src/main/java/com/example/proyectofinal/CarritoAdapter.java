package com.example.proyectofinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.ViewHolder> {

    Context ctx;
    List<Producto> data;

    public CarritoAdapter(Context c, List<Producto> d) {
        ctx = c;
        data = d;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_carrito, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int pos) {

        Producto p = data.get(pos);

        h.tvNombre.setText(p.nombre);
        h.tvPrecio.setText("€ " + (p.precio / 100.0));
        h.tvCantidad.setText(String.valueOf(p.cantidad));

        Picasso.get().load(p.imagen).into(h.img);
    }

    @Override
    public int getItemCount() { return data.size(); }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView tvNombre, tvPrecio, tvCantidad;

        public ViewHolder(View v) {
            super(v);
            img = v.findViewById(R.id.img6CarItem);
            tvNombre = v.findViewById(R.id.tv6CarNombre);
            tvPrecio = v.findViewById(R.id.tv6CarPrecio);
            tvCantidad = v.findViewById(R.id.tv6Cantidad);
        }
    }
}

