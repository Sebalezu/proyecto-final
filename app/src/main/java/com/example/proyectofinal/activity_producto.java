package com.example.proyectofinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.ArrayList;

public class activity_producto extends AppCompatActivity {

    private RecyclerView rvProductos;
    private EditText txtBuscarPorNombre;      // ← EditText en vez de Spinner
    private Button btnBuscar, btnCarrito;

    private ArrayList<Product> lista = new ArrayList<>();
    private ProductoAdapter adapter;
    private RequestQueue queue;

    private static final String URL_API =
            "https://api.apify.com/v2/datasets/y4wGLhpyhg3UCHN6Q/items?token=apify_api_AOu38iajyUbMHSuyBIQRu6fiywHY042pb8wG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);

        rvProductos = findViewById(R.id.rvProductos);
        txtBuscarPorNombre = findViewById(R.id.txt5Buscarpornombre);   // ← EditText
        btnBuscar = findViewById(R.id.btnBuscarProducto);
        btnCarrito = findViewById(R.id.btnCarrito);

        rvProductos.setLayoutManager(new LinearLayoutManager(this));

        queue = Volley.newRequestQueue(this);

        cargarProductos();

        // ABRIR CARRITO
        btnCarrito.setOnClickListener(v -> {
            Intent intent = new Intent(activity_producto.this, activity_carrito.class);
            startActivity(intent);
        });

        // 🔍 BOTÓN BUSCAR
        btnBuscar.setOnClickListener(v -> filtrarPorNombre());
    }


    private void cargarProductos() {

        JsonArrayRequest req = new JsonArrayRequest(
                Request.Method.GET,
                URL_API,
                null,
                response -> procesarRespuesta(response),
                error -> {}
        );

        queue.add(req);
    }


    private void procesarRespuesta(JSONArray response) {

        lista.clear();

        try {
            for (int i = 0; i < response.length(); i++) {

                var obj = response.getJSONObject(i);

                String id = obj.getString("id");
                String title = obj.getString("title");
                int price = obj.getJSONObject("prices")
                        .getJSONObject("price")
                        .getInt("amount");
                String imagen = obj.getString("imageUrl");

                Product p = new Product(id, title, price, imagen);
                lista.add(p);
            }

            adapter = new ProductoAdapter(this, lista);
            rvProductos.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 🔍 FILTRAR POR NOMBRE USANDO EDITTEXT — 100% corregido
    private void filtrarPorNombre() {

        String texto = txtBuscarPorNombre.getText().toString().trim().toLowerCase();

        ArrayList<Product> filtrados = new ArrayList<>();

        for (Product p : lista) {
            if (p != null && p.getTitle() != null) {
                if (p.getTitle().toLowerCase().contains(texto)) {
                    filtrados.add(p);
                }
            }
        }

        adapter = new ProductoAdapter(this, filtrados);
        rvProductos.setAdapter(adapter);
    }
}

