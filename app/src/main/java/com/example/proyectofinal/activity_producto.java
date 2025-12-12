package com.example.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class activity_producto extends AppCompatActivity {

    RecyclerView rv;
    ProductoAdapter adapter;
    ArrayList<Producto> lista = new ArrayList<>();

    String URL = "https://api.apify.com/v2/datasets/y4wGLhpyhg3UCHN6Q/items?token=apify_api_AOu38iajyUbMHSuyBIQRu6fiywHY042pb8wG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);

        rv = findViewById(R.id.rvProductos);
        rv.setLayoutManager(new LinearLayoutManager(this));

        Button btnCarrito = findViewById(R.id.btnCarrito);
        btnCarrito.setOnClickListener(v ->
                startActivity(new Intent(this, activity_carrito.class)));

        cargarDatos();
    }

    private void cargarDatos() {

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, URL, null,
                response -> {
                    lista.clear();
                    parsearProductos(response);
                },
                error -> {}
        );

        Volley.newRequestQueue(this).add(req);
    }

    private void parsearProductos(JSONArray arr) {
        try {
            for (int i = 0; i < arr.length(); i++) {

                JSONObject o = arr.getJSONObject(i);

                String id = o.getString("id");
                String nombre = o.getString("title");

                String img = o.getString("imageUrl");

                double precio = o.getJSONObject("prices")
                        .getJSONObject("price")
                        .getDouble("amount");

                lista.add(new Producto(id, nombre, img, precio));
            }

            adapter = new ProductoAdapter(this, lista);
            rv.setAdapter(adapter);

        } catch (Exception e) { e.printStackTrace(); }
    }
}
