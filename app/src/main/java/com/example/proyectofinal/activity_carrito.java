package com.example.proyectofinal;


import android.os.Bundle;
import android.widget.Button;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.proyectofinal.CarritoAdapter;
import com.example.proyectofinal.CarritoManager;


public class activity_carrito extends AppCompatActivity {

    private RecyclerView rv;
    private TextView tvTotal;
    private Button btnComprar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        rv = findViewById(R.id.recyclerCarrito);
        tvTotal = findViewById(R.id.tvTotal);
        btnComprar = findViewById(R.id.btn5Comprar);

        rv.setLayoutManager(new LinearLayoutManager(this));

        actualizarCarrito();

        btnComprar.setOnClickListener(v -> comprar());
    }


    private void actualizarCarrito() {

        CarritoAdapter adapter = new CarritoAdapter(
                this,
                CarritoManager.getCarrito(),
                this::mostrarTotal
        );

        rv.setAdapter(adapter);
        mostrarTotal();
    }


    private void mostrarTotal() {
        double totalEuros = CarritoManager.getTotal() / 100.0;
        tvTotal.setText("Total: € " + totalEuros);
    }


    private void comprar() {

        boolean todoBien = true; // Simula proceso real

        if (todoBien) {
            CarritoManager.vaciarCarrito();
            actualizarCarrito();
            Toast.makeText(this, "Compra realizada con éxito", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Fallo en la compra", Toast.LENGTH_LONG).show();
        }
    }
}

