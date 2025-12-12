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

    private List<Producto> productos;
    private Context context;

    public ProductoAdapter(Context context, List<Producto> productos) {
        this.context = context;
        this.productos = productos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Producto p = productos.get(position);

        holder.txtNombre.setText(p.getTitle());
        holder.txtPrecio.setText("€ " + (p.getPrice() / 100.0));

        Picasso.get()
                .load(p.getImageUrl())
                .into(holder.imgProducto);

        holder.btnAgregar.setOnClickListener(v -> {
            if (!GlobalVars.carrito.containsKey(p.getId())) {
                p.setQuantityInCart(1);
                GlobalVars.carrito.put(p.getId(), p);
            } else {
                Producto existente = GlobalVars.carrito.get(p.getId());
                existente.setQuantityInCart(existente.getQuantityInCart() + 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombre, txtPrecio;
        ImageView imgProducto;
        Button btnAgregar;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.tv5Nombre);
            txtPrecio = itemView.findViewById(R.id.tv5Precio);
            imgProducto = itemView.findViewById(R.id.img5Producto);
            btnAgregar = itemView.findViewById(R.id.btn5Agregar);
        }
    }
}
