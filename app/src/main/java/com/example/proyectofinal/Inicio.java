package com.example.proyectofinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Inicio extends AppCompatActivity {

    // Declarar variables
    private Button btnUbicacionD1, btnUbicacionJusto, btnUbicacionExito;
    private LinearLayout layoutD1, layoutJusto, layoutExito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);

        // Inicializar botones GPS
        btnUbicacionD1 = findViewById(R.id.btn_ubicacion_d1);
        btnUbicacionJusto = findViewById(R.id.btn_ubicacion_justo);
        btnUbicacionExito = findViewById(R.id.btn_ubicacion_exito);

        // Inicializar layouts clickeables (para cuando se haga click en la imagen/tienda)
        layoutD1 = findViewById(R.id.layout_d1);
        layoutJusto = findViewById(R.id.layout_justo);
        layoutExito = findViewById(R.id.layout_exito);

        // ===== BOTONES DE UBICACIÓN GPS =====

        // Botón GPS D1
        btnUbicacionD1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirMapa("D1");
            }
        });

        // Botón GPS Justo & Bueno
        btnUbicacionJusto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirMapa("Justo & Bueno");
            }
        });

        // Botón GPS Éxito
        btnUbicacionExito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirMapa("Éxito");
            }
        });

        // ===== CLICK EN IMAGEN/LAYOUT DE TIENDA (para catálogo) =====

        // Click en tienda D1
        layoutD1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCatalogo("D1");
            }
        });

        // Click en tienda Justo & Bueno
        layoutJusto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCatalogo("Justo & Bueno");
            }
        });

        // Click en tienda Éxito
        layoutExito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCatalogo("Éxito");
            }
        });
    }

    // ===== MÉTODOS PARA ABRIR OTRAS PANTALLAS =====

    /**
     * Método para abrir la pantalla del mapa con la ubicación de la tienda
     * @param nombreTienda Nombre de la tienda seleccionada
     */
    private void abrirMapa(String nombreTienda) {
        Intent intent = new Intent(this, mapa.class);
        intent.putExtra("tienda", nombreTienda);
        startActivity(intent);
    }

    /**
     * Método para abrir el catálogo de productos de la tienda
     * @param nombreTienda Nombre de la tienda seleccionada
     */
    private void abrirCatalogo(String nombreTienda) {
        // Por ahora mostramos un Toast (mensaje temporal)
        // Cuando tu compañero cree la Activity del catálogo, actualiza esto

        Toast.makeText(this, "Abriendo catálogo de " + nombreTienda, Toast.LENGTH_SHORT).show();

        // TODO: Cuando tu compañero tenga su Activity lista, descomentar esto:
        /*
        Intent intent = new Intent(this, CatalogoActivity.class);
        intent.putExtra("tienda", nombreTienda);
        startActivity(intent);
        */
    }
}