package com.example.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

public class activity_carrito extends AppCompatActivity {

    RecyclerView rv;
    CarritoAdapter adapter;
    TextView tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        rv = findViewById(R.id.rv5Carrito);
        tvTotal = findViewById(R.id.tvTotal);

        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CarritoAdapter(this, GlobalVars.carrito);
        rv.setAdapter(adapter);

        calcularTotal();
    }

    private void calcularTotal() {

        double total = 0;

        for (Producto p : GlobalVars.carrito) {
            total += (p.precio * p.cantidad);
        }

        tvTotal.setText("TOTAL: € " + (total / 100.0));
    }
}
