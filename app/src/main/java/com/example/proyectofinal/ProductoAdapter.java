package com.example.proyectofinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {

    Context context;
    List<Producto> lista;

    public ProductoAdapter(Context ctx, List<Producto> data) {
        this.context = ctx;
        this.lista = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int pos) {
        Producto p = lista.get(pos);

        h.tvNombre.setText(p.nombre);
        h.tvPrecio.setText("€ " + (p.precio / 100.0));

        Picasso.get()
                .load(p.imagen)
                .into(h.img);

        h.btnAgregar.setOnClickListener(v -> GlobalVars.agregarAlCarrito(p));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView tvNombre, tvPrecio, tvTipo;
        Button btnAgregar;

        ViewHolder(View v) {
            super(v);
            img = v.findViewById(R.id.img5Producto);
            tvNombre = v.findViewById(R.id.tv5Nombre);
            tvPrecio = v.findViewById(R.id.tv5Precio);
            tvTipo = v.findViewById(R.id.tv5Tipo);
            btnAgregar = v.findViewById(R.id.btn5Agregar);
        }
    }
}
