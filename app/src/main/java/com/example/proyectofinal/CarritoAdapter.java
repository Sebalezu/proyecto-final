package com.example.proyectofinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import com.example.proyectofinal.R;
import com.example.proyectofinal.CartItem;
import com.example.proyectofinal.CarritoAdapter;


import java.util.ArrayList;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CartItem> lista;
    private Runnable updateTotalCallback;

    public CarritoAdapter(Context context, ArrayList<CartItem> lista, Runnable updateTotalCallback) {
        this.context = context;
        this.lista = lista;
        this.updateTotalCallback = updateTotalCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_carrito, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {

        CartItem item = lista.get(position);

        h.txtNombre.setText(item.getProduct().getTitle());
        h.txtPrecio.setText("€ " + (item.getProduct().getPrice() / 100.0));
        h.txtCantidad.setText(String.valueOf(item.getQuantity()));

        Picasso.get().load(item.getProduct().getImageUrl()).into(h.img);

        h.btnMas.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            notifyItemChanged(position);
            updateTotalCallback.run();
        });

        h.btnMenos.setOnClickListener(v -> {
            int nuevaCantidad = item.getQuantity() - 1;

            if (nuevaCantidad <= 0) {
                CarritoManager.eliminarProducto(item.getProduct());
                notifyItemRemoved(position);
            } else {
                item.setQuantity(nuevaCantidad);
                notifyItemChanged(position);
            }

            updateTotalCallback.run();
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView txtNombre, txtPrecio, txtCantidad;
        Button btnMas, btnMenos;

        public ViewHolder(@NonNull View v) {
            super(v);

            img = v.findViewById(R.id.imgCarrito);
            txtNombre = v.findViewById(R.id.txtNombreCarrito);
            txtPrecio = v.findViewById(R.id.txtPrecioCarrito);
            txtCantidad = v.findViewById(R.id.txtCantidad);
            btnMas = v.findViewById(R.id.btnMas);
            btnMenos = v.findViewById(R.id.btnMenos);
        }
    }
}

