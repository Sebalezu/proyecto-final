package com.example.proyectofinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.ViewHolder> {

    private ArrayList<Producto> listaCarrito;
    private Context context;

    public CarritoAdapter(Context context, ArrayList<Producto> listaCarrito) {
        this.context = context;
        this.listaCarrito = listaCarrito;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_carrito, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Producto p = listaCarrito.get(position);

        holder.txtNombre.setText(p.getTitle());
        holder.txtPrecio.setText("€ " + (p.getPrice() / 100.0));
        holder.txtCantidad.setText("x" + p.getQuantityInCart());

        Picasso.get()
                .load(p.getImageUrl())
                .into(holder.imgProducto);
    }

    @Override
    public int getItemCount() {
        return listaCarrito.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombre, txtPrecio, txtCantidad;
        ImageView imgProducto;

        public ViewHolder(View itemView) {
            super(itemView);

            txtNombre = itemView.findViewById(R.id.tv6CarNombre);
            txtPrecio = itemView.findViewById(R.id.tv6CarPrecio);
            txtCantidad = itemView.findViewById(R.id.tv6Cantidad);
            imgProducto = itemView.findViewById(R.id.img6CarItem);
        }
    }
}
